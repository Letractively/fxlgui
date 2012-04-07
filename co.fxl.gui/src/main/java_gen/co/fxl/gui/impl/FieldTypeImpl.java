package co.fxl.gui.impl;

import co.fxl.gui.api.IImage;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class FieldTypeImpl implements IFieldType {
    public Class<?> clazz = String.class;
    public boolean isLong = false;
    private List<java.lang.Object> values;
    public boolean isRelation = false;
    public int maxLength = -1;
    public boolean encryptedText = false;
    public boolean isShort = false;
    public boolean isColor = false;
    public boolean isHTML = false;

    public boolean equals(Object o) {
        FieldTypeImpl t = ((FieldTypeImpl) (o));

        if (!((((((((clazz.equals(t.clazz)) && ((isLong) == (t.isLong))) &&
                ((isRelation) == (t.isRelation))) &&
                ((maxLength) == (t.maxLength))) &&
                ((encryptedText) == (t.encryptedText))) &&
                ((isShort) == (t.isShort))) && ((isColor) == (t.isColor))) &&
                ((isHTML) == (t.isHTML)))) {
            return false;
        }

        if ((values) == null) {
            return (t.values) == null;
        }

        if ((values.size()) != (t.values.size())) {
            return false;
        }

        for (int i = 0; i < (values.size()); i++) {
            if ((values.get(i)) == null) {
                return (t.values.get(i)) == null;
            } else if (!(values.get(i).equals(t.values.get(i)))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return isHTML ? "HTML" : String.valueOf(clazz);
    }

    @Override
    public IFieldType type(Class<?> clazz) {
        this.clazz = clazz;

        return this;
    }

    @Override
    public IFieldType date() {
        return type(Date.class);
    }

    @Override
    public IFieldType color() {
        isColor = true;

        return type(String.class);
    }

    @Override
    public IFieldType image() {
        return type(IImage.class);
    }

    @Override
    public IFieldType integer() {
        return type(Integer.class);
    }

    @Override
    public IFieldType longText() {
        isLong = true;

        return type(String.class);
    }

    @Override
    public IFieldType addConstraint(Object... values) {
        if ((this.values) == null) {
            setHasConstraints();
        }

        for (Object v : values)
            this.values.add(v);

        return this;
    }

    @Override
    public IFieldType setHasConstraints() {
        this.values = new LinkedList<java.lang.Object>();

        return this;
    }

    public boolean hasConstraints() {
        return (values) != null;
    }

    public List<java.lang.Object> getConstraints() {
        if ((values) == null) {
            return new LinkedList<java.lang.Object>();
        }

        return values;
    }

    @Override
    public IFieldType text() {
        return type(String.class);
    }

    @Override
    public IFieldType shortText() {
        isShort = true;

        return type(String.class);
    }

    @Override
    public IFieldType longType() {
        return type(Long.class);
    }

    @Override
    public IFieldType logic() {
        return type(Boolean.class);
    }

    @Override
    public Class<?> clazz() {
        return clazz;
    }

    @Override
    public IFieldType relation() {
        isRelation = true;

        return this;
    }

    @Override
    public IFieldType maxLength(int maxLength) {
        this.maxLength = maxLength;

        return this;
    }

    @Override
    public IFieldType encryptedText() {
        encryptedText = true;

        return this;
    }

    @Override
    public IFieldType clearConstraints() {
        if ((values) != null) {
            values.clear();
        }

        return this;
    }

    @Override
    public IFieldType time() {
        type(Date.class);
        isShort = true;

        return this;
    }

    @Override
    public IFieldType dateTime() {
        type(Date.class);
        isLong = true;

        return this;
    }

    @Override
    public IFieldType html() {
        isHTML = true;

        return this;
    }

    @Override
    public IFieldType doubleValue() {
        type(Double.class);

        return this;
    }

    @Override
    public boolean isRelation() {
        return isRelation;
    }

    @Override
    public boolean isText() {
        return (clazz.equals(String.class)) && ((values) == null);
    }
}
