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
//package co.fxl.gui.style.gplus;
//
//import co.fxl.gui.api.IHorizontalPanel;
//import co.fxl.gui.api.ILabel;
//import co.fxl.gui.api.IPanel;
//import co.fxl.gui.style.api.IStyle.IOptionMenu;
//
//public class GPlusOptionMenu implements IOptionMenu {
//
//	@Override
//	public ILabel addCommand(IPanel<?> panel, String text) {
//		ILabel l = panel.add().label().text(text).hyperlink();
//		// l.font().weight().bold().color().white();
//		// new HyperlinkMouseOverListener(l);
//		return l;
//	}
//
//	@Override
//	public IOptionMenu label(ILabel label) {
//		// label.font().color().lightgray();
//		return this;
//	}
//
//	@Override
//	public IOptionMenu searchButton(IHorizontalPanel buttonPanel) {
//		buttonPanel.color().rgb(77, 144, 255).gradient().vertical()
//				.rgb(71, 135, 237);
//		return this;
//	}
//
// }