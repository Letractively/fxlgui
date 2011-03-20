package co.fxl.gui.gwt;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWidgetProvider;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTPopUp implements IPopUp, WidgetParent {

	private GWTDisplay display;
	private PopupPanel popUp;

	GWTPopUp(GWTDisplay display) {
		this.display = display;
		popUp = new PopupPanel(true);
	}

	@Override
	public IContainer container() {
		return new GWTContainer<Widget>(this);
	}

	@Override
	public IPopUp visible(boolean visible) {
		if (!visible)
			popUp.hide();
		return this;
	}

	@Override
	public void add(Widget widget) {
		popUp.setWidget(widget);
	}

	@Override
	public void remove(Widget widget) {
		throw new MethodNotImplementedException();
	}

	@Override
	public GWTDisplay lookupDisplay() {
		return display;
	}

	@Override
	public IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		throw new MethodNotImplementedException();
	}
}
