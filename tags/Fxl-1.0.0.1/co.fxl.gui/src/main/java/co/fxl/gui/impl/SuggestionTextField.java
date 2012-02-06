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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.ITextInput;
import co.fxl.gui.api.IVerticalPanel;

public class SuggestionTextField implements
		co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> {

	private List<String> texts = new LinkedList<String>();
	private ITextField textField;
	private ElementPopUp popUp;

	public SuggestionTextField(IContainer container) {
		textField = container.textField().visible(false);
		ElementPopUp.HEIGHTS.decorate(textField);
		popUp = new ElementPopUp(textField);
	}

	@Override
	public void onUpdate(Boolean value) {
		if (value) {
			clearPopUp();
			createPopUp();
			popUp.visible(true);
		}
	}

	public SuggestionTextField addText(String... texts) {
		this.texts.addAll(Arrays.asList(texts));
		popUp.lines(this.texts.size());
		popUp.scrollPane(this.texts.size() > 1);
		return this;
	}

	private void createPopUp() {
		IVerticalPanel v = popUp.create();
		for (final String text : texts) {
			ILabel cb = v.add().panel().horizontal().align().begin().add()
					.panel().horizontal().align().begin().add().label()
					.text(text);
			cb.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					textField.text(text);
					clearPopUp();
				}
			});
		}
	}

	private void clearPopUp() {
		popUp.clear();
	}

	public SuggestionTextField width(int width) {
		textField.width(width);
		return this;
	}

	public SuggestionTextField clear() {
		clearPopUp();
		texts.clear();
		return this;
	}

	public SuggestionTextField visible(boolean visible) {
		textField.visible(visible);
		textField.addFocusListener(this);
		return this;
	}

	public ITextInput<?> textField() {
		return textField;
	}
}
