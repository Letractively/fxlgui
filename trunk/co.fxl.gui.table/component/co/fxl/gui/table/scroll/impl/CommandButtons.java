package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IInsert;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;

class CommandButtons implements ICommandButtons, IButtonPanelDecorator,
		ISelectionListener<Object> {

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
						if (true)
							execute();
					}
				});
			else
				execute();
		}

		private void execute() {
			int index = widget.rows.find(selection);
			widget.rows.swap(index, index + inc);
			widget.update();
			updateButtons(index + inc);
		}
	}

	private ScrollTableWidgetImpl widget;
	private boolean listenOnAdd;
	private boolean listenOnRemove;
	private boolean listenOnMoveUp;
	private boolean listenOnMoveDown;
	private boolean listenOnShow;
	private IRowListener<IInsert> listenOnAddListener;
	private IRowListener<Boolean> listenOnRemoveListener;
	private IMoveRowListener<Boolean> listenOnMoveUpListener;
	private IMoveRowListener<Boolean> listenOnMoveDownListener;
	private IRowListener<Boolean> listenOnShowListener;
	private Object selection;
	private IImage imageUp;
	private IHorizontalPanel panel;
	private IImage imageDown;

	CommandButtons(ScrollTableWidgetImpl widget) {
		this.widget = widget;
		widget.buttonPanel(this);
	}

	@Override
	public ICommandButtons listenOnAdd(IRowListener<IInsert> l) {
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
		if (listenOnMoveUp) {
			imageUp = addMoveImage("up.png", listenOnMoveUpListener, -1);
		}
		if (listenOnMoveDown) {
			imageDown = addMoveImage("down.png", listenOnMoveDownListener, 1);
		}
		if (listenOnAdd) {
			throw new MethodNotImplementedException();
		}
		if (listenOnRemove) {
			throw new MethodNotImplementedException();
		}
		if (listenOnShow) {
			throw new MethodNotImplementedException();
		}
	}

	private IImage addMoveImage(String resource,
			IMoveRowListener<Boolean> listenOnMoveUpListener2, int i) {
		IImage image = panel.add().image().resource(resource);
		image.addClickListener(new Move(listenOnMoveUpListener2, i))
				.mouseLeft();
		image.clickable(false);
		return image;
	}

	@Override
	public void onSelection(Object selection) {
		this.selection = selection;
		updateButtons();
	}

	private void updateButtons() {
		int index = widget.rows.find(selection);
		updateButtons(index);
	}

	private void updateButtons(int index) {
		if (imageUp != null)
			imageUp.clickable(index > 0 && selection != null);
		if (imageDown != null)
			imageDown
					.clickable(index < widget.rows.size() && selection != null);
	}
}
