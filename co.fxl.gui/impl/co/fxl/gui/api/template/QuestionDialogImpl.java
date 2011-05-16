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
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

public class QuestionDialogImpl implements IQuestionDialog {

	private IDisplay display;
	private String title = "Question";
	private boolean allowCancel = false;
	private String message;
	private IQuestionDialogListener listener;

	public QuestionDialogImpl(IDisplay display) {
		this.display = display;
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
		final IPopUp popUp = display.showPopUp();
		popUp.center();
		popUp.modal(true);
		IVerticalPanel v = popUp.container().panel().vertical().spacing(1);
		WidgetTitle.decorateBorder(v.border().color());
		WidgetTitle t = new WidgetTitle(v.add().panel()).grayBackground()
				.foldable(false).space(0);
		t.addTitle(title.toUpperCase());
		t.addHyperlink("accept.png", "Yes").addClickListener(
				new IClickListener() {

					@Override
					public void onClick() {
						popUp.visible(false);
						listener.onYes();
					}
				});
		t.addHyperlink("back.png", "No").addClickListener(new IClickListener() {

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
		t.content().panel().vertical().spacing(10).add().label().text(message);
		// .font().weight().bold();
		popUp.visible(true);
		// addShadow(popUp.offsetX() + 40, popUp.offsetY() + popUp.height(), 40,
		// 40);
	}

	//
	// private void addShadow(int x, int y, int w, int h) {
	// IPopUp popUp = display.showPopUp();
	// popUp.modal(true);
	// popUp.offset(x, y);
	// popUp.size(w, h);
	// popUp.container().panel().vertical().color().lightgray();
	// popUp.visible(true);
	// }

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
}
