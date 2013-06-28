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
	private boolean ignore;

	public HTMLAreaImpl(IContainer element) {
		panel = element.panel().vertical();
		toolbar = new RichTextToolbarImpl(panel.add(), this);
		element(panel.add().textArea());
		element().addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (!ignore)
					toolbar.updateStatus();
			}
		});
		element().addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				toolbar.updateStatus();
			}
		});
		toolbar.updateStatus();
	}

	@Override
	public String html() {
		return element().text();
	}

	@Override
	public IHTMLArea editable(boolean editable) {
		toolbar.editable(editable);
		return (IHTMLArea) super.editable(editable);
	}

	@Override
	public IHTMLArea html(String html) {
		element().text(html);
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
		ignore = true;
		int cursorPosition = element().cursorPosition();
		StringBuilder text = new StringBuilder(element().text());
		text.insert(cursorPosition, insert);
		element().text(text.toString());
		element().cursorPosition(cursorPosition + insert.length());
		ignore = false;
	}

	@Override
	public boolean is(Formatting f) {
		String open = open(f);
		String close = close(f);
		int count = 0;
		for (int i = 0; i < element().cursorPosition(); i++) {
			if (containsTokenAt(open, i)) {
				count++;
			} else if (containsTokenAt(close, i)) {
				count--;
			}
		}
		return count > 0;
	}

	@Override
	public boolean is(String f) {
		return parse(new String[] { f })[0];
	}

	@Override
	public boolean[] parse(String[] css) {
		return parse(this, css);
	}

	private static boolean[] parse(IHTMLArea html, String[] css) {
		String body = html.html();
		int htmlCursorPosition = html.htmlCursorPosition();
		return RichTextToolbarImpl.parse(css, body, htmlCursorPosition, "");
	}

	// public static void main(String[] args) {
	// String string =
	// "<span class='section'><span class='section-title'>TITLE</span>BODY</span>";
	// int count = 0;
	// for (int i = 0; i < string.length(); i++) {
	// if (string.charAt(i) == '<')
	// count++;
	// else if (string.charAt(i) == '>')
	// count--;
	// else if (count == 0) {
	// System.out.println(string.substring(i) + ": ");
	// System.out.println(Arrays.toString(parse(new String[] {
	// "section", "section-title" }, string, i)));
	// }
	// }
	// }

	private boolean containsTokenAt(String open, int i) {
		String text = element().text();
		return RichTextToolbarImpl.containsTokenAt(open, i, text);
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

	@Override
	public int htmlCursorPosition() {
		return cursorPosition();
	}

	@Override
	public IHTMLArea insertHTML(String html) {
		insert(html);
		return this;
	}

	@Override
	public IHTMLArea insertImage(String string) {
		insert("<img src='" + string + "'/>");
		return this;
	}

	@Override
	public IHTMLArea insertHyperlink(String link) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IHTMLAreaButton addButton() {
		return toolbar.addButton();
	}

}
