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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IRichTextArea;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RichTextArea;

public class GWTRichTextArea extends
		GWTTextAreaTemplate<RichTextArea, IRichTextArea> implements
		IRichTextArea {

	private List<IUpdateListener<String>> changeListeners = new LinkedList<IUpdateListener<String>>();

	GWTRichTextArea(final GWTContainer<RichTextArea> container) {
		super(container);
		// final RichTextArea widget = super.container.widget;
		// final Formatter formatter = widget.getFormatter();
		// new RichTextToolbar(this, new IRichTextAdapter() {
		// @Override
		// public void toggleBold() {
		// formatter.toggleBold();
		// container.display().invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// widget.setFocus(true);
		// }
		// });
		// }
		//
		// @Override
		// public boolean isBold() {
		// return formatter.isBold();
		// }
		// });
	};

	@Override
	public IRichTextArea text(String text) {
		String previous = container.widget.getHTML();
		container.widget.setHTML(text);
		if (!previous.equals(text)) {
			for (IUpdateListener<String> ul : changeListeners)
				ul.onUpdate(text);
		}
		return this;
	}

	@Override
	public String text() {
		String text = container.widget.getHTML();
		if (text == null)
			return "";
		return text;
	}

	@Override
	public IRichTextArea addUpdateListener(
			final IUpdateListener<String> changeListener) {
		changeListeners.add(changeListener);
		container.widget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				changeListener.onUpdate(text());
			}
		});
		return this;
	}

	@Override
	public IRichTextArea tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public IRichTextArea editable(boolean editable) {
		container.widget.setEnabled(editable);
		return this;
	}

	@Override
	public IRichTextArea maxLength(int maxLength) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean editable() {
		return container.widget.isEnabled();
	}
}
