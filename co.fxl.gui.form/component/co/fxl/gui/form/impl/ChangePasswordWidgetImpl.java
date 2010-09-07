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
package co.fxl.gui.form.impl;

import java.util.LinkedList;
import java.util.List;


import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IPasswordField.ICarriageReturnListener;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.api.IChangePasswordWidget;
import co.fxl.gui.form.api.IFormWidget;

public class ChangePasswordWidgetImpl implements IChangePasswordWidget,
		IClickListener, IUpdateListener<String>, ICarriageReturnListener {

	protected IFormWidget widget;
	private IPasswordField currentPassword;
	private IPasswordField newPassword;
	private IPasswordField confirmPassword;
	private String currentPasswordText;
	private List<IPasswordListener> listeners = new LinkedList<IPasswordListener>();
	private IVerticalPanel verticalPanel;
	private boolean isFirstTitle = true;

	public ChangePasswordWidgetImpl(ILayout display) {
		verticalPanel = display.vertical();
		verticalPanel.stretch(true);
		widget = (IFormWidget) verticalPanel.add().widget(IFormWidget.class);
	}

	@Override
	public IChangePasswordWidget current(String currentPassword) {
		currentPasswordText = currentPassword;
		return this;
	}

	@Override
	public IChangePasswordWidget visible(boolean visible) {
		currentPassword = widget.addPasswordField("Current Password")
				.valueElement();
		currentPassword.addUpdateListener((IUpdateListener<String>) this);
		currentPassword
				.addCarriageReturnListener((ICarriageReturnListener) this);
		newPassword = widget.addPasswordField("New Password").valueElement();
		newPassword.addUpdateListener((IUpdateListener<String>) this);
		newPassword.addCarriageReturnListener((ICarriageReturnListener) this);
		confirmPassword = widget.addPasswordField("Confirm New Password")
				.valueElement();
		confirmPassword.addUpdateListener((IUpdateListener<String>) this);
		confirmPassword
				.addCarriageReturnListener((ICarriageReturnListener) this);
		ILabel oKButton = widget.addOKHyperlink();
		oKButton.text("Change Password");
		oKButton.addClickListener(this);
		widget.visible(true);
		return this;
	}

	@Override
	public void onClick() {
		if (currentPassword.text().equals("") && newPassword.text().equals("")
				&& confirmPassword.text().equals(""))
			return;
		boolean oK = true;
		if (currentPassword.text().equals("")) {
			showDialog("Field 'Current Password' is empty.");
			oK = false;
		}
		if (!currentPassword.text().equals(currentPasswordText)) {
			showDialog("Field 'Current Password' is incorrectly specified.");
			oK = false;
		}
		String newPasswordText = newPassword.text();
		if (newPasswordText.equals("")) {
			showDialog("Field 'New Password' is empty.");
			oK = false;
		}
		if (confirmPassword.text().equals("")) {
			showDialog("Field 'Confirm Password' is empty.");
			oK = false;
		}
		if (!newPasswordText.equals(confirmPassword.text())) {
			showDialog("Fields 'New Password' and 'Confirm Password' don't match.");
			oK = false;
		}
		if (!oK)
			return;
		clear();
		for (IPasswordListener l : listeners)
			l.onChange(newPasswordText);
		current(newPasswordText);
	}

	private void showDialog(String string) {
		verticalPanel.display().showDialog().title("Change Password Error")
				.type().error().message(string);
	}

	private void clear() {
		currentPassword.text("");
		newPassword.text("");
		confirmPassword.text("");
		onUpdate(null);
	}

	@Override
	public void onCarriageReturn() {
		onUpdate(null);
	}

	public void visible() {
		widget.visible(true);
	}

	@Override
	public IChangePasswordWidget addListener(IPasswordListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public void onUpdate(String input) {
	}

	@Override
	public ILabel addTitle(String title) {
		if (isFirstTitle)
			title = title + ".";
		ILabel label = widget.addTitle(title);
		if (isFirstTitle)
			label.font().color().gray();
		isFirstTitle = false;
		return label;
	}
}
