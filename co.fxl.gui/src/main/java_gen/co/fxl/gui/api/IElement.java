package co.fxl.gui.api;

public interface IElement<T> extends ILocated<T> {
    T visible(boolean visible);

    boolean visible();

    void remove();

    T tooltip(String tooltip);

    IDisplay display();

    IPadding padding();

    T padding(int padding);

    IMargin margin();

    T margin(int margin);

    T opacity(double opacity);

    <N> N nativeElement();

    <N> T nativeElement(N nativeElement);
}
