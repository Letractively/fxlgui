package co.fxl.gui.api;

public interface ICheckBox extends IEditable<co.fxl.gui.api.ICheckBox>,
    IFocusable<co.fxl.gui.api.ICheckBox>, IKeyRecipient<co.fxl.gui.api.ICheckBox>,
    ITextElement<co.fxl.gui.api.ICheckBox>, IUpdateable<java.lang.Boolean> {
    ICheckBox checked(boolean checked);

    boolean checked();
}
