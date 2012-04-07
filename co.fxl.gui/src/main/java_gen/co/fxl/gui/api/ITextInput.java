package co.fxl.gui.api;

public interface ITextInput<T> extends IBordered, IColored, IEditable<T>,
    IFocusable<T>, ITextElement<T>, IUpdateable<java.lang.String> {
    T maxLength(int maxLength);
}
