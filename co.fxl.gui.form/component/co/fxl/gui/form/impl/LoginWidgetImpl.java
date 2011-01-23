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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.ITextField.ICarriageReturnListener;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.form.api.ILoginWidget;

class LoginWidgetImpl implements ILoginWidget {

	class LoginListener extends LazyClickListener implements
			ICarriageReturnListener,
			co.fxl.gui.api.IPasswordField.ICarriageReturnListener {

		@Override
		public void onAllowedClick() {
			if (loginID.text().equals("")) {
				dialog("No ID specified");
				return;
			}
			if (password.text().equals("")) {
				dialog("No password specified");
				return;
			}
			listener.authorize(loginID.text(), password.text(), new Callback() {

				@Override
				public void onAuthorization(Authorization authorization) {
					if (authorization == Authorization.FAILED) {
						dialog("Unknown user or password");
					} else {
						loggedInAs.text(loginID.text());
						loginID.text("");
						password.text("");
						logoutPanel.visible(true);
						loginPanel.visible(false);
					}
				}
			});
		}

		@Override
		public void onCarriageReturn() {
			onClick();
		}
	}

	class LogoutListener extends LazyClickListener {

		@Override
		public void onAllowedClick() {
			listener.logout();
			loginPanel.visible(true);
			logoutPanel.visible(false);
		}
	}

	private IHorizontalPanel cards;
	private IHorizontalPanel loginPanel;
	private IAuthorizationListener listener;
	private ITextField loginID;
	private IPasswordField password;
	private LoginListener loginListener = new LoginListener();
	private LogoutListener logoutListener = new LogoutListener();
	private IHorizontalPanel logoutPanel;
	private ILabel loggedInAs;

	LoginWidgetImpl(IContainer display) {
		cards = display.panel().horizontal().align().end();
		loginPanel = cards.add().panel().horizontal();
		logoutPanel = cards.add().panel().horizontal();
		loginPanel.visible(true);
		logoutPanel.visible(false);
	}

	void dialog(String string) {
		IDialog dialog = loginPanel.display().showDialog();
		dialog.title("Login Failed");
		dialog.type().error().message(string);
	}

	@Override
	public ILoginWidget listener(IAuthorizationListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public ILoginWidget visible(boolean visible) {
		if (loginID == null) {
			addLogin();
			addLogout();
			loginPanel.visible(true);
			logoutPanel.visible(false);
		}
		return this;
	}

	private void addLogin() {
		IHorizontalPanel liPanel = loginPanel.add().panel().horizontal()
				.spacing(4);
		decorate(liPanel.add().label().text("ID"));
		decorate(loginID = liPanel.add().textField()
				.addCarriageReturnListener(loginListener));
		decorate(liPanel.add().label().text("Password"));
		decorate(password = liPanel.add().passwordField()
				.addCarriageReturnListener(loginListener));
		ILabel label = liPanel.add().label().text("Login").hyperlink()
				.addClickListener(loginListener).mouseLeft();
		decorateHyperlink(label);
	}

	private void addLogout() {
		ILabel label;
		IHorizontalPanel loPanel = logoutPanel.add().panel().horizontal()
				.spacing(4);
		ILabel loggedInHead = loPanel.add().label().text("Logged in as");
		decorate(loggedInHead).font().color().lightgray();
		loggedInAs = loPanel.add().label();
		decorate(loggedInAs).font().weight().bold().color().white();
		label = loPanel.add().label().text("Logout").hyperlink()
				.addClickListener(logoutListener).mouseLeft();
		decorateHyperlink(label);
	}

	private ILabel decorate(ILabel label) {
		label.font().pixel(13).color().white();
		return label;
	}

	private void decorateHyperlink(ILabel label) {
		label.font().pixel(13).color().mix().blue().white().white().white();
	}

	private void decorate(ITextField formField) {
		formField.font().pixel(11);
		formField.width(100);
		formField.border().color().lightgray();
	}

	private void decorate(IPasswordField formField) {
		formField.font().pixel(11);
		formField.width(100);
		formField.border().color().lightgray();
	}

	@Override
	public ILoginWidget login(String user, String pwd) {
		loginID.text(user);
		password.text(pwd);
		loginListener.onClick();
		return this;
	}
}