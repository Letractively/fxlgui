package co.fxl.gui.gwt;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.Display;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTPopUp implements IPopUp, WidgetParent {

	private PopupPanel popUp;
	private boolean center;
	private int w;
	private int h;
	private boolean fitInScreen;
	private int x = -1;
	private int y = -1;

	GWTPopUp() {
		popUp = new PopupPanel(false, false);
		// TODO popUp.getElement().setDraggable(Element.DRAGGABLE_TRUE);
		popUp.getElement().getStyle().setPadding(0, Unit.PX);
	}

	@Override
	public IContainer container() {
		return new GWTContainer<Widget>(this);
	}

	@Override
	public IPopUp visible(boolean visible) {
		// Log.instance().warn("PopUp: " + visible);
		if (visible) {
			if (center)
				popUp.center();
			if (fitInScreen && x != -1 && y != -1) {
				popUp.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = x;
						int top = y;
						if (x + offsetWidth > Display.instance().width()) {
							left = Display.instance().width() - 10
									- offsetWidth;
						}
						if (y + offsetHeight > Display.instance().height()) {
							top = Display.instance().height() - 10
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
		// if (h > 0)
		// widget.setHeight(h + "px");
		popUp.setWidget(widget);
	}

	@Override
	public void remove(Widget widget) {
		throw new UnsupportedOperationException();
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
	public IPopUp atLastClick(int offsetX, int offsetY) {
		offset(GWTDisplay.lastClickX + offsetX, GWTDisplay.lastClickY + offsetY);
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

	@Override
	public IPopUp opacity(double opacity) {
		popUp.getElement().getStyle().setOpacity(opacity);
		return this;
	}

	@Override
	public IPopUp width(double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPopUp height(double height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IColor color() {
		return new GWTColor() {

			@Override
			protected IColor setColorInternal(String string) {
				popUp.getElement().getStyle().setBackgroundColor(string);
				return this;
			}
		};
	}

	@Override
	public IPopUp atLastClick() {
		return atLastClick(0, 0);
	}
}