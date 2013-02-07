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
//package co.fxl.gui.impl;
//
//import co.fxl.gui.api.IHorizontalPanel;
//import co.fxl.gui.api.IPopUp;
//import co.fxl.gui.api.IRichTextArea;
//import co.fxl.gui.api.IUpdateable.IUpdateListener;
//
//public class RichTextToolbar {
//
//	public interface IRichTextAdapter {
//
//		void toggleBold();
//
//		boolean isBold();
//
//	}
//
//	public RichTextToolbar(final IRichTextArea rta, final IRichTextAdapter adp) {
//		rta.addFocusListener(new IUpdateListener<Boolean>() {
//
//			private IPopUp popUp;
//
//			@Override
//			public void onUpdate(Boolean value) {
//				if (popUp == null && value) {
//					popUp = rta.display().showPopUp();
//					popUp.offset(rta.offsetX(), rta.offsetY() + rta.height());
//					popUp.size(rta.width(), 40);
//					IHorizontalPanel h = popUp.container().panel().horizontal()
//							.spacing(4);
//					h.color().lightgray();
//					h.border().color().gray();
//					h.width(rta.width());
//					h.add().toggleButton().text("b").down(adp.isBold())
//							.addUpdateListener(new IUpdateListener<Boolean>() {
//								@Override
//								public void onUpdate(Boolean value) {
//									if (adp.isBold() != value)
//										adp.toggleBold();
//									rta.focus(true);
//								}
//							});
//					popUp.height(h.height());
//					popUp.visible(true);
//				} else if (popUp != null && !value) {
//					popUp.visible(false);
//					popUp = null;
//				}
//			}
//		});
//	}
//
//}
