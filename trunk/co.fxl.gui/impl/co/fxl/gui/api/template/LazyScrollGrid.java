package co.fxl.gui.api.template;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IScrollListener;
import co.fxl.gui.api.IScrollPane;

public class LazyScrollGrid implements IScrollListener, IScrollGrid {

	private static final int RESIZE_INTERVALL = 50;
	private static final int INC = 100;
	private int height = 100;
	private int maxColumns;
	private int maxRows;
	private IScrollPane scrollPanel;
	private int paintedRows = 0;
	private IGridPanel grid;
	private boolean painting = false;
	private int scrollPosition = 0;
	private int offsetHeight = 0;
	private IContainer container;
	private ILazyGridDecorator decorator;

	public LazyScrollGrid(IContainer container) {
		this.container = container;
	}

	@Override
	public LazyScrollGrid columns(int columns) {
		maxColumns = columns;
		return this;
	}

	@Override
	public LazyScrollGrid rows(int rows) {
		maxRows = rows;
		return this;
	}

	@Override
	public LazyScrollGrid height(int height) {
		this.height = height;
		return this;
	}

	@Override
	public LazyScrollGrid decorator(ILazyGridDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public LazyScrollGrid visible(boolean visible) {
		if (visible) {
			scrollPanel = container.scrollPane();
			grid = scrollPanel.viewPort().panel().grid();
			scrollPanel.height(height);
			grid.spacing(0);
			grid.indent(0);
			grid.prepare(maxColumns, Math.min(maxRows, RESIZE_INTERVALL));
			scrollPanel.addScrollListener(this);
			onScroll(0);
			return this;
		} else
			throw new MethodNotImplementedException();
	}

	@Override
	public void onScroll(int max) {
		scrollPosition = Math.max(scrollPosition, max + height + INC);
		if (painting)
			return;
		painting = true;
		for (; paintedRows < maxRows && offsetHeight < scrollPosition; paintedRows++) {
			int inc = 0;
			resize();
			for (int column = 0; column < maxColumns; column++) {
				IElement<?> e = decorator.decorate(column, paintedRows,
						grid.cell(column, paintedRows));
				inc = Math.max(inc, e.height());
			}
			offsetHeight += inc;
		}
		painting = false;
	}

	private void resize() {
		if (grid.rows() > paintedRows)
			return;
		grid.prepare(maxColumns,
				Math.min(paintedRows + RESIZE_INTERVALL, maxRows));
	}
}
