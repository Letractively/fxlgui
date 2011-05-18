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
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.form.api.ILoginWidget;

class LoginWidgetImpl implements ILoginWidget {

	class LoginListener extends LazyClickListener {

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
						addLogout();
						loggedInAs.text(loginID.text());
						loginID.text("");
						password.text("");
					}
				}
			});
		}
	}

	class LogoutListener extends LazyClickListener {

		@Override
		public void onAllowedClick() {
			listener.logout();
			addLogin();
		}
	}

	private IHorizontalPanel cards;
	private IAuthorizationListener listener;
	private ITextField loginID;
	private IPasswordField password;
	private LoginListener loginListener = new LoginListener();
	private LogoutListener logoutListener = new LogoutListener();
	private ILabel loggedInAs;
	private IStatusPanelDecorator decorator;

	LoginWidgetImpl(IContainer display) {
		cards = display.panel().horizontal().align().end();
	}

	void dialog(String string) {
		IDialog dialog = cards.display().showDialog();
		dialog.title("Login Failed");
		dialog.message(string).error();
		dialog.visible(true);
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
		}
		return this;
	}

	private void addLogin() {
		cards.clear();
		IHorizontalPanel liPanel = cards.add().panel().horizontal().spacing(4);
		if (decorator != null)
			decorator.decorateBegin(liPanel, false);
		decorate(liPanel.add().label().text("ID"));
		decorate(loginID = liPanel.add().textField()
				.addCarriageReturnListener(loginListener).focus(true));
		decorate(liPanel.add().label().text("Password"));
		decorate(password = liPanel.add().passwordField()
				.addCarriageReturnListener(loginListener));
		liPanel.add().label().text("Login").hyperlink()
				.addClickListener(loginListener).mouseLeft();
		// decorateHyperlink(label);
		if (decorator != null)
			decorator.decorateEnd(liPanel, false);
	}

	private void addLogout() {
		cards.clear();
		IHorizontalPanel loPanel = cards.add().panel().horizontal().spacing(4);
		if (decorator != null)
			decorator.decorateBegin(loPanel, true);
		ILabel loggedInHead = loPanel.add().label().text("Logged in as");
		decorate(loggedInHead).font().color().gray();
		loggedInAs = loPanel.add().label();
		decorate(loggedInAs).font().weight().bold().color().mix().black()
				.gray();
		loPanel.add().label().text("Logout").hyperlink()
				.addClickListener(logoutListener).mouseLeft();
		// decorateHyperlink(label);
		if (decorator != null)
			decorator.decorateEnd(loPanel, true);
	}

	private ILabel decorate(ILabel label) {
		label.font().color().mix().gray().black();
		return label;
	}

	// private void decorateHyperlink(ILabel label) {
	// label.font().color().rgb(0, 87, 141);
	// }

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

	@Override
	public ILoginWidget statusPanelDecorator(IStatusPanelDecorator decorator) {
		this.decorator = decorator;
		return this;
	}
}