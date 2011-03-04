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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.template.KeyTemplate;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class GWTGridPanelClickHandler extends KeyTemplate<IGridPanel> implements
		ClickHandler {

	private IGridClickListener clickListener;

	public GWTGridPanelClickHandler(GWTGridPanel element,
			IGridClickListener clickListener) {
		super(element);
		this.clickListener = clickListener;
		Grid grid = (Grid) element.container.widget;
		grid.addClickHandler(this);
		grid.addStyleName("cursor-pointer");
	}

	@Override
	public void onClick(ClickEvent event) {
		for (KeyType pressedKey : pressedKeys.keySet()) {
			Boolean check = pressedKeys.get(pressedKey);
			boolean keyMatches = keyMatches(pressedKey, event.getNativeEvent());
			if (keyMatches != check)
				return;
		}
		if (!buttonMatches(event))
			return;
		event.preventDefault();
		event.stopPropagation();
		@SuppressWarnings("unchecked")
		Grid grid = (Grid) ((GWTElement<Grid, IGridPanel>) element).container.widget;
		Cell cell = grid.getCellForEvent(event);
		GWTDisplay d = (GWTDisplay) ((IElement<?>) element).display();
		if (d.waiting)
			return;
		clickListener.onClick(cell.getCellIndex(), cell.getRowIndex());
	}

	private boolean buttonMatches(ClickEvent event) {
		if (buttonType == ButtonType.LEFT)
			return true;
		else
			throw new MethodNotImplementedException();
	}

	boolean keyMatches(KeyType key, NativeEvent nativeEvent) {
		switch (key) {
		case SHIFT_KEY:
			return nativeEvent.getShiftKey();
		case CTRL_KEY:
			return nativeEvent.getCtrlKey();
		default:
			return nativeEvent.getAltKey();
		}
	}
}