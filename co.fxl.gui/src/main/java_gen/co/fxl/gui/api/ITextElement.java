package co.fxl.gui.api;

public interface ITextElement<T> extends IElement<T>, IFontElement {
    T text(String text);

    T tooltip(String text);

    String text();
}
