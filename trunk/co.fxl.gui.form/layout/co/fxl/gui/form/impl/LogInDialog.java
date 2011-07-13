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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;

public class LogInDialog {

	public static IHorizontalPanel addButton(final LoginWidgetImpl widget,
			final IPanel<?> liPanel) {
		IHorizontalPanel hPanel = liPanel.add().panel().horizontal()
				.visible(false);
		liPanel.add().label().text("Login").hyperlink()
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						final IDialog dialog = liPanel.display().showDialog()
								.title("LOGIN");
						final IClickListener originalListener = widget.loginListener;
						widget.loginListener = new IClickListener() {

							@Override
							public void onClick() {
								dialog.visible(false);
								originalListener.onClick();
							}
						};
						dialog.addButton().ok()
								.addClickListener(new IClickListener() {
									@Override
									public void onClick() {
										widget.loginListener.onClick();
									}
								});
						dialog.addButton().cancel()
								.addClickListener(new IClickListener() {
									@Override
									public void onClick() {
										dialog.visible(false);
									}
								});
						ILinearPanel<?> p = dialog.container().panel()
								.vertical().spacing(6).add().panel().vertical()
								.spacing(4);
						widget.addLoginFields(p);
						dialog.visible(true);
						widget.loginID.focus(true);
					}
				}).mouseLeft();
		return hPanel;
	}
}
