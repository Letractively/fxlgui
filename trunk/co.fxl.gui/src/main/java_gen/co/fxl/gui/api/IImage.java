package co.fxl.gui.api;

public interface IImage extends IClickable<co.fxl.gui.api.IImage>,
    IElement<co.fxl.gui.api.IImage>, IMouseOverElement<co.fxl.gui.api.IImage> {
    IImage localURI(String uRI);

    IImage uRI(String uRI);

    IImage resource(String iD);

    String resource();

    IBordered.IBorder border();
}
