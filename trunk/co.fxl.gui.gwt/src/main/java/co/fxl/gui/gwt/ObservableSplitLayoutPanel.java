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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

class ObservableSplitLayoutPanel extends SplitLayoutPanel {

	GWTSplitPane owner;

	@Override
	public void onResize() {
		super.onResize();
		Widget left = getWidget(0);
		owner.splitPosition = left.getElement().getParentElement()
				.getOffsetWidth();
		left.setWidth(owner.splitPosition + "px");
		owner.onResize(owner.splitPosition);
	}

	void updatePosition() {
		if (getWidgetCount() == 0)
			return;
		Widget left = getWidget(0);
		LayoutData layout = (LayoutData) left.getLayoutData();
		layout.oldSize = layout.size;
		layout.size = owner.splitPosition;
		animate(500);
		left.getElement().getParentElement().getStyle()
				.setWidth(owner.splitPosition, Unit.PX);
		left.setWidth(owner.splitPosition + "px");
	}
}