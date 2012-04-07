package co.fxl.gui.i18n.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.api.II18N;
import co.fxl.gui.i18n.impl.I18N;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings(value = "serial")
public class I18NTemplate extends HashMap<java.lang.String, java.lang.String>
    implements II18N {
    private boolean active = true;
    private List<java.lang.String> constants = new LinkedList<java.lang.String>();
    private Map<java.lang.String, java.lang.String> rules = new HashMap<java.lang.String, java.lang.String>();

    protected I18NTemplate() {
    }

    protected void dontTranslate(String string) {
        put(string, string);
    }

    protected void dontTranslateIgnoreCase(String string) {
        putIgnoreCase(string, string);
    }

    @Override
    public String translate(String text) {
        if (!(active)) {
            return text;
        }

        if ((text.length()) <= 1) {
            return text;
        }

        if (java.lang.Character.isDigit(text.charAt(0))) {
            return text;
        }

        String translation = get(text);

        if (translation == null) {
            String e = ("no translation found for \'" + text) + "\'";
            System.err.println(e);
            throw new RuntimeException(e);
        }

        return translation;
    }

    @Override
    public void addHelp(String iD, ILabel label) {
    }

    @Override
    public String put(String text, String translation) {
        super.put(text.toUpperCase(), translation.toUpperCase());

        return super.put(text, translation);
    }

    @Override
    public boolean active(boolean active) {
        boolean state = this.active;
        this.active = active;

        return state;
    }

    @Override
    public void addConstant(String token) {
        dontTranslate(token);
        constants.add(token);
        handleRules(token);

        if ((subString(token)) != null) {
            handleRules(subString(token));
        }
    }

    private String subString(String token) {
        if (!(token.contains(" "))) {
            return null;
        }

        return token.substring(((token.indexOf(" ")) + 1));
    }

    private void handleRules(String token) {
        for (String r : rules.keySet()) {
            handleRule(token, r, rules.get(r));
        }
    }

    private void handleRule(String token, String template,
        String translationTemplate) {
        put(template.replace("$", token),
            translationTemplate.replace("$", token));
    }

    @Override
    public void addRule(String template, String translationTemplate) {
        rules.put(template, translationTemplate);

        for (String c : constants) {
            handleRule(c, template, translationTemplate);
        }
    }

    protected void putIgnoreCase(String string, String string2) {
        put(string, string2);
        put(string.toUpperCase(), string2.toUpperCase());
    }
}
