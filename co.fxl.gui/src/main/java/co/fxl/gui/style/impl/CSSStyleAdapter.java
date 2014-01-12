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
//package co.fxl.gui.style.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import co.fxl.gui.api.IBordered.IBorder;
//import co.fxl.gui.api.IContainer;
//import co.fxl.gui.api.IDecorator;
//import co.fxl.gui.api.IDisplay;
//import co.fxl.gui.api.IElement;
//import co.fxl.gui.api.ILabel;
//import co.fxl.gui.api.IPanel;
//import co.fxl.gui.api.ISuggestField;
//import co.fxl.gui.impl.Display;
//import co.fxl.gui.impl.NavigationView;
//import co.fxl.gui.style.api.IStyle;
//
//class CSSStyleAdapter implements IStyle {
//
//	private IStyle style;
//	private Map<String, IDecorator<?>> styles = new HashMap<String, IDecorator<?>>();
//
//	CSSStyleAdapter(IStyle style) {
//		this.style = style;
//	}
//
//	private <T extends IElement<T>> void inject(String id,
//			IDecorator<T> decorator, T element) {
//		@SuppressWarnings("unchecked")
//		IDecorator<T> adapter = (IDecorator<T>) styles.get(id);
//		if (adapter == null) {
//			adapter = Display.instance().adoptStyle(decorator);
//			styles.put(id, adapter);
//		}
//		adapter.decorate(element);
//	}
//
//	@Override
//	public IRegisterStyle register() {
//		return style.register();
//	}
//
//	@Override
//	public IApplicationPanel applicationPanel() {
//		return style.applicationPanel();
//	}
//
//	@Override
//	public ILogin login() {
//		return style.login();
//	}
//
//	@Override
//	public IUserPanel userPanel() {
//		return style.userPanel();
//	}
//
//	@Override
//	public int fontSize() {
//		return style.fontSize();
//	}
//
//	@Override
//	public String fontFamily() {
//		return style.fontFamily();
//	}
//
//	@Override
//	public INavigation navigation() {
//		return style.navigation();
//	}
//
//	@Override
//	public ILogoPanel logoPanel() {
//		return style.logoPanel();
//	}
//
//	@Override
//	public IViewSelection createSelection(IContainer c,
//			boolean noDiscardChangesDialog) {
//		return style.createSelection(c, noDiscardChangesDialog);
//	}
//
//	@Override
//	public void background(IPanel<?> panel) {
//		style.background(panel);
//	}
//
//	@Override
//	public void display(IDisplay display) {
//		style.display(display);
//	}
//
//	@Override
//	public IWindow window() {
//		return style.window();
//	}
//
//	@Override
//	public IMDT mdt() {
//		return new IMDT() {
//
//			private IMDT mdt = style.mdt();
//
//			@Override
//			public boolean showQuickSearchOnTop() {
//				return mdt.showQuickSearchOnTop();
//			}
//
//			@Override
//			public void quickSearch(ISuggestField sf) {
//				inject("quickSearch", new IDecorator<ISuggestField>() {
//					@Override
//					public void decorate(ISuggestField element) {
//						mdt.quickSearch(element);
//					}
//				}, sf);
//			}
//
//			@Override
//			public void actionPanel(NavigationView navigationView) {
//				mdt.actionPanel(navigationView);
//			}
//
//		};
//	}
//
//	@Override
//	public IPopUpWindow popUp() {
//		return style.popUp();
//	}
//
//	@Override
//	public IFilterPanel filter() {
//		return style.filter();
//	}
//
//	@Override
//	public ITable table() {
//		return style.table();
//	}
//
//	@Override
//	public ITree tree() {
//		return style.tree();
//	}
//
//	@Override
//	public IFormStyle form() {
//		return style.form();
//	}
//
//	@Override
//	public IN2MStyle n2m() {
//		return style.n2m();
//	}
//
//	@Override
//	public void hyperlink(ILabel label) {
//		style.hyperlink(label);
//	}
//
//	@Override
//	public void popUp(IBorder border) {
//		style.popUp(border);
//	}
//
//	@Override
//	public IExportHtml exportHtml() {
//		return style.exportHtml();
//	}
//
//	@Override
//	public boolean embedded() {
//		return style.embedded();
//	}
//
//	@Override
//	public void embedded(boolean embedded) {
//		style.embedded(embedded);
//	}
//
//	@Override
//	public boolean glass() {
//		return style.glass();
//	}
//
//	@Override
//	public IButtonImage button() {
//		return style.button();
//	}
//
//	@Override
//	public boolean allowDetailView() {
//		return style.allowDetailView();
//	}
//
//	@Override
//	public void allowDetailView(boolean allowDetailView) {
//		style.allowDetailView(allowDetailView);
//	}
//
// }
