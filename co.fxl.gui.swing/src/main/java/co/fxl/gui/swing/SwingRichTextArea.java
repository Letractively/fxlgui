///**
// * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
// *  
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
// */
//package co.fxl.gui.swing;
//
//import javax.swing.JTextArea;
//
//import co.fxl.gui.api.IRichTextArea;
//import co.fxl.gui.api.IUpdateable;
//import co.fxl.gui.impl.RichTextToolbar;
//import co.fxl.gui.impl.RichTextToolbar.IRichTextAdapter;
//
//class SwingRichTextArea extends SwingTextInput<JTextArea, IRichTextArea>
//		implements IRichTextArea {
//
//	// TODO add scrollpanel
//
//	SwingRichTextArea(SwingContainer<JTextArea> container) {
//		super(container);
//		jTextArea().setLineWrap(true);
//		font().family().arial();
//		font().pixel(12);
//		new RichTextToolbar(this, new IRichTextAdapter() {
//
//			private boolean isBold = false;
//
//			@Override
//			public void toggleBold() {
//				if (isBold)
//					text(text() + "</b>");
//				else
//					text(text() + "<b>");
//				cursorPosition(text().length());
//				isBold = !isBold;
//			}
//
//			@Override
//			public boolean isBold() {
//				return isBold;
//			}
//		});
//	}
//
//	private JTextArea jTextArea() {
//		return (JTextArea) container.component;
//	}
//
//	@Override
//	public IRichTextArea text(String text) {
//		super.setText(text);
//		return this;
//	}
//
//	@Override
//	String processText() {
//		return html.text;
//	}
//
//	public int cursorPosition() {
//		return container.component.getCaretPosition();
//	}
//
//	public IRichTextArea cursorPosition(int position) {
//		container.component.setCaretPosition(position);
//		container.component.requestFocus();
//		return this;
//	}
//
//	@Override
//	public IUpdateable<String> addUpdateListener(
//			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
//		return super.addStringUpdateListener(listener);
//	}
//}