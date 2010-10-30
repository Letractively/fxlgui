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
package co.fxl.gui.swing;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
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

@SuppressWarnings("unchecked")
class SwingContainer<T extends JComponent> implements IContainer {

	static final Insets INSETS = new Insets(4, 4, 4, 4);
	static final int MIN_HEIGHT_TEXT_COMPONENT = 24;
	static final int MIN_HEIGHT_TEXTAREA_COMPONENT = 100;
	ComponentParent parent;
	T component;

	SwingContainer(ComponentParent parent) {
		this.parent = parent;
	}

	void setComponent(T component) {
		this.component = component;
		if (parent != null)
			parent.add(component);
	}

	@Override
	public IButton button() {
		setComponent((T) new JButton());
		return new SwingButton((SwingContainer<JButton>) this);
	}

	@Override
	public ICheckBox checkBox() {
		setComponent((T) new JCheckBox());
		return new SwingCheckBox((SwingContainer<JCheckBox>) this);
	}

	@Override
	public IComboBox comboBox() {
		setComponent((T) new ComboBoxComponent());
		return new SwingComboBox((SwingContainer<JComboBox>) this);
	}

	@Override
	public IElement<?> element(IElement<?> element) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IImage image() {
		setComponent((T) new JLabel());
		return new SwingImage((SwingContainer<JLabel>) this);
	}

	@Override
	public ILabel label() {
		setComponent((T) new JLabel());
		return new SwingLabel((SwingContainer<JLabel>) this);
	}

	@Override
	public IToggleButton toggleButton() {
		setComponent((T) new JToggleButton());
		return new SwingToggleButton((SwingContainer<JToggleButton>) this);
	}

	@Override
	public IElement<?> nativeElement(Object object) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILayout panel() {
		setComponent((T) new JPanel());
		SwingPanel<?> panel = new SwingDockPanel((SwingContainer<JPanel>) this);
		return new SwingLayout(panel);
	}

	@Override
	public IRadioButton radioButton() {
		setComponent((T) new JRadioButton());
		return new SwingRadioButton((SwingContainer<JRadioButton>) this);
	}

	@Override
	public ITextArea textArea() {
		setComponent((T) new TextAreaComponent<T>());
		return new SwingTextArea((SwingContainer<JTextArea>) this);
	}

	@Override
	public ITextField textField() {
		setComponent((T) new TextFieldComponent<T>());
		return new SwingTextField((SwingContainer<JTextField>) this);
	}

	@Override
	public IPasswordField passwordField() {
		setComponent((T) new PasswordFieldComponent<T>());
		return new SwingPasswordField((SwingContainer<JPasswordField>) this);
	}

	@Override
	public IScrollPane scrollPane() {
		setComponent((T) new JScrollPane());
		return new SwingScrollPane((SwingContainer<JScrollPane>) this);
	}

	@Override
	public ISplitPane splitPane() {
		setComponent((T) new JSplitPane());
		return new SwingSplitPane((SwingContainer<JSplitPane>) this);
	}

	@Override
	public Object widget(Class<?> clazz) {
		IWidgetProvider<?> widgetProvider = lookupWidgetProvider(clazz);
		if (widgetProvider == null)
			throw new WidgetProviderNotFoundException(clazz);
		return widgetProvider.createWidget(panel());
	}

	IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		return parent.lookupWidgetProvider(interfaceClass);
	}

	SwingDisplay lookupSwingDisplay() {
		return parent.lookupSwingDisplay();
	}

	@Override
	public IElement<?> element() {
		return new SwingElement(this);
	}

	@Override
	public IContainer clear() {
		if (component != null)
			element().remove();
		return this;
	}
}
