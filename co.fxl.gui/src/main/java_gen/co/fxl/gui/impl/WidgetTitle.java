package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.ISpaced;
import co.fxl.gui.api.IVerticalPanel;

import java.util.LinkedList;
import java.util.List;


public class WidgetTitle implements IClickable.IClickListener {
    public static final int LARGE_FONT = 18;
    private static final String FOLDABLE = "Click to minimize/maximize";
    public IGridPanel panel;
    private IHorizontalPanel titlePanel;
    private IHorizontalPanel commandPanel;
    private boolean hasCommands = false;
    private boolean hasHeaderPanel = false;
    private IGridPanel headerPanel;
    private IContainer contentContainer;
    private boolean open = true;
    private boolean foldable = true;
    public ILabel headerLabel;
    private List<co.fxl.gui.api.ILabel> labels = new LinkedList<co.fxl.gui.api.ILabel>();
    private List<co.fxl.gui.api.IImage> images = new LinkedList<co.fxl.gui.api.IImage>();
    private int space = 10;
    private IContainer bottomContainer;
    private IHorizontalPanel commandPanelTop;
    private boolean commandsOnTop = false;
    private boolean addToContextMenu = false;
    private boolean sideWidget = false;
    private String title;
    private IGridPanel bPanel;
    private boolean hyperlinkVisible = true;
    private IDockPanel footer;
    private IImage more;
    private boolean addBorder;

    public WidgetTitle() {
    }

    public WidgetTitle(ILayout layout) {
        this(layout, false);
    }

    public WidgetTitle(ILayout layout, boolean addBorder) {
        panel = layout.grid();
        panel.color().white();
        this.addBorder = addBorder;
        setUp();
    }

    private void setUp() {
        headerPanel = panel.cell(0, 0).panel().grid();
        headerPanel.color().rgb(136, 136, 136).gradient().vertical()
                   .rgb(113, 113, 113);
        bPanel = panel.cell(0, 1).panel().grid();

        IBorder border = headerPanel.border();
        border.color().rgb(172, 197, 213);
        border.style().bottom();
        headerPanel.visible(false);

        if (addBorder) {
            panel.border().color().rgb(172, 197, 213);
        }
    }

    public void styleHeader(IPanel<?> headerPanel) {
        headerPanel.color().rgb(136, 136, 136).gradient().vertical()
                   .rgb(113, 113, 113);

        IBorder border = headerPanel.border();
        border.color().rgb(172, 197, 213);
        border.style().bottom();
    }

    public WidgetTitle space(int space) {
        this.space = space;

        return this;
    }

    public WidgetTitle triangleIcons() {
        return this;
    }

    public WidgetTitle foldable(boolean foldable) {
        this.foldable = foldable;

        return this;
    }

    public WidgetTitle commandsOnTop() {
        commandsOnTop = true;

        return this;
    }

    private void initHeader() {
        if (hasHeaderPanel) {
            return;
        }

        headerPanel.visible(true);

        IHorizontalPanel horizontal = headerPanel.cell(0, 0).align().begin()
                                                 .valign().center().panel()
                                                 .horizontal().align().begin();
        titlePanel = horizontal.add().panel().horizontal().align().begin();
        titlePanel.spacing().left(10).top(6).bottom(6).right(6);
        hasHeaderPanel = true;
    }

    IHorizontalPanel commandPanel() {
        setUpCommandPanel();

        if (commandsOnTop) {
            return commandPanelTop;
        } else {
            return commandPanel;
        }
    }

    void setUpCommandPanel() {
        if (commandsOnTop) {
            if ((commandPanelTop) != null) {
                return;
            }

            IContainer cell = headerPanel.cell(1, 0).align().end().valign()
                                         .center();
            commandPanelTop = cell.panel().horizontal().align().end().add()
                                  .panel().horizontal().align().end().spacing(4);
        } else {
            if ((commandPanel) != null) {
                return;
            }

            footer = bottom().panel().dock();
            footer.center().panel().vertical();

            IContainer cell = footer.right();
            commandPanel = cell.panel().horizontal().spacing(6);
            styleFooter(footer);
        }
    }

    public void styleFooter(IPanel<?> vertical) {
        vertical.color().rgb(249, 249, 249).gradient().vertical()
                .rgb(216, 216, 216);

        IBorder border2 = vertical.border();
        IColored.IColor c = border2.color();
        WidgetTitle.decorateBorder(c);
        border2.style().top();
    }

    public static void decorateBorder(IColored.IColor c) {
        c.rgb(172, 197, 213);
    }

    @Override
    public void onClick() {
        open = !(open);
        bPanel.visible(open);
        more.visible(!(open));
    }

    public ILabel addTitle(String title) {
        initHeader();

        String text = sideWidget ? title.toUpperCase() : title;
        ILabel label = titlePanel.add().label().text(text);
        more = titlePanel.add().image();

        String r = "more.png";
        more.resource(r);
        more.visible(false);
        styleHeaderTitleSide(label);
        headerLabel = label;

        if (foldable) {
            headerLabel.addClickListener(this);
            headerLabel.tooltip(FOLDABLE);
        }

        this.title = title;

        if (addToContextMenu) {
            ContextMenu.instance().group(title).clear();
        }

        return headerLabel;
    }

    public WidgetTitle addTitleSpace() {
        titlePanel.addSpace(10);

        return this;
    }

    public void styleHeaderTitleSide(ILabel label) {
        label.font().weight().bold().pixel(12).color().white();
    }

    public CommandLink addHyperlink(String text) {
        return addHyperlink(null, text);
    }

    private ILabel addHyperlinkLabel(String text, IHorizontalPanel iPanel) {
        return WidgetTitle.addHyperlinkLabel2Panel(text, iPanel);
    }

    public CommandLink addHyperlink(String imageResource, String text) {
        initHeader();

        IHorizontalPanel cp = commandPanel();

        if ((hasCommands) && (imageResource == null)) {
            ILabel label = addSeparator(cp);
            labels.add(label);
        }

        hasCommands = true;

        IHorizontalPanel iPanel0 = cp.add().panel().horizontal();
        IHorizontalPanel iPanel = iPanel0;

        if (commandsOnTop) {
            styleWindowHeaderButton(iPanel);
            iPanel.spacing(4);
            iPanel = iPanel.add().panel().horizontal();
        }

        IImage image = null;

        if (imageResource != null) {
            image = iPanel.add().image().resource(imageResource).size(16, 16);
            images.add(image);
        }

        if (text == null) {
            ILabel label = iPanel.add().label().visible(false);

            return createCommandLink(iPanel0, image, label);
        } else {
            ILabel label = addHyperlinkLabel(text, iPanel);

            return createCommandLink(iPanel0, image, label);
        }
    }

    private CommandLink createCommandLink(IHorizontalPanel iPanel0,
        IImage image, ILabel label) {
        labels.add(label);

        CommandLink cl = new CommandLink(this, iPanel0, image, label);
        cl.clickable(hyperlinkVisible);

        if (addToContextMenu) {
            cl.addToContextMenu(title);
        }

        return cl;
    }

    public ToggleImageButton addToggleButton(String imageResource, String text) {
        return new ToggleImageButton(addHyperlink(imageResource, text),
            imageResource, text);
    }

    public void styleWindowHeaderButton(IHorizontalPanel iPanel) {
        iPanel.color().rgb(248, 248, 248).gradient().vertical()
              .rgb(216, 216, 216);

        IBorder b = iPanel.border();
        b.color().rgb(172, 197, 213);
        b.style().rounded();
    }

    public IClickable<?> addHyperlink(String imageResource, String name,
        String toolTipClickable, String toolTipNotClickable) {
        return addHyperlink(imageResource, name)
                   .tooltips(toolTipClickable, toolTipNotClickable);
    }

    public static ILabel addHyperlinkLabel2Panel(String text,
        IHorizontalPanel iPanel) {
        ILabel l = iPanel.addSpace(4).add().label();
        ILabel label = l.text(text);
        label.font().pixel(12);

        return label;
    }

    public IImage addImage(String image) {
        initHeader();

        IHorizontalPanel cp = commandPanel();

        if (hasCommands) {
            addSeparator(cp);
        }

        hasCommands = true;

        IImage label = cp.add().image().resource(image);

        return label;
    }

    protected ILabel addSeparator(IHorizontalPanel cp) {
        ILabel label = cp.add().label().text("|");
        styleSeparator(label);

        return label;
    }

    public void styleSeparator(ILabel label) {
        label.font().color().gray();
    }

    public IContainer content() {
        if ((contentContainer) != null) {
            return contentContainer;
        }

        return contentContainer = ((space) == 0)
            ? bPanel.cell(0, 0).panel().vertical().add()
            : bPanel.cell(0, 0).panel().vertical().addSpace(space).add();
    }

    public IContainer bottom() {
        content();

        if ((bottomContainer) != null) {
            return bottomContainer;
        }

        return bottomContainer = ((space) == 0) ? bPanel.cell(0, 1)
                                                : bPanel.cell(0, 1).panel()
                                                        .vertical()
                                                        .addSpace(space).add();
    }

    public WidgetTitle clearHyperlinks() {
        commandPanel().clear();
        hasCommands = false;

        return this;
    }

    public WidgetTitle visible(boolean b) {
        panel.visible(b);

        return this;
    }

    public WidgetTitle addToContextMenu(boolean b) {
        addToContextMenu = b;

        return this;
    }

    public WidgetTitle sideWidget(boolean sideWidget) {
        this.sideWidget = sideWidget;

        return this;
    }

    public WidgetTitle hyperlinkVisible(boolean hyperlinkVisible) {
        this.hyperlinkVisible = hyperlinkVisible;

        return this;
    }

    public void adjustHeader() {
        if (((headerPanel) != null) && ((footer) != null)) {
            headerPanel.height(footer.height());
        }
    }
}
