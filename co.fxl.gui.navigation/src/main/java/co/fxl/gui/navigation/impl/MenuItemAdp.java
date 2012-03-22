///**
// * This file is part of FXL GUI API.
// *  
// * FXL GUI API is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *  
// * FXL GUI API is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *  
// * You should have received a copy of the GNU General Public License
// * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
// *
// * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
// */
//package co.fxl.gui.navigation.impl;
//
//import co.fxl.gui.api.ICallback;
//import co.fxl.gui.api.IVerticalPanel;
//import co.fxl.gui.navigation.group.api.INavigationItem.IDecorator;
//import co.fxl.gui.navigation.group.impl.NavigationItemImpl;
//import co.fxl.gui.navigation.menu.api.IMenuItem;
//
//class MenuItemAdp implements IMenuItem, IDecorator {
//
//	private NavigationItemImpl item;
//	private boolean enabled = true;
//	private boolean visible = true;
//	private INavigationListener listener;
//	private IVerticalPanel panel;
//
//	MenuItemAdp(NavigationItemImpl item) {
//		this.item = item;
//		item.decorator(this);
//	}
//
//	@Override
//	public IMenuItem addNavigationItem() {
//		throw new RuntimeException();
//	}
//
//	@Override
//	public IMenuItem listener(
//			co.fxl.gui.navigation.menu.api.IMenuItem.INavigationListener listener) {
//		this.listener = listener;
//		return this;
//	}
//
//	@Override
//	public IMenuItem name(String text) {
//		item.name(text);
//		return this;
//	}
//
//	@Override
//	public IVerticalPanel contentPanel() {
//		assert panel != null;
//		return panel;
//	}
//
//	@Override
//	public IMenuItem visible(boolean visible) {
//		this.visible = visible;
//		item.visible(visible && enabled);
//		return this;
//	}
//
//	@Override
//	public IMenuItem active(boolean active) {
//		assert active;
//		item.active();
//		return this;
//	}
//
//	@Override
//	public boolean isActive() {
//		return item.isActive();
//	}
//
//	@Override
//	public IMenuItem enabled(boolean enabled) {
//		this.enabled = enabled;
//		item.visible(visible && enabled);
//		return this;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	@Override
//	public IMenuItem toggleLoading(boolean toggleLoading) {
//		item.toggleLoading(toggleLoading);
//		return this;
//	}
//
//	@Override
//	public void decorate(IVerticalPanel panel, ICallback<Void> cb) {
//		this.panel = panel;
//		listener.onActive(true, cb);
//	}
//
//}