package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalLine;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

import java.util.LinkedList;
import java.util.List;


public class ContextMenu {
    private static final boolean SHOW_INACTIVE = false;
    private static ContextMenu instance;
    private IDisplay display;
    private List<co.fxl.gui.impl.ContextMenu.Group> groups = new LinkedList<co.fxl.gui.impl.ContextMenu.Group>();
    private boolean active = true;
    private IPopUp popUp;

    public ContextMenu(IDisplay display) {
        this.display = display;
    }

    public Group group(String name) {
        assert name != null;

        Group group = null;

        for (Group g : groups) {
            String gName = g.name;

            if (gName.equals(name)) {
                group = g;
            }
        }

        if (group == null) {
            group = new Group(name);
            groups.add(group);
        }

        return group;
    }

    public ContextMenu reset() {
        groups.clear();

        return this;
    }

    public ContextMenu decorate(IClickable<?> c) {
        c.addClickListener(new IClickable.IClickListener() {
                @Override
                public void onClick() {
                    show();
                }
            }).mouseRight();

        return this;
    }

    public void show() {
        if (!(active)) {
            return;
        }

        showAlways();
    }

    public void showAlways() {
        if (((popUp) != null) && (popUp.visible())) {
            return;
        }

        popUp = display.showPopUp().autoHide(true).atLastClick();
        new Heights(0).decorateBorder(popUp).style().shadow();

        IVerticalPanel v = popUp.container().panel().vertical().spacing(8);
        IVerticalPanel panel = v.add().panel().vertical();
        panel.spacing(4);

        boolean first = true;

        for (Group g : groups) {
            if (visible(g)) {
                if (!first) {
                    panel.addSpace(1);
                }

                first = false;

                IHorizontalPanel h = panel.add().panel().horizontal();
                h.add().label().text(g.name).font().pixel(9).weight().bold()
                 .color().gray();
                h.addSpace(4).add().line();

                for (Entry o : g) {
                    if (!(visible(o))) {
                        continue;
                    }

                    if (o instanceof Entry) {
                        final Entry e = ((Entry) (o));
                        IHorizontalPanel h2 = panel.add().panel().horizontal()
                                                   .addSpace(10);
                        IClickable.IClickListener clickListener = new IClickable.IClickListener() {
                                @Override
                                public void onClick() {
                                    popUp.visible(false);
                                    popUp = null;

                                    for (IClickable.IClickListener cl : e.clickListeners)
                                        cl.onClick();
                                }
                            };

                        if ((e.imageResource) != null) {
                            h2.add().image().resource(e.imageResource)
                              .addClickListener(clickListener).mouseLeft()
                              .clickable(e.clickable);
                            h2.addSpace(4);
                        }

                        ILabel l = h2.add().label().text(e.text).hyperlink();
                        l.addClickListener(clickListener);
                        l.clickable(e.clickable);
                        h2.addClickListener(clickListener);
                        h2.clickable(e.clickable);
                    }
                }
            }
        }

        popUp.fitInScreen(true);
        popUp.visible(true);
    }

    private boolean visible(Entry o) {
        if (SHOW_INACTIVE) {
            return true;
        }

        return o.clickable;
    }

    private boolean visible(Group g) {
        for (Entry o : g) {
            if (visible(o)) {
                return true;
            }
        }

        return false;
    }

    public static ContextMenu instance(IDisplay display) {
        if ((ContextMenu.instance) == null) {
            ContextMenu.instance = new ContextMenu(display);
        }

        return ContextMenu.instance;
    }

    public static ContextMenu instance() {
        return ContextMenu.instance;
    }

    public void active(boolean active) {
        this.active = active;
    }

    public void decorate(IKeyRecipient<?> keyRecipient) {
        for (Group g : groups) {
            for (final Entry e : g) {
                if ((e.key) != 0) {
                    keyRecipient.addKeyListener(new IClickable.IClickListener() {
                            @Override
                            public void onClick() {
                                if (e.clickable) {
                                    e.fireClick();
                                }
                            }
                        }).ctrl().character(e.key);
                }
            }
        }
    }

    public class Entry implements IClickable<co.fxl.gui.impl.ContextMenu.Entry> {
        private String text = null;
        private boolean clickable = true;
        private List<co.fxl.gui.api.IClickable.IClickListener> clickListeners = new LinkedList<co.fxl.gui.api.IClickable.IClickListener>();
        private String imageResource = null;
        private char key;

        private Entry(String text) {
            this.text = text;
        }

        public Entry imageResource(String imageResource) {
            this.imageResource = imageResource;

            return this;
        }

        @Override
        public Entry clickable(boolean clickable) {
            this.clickable = clickable;

            return this;
        }

        @Override
        public boolean clickable() {
            return clickable;
        }

        @Override
        public IClickable.IKey<co.fxl.gui.impl.ContextMenu.Entry> addClickListener(
            IClickable.IClickListener clickListener) {
            clickListeners.add(clickListener);

            return null;
        }

        public Entry text(String text) {
            this.text = text;

            return this;
        }

        public Entry ctrlKey(char key) {
            this.key = key;

            return this;
        }

        private void fireClick() {
            for (IClickable.IClickListener c : clickListeners)
                c.onClick();
        }
    }

    @SuppressWarnings(value = "serial")
    public class Group extends LinkedList<co.fxl.gui.impl.ContextMenu.Entry> {
        private String name;

        public Group(String name) {
            this.name = name;
        }

        public Entry addEntry(String text) {
            for (Entry o : this) {
                if (text.equals(o.text)) {
                    Entry entry = ((Entry) (o));
                    entry.clickListeners.clear();

                    return entry;
                }
            }

            Entry entry = new Entry(text);
            add(entry);

            return entry;
        }
    }
}
