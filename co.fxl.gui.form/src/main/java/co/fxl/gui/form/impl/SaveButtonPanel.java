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
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.StylishButton;

class SaveButtonPanel implements IClickable<Object> {

	private final class ClickListener implements IClickListener {

		private final FormWidgetImpl widget;
		private final IElement<?> cancelButtonElement;
		private final IClickable<?> cancelButton;
		private final IHorizontalPanel subPanel;
		private boolean isAndBack;

		private ClickListener(FormWidgetImpl widget,
				IElement<?> cancelButtonElement, IClickable<?> cancelButton,
				IHorizontalPanel subPanel, boolean isAndBack) {
			this.widget = widget;
			this.cancelButtonElement = cancelButtonElement;
			this.cancelButton = cancelButton;
			this.subPanel = subPanel;
			this.isAndBack = isAndBack;
		}

		@Override
		public void onClick() {
			IElement<?> iElement = (IElement<?>) saveButton.panel();
			int h = (iElement.height() - 11) / 2;
			int v = (iElement.width() - 16) / 2;
			subPanel.add().image().resource("saving.gif").margin().left(v)
					.right(v + (iElement.width() % 2)).top(h).bottom(h);
			iElement.remove();
			widget.saveListener.save(isAndBack,
					new CallbackTemplate<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							resetButton();
							if (!result)
								return;
							widget.validation.update();
							saveButton.clickable(false);
							if (!widget.alwaysAllowCancel)
								cancelButton.clickable(false);
						}

						private void resetButton() {
							widget.addSaveButton(subPanel.clear(),
									cancelButton, cancelButtonElement);
						}

						@Override
						public void onFail(Throwable throwable) {
							resetButton();
							super.onFail(throwable);
						}
					});
		}
	}

	private IClickListener saveClickListener;
	private StylishButton saveButton;

	SaveButtonPanel(final FormWidgetImpl widget,
			final IHorizontalPanel subPanel, final IClickable<?> cancelButton,
			final IElement<?> cancelButtonElement) {
		if (widget.saveListener.allowsSaveAndBack()) {
			// TODO ...
		}
		saveButton = new StylishButton(subPanel.add().panel().horizontal(),
				widget.saveTitle, true, 3, false);
		saveClickListener = new ClickListener(widget, cancelButtonElement,
				cancelButton, subPanel, false);
		saveButton.addClickListener(saveClickListener);
	}

	@Override
	public Object clickable(boolean clickable) {
		saveButton.clickable(clickable);
		return this;
	}

	@Override
	public boolean clickable() {
		return saveButton.clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		return saveButton.addClickListener(clickListener);
	}

	void onCR() {
		if (saveClickListener != null && saveButton.clickable())
			saveClickListener.onClick();
	}
}
