package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IScrollPane;

import java.util.Set;


public class ScrollGridImpl extends ScrollGridTemplate {
    public ScrollGridImpl(IContainer container) {
        super(container);
    }

    @Override
    void setUp() {
        grid = scrollPanel.viewPort().panel().grid();
        decorator.decorate(grid);
        scrollPanel.height(height);
        grid.spacing(spacing);
        grid.indent(indent);
        grid.resize(maxColumns, maxRows);

        for (int row = 0; row < (maxRows); row++) {
            for (int column = 0; column < (maxColumns); column++) {
                decorator.decorate(column, row, grid.cell(column, row));
            }
        }

        for (IGridPanel.IGridClickListener l : listeners.keySet()) {
            String key = listeners.get(l);
            IClickable.IKey<co.fxl.gui.api.IGridPanel> keyCB = grid.addGridClickListener(l);

            if (IScrollGrid.SHIFT.equals(key)) {
                keyCB.shiftPressed();
            } else if (IScrollGrid.CTRL.equals(key)) {
                keyCB.ctrlPressed();
            }
        }
    }
}
