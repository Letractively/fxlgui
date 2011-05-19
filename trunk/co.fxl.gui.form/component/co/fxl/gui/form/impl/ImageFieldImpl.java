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

	ImageFieldImpl(FormWidgetImpl widget, String name) {
		FormEntryLabel formEntryLabel = widget.addFormEntryLabel(name,
				widget.gridIndex);
		cell = formEntryLabel.cell;
		label = formEntryLabel.formEntryLabel;
		image = widget.addImage(widget.gridIndex);
		widget.addFillColumn();
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
