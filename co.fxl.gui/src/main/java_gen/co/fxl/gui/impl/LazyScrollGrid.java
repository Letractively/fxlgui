package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

import java.util.Set;


public class LazyScrollGrid extends ScrollGridTemplate implements IScrollPane.IScrollListener {
    private static final int RESIZE_INTERVALL = 50;
    private static final int INC = 400;
    private int paintedRows = 0;
    private boolean painting = false;
    private int scrollPosition;
    private int offsetHeight = 0;

    public LazyScrollGrid(IContainer container) {
        super(container);
    }

    @Override
    void setUp() {
        grid = scrollPanel.viewPort().panel().vertical().add().panel().grid();
        decorator.decorate(grid);
        scrollPanel.height(height);
        grid.spacing(0);
        grid.indent(0);
        grid.resize(maxColumns, java.lang.Math.min(maxRows, RESIZE_INTERVALL));
        scrollPanel.addScrollListener(this);
        grid.spacing(spacing);
        grid.indent(indent);

        for (IGridPanel.IGridClickListener l : listeners.keySet()) {
            String key = listeners.get(l);
            IClickable.IKey<co.fxl.gui.api.IGridPanel> keyCB = grid.addGridClickListener(l);

            if (IScrollGrid.SHIFT.equals(key)) {
                keyCB.shiftPressed();
            } else if (IScrollGrid.CTRL.equals(key)) {
                keyCB.ctrlPressed();
            }
        }

        onScroll(0);
    }

    @Override
    public void onScroll(int max) {
        scrollPosition = max;

        if (painting) {
            return;
        }

        painting = true;

        while (check()) {
            int inc = 16;
            resize();

            for (int column = 0; column < (maxColumns); column++) {
                IGridPanel.IGridCell cell = grid.cell(column, paintedRows);
                decorator.decorate(column, paintedRows, cell);
                inc = java.lang.Math.max(inc, cell.height());
            }

            offsetHeight += inc;
            (paintedRows)++;
        }

        painting = false;
    }

    private boolean check() {
        return ((paintedRows) < (maxRows)) &&
        ((offsetHeight) < (((scrollPosition) + (height)) + (INC)));
    }

    private void resize() {
        if ((grid.rows()) > (paintedRows)) {
            return;
        }

        grid.resize(maxColumns,
            java.lang.Math.min(((paintedRows) + (RESIZE_INTERVALL)), maxRows));
    }
}
