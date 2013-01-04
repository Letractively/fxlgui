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

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.Display;

class SwingPopUp implements IPopUp, ComponentParent {

	private static final int _12 = 0;
	private SwingDisplay panel;
	private Popup dialog;
	private int x = 0;
	private int y = 0;
	private boolean center = false;
	protected JComponent component;
	private int w = -1;
	private int h = -1;
	private boolean autoHide = false;
	JPanel p;
	private boolean fitInScreen;
	private List<IUpdateListener<Boolean>> visibleListeners = new LinkedList<IUpdateListener<Boolean>>();
	private boolean visible;

	SwingPopUp(SwingDisplay panel) {
		this.panel = panel;
		p = new JPanel();
		p.getInsets().set(0, 0, 0, 0);
		p.setBackground(Color.WHITE);
	}

	@Override
	public IPopUp offset(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public IContainer container() {
		return new SwingContainer<JComponent>(this);
	}

	@Override
	public void add(JComponent component) {
		SwingPopUp.this.component = component;
	}

	@Override
	public void remove(JComponent component) {
		dialog.hide();
	}

	@Override
	public JComponent getComponent() {
		return SwingPopUp.this.component;
	}

	@Override
	public ComponentParent getParent() {
		return null;
	}

	@Override
	public IPopUp visible(boolean visible) {
		if (autoHide) {
			if (SwingDisplay.popUp() != null && SwingDisplay.popUp() != this) {
				SwingDisplay.popUp().visible(false);
				SwingDisplay.popUp(null);
			}
			SwingDisplay.popUp(this);
		}
		if (dialog == null)
			if (component != null) {
				if (w > 0)
					component.getPreferredSize().width = w - _12;
				if (h > 0)
					component.getPreferredSize().height = h;
				PopupFactory factory = PopupFactory.getSharedInstance();
				p.add(component);
				if (center) {
					x = panel.width() / 2
							- Math.max(80, p.getPreferredSize().width / 2);
					y = panel.height() / 2
							- Math.max(40, p.getPreferredSize().height / 2);
				}
				if (fitInScreen) {
					if (y + 20 + p.getPreferredSize().height > Display
							.instance().height()) {
						y = Display.instance().height() - 30
								- p.getPreferredSize().height;
					}
					if (x + p.getPreferredSize().width > SwingDisplay
							.instance().width()) {
						y = SwingDisplay.instance().width() - 10
								- p.getPreferredSize().width;
					}
				}
				dialog = factory.getPopup(panel.frame, p, x, y + 20);
			}
		assert dialog != null;
		if (visible) {
			dialog.show();
		} else {
			dialog.hide();
			SwingDisplay.popUp(null);
		}
		notifyVisible(visible);
		this.visible = visible;
		return this;
	}

	private void notifyVisible(boolean visible) {
		for (IUpdateListener<Boolean> l : visibleListeners)
			l.onUpdate(visible);
	}

	@Override
	public IPopUp modal(boolean modal) {
		// TODO SWING-FXL: IMPL: ...
		return this;
	}

	@Override
	public IPopUp center() {
		this.center = true;
		return this;
	}

	@Override
	public int offsetX() {
		return x;
	}

	@Override
	public int offsetY() {
		return y;
	}

	@Override
	public int width() {
		if (w != -1)
			return w;
		return component.getWidth();
	}

	@Override
	public int height() {
		if (h != -1)
			return h;
		return component.getHeight();
	}

	@Override
	public IPopUp size(int w, int h) {
		this.w = w;
		this.h = h;
		if (component != null)
			component.setPreferredSize(new Dimension(w - _12, h));
		return this;
	}

	@Override
	public IPopUp autoHide(boolean autoHide) {
		this.autoHide = autoHide;
		return this;
	}

	@Override
	public IBorder border() {
		return new SwingBorder(p);
	}

	@Override
	public IPopUp width(int width) {
		if (component != null)
			component.getPreferredSize().width = width - _12;
		else {
			this.w = width;
		}
		return this;
	}

	@Override
	public IPopUp height(int height) {
		if (component != null)
			component.getPreferredSize().height = height;
		else {
			this.h = height;
		}
		return this;
	}

	@Override
	public IPopUp atLastClick(int offsetX, int offsetY) {
		offset(SwingDisplay.lastClickX + offsetX, SwingDisplay.lastClickY
				+ offsetY);
		return this;
	}

	@Override
	public IPopUp fitInScreen(boolean fitInScreen) {
		this.fitInScreen = fitInScreen;
		return this;
	}

	@Override
	public IPopUp addVisibleListener(IUpdateListener<Boolean> l) {
		visibleListeners.add(l);
		return this;
	}

	@Override
	public boolean visible() {
		return visible;
	}

	@Override
	public IPopUp glass(boolean glass) {
		// TODO SWING-FXL: IMPL: ...
		return this;
	}

	@Override
	public IPopUp opacity(double opacity) {
		// TODO SWING-FXL: IMPL: ...
		return this;
	}

	@Override
	public IPopUp width(double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPopUp height(double height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IColor color() {
		return new SwingColor() {

			@Override
			protected void setColor(Color color) {
				// TODO ... SWING-FXL: ...
			}
		};
	}

	@Override
	public IPopUp atLastClick() {
		return atLastClick(0, 0);
	}
}
