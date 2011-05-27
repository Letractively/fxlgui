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
package co.fxl.gui.android;

import android.view.View;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IHorizontalLine;
import co.fxl.gui.api.IHyperlink;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IRadioButton;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IToggleButton;
import co.fxl.gui.api.IWidgetProvider;

public class AndroidContainer implements IContainer {

	public Parent parent;
	int paddingTop = 0;
	int paddingLeft = 0;
	IElement<?> element;

	public AndroidContainer(Parent parent) {
		this.parent = parent;
	}

	@Override
	public IButton button() {
		return (IButton) (element = new AndroidButton(this));
	}

	@Override
	public ICheckBox checkBox() {
		return (ICheckBox) (element = new AndroidCheckBox(this));
	}

	@Override
	public IContainer clear() {
		if (element != null)
			element.remove();
		return this;
	}

	@Override
	public IComboBox comboBox() {
		return (IComboBox) (element = new AndroidComboBox(this));
	}

	@Override
	public IElement<?> element(IElement<?> element) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IElement<?> element() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IImage image() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILabel label() {
		return (ILabel) (element = new AndroidLabel(this));
	}

	@Override
	public IElement<?> nativeElement(Object object) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILayout panel() {
		return new AndroidLayout(this);
	}

	@Override
	public IPasswordField passwordField() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IRadioButton radioButton() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane scrollPane() {
		return new AndroidScrollPane(this);
	}

	@Override
	public ISplitPane splitPane() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ITextArea textArea() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ITextField textField() {
		return new AndroidTextField(this);
	}

	@Override
	public IToggleButton toggleButton() {
		throw new MethodNotImplementedException();
	}

	@Override
	public Object widget(Class<?> interfaceClass) {
		IWidgetProvider<?> provider = parent.androidDisplay().widgetProvider(
				interfaceClass);
		return provider.createWidget(this);
	}

	void layout(View view) {
		view.setPadding(paddingLeft, paddingTop, 0, 0);
	}

	@Override
	public IHorizontalLine line() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IHyperlink hyperlink() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay display() {
		throw new MethodNotImplementedException();
	}
}
