package co.fxl.gui.api;

public interface IComboBox extends IBordered, IColored,
    IEditable<co.fxl.gui.api.IComboBox>, IFocusable<co.fxl.gui.api.IComboBox>,
    IKeyRecipient<co.fxl.gui.api.IComboBox>,
    ITextElement<co.fxl.gui.api.IComboBox>, IUpdateable<java.lang.String> {
    IComboBox clear();

    IComboBox addNull();

    IComboBox addText(String... texts);
}
