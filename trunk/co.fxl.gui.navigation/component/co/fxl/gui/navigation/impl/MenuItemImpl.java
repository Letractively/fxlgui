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
package co.fxl.gui.navigation.impl;

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.navigation.api.IMenuItem;
import co.fxl.gui.navigation.api.IToolbarItem;
import co.fxl.gui.register.api.IRegister;
import co.fxl.gui.register.api.IRegister.IRegisterListener;
import co.fxl.gui.register.impl.RegisterWidgetImpl;

class MenuItemImpl implements IMenuItem, IRegisterListener {

	private IRegister register;
	private RegisterStyle style;
	private RegisterWidgetImpl registerWidget;
	private RegisterStyle styleChild;
	private INavigationListener listener;

	MenuItemImpl(RegisterWidgetImpl registerWidget, RegisterStyle style) {
		register = registerWidget.addRegister();
		this.style = style;
		style.init(register.title());
		register.listener(this);
	}

	@Override
	public IMenuItem active() {
		register.top();
		return this;
	}

	@Override
	public IMenuItem listener(INavigationListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public IVerticalPanel contentPanel() {
		return register.contentPanel();
	}

	@Override
	public IMenuItem visible(boolean visible) {
		register.visible(visible);
		return this;
	}

	@Override
	public IMenuItem addNavigationItem() {
		RegisterWidgetImpl registerWidget = registerWidget();
		return new MenuItemImpl(registerWidget, styleChild);
	}

	private RegisterWidgetImpl registerWidget() {
		if (registerWidget == null) {
			registerWidget = new RegisterWidgetImpl(contentPanel().layout());
			styleChild = style.child();
			styleChild.decorateWidget(registerWidget);
		}
		return registerWidget;
	}

	@Override
	public void onTop(final boolean visible, final ICallback<Void> cb) {
		listener.onActive(visible, new CallbackTemplate<Void>(cb) {

			@Override
			public void onSuccess(Void result) {
				if (visible) {
					style.onFront(register.title());
				} else {
					style.onBack(register.title());
				}
				cb.onSuccess(result);
			}
		});
	}

	@Override
	public IToolbarItem toolbarItem() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IMenuItem text(String name) {
		register.title().text(name);
		return this;
	}

	@Override
	public IMenuItem enabled(boolean enabled) {
		register.enabled(enabled);
		return this;
	}

	@Override
	public boolean isActive() {
		return register.isActive();
	}

	@Override
	public boolean enabled() {
		return register.enabled();
	}

	@Override
	public IMenuItem imageResource(String imageResource) {
		register.imageResource(imageResource);
		return this;
	}

	@Override
	public IMenuItem toggleLoading(boolean loading) {
		register.toggleLoading(loading);
		return this;
	}
}
