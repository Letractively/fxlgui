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
package co.fxl.gui.panel.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDraggable.IDragStartListener;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.panel.api.IFlexHorizontalPanel;

class FlexHorizontalPanelImpl implements IFlexHorizontalPanel<Object> {

	private class Child implements IDragStartListener, IDropListener {

		private Object iD;
		private IFocusPanel childPanel;
		private IContainer container;
		private String dragID;

		private Child(Object iD) {
			this.iD = iD;
			childPanel = panel.add().panel().focus();
			childPanel.addDragStartListener(this);
			childPanel.addDropListener(this);
			container = childPanel.add();
			dragID = String.valueOf(nextID++);
		}

		@Override
		public void onDragStart(IDragStartEvent event) {
			event.iD(dragID);
			event.dragImage(childPanel);
		}

		@Override
		public void onDragEnd() {
		}

		@Override
		public void onDropOn(IDragEvent point) {
			String dragID = point.iD();
			Child droppedOn = null;
			for (Child c : children) {
				if (c.dragID.equals(dragID)) {
					droppedOn = c;
					break;
				}
			}
			if (droppedOn == null)
				return;
			flip(this, droppedOn);
		}
	}

	private IHorizontalPanel panel;
	private List<Child> children = new LinkedList<Child>();
	private static long nextID = 0;

	FlexHorizontalPanelImpl(IContainer container) {
		panel = container.panel().horizontal();
	}

	protected void flip(Child dragged, Child droppedOn) {
		if (dragged == droppedOn)
			return;
		int index = children.indexOf(droppedOn);
		children.remove(dragged);
		if (children.indexOf(droppedOn) < index && index >= children.size())
			children.add(dragged);
		else
			children.add(index, dragged);
		for (Child c : children) {
			c.childPanel.remove();
		}
		for (Child c : children) {
			panel.add(c.childPanel);
		}
	}

	@Override
	public IFlexHorizontalPanel<Object> addSpace(int space) {
		panel.addSpace(space);
		return this;
	}

	@Override
	public IContainer add() {
		return panel.add();
	}

	@Override
	public IContainer add(Object iD) {
		Child c = new Child(iD);
		children.add(c);
		return c.container;
	}

	@Override
	public IFlexHorizontalPanel<Object> addPanel(Object iD) {
		return new FlexHorizontalPanelImpl(add(iD));
	}

	@Override
	public List<Object> order() {
		List<Object> iDs = new LinkedList<Object>();
		for (Child c : children)
			iDs.add(c.iD);
		return iDs;
	}
}
