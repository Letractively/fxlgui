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
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

public class DialogImpl implements IDialog {

	private class DialogButtonImpl implements IDialogButton {

		private List<IClickListener> listeners = new LinkedList<IClickListener>();
		private String imageResource;
		private String text;
		private CommandLink l;

		@Override
		public IDialogButton imageResource(String imageResource) {
			this.imageResource = imageResource;
			return this;
		}

		@Override
		public IDialogButton text(String text) {
			this.text = text;
			return this;
		}

		@Override
		public IDialogButton ok() {
			return imageResource(Icons.ACCEPT).text("Ok");
		}

		@Override
		public IDialogButton close() {
			return imageResource(Icons.CANCEL).text("Close");
		}

		@Override
		public IDialogButton yes() {
			return imageResource(Icons.ACCEPT).text("Yes");
		}

		@Override
		public IDialogButton no() {
			return imageResource(Icons.CANCEL).text("No");
		}

		@Override
		public IDialogButton cancel() {
			return imageResource(Icons.CANCEL).text("Cancel");
		}

		@Override
		public IDialogButton addClickListener(final IClickListener l) {
			listeners.add(new IClickListener() {

				@Override
				public void onClick() {
					DialogImpl.this.visible(false);
					l.onClick();
				}
			});
			return this;
		}

		@Override
		public IDialogButton clickable(boolean clickable) {
			if (l != null)
				l.clickable(clickable);
			return this;
		}

		public void link(CommandLink l) {
			this.l = l;
		}
	}

	private IDisplay display;
	private boolean modal = true;
	private String title = "Dialog";
	protected String message;
	protected String type = "Information";
	private IPopUp popUp;
	private List<DialogButtonImpl> buttons = new LinkedList<DialogButtonImpl>();
	private IContainer container;
	protected int width = -1;
	private int height = -1;

	public DialogImpl(IDisplay display) {
		this.display = display;
		confirm();
	}

	@Override
	public IDialog modal(boolean modal) {
		this.modal = modal;
		return this;
	}

	@Override
	public IDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IType message(String message) {
		this.message = message;
		return new IType() {

			private IDialog type(String string) {
				type = string;
				if (title == null)
					title(string);
				return DialogImpl.this;
			}

			@Override
			public IDialog error() {
				return type("Error");
			}

			@Override
			public IDialog info() {
				return type("Information");
			}

			@Override
			public IDialog warn() {
				return type("Warning");
			}
		};
	}

	@Override
	public IContainer container() {
		if (container == null) {
			if (title != null || !buttons.isEmpty())
				getPopUp();
			else {
				popUp = display.showPopUp().modal(modal);
				popUp.border().style().shadow();
				container = popUp.container();
			}
		}
		return container;
	}

	IPopUp getPopUp() {
		if (popUp == null) {
			popUp = display.showPopUp().modal(modal).autoHide(false);
			popUp.border().style().shadow();
			if (width != -1 && height != -1) {
				popUp.size(width, height);
			} else if (width != -1) {
				popUp.width(width);
			} else if (height != -1)
				popUp.height(height);
			popUp.center();
			IVerticalPanel panel = popUp.container().panel().vertical();
			WidgetTitle.decorateBorder(panel.spacing(1).border().color());
			WidgetTitle t = new WidgetTitle(panel.add().panel())
					.foldable(false).space(0);
			t.addTitle(title.toUpperCase());
			t.addTitleSpace();
			if (message != null && buttons.isEmpty()) {
				addButton().ok().addClickListener(new IClickListener() {
					@Override
					public void onClick() {
					}
				});
			}
			for (DialogButtonImpl b : buttons) {
				CommandLink l = t.addHyperlink(b.imageResource, b.text);
				for (IClickListener cl : b.listeners)
					l.addClickListener(cl);
				b.link(l);
			}
			IContainer content = t.content();
			if (message != null) {
				createLabel(content);
				decorate(popUp, panel);
			} else
				container = content;
		}
		return popUp;
	}

	protected void createLabel(IContainer content) {
		IGridPanel grid = content.panel().vertical().add().panel()
				.grid().spacing(10).resize(2, 1);
		if (width != -1)
			decorate(grid);
		grid.cell(0, 0).align().begin().valign().begin().image()
				.resource(image(type)).size(16, 16);
		IGridCell c = grid.cell(1, 0).valign().center();
		if (width != -1)
			c.width(width - 3 * 10 - 16);
		c.label().text(message).autoWrap(true);
	}

	protected void decorate(IPopUp popUp, IVerticalPanel panel) {
	}

	protected void decorate(IGridPanel grid) {
	}

	@Override
	public IDialog visible(boolean visible) {
		getPopUp().visible(visible);
		return this;
	}

	protected String image(String type2) {
		if (type.equals("Information"))
			return Icons.INFO;
		else if (type.equals("Warning"))
			return Icons.SKIP;
		else if (type.equals("Error"))
			return Icons.CANCEL;
		else
			throw new MethodNotImplementedException();
	}

	@Override
	public IDialogButton addButton() {
		DialogButtonImpl button = new DialogButtonImpl();
		buttons.add(button);
		return button;
	}

	@Override
	public IDialog confirm() {
		return title("Please Confirm");
	}

	@Override
	public IDialog size(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	@Override
	public IDialog width(int width) {
		this.width = width;
		return this;
	}

	@Override
	public IDialog height(int height) {
		this.height = height;
		return this;
	}
}