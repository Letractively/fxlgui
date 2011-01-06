package co.fxl.gui.api.template;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IScrollPane;

public class ScrollGridImpl implements IScrollGrid {

	private int height = 100;
	private int maxColumns;
	private int maxRows;
	private IScrollPane scrollPanel;
	private IGridPanel grid;
	private IContainer container;
	private ILazyGridDecorator decorator;

	public ScrollGridImpl(IContainer container) {
		this.container = container;
	}

	@Override
	public ScrollGridImpl columns(int columns) {
		maxColumns = columns;
		return this;
	}

	@Override
	public ScrollGridImpl rows(int rows) {
		maxRows = rows;
		return this;
	}

	@Override
	public ScrollGridImpl height(int height) {
		this.height = height;
		return this;
	}

	@Override
	public ScrollGridImpl decorator(ILazyGridDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public ScrollGridImpl visible(boolean visible) {
		if (visible) {
			scrollPanel = container.scrollPane();
			grid = scrollPanel.viewPort().panel().grid();
			scrollPanel.height(height);
			grid.spacing(0);
			grid.indent(0);
			grid.prepare(maxColumns, maxRows);
			for (int row = 0; row < maxRows; row++) {
				for (int column = 0; column < maxColumns; column++) {
					decorator.decorate(column, row, grid.cell(column, row));
				}
			}
			return this;
		} else
			throw new MethodNotImplementedException();
	}
}
