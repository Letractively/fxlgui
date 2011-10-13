/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IScrollTableClickListener;

public class CommandButtonsImpl implements ICommandButtons<Object>,
		IButtonPanelDecorator, ISelectionListener<Object> {

	// TODO FEATURE: Option: Usability: Enable Drag & Drop for reordering of
	// table content (additionally to move buttons)

	class Edit {

		private int previousEdit = -1;

		Edit() {
			edit = clickable(panel.add(), "Edit");
			edit.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					if (previousEdit != -1) {
						// TODO save
						widget.editable(previousEdit, false);
					}
					previousEdit = selectionIndex;
					widget.editable(previousEdit, true);
					clickable(edit, "Save");
					// TODO validation
				}
			});
			edit.clickable(!widget.preselectedList.isEmpty());
			widget.selection().single()
					.addSelectionListener(new ISelectionListener<Object>() {

						@Override
						public void onSelection(int index, Object selection) {
							if (previousEdit != -1) {
								widget.editable(previousEdit, false);
								clickable(edit, "Edit");
							}
						}
					});
		}
	}

	private final class Update implements IClickListener {

		private IRowListener<IRows<Object>> l;
		private boolean deleteSelection;

		Update(IRowListener<IRows<Object>> l) {
			this(l, false);
		}

		Update(IRowListener<IRows<Object>> l, boolean deleteSelection) {
			this.l = l;
			this.deleteSelection = deleteSelection;
		}

		@Override
		public void onClick() {
			if (l != null) {
				Object s = selection;
				int i = selectionIndex;
				if (deleteSelection) {
					selection = null;
					selectionIndex = -1;
				}
				l.onClick(s, i, new CallbackTemplate<IRows<Object>>() {
					@Override
					public void onSuccess(IRows<Object> result) {
						selection = null;
						selectionIndex = -1;
						execute(result);
					}
				});
			} else
				execute(null);
		}

		private void execute(IRows<Object> result) {
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
			int index = selectionIndex;
			int secondIndex;
			if (inc == Integer.MIN_VALUE)
				secondIndex = 0;
			else if (inc == Integer.MAX_VALUE)
				secondIndex = widget.rows.size() - 1;
			else
				secondIndex = index + inc;
			if (l != null)
				l.onClick(index, selection, inc == Integer.MAX_VALUE
						|| inc == Integer.MIN_VALUE,
						new CallbackTemplate<IRows<Object>>() {
							@Override
							public void onSuccess(IRows<Object> result) {
								assert result != null;
								widget.rows(result);
								widget.update();
							}
						});
			else {
				widget.rows.swap(index, secondIndex);
				widget.notifySelection(secondIndex, selection);
				widget.update();
				updateButtons(secondIndex);
			}
		}
	}

	public static Link clickable(IContainer c, String string) {
		Link link = new Link();
		link = link.clickableLink(c, string);
		return link;
	}

	private static IClickable<?> clickable(IClickable<?> c, String string) {
		Link l = (Link) c;
		l.image.resource(string.toLowerCase() + ".png");
		l.label.text(string);
		return l;
	}

	private static final IDecorator DEFAULT_DECORATOR = new IDecorator() {
		@Override
		public IClickable<?> decorate(IPanel<?> c) {
			String string = "Add";
			return clickable(c.add(), string);
		}
	};
	private ScrollTableWidgetImpl widget;
	private boolean listenOnAdd;
	private boolean listenOnRemove;
	private boolean listenOnMoveUp;
	private boolean listenOnMoveDown;
	private boolean listenOnShow;
	private IRowListener<IRows<Object>> listenOnAddListener;
	private IRowListener<IRows<Object>> listenOnRemoveListener;
	private IMoveRowListener<IRows<Object>> listenOnMoveUpListener;
	private IMoveRowListener<IRows<Object>> listenOnMoveDownListener;
	private IRowListener<IRows<Object>> listenOnShowListener;
	int selectionIndex = -1;
	Object selection;
	private IHorizontalPanel panel;
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

	CommandButtonsImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		widget.buttonPanel(this);
		if (!widget.preselectedList.isEmpty()) {
			selectionIndex = widget.preselectedIndex;
			selection = widget.preselectedList.get(0);
		}
	}

	void setSpace(int i) {
		Link.SPACE = i;
	}

	@Override
	public ICommandButtons<Object> listenOnAdd(IDecorator dec,
			IRowListener<IRows<Object>> l) {
		listenOnAdd = true;
		listenOnAddListenerDecorator = dec;
		widget.showNoRowsFound = false;
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
	public ICommandButtons<Object> listenOnRemove(IRowListener<IRows<Object>> l) {
		listenOnRemove = true;
		listenOnRemoveListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnMoveUp(
			IMoveRowListener<IRows<Object>> l) {
		listenOnMoveUp = true;
		listenOnMoveUpListener = l;
		return this;
	}

	@Override
	public ICommandButtons<Object> listenOnMoveDown(
			IMoveRowListener<IRows<Object>> l) {
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
		doubleClickListener(l);
		return this;
	}

	@Override
	public void decorate(IGridCell container) {
		widget.selection().single().addSelectionListener(this);
		panel = container.panel().horizontal().align().end().add().panel()
				.horizontal().align().end().spacing(8);
		if (listenOnAdd) {
			IClickable<?> image = listenOnAddListenerDecorator.decorate(panel);
			image.addClickListener(new Update(listenOnAddListener));
		}
		if (listenOnRemove) {
			// remove = panel.add().button().text("Remove");
			remove = clickable(panel.add(), "Remove");
			remove.addClickListener(new Update(listenOnRemoveListener, true));
			remove.clickable(false);
		}
		if (listenOnShow) {
			show = clickable(panel.add(), "Show");
			show.addClickListener(new Update(listenOnShowListener));
			show.clickable(!widget.preselectedList.isEmpty());
		}
		if (listenOnEdit) {
			edit = clickable(panel.add(), "Edit");
			final Update clickListener = new Update(listenOnEditListener);
			edit.addClickListener(clickListener);
			edit.clickable(!widget.preselectedList.isEmpty());
			widget.addTableClickListener(new IScrollTableClickListener() {

				@Override
				public void onClick(Object identifier, int rowIndex) {
					widget.rows.selected(rowIndex);
					selection = identifier;
					selectionIndex = rowIndex;
					clickListener.onClick();
				}
			}).doubleClick();
		}
		if (listenOnMoveUp) {
			imageUpMax = addMoveImage(ScrollTableWidgetImpl.ARROW_UP,
					listenOnMoveUpListener, Integer.MIN_VALUE);
			imageUp = addMoveImage(ScrollTableWidgetImpl.ARROW_UP,
					listenOnMoveUpListener, -1);
		}
		if (listenOnMoveDown) {
			imageDown = addMoveImage(ScrollTableWidgetImpl.ARROW_DOWN,
					listenOnMoveDownListener, 1);
			imageDownMax = addMoveImage(ScrollTableWidgetImpl.ARROW_DOWN,
					listenOnMoveDownListener, Integer.MAX_VALUE);
		}
		// boolean editable = false;
		// for (ScrollTableColumnImpl c : widget.columns) {
		// if (c.editable)
		// editable = true;
		// }
		// if (editable) {
		// new Edit();
		// }
	}

	private IClickable<?> addMoveImage(String resource,
			IMoveRowListener<IRows<Object>> listenOnMoveUpListener2, int i) {
		String res = null;
		// resource
		// + (i == Integer.MAX_VALUE || i == Integer.MIN_VALUE ? resource
		// : "");
		if (resource.equals(ScrollTableWidgetImpl.ARROW_UP)
				&& i == Integer.MIN_VALUE)
			res = "Top";
		if (resource.equals(ScrollTableWidgetImpl.ARROW_UP) && i == -1)
			res = "Up";
		if (resource.equals(ScrollTableWidgetImpl.ARROW_DOWN) && i == 1)
			res = "Down";
		if (resource.equals(ScrollTableWidgetImpl.ARROW_DOWN)
				&& i == Integer.MAX_VALUE)
			res = "Bottom";
		IClickable<?> image = clickable(panel.add(), res);
		// image.font().weight().bold();
		image.addClickListener(new Move(listenOnMoveUpListener2, i));
		boolean canClick = widget.preselectedList.size() == 1;
		if (canClick) {
			canClick &= i > 0 || widget.preselectedIndex > 0;
			canClick &= i < 0
					|| widget.preselectedIndex < widget.rows.size() - 1;
		}
		image.clickable(canClick);
		return image;
	}

	@Override
	public void onSelection(int index, Object selection) {
		this.selection = selection;
		selectionIndex = index;
		updateButtons();
	}

	void updateButtons() {
		if (selectionIndex == -1 && selection != null) {
			selectionIndex = widget.rows.find(selection);
		}
		updateButtons(selectionIndex);
	}

	private void updateButtons(int index) {
		if (imageUp != null) {
			boolean up = index > 0 && selection != null;
			imageUp.clickable(up);
			imageUpMax.clickable(up);
		}
		if (imageDown != null) {
			boolean c = index < widget.rows.size() - 1 && selection != null;
			imageDownMax.clickable(c);
			imageDown.clickable(c);
		}
		if (remove != null) {
			boolean c = selection != null
					&& widget.rows
							.deletable(selectionIndex != -1 ? selectionIndex
									: widget.rows.find(selection));
			remove.clickable(c);
		}
		if (show != null) {
			boolean c = selection != null;
			show.clickable(c);
		}
		if (edit != null) {
			boolean c = selection != null;
			edit.clickable(c);
		}
	}

	public void reset() {
		selection = null;
		selectionIndex = -1;
	}
}
