package co.fxl.gui.i18n.api;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.impl.I18N;


public interface II18N {
    String put(String text, String translation);

    String translate(String text);

    void addConstant(String token);

    void addRule(String template, String translationTemplate);

    void addHelp(String iD, ILabel label);

    boolean active(boolean active);
}
