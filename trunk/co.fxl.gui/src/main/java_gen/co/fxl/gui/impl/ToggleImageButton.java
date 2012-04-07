package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IUpdateable;


public class ToggleImageButton implements IUpdateable<java.lang.Boolean> {
    private CommandLink c;
    private String img;
    private String text;
    private String tImg;
    private String tText;
    private boolean toggled = false;

    public ToggleImageButton(CommandLink c, String img, String text) {
        this.c = c;
        this.img = img;
        this.text = text;
        c.clickable(true);
    }

    public ToggleImageButton toggleImageText(String img, String text) {
        this.tImg = img;
        this.tText = text;

        return this;
    }

    @Override
    public IUpdateable<java.lang.Boolean> addUpdateListener(
        final IUpdateable.IUpdateListener<java.lang.Boolean> listener) {
        c.addClickListener(new IClickable.IClickListener() {
                @Override
                public void onClick() {
                    toggled = !(toggled);
                    update();
                    listener.onUpdate(toggled);
                }
            });

        return this;
    }

    public ToggleImageButton toggled(boolean editable) {
        toggled = editable;
        update();

        return this;
    }

    void update() {
        c.text(((!(toggled)) ? text : tText));
        c.image(((!(toggled)) ? img : tImg));
    }
}
