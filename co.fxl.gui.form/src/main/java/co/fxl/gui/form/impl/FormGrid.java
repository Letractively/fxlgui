package co.fxl.gui.form.impl;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;

interface FormGrid {

	IGridCell label(int row);

	IGridCell value(int row);

	IGridPanel grid(int row);

	void removeRow(int index);

	void insertRow(int index);

}
