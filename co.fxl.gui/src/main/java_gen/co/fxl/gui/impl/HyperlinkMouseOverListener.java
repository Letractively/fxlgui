package co.fxl.gui.impl;

import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement;


public class HyperlinkMouseOverListener implements IMouseOverElement.IMouseOverListener {
    ILabel label;

    public HyperlinkMouseOverListener(ILabel label) {
        this.label = label;
        label.addMouseOverListener(this);
    }

    @Override
    public void onMouseOver() {
        if (label.clickable()) {
            label.font().underline(true);
        }
    }

    @Override
    public void onMouseOut() {
        label.font().underline(false);
    }
}
