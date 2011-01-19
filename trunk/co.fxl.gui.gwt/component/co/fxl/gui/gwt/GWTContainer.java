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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
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
import co.fxl.gui.api.WidgetProviderNotFoundException;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("unchecked")
public class GWTContainer<T extends Widget> implements IContainer {

	public WidgetParent parent;
	public T widget;
	protected GWTElement<?, ?> element;

	public GWTContainer(WidgetParent parent) {
		this.parent = parent;
	}

	public void setComponent(T component) {
		this.widget = component;
		if (parent != null)
			parent.add(component);
	}

	@Override
	public IButton button() {
		setComponent((T) new Button());
		return (IButton) (element = new GWTButton((GWTContainer<Button>) this));
	}

	@Override
	public ICheckBox checkBox() {
		setComponent((T) new CheckBox());
		return (ICheckBox) (element = new GWTCheckBox(
				(GWTContainer<CheckBox>) this));
	}

	@Override
	public IComboBox comboBox() {
		setComponent((T) new ListBox());
		return (IComboBox) (element = new GWTComboBox(
				(GWTContainer<ListBox>) this));
	}

	@Override
	public IElement<?> element(IElement<?> element) {
		GWTElement<?, ?> gwtElement = (GWTElement<?, ?>) element;
		setComponent((T) gwtElement.container.widget);
		this.element = gwtElement;
		return element;
	}

	@Override
	public IImage image() {
		setComponent((T) new Image());
		return (IImage) (element = new GWTImage((GWTContainer<Image>) this));
	}

	@Override
	public ILabel label() {
		setComponent((T) new HTML());
		return (ILabel) (element = new GWTLabel((GWTContainer<HTML>) this));
	}

	@Override
	public IElement<?> nativeElement(Object object) {
		setComponent((T) object);
		return (IElement<?>) (element = new GWTElement<T, IElement<?>>(
				(GWTContainer<T>) this));
	}

	@Override
	public ILayout panel() {
		return new GWTLayout(this);
	}

	@Override
	public IRadioButton radioButton() {
		setComponent((T) new RadioButton(GWTRadioButton.nextGroup()));
		return (IRadioButton) (element = new GWTRadioButton(
				(GWTContainer<RadioButton>) this));
	}

	@Override
	public ITextArea textArea() {
		setComponent((T) new TextArea());
		return (ITextArea) (element = new GWTTextArea(
				(GWTContainer<TextArea>) this));
	}

	@Override
	public ITextField textField() {
		setComponent((T) new TextBox());
		return (ITextField) (element = new GWTTextField(
				(GWTContainer<TextBox>) this));
	}

	@Override
	public IPasswordField passwordField() {
		setComponent((T) new PasswordTextBox());
		return (IPasswordField) (element = new GWTPasswordField(
				(GWTContainer<PasswordTextBox>) this));
	}

	@Override
	public IToggleButton toggleButton() {
		setComponent((T) new ToggleButton());
		return (IToggleButton) (element = new GWTToggleButton(
				(GWTContainer<ToggleButton>) this));
	}

	@Override
	public IScrollPane scrollPane() {
		setComponent((T) new ScrollPanel());
		return (IScrollPane) (element = new GWTScrollPane(
				(GWTContainer<ScrollPanel>) this));
	}

	@SuppressWarnings("deprecation")
	@Override
	public ISplitPane splitPane() {
		setComponent((T) new HorizontalSplitPanel());
		return (ISplitPane) (element = new GWTSplitPane(
				(GWTContainer<Widget>) this));
	}

	@Override
	public Object widget(Class<?> clazz) {
		IWidgetProvider<?> widgetProvider = lookupWidgetProvider(clazz);
		if (widgetProvider == null)
			throw new WidgetProviderNotFoundException(clazz);
		return widgetProvider.createWidget(this);
	}

	GWTDisplay lookupDisplay() {
		return parent.lookupDisplay();
	}

	IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		return parent.lookupWidgetProvider(interfaceClass);
	}

	@Override
	public IElement<?> element() {
		return element;
	}

	@Override
	public IContainer clear() {
		if (element != null)
			element.remove();
		return this;
	}

	@Override
	public IDisplay display() {
		return parent.lookupDisplay();
	}
}
