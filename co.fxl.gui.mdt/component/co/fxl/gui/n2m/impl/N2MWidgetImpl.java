package co.fxl.gui.n2m.impl;

import java.util.List;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.n2m.api.IN2MWidget;

class N2MWidgetImpl implements IN2MWidget<Object> {

	private IGridPanel grid;
	private SelectableList left;
	private SelectableList right;
	private IVerticalPanel center;
	private IN2MRelationListener<Object> listener;
	private List<Object> last = null;

	N2MWidgetImpl(IContainer container) {
		grid = container.panel().grid();
		left = new SelectableList(grid.cell(0, 0));
		center = grid.cell(1, 0).width(80).align().center().panel().vertical()
				.add().panel().vertical().spacing(10);
		right = new SelectableList(grid.cell(2, 0));
		IButton leftButton = center.add().button().text("<")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						Object selection = right.removeSelected();
						right.visible(selection, false);
						left.visible(selection, true);
						update();
					}
				}).mouseLeft();
		right.link(leftButton);
		IButton buttonRight = center.add().button().text(">")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						Object selection = left.removeSelected();
						left.visible(selection, false);
						right.visible(selection, true);
						update();
					}
				}).mouseLeft();
		left.link(buttonRight);
		center.addSpace(20);
		IButton buttonLeftAll = center.add().button().text("<<")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						List<Object> selection = right.removeAll();
						right.visible(selection, false);
						left.visible(selection, true);
						update();
					}
				}).mouseLeft();
		right.linkAll(buttonLeftAll);
		IButton buttonRightAll = center.add().button().text(">>")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						List<Object> selection = left.removeAll();
						left.visible(selection, false);
						right.visible(selection, true);
						update();
					}
				}).mouseLeft();
		left.linkAll(buttonRightAll);
	}

	@Override
	public IN2MWidget<Object> domain(List<Object> tokens) {
		left.domain(tokens, true);
		right.domain(tokens, false);
		return this;
	}

	@Override
	public IN2MWidget<Object> selection(List<Object> tokens) {
		left.visible(tokens, false);
		right.visible(tokens, true);
		update();
		return this;
	}

	@Override
	public List<Object> selection() {
		return right.getItems();
	}

	@Override
	public IN2MWidget<Object> listener(
			co.fxl.gui.n2m.api.IN2MWidget.IN2MRelationListener<Object> listener) {
		update();
		this.listener = listener;
		return this;
	}

	private void update() {
		List<Object> items = left.getItems();
		if (listener != null && (last == null || !equals(last, items))) {
			listener.onChange(items);
		}
		last = items;
	}

	private boolean equals(List<Object> l1, List<Object> l2) {
		for (Object o : l1) {
			if (!l2.contains(o))
				return false;
		}
		for (Object o : l2) {
			if (!l1.contains(o))
				return false;
		}
		return true;
	}
}
