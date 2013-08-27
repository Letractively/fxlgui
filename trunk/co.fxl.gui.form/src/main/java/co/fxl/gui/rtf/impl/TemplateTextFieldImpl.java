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
package co.fxl.gui.rtf.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.TextFieldAdp;
import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.IHTMLArea.Formatting;
import co.fxl.gui.rtf.api.ITemplateTextField;
import co.fxl.gui.rtf.api.ITokenButton;

public class TemplateTextFieldImpl extends TextFieldAdp implements
		ITemplateTextField, RichTextToolbarTextAdp {

	private IVerticalPanel panel;
	private RichTextToolbarImpl toolbar;
	private boolean ignore;

	public TemplateTextFieldImpl(IContainer element) {
		panel = element.panel().vertical();
		toolbar = new RichTextToolbarImpl(panel.add(), this);
		element(panel.add().textField());
		element().addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (!ignore)
					toolbar.updateStatus();
			}
		});
		toolbar.updateStatus();
	}

	@Override
	public ITextField editable(boolean editable) {
		toolbar.editable(editable);
		return (ITextField) super.editable(editable);
	}

	@Override
	public void toggle(Formatting f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean is(Formatting f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean is(String f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean supports(Formatting f) {
		return false;
	}

	@Override
	public void apply(Formatting f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void notifyChange() {
	}

	@Override
	public IHTMLArea insertHTML(String html) {
		int index = cursorPosition() + html.length();
		StringBuilder text = new StringBuilder(text());
		text.insert(cursorPosition(), html);
		text(text.toString());
		focus(true);
		cursorPosition(index >= text().length() ? text().length() - 1 : index);
		return null;
	}

	@Override
	public IHTMLArea insertImage(String string) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IHTMLArea insertHyperlink(String link) {
		throw new UnsupportedOperationException();
	}

	public ITokenButton addTokenButton() {
		return toolbar.addTokenButton();
	}

}
