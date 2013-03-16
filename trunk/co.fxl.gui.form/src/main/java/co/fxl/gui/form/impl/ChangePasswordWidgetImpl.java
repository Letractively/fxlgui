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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.api.IChangePasswordWidget;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;

class ChangePasswordWidgetImpl implements IChangePasswordWidget, IClickListener {

	protected IFormWidget widget;
	private IPasswordField currentPassword;
	private IPasswordField newPassword;
	private IPasswordField confirmPassword;
	// private String currentPasswordText;
	private List<IPasswordListener> listeners = new LinkedList<IPasswordListener>();
	private IContainer display;
	private boolean requiresCurrent = true;

	public ChangePasswordWidgetImpl(IContainer display) {
		this.display = display;
		widget = (IFormWidget) display.widget(IFormWidget.class);
		widget.saveListener("Submit", new SaveListenerTemplate() {

			@Override
			public void save(boolean isAndBack, ICallback<Boolean> cb) {
				onClick();
				cb.onSuccess(true);
			}

			@Override
			public void cancel(boolean isAndBack, ICallback<Boolean> cb) {
				for (IPasswordListener pl : listeners)
					pl.onCancel();
				cb.onSuccess(false);
			}

			@Override
			public boolean allowsCancel() {
				return true;
			}
		});
		widget.alwaysAllowCancel();
		widget.isNew(false);
	}

	// @Override
	// public IChangePasswordWidget current(String currentPassword) {
	// currentPasswordText = currentPassword != null ? currentPassword : "";
	// return this;
	// }

	@Override
	public IChangePasswordWidget visible(boolean visible) {
		if (requiresCurrent) {
			IFormField<IPasswordField, String> pw = widget
					.addPasswordField("Current");
			currentPassword = pw.valueElement();
			pw.required();
		}
		newPassword = widget
				.addPasswordField(requiresCurrent ? "New" : "New Password")
				.required().valueElement();
		confirmPassword = widget.addPasswordField("Confirm").required()
				.valueElement();
		widget.visible(true);
		return this;
	}

	@Override
	public void onClick() {
		if (newPassword.text().equals("") && confirmPassword.text().equals(""))
			return;
		boolean oK = true;
		// if (currentPassword.text().equals("")) {
		// showDialog("Field 'Current Password' is empty.");
		// oK = false;
		// }
		// if (oK && !currentPassword.text().equals(currentPasswordText)) {
		// showDialog("Field 'Current Password' is incorrectly specified.");
		// oK = false;
		// }
		String newPasswordText = newPassword.text();
		if (oK && newPasswordText.equals("")) {
			showDialog("Field 'New Password' is empty.");
			oK = false;
		}
		if (oK && confirmPassword.text().equals("")) {
			showDialog("Field 'Confirm Password' is empty.");
			oK = false;
		}
		if (oK && !newPasswordText.equals(confirmPassword.text())) {
			showDialog("Fields 'New Password' and 'Confirm Password' don't match.");
			oK = false;
		}
		if (!oK)
			return;
		// clear();
		for (IPasswordListener l : listeners)
			l.onChange(currentPassword != null ? currentPassword.text() : null,
					newPasswordText);
		// current(newPasswordText);
	}

	private void showDialog(String string) {
		display.display().showDialog().title("Change Password Error")
				.message(string).error().visible(true)
				.addVisibleListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						if (!value) {
							clear();
							widget.notifyUpdate();
						}
					}
				});
	}

	private void clear() {
		currentPassword.text("");
		newPassword.text("");
		confirmPassword.text("");
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
	public ILabel addTitle(String title) {
		ILabel label = widget.addTitle(title);
		return label;
	}

	@Override
	public IChangePasswordWidget requiresCurrent(boolean requiresCurrent) {
		this.requiresCurrent = requiresCurrent;
		return this;
	}

	@Override
	public IChangePasswordWidget addLargeTitle(String string) {
		widget.addLargeTitle(string);
		return this;
	}
}
