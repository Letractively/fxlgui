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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog.IQuestionDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

class QuestionDialogImpl implements IQuestionDialog {

	private IDisplay display;
	private String title = "Question";
	private boolean allowCancel = false;
	private String message;
	private IQuestionDialogListener listener;
	private boolean hasNo = true;
	IPopUp popUp;
	private boolean modal = true;
	private String imageResource;

	QuestionDialogImpl(IDisplay display, boolean hasNo) {
		this.display = display;
		this.hasNo = hasNo;
	}

	@Override
	public IQuestionDialog title(String title) {
		this.title = title;
		update();
		return this;
	}

	private void update() {
		if (message == null || listener == null)
			return;
		popUp = display.showPopUp();
		popUp.center();
		popUp.modal(modal);
		IVerticalPanel v = popUp.container().panel().vertical().spacing(1);
		WidgetTitle.decorateBorder(v.border().color());
		WidgetTitle t = new WidgetTitle(v.add().panel()).grayBackground()
				.foldable(false).space(0);
		t.addTitle(title.toUpperCase());
		t.addHyperlink("accept.png", hasNo ? "Yes" : "Ok").addClickListener(
				new IClickListener() {

					@Override
					public void onClick() {
						popUp.visible(false);
						listener.onYes();
					}
				});
		if (hasNo)
			t.addHyperlink("back.png", "No").addClickListener(
					new IClickListener() {

						@Override
						public void onClick() {
							popUp.visible(false);
							listener.onNo();
						}
					});
		if (allowCancel)
			t.addHyperlink("cancel.png", "Cancel").addClickListener(
					new IClickListener() {

						@Override
						public void onClick() {
							popUp.visible(false);
							listener.onCancel();
						}
					});
		IHorizontalPanel v0 = t.content().panel().vertical().spacing(10).add()
				.panel().horizontal().align().begin().add().panel()
				.horizontal().align().begin();
		if (imageResource != null) {
			v0.add().image().resource(imageResource);
			v0.addSpace(4);
		}
		v0.add().label().text(message);
		popUp.visible(true);
	}

	@Override
	public IQuestionDialog allowCancel() {
		this.allowCancel = true;
		update();
		return this;
	}

	@Override
	public IQuestionDialog question(String message) {
		this.message = message;
		update();
		return this;
	}

	@Override
	public IQuestionDialog addQuestionListener(IQuestionDialogListener listener) {
		this.listener = listener;
		update();
		return this;
	}

	@Override
	public IQuestionDialog modal(boolean modal) {
		this.modal = modal;
		return this;
	}

	@Override
	public IQuestionDialog imageResource(String imageResource) {
		this.imageResource = imageResource;
		return this;
	}
}
