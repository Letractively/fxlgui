package co.fxl.gui.api;

public interface IHyperlink extends IClickable<co.fxl.gui.api.IHyperlink> {
    IHyperlink text(String text);

    IHyperlink uRI(String uRI);

    IHyperlink localURI(String string);
}
