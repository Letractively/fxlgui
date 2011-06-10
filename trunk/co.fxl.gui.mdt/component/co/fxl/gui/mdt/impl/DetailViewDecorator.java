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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.ITextInput;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.DiscardChangesDialog;
import co.fxl.gui.api.template.DiscardChangesDialog.DiscardChangesListener;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;
import co.fxl.gui.format.api.IFormat;
import co.fxl.gui.format.impl.Format;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.mdt.impl.PropertyImpl.ConditionRuleImpl;
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
	private int spacing = 8;
	private Map<PropertyImpl, IFormField<?, ?>> property2formField = new HashMap<PropertyImpl, IFormField<?, ?>>();
	private Object node;
	private IVerticalPanel panel;
	private Map<PropertyImpl, IUpdateListener<Object>> listeners = new HashMap<PropertyImpl, IUpdateListener<Object>>();
	private boolean displayAllProperties = false;

	public void displayAllProperties() {
		displayAllProperties = true;
	}

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
	public boolean clear(IVerticalPanel panel) {
		panel.clear();
		return true;
	}

	protected void save(ITree<Object> tree, ICallback<Boolean> cb) {
	}

	protected void save(Object object, ICallback<Boolean> cb) {
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			final ITree<Object> node) {
		this.tree = node;
		isUpdateable = node.isUpdateable();
		isNew = node.isNew();
		decorate(panel, bottom, node.object());
	}

	public IFormWidget form() {
		return form;
	}

	public DetailViewDecorator spacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			final Object node) {
		this.panel = panel;
		updates.clear();
		assert node != null;
		this.node = node;
		panel.clear();
		decorateBorder(panel);
		if (bottom != null) {
			IGridPanel statusPanel = bottom.add().panel().grid().resize(3, 1);
			statusPanel.spacing(4);
		}
		property2formField.clear();
		listeners.clear();
		form = (IFormWidget) panel.add().panel().vertical().spacing(spacing)
				.add().widget(IFormWidget.class);
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
							new CallbackTemplate<Boolean>(cb) {

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
			form.addTitle(title).font().pixel(18);
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
		for (PropertyGroupImpl g : gs) {
			if (g.applies(node)) {
				for (final PropertyImpl property : g.properties) {
					boolean hasProperty = node == null ? true
							: property.adapter.hasProperty(node);
					if ((property.displayInDetailView || displayAllProperties)
							&& hasProperty) {
						final IFormField<?, ?> formField;
						final Object valueOf = node != null ? property.adapter
								.valueOf(node) : null;
						final ITextElement<?> valueElement;
						if (property.type.clazz.equals(String.class)) {
							if (property.type.isRelation
									|| property.type.encryptedText) {
								if (!property.editable) {
									IFormField<ITextField, String> tf = form
											.addTextField(property.name);
									formField = tf;
									valueElement = (ITextElement<?>) formField
											.valueElement();
									tf.valueElement().editable(false);
								} else {
									final IFormField<?, String> tf = property.type.encryptedText ? form
											.addPasswordField(property.name)
											: form.addTextField(property.name);
									formField = tf;
									valueElement = (ITextElement<?>) formField
											.valueElement();
									if (valueElement instanceof IPasswordField) {
										((IPasswordField) valueElement)
												.editable(false);
									} else {
										((ITextField) valueElement)
												.editable(false);
									}
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
																			valueElement
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
								IFormField<ITextArea, String> textArea = form
										.addTextArea(property.name);
								if (!property.editable)
									textArea.valueElement().editable(false);
								formField = textArea;
								valueElement = (ITextElement<?>) formField
										.valueElement();
							} else if (property.type.values.size() > 0) {
								formField = form.addComboBox(property.name);
								setValues(node, property, formField);
								valueElement = (ITextElement<?>) formField
										.valueElement();
							} else {
								IFormField<ITextField, String> tf = form
										.addTextField(property.name);
								formField = tf;
								decorateEditable(property, formField);
								valueElement = (ITextElement<?>) formField
										.valueElement();
							}
							setValue(valueOf, valueElement);
							updates.add(new Runnable() {
								@Override
								public void run() {
									ITextElement<?> valueElement = (ITextElement<?>) property2formField
											.get(property).valueElement();
									String text = valueElement.text();
									IAdapter<Object, Object> adapter = property.adapter;
									assert adapter != null;
									if (adapter.valueOf(node) == null
											&& "".equals(text))
										return;
									adapter.valueOf(node, valueElement.text());
								}
							});
						} else if (property.type.clazz.equals(Date.class)) {
							IFormField<ITextField, Date> tf = form
									.addDateField(property.name);
							formField = tf;
							decorateEditable(property, formField);
							formField.type().date();
							final IFormat<Date> dateFormat;
							if (property.type.isLong)
								dateFormat = Format.dateTime();
							else if (property.type.isShort)
								dateFormat = Format.time();
							else
								dateFormat = DetailView.DATE_FORMAT;
							String value = valueOf == null ? null : dateFormat
									.format((Date) valueOf);
							((ITextElement<?>) formField.valueElement())
									.text(value);
							updates.add(new Runnable() {
								@Override
								public void run() {
									Date value = null;
									String text = ((ITextElement<?>) formField
											.valueElement()).text().trim();
									if (!text.trim().equals("")) {
										value = dateFormat.parse(text);
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
									ICheckBox checkBox = (ICheckBox) formField
											.valueElement();
									property.adapter.valueOf(node,
											checkBox.checked());
								}
							});
						} else if (property.type.clazz.equals(Long.class)
								|| property.type.clazz.equals(Integer.class)) {
							final boolean isLong = property.type.clazz
									.equals(Long.class);
							IFormField<ITextField, String> tf = form
									.addTextField(property.name);
							formField = tf;
							decorateEditable(property, formField);
							@SuppressWarnings("rawtypes")
							final IFormat format;
							if (isLong) {
								formField.type().longType();
								format = Format.longInt();
							} else {
								formField.type().integer();
								format = Format.integer();
							}
							@SuppressWarnings("unchecked")
							String value = valueOf == null ? "" : format
									.format(valueOf);
							((ITextElement<?>) formField.valueElement())
									.text(value);
							if (property.editable)
								updates.add(new Runnable() {
									@Override
									public void run() {
										Object value = null;
										String text = ((ITextElement<?>) formField
												.valueElement()).text().trim();
										if (!text.equals("")) {
											value = format.parse(text);
											value = format.parse(text);
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
						supplement(form, property, property.name, formField);
						if (property != null && formField != null)
							addConditionRules(property, formField);
						property2formField.put(property, formField);
					}
				}
			}
		}
		supplement(form);
		if (alwaysShowCancel)
			form.alwaysAllowCancel();
		form.visible(true);
		for (PropertyImpl p : listeners.keySet()) {
			if (listeners.get(p) != null)
				listeners.get(p).onUpdate(p.adapter.valueOf(node));
		}
	}

	IComboBox setValues(final Object node, final PropertyImpl property,
			final IFormField<?, ?> formField) {
		IComboBox cb = (IComboBox) formField.valueElement();
		List<Object> vs = getDomain(node, property);
		for (Object s : vs)
			cb.addText((String) s);
		if (!property.editable)
			cb.editable(false);
		return cb;
	}

	public void setValue(final Object valueOf,
			final ITextElement<?> valueElement) {
		String value = valueOf == null ? null
				: (valueOf instanceof String ? (String) valueOf : String
						.valueOf(valueOf));
		valueElement.text(value);
	}

	public List<Object> getDomain(final Object node, final PropertyImpl property) {
		List<Object> vs = property.type.values;
		if (property.constraintAdapter != null) {
			vs = property.constraintAdapter.constraints(node);
		}
		return vs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addConditionRules(final PropertyImpl property,
			final IFormField<?, ?> formField) {
		if (!property.conditionRules.isEmpty()) {
			IUpdateListener listener = new IUpdateListener() {

				@Override
				public void onUpdate(final Object value) {
					panel.display().invokeLater(new Runnable() {
						@Override
						public void run() {
							for (final ConditionRuleImpl cr : property.conditionRules) {
								boolean satisfied = cr.condition.satisfied(
										node, value);
								boolean invisible = cr.invisible != null
										&& cr.invisible;
								if (cr.nonModifieable != null
										&& cr.nonModifieable && !invisible) {
									nonModifieable(cr, satisfied, value);
								}
								if (invisible) {
									invisible(cr, satisfied, value);
								}
								if (cr.targetValues != null) {
									targetValues(cr, satisfied, value);
								}
							}
						}
					});
				}

				private void targetValues(ConditionRuleImpl cr,
						boolean satisfied, Object value) {
					PropertyImpl p = property(cr);
					IFormField<IComboBox, String> ff = (IFormField<IComboBox, String>) target(p);
					if (ff == null)
						return;
					IFieldType type = ff.type();
					Object[] targetValues = satisfied ? withNull(ff,
							cr.targetValues) : getDomain(node, p).toArray();
					type.clearConstraints();
					for (Object o : targetValues) {
						if (o != null)
							type.addConstraint(o);
					}
					if (ff.visible()) {
						IComboBox comboBox = ff.valueElement();
						comboBox.clear();
						String text = comboBox.text();
						boolean found = false;
						String s0 = null;
						for (Object o : targetValues) {
							found |= o == null ? text == null : o.equals(text);
							comboBox.addText((String) o);
							if (s0 == null)
								s0 = (String) o;
						}
						if (!found && s0 != null) {
							comboBox.text(s0);
						}
					}
				}

				private Object[] withNull(IFormField<IComboBox, String> field,
						Object[] targetValues) {
					if (field.isRequired()) {
						Object[] o = new Object[targetValues.length + 1];
						o[0] = null;
						System.arraycopy(targetValues, 0, o, 1,
								targetValues.length);
						return o;
					}
					return targetValues;
				}

				private void nonModifieable(ConditionRuleImpl cr,
						boolean satisfied, Object value) {
					PropertyImpl p = property(cr);
					IFormField<?, ?> ff = target(p);
					ff.editable(!satisfied);
				}

				private void invisible(ConditionRuleImpl cr, boolean satisfied,
						Object value) {
					PropertyImpl p = property(cr);
					IFormField<?, ?> ff = target(p);
					boolean visible = !satisfied;
					boolean changed = ff.visible(visible);
					if (visible && changed) {
						setValues(node, p, ff);
						setValue(p.adapter.valueOf(node),
								(ITextElement<?>) ff.valueElement());
					}
				}

				private PropertyImpl property(ConditionRuleImpl cr) {
					return (PropertyImpl) cr.target;
				}

				private IFormField<?, ?> target(PropertyImpl p) {
					return property2formField.get(p);
				}
			};
			listeners.put(property, listener);
			formField.addUpdateListener(listener);
		}
	}

	public final void supplement(IFormWidget form, PropertyImpl property,
			String name, IFormField<?, ?> formField) {
		if (property.type.maxLength == -1
				|| !(formField.valueElement() instanceof ITextInput<?>))
			return;
		ITextInput<?> te = (ITextInput<?>) formField.valueElement();
		te.maxLength(property.type.maxLength);
	}

	private void decorateEditable(final PropertyImpl property,
			final IFormField<?, ?> formField) {
		if (!property.editable || !isUpdateable) {
			assert formField instanceof IFormField : "cast error in detail view";
			@SuppressWarnings("unchecked")
			IFormField<ITextField, ?> iFormField = (IFormField<ITextField, ?>) formField;
			iFormField.editable(false);
		}
	}

	public final void supplement(IFormWidget form) {
	}

	protected final void decorateBorder(IVerticalPanel panel) {
	}

	public DetailViewDecorator alwaysShowCancel() {
		alwaysShowCancel = true;
		return this;
	}
}