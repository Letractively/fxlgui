package co.fxl.gui.impl;

public interface IFieldType {
    IFieldType integer();

    IFieldType date();

    IFieldType dateTime();

    IFieldType time();

    IFieldType text();

    IFieldType html();

    IFieldType color();

    IFieldType encryptedText();

    IFieldType longText();

    IFieldType image();

    IFieldType longType();

    IFieldType addConstraint(Object... values);

    IFieldType type(Class<?> clazz);

    IFieldType maxLength(int maxLength);

    IFieldType logic();

    IFieldType relation();

    Class<?> clazz();

    IFieldType clearConstraints();

    IFieldType setHasConstraints();

    IFieldType shortText();

    IFieldType doubleValue();

    boolean isRelation();

    boolean isText();
}
