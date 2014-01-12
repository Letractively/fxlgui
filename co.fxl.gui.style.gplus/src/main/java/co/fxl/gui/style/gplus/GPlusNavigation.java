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
//import co.fxl.gui.api.IBordered.IBorder;
//import co.fxl.gui.api.IColored;
//import co.fxl.gui.api.ILabel;
//import co.fxl.gui.api.ILinearPanel;
//import co.fxl.gui.api.IPanel;
//import co.fxl.gui.style.api.IStyle.INavigation;
//
//class GPlusNavigation implements INavigation {
//
//	@Override
//	public INavigationGroup group() {
//		return new INavigationGroup() {
//
//			@Override
//			public INavigation mainPanel(IPanel<?> panel) {
//				panel.color().remove();
//				panel.color().white();
//				// panel.color().rgb(245, 245, 245);
//				// IBorder b = panel.border();
//				// b.color().lightgray();
//				// b.style().bottom();
//				return GPlusNavigation.this;
//			}
//
//			@Override
//			public INavigation groupPanel(ILinearPanel<?> panel) {
//				// panel.spacing(3);
//				return GPlusNavigation.this;
//			}
//
//			@Override
//			public INavigationItem item() {
//				return new INavigationItem() {
//
//					@Override
//					public INavigation inactive(ILinearPanel<?> buttonPanel,
//							ILabel button) {
////						buttonPanel.spacing(4);
//						// button.font().pixel(13);
//						IBorder border = buttonPanel.border();
//						border.color().white();
//						border.style().noBottom();
//						buttonPanel.color().remove();
//						buttonPanel.color().white();
//						button.font().color().gray();
//						// buttonPanel.spacing(4);
//						// button.font().pixel(13);
//						// buttonPanel.border().color().gray();
//						// buttonPanel.color().gray();
//						// button.font().color().white();
//						return GPlusNavigation.this;
//					}
//
//					@Override
//					public INavigation active(ILinearPanel<?> buttonPanel,
//							ILabel button) {
//						IBorder border = buttonPanel.border();
//						border.color().gray();
//						border.style().noBottom();
//						buttonPanel.color().remove();
//						buttonPanel.color().rgb(235, 235, 235);
//						button.font().color().gray();
//						button.font().color().black();
//						return GPlusNavigation.this;
//					}
//
//					@Override
//					public String image(String resource) {
//						return resource.equals("loading_white.gif") ? "loading_black.gif"
//								: resource;
//					}
//				};
//			}
//		};
//	}
//
//	@Override
//	public INavigation background(IColored color) {
//		color.color().rgb(235, 235, 235);
//		return this;
//	}
//
// }
