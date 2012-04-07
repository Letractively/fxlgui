package co.fxl.gui.layout.api;

import co.fxl.gui.api.IGridPanel;


public interface IFormLayout {
    IGridPanel.IGridCell middleCell(IGridPanel grid, int row, int column);

    IGridPanel.IGridCell outerCell(IGridPanel grid, int row);

    IGridPanel.IGridCell cell(IGridPanel.IGridCell cell);

    IFormLayout grid(IGridPanel grid);
}
