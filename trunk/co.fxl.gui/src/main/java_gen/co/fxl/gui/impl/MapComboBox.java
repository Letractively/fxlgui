package co.fxl.gui.impl;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IUpdateable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MapComboBox<T> implements IUpdateable<T> {
    private static Heights HEIGHTS = new Heights(0);
    private IComboBox comboBox;
    private Map<java.lang.String, T> text2object = new HashMap<java.lang.String, T>();
    private Map<T, java.lang.String> object2text = new HashMap<T, java.lang.String>();

    public MapComboBox(IContainer container) {
        this.comboBox = container.comboBox();
    }

    public MapComboBox(IComboBox comboBox) {
        this.comboBox = comboBox;
        MapComboBox.HEIGHTS.decorate(comboBox);
    }

    @Override
    public IUpdateable<T> addUpdateListener(
        final IUpdateable.IUpdateListener<T> listener) {
        comboBox.addUpdateListener(new IUpdateable.IUpdateListener<java.lang.String>() {
                @Override
                public void onUpdate(String value) {
                    listener.onUpdate(text2object.get(value));
                }
            });

        return this;
    }

    public MapComboBox<T> clear() {
        comboBox.clear();
        text2object.clear();
        object2text.clear();

        return this;
    }

    public MapComboBox<T> addNull() {
        comboBox.addNull();

        return this;
    }

    public MapComboBox<T> addObject(T object) {
        if (object == null) {
            return addNull();
        }

        return addObject(String.valueOf(object), object);
    }

    public MapComboBox<T> addObject(String text, T object) {
        comboBox.addText(text);
        text2object.put(text, object);
        object2text.put(object, text);

        return this;
    }

    public T object() {
        return text2object.get(comboBox.text());
    }

    public MapComboBox<T> object(T object) {
        comboBox.text(object2text.get(object));

        return this;
    }

    public MapComboBox<T> editable(boolean editable) {
        comboBox.editable(editable);

        return this;
    }

    public IComboBox comboBox() {
        return comboBox;
    }

    public Collection<T> objects() {
        return object2text.keySet();
    }
}
