package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;


public class DynamicHeightGrid implements IScrollGrid {
    private IGridPanel grid;
    private int columns;
    private int rows;
    private IScrollGrid.ILazyGridDecorator decorator;

    public DynamicHeightGrid(IContainer container) {
        grid = container.panel().grid();
    }

    @Override
    public IScrollGrid columns(int columns) {
        this.columns = columns;

        return this;
    }

    @Override
    public IScrollGrid rows(int rows) {
        this.rows = rows;

        return this;
    }

    @Override
    public IScrollGrid height(int height) {
        return this;
    }

    @Override
    public IScrollGrid decorator(IScrollGrid.ILazyGridDecorator decorator) {
        this.decorator = decorator;

        return this;
    }

    @Override
    public IScrollGrid visible(boolean visible) {
        decorator.decorate(grid);

        for (int y = 0; y < (rows); y++) {
            for (int x = 0; x < (columns); x++) {
                decorator.decorate(x, y, grid.cell(x, y));
            }
        }

        return this;
    }

    @Override
    public IScrollGrid spacing(int spacing) {
        grid.spacing(spacing);

        return this;
    }

    @Override
    public IScrollGrid indent(int indent) {
        grid.indent(indent);

        return this;
    }

    @Override
    public IScrollGrid addGridClickListener(
        IGridPanel.IGridClickListener listener, String key) {
        IClickable.IKey<co.fxl.gui.api.IGridPanel> k = grid.addGridClickListener(listener);

        if ((key != null) && (key.equals(IScrollGrid.SHIFT))) {
            k.shiftPressed();
        }

        if ((key != null) && (key.equals(IScrollGrid.CTRL))) {
            k.ctrlPressed();
        }

        return this;
    }

    @Override
    public int offsetY() {
        return grid.offsetY();
    }
}
