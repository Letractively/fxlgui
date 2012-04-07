package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;


public class ElementDecorator<T> implements IElement<T> {
    private IElement<T> element;

    ElementDecorator(IElement<T> element) {
        this.element = element;
    }

    @Override
    public int offsetX() {
        return element.offsetX();
    }

    @Override
    public int offsetY() {
        return element.offsetY();
    }

    @Override
    public int width() {
        return element.width();
    }

    @Override
    public int height() {
        return element.height();
    }

    @Override
    public T offset(int x, int y) {
        return element.offset(x, y);
    }

    @Override
    public T width(int width) {
        return element.width(width);
    }

    @Override
    public T height(int height) {
        return element.height(height);
    }

    @Override
    public T size(int width, int height) {
        return element.size(width, height);
    }

    @Override
    public T visible(boolean visible) {
        return element.visible(visible);
    }

    @Override
    public boolean visible() {
        return element.visible();
    }

    @Override
    public void remove() {
        remove();
    }

    @Override
    public T tooltip(String tooltip) {
        return element.tooltip(tooltip);
    }

    @Override
    public IDisplay display() {
        return element.display();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public <N> N nativeElement() {
        return ((N) (element.nativeElement()));
    }

    @Override
    public IPadding padding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T padding(int padding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IMargin margin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T margin(int margin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T opacity(double opacity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <N> T nativeElement(N nativeElement) {
        throw new UnsupportedOperationException();
    }
}
