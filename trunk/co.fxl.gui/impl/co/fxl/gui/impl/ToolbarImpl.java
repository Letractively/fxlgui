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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;

public class ToolbarImpl implements IToolbar {

	private static int SPACING = 4;
	private IFlowPanel panel;
	private ToolbarImpl parent;
	private List<Object> content = new LinkedList<Object>();
	private boolean hasContent = false;

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
		IHorizontalPanel childPanel = root.panel.add().panel().horizontal()
				.height(40).align().center();
			childPanel.spacing().left(root.hasContent ? 0 : SPACING)
					.top(0).bottom(0).right(SPACING);
		childPanel = childPanel.add().panel().horizontal().align().center();
		content.add(childPanel);
		root.hasContent = true;
		return childPanel.add();
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

}
