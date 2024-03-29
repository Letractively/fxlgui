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
// * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
// */
//package co.fxl.gui.style.gplus;
//
//import co.fxl.gui.api.ILabel;
//import co.fxl.gui.api.ILinearPanel;
//import co.fxl.gui.api.IPanel;
//import co.fxl.gui.impl.Constants;
//import co.fxl.gui.style.api.IStyle;
//import co.fxl.gui.style.impl.Style;
//
//public class GPlusStyle implements IStyle {
//
//	static final int SPACING_TOP = 10;
//
//	@Override
//	public IWindow window() {
//		assertEnabled();
//		return new GPlusWindow();
//	}
//
//	private void assertEnabled() {
//		assert Style.ENABLED;
//	}
//
//	@Override
//	public IStyle background(IPanel<?> panel) {
//		assertEnabled();
//		// panel.color().white();
//		return this;
//	}
//
//	@Override
//	public IStyle side(ILinearPanel<?> panel) {
//		assertEnabled();
//		// panel.spacing().left(10);
//		// IBorder border = panel.border();
//		// border.color().lightgray();
//		// border.width(1);
//		// border.style().left();
//		// panel.color().white();
//		return this;
//	}
//
//	@Override
//	public IStyle hyperlink(ILabel label) {
//		label.font().color().rgb(114, 102, 211);
//		return this;
//	}
//
//	@Override
//	public ILogin login() {
//		return new GPlusLogin();
//	}
//
//	@Override
//	public INavigation navigation() {
//		return new GPlusNavigation();
//	}
//
//	@Override
//	public IRegister register() {
//		return new GPlusRegister();
//	}
//
//	@Override
//	public ITable table() {
//		return new GPlusTable();
//	}
//
//	@Override
//	public ITree tree() {
//		return new GPlusTree();
//	}
//
//	@Override
//	public IOptionMenu optionMenu() {
//		return new GPlusOptionMenu();
//	}
//
//	@Override
//	public ITop top() {
//		return new GPlusTop();
//	}
//
//	@Override
//	public IStyle activate(boolean activate) {
//		if (!activate) {
//			Constants.remove("TOP_SPACING");
//		}
////		Constants.put("ADD_NAVIGATION_BAR", activate);
//		return this;
//	}
// }
