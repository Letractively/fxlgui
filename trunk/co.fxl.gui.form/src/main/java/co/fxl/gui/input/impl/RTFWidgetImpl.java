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
package co.fxl.gui.input.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.input.api.IRTFWidget;

public class RTFWidgetImpl implements IRTFWidget {

	private IVerticalPanel panel;
	private ITextArea textArea;
	private IHorizontalPanel buttonPanel;
	protected List<Object> tokens = new LinkedList<Object>();

	protected RTFWidgetImpl(IContainer container) {
		panel = container.panel().vertical();
		textArea = panel.add().textArea();
		buttonPanel = panel.addSpace(6).add().panel().horizontal().spacing(4);
	}

	@Override
	public IRTFWidget addToken(String label, String token) {
		tokens.add(new String[] { label, token });
		return this;
	}

	@Override
	public IComposite addComposite() {
		IComposite c = new CompositeImpl();
		tokens.add(c);
		return c;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Object o : tokens) {
			if (b.length() > 0)
				b.append("\n");
			b.append(o instanceof String[] ? Arrays.toString((String[]) o) : o
					.toString());
		}
		return b.toString();
	}

	@Override
	public String html() {
		return textArea.text();
	}

	@Override
	public IRTFWidget html(String html) {
		textArea.text(html);
		return this;
	}

	@Override
	public IRTFWidget visible(boolean visible) {
		for (Object o : tokens) {
			if (o instanceof String[]) {
				String[] t = (String[]) o;
				buttonPanel.add().button().text(t[0])
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								throw new MethodNotImplementedException();
							}
						});
				// TODO ...
			} else {
				CompositeImpl c = (CompositeImpl) o;
				// TODO ...
			}
		}
		return this;
	}

}
