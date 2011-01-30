package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;

class CommandButtons implements ICommandButtons, IButtonPanelDecorator,
		ISelectionListener<Object> {

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
			if (l != null)
				l.onClick(selection, false, new CallbackTemplate<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if (result)
							execute();
					}
				});
			else
				execute();
		}

		private void execute() {
			int index = widget.rows.find(selection);
			int secondIndex;
			if (inc == Integer.MIN_VALUE)
				secondIndex = 0;
			else if (inc == Integer.MAX_VALUE)
				secondIndex = widget.rows.size() - 1;
			else
				secondIndex = index + inc;
			widget.rows.swap(index, secondIndex);
			widget.update();
			updateButtons(secondIndex);
		}
	}

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
	private Object selection;
	private IHorizontalPanel panel;
	private IClickable<?> imageUp;
	private IClickable<?> imageDown;
	private int selectionIndex;
	private IButton remove;

	CommandButtons(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		widget.buttonPanel(this);
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
	public void decorate(IGridCell container) {
		widget.selection().single().addSelectionListener(this);
		panel = container.panel().horizontal().align().end().add().panel()
				.horizontal().align().end().spacing(4);
		if (listenOnAdd) {
			IButton image = panel.add().button().text("Add");
			image.addClickListener(new Update(listenOnAddListener));
		}
		if (listenOnRemove) {
			remove = panel.add().button().text("Remove");
			remove.addClickListener(new Update(listenOnRemoveListener));
			remove.clickable(false);
		}
		if (listenOnMoveUp) {
			imageUp = addMoveImage("up.png", listenOnMoveUpListener, -1);
		}
		if (listenOnMoveDown) {
			imageDown = addMoveImage("down.png", listenOnMoveDownListener, 1);
		}
		if (listenOnShow) {
			throw new MethodNotImplementedException();
		}
	}

	private IClickable<?> addMoveImage(String resource,
			IMoveRowListener<Boolean> listenOnMoveUpListener2, int i) {
		IImage image = panel.add().image().resource(resource);
		image.addClickListener(new Move(listenOnMoveUpListener2, i))
				.mouseLeft();
		image.addClickListener(
				new Move(listenOnMoveUpListener2, i == -1 ? Integer.MIN_VALUE
						: Integer.MAX_VALUE)).doubleClick();
		image.clickable(false);
		return image;
	}

	@Override
	public void onSelection(Object selection) {
		this.selection = selection;
		selectionIndex = widget.rows.find(selection);
		updateButtons();
	}

	private void updateButtons() {
		updateButtons(selectionIndex);
	}

	private void updateButtons(int index) {
		if (imageUp != null)
			imageUp.clickable(index > 0 && selection != null);
		if (imageDown != null) {
			boolean c = index < widget.rows.size() - 1 && selection != null;
			imageDown.clickable(c);
		}
		if (remove != null) {
			boolean c = selection != null;
			remove.clickable(c);
		}
	}
}
