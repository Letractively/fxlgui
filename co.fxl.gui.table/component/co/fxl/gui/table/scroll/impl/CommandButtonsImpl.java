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
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IScrollTableClickListener;

public class CommandButtonsImpl implements ICommandButtons,
		IButtonPanelDecorator, ISelectionListener<Object> {

	// TODO FEATURE: Option: Usability: Enable Drag & Drop for reordering of
	// table content (additionally to move buttons)

	static final int SPACE = 4;

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

		private IRowListener<Boolean> l;
		private boolean deleteSelection;

		Update(IRowListener<Boolean> l) {
			this(l, false);
		}

		Update(IRowListener<Boolean> l, boolean deleteSelection) {
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
				l.onClick(s, i, new CallbackTemplate<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						selection = null;
						selectionIndex = -1;
						if (result != null && result)
							execute();
					}
				});
			} else
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

	static class Link implements IClickable<Link> {

		private IImage image;
		private ILabel label;
		private IHorizontalPanel p;

		Link(IHorizontalPanel p, IImage image, ILabel label) {
			this.p = p;
			this.image = image;
			this.label = label;
		}

		@Override
		public Link clickable(boolean clickable) {
			p.clickable(clickable);
			image.clickable(clickable);
			label.clickable(clickable);
			if (clickable) {
				label.font().color().black();
				// label.font().underline(true);
			} else {
				label.font().color().gray();
				// label.font().underline(false);
			}
			return this;
		}

		@Override
		public boolean clickable() {
			return label.clickable();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<Link> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			p.addClickListener(clickListener);
			image.addClickListener(clickListener);
			label.addClickListener(clickListener);
			return null;
		}
	}

	public static IClickable<?> clickable(IContainer c, String string) {
		IHorizontalPanel p = c.panel().horizontal();
		// p.spacing(4);
		// p.color().gray();
		// p.border().color().gray();
		String imageR = string.toLowerCase();
		if (imageR.equals("remove"))
			imageR = "cancel";
		if (imageR.equals("edit") || imageR.equals("show"))
			imageR = "detail";
		IImage image = p.add().image().resource(imageR + ".png");
		p.addSpace(SPACE);
		ILabel label = p.add().label().text(string);
		Link l = new Link(p, image, label);
		l.clickable(true);
		return l;
	}

	private static IClickable<?> clickable(IClickable<?> c, String string) {
		Link l = (Link) c;
		l.image.resource(string.toLowerCase() + ".png");
		l.label.text(string);
		return l;
	}

	private static final IDecorator DEFAULT_DECORATOR = new IDecorator() {
		@Override
		public IClickable<?> decorate(IContainer c) {
			String string = "Add";
			return clickable(c, string);
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
	private IClickable<?> remove;
	private IDecorator listenOnAddListenerDecorator = DEFAULT_DECORATOR;
	private IClickable<?> edit;
	private boolean listenOnEdit;
	private IRowListener<Boolean> listenOnEditListener;
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
	public ICommandButtons listenOnShow(final IRowListener<Boolean> l) {
		listenOnShow = true;
		listenOnShowListener = l;
		doubleClickListener(l);
		return this;
	}

	private void doubleClickListener(final IRowListener<Boolean> l) {
		widget.addTableClickListener(new IScrollTableClickListener() {

			@Override
			public void onClick(Object identifier, int rowIndex) {
				l.onClick(identifier, rowIndex,
						new CallbackTemplate<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
							}
						});
			}
		}).doubleClick();
	}

	@Override
	public ICommandButtons listenOnEdit(IRowListener<Boolean> l) {
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
			IClickable<?> image = listenOnAddListenerDecorator.decorate(panel
					.add());
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
			IMoveRowListener<Boolean> listenOnMoveUpListener2, int i) {
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
}
