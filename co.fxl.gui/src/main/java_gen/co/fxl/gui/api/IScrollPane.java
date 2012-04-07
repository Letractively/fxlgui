package co.fxl.gui.api;

public interface IScrollPane extends IBordered, IColored,
    IElement<co.fxl.gui.api.IScrollPane> {
    IScrollPane horizontal();

    IContainer viewPort();

    IScrollPane addScrollListener(IScrollListener listener);

    IScrollPane showScrollbarsAlways(boolean showScrollbarsAlways);

    IScrollPane scrollTo(int pos);

    IScrollPane scrollIntoView(IElement<?> element);

    int scrollOffset();

    public interface IScrollListener {
        void onScroll(int maxOffset);
    }
}
