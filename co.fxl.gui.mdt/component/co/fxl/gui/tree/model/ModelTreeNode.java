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
package co.fxl.gui.tree.model;

import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.LazyClickListener;
import co.fxl.gui.tree.api.ITree;

class ModelTreeNode<T> extends LazyClickListener {

	private static final String FOLDER_CLOSED = "folder_closed.png";
	private static final String FOLDER_EMPTY = "folder_empty.png";
	private static final String FOLDER_OPEN = "folder_open.png";
	private static final String CLOSED = "closed.png";
	private static final String EMPTY = "empty.png";
	private static final String OPEN = "open.png";
	private static final String LEAF = "leaf.png";
	private static final int INDENT = 10;
	private static final String OPENORCLOSED = "openorclosed.png";
	IHorizontalPanel content;
	private IVerticalPanel childrenPanel = null;
	ITree<T> tree;
	private int depth;
	private IImage image;
	private ModelTreeWidget<T> widget;
	IHorizontalPanel container;
	private boolean expand;
	private IImage imageRefresh;
	private ILabel label;
	private ILabel refreshLabel;
	List<ITree<T>> path;
	boolean expandLoadedNode = false;
	private IImage icon;
	protected boolean isExpanded;
	private IHorizontalPanel buttonPanel;
	boolean moveActive = false;
	private IImage moveTop;
	private IImage moveUp;
	private IImage moveDown;
	private IImage moveBottom;

	ModelTreeNode(final ModelTreeWidget<T> widget, IVerticalPanel panel,
			final ITree<T> root, int depth, boolean expand, List<ITree<T>> path) {
		assert root != null : "Tree cannot be null";
		this.tree = root;
		this.widget = widget;
		this.expand = expand;
		isExpanded = expand;
		this.path = path;
		container = panel.add().panel().horizontal();
		IClickable<?> clickable = container;
		clickable.addClickListener(this);
		injectTreeListener(clickable);
		content = container.add().panel().horizontal().spacing(2);
		content.addSpace(depth * INDENT);
		image = content.add().image();
		if (root.childCount() != 0) {
			if (icon() != null) {
				image.resource(!root.isLoaded() ? getOpenOrClosedIcon()
						: CLOSED);
				icon = content.add().image().resource(icon());
				icon.addClickListener(this);
				injectTreeListener(icon);
			} else
				image.resource(FOLDER_CLOSED);
		} else {
			if (icon() != null) {
				image.resource(!root.isLoaded() ? getOpenOrClosedIcon() : EMPTY);
				icon = content.add().image().resource(icon());
				icon.addClickListener(this);
				injectTreeListener(icon);
			} else
				image.resource(root.isLeaf() ? LEAF : FOLDER_EMPTY);
		}
		image.addClickListener(this);
		injectTreeListener(image);
		content.addSpace(2);
		String name = root.name();
		boolean isNull = name == null || name.trim().equals("");
		label = content.add().label().text(isNull ? "unnamed" : name);
		label.font().pixel(12);
		buttonPanel = content.addSpace(4).add().panel().horizontal();
		if (isNull)
			label.font().weight().italic().color().gray();
		injectTreeListener(label);
		// if (widget.detailPanel != null) {
		// // label.addClickListener(showClickListener);
		// }
		// if (root.children().size() != 0 && expand
		// && root.childCount() > root.children().size()) {
		// String text = " [" + root.children().size() + "/"
		// + root.childCount() + "]";
		// refreshLabel = content.add().label().text(text);
		// refreshLabel.font().color().gray();
		// content.addSpace(4);
		// imageRefresh = content.add().image().resource(DOWN);
		// imageRefresh.addClickListener(new IClickListener() {
		// @Override
		// public void onClick() {
		// expandLazyNode();
		// }
		// });
		// imageRefresh.addClickListener(showClickListener);
		// }
		label.addClickListener(this);
		this.depth = depth;
		content.addSpace(10);
		childrenPanel = panel.add().panel().vertical();
		if (root.children().size() != 0 && expand)
			expandLoadedNode();
		else if (root.childCount() != 0 && path != null && path.contains(tree)) {
			expandLazyNode();
		}
		widget.object2node.put(root.object(), this);
		decorate();
	}

	String getOpenOrClosedIcon() {
		if (tree.isLeaf())
			return EMPTY;
		return OPENORCLOSED;
	}

	void decorate() {
		if (widget.cutted != null && tree.equals(widget.cutted.tree)) {
			container.border().style().dotted();
		} else
			container.border().color().white();
		if (tree != null && tree.decorator() != null)
			tree.decorator().decorate(label);
		buttonPanel.clear();
		if (moveActive && tree.isMovable()) {
			setUpMoveButtons();
		}
	}

	void setUpMoveButtons() {
		int index = tree.parent().children().indexOf(tree);
		int num = tree.parent().children().size();
		moveTop = buttonPanel.add().image().resource("top.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveTop(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParent();
							}
						});
					}
				}).mouseLeft();
		moveTop.clickable(index > 0);
		buttonPanel.addSpace(4);
		moveUp = buttonPanel.add().image().resource("up.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveUp(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParent();
							}
						});
					}
				}).mouseLeft();
		moveUp.clickable(index > 0);
		buttonPanel.addSpace(4);
		moveDown = buttonPanel.add().image().resource("down.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveDown(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParent();
							}
						});
					}
				}).mouseLeft();
		moveDown.clickable(index < num - 1);
		buttonPanel.addSpace(4);
		moveBottom = buttonPanel.add().image().resource("bottom.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveBottom(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParent();
							}
						});
					}
				}).mouseLeft();
		moveBottom.clickable(index < num - 1);
	}

	private void updateParent() {
		widget.getObject2node(tree.parent().object()).refresh(true);
		widget.selection(tree.object());
		widget.getObject2node(tree.object()).moveActive = true;
		widget.notifyUpdate(widget.selection);
	}

	@SuppressWarnings("unchecked")
	private void injectTreeListener(IClickable<?> clickable) {
		if (widget.treeClickListener != null) {
			IKey<?> key = clickable.addClickListener(new LazyClickListener() {

				@Override
				public void onAllowedClick() {
					widget.treeClickListener.onClick(tree);
				}
			});
			widget.treeClickAdapter.forward((IKey<Object>) key);
		}
	}

	void update(T object) {
		label.text(tree.name());
		label.font().weight().plain().color().black();
		if (tree.childCount() != 0) {
			if (icon() != null) {
				isExpanded = false;
				image.resource(CLOSED);
			} else
				image.resource(FOLDER_CLOSED);
		} else {
			if (icon() != null) {
				image.resource(EMPTY);
			} else
				image.resource(tree.isLeaf() ? LEAF : FOLDER_EMPTY);
		}
		if (icon != null)
			icon.resource(icon());
		decorate();
	}

	private String icon() {
		if (isExpanded)
			return tree.icon();
		else
			return tree.iconClosed();
	}

	void refresh(boolean refreshChildren) {
		update(null);
		if (!refreshChildren)
			return;
		expand();
		expand();
	}

	@Override
	public void onAllowedClick() {
		expandCollapse();
		widget.show(ModelTreeNode.this);
	}

	private void expandCollapse() {
		if (tree.childCount() == 0)
			return;
		if (widget.selection != null && !tree.object().equals(widget.selection)) {
			return;
		}
		if (!expandLoadedNode) {
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

	protected void expandLazyNode() {
		ICallback<List<T>> lCallback = new CallbackTemplate<List<T>>() {

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

	protected void expandLoadedNode() {
		clear();
		expandLoadedNode = true;
		for (ITree<T> child : tree.children()) {
			assert child != null : "Tree child cannot be null";
			widget.newNode(widget, childrenPanel.add().panel().vertical(),
					child, depth + 1, expand, path, new Runnable() {
						@Override
						public void run() {
							if (tree.childCount() > 0) {
								isExpanded = true;
								if (icon() != null) {
									image.resource(OPEN);
									icon.resource(icon());
								} else
									image.resource(FOLDER_OPEN);
							}
						}
					});
		}
	}

	void clear() {
		if (!expandLoadedNode)
			return;
		expandLoadedNode = false;
		clearLoadedNode();
	}

	private void clearLoadedNode() {
		childrenPanel.clear();
		if (icon() != null) {
			if (tree.childCount() != 0) {
				isExpanded = false;
				image.resource(CLOSED);
				icon.resource(icon());
			} else
				image.resource(EMPTY);
		} else
			image.resource(FOLDER_CLOSED);
		if (imageRefresh != null)
			imageRefresh.resource(null);
	}

	void selected(boolean selected) {
		if (!selected)
			container.color().white();
		else {
			container.color().rgb(0xD0, 0xE4, 0xF6);
		}
		buttonPanel.visible(selected);
	}

	@Override
	public String toString() {
		return String.valueOf(tree.object());
	}
}
