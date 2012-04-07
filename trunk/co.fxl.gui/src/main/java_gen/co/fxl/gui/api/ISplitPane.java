package co.fxl.gui.api;

public interface ISplitPane extends IBordered,
    IElement<co.fxl.gui.api.ISplitPane> {
    ISplitPane vertical();

    IContainer first();

    IContainer second();

    ISplitPane splitPosition(int pixel);

    ISplitPane addResizeListener(ISplitPaneResizeListener l);

    int splitPosition();

    public interface ISplitPaneResizeListener {
        void onResize(int splitPosition);
    }
}
