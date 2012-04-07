package co.fxl.gui.api;

public interface ILabel extends IClickable<co.fxl.gui.api.ILabel>,
    IDraggable<co.fxl.gui.api.ILabel>, IDropTarget<co.fxl.gui.api.ILabel>,
    IMouseOverElement<co.fxl.gui.api.ILabel>, ITextElement<co.fxl.gui.api.ILabel> {
    ILabel html(String html);

    ILabel autoWrap(boolean autoWrap);

    ILabel hyperlink();
}
