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

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;

class CommandButtonsImpl implements ICommandButtons, IButtonPanelDecorator,
		ISelectionListener<Object> {

	class Edit {

		private int previousEdit = -1;

		Edit() {
			edit = panel.add().button().text("Edit");
			edit.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					if (previousEdit != -1) {
						// TODO save
						widget.editable(previousEdit, false);
					}
					previousEdit = selectionIndex;
					widget.editable(previousEdit, true);
					edit.text("Save");
					// TODO validation
				}
			});
			edit.clickable(widget.preselected != null);
			widget.selection().single()
					.addSelectionListener(new ISelectionListener<Object>() {

						@Override
						public void onSelection(int index, Object selection) {
							if (previousEdit != -1) {
								widget.editable(previousEdit, false);
								edit.text("Edit");
							}
						}
					});
		}
	}

	private final class Update implements IClickListener {

		private IRowListener<Boolean> l;

		Update(IRowListener<Boolean> l) {
			this.l = l;
		}

		@Override
		public void onClick() {
			if (l != null)
				l.onClick(selection, selectionIndex,
						new CallbackTemplate<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result != null && result)
									execute();
							}
						});
			else
				execute();
		}

		private void execute() {
			widget.visible(true);
		}
	}

	private final class Move implements IClickListener {

		private IMoveRowListener<Boolean> l;
		private int inc;

		Move(IMoveRowListener<Boolean> l, int inc) {
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
			widget.rows.swap(index, secondIndex);
			widget.notifySelection(secondIndex, selection);
			widget.update();
			updateButtons(secondIndex);
			if (l != null)
				l.onClick(index, selection, inc == Integer.MAX_VALUE
						|| inc == Integer.MIN_VALUE,
						new CallbackTemplate<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
							}
						});
		}
	}

	private static final IDecorator DEFAULT_DECORATOR = new IDecorator() {
		@Override
		public IClickable<?> decorate(IContainer c) {
			return c.button().text("Add");
		}
	};
	private ScrollTableWidgetImpl widget;
	private boolean listenOnAdd;
	private boolean listenOnRemove;
	private boolean listenOnMoveUp;
	private boolean listenOnMoveDown;
	private boolean listenOnShow;
	private IRowListener<Boolean> listenOnAddListener;
	private IRowListener<Boolean> listenOnRemoveListener;
	private IMoveRowListener<Boolean> listenOnMoveUpListener;
	private IMoveRowListener<Boolean> listenOnMoveDownListener;
	private IRowListener<Boolean> listenOnShowListener;
	private int selectionIndex;
	private Object selection;
	private IHorizontalPanel panel;
	private IClickable<?> imageUp;
	private IClickable<?> imageDown;
	private IButton remove;
	private IDecorator listenOnAddListenerDecorator = DEFAULT_DECORATOR;
	private IButton edit;
	private boolean listenOnEdit;
	private IRowListener<Boolean> listenOnEditListener;
	private IClickable<?> imageUpMax;
	private IClickable<?> imageDownMax;

	CommandButtonsImpl(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		widget.buttonPanel(this);
		if (widget.preselected != null) {
			selectionIndex = widget.preselectedIndex;
			selection = widget.preselected;
		}
	}

	@Override
	public ICommandButtons listenOnAdd(IDecorator dec, IRowListener<Boolean> l) {
		listenOnAdd = true;
		listenOnAddListenerDecorator = dec;
		widget.showNoRowsFound = false;
		listenOnAddListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnAdd(IRowListener<Boolean> l) {
		listenOnAdd = true;
		listenOnAddListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnRemove(IRowListener<Boolean> l) {
		listenOnRemove = true;
		listenOnRemoveListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnMoveUp(IMoveRowListener<Boolean> l) {
		listenOnMoveUp = true;
		listenOnMoveUpListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnMoveDown(IMoveRowListener<Boolean> l) {
		listenOnMoveDown = true;
		listenOnMoveDownListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnShow(IRowListener<Boolean> l) {
		listenOnShow = true;
		listenOnShowListener = l;
		return this;
	}

	@Override
	public ICommandButtons listenOnEdit(IRowListener<Boolean> l) {
		listenOnEdit = true;
		listenOnEditListener = l;
		return this;
	}

	@Override
	public void decorate(IGridCell container) {
		widget.selection().single().addSelectionListener(this);
		panel = container.panel().horizontal().align().end().add().panel()
				.horizontal().align().end().spacing(4);
		if (listenOnAdd) {
			IClickable<?> image = listenOnAddListenerDecorator.decorate(panel
					.add());
			image.addClickListener(new Update(listenOnAddListener));
		}
		if (listenOnRemove) {
			remove = panel.add().button().text("Remove");
			remove.addClickListener(new Update(listenOnRemoveListener));
			remove.clickable(widget.preselected != null);
		}
		if (listenOnShow) {
			throw new MethodNotImplementedException();
		}
		if (listenOnEdit) {
			edit = panel.add().button().text("Edit");
			edit.addClickListener(new Update(listenOnEditListener));
			edit.clickable(widget.preselected != null);
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
			IMoveRowListener<Boolean> listenOnMoveUpListener2, int i) {
		IButton image = panel
				.add()
				.button()
				.text(resource
						+ (i == Integer.MAX_VALUE || i == Integer.MIN_VALUE ? resource
								: ""));
		image.font().weight().bold();
		image.addClickListener(new Move(listenOnMoveUpListener2, i))
				.mouseLeft();
		boolean canClick = widget.preselected != null;
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

	private void updateButtons() {
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
			boolean c = selection != null;
			remove.clickable(c);
		}
		if (edit != null) {
			boolean c = selection != null;
			edit.clickable(c);
		}
	}
}
