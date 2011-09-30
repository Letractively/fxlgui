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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;

class FilterDialog {

	// TODO ARCHITECTURE: Decomposition: extract layout, move as much as
	// possible into component, only leave minimum of glue code in aspect

	static void addButton(final ScrollTableWidgetImpl widget) {
		if (widget.hasFilter() || widget.viewComboBoxText != null) {
			widget.topPanelCell(0, 0).clear().panel().horizontal().addSpace(8)
					.add().label().text("Filter >>").hyperlink()
					.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							final IPopUp popUp = widget.topPanel.display()
									.showPopUp().center();
							popUp.border().style().shadow();
							widget.filter = (IFilterWidget) popUp.container()
									.widget(IFilterWidget.class);
							widget.filter
									.addCancelListener(new IClickListener() {
										@Override
										public void onClick() {
											popUp.visible(false);
										}
									});
							widget.filter
									.addFilterListener(new IFilterListener() {
										@Override
										public void onApply(
												IFilterConstraints constraints) {
											popUp.visible(false);
										}
									});
							widget.setUpFilter();
							popUp.visible(true);
						}
					}).mouseLeft();
		}
	}
}
