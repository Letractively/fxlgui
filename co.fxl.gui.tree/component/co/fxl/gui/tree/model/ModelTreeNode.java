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
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.LazyClickListener;
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
	IImage image;
	private ModelTreeWidget<T> widget;
	IHorizontalPanel container;
	private IImage imageRefresh;
	private ILabel label;
	private ILabel refreshLabel;
	boolean childrenExpanded = false;
	private IImage icon;
	protected boolean isExpanded;
	private IHorizontalPanel buttonPanel;
	private IImage moveTop;
	private IImage moveUp;
	private IImage moveDown;
	private IImage moveBottom;
	private IVerticalPanel panel;
	boolean drawn;
	private IClickable<?> gridButton;
	private IImage acceptMove;

	ModelTreeNode(final ModelTreeWidget<T> widget, IVerticalPanel panel,
			final ITree<T> root, int depth, boolean draw) {
		assert root != null && root.object() != null;
		this.widget = widget;
		this.panel = panel.add().panel().vertical();
		this.tree = root;
		this.depth = depth;
		if (!draw) {
			int height = numberLoadedDescendants(tree) + 1;
			this.panel.height(height * 22);
		} else
			draw();
		widget.model.register(this);
	}

	private int numberLoadedDescendants(ITree<T> tree) {
		int i = 0;
		for (ITree<T> t : tree.children()) {
			i += 1 + numberLoadedDescendants(t);
		}
		return i;
	}

	int bottom() {
		return panel.offsetY() + panel.height();
	}

	int top() {
		return panel.offsetY();
	}

	void draw() {
		if (drawn)
			return;
		drawn = true;
		isExpanded = false;
		container = panel.add().panel().horizontal();
		IClickable<?> clickable = container;
		clickable.addClickListener(this);
		content = container.add().panel().horizontal().spacing(2);
		content.addSpace(depth * INDENT);
		image = content.add().image();
		if (tree.childCount() != 0) {
			if (icon() != null) {
				image.resource(!tree.isLoaded() ? getOpenOrClosedIcon()
						: CLOSED);
				icon = content.add().image().resource(icon());
				icon.addClickListener(this);
			} else
				image.resource(FOLDER_CLOSED);
		} else {
			if (icon() != null) {
				image.resource(!tree.isLoaded() ? getOpenOrClosedIcon() : EMPTY);
				icon = content.add().image().resource(icon());
				icon.addClickListener(this);
			} else
				image.resource(tree.isLeaf() ? LEAF : FOLDER_EMPTY);
		}
		image.addClickListener(new LazyClickListener() {
			@Override
			protected void onAllowedClick() {
				expandCollapse(true, childrenExpanded);
				widget.model.selection(tree);
			}
		});
		content.addSpace(2);
		String name = tree.name();
		boolean isNull = name == null || name.trim().equals("");
		label = content.add().label().text(isNull ? "unnamed" : name);
		label.font().pixel(12);
		buttonPanel = content.addSpace(4).add().panel().horizontal()
				.visible(false);
		if (isNull)
			label.font().weight().italic().color().gray();
		label.addClickListener(this);
		content.addSpace(10);
		childrenPanel = panel.add().panel().vertical();
		if (tree.children().size() != 0)
			expandLoadedNode();
		decorate();
	}

	String getOpenOrClosedIcon() {
		if (tree.isLeaf())
			return EMPTY;
		return OPENORCLOSED;
	}

	void decorate() {
		if (widget.model.isCutCopy(this)) {
			container.border().style().dotted();
		} else
			container.border().color().white();
		if (tree != null && tree.decorator() != null)
			tree.decorator().decorate(label);
		buttonPanel.clear();
		injectTreeListener();
		if (tree.isMovable()) {
			addMoveButtons();
		}
	}

	void addMoveButtons() {
		int index = tree.parent().children().indexOf(tree);
		int num = tree.parent().children().size();
		if (gridButton != null)
			buttonPanel.addSpace(4);
		boolean allowMove = widget.moveActive && widget.model.allowMove(tree)
				&& widget.model.isSelected(tree);
		IHorizontalPanel movePanel = buttonPanel;
		moveTop = movePanel.add().image().resource("top.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveTop(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParentAfterMove();
							}
						});
					}
				}).mouseLeft().visible(allowMove);
		moveTop.clickable(index > 0);
		movePanel.addSpace(4);
		moveUp = movePanel.add().image().resource("up.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveUp(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParentAfterMove();
							}
						});
					}
				}).mouseLeft().visible(allowMove);
		moveUp.clickable(index > 0);
		movePanel.addSpace(4);
		moveDown = movePanel.add().image().resource("down.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveDown(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParentAfterMove();
							}
						});
					}
				}).mouseLeft().visible(allowMove);
		moveDown.clickable(index < num - 1);
		movePanel.addSpace(4);
		moveBottom = movePanel.add().image().resource("bottom.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						tree.moveBottom(new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								updateParentAfterMove();
							}
						});
					}
				}).mouseLeft().visible(allowMove);
		moveBottom.clickable(index < num - 1);
		movePanel.addSpace(4);
		acceptMove = movePanel.add().image().resource("accept.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						widget.model.moveStop();
					}
				}).mouseLeft().visible(allowMove);
	}

	private void updateParentAfterMove() {
		widget.model.refresh(tree.parent(), true);
		// widget.notifyUpdate(widget.model.selection().object());
	}

	@SuppressWarnings("unchecked")
	private void injectTreeListener() {
		if (widget.treeClickListener != null) {
			gridButton = buttonPanel.add().image().resource("grid.png")
					.tooltip("Double-Click to switch to Grid");
			IKey<?> key = gridButton.addClickListener(new LazyClickListener() {

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

	ModelTreeNode<T> refresh(boolean refreshChildren) {
		update(null);
		if (refreshChildren)
			expand();
		return this;
	}

	@Override
	public void onAllowedClick() {
		expandCollapse();
		widget.model.selection(tree);
	}

	void expandCollapse() {
		expandCollapse(childrenExpanded);
	}

	void expandCollapse(boolean childrenExpanded) {
		expandCollapse(false, childrenExpanded);
	}

	void expandCollapse(boolean expandNoMatterWhat, boolean childrenExpanded) {
		if (!expandNoMatterWhat
				&& (widget.model.selection() != null && !tree.object().equals(
						widget.model.selection().object()))) {
			return;
		}
		if (tree.childCount() == 0)
			return;
		if (!childrenExpanded) {
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
		tree.loadChildren(lCallback);
	}

	protected void expandLoadedNode() {
		clear();
		childrenExpanded = true;
		for (ITree<T> child : tree.children()) {
			widget.newNode(widget, childrenPanel, child, depth + 1, null,
					false, true);
		}
		if (tree.childCount() > 0) {
			isExpanded = true;
			if (icon() != null) {
				image.resource(OPEN);
				icon.resource(icon());
			} else
				image.resource(FOLDER_OPEN);
		}
	}

	void clear() {
		if (!childrenExpanded)
			return;
		childrenExpanded = false;
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
		draw();
		if (!selected)
			container.color().white();
		else {
			container.color().rgb(0xD0, 0xE4, 0xF6);
		}
		if (selected)
			widget.scrollIntoView(this);
		buttonPanel.visible(selected);
		boolean allowMove = widget.moveActive && selected
				&& widget.model.allowMove(tree);
		if (moveUp != null) {
			moveUp.visible(allowMove);
			moveDown.visible(allowMove);
			moveTop.visible(allowMove);
			moveBottom.visible(allowMove);
			acceptMove.visible(allowMove);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(tree.object());
	}
}
