package co.fxl.gui.impl;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;


public class FlipPage {
    private ICardPanel cardPanel;
    private IVerticalPanel page1;
    private IVerticalPanel page2;
    private IVerticalPanel active;

    public FlipPage(IContainer c) {
        cardPanel = c.panel().card();
        page1 = cardPanel.add().panel().vertical();
        page2 = cardPanel.add().panel().vertical();
        active = page2;
        cardPanel.show(page1);
    }

    public IContainer next() {
        if ((active) == (page2)) {
            return page1.clear().add();
        } else {
            return page2.clear().add();
        }
    }

    public void flip() {
        IVerticalPanel inactive = active;

        if ((active) == (page2)) {
            active = page1;
        } else {
            active = page2;
        }

        cardPanel.show(active);
        inactive.clear();
    }

    public void width(int width) {
        cardPanel.width(width);
        page1.width(width);
        page2.width(width);
    }

    public void height(int height) {
        cardPanel.height(height);
        page1.height(height);
        page2.height(height);
    }
}
