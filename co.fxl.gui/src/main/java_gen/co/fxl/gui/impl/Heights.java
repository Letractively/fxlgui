package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextInput;


public class Heights {
    public static final int CELL_HEIGHT = 28;
    public static final int TEXTFIELD_HEIGHT = 24;
    public static final int COMBOBOX_HEIGHT = 24;
    public static final Heights INSTANCE = new Heights(0);
    private int inc;

    public Heights(int inc) {
        this.inc = inc;
    }

    public void decorate(IComboBox textField) {
        decorateHeight(textField);
        styleColor(textField);
    }

    public void styleColor(IColored label) {
        label.color().rgb(249, 249, 249);
    }

    private IBordered.IBorder styleInputBorder(IBordered bordered) {
        IBordered.IBorder border = bordered.border();
        border.color().rgb(211, 211, 211);

        return border;
    }

    public void decorateHeight(IComboBox textField) {
        textField.height(((inc) + (COMBOBOX_HEIGHT)));
        styleInputBorder(((IBordered) (textField)));
    }

    public void decorate(ITextInput<?> textField) {
        decorateHeight(textField);
        styleColor(textField);
    }

    public void decorate(ITextArea textField) {
        styleColor(textField);
    }

    public void decorateHeight(ITextInput<?> textField) {
        textField.height(((inc) + (TEXTFIELD_HEIGHT)));
        styleInputBorder(((IBordered) (textField)));
    }

    public void decorate(IPasswordField textField) {
        decorateHeight(textField);
        styleColor(textField);
    }

    public void decorateHeight(IPasswordField textField) {
        textField.height(((inc) + (TEXTFIELD_HEIGHT)));
        styleInputBorder(((IBordered) (textField)));
    }

    public void decorate(IGridPanel.IGridCell cell) {
        cell.height(((inc) + (CELL_HEIGHT)));
    }

    public void decorate(IHorizontalPanel panel) {
        panel.height(((inc) + (CELL_HEIGHT)));
    }

    public void valuePanel(ICheckBox valuePanel) {
    }

    public void decorate(ILabel label) {
        label.height(((inc) + (TEXTFIELD_HEIGHT)));
    }

    public void decorate(IPanel<?> panel) {
        decorateHeight(panel);
        styleColor(panel);
    }

    public void decorateHeight(IPanel<?> panel) {
        panel.height(((inc) + (COMBOBOX_HEIGHT)));
    }

    public void decorate(ICheckBox c) {
        c.height(((inc) + (COMBOBOX_HEIGHT)));
    }

    public IBordered.IBorder decorateBorder(IBordered border) {
        return styleInputBorder(border);
    }

    public void decorateHeight(IButton b) {
        b.height(((inc) + (COMBOBOX_HEIGHT)));
    }
}
