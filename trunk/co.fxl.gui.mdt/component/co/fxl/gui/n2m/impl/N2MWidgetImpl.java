/**
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.n2m.impl;

import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.n2m.api.IN2MWidget;

class N2MWidgetImpl implements IN2MWidget<Object> {

	// TODO FEATURE: Option: Usability: Enable Drag & Drop for N2M-Widget, can
	// be used for reordering & moving from left 2 right / right 2 left

	private static final int _20 = 10;
	private IGridPanel grid;
	private SelectableList left;
	private SelectableList right;
	private IVerticalPanel center;
	private IN2MRelationListener<Object> listener;
	private List<Object> last = null;
	private IImage leftButton;
	private IImage buttonRight;
	private IImage buttonLeftAll;
	private IImage buttonRightAll;
	boolean editable = true;
	String itemImage;
	IItemImageProvider<Object> imageProvider;
	private IImage upButton;
	private IImage downButton;
	private IImage topButton;
	private IImage bottomButton;
	boolean allowReorder = false;

	N2MWidgetImpl(IContainer container) {
		grid = container.panel().grid();
		left = new SelectableList(this, grid.cell(0, 0), "Available", false);
		grid.column(0).expand();
		center = grid.cell(1, 0).width(36).align().center().panel().vertical()
				.add().panel().vertical().align().center().spacing(10);
		right = new SelectableList(this, grid.cell(2, 0), "Selected", true);
		grid.column(2).expand();
		IClickListener leftClickListener = new IClickListener() {
			@Override
			public void onClick() {
				Object selection = right.removeSelected();
				right.visible(selection, false);
				left.visible(selection, true);
				update();
			}
		};
		leftButton = center.add().image().resource("(_02.png").size(16, 16)
				.addClickListener(leftClickListener).mouseLeft();
		right.link(leftButton, leftClickListener);
		IClickListener rightClickListener = new IClickListener() {
			@Override
			public void onClick() {
				Object selection = left.removeSelected();
				left.visible(selection, false);
				right.visible(selection, true);
				update();
			}
		};
		buttonRight = center.add().image().resource(")_02.png").size(16, 16)
				.addClickListener(rightClickListener).mouseLeft();
		left.link(buttonRight, rightClickListener);
		center.addSpace(_20);
		buttonLeftAll = center.add().image().resource("((_02.png").size(16, 16)
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
		buttonRightAll = center.add().image().resource("))_02.png")
				.size(16, 16).addClickListener(new IClickListener() {
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

	private void addReorderButtons() {
		IClickListener upClickListener = new IClickListener() {
			@Override
			public void onClick() {
				right.up();
				update();
			}
		};
		upButton = center.addSpace(_20).add().image().resource("up.png")
				.size(16, 16).addClickListener(upClickListener).mouseLeft();
		right.linkUp(upButton, upClickListener);
		IClickListener downClickListener = new IClickListener() {
			@Override
			public void onClick() {
				right.down();
				update();
			}
		};
		downButton = center.add().image().resource("down.png").size(16, 16)
				.addClickListener(downClickListener).mouseLeft();
		right.linkDown(downButton, downClickListener);
		IClickListener topClickListener = new IClickListener() {
			@Override
			public void onClick() {
				right.top();
				update();
			}
		};
		topButton = center.addSpace(_20).add().image().resource("top.png")
				.size(16, 16).addClickListener(topClickListener).mouseLeft();
		right.linkTop(topButton, topClickListener);
		IClickListener bottomClickListener = new IClickListener() {
			@Override
			public void onClick() {
				right.bottom();
				update();
			}
		};
		bottomButton = center.add().image().resource("bottom.png").size(16, 16)
				.addClickListener(bottomClickListener).mouseLeft();
		right.linkBottom(bottomButton, bottomClickListener);
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
		List<Object> items = right.getItems();
		if (listener != null && (last == null || !equals(last, items))) {
			listener.onChange(items);
		}
		last = items;
	}

	private boolean equals(List<Object> l1, List<Object> l2) {
		if (topButton != null) {
			if (l1.size() != l2.size())
				return false;
			for (int i = 0; i < l1.size(); i++)
				if (!l1.get(i).equals(l2.get(i)))
					return false;
			return true;
		}
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

	@Override
	public IN2MWidget<Object> titles(String left, String right) {
		this.left.title(left);
		this.right.title(right);
		return this;
	}

	@Override
	public int offsetY() {
		return grid.offsetY();
	}

	@Override
	public IN2MWidget<Object> height(int maxFromDisplay) {
		left.height(maxFromDisplay);
		right.height(maxFromDisplay);
		return this;
	}

	@Override
	public IN2MWidget<Object> editable(boolean editable) {
		if (!editable) {
			leftButton.clickable(false);
			buttonLeftAll.clickable(false);
			buttonRight.clickable(false);
			buttonRightAll.clickable(false);
		}
		this.editable = editable;
		return this;
	}

	@Override
	public IN2MWidget<Object> itemImage(String image) {
		itemImage = image;
		return this;
	}

	@Override
	public IN2MWidget<Object> itemImageProvider(
			co.fxl.gui.n2m.api.IN2MWidget.IItemImageProvider<Object> imageProvider) {
		this.imageProvider = imageProvider;
		return this;
	}

	// String getItemImage(Object object) {
	// if (imageProvider != null && imageProvider.itemImage(object) != null) {
	// return imageProvider.itemImage(object);
	// }
	// if (itemImage != null)
	// return itemImage;
	// return null;
	// }

	@Override
	public IN2MWidget<Object> allowReorder(boolean allowReorder) {
		this.allowReorder = allowReorder;
		if (allowReorder)
			addReorderButtons();
		return this;
	}

	@Override
	public Object selectedItem() {
		return right.selection != null ? right.selection.object : null;
	}

	@Override
	public IN2MWidget<Object> selectedItem(Object o) {
		right.preselected = o;
		return this;
	}

	boolean hasItemImage() {
		return itemImage != null || imageProvider != null;
	}
}
