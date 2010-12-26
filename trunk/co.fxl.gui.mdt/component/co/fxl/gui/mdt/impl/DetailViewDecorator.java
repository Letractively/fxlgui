package co.fxl.gui.mdt.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

final class DetailViewDecorator implements IDecorator<Object> {

	private final DetailView detailView;
	private final PropertyGroupImpl group;

	DetailViewDecorator(DetailView detailView, PropertyGroupImpl group) {
		this.detailView = detailView;
		this.group = group;
	}

	@Override
	public void clear(IVerticalPanel panel) {
		panel.clear();
	}

	@Override
	public void decorate(IVerticalPanel panel, final Object node) {
		panel.clear();
		IBorder border = panel.border();
		border.color().gray();
		border.style().top();
		IFormWidget form = (IFormWidget) panel.add().widget(IFormWidget.class);
		final List<Runnable> updates = new LinkedList<Runnable>();
		form.saveListener("Save", new ISaveListener() {

			@Override
			public void onSave() {
				for (Runnable update : updates)
					update.run();
				DetailViewDecorator.this.detailView.itree.save(node);
				DetailViewDecorator.this.detailView.tree.notifyUpdate(node);
			}
		});
		for (final PropertyImpl property : group.properties) {
			boolean hasProperty = property.adapter.hasProperty(node);
			if (property.displayInDetailView && hasProperty) {
				final IFormField<?> formField;
				Object valueOf = property.adapter.valueOf(node);
				final ITextElement<?> valueElement;
				if (property.type.clazz.equals(String.class)) {
					if (property.type.isLong) {
						formField = form.addTextArea(property.name);
						valueElement = formField.valueElement();
					} else if (property.type.values.size() > 0) {
						formField = form.addComboBox(property.name);
						IComboBox cb = (IComboBox) formField.valueElement();
						for (Object s : property.type.values)
							cb.addText((String) s);
						valueElement = formField.valueElement();
					} else {
						formField = form.addTextField(property.name);
						valueElement = formField.valueElement();
					}
					String value = valueOf == null ? ""
							: (valueOf instanceof String ? (String) valueOf
									: String.valueOf(valueOf));
					valueElement.text(value);
					updates.add(new Runnable() {
						@Override
						public void run() {
							property.adapter.valueOf(node, valueElement.text());
						}
					});
				} else if (property.type.clazz.equals(Date.class)) {
					formField = form.addTextField(property.name);
					formField.type().date();
					String value = DetailView.DATE_FORMAT
							.format((Date) valueOf);
					formField.valueElement().text(value);
					updates.add(new Runnable() {
						@Override
						public void run() {
							Date value = null;
							String text = formField.valueElement().text()
									.trim();
							if (!text.equals("")) {
								value = DetailView.DATE_FORMAT.parse(text);
							}
							property.adapter.valueOf(node, value);
						}
					});
				} else if (property.type.clazz.equals(Boolean.class)) {
					formField = form.addCheckBox(property.name);
					ICheckBox checkBox = (ICheckBox) formField.valueElement();
					checkBox.checked((Boolean) valueOf);
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
					// TODO long ...
					formField.type().integer();
					String value = ((Number) valueOf).toString();
					formField.valueElement().text(value);
					updates.add(new Runnable() {
						@Override
						public void run() {
							Object value = null;
							String text = formField.valueElement().text()
									.trim();
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
					throw new MethodNotImplementedException(property.type.clazz);
				if (property.required)
					formField.required();
			}
		}
		form.visible(true);
	}
}