package co.fxl.gui.impl;

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.ILabel;


public class HyperlinkDecorator extends HyperlinkMouseOverListener {
    public HyperlinkDecorator(ILabel label) {
        super(label);
        styleHyperlinkActive(label);
    }

    private void styleHyperlinkActive(ILabel label) {
        label.font().color().rgb(0, 87, 141);
    }

    private void styleHyperlinkInactive(ILabel label) {
        label.font().color().gray();
    }

    public HyperlinkDecorator clickable(boolean enable) {
        if (enable) {
            styleHyperlinkActive(label);
        } else {
            styleHyperlinkInactive(label);
        }

        return this;
    }
}
