package co.fxl.gui.impl;

public class HTMLText {
    public String text = "";
    public boolean center = false;
    public boolean underline = false;
    public boolean autoWrap = true;
    public boolean selectable = true;
    private boolean allowHTML = false;

    public void setText(String text) {
        if (text == null) {
            text = "";
        }

        this.text = text;
    }

    public void allowHTML(boolean allowHTML) {
        this.allowHTML = allowHTML;
    }

    private String center(String text) {
        return ("<p align=\'center\'>" + text) + "</p>";
    }

    public static String html(String toString) {
        toString = toString.replace("\n", "<br/>");

        StringBuilder b = new StringBuilder();

        for (int i = 0; i < (toString.length()); i++) {
            char c = toString.charAt(i);

            switch (c) {
            case 228:
                b.append("&auml;");

                break;

            case 246:
                b.append("&ouml;");

                break;

            case 252:
                b.append("&uuml;");

                break;

            case 196:
                b.append("&Auml;");

                break;

            case 214:
                b.append("&Ouml;");

                break;

            case 220:
                b.append("&Uuml;");

                break;

            default:
                b.append(c);

                break;
            }
        }

        return ("<html>" + (b.toString())) + "</html>";
    }

    private String underline(String toString) {
        return ("<u>" + toString) + "</u>";
    }

    private String noWrap(String toString) {
        return ("<p style=\'white-space: nowrap;\'>" + (text)) + "</p>";
    }

    @Override
    public String toString() {
        String toString = allowHTML ? text : text.replace("<", "&#060;");
        toString = toString.replace("\n", "<br>");

        if (center) {
            toString = center(toString);
        }

        if (underline) {
            toString = underline(toString);
        }

        if (!(autoWrap)) {
            toString = noWrap(toString);
        }

        toString = HTMLText.html(toString);

        if (!(selectable)) {
            toString = unselectable(toString);
        }

        return toString;
    }

    private String unselectable(String toString) {
        return ("<div unselectable=\"on\" class=\"unselectable\">" + toString) +
        "</div>";
    }
}
