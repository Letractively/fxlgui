package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.ISpaced;
import co.fxl.gui.api.IVerticalPanel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ToolbarImpl implements IToolbar {
    public static boolean ADJUST_HEIGHTS = false;
    private static int SPACING = 4;
    public static boolean ALLOW_ALIGN_END_FOR_FLOW_PANEL = true;
    private IFlowPanel panel;
    private ToolbarImpl parent;
    private Map<java.lang.Object, co.fxl.gui.api.IHorizontalPanel> blockers = new HashMap<java.lang.Object, co.fxl.gui.api.IHorizontalPanel>();
    private List<java.lang.Object> content = new LinkedList<java.lang.Object>();
    private IHorizontalPanel blocker;
    private Map<java.lang.Object, co.fxl.gui.api.IVerticalPanel> mainPanels = new HashMap<java.lang.Object, co.fxl.gui.api.IVerticalPanel>();
    private boolean hasContent = false;
    private int spacing = ToolbarImpl.SPACING;

    public ToolbarImpl(IContainer container) {
        panel = container.panel().flow();
    }

    private ToolbarImpl(ToolbarImpl parent) {
        this.parent = parent;
    }

    ToolbarImpl root() {
        if ((parent) != null) {
            return parent.root();
        }

        return this;
    }

    @Override
    public IContainer add() {
        ToolbarImpl root = root();

        if (ToolbarImpl.ADJUST_HEIGHTS) {
            IVerticalPanel childPanel0 = root.panel.add().panel().vertical();
            blocker = childPanel0.add().panel().horizontal();
            blocker.add().label().text("&#160;");

            IPanel<?> childPanel = childPanel0.add().panel().horizontal()
                                              .spacing(((ToolbarImpl.SPACING) / 2))
                                              .add().panel().horizontal().align()
                                              .center();
            blockers.put(childPanel, blocker);
            mainPanels.put(childPanel, childPanel0);
            content.add(childPanel);

            return childPanel.add();
        } else {
            IHorizontalPanel childPanel = root.panel.add().panel().horizontal()
                                                    .height(40).align().center();
            childPanel.spacing().left((root.hasContent ? 0 : spacing))
                      .top(spacing).bottom(spacing).right(spacing);
            childPanel = childPanel.add().panel().horizontal().align().center();
            content.add(childPanel);

            return childPanel.add();
        }
    }

    @Override
    public IToolbar addGroup() {
        ToolbarImpl toolbar = new ToolbarImpl(this);
        content.add(toolbar);

        return toolbar;
    }

    @Override
    public void clear() {
        for (Object o : content) {
            if (o instanceof ToolbarImpl) {
                ToolbarImpl toolbar = ((ToolbarImpl) (o));
                toolbar.clear();
            } else {
                IHorizontalPanel childPanel = ((IHorizontalPanel) (o));

                if (ToolbarImpl.ADJUST_HEIGHTS) {
                    mainPanels.get(childPanel).remove();
                } else {
                    childPanel.remove();
                }
            }
        }

        blockers.clear();
        mainPanels.clear();
        content.clear();
    }

    @Override
    public IToolbar visible(boolean visible) {
        for (Object o : content) {
            if (o instanceof ToolbarImpl) {
                ToolbarImpl toolbar = ((ToolbarImpl) (o));
                toolbar.visible(visible);
            } else {
                IHorizontalPanel childPanel = ((IHorizontalPanel) (o));

                if (ToolbarImpl.ADJUST_HEIGHTS) {
                    mainPanels.get(childPanel).visible(visible);
                } else {
                    childPanel.visible(visible);
                }
            }
        }

        return this;
    }

    @Override
    public IToolbar adjustHeights() {
        if (ToolbarImpl.ADJUST_HEIGHTS) {
            for (Object o : content) {
                if (o instanceof ToolbarImpl) {
                    ToolbarImpl toolbar = ((ToolbarImpl) (o));
                    toolbar.adjustHeights();
                } else {
                    IVerticalPanel childPanel = ((IVerticalPanel) (mainPanels.get(o)));

                    if ((childPanel.height()) < 24) {
                        IHorizontalPanel blocker = ((IHorizontalPanel) (blockers.get(o)));
                        int inc = (24 - (childPanel.height())) / 2;
                        blocker.height(inc);
                    }
                }
            }
        }

        return this;
    }

    @Override
    public IAlignment<co.fxl.gui.impl.IToolbar> align() {
        return new IAlignment<co.fxl.gui.impl.IToolbar>() {
                @Override
                public ToolbarImpl begin() {
                    panel.align().begin();

                    return ToolbarImpl.this;
                }

                @Override
                public ToolbarImpl center() {
                    panel.align().center();

                    return ToolbarImpl.this;
                }

                @Override
                public ToolbarImpl end() {
                    panel.align().end();

                    return ToolbarImpl.this;
                }
            };
    }
}
