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
package co.fxl.gui.tree.impl;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.tree.api.ITree;

class Node implements IClickListener {

	private static final String FOLDER_CLOSED = "folder_closed.png";
	private static final String FOLDER_OPEN = "folder_open.png";
	private static final String LEAF = "leaf.png";
	private static final int INDENT = 10;
	private IVerticalPanel panel;
	private IHorizontalPanel content;
	private IVerticalPanel childrenPanel = null;
	ITree<Object> tree;
	private int depth;
	private IImage image;
	private TreeWidgetImpl widget;
	private IHorizontalPanel container;
	private boolean expand;

	Node(TreeWidgetImpl widget, IVerticalPanel panel, ITree<Object> root,
			int depth, boolean expand) {
		this.widget = widget;
		this.panel = panel;
		this.expand = expand;
		container = panel.add().panel().horizontal();
		content = container.add().panel().horizontal().spacing(2);
		IClickListener showClickListener = new IClickListener() {
			@Override
			public void onClick() {
				Node.this.widget.show(Node.this);
			}
		};
		if (root.children().size() != 0) {
			content.addSpace(depth * INDENT);
			image = content.add().image().resource(FOLDER_CLOSED);
			image.addClickListener(this);
			content.addSpace(4);
		} else {
			content.addSpace(depth * INDENT);
			image = content.add().image().resource(LEAF);
			image.addClickListener(showClickListener);
			content.addSpace(4);
		}
		ILabel label = content.add().label().text(root.name());
		if (widget.detailPanel != null) {
			label.addClickListener(showClickListener);
		}
		this.tree = root;
		this.depth = depth;
		content.addSpace(10);
		if (root.children().size() != 0 && expand)
			onClick();
		widget.object2node.put(root.object(), this);
	}

	@Override
	public void onClick() {
		if (childrenPanel == null) {
			expand();
		} else {
			clear();
		}
	}

	private void expand() {
		clear();
		childrenPanel = panel.add().panel().vertical();
		for (ITree<Object> child : tree.children()) {
			new Node(widget, childrenPanel.add().panel().vertical(), child,
					depth + 1, expand);
		}
		if (tree.canLoadChildren()) {
			IHorizontalPanel panel = childrenPanel.add().panel().horizontal();
			panel.addSpace(depth * INDENT + 14);
			panel.add().label().text("Load").font().pixel(10).color().gray();
			panel.addSpace(2);
			final ILabel hyperlink = panel.add().label().text("Children")
					.hyperlink();
			hyperlink.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					tree.loadChildren();
					expand();
				}
			});
			hyperlink.font().pixel(10);
		}
		image.resource(FOLDER_OPEN);
	}

	private void clear() {
		if (childrenPanel == null)
			return;
		childrenPanel.clear();
		childrenPanel.remove();
		childrenPanel = null;
		image.resource(FOLDER_CLOSED);
	}

	void selected(boolean selected) {
		if (!selected)
			container.color().white();
		else
			container.color().rgb(230, 230, 255);
	}
}
