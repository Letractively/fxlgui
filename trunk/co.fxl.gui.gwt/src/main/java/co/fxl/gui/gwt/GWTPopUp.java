package co.fxl.gui.gwt;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IWidgetProvider;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTPopUp implements IPopUp, WidgetParent {

	private GWTDisplay display;
	private PopupPanel popUp;
	private boolean center;
	private int w;
	private int h;
	private boolean fitInScreen;
	private int x = -1;
	private int y = -1;

	GWTPopUp(GWTDisplay display) {
		this.display = display;
		popUp = new PopupPanel(false, false);
		popUp.getElement().getStyle().setPadding(0, Unit.PX);
	}

	@Override
	public IContainer container() {
		return new GWTContainer<Widget>(this);
	}

	@Override
	public IPopUp visible(boolean visible) {
		if (visible) {
			if (center)
				popUp.center();
			if (fitInScreen && x != -1 && y != -1) {
				popUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = x;
						int top = y;
						if (x + offsetWidth > GWTDisplay.instance().width()) {
							left = GWTDisplay.instance().width() - 10
									- offsetWidth;
						}
						if (y + offsetHeight > GWTDisplay.instance().height()) {
							top = GWTDisplay.instance().height() - 10
									- offsetHeight;
						}
						popUp.setPopupPosition(left, top);
					}
				});
			} else
				popUp.show();
		} else
			popUp.hide();
		return this;
	}

	@Override
	public void add(Widget widget) {
		if (w > 0)
			widget.setWidth((w - 16) + "px");
//		if (h > 0)
//			widget.setHeight(h + "px");
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
		return display.lookupWidgetProvider(interfaceClass);
	}

	@Override
	public IPopUp offset(int x, int y) {
		this.x = x;
		this.y = y;
		popUp.setPopupPosition(x, y);
		return this;
	}

	@Override
	public IPopUp modal(boolean modal) {
		popUp.setModal(modal);
		popUp.setGlassEnabled(modal);
		popUp.setAutoHideEnabled(!modal);
		return this;
	}

	@Override
	public IPopUp glass(boolean glass) {
		popUp.setGlassEnabled(glass);
		return this;
	}

	@Override
	public IPopUp center() {
		center = true;
		popUp.center();
		return this;
	}

	@Override
	public int offsetX() {
		return popUp.getPopupLeft();
	}

	@Override
	public int offsetY() {
		return popUp.getPopupTop();
	}

	@Override
	public int width() {
		return popUp.getOffsetWidth();
	}

	@Override
	public int height() {
		return popUp.getOffsetHeight();
	}

	@Override
	public IPopUp size(int w, int h) {
		this.w = w;
		this.h = h;
		popUp.setWidth(w + "px");
		popUp.setHeight(h + "px");
		return this;
	}

	@Override
	public IPopUp autoHide(boolean autoHide) {
		popUp.setAutoHideEnabled(autoHide);
		return this;
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(popUp);
	}

	@Override
	public IPopUp width(int width) {
		this.w = width;
		popUp.setWidth(w + "px");
		return this;
	}

	@Override
	public IPopUp height(int height) {
		this.h = height;
		popUp.setHeight(h + "px");
		return this;
	}

	@Override
	public IPopUp atLastClick() {
		offset(GWTDisplay.lastClickX, GWTDisplay.lastClickY);
		return this;
	}

	@Override
	public IPopUp fitInScreen(boolean fitInScreen) {
		this.fitInScreen = fitInScreen;
		return this;
	}

	@Override
	public IPopUp addVisibleListener(final IUpdateListener<Boolean> l) {
		popUp.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				l.onUpdate(false);
			}
		});
		return this;
	}

	@Override
	public boolean visible() {
		return popUp.isVisible() && popUp.isAttached();
	}
}
