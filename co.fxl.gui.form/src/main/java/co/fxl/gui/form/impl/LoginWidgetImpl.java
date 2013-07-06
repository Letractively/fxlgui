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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.ILoginWidget;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Dialog;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.UserPanel;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.style.impl.Style;

public class LoginWidgetImpl implements ILoginWidget {

	private static final int MAX_LENGTH_USER_NAME = 20;
	// private static final boolean IGNORE_EMPTY_PASSWORD = true;
	private static int HEIGHT_DECREMENT = Constants.get(
			"LoginWidgetImpl.HEIGHT_DECREMENT", 0);
	private IAuthorizationListener listener;
	private String userText;
	private Decorator[] decorators;

	// private String passwordText;

	LoginWidgetImpl(IContainer display) {
		UserPanel.instance().container(display);
	}

	void dialog(String string) {
		IDialog dialog = Dialog.newInstance();
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
		UserPanel.instance().add(new Decorator() {
			@Override
			public void decorate(IPanel<?> panel) {
				if (userText == null)
					addLogin(panel);
				else
					addLogout((ILinearPanel<?>) panel);
			}

			@Override
			public boolean isVisible() {
				return true;
			}

			// @Override
			// public void onClick() {
			// throw new UnsupportedOperationException();
			// }
		}).weight(-100);
		return this;
	}

	private void addLogin(IPanel<?> panel) {
		decorate(panel.add().label().text("ID"));
		final ITextField loginID = panel.add().textField();
		decorate(loginID);
		ILabel text = panel.add().label().text("Password");
		decorate(text);
		final IPasswordField password = panel.add().passwordField();
		decorate(password);
		ILabel loginLabel = panel.add().label().text("Login");
		hyperlink(loginLabel);
		final LazyClickListener loginListener = new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				if (loginID.text().equals("")) {
					dialog("No ID specified");
					return;
				}
				// if (!IGNORE_EMPTY_PASSWORD && password.text().equals("")) {
				// dialog("No password specified");
				// return;
				// }
				listener.authorize(loginID.text(), password.text(),
						new Callback() {

							@Override
							public void onAuthorization(
									Authorization authorization) {
								if (authorization == Authorization.FAILED) {
									dialog("Unknown user or password or account has been deactivated.");
								} else {
									userText = loginID.text();
									// passwordText = password.text();
									UserPanel.instance().update();
								}
							}
						});
			}
		};
		password.addKeyListener(loginListener).enter();
		loginLabel.addClickListener(loginListener).mouseLeft();
		loginID.addKeyListener(loginListener).enter().focus(true);
	}

	private void addLogout(ILinearPanel<?> panel) {
		IClickable<?> prefix = addLogInPrefix(panel);
		ILabel loggedInHead = panel
				.add()
				.label()
				.text(userText.length() < MAX_LENGTH_USER_NAME ? userText
						: userText.substring(0, MAX_LENGTH_USER_NAME - 3)
								+ "...");
		loggedInHead.font().weight().bold();
		decorate(loggedInHead);
		if (Style.ENABLED && Style.instance().login().useMore()) {
			loggedInHead.font().pixel(12);
			new HyperlinkMouseOverListener(loggedInHead);
			final IImage image = panel.add().image()
					.resource(Style.instance().login().moreImage());
			IClickListener clickListener = new IClickListener() {

				@Override
				public void onClick() {
					final IPopUp popUp = PopUp.showPopUp(true).width(140)
							.autoHide(true);
					popUp.offset(image.offsetX() - 140 + 12,
							image.offsetY() + 22);
					IVerticalPanel p = popUp.container().panel().vertical()
							.spacing(8);// .addSpace(2);
					p.margin().top(2);
					boolean addLine = false;
					for (Decorator d : decorators) {
						if (d.isVisible()) {
							d.decorate(p.add().panel().horizontal());
							addLine = true;
						}
					}
					if (addLine)
						p.add().line();
					ImageButton ib = new ImageButton(p.add());
					ib.imageResource("logout.png").text("Logout")
							.addClickListener(logoutListener());
					ib.label().hyperlink();
					popUp.visible(true);
				}
			};
			prefix.addClickListener(clickListener);
			loggedInHead.addClickListener(clickListener);
			image.addClickListener(clickListener);
			return;
		}
		ILabel text = panel.add().label().text("Logout");
		hyperlink(text);
		text.addClickListener(logoutListener()).mouseLeft();
	}

	LazyClickListener logoutListener() {
		return new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				userText = null;
				// passwordText = null;
				listener.logout(DummyCallback.voidInstance());
				// UserPanel.instance().update();
			}
		};
	}

	IClickable<?> addLogInPrefix(ILinearPanel<?> panel) {
		if (Style.ENABLED) {
			return Style.instance().login().addDecoration(panel);
		}
		ILabel loggedInAs = panel.add().label().text("Logged in as");
		decorate(loggedInAs);
		return loggedInAs;
	}

	void hyperlink(ILabel text) {
		text.hyperlink();
	}

	public static void decorate(ILabel label) {
		if (Style.ENABLED) {
			Style.instance().login().label(label);
			return;
		}
		label.font().color().mix().gray().black();
	}

	public static void decorate(ITextField formField) {
		formField.font().pixel(11);
		formField.width(100);
		new Heights(HEIGHT_DECREMENT).decorateHeight(formField);
	}

	public static void decorate(IPasswordField formField) {
		formField.font().pixel(11);
		formField.width(100);
		formField.border().color().lightgray();
		new Heights(HEIGHT_DECREMENT).decorateHeight(formField);
	}

	@Override
	public ILoginWidget login(String user, String pwd) {
		this.userText = user;
		// passwordText = pwd;
		UserPanel.instance().update();
		return this;
	}

	@Override
	public ILoginWidget preset(String user, String pwd) {
		this.userText = user;
		// passwordText = pwd;
		return this;
	}

	@Override
	public ILoginWidget decorators(Decorator[] ds) {
		decorators = ds;
		return this;
	}
}