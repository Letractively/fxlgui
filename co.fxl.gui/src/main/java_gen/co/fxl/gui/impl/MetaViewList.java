package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILayout;

import java.util.LinkedList;
import java.util.List;


public class MetaViewList {
    private SplitLayout layout;
    List<co.fxl.gui.impl.ViewList> viewLists = new LinkedList<co.fxl.gui.impl.ViewList>();
    private FlipPage card;
    private FlipPage sideCard;

    public MetaViewList(ILayout layout) {
        this.layout = new SplitLayout(layout);
    }

    FlipPage contentPanel() {
        if ((card) == null) {
            card = new FlipPage(layout.mainPanel.add());
        }

        return card;
    }

    public FlipPage sideContentPanel() {
        if ((sideCard) == null) {
            sideCard = new FlipPage(layout.sidePanel.add());
        }

        return sideCard;
    }

    public ViewList addViewList() {
        ViewList viewList = new ViewList(this, layout.sidePanel.add().panel());
        viewLists.add(viewList);

        return viewList;
    }

    public void selectFirst() {
        viewLists.get(0).views.get(0).onClick();
    }
}
