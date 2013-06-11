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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.IResizableWidget.Size;
import co.fxl.gui.impl.PopUp.TransparentPopUp;

public class PopUpPageContainer implements PageContainer {

	private IPopUp popUp;
	private IVerticalPanel panel;
	private ResizableWidgetTemplate widget;
	private Size popUpSize;
	private boolean isModal;
	private boolean added;

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
	public IVerticalPanel panel(IClickListener cl) {
		TransparentPopUp closablePopUp = PopUp.showClosablePopUp(true, cl);
		popUp = closablePopUp.popUp().glass(true).autoHide(true);
		if (isModal)
			popUp.modal(true);
		panel = closablePopUp.panel();
		resize();
		popUp.visible(true);
		return panel;
	}

	@Override
	public void visible(boolean visible) {
		if (visible) {
			PopUp.adp.notifyPopUp(new IClickListener() {
				@Override
				public void onClick() {
					popUp.visible(false);
				}
			});
		}
		popUp.visible(visible);
		if (visible && !added) {
			widget.addResizableWidgetToDisplay(popUp);
			added = true;
		}
	}

	@Override
	public void resize() {
		int w = Math.max(popUpSize.minWidth, Shell.instance().dwidth()
				- popUpSize.widthDecrement);
		int h = Math.max(popUpSize.minHeight, Shell.instance().dheight()
				- popUpSize.heightDecrement);
		panel.size(w, h);
		popUp.size(w, h).offset((Shell.instance().dwidth() - w) / 2,
				(Shell.instance().dheight() - h) / 2);
	}

	public void addCloseListener(final Runnable runnable) {
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					runnable.run();
				}
			}
		});
	}

	public boolean visible() {
		return popUp.visible();
	}
}