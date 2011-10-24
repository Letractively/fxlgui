/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;

public class ToolbarImpl implements IToolbar {

	public static boolean ADJUST_HEIGHTS = false;
	private static int SPACING = 4;
	private IFlowPanel panel;
	private ToolbarImpl parent;
	private Map<Object, IHorizontalPanel> blockers = new HashMap<Object, IHorizontalPanel>();
	private List<Object> content = new LinkedList<Object>();
	private IHorizontalPanel blocker;
	private Map<Object, IVerticalPanel> mainPanels = new HashMap<Object, IVerticalPanel>();
	private boolean hasContent = false;
	private int spacing = SPACING;

	public ToolbarImpl(IContainer container) {
		panel = container.panel().flow();
	}

	ToolbarImpl root() {
		if (parent != null)
			return parent.root();
		return this;
	}

	private ToolbarImpl(ToolbarImpl parent) {
		this.parent = parent;
	}

	@Override
	public IContainer add() {
		ToolbarImpl root = root();
		if (ADJUST_HEIGHTS) {
			IVerticalPanel childPanel0 = root.panel.add().panel().vertical();
			// .align().center();
			blocker = childPanel0.add().panel().horizontal();
			blocker.add().label().text("&#160;");
			IPanel<?> childPanel = childPanel0.add().panel().horizontal()
					.spacing(SPACING / 2).add().panel().horizontal().align()
					.center();
			blockers.put(childPanel, blocker);
			mainPanels.put(childPanel, childPanel0);
			content.add(childPanel);
			return childPanel.add();
		} else {
			IHorizontalPanel childPanel = root.panel.add().panel().horizontal()
					.height(40).align().center();
			childPanel.spacing().left(root.hasContent ? 0 : spacing)
					.top(spacing).bottom(spacing).right(spacing);
			childPanel = childPanel.add().panel().horizontal().align().center();
			content.add(childPanel);
			return childPanel.add();
		}
	}

	@Override
	public IToolbar addGroup() {
		ToolbarImpl toolbar = new ToolbarImpl(this);
		content.add(toolbar);
		return toolbar;
	}

	@Override
	public void clear() {
		for (Object o : content) {
			if (o instanceof ToolbarImpl) {
				ToolbarImpl toolbar = (ToolbarImpl) o;
				toolbar.clear();
			} else {
				IHorizontalPanel childPanel = (IHorizontalPanel) o;
				childPanel.remove();
			}
		}
		content.clear();
	}

	@Override
	public IToolbar visible(boolean visible) {
		for (Object o : content) {
			if (o instanceof ToolbarImpl) {
				ToolbarImpl toolbar = (ToolbarImpl) o;
				toolbar.visible(visible);
			} else {
				IHorizontalPanel childPanel = (IHorizontalPanel) o;
				childPanel.visible(visible);
			}
		}
		return this;
	}

	@Override
	public IToolbar adjustHeights() {
		if (ADJUST_HEIGHTS)
		for (Object o : content) {
			if (o instanceof ToolbarImpl) {
				ToolbarImpl toolbar = (ToolbarImpl) o;
				toolbar.adjustHeights();
			} else {
				IVerticalPanel childPanel = (IVerticalPanel) mainPanels.get(o);
				if (childPanel.height() < 24) {
					IHorizontalPanel blocker = (IHorizontalPanel) blockers
							.get(o);
					int inc = (24 - childPanel.height()) / 2;
					blocker.height(inc);
				}
			}
		}
		return this;
	}

}
