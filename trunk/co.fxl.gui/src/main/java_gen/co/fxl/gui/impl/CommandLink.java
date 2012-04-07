package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;

import java.util.LinkedList;
import java.util.List;


public class CommandLink implements IClickable<co.fxl.gui.api.IClickable<?>> {
    final WidgetTitle widgetTitle;
    private IHorizontalPanel iPanel;
    private ILabel label;
    private IImage image;
    private String toolTipClickable = null;
    private String toolTipNotClickable = null;
    private List<co.fxl.gui.api.IClickable.IClickListener> clickListeners = new LinkedList<co.fxl.gui.api.IClickable.IClickListener>();
    private ContextMenu.Entry contextMenuEntry;
    private String text;
    private char ctrlKey;

    public CommandLink(WidgetTitle widgetTitle, IHorizontalPanel iPanel,
        IImage image, ILabel headerLabel) {
        this.widgetTitle = widgetTitle;
        this.iPanel = iPanel;
        noDoubleClicks(iPanel);
        this.image = image;

        if (image != null) {
            noDoubleClicks(image);
        }

        this.label = headerLabel;

        if (headerLabel != null) {
            text = headerLabel.text();
        }

        noDoubleClicks(label);
    }

    private void noDoubleClicks(IClickable<?> c) {
        if (c != null) {
            c.addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                    }
                }).doubleClick();
        }
    }

    public CommandLink label(String l) {
        this.text = l;

        if ((label) != null) {
            label.text(l);
        }

        return this;
    }

    @Override
    public IClickable<?> clickable(boolean clickable) {
        if ((label) != null) {
            label.clickable(clickable);
        }

        if ((image) != null) {
            image.clickable(clickable);
        }

        iPanel.clickable(clickable);

        String tooltip = clickable ? toolTipClickable : toolTipNotClickable;

        if (tooltip != null) {
            iPanel.tooltip(tooltip);

            if ((image) != null) {
                image.tooltip(tooltip);
            }

            label.tooltip(tooltip);
        }

        styleDialogButton(label);

        if ((contextMenuEntry) != null) {
            contextMenuEntry.clickable(clickable);
        }

        return this;
    }

    public void styleDialogButton(ILabel label) {
        if (label == null) {
            return;
        }

        if (label.clickable()) {
            label.font().color().black();
        } else {
            label.font().color().gray();
        }
    }

    @Override
    public boolean clickable() {
        if ((label) != null) {
            return label.clickable();
        } else {
            return image.clickable();
        }
    }

    @Override
    public IClickable.IKey<co.fxl.gui.api.IClickable<?>> addClickListener(
        IClickable.IClickListener clickListener) {
        label.addClickListener(clickListener);

        if ((image) != null) {
            image.addClickListener(clickListener);
        }

        iPanel.addClickListener(clickListener);
        clickListeners.add(clickListener);

        return null;
    }

    public IClickable<?> tooltips(String toolTipClickable,
        String toolTipNotClickable) {
        this.toolTipClickable = toolTipClickable;
        this.toolTipNotClickable = toolTipNotClickable;

        return this;
    }

    public CommandLink addToContextMenu(String group) {
        contextMenuEntry = ContextMenu.instance().group(group).addEntry(text);

        if ((ctrlKey) != 0) {
            contextMenuEntry.ctrlKey(ctrlKey);
        }

        if ((image) != null) {
            String resource = image.resource();
            contextMenuEntry.imageResource(resource);
        }

        contextMenuEntry.addClickListener(new IClickable.IClickListener() {
                @Override
                public void onClick() {
                    for (IClickable.IClickListener l : clickListeners)
                        l.onClick();
                }
            });

        return this;
    }

    public void text(String string) {
        label.text(string);

        if ((contextMenuEntry) != null) {
            contextMenuEntry.text(string);
        }
    }

    public void image(String string) {
        image.resource(string);

        if ((contextMenuEntry) != null) {
            contextMenuEntry.imageResource(string);
        }
    }

    public CommandLink ctrlKey(char c) {
        ctrlKey = c;

        if ((contextMenuEntry) != null) {
            contextMenuEntry.ctrlKey(c);
        }

        return this;
    }
}
