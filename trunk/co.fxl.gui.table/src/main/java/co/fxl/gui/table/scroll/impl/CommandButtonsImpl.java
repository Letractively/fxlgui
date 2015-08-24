/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.table.scroll.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.impl.ContextMenu.Entry;
import co.fxl.gui.impl.ContextMenu.Group;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.IToolbar.IToolbarElement;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMultiRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IScrollTableClickListener;

public class CommandButtonsImpl implements ICommandButtons<Object>,
		IButtonPanelDecorator, IChangeListener<Object>, RuntimeConstants {

	// class Edit {
	//
	// private int previousEdit = -1;
	//
	// Edit() {
	// edit = clickable(panel.add(), "Edit", true);
	// edit.addClickListener(new IClickListener() {
	//
	// @Override
	// public void onClick() {
	// if (previousEdit != -1) {
	// // TODO save
	// widget.editable(previousEdit, false);
	// }
	// previousEdit = selectionIndex();
	// widget.editable(previousEdit, true);
	// clickable(edit, "Save");
	// // TODO validation
	// }
	// });
	// edit.clickable(!widget.preselectedList.isEmpty());
	// widget.selection().single()
	// .addSelectionListener(new ISelectionListener<Object>() {
	//
	// @Override
	// public void onSelection(int index, Object selection) {
	// if (previousEdit != -1) {
	// widget.editable(previousEdit, false);
	// clickable(edit, "Edit");
	// }
	// }
	// });
	// }
	// }

	private static final String REMOVE2 = "Remove";
	private static final String ADD = "Add";
	private static final String BOTTOM = "Bottom";
	private static final String DOWN = "Down";
	private static final String UP = "Up";
	private static final String TOP = "Top";
	private static final String EDIT2 = "Edit";
	private static final String SHOW2 = "Show";

	private int selectionIndex() {
		if (selectionList.isEmpty())
			return -1;
		return widget.rows.find(lastSelected());
	}

	private Object lastSelected() {
		if (selectionList.isEmpty())
			return null;
		return selectionList.get(selectionList.size() - 1);
	}

	private final class Update implements IClickListener {

		private IRowListener<IRows<Object>> l;
		private boolean deleteSelection;
		private IMultiRowListener<IRows<Object>> l2;

		Update(IRowListener<IRows<Object>> l) {
			this(l, false);
		}

		Update(IMultiRowListener<IRows<Object>> l2, boolean deleteSelection) {
			this.l2 = l2;
			this.deleteSelection = deleteSelection;
		}

		Update(IRowListener<IRows<Object>> l, boolean deleteSelection) {
			this.l = l;
			this.deleteSelection = deleteSelection;
		}

		@Override
		public void onClick() {
			CallbackTemplate<IRows<Object>> callback = new CallbackTemplate<IRows<Object>>() {
				@Override
				public void onSuccess(IRows<Object> result) {
					// selectionIndex = -1;
					execute(result);
					if (deleteSelection) {
						widget.selection().clear();
						// selectionIndex = -1;
					}
					updateButtons();
				}
			};
			if (l != null) {
				Object s = lastSelected();
				int i = selectionIndex();
				l.onClick(s, i, callback);
			} else if (l2 != null) {
				l2.onClick(selectionList, callback);
			} else {
				callback.onSuccess(null);
			}
		}

		private void execute(IRows<Object> result) {
			// widget.listeners.clear();
			widget.visible(result);
		}
	}

	private final class Move implements IClickListener {

		private IMoveRowListener<IRows<Object>> l;
		private int inc;

		Move(IMoveRowListener<IRows<Object>> l, int inc) {
			this.l = l;
			this.inc = inc;
		}

		@Override
		public void onClick() {
			int index = selectionIndex();
			int secondIndex;
			if (inc == Integer.MIN_VALUE)
				secondIndex = 0;
			else if (inc == Integer.MAX_VALUE)
				secondIndex = widget.rows.size() - 1;
			else
				secondIndex = index + inc;
			if (l != null)
				l.onClick(index, lastSelected(), inc == Integer.MAX_VALUE
						|| inc == Integer.MIN_VALUE,
						new CallbackTemplate<IRows<Object>>() {
							@Override
							public void onSuccess(IRows<Object> result) {
								assert result != null;
								// if (result == null) {
								// widget.refresh();
								// return;
								// }
								widget.rows(result);
								widget.update();
							}
						});
			else {
				widget.rows.swap(index, secondIndex);
				widget.notifySelection(secondIndex, lastSelected());
				widget.update();
				updateButtons();
			}
		}
	}

	public static Link clickableLabel(IToolbarElement c, String string,
			boolean showLabel) {
		Link link = new Link();
		link = link.clickableLink(c, string).showLabel(showLabel);
		return link;
	}

	public Link clickable(IToolbarElement c, String string, boolean showLabel) {
		return clickableLabel(c, string, showLabel && showLabels);
	}

	// private static IClickable<?> clickable(IClickable<?> c, String string) {
	// Link l = (Link) c;
	// l.image.resource(string.toLowerCase() + ".png");
	// l.label.text(string);
	// return l;
	// }

	private final IDecorator DEFAULT_DECORATOR = new IDecorator() {
		@Override
		public IClickable<?> decorate(IToolbar c) {
			String string = ADD;
			return clickable(c.addElement(), string, true);
		}
	};
	private static boolean ALIGN_END = ToolbarImpl.ALLOW_ALIGN_END_FOR_FLOW_PANEL
			&& Constants.get("CommandButtonsImpl.ALIGN_END", false);
	private ScrollTableWidgetImpl widget;
	private boolean listenOnAdd;
	private boolean listenOnRemove;
	private boolean listenOnMoveUp;
	private boolean listenOnMoveDown;
	private boolean listenOnShow;
	private IRowListener<IRows<Object>> listenOnAddListener;
	private IMultiRowListener<IRows<Object>> listenOnRemoveListener;
	private IMoveRowListener<IRows<Object>> listenOnMoveUpListener;
	private IMoveRowListener<IRows<Object>> listenOnMoveDownListener;
	private IRowListener<IRows<Object>> listenOnShowListener;
	// int selectionIndex = -1;
	List<Object> selectionList = new LinkedList<Object>();
	private IToolbar panel;
	private IClickable<?> imageUp;
	private IClickable<?> imageDown;
	private IClickable<?> remove;
	private IDecorator listenOnAddListenerDecorator = DEFAULT_DECORATOR;
	private IClickable<?> edit;
	private boolean listenOnEdit;
	private IRowListener<IRows<Object>> listenOnEditListener;
	private IClickable<?> imageUpMax;
	private IClickable<?> imageDownMax;
	private IClickable<?> show;
	private ContextMenu contextMenu;
	private Entry addContext;
	private Entry removeContext;
	private Entry showContext;
	private Entry editContext;
	private Entry topContext;
	private Entry upContext;
	private Entry downContext;
	private Entry bottomContext;
	private IRowListener<IRows<Object>> listenOnExpand;
	private boolean showLabels = true;
	private boolean tableClickListenerAdded;

	CommandButtonsImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		showLabels = widget.showLabels;
		widget.buttonPanel(this);
		selectionList(widget.preselectedList);
	}

	void setSpace(int i) {
		Link.SPACE = i;
	}

	@Override
	public ICommandButtons<Object> listenOnAdd(IDecorator dec,
			IRowListener<IRows<Object>> l) {
		listenOnAdd = true;
		listenOnAddListenerDecorator = dec;
		// widget.showNoRowsFound = false;
		listenOnAddListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnAdd(IRowListener<IRows<Object>> l) {
		listenOnAdd = true;
		listenOnAddListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnRemove(
			IMultiRowListener<IRows<Object>> l) {
		listenOnRemove = true;
		listenOnRemoveListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnMoveUp(
			IMoveRowListener<IRows<Object>> l) {
		showLabels = false;
		listenOnMoveUp = true;
		listenOnMoveUpListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnMoveDown(
			IMoveRowListener<IRows<Object>> l) {
		showLabels = false;
		listenOnMoveDown = true;
		listenOnMoveDownListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnShow(
			final IRowListener<IRows<Object>> l) {
		listenOnShow = true;
		listenOnShowListener = l;
		doubleClickListener(l);
		return this;
	}

	private void doubleClickListener(final IRowListener<IRows<Object>> l) {
		widget.addTableClickListener(new IScrollTableClickListener() {

			@Override
			public void onClick(Object identifier, int rowIndex) {
				l.onClick(identifier, rowIndex,
						new CallbackTemplate<IRows<Object>>() {

							@Override
							public void onSuccess(IRows<Object> result) {
							}
						});
			}
		}).doubleClick();
	}

	@Override
	public ICommandButtons<Object> listenOnEdit(IRowListener<IRows<Object>> l) {
		listenOnEdit = true;
		listenOnEditListener = l;
		// doubleClickListener(l);
		return this;
	}

	@Override
	public void decorate(IGridCell container) {
		widget.selection().multi().addChangeListener(this);
		// IHorizontalPanel ps = container.panel().horizontal().align().end()
		// .add().panel().horizontal().align().end();
		panel = new ToolbarImpl(container);
		panel.margin().right(4);
		if (ALIGN_END) {
			panel.align().end();// ps.add());
			addRemove();
			addAdd();
		} else {
			addAdd();
			addRemove();
		}
		if (listenOnShow) {
			show = clickable(panel.addElement(), SHOW2, true);
			show.addClickListener(showListener());
			show.clickable(!widget.preselectedList.isEmpty());
		}
		if (listenOnEdit) {
			edit = clickable(panel.addElement(), EDIT2, true);
			final Update clickListener = editListener();
			edit.addClickListener(clickListener);
			edit.clickable(!widget.preselectedList.isEmpty());
			if (!tableClickListenerAdded) {
				widget.addTableClickListener(new IScrollTableClickListener() {

					@Override
					public void onClick(Object identifier, int rowIndex) {
						widget.rows.selected(rowIndex);
						selectionList.add(identifier);
						// selectionIndex = rowIndex;
						clickListener.onClick();
					}
				}).doubleClick();
				tableClickListenerAdded = true;
			}
		}
		if (listenOnMoveUp) {
			imageUpMax = addMoveImage(IBulkTableWidget.ARROW_UP,
					listenOnMoveUpListener, Integer.MIN_VALUE);
			imageUp = addMoveImage(IBulkTableWidget.ARROW_UP,
					listenOnMoveUpListener, -1);
		}
		if (listenOnMoveDown) {
			imageDown = addMoveImage(IBulkTableWidget.ARROW_DOWN,
					listenOnMoveDownListener, 1);
			imageDownMax = addMoveImage(IBulkTableWidget.ARROW_DOWN,
					listenOnMoveDownListener, Integer.MAX_VALUE);
		}
		if (listenOnExpand != null) {
			Link l = clickable(panel.addElement(), "More", true);
			l.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					listenOnExpand.onClick(selectionList.isEmpty() ? null
							: selectionList.get(0), selectionIndex(),
							new CallbackTemplate<IRows<Object>>() {
								@Override
								public void onSuccess(IRows<Object> result) {
									throw new UnsupportedOperationException();
								}
							});
				}
			});
		}
		// boolean editable = false;
		// for (ScrollTableColumnImpl c : widget.columns) {
		// if (c.editable)
		// editable = true;
		// }
		// if (editable) {
		// new Edit();
		// }
		panel.adjustHeights();
	}

	private Update showListener() {
		return new Update(listenOnShowListener);
	}

	private Update editListener() {
		return new Update(listenOnEditListener);
	}

	void addRemove() {
		if (listenOnRemove) {
			// remove = panel.add().button().text("Remove");
			remove = clickable(panel.addElement(), REMOVE2, true);
			remove.addClickListener(removeListener());
			remove.clickable(false);
		}
	}

	private Update removeListener() {
		return new Update(listenOnRemoveListener, true);
	}

	void addContextMenu(IBulkTableWidget grid) {
		if (contextMenu == null) {
			contextMenu = grid.contextMenu();
			Group group = contextMenu.group(widget.title);
			if (listenOnAdd) {
				addContext = group.addEntry(ADD).imageResource("add.png");
				addContext.addClickListener(addListener());
			}
			if (listenOnRemove) {
				removeContext = group.addEntry(REMOVE2).imageResource(
						"cancel.png");
				removeContext.addClickListener(removeListener());
			}
			if (listenOnShow) {
				showContext = group.addEntry(SHOW2).imageResource("detail.png");
				showContext.addClickListener(showListener());
			}
			if (listenOnEdit) {
				editContext = group.addEntry(EDIT2).imageResource("detail.png");
				editContext.addClickListener(editListener());
			}
			if (listenOnMoveUp) {
				topContext = group.addEntry(TOP).imageResource("top.png");
				topContext.addClickListener(new Move(listenOnMoveUpListener,
						Integer.MIN_VALUE));
				upContext = group.addEntry(UP).imageResource("up.png");
				upContext
						.addClickListener(new Move(listenOnMoveUpListener, -1));
			}
			if (listenOnMoveDown) {
				downContext = group.addEntry(DOWN).imageResource("down.png");
				downContext.addClickListener(new Move(listenOnMoveDownListener,
						1));
				bottomContext = group.addEntry(BOTTOM).imageResource(
						"bottom.png");
				bottomContext.addClickListener(new Move(
						listenOnMoveDownListener, Integer.MAX_VALUE));
			}
			updateButtons();
		} else {
			grid.contextMenu(contextMenu);
		}
	}

	// private boolean showLabel() {
	// // boolean b = !listenOnMoveDown && !listenOnMoveUp;
	// return true;
	// }

	void addAdd() {
		if (listenOnAdd) {
			IClickable<?> image = listenOnAddListenerDecorator.decorate(panel);
			image.addClickListener(addListener());
		}
	}

	Update addListener() {
		return new Update(listenOnAddListener);
	}

	private IClickable<?> addMoveImage(String resource,
			IMoveRowListener<IRows<Object>> listenOnMoveUpListener2, int i) {
		String res = null;
		// resource
		// + (i == Integer.MAX_VALUE || i == Integer.MIN_VALUE ? resource
		// : "");
		if (resource.equals(IBulkTableWidget.ARROW_UP)
				&& i == Integer.MIN_VALUE)
			res = TOP;
		if (resource.equals(IBulkTableWidget.ARROW_UP) && i == -1)
			res = UP;
		if (resource.equals(IBulkTableWidget.ARROW_DOWN) && i == 1)
			res = DOWN;
		if (resource.equals(IBulkTableWidget.ARROW_DOWN)
				&& i == Integer.MAX_VALUE)
			res = BOTTOM;
		IClickable<?> image = clickable(panel.addElement(), res, false);
		// image.font().weight().bold();
		image.addClickListener(new Move(listenOnMoveUpListener2, i));
		boolean canClick = widget.preselectedList.size() == 1;
		if (canClick) {
			canClick &= i > 0 || widget.preselectedIndex() > 0;
			canClick &= i < 0
					|| widget.preselectedIndex() < widget.rows.size() - 1;
		}
		image.clickable(canClick);
		return image;
	}

	@Override
	public void onChange(List<Object> selection) {
		selectionList = selection;
		// selectionIndex = index;
		updateButtons();
	}

	void updateButtons() {
		// if (selectionIndex == -1 && selection != null) {
		// selectionIndex = widget.rows.find(selection);
		// }
		// updateButtons(selectionIndex);
		// }
		//
		// private void updateButtons(int index) {
		int index = selectionIndex();
		if (imageUp != null) {
			boolean up = index > 0 && selectionList.size() == 1;
			imageUp.clickable(up);
			imageUpMax.clickable(up);
			if (upContext != null) {
				upContext.clickable(up);
				topContext.clickable(up);
			}
		}
		if (imageDown != null) {
			boolean c = index < widget.rows.size() - 1
					&& selectionList.size() == 1;
			imageDownMax.clickable(c);
			imageDown.clickable(c);
			if (downContext != null) {
				downContext.clickable(c);
				bottomContext.clickable(c);
			}
		}
		if (remove != null) {
			boolean c = selectionList.size() >= 1;
			if (c)
				for (Object o : selectionList)
					c &= widget.rows.deletable(widget.rows.find(o));
			remove.clickable(c);
			if (removeContext != null) {
				removeContext.clickable(c);
			}
		}
		if (show != null) {
			boolean c = lastSelected() != null && selectionList.size() == 1;
			show.clickable(c);
			if (showContext != null) {
				showContext.clickable(c);
			}
		}
		if (edit != null) {
			boolean c = lastSelected() != null && selectionList.size() == 1;
			edit.clickable(c);
			if (editContext != null) {
				editContext.clickable(c);
			}
		}
	}

	int getSelectionIndex() {
		return selectionList.isEmpty() ? -1 : widget.rows.find(lastSelected());
	}

	public void reset() {
		selectionList.clear();
		// selectionIndex = -1;
	}

	void selectionList(List<Object> preselectedList) {
		if (preselectedList != null && !preselectedList.isEmpty()
				&& widget.rows != null) {
			selectionList = new LinkedList<Object>();
			for (Object o : preselectedList)
				if (widget.rows.find(o) != -1)
					selectionList.add(o);
		}
	}

	@Override
	public ICommandButtons<Object> listenOnExpand(
			IRowListener<IRows<Object>> iRowListener) {
		listenOnExpand = iRowListener;
		return this;
	}
}
