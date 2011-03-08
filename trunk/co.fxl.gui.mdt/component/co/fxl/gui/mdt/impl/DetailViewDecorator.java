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

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.DiscardChangesDialog;
import co.fxl.gui.api.template.DiscardChangesDialog.DiscardChangesListener;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

public abstract class DetailViewDecorator implements IDecorator<Object> {

	public interface DeleteListener {

		void onDelete(ITree<Object> tree, ICallback<Boolean> cb);

		void onDelete(Object tree, ICallback<Boolean> cb);
	}

	private final List<PropertyGroupImpl> gs;
	private String title = null;
	private boolean hasRequiredAttributes = true;
	private boolean isUpdateable = true;
	IFormWidget form;
	// private Object node;
	final List<Runnable> updates = new LinkedList<Runnable>();
	private boolean isNew;
	private ITree<Object> tree;
	private DeleteListener deleteListener;
	private boolean alwaysShowCancel = false;

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

	public DetailViewDecorator refreshListener(DeleteListener refreshListener) {
		this.deleteListener = refreshListener;
		return this;
	}

	public DetailViewDecorator(PropertyGroupImpl pGroup) {
		gs = new LinkedList<PropertyGroupImpl>();
		gs.add(pGroup);
	}

	public DetailViewDecorator isNew(boolean isNew) {
		this.isNew = isNew;
		return this;
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

	protected void save(ITree<Object> tree, ICallback<Boolean> cb) {
	}

	protected void save(Object object, ICallback<Boolean> cb) {
	}

	@Override
	public void decorate(IVerticalPanel panel, final ITree<Object> node) {
		this.tree = node;
		isUpdateable = node.isUpdateable();
		isNew = node.isNew();
		decorate(panel, node.object());
	}

	public IFormWidget form() {
		return form;
	}

	@Override
	public void decorate(IVerticalPanel panel, final Object node) {
		updates.clear();
		assert node != null;
		panel.clear();
		decorateBorder(panel);
		form = (IFormWidget) panel.add().widget(IFormWidget.class);
		form.isNew(isNew);
		if (isNew) {
			DiscardChangesDialog.listener = new DiscardChangesListener() {

				@Override
				public void onKeepChanges(ICallback<Boolean> cb) {
					cb.onSuccess(false);
				}

				@Override
				public void onDiscardChanges(final ICallback<Boolean> cb) {
					// tree.delete(new CallbackTemplate<Object>() {
					//
					// @Override
					// public void onSuccess(Object result) {
					deleteListener.onDelete(tree,
							new CallbackTemplate<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									cb.onSuccess(true);
								}
							});
					// }
					// });
				}
			};
		}
		if (title != null)
			form.addTitle(title);
		if (isUpdateable)
			form.saveListener("Save", new ISaveListener() {

				@Override
				public void save(ICallback<Boolean> cb) {
					for (Runnable update : updates)
						update.run();
					DetailViewDecorator.this.save(tree, cb);
					DetailViewDecorator.this.save(node, cb);
				}

				@Override
				public void cancel(ICallback<Boolean> cb) {
					if (deleteListener != null) {
						deleteListener.onDelete(tree, cb);
						deleteListener.onDelete(node, cb);
					} else
						cb.onSuccess(true);
				}

				@Override
				public boolean allowsCancel() {
					return true;
				}
			});
		for (PropertyGroupImpl g : gs)
			if (g.applies(node))
				for (final PropertyImpl property : g.properties) {
					boolean hasProperty = node == null ? true
							: property.adapter.hasProperty(node);
					if (property.displayInDetailView && hasProperty) {
						final IFormField<?> formField;
						final Object valueOf = node != null ? property.adapter
								.valueOf(node) : null;
						final ITextElement<?> valueElement;
						if (property.type.clazz.equals(String.class)) {
							if (property.type.isRelation) {
								if (!property.editable) {
									IFormField<ITextField> tf = form
											.addTextField(property.name);
									formField = tf;
									valueElement = formField.valueElement();
									tf.valueElement().editable(false);
								} else {
									final IFormField<ITextField> tf = form
											.addTextField(property.name);
									tf.valueElement().editable(false);
									formField = tf;
									valueElement = formField.valueElement();
									if (property.listener != null) {
										final ILabel assign = formField
												.addButton(valueOf == null ? "Assign"
														: "Change");
										assign.addClickListener(new LazyClickListener() {
											@Override
											public void onAllowedClick() {
												property.listener
														.update(node,
																new CallbackTemplate<Boolean>() {

																	@Override
																	public void onSuccess(
																			Boolean update) {
																		if (update) {
																			String newString = (String) property.adapter
																					.valueOf(node);
																			tf.valueElement()
																					.text(newString);
																			assign.text(newString == null ? "Assign"
																					: "Change");
																			CallbackTemplate<Boolean> cb = new CallbackTemplate<Boolean>() {
																				@Override
																				public void onSuccess(
																						Boolean result) {
																				}
																			};
																			save(tree,
																					cb);
																			save(node,
																					cb);
																		}
																	}
																});
											}
										});
									}
								}
							} else if (property.type.isLong) {
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
								List<Object> vs = property.type.values;
								if (property.constraintAdapter != null) {
									vs = property.constraintAdapter
											.constraints(node);
								}
								for (Object s : vs)
									cb.addText((String) s);
								valueElement = formField.valueElement();
							} else {
								IFormField<ITextField> tf = form
										.addTextField(property.name);
								formField = tf;
								decorateEditable(property, formField);
								valueElement = formField.valueElement();
							}
							String value = valueOf == null ? ""
									: (valueOf instanceof String ? (String) valueOf
											: String.valueOf(valueOf));
							valueElement.text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									if (property.adapter.valueOf(node) == null
											&& valueElement.text().equals(""))
										return;
									property.adapter.valueOf(node,
											valueElement.text());
								}
							});
						} else if (property.type.clazz.equals(Date.class)) {
							IFormField<ITextField> tf = form
									.addTextField(property.name);
							formField = tf;
							decorateEditable(property, formField);
							formField.type().date();
							String value = valueOf == null ? ""
									: DetailView.DATE_FORMAT
											.format((Date) valueOf);
							formField.valueElement().text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									Date value = null;
									String text = formField.valueElement()
											.text().trim();
									if (!text.trim().equals("")) {
										value = DetailView.DATE_FORMAT
												.parse(text);
									}
									property.adapter.valueOf(node, value);
								}
							});
						} else if (property.type.clazz.equals(Boolean.class)) {
							formField = form.addCheckBox(property.name);
							final ICheckBox checkBox = (ICheckBox) formField
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
									property.adapter.valueOf(node,
											checkBox.checked());
								}
							});
						} else if (property.type.clazz.equals(Long.class)
								|| property.type.clazz.equals(Integer.class)) {
							final boolean isLong = property.type.clazz
									.equals(Long.class);
							IFormField<ITextField> tf = form
									.addTextField(property.name);
							formField = tf;
							decorateEditable(property, formField);
							// TODO long ...
							formField.type().integer();
							String value = valueOf == null ? ""
									: ((Number) valueOf).toString();
							formField.valueElement().text(value);
							if (property.editable)
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
						} else {
							formField = null;
							new MethodNotImplementedException(
									"type in detail view not supported: "
											+ property.type.clazz)
									.printStackTrace();
							// throw new MethodNotImplementedException(
							// "type in detail view not supported: "
							// + property.type.clazz);
						}
						if (property != null && formField != null
								&& property.required && hasRequiredAttributes)
							formField.required();
						supplement(form, property.name, formField);
					}
				}
		supplement(form);
		if (alwaysShowCancel)
			form.alwaysAllowCancel();
		form.visible(true);
	}

	public void supplement(IFormWidget form, String name,
			IFormField<?> formField) {
	}

	private void decorateEditable(final PropertyImpl property,
			final IFormField<?> formField) {
		if (!property.editable) {
			assert formField instanceof IFormField : "cast error in detail view";
			@SuppressWarnings("unchecked")
			IFormField<ITextField> iFormField = (IFormField<ITextField>) formField;
			iFormField.valueElement().editable(false);
		}
	}

	public void supplement(IFormWidget form) {
	}

	protected void decorateBorder(IVerticalPanel panel) {
		// IBorder border = panel.border();
		// border.color().gray();
		// border.style().top();
	}

	public DetailViewDecorator alwaysShowCancel() {
		alwaysShowCancel = true;
		return this;
	}
}