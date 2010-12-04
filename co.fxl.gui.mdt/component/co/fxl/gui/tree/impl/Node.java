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

import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.tree.api.ITree;

/**
 * Represents the ui of a tree node.
 * 
 * @param <T>
 */
class Node<T> implements IClickListener {
 
	private static final String DOWN = "refresh.png";
	private static final String FOLDER_CLOSED = "folder_closed.png";
	private static final String FOLDER_OPEN = "folder_open.png";
	private static final String LEAF = "leaf.png";
	private static final int INDENT = 10;
	private IVerticalPanel panel;
	private IHorizontalPanel content;
	private IVerticalPanel childrenPanel = null;
	ITree<T> tree;
	private int depth;
	private IImage image;
	private TreeWidgetImpl<T> widget;
	private IHorizontalPanel container;
	private boolean expand;
	private IImage imageRefresh;
	private ILabel label;
	private ILabel refreshLabel;
	List<ITree<T>> path;

	Node(TreeWidgetImpl<T> widget, IVerticalPanel panel, ITree<T> root,
			int depth, boolean expand, List<ITree<T>> path) {
		this.widget = widget;
		this.panel = panel;
		this.expand = expand;
		this.path = path;
		container = panel.add().panel().horizontal();
		content = container.add().panel().horizontal().spacing(2);
		IClickListener showClickListener = new IClickListener() {
			@Override
			public void onClick() {
				Node.this.widget.show(Node.this);
			}
		};
		if (root.childCount() != 0) {
			content.addSpace(depth * INDENT);
			image = content.add().image().resource(FOLDER_CLOSED);
			image.addClickListener(this);
			image.addClickListener(showClickListener);
			content.addSpace(4);
		} else {
			content.addSpace(depth * INDENT);
			image = content.add().image().resource(LEAF);
			image.addClickListener(showClickListener);
			content.addSpace(4);
		}
		label = content.add().label().text(root.name());
		if (widget.detailPanel != null) {
			label.addClickListener(showClickListener);
		}
		if (root.children().size() != 0 && expand
				&& root.childCount() > root.children().size()) {
			String text = " [" + root.children().size() + "/"
					+ root.childCount() + "]";
			refreshLabel = content.add().label().text(text);
			refreshLabel.font().color().gray();
			content.addSpace(4);
			imageRefresh = content.add().image().resource(DOWN);
			imageRefresh.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					expandLazyNode();
				}
			});
			imageRefresh.addClickListener(showClickListener);
		}
		container.addClickListener(showClickListener);
		this.tree = root;
		this.depth = depth;
		content.addSpace(10);
		if (root.children().size() != 0 && expand)
			expandLoadedNode();
		else if (path != null && path.contains(tree)) {
			expandLoadedNode();
		}
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

	protected void expand() {
		if (tree.childCount() > tree.children().size()) {
			expandLazyNode();
		} else {
			expandLoadedNode();
		}
	}

	/**
	 * Expands not yet loaded node.
	 */
	protected void expandLazyNode() {
		// Lazy load children
		ICallback<List<T>> lCallback = new ICallback<List<T>>() {
			@Override
			public void onFail(Throwable caught) {
				// TODO
			}

			@Override
			public void onSuccess(List<T> result) {
				expandLoadedNode();
			}
		};
		if (imageRefresh != null) {
			imageRefresh.resource(null);
			refreshLabel.remove();
		}
		expand = false;
		tree.loadChildren(lCallback);
	}

	/**
	 * Expands already loaded nodes.
	 */
	protected void expandLoadedNode() { 
		clear();
		childrenPanel = panel.add().panel().vertical();
		for (ITree<T> child : tree.children()) {
			new Node<T>(widget, childrenPanel.add().panel().vertical(), child,
					depth + 1, expand, path);
		}
		if (tree.childCount() > 0)
			image.resource(FOLDER_OPEN);
	}

	private void clear() {
		if (childrenPanel == null)
			return;
		childrenPanel.clear();
		childrenPanel.remove();
		childrenPanel = null;
		image.resource(FOLDER_CLOSED);
		if (imageRefresh != null)
			imageRefresh.resource(null);
	}

	void selected(boolean selected) {
		if (!selected)
			container.color().white();
		else {
			container.color().rgb(230, 230, 255);
		}
	}
}
