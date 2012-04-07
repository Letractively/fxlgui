package co.fxl.gui.api;

public interface IDisplay extends IColored, IRegistry<co.fxl.gui.api.IDisplay> {
    IDisplay title(String title);

    IDisplay width(int width);

    IDisplay height(int height);

    IDisplay size(int width, int height);

    IDisplay visible(boolean visible);

    IContainer container();

    IDisplay fullscreen();

    IDialog showDialog();

    IWebsite showWebsite();

    IPopUp showPopUp();

    IDisplay addExceptionHandler(IExceptionHandler handler);

    IDisplay addResizeListener(IResizeListener listener);

    IDisplay removeResizeListener(IResizeListener listener);

    int width();

    int height();

    ICursor cursor();

    IDisplay block(boolean waiting);

    IDisplay invokeLater(Runnable runnable);

    IDisplay invokeLater(Runnable runnable, int ms);

    String title();

    IDisplay clear();

    public interface IResizeListener {
        boolean onResize(int width, int height);
    }

    public interface IExceptionHandler {
        void onException(Throwable exception);
    }
}
