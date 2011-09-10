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
package co.fxl.gui.layout.handheld;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDialog.IDialogButton;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.impl.LoginWidgetImpl;
import co.fxl.gui.layout.api.ILayout.ILogin;

class HandheldLogin implements ILogin, IClickListener, IUpdateListener<String> {

	private IDialog dialog;
	private IClickListener loginListener;
	private ITextField loginID;
	private IPasswordField password;
	protected IDialogButton ok;
	private ITextField idField;
	private IPasswordField passwordField;

	@Override
	public ILogin loginListener(IClickListener l) {
		loginListener = l;
		return this;
	}

	@Override
	public ILogin loginPanel(IHorizontalPanel p) {
		p.visible(false);
		return this;
	}

	@Override
	public ILogin loggedInAs(ILabel label) {
		label.visible(false);
		return this;
	}

	@Override
	public ILogin label(ILabel label) {
		label.visible(false);
		return this;
	}

	@Override
	public ILogin panel(final IHorizontalPanel liPanel) {
		liPanel.add().label().text("Login").hyperlink()
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						dialog = liPanel.display().showDialog().title("LOGIN");
						ok = dialog.addButton().ok()
								.addClickListener(new IClickListener() {
									@Override
									public void onClick() {
										onClick();
									}
								});
						ok.clickable(false);
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
						addLoginFields(p);
						dialog.visible(true);
					}
				}).mouseLeft();
		return this;
	}

	protected void addLoginFields(ILinearPanel<?> liPanel) {
		LoginWidgetImpl.decorate(liPanel.add().label().text("ID"));
		loginID = liPanel.add().textField().addKeyListener(this).enter()
				.focus(true);
		LoginWidgetImpl.decorate(loginID);
		LoginWidgetImpl.decorate(liPanel.add().label().text("Password"));
		LoginWidgetImpl.decorate(password = liPanel.add().passwordField()
				.addKeyListener(this).enter());
		loginID.addUpdateListener(this);
		password.addUpdateListener(this);
	}

	@Override
	public void onClick() {
		dialog.visible(false);
		idField.text(loginID.text());
		passwordField.text(password.text());
		loginListener.onClick();
	}

	@Override
	public void onUpdate(String value) {
		ok.clickable(isDefined(loginID) && isDefined(password));
	}

	private boolean isDefined(ITextElement<?> i) {
		return i.text().trim().length() > 0;
	}

	@Override
	public ILogin id(ITextField id) {
		this.idField = id;
		return this;
	}

	@Override
	public ILogin password(IPasswordField password) {
		this.passwordField = password;
		return this;
	}
}
