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

import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.IResizableWidget.Size;
import co.fxl.gui.impl.PopUp.TransparentPopUp;
import co.fxl.gui.style.impl.Style;

public class PopUpPageContainer implements PageContainer, RuntimeConstants {

	private IPopUp popUp;
	private IVerticalPanel panel;
	private ResizableWidgetTemplate widget;
	private Size popUpSize;
	private boolean isModal;
	private boolean added;
	private IServerListener oldListener;
	private TransparentPopUp closablePopUp;
	private boolean statePushed;

	// private Runnable cl;

	public PopUpPageContainer(ResizableWidgetTemplate widget, Size popUpSize,
			boolean isModal) {
		this.popUpSize = popUpSize;
		this.isModal = isModal;
		parent(widget);
	}

	public void parent(ResizableWidgetTemplate widget) {
		this.widget = widget;
	}

	@Override
	public IVerticalPanel panel(Runnable cl, boolean noDiscardChanges) {
		closablePopUp = PopUp.showClosablePopUp(true, cl, false,
				noDiscardChanges);
		popUp = closablePopUp.popUp.glass(Style.instance().glass()).autoHide(
				true);
		if (isModal)
			popUp.modal(true);
		panel = closablePopUp.panel;
		resize();
		popUp.visible(true);
		oldListener = ServerListener.instance;
		if (NOT_SWING)
			ServerListener.instance = new ServerCallCounter(true) {

				@Override
				protected void notifyCallPending() {
					closablePopUp.ignoreNotify = true;
					popUp.visible(false);
					closablePopUp.ignoreNotify = false;
				}

				@Override
				protected void notifyAllReturned() {
					popUp.visible(true);
				}
			};
		return panel;
	}

	@Override
	public void visible(boolean visible) {
		if (visible) {
			ServerListener.instance = oldListener;
			if (!statePushed) {
				closablePopUp.show();
				statePushed = true;
			}
			if (!added) {
				widget.addResizableWidgetToDisplay(popUp);
				added = true;
			}
		} else {
			statePushed = false;
		}
		if (popUp.visible() == visible)
			return;
		// if (visible) {
		// PopUp.adp.notifyShowPopUp(new Runnable() {
		// @Override
		// public void run() {
		// popUp.visible(false);
		// if (cl != null)
		// cl.run();
		// }
		// });
		// }
		popUp.visible(visible);
	}

	@Override
	public void resize() {
		int w = Math.max(popUpSize.minWidth(), Shell.instance().dwidth()
				- popUpSize.widthDecrement());
		int h = Math.max(popUpSize.minHeight(), Shell.instance().dheight()
				- popUpSize.heightDecrement());
		panel.size(w, h);
		int x = (Shell.instance().dwidth() - w) / 2;
		int y = (Shell.instance().dheight() - h) / 2;
		popUp.size(w, h).offset(x, y);
	}

	public void addCloseListener(final Runnable runnable) {
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value && !closablePopUp.destroyed) {
					statePushed = false;
					runnable.run();
				}
			}
		});
	}

	public boolean visible() {
		return popUp.visible();
	}

	@Override
	public void destroy() {
		ServerListener.instance = oldListener;
		closablePopUp.destroyed = true;
		statePushed = false;
		popUp.visible(false);
	}
}