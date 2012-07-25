package co.fxl.gui.gwt;

import com.google.gwt.user.client.ui.Widget;

public interface WidgetParent {

	void add(Widget widget);

	void remove(Widget widget);

	// GWTDisplay lookupDisplay();
	//
	// IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass);

}
