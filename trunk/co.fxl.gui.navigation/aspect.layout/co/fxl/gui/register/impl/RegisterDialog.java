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
package co.fxl.gui.register.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.register.api.IRegister;
import co.fxl.gui.register.api.IRegisterWidget;

public class RegisterDialog {

	public static void addButton(final RegisterWidgetImpl widget) {
		final ILabel label = widget.headerPanel.add().label().text("More >>")
				.hyperlink().addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						final IDialog dialog = widget.headerPanel.display()
								.showDialog().title("SELECT REGISTER");
						dialog.addButton().cancel()
								.addClickListener(new IClickListener() {
									@Override
									public void onClick() {
										dialog.visible(false);
									}
								});
						IVerticalPanel p = dialog.container().panel()
								.vertical().spacing(6).add().panel().vertical()
								.spacing(4);
						for (final RegisterImpl register : widget.registers) {
							if (!register.disabled)
								p.add().label()
										.text(register.buttonLabel.text())
										.hyperlink()
										.addClickListener(new IClickListener() {

											@Override
											public void onClick() {
												dialog.visible(false);
												register.onAllowedClick();
											}
										}).mouseLeft().font().pixel(13);
						}
						dialog.visible(true);
					}
				}).mouseLeft();
		widget.addRegisterListener(new IRegisterWidget.IRegisterListener() {
			@Override
			public void onTop(IRegister register) {
				RegisterImpl item = (RegisterImpl) register;
				boolean hasMore = false;
				for (RegisterImpl widgetRegister : widget.registers) {
					widgetRegister.visible(widgetRegister == item);
					if (widgetRegister != item) {
						if (!widgetRegister.disabled)
							hasMore = true;
					}
				}
				label.clickable(hasMore);
			}
		});
	}
}
