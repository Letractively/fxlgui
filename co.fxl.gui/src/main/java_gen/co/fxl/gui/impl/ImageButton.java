package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

import java.util.LinkedList;
import java.util.List;


public class ImageButton implements IClickable<java.lang.Object>,
    IClickable.IClickListener {
    private static final int SPACE = 4;
    IImage image;
    private ILabel label;
    private IHorizontalPanel panel;
    private List<co.fxl.gui.api.ILabel> additionalLabels = new LinkedList<co.fxl.gui.api.ILabel>();
    private IVerticalPanel p0;
    private String addToContextMenu = null;
    private List<co.fxl.gui.api.IClickable.IClickListener> clickListeners = new LinkedList<co.fxl.gui.api.IClickable.IClickListener>();
    private ContextMenu.Entry entry;
    private boolean hasElements = false;

    public ImageButton(IContainer c) {
        this.panel = c.panel().horizontal();
        this.image = panel.add().image().size(16, 16);
        this.label = panel.addSpace(SPACE).add().label();
        clickable(true);
    }

    public ImageButton(IHorizontalPanel panel, IImage image, ILabel textLabel) {
        this(null, panel, image, textLabel);
    }

    public ImageButton(IVerticalPanel p0, IHorizontalPanel panel, IImage image,
        ILabel textLabel) {
        this.p0 = p0;
        this.panel = panel;
        this.image = image;
        this.label = textLabel;
        clickable(true);
    }

    public ImageButton text(String text) {
        label.text(text);

        if ((addToContextMenu) != null) {
            if ((entry) == null) {
                entry = ContextMenu.instance().group(addToContextMenu)
                                   .addEntry(text);
                entry.addClickListener(this);

                if ((image) != null) {
                    entry.imageResource(image.resource());
                }
            } else {
                entry.text(text);
            }
        }

        return this;
    }

    public ImageButton clickable(boolean clickable) {
        label.font().weight().plain();

        if ((image) != null) {
            image.clickable(clickable);
        }

        label.clickable(clickable);

        for (ILabel l : additionalLabels)
            l.clickable(clickable);

        if (!(hasElements)) {
            if ((p0) != null) {
                p0.clickable(clickable);
            } else {
                panel.clickable(clickable);
            }
        }

        if ((entry) != null) {
            entry.clickable(clickable);
        }

        return this;
    }

    @Override
    public boolean clickable() {
        return label.clickable();
    }

    public ImageButton hyperlink() {
        label.hyperlink();

        return this;
    }

    @Override
    public IClickable.IKey<java.lang.Object> addClickListener(
        IClickable.IClickListener clickListener) {
        label.addClickListener(clickListener);

        if ((image) != null) {
            image.addClickListener(clickListener);
        }

        if ((p0) != null) {
            p0.addClickListener(clickListener);
        } else {
            panel.addClickListener(clickListener);
        }

        clickListeners.add(clickListener);

        return null;
    }

    public ILabel addHyperlink(String text) {
        panel.addSpace(SPACE);

        ILabel label = panel.add().label().text("|");
        styleSeparator(label);

        ILabel l = panel.addSpace(4).add().label().text(text).hyperlink();
        additionalLabels.add(l);

        return l;
    }

    public void styleSeparator(ILabel label) {
        label.font().color().gray();
    }

    public ImageButton imageResource(String string) {
        image.resource(string);

        return this;
    }

    public ImageButton addToContextMenu(String group) {
        addToContextMenu = group;

        return this;
    }

    @Override
    public void onClick() {
        for (IClickable.IClickListener l : clickListeners)
            l.onClick();
    }

    public IContainer addElement() {
        return addElement(SPACE);
    }

    public IContainer addElement(int i) {
        hasElements = true;
        p0.clickable(false);
        panel.clickable(false);

        return panel.addSpace(i).add();
    }

    public ImageButton addSpace(int i) {
        panel.addSpace(i);

        return this;
    }

    public void highlight(boolean b) {
        if (label.clickable()) {
            return;
        }

        if (b) {
            label.font().weight().bold().color().black();
        } else {
            label.font().weight().plain().color().gray();
        }
    }

    public String text() {
        return label.text();
    }

    public ILabel label() {
        return label;
    }
}
