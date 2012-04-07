package co.fxl.gui.api;

public interface IPasswordField extends IBordered, IColored,
    IFocusable<co.fxl.gui.api.IPasswordField>,
    IKeyRecipient<co.fxl.gui.api.IPasswordField>,
    ITextElement<co.fxl.gui.api.IPasswordField>, IUpdateable<java.lang.String> {
    IPasswordField editable(boolean editable);

    IPasswordField columns(int rows);
}
