/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.mdt.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

public abstract class DetailViewDecorator implements IDecorator<Object> {

	private final List<PropertyGroupImpl> gs;
	private String title = null;
	private boolean hasRequiredAttributes = true;
	private boolean isUpdateable = true;

	public void setUpdateable(boolean isUpdateable) {
		this.isUpdateable = isUpdateable;
	}

	public DetailViewDecorator setTitle(String title) {
		this.title = title;
		return this;
	}

	public DetailViewDecorator(List<PropertyGroupImpl> gs) {
		this.gs = gs;
	}

	public DetailViewDecorator(PropertyGroupImpl pGroup) {
		gs = new LinkedList<PropertyGroupImpl>();
		gs.add(pGroup);
	}

	public DetailViewDecorator setHasRequiredAttributes(
			boolean hasRequiredAttributes) {
		this.hasRequiredAttributes = hasRequiredAttributes;
		return this;
	}

	@Override
	public void clear(IVerticalPanel panel) {
		panel.clear();
	}

	protected abstract void save(Object node);

	@Override
	public void decorate(IVerticalPanel panel, final ITree<Object> node) {
		isUpdateable = node.isUpdateable();
		decorate(panel, node.object());
	}

	@Override
	public void decorate(IVerticalPanel panel, final Object node) {
		panel.clear();
		decorateBorder(panel);
		IFormWidget form = (IFormWidget) panel.add().widget(IFormWidget.class);
		if (title != null)
			form.addTitle(title);
		final List<Runnable> updates = new LinkedList<Runnable>();
		if (isUpdateable)
			form.saveListener("Save", new ISaveListener() {

				@Override
				public void onSave() {
					for (Runnable update : updates)
						update.run();
					save(node);
				}
			});
		for (PropertyGroupImpl g : gs)
			if (g.applies(node))
				for (final PropertyImpl property : g.properties) {
					boolean hasProperty = node == null ? true
							: property.adapter.hasProperty(node);
					if (property.displayInDetailView && hasProperty) {
						final IFormField<?> formField;
						Object valueOf = node != null ? property.adapter
								.valueOf(node) : null;
						final ITextElement<?> valueElement;
						if (property.type.clazz.equals(String.class)) {
							if (property.type.isLong) {
								IFormField<ITextArea> textArea = form
										.addTextArea(property.name);
								if (!property.editable)
									textArea.valueElement().editable(false);
								formField = textArea;
								valueElement = formField.valueElement();
							} else if (property.type.values.size() > 0) {
								formField = form.addComboBox(property.name);
								IComboBox cb = (IComboBox) formField
										.valueElement();
								if (!property.editable)
									cb.editable(false);
								for (Object s : property.type.values)
									cb.addText((String) s);
								valueElement = formField.valueElement();
							} else {
								formField = form.addTextField(property.name);
								if (!property.editable)
									((ITextField) formField).editable(false);
								valueElement = formField.valueElement();
							}
							String value = valueOf == null ? ""
									: (valueOf instanceof String ? (String) valueOf
											: String.valueOf(valueOf));
							valueElement.text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									property.adapter.valueOf(node,
											valueElement.text());
								}
							});
						} else if (property.type.clazz.equals(Date.class)) {
							formField = form.addTextField(property.name);
							if (!property.editable)
								((ITextField) formField).editable(false);
							formField.type().date();
							String value = DetailView.DATE_FORMAT
									.format((Date) valueOf);
							formField.valueElement().text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									Date value = null;
									String text = formField.valueElement()
											.text().trim();
									if (!text.equals("")) {
										value = DetailView.DATE_FORMAT
												.parse(text);
									}
									property.adapter.valueOf(node, value);
								}
							});
						} else if (property.type.clazz.equals(Boolean.class)) {
							formField = form.addCheckBox(property.name);
							ICheckBox checkBox = (ICheckBox) formField
									.valueElement();
							if (!property.editable)
								checkBox.editable(false);
							Boolean b = (Boolean) valueOf;
							if (b == null)
								b = false;
							checkBox.checked(b);
							updates.add(new Runnable() {
								@Override
								public void run() {
									throw new MethodNotImplementedException();
								}
							});
						} else if (property.type.clazz.equals(Long.class)
								|| property.type.clazz.equals(Integer.class)) {
							final boolean isLong = property.type.clazz
									.equals(Long.class);
							formField = form.addTextField(property.name);
							if (!property.editable)
								((ITextField) formField).editable(false);
							// TODO long ...
							formField.type().integer();
							String value = valueOf == null ? ""
									: ((Number) valueOf).toString();
							formField.valueElement().text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									Object value = null;
									String text = formField.valueElement()
											.text().trim();
									if (!text.equals("")) {
										if (isLong)
											value = Long.valueOf(text);
										else
											value = Integer.valueOf(text);
									}
									property.adapter.valueOf(node, value);
								}
							});
						} else
							throw new MethodNotImplementedException(
									property.type.clazz);
						if (property.required && hasRequiredAttributes)
							formField.required();
					}
				}
		form.visible(true);
	}

	protected void decorateBorder(IVerticalPanel panel) {
		IBorder border = panel.border();
		border.color().gray();
		border.style().top();
	}
}