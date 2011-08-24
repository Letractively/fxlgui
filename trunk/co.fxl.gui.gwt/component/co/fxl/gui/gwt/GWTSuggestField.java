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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ISuggestField;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

class GWTSuggestField extends GWTElement<SuggestBox, ISuggestField> implements
		ISuggestField {

	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();
	private MultiWordSuggestOracle oracle;

	GWTSuggestField(GWTContainer<SuggestBox> container) {
		super(container);
		assert container != null : "GWTTextField.new: container is null";
		container.widget.addStyleName("gwt-TextBox-FXL");
		defaultFont();
		oracle = (MultiWordSuggestOracle) ((SuggestBox) container.widget)
				.getSuggestOracle();
	}

	public ISuggestField text(String text) {
		if (text == null)
			text = "";
		container.widget.setText(text);
		for (IUpdateListener<String> l : updateListeners)
			l.onUpdate(text);
		return this;
	}

	@Override
	public ISuggestField addUpdateListener(
			final IUpdateListener<String> changeListener) {
		updateListeners.add(changeListener);
		container.widget.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				changeListener.onUpdate(container.widget.getText());
			}
		});
		return this;
	}

	@Override
	public String text() {
		String text = container.widget.getText();
		if (text == null)
			return "";
		return text;
	}

	@Override
	public int width() {
		return super.width() + 8;
	}

	@Override
	public ISuggestField width(int width) {
		return (ISuggestField) super.width(width - 8);
	}

	@Override
	public ISuggestField addText(String... texts) {
		for (String t : texts)
			oracle.add(t);
		return this;
	}

	@Override
	public ISuggestField editable(boolean editable) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ISuggestField maxLength(int maxLength) {
		throw new MethodNotImplementedException();
	}
}
