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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.LazyClickListener;

class CancelButtonPanel implements IClickable<Object> {

	private ILabel cb;
	private ILabel cbAndBack;
	private FormWidgetImpl widget;

	CancelButtonPanel(FormWidgetImpl widget, IHorizontalPanel panel) {
		this.widget = widget;
		if (widget.saveListener.allowsSaveAndBack() || widget.isNew)
			cbAndBack = addButton(panel, "Cancel & Back", widget.saveListener.allowsSaveAndBack());
		if (!widget.isNew)
			cb = addButton(panel, "Cancel", false);
	}

	private ILabel addButton(IHorizontalPanel panel, String text,
			final boolean andBack) {
		ILabel cb = panel.addSpace(8).add().label().text(text).hyperlink();
		cb.font().pixel(11);
		cb.addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				widget.saveListener.cancel(andBack,
						new CallbackTemplate<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								widget.validation.reset();
								widget.saveButton.clickable(false);
								if (!widget.alwaysAllowCancel)
									clickable(false);
							}
						});
			}
		});
		return cb;
	}

	void visible(boolean visible) {
		if (cb != null)
			cb.visible(visible);
		if (cbAndBack != null)
			cbAndBack.visible(visible);
	}

	@Override
	public Object clickable(boolean clickable) {
		if (cb != null)
			cb.clickable(clickable);
		// if (cbAndBack != null)
		// cbAndBack.clickable(clickable);
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IKey addClickListener(IClickListener clickListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean clickable() {
		return cb != null ? cb.clickable() : cbAndBack.clickable();
	}

}
