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
import co.fxl.gui.api.IHorizontalLine;
import co.fxl.gui.api.IHyperlink;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IRadioButton;
import co.fxl.gui.api.IRichTextArea;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISplitPane;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IToggleButton;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.WidgetProviderNotFoundException;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("unchecked")
public class GWTContainer<T extends Widget> implements IContainer {

	public WidgetParent parent;
	public T widget;
	protected GWTElement<?, ?> element;
	int index = -1;

	public GWTContainer(WidgetParent parent) {
		this.parent = parent;
	}

	// public GWTContainer(WidgetParent parent, int index) {
	// this(parent);
	// this.index = index;
	// }

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
		this.element = gwtElement;
		setComponent((T) gwtElement.container.widget);
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
	public ISuggestField suggestField() {
		setComponent((T) new SuggestBox(new MultiWordSuggestOracle()));
		return (ISuggestField) (element = new GWTSuggestField(
				(GWTContainer<SuggestBox>) this));
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
	public IHorizontalLine line() {
		setComponent((T) new HTML());
		return (IHorizontalLine) (element = new GWTHorizontalLine(
				(GWTContainer<HTML>) this));
	}

	@Override
	public IScrollPane scrollPane() {
		setComponent((T) new ScrollPanel());
		return (IScrollPane) (element = new GWTScrollPane(
				(GWTContainer<ScrollPanel>) this));
	}

	@Override
	public ISplitPane splitPane() {
		setComponent((T) GWTSplitPane.adapter.newSplitPanel());
		return (ISplitPane) (element = new GWTSplitPane(
				(GWTContainer<Widget>) this));
	}

	@Override
	public IHyperlink hyperlink() {
		setComponent((T) new HTML());
		return (IHyperlink) (element = new GWTHyperlink(
				(GWTContainer<HTML>) this));
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T widget(Class<T> clazz) {
		IWidgetProvider<?> widgetProvider = lookupWidgetProvider(clazz);
		if (widgetProvider == null)
			throw new WidgetProviderNotFoundException(clazz);
		return (T) widgetProvider.createWidget(this);
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

	@Override
	public IRichTextArea richTextArea() {
		setComponent((T) new RichTextArea());
		return (IRichTextArea) (element = new GWTRichTextArea(
				(GWTContainer<RichTextArea>) this));
	}

	// @SuppressWarnings("hiding")
	// @Override
	// public <T> void widget(Class<T> interfaceClass, ICallback<T> widget) {
	// GWTDisplay d = parent.lookupDisplay();
	// d.createWidget(interfaceClass, this, widget);
	// }
}
