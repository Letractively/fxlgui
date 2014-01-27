/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.fxl.gui.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

class VerticalLayoutManager implements LayoutManager {

	final static int CENTER = 0;
	final static int RIGHT = 1;
	final static int LEFT = 2;
	final static int BOTH = 3;
	final static int TOP = 1;
	final static int BOTTOM = 2;
	int vgap = 0;
	int alignment = LEFT;
	int anchor = TOP;
	Insets insets = new Insets(0, 0, 0, 0);
	boolean stretch = true;
	int width = -1;
	int height = -1;

	VerticalLayoutManager() {
	}

	VerticalLayoutManager(boolean stretch) {
		this.stretch = stretch;
	}

	private Dimension layoutSize(Container parent) {
		Dimension dim = new Dimension(0, 0);
		synchronized (parent.getTreeLock()) {
			int n = parent.getComponentCount();
			for (int i = 0; i < n; i++) {
				Component c = parent.getComponent(i);
				if (c.isVisible()) {
					Dimension d = c.getPreferredSize();
					dim.width = Math.max(width(dim), width(d));
					dim.height += d.height;
					if (i > 0)
						dim.height += vgap;
				}
			}
		}
		if (height != -1)
			dim.height = Math.max(height, dim.height);
		dim.width += insets.left + insets.right;
		dim.height += insets.top + insets.bottom;
		return dim;
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			int n = parent.getComponentCount();
			Dimension pd = parent.getSize();
			int y = insets.top + getAnchorOffset(parent);
			for (int i = 0; i < n; i++) {
				Component c = parent.getComponent(i);
				Dimension d = c.getPreferredSize();
				int x = insets.left;
				int wid;
				int max = width(pd) - insets.left - insets.right;
				if (stretch) {
					wid = max;
				} else {
					wid = width(d);
					switch (alignment) {
					case CENTER:
						x += (max - wid) / 2;
						break;
					case RIGHT:
						x += max - wid - insets.right;
						break;
					default:
						break;
					}
				}
				c.setBounds(x, y, wid, d.height);
				y += d.height + vgap;
			}
		}
	}

	private int width(Dimension pd) {
		if (width != -1)
			return width;
		return pd.width;
	}

	private int getAnchorOffset(Container parent) {
		if (anchor == TOP)
			return 0;
		if (anchor == CENTER)
			return anchorOffset(parent) / 2;
		return anchorOffset(parent);
	}

	private int anchorOffset(Container parent) {
		int n = parent.getComponentCount();
		Dimension pd = parent.getSize();
		int h = pd.height - insets.top - insets.bottom;
		if (parent.getComponentCount() != 0)
			h -= (parent.getComponentCount() - 1) * vgap;
		for (int i = 0; i < n; i++) {
			Component c = parent.getComponent(i);
			Dimension d = c.getPreferredSize();
			h -= d.getHeight();
		}
		return h;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return layoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return layoutSize(parent);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}

	@Override
	public String toString() {
		return getClass().getName() + "[vgap=" + vgap + " align=" + alignment
				+ " anchor=" + anchor + "]";
	}
}
