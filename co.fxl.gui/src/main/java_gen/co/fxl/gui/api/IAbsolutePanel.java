package co.fxl.gui.api;

public interface IAbsolutePanel extends IPanel<co.fxl.gui.api.IAbsolutePanel> {
    IAbsolutePanel addResizeListener(IResizeListener listener);

    IAbsolutePanel offset(int x, int y);

    IAbsolutePanel offset(IElement<?> element, int x, int y);

    public interface IResizeListener {
        void onResize(int width, int height);

        void onResize(IElement<?> element, int width, int height);
    }
}
