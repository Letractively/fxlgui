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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ISuggestField.ISource;
import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.api.IMultiSelectionWidget;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Display;

public class MultiSelectionWidgetImpl implements IMultiSelectionWidget {

	private class Entry implements IClickListener {

		private IHorizontalPanel hp;
		private Object o;

		public Entry(IHorizontalPanel hp, Object o) {
			this.hp = hp;
			this.o = o;
		}

		@Override
		public void onClick() {
			tokens.remove(this);
			hp.remove();
			if (tokens.isEmpty())
				panel.height(25);
			notifyTextArea();
			// input.focus(true);
		}

	}

	private final class Suggestion implements ISuggestion {

		private final String o;

		private Suggestion(String o) {
			this.o = o;
		}

		@Override
		public String insertText() {
			return o;
		}

		@Override
		public String displayText() {
			return "<table cellspacing='0'><tbody><tr><td><img src='"
					+ image(o) + "'/>" + "</td><td>" + adapter.label(o)
					+ "</td></tr></tbody></table>";
		}
	}

	private IFlowPanel panel;
	private ISuggestField input;
	private IMultiSelectionAdapter adapter;
	private List<Entry> tokens = new LinkedList<Entry>();
	private ITextArea textArea;
	private boolean ignore;

	protected MultiSelectionWidgetImpl(IContainer container) {
		panel = container.panel().flow().spacing(2).height(25);
		panel.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				input.focus(true);
			}
		});
		textArea = panel.add().textArea().visible(false);
		panel.color().gray(253);
		IBorder border = panel.border().width(1);
		border.color().rgb(211, 211, 211);
		border.style().rounded().width(3);
		border.width(1);
		input = panel.add().suggestField().width(100).autoSelect(true)
		// .requestOnFocus(true)
				.outline(false);
		input.color().gray(253);
		textArea.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (!ignore && !value.trim().equals(""))
					for (String v : value.trim().split(SEPARATOR)) {
						append(v.trim(), false);
					}
			}
		});
		input.padding().bottom(0);
		input.margin().bottom(4);
		input.border().remove();
		input.source(new ISource() {
			@Override
			public void query(String prefix,
					final ICallback<List<ISuggestion>> callback) {
				adapter.query(prefix, new CallbackTemplate<List<String>>(
						callback) {
					@Override
					public void onSuccess(List<String> result) {
						List<ISuggestion> sgs = new LinkedList<ISuggestion>();
						for (final String o : result) {
							sgs.add(new Suggestion(o));
						}
						callback.onSuccess(sgs);
					}
				});
			}
		});
		input.addSuggestionListener(new IUpdateListener<ISuggestField.ISource.ISuggestion>() {
			@Override
			public void onUpdate(ISuggestion value) {
				String o = value.insertText();
				append(o, true);
			}
		});
		addKeyListeners(input, new IClickListener() {
			@Override
			public void onClick() {
				String create = adapter.create(input.text());
				append(create, true);
			}
		}, new IClickListener() {
			@Override
			public void onClick() {
				if (!tokens.isEmpty() && input.text().length() == 0)
					tokens.get(tokens.size() - 1).onClick();
			}
		});
	}

	protected void addKeyListeners(ISuggestField input, IClickListener cr,
			IClickListener backspace) {
	}

	protected String getImageURL(String icon) {
		return icon;
	}

	private void append(final String o, boolean b) {
		input.text("");
		if (o == null)
			return;
		String label = adapter.label(o);
		if (label.equals(""))
			return;
		input.remove();
		final IHorizontalPanel hp = panel.height(-1).add().panel().horizontal()
				.spacing(2);
		final Entry e = new Entry(hp, o);
		tokens.add(e);
		IBorder border = hp.border();
		border.style().rounded();
		border.width(1).color().lightgray();
		hp.color().rgb(230, 230, 230);
		hp.add().image().resource(adapter.icon(o));
		String text = label.length() < 32 ? label : label.substring(0, 28)
				+ "...";
		hp.add().label().text(text).font().weight().bold().pixel(10);
		hp.add().image().resource("cancel_gray_small.png").addClickListener(e);
		panel.add().element(input);
		if (b)
			update();
	}

	private void update() {
		notifyTextArea();
		Display.instance().invokeLater(new Runnable() {
			@Override
			public void run() {
				input.focus(true);
			}
		});
	}

	private String image(final String o) {
		return getImageURL(adapter.icon(o));
	}

	private void notifyTextArea() {
		StringBuilder b = new StringBuilder();
		for (Entry token : tokens) {
			if (b.length() > 0)
				b.append(SEPARATOR);
			b.append(token.o);
		}
		ignore = true;
		textArea.text(b.toString());
		ignore = false;
	}

	@Override
	public IMultiSelectionWidget adapter(IMultiSelectionAdapter adapter) {
		this.adapter = adapter;
		return this;
	}

	@Override
	public ITextArea invisibleTextArea() {
		return textArea;
	}

	@Override
	public ISuggestField suggestField() {
		return input;
	}
}
