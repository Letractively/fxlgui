package co.fxl.gui.api;

public interface IPopUp extends IBordered, ILocated<co.fxl.gui.api.IPopUp> {
    IPopUp modal(boolean modal);

    IContainer container();

    IPopUp visible(boolean visible);

    IPopUp center();

    IPopUp atLastClick();

    IPopUp autoHide(boolean autoHide);

    IPopUp fitInScreen(boolean fitInScreen);

    IPopUp addVisibleListener(IUpdateable.IUpdateListener<java.lang.Boolean> l);

    boolean visible();

    IPopUp glass(boolean glass);
}
