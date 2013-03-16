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
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.StylishButton;

class SaveButtonPanel implements IClickable<Object> {

	private final class ClickListener implements IClickListener {

		private final FormWidgetImpl widget;
		private final CancelButtonPanel cancelButtonElement;
		private final IHorizontalPanel subPanel;
		private boolean isAndBack;
		private StylishButton sButton;

		private ClickListener(FormWidgetImpl widget,
				CancelButtonPanel cancelButtonElement,
				IHorizontalPanel subPanel, boolean isAndBack,
				StylishButton sButton) {
			this.widget = widget;
			this.cancelButtonElement = cancelButtonElement;
			this.subPanel = subPanel;
			this.isAndBack = isAndBack;
			this.sButton = sButton;
		}

		@Override
		public void onClick() {
			IHorizontalPanel iElement = sButton.panel();
			int h = (iElement.height() - 11) / 2;
			int v = (iElement.width() - 16) / 2;
			iElement.clear();
			iElement.add().image().resource("saving.gif").margin().left(v)
					.right(v + (iElement.width() % 2)).top(h).bottom(h);
			widget.saveListener.save(isAndBack,
					new CallbackTemplate<Boolean>() {

						@Override
						public void onSuccess(Boolean result) {
							resetButton();
							if (!result)
								return;
							widget.validation.update();
							sButton.clickable(false);
							if (!widget.alwaysAllowCancel)
								cancelButtonElement.clickable(false);
						}

						private void resetButton() {
							widget.addSaveButton(subPanel.clear(),
									cancelButtonElement);
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
	private StylishButton saveAndBackButton;
	private ClickListener saveAndBackClickListener;

	SaveButtonPanel(final FormWidgetImpl widget,
			final IHorizontalPanel subPanel,
			CancelButtonPanel cancelButtonElement) {
		if (widget.saveListener.allowsSaveAndBack()) {
			saveAndBackButton = new StylishButton(subPanel.add().panel()
					.horizontal(), "Save & Back", true, 3, false);
			saveAndBackClickListener = new ClickListener(widget,
					cancelButtonElement, subPanel, true, saveAndBackButton);
			saveAndBackButton.addClickListener(saveAndBackClickListener);
			subPanel.addSpace(4);
		}
		saveButton = new StylishButton(subPanel.add().panel().horizontal(),
				widget.saveTitle, true, 3, false);
		saveClickListener = new ClickListener(widget, cancelButtonElement,
				subPanel, false, saveButton);
		saveButton.addClickListener(saveClickListener);
	}

	@Override
	public Object clickable(boolean clickable) {
		saveButton.clickable(clickable);
		if (saveAndBackButton != null)
			saveAndBackButton.clickable(clickable);
		return this;
	}

	@Override
	public boolean clickable() {
		return saveButton.clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		throw new UnsupportedOperationException();
	}

	void onCR() {
		if (saveAndBackClickListener != null && saveAndBackButton.clickable())
			saveAndBackClickListener.onClick();
		else if (saveClickListener != null && saveButton.clickable())
			saveClickListener.onClick();
	}
}
