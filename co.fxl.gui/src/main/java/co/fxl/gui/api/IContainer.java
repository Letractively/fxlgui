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
package co.fxl.gui.api;

public interface IContainer {

	ILayout panel();

	IButton button();

	IImage image();

	ILabel label();

	ITextField textField();

	ISuggestField suggestField();

	IPasswordField passwordField();

	ITextArea textArea();

	IRichTextArea richTextArea();

	ICheckBox checkBox();

	IComboBox comboBox();

	IRadioButton radioButton();

	IHorizontalLine line();

	IToggleButton toggleButton();

	IScrollPane scrollPane();

	ISplitPane splitPane();

	IHyperlink hyperlink();

	IElement<?> element(IElement<?> element);

	IElement<?> nativeElement(Object object);

	IElement<?> element();

	<T> T widget(Class<T> interfaceClass);

//	<T> void widget(Class<T> interfaceClass, ICallback<T> callback);

	IContainer clear();

	IDisplay display();
}
