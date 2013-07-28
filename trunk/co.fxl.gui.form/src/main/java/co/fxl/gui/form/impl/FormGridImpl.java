package co.fxl.gui.form.impl;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IGridPanel.IGridRow;
import co.fxl.gui.api.IVerticalPanel;

class FormGridImpl implements FormGrid {

	private IGridPanel grid;

	FormGridImpl(FormWidgetImpl widget, IVerticalPanel content) {
		grid = content.add().panel().grid();
		grid.indent(1);
		grid.spacing(1);
		grid.resize(2, 1);
		grid.column(0).width(120);
		widget.decorate(grid);
	}

	@Override
	public IGridCell label(int row) {
		return grid.cell(0, row);
	}

	@Override
	public IGridCell value(int row) {
		return grid.cell(1, row);
	}

	private IGridRow row(int row) {
		return grid.row(row);
	}

	@Override
	public IGridPanel grid(int row) {
		return grid;
	}

	@Override
	public void removeRow(int index) {
		row(index).remove();
	}

	@Override
	public void insertRow(int index) {
		row(index).insert();
	}

}
