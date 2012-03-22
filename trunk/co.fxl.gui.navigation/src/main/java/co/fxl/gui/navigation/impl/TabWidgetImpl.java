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
//import co.fxl.gui.api.IContainer;
//import co.fxl.gui.navigation.api.ITabWidget;
//import co.fxl.gui.navigation.group.api.INavigationGroup;
//import co.fxl.gui.navigation.group.impl.NavigationItemImpl;
//import co.fxl.gui.navigation.group.impl.NavigationWidgetImpl;
//import co.fxl.gui.navigation.menu.api.IMenuItem;
//
//class TabWidgetImpl extends NavigationWidgetImpl implements ITabWidget {
//
//	private INavigationGroup group;
//
//	TabWidgetImpl(IContainer layout) {
//		super(layout);
//		group = addGroup();
//	}
//
//	@Override
//	public int height() {
//		return mainPanel.height();
//	}
//
//	@Override
//	public IMenuItem addNavigationItem() {
//		return new MenuItemAdp((NavigationItemImpl) group.addItem());
//	}
//
//}
