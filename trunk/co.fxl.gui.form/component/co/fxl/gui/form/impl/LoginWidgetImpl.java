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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.form.api.ILoginWidget;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.LazyClickListener;

public class LoginWidgetImpl implements ILoginWidget {

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
	ITextField loginID;
	private IPasswordField password;
	IClickListener loginListener = new LoginListener();
	private LogoutListener logoutListener = new LogoutListener();
	private ILabel loggedInAs;
	private IStatusPanelDecorator decorator;
	private ILabel loginLabel;
	private IHorizontalPanel pPanel;

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
		addLoginForm(liPanel);
		// decorateHyperlink(label);
		if (decorator != null)
			decorator.decorateEnd(liPanel, false);
	}

	private void addLoginForm(IHorizontalPanel liPanel) {
		addLoginFields(liPanel);
		loginLabel = liPanel.add().label().text("Login");
		hyperlink(loginLabel);
		loginLabel.addClickListener(loginListener).mouseLeft();
	}

	// TODO extract class LoginPanel
	void addLoginFields(IHorizontalPanel liPanel) {
		pPanel = liPanel.add().panel().horizontal();
		decorate(pPanel.add().label().text("ID"));
		decorate(loginID = pPanel.add().textField()
				.addKeyListener(loginListener).enter().focus(true));
		decorate(pPanel.add().label().text("Password"));
		decorate(password = pPanel.add().passwordField()
				.addKeyListener(loginListener).enter());
	}

	private void addLogout() {
		cards.clear();
		IHorizontalPanel loPanel = cards.add().panel().horizontal().spacing(4);
		if (decorator != null)
			decorator.decorateBegin(loPanel, true);
		loggedInAs = loPanel.add().label().text("Logged in as");
		decorate(loggedInAs);
		ILabel loggedInHead = loPanel.add().label().text(loginID.text());
		decorate(loggedInHead).font().weight().bold();
		ILabel text = loPanel.add().label().text("Logout");
		hyperlink(text);
		text.addClickListener(logoutListener).mouseLeft();
		// decorateHyperlink(label);
		if (decorator != null)
			decorator.decorateEnd(loPanel, true);
	}

	void hyperlink(ILabel text) {
		text.hyperlink();
	}

	public static ILabel decorate(ILabel label) {
		label.font().color().mix().gray().black();
		return label;
	}

	// private void decorateHyperlink(ILabel label) {
	// label.font().color().rgb(0, 87, 141);
	// }

	public static void decorate(ITextField formField) {
		formField.font().pixel(11);
		formField.width(100);
		new Heights(0).decorateHeight(formField);
	}

	public static void decorate(IPasswordField formField) {
		formField.font().pixel(11);
		formField.width(100);
		formField.border().color().lightgray();
		new Heights(0).decorateHeight(formField);
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