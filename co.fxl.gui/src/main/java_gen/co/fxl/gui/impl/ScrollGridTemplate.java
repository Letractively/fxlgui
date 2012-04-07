package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IScrollPane;

import java.util.HashMap;
import java.util.Map;


abstract class ScrollGridTemplate implements IScrollGrid {
    int height = 600;
    int maxColumns;
    int maxRows;
    IScrollPane scrollPanel;
    IGridPanel grid;
    IScrollGrid.ILazyGridDecorator decorator;
    int spacing = 0;
    int indent = 0;
    Map<co.fxl.gui.api.IGridPanel.IGridClickListener, java.lang.String> listeners =
        new HashMap<co.fxl.gui.api.IGridPanel.IGridClickListener, java.lang.String>();

    public ScrollGridTemplate(IContainer container) {
        scrollPanel = container.scrollPane();
    }

    @Override
    public IScrollGrid columns(int columns) {
        maxColumns = columns;

        return this;
    }

    @Override
    public IScrollGrid rows(int rows) {
        maxRows = rows;

        return this;
    }

    @Override
    public IScrollGrid height(int height) {
        if ((scrollPanel) != null) {
            scrollPanel.height(height);
        }

        this.height = height;

        return this;
    }

    @Override
    public IScrollGrid decorator(IScrollGrid.ILazyGridDecorator decorator) {
        this.decorator = decorator;

        return this;
    }

    @Override
    public IScrollGrid visible(boolean visible) {
        if (visible) {
            setUp();

            return this;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    abstract void setUp();

    @Override
    public IScrollGrid spacing(int spacing) {
        this.spacing = spacing;

        return this;
    }

    @Override
    public IScrollGrid indent(int indent) {
        this.indent = indent;

        return this;
    }

    @Override
    public IScrollGrid addGridClickListener(
        IGridPanel.IGridClickListener listener, String key) {
        listeners.put(listener, key);

        return this;
    }

    @Override
    public int offsetY() {
        return scrollPanel.offsetY();
    }
}
