package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.ITextInput;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class SuggestionTextField implements IUpdateable.IUpdateListener<java.lang.Boolean> {
    private List<java.lang.String> texts = new LinkedList<java.lang.String>();
    private ITextField textField;
    private ElementPopUp popUp;

    public SuggestionTextField(IContainer container) {
        textField = container.textField().visible(false);
        ElementPopUp.HEIGHTS.decorate(textField);
        popUp = new ElementPopUp(textField);
    }

    @Override
    public void onUpdate(Boolean value) {
        if (value) {
            clearPopUp();
            createPopUp();
            popUp.visible(true);
        }
    }

    public SuggestionTextField addText(String... texts) {
        this.texts.addAll(java.util.Arrays.asList(texts));
        popUp.lines(this.texts.size());
        popUp.scrollPane(((this.texts.size()) > 1));

        return this;
    }

    private void createPopUp() {
        IVerticalPanel v = popUp.create();

        for (final String text : texts) {
            ILabel cb = v.add().panel().horizontal().align().begin().add()
                         .panel().horizontal().align().begin().add().label()
                         .text(text);
            cb.addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        textField.text(text);
                        clearPopUp();
                    }
                });
        }
    }

    private void clearPopUp() {
        popUp.clear();
    }

    public SuggestionTextField width(int width) {
        textField.width(width);

        return this;
    }

    public SuggestionTextField clear() {
        clearPopUp();
        texts.clear();

        return this;
    }

    public SuggestionTextField visible(boolean visible) {
        textField.visible(visible);
        textField.addFocusListener(this);

        return this;
    }

    public ITextInput<?> textField() {
        return textField;
    }
}
