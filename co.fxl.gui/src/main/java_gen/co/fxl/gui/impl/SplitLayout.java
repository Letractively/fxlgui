package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISpaced;
import co.fxl.gui.api.IVerticalPanel;


public class SplitLayout implements IDisplay.IResizeListener {
    public static int SCROLLBAR_WIDTH = 20;
    public static int WIDTH_SIDE_PANEL = 320;
    private ILayout layout;
    IGridPanel panel;
    public IVerticalPanel mainPanel;
    public IVerticalPanel sidePanel;
    IGridPanel.IGridCell cell1;
    private IScrollPane sideScrollPanel;
    IGridPanel.IGridCell cell0;
    private boolean resizeMainPanel;
    private IVerticalPanel sideBasePanel;
    private boolean isDetached;

    public SplitLayout(ILayout layout) {
        this(layout, false);
    }

    public SplitLayout(ILayout layout, boolean resizeMainPanel) {
        this.layout = layout;
        this.resizeMainPanel = resizeMainPanel;
        init();
    }

    private void init() {
        panel = layout.grid();
        cell0 = panel.cell(0, 0)
                     .width((((panel.width()) - (SplitLayout.WIDTH_SIDE_PANEL)) -
                (3 * 10)));

        IVerticalPanel vpanel = cell0.valign().begin().panel().vertical()
                                     .spacing(10);
        mainPanel = addMainPanel(vpanel);
        cell1 = panel.cell(1, 0).width(SplitLayout.WIDTH_SIDE_PANEL).valign()
                     .begin().align().end();
        sideBasePanel = cell1.panel().vertical();
        sideScrollPanel = sideBasePanel.addSpace(10).add().scrollPane();
        sidePanel = sideScrollPanel.viewPort().panel().vertical();
        sidePanel.spacing().right(10).inner(10);
        co.fxl.gui.impl.SidePanelResizeListener.setup(panel.display(), this);
        onResize(panel.display().width(), panel.display().height());
    }

    @Override
    public boolean onResize(int width, int height) {
        if (!(panel.visible())) {
            return false;
        }

        resizeSidePanel(height);
        cell0.width((width - (SplitLayout.WIDTH_SIDE_PANEL)));

        return true;
    }

    public void detachSidePanel() {
        if (isDetached) {
            return;
        }

        sideBasePanel.remove();
        isDetached = true;
    }

    public void attachSidePanel() {
        if (!(isDetached)) {
            return;
        }

        cell1.element(sideBasePanel);
        isDetached = false;
    }

    private void resizeSidePanel(int height) {
        int offsetY = sideScrollPanel.offsetY();

        if (offsetY < 68) {
            offsetY = 68;
        }

        int maxFromDisplay = (height - offsetY) - 10;

        if (maxFromDisplay > 0) {
            if (resizeMainPanel) {
                mainPanel.height(maxFromDisplay);
            }

            sideScrollPanel.height(maxFromDisplay);
        }
    }

    protected IVerticalPanel addMainPanel(IVerticalPanel vpanel) {
        mainPanel = vpanel.add().panel().vertical();
        mainPanel.color().white();

        return mainPanel;
    }

    public void reset() {
        panel.remove();
        init();
    }

    public void showSplit(boolean showSplit) {
    }
}
