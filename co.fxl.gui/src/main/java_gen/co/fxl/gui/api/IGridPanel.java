package co.fxl.gui.api;

public interface IGridPanel extends IPanel<co.fxl.gui.api.IGridPanel> {
    int columns();

    int rows();

    IGridPanel spacing(int pixel);

    IGridPanel indent(int pixel);

    IGridCell cell(int column, int row);

    IClickable.IKey<co.fxl.gui.api.IGridPanel> addGridClickListener(
        IGridClickListener listener);

    IGridPanel resize(int columns, int rows);

    IBordered.IBorder cellBorder();

    IGridRow row(int row);

    IGridColumn column(int column);

    public interface IGridColumn {
        IGridColumn expand();

        IGridColumn width(int width);

        IGridColumn width(double width);
    }

    public interface IGridRow {
        int height();

        IGridPanel remove();

        IGridPanel insert();
    }

    public interface IGridClickListener {
        void onClick(int column, int row);
    }

    interface IGridCell extends IContainer {
        IElement<?> element();

        IGridCell width(int width);

        IGridCell height(int height);

        int height();

        int width();

        IColored.IColor color();

        IGridCell visible(boolean visible);

        IAlignment<co.fxl.gui.api.IGridPanel.IGridCell> align();

        IAlignment<co.fxl.gui.api.IGridPanel.IGridCell> valign();

        IGridCell clear();

        IBordered.IBorder border();
    }
}
