package co.fxl.gui.gwt;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

class ObservableSplitLayoutPanel extends SplitLayoutPanel {

	GWTSplitPane owner;

	@Override
	public void onResize() {
		super.onResize();
		Widget left = getWidget(0);
		owner.splitPosition = left.getElement().getParentElement()
				.getOffsetWidth();
		left.setWidth(owner.splitPosition + "px");
		owner.onResize(owner.splitPosition);
	}

	void updatePosition() {
		if (getWidgetCount() == 0)
			return;
		Widget left = getWidget(0);
		left.getElement().getParentElement().getStyle()
				.setWidth(owner.splitPosition, Unit.PX);
		left.setWidth(owner.splitPosition + "px");
	}
}