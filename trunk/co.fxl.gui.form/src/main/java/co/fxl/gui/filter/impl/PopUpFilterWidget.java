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
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.api.IPopUpFilterWidget;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.PopUp.TransparentPopUp;
import co.fxl.gui.impl.SplitLayout;

public class PopUpFilterWidget extends FilterWidgetImpl implements
		IPopUpFilterWidget {

	private IHorizontalPanel p;
	private IElement<?> element;
	private ImageButton button;
	private ImageButton clearButton;
	private IPopUp popUp;

	PopUpFilterWidget(IContainer panel) {
		p = panel.panel().horizontal();
		p.padding().top(3);
		element = setUp(p.add());
		element.remove();
		button = new ImageButton(p.add());
		button.imageResource("filter.png");// .text("Filter");
		button.hyperlink();
		button.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				final TransparentPopUp p = PopUp.showClosablePopUp(true, null,
						true);
				IPanel<?> labelPanel = button.panel();
				int y = labelPanel.offsetY() + labelPanel.height();
				p.popUp.offset(labelPanel.offsetX() - 4, y);
				p.popUp.width(SplitLayout.WIDTH_SIDE_PANEL
						+ SplitLayout.SCROLLBAR_WIDTH + 20);
				p.popUp.modal(false).autoHide(true).visible(true);
				p.panel.add().element(element);
				p.panel.padding().left(10).right(10).bottom(10);
				if (y + p.panel.height() > Display.instance().height() - 30) {
					p.panel.clear().add().scrollPane()
							.height(Display.instance().height() - 30 - y)
							.viewPort().element(element);
				}
				PopUp.autoHideOnResize(p);
				popUp = p.popUp;
				popUp.addVisibleListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						if (!value)
							popUp = null;
					}
				});
			}
		});
		clearButton = new ImageButton(p.addSpace(8).add());
		clearButton.imageResource("clear_filter.png");// .text("Clear");
		clearButton.hyperlink();
		clearButton.addClickListener(new LazyClickListener() {
			@Override
			protected void onAllowedClick() {
				clearClick();
			}
		});
	}

	@Override
	boolean alwaysUseDirectApply() {
		return false;
	}

	@Override
	void notifyListeners(ICallback<Void> cb) {
		hidePopUp();
		super.notifyListeners(cb);
	}

	void hidePopUp() {
		if (popUp != null) {
			popUp.visible(false);
			popUp = null;
		}
	}

	@Override
	void clearClickable(boolean constrained) {
		clearButton.clickable(constrained);
		super.clearClickable(constrained);
	}

}
