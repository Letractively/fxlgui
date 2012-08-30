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
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.TextAreaAdp;
import co.fxl.gui.rtf.api.IHTMLArea;

public class HTMLAreaImpl extends TextAreaAdp implements IHTMLArea {

	private IVerticalPanel panel;
	private RichTextToolbarImpl toolbar;

	public HTMLAreaImpl(IContainer element) {
		panel = element.panel().vertical();
		toolbar = new RichTextToolbarImpl(panel.add(), this);
		super.element = panel.add().textArea();
		super.element.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				toolbar.updateStatus();
			}
		});
		super.element.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				toolbar.updateStatus();
			}
		});
		toolbar.updateStatus();
	}

	@Override
	public String html() {
		return element.text();
	}

	@Override
	public IHTMLArea html(String html) {
		element.text(html);
		toolbar.updateStatus();
		return this;
	}

	@Override
	public IHTMLArea height(int height) {
		panel.height(height);
		super.height(height - toolbar.height());
		return this;
	}

	@Override
	public void toggle(Formatting f) {
		String insert = is(f) ? close(f) : open(f);
		insert(insert);
		toolbar.updateStatus();
	}

	protected void insert(String insert) {
		int cursorPosition = element.cursorPosition();
		StringBuilder text = new StringBuilder(element.text());
		text.insert(cursorPosition, insert);
		element.text(text.toString());
		element.cursorPosition(cursorPosition + insert.length());
	}

	@Override
	public boolean is(Formatting f) {
		String open = open(f);
		String close = close(f);
		int count = 0;
		for (int i = 0; i < element.cursorPosition(); i++) {
			if (containsTokenAt(open, i)) {
				count++;
			} else if (containsTokenAt(close, i)) {
				count--;
			}
		}
		return count > 0;
	}

	private boolean containsTokenAt(String open, int i) {
		String text = element.text();
		for (int j = 0; j < open.length() && i + j < text.length(); j++) {
			if (text.charAt(i + j) != open.charAt(j))
				return false;
		}
		return true;
	}

	private String close(Formatting f) {
		return "</" + token(f) + ">";
	}

	private String token(Formatting f) {
		switch (f) {
		case TOGGLE_BOLD:
			return "b";
		case TOGGLE_ITALIC:
			return "i";
		default:
			throw new UnsupportedOperationException(f.name());
		}
	}

	private String open(Formatting f) {
		return "<" + token(f) + ">";
	}

	@Override
	public boolean supports(Formatting f) {
		switch (f) {
		case TOGGLE_BOLD:
		case TOGGLE_ITALIC:
		case INSERT_HORIZONTAL_RULE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void apply(Formatting f) {
		switch (f) {
		case INSERT_HORIZONTAL_RULE:
			insert("<hr/>");
			return;
		default:
			throw new UnsupportedOperationException(f.name());
		}
	}

	@Override
	public void notifyChange() {
	}

	@Override
	public IHTMLArea closeListener(IClickListener l) {
		toolbar.closeListener(l);
		return this;
	}

}
