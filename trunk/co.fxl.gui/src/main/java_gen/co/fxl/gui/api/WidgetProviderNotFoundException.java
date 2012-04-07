package co.fxl.gui.api;

public class WidgetProviderNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -992209490569188032L;

    public WidgetProviderNotFoundException(Class<?> clazz) {
        super(clazz.getName());
    }

    public String getMessage() {
        return "No widget provider found for widget class " +
        (super.getMessage());
    }
}
