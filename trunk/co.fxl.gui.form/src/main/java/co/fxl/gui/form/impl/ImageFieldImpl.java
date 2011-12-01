package co.fxl.gui.form.impl;

import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.form.api.IImageField;
import co.fxl.gui.form.impl.FormWidgetImpl.FormEntryLabel;

class ImageFieldImpl implements IImageField {

	private IImage image;
	private IGridCell cell;
	private ILabel label;

	ImageFieldImpl(FormWidgetImpl widget, int index, String name) {
		FormEntryLabel formEntryLabel = widget.addFormEntryLabel(name, index);
		createLabelColumn(widget, index, formEntryLabel);
	}

	void createLabelColumn(FormWidgetImpl widget, int index,
			FormEntryLabel formEntryLabel) {
		cell = formEntryLabel.cell;
		label = formEntryLabel.formEntryLabel;
		image = widget.addImage(index);
	}

	@Override
	public IImage valueElement() {
		return image;
	}

	@Override
	public IGridCell cell() {
		return cell;
	}

	@Override
	public ILabel titleElement() {
		return label;
	}
}
