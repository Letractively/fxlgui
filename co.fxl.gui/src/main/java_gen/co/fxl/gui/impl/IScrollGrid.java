package co.fxl.gui.impl;

import co.fxl.gui.api.IGridPanel;


public interface IScrollGrid {
    String CTRL = "CTRL";
    String SHIFT = "SHIFT";

    IScrollGrid columns(int columns);

    IScrollGrid rows(int rows);

    IScrollGrid height(int height);

    IScrollGrid decorator(ILazyGridDecorator decorator);

    IScrollGrid visible(boolean visible);

    IScrollGrid spacing(int spacing);

    IScrollGrid indent(int indent);

    IScrollGrid addGridClickListener(IGridPanel.IGridClickListener listener,
        String key);

    int offsetY();

    public interface ILazyGridDecorator {
        void decorate(IGridPanel grid);

        void decorate(int column, int row, IGridPanel.IGridCell cell);
    }
}
