package co.fxl.gui.form.api;

import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;

public interface IImageField {

	IGridCell cell();

	ILabel titleElement();

	IImage valueElement();
}
