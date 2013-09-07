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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ISuggestField.ISource;
import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.form.api.IMultiSelectionWidget;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Heights;

class MultiSelectionWidgetImpl implements IMultiSelectionWidget<Object> {

	private final class Suggestion implements ISuggestion {

		private final Object o;

		private Suggestion(Object o) {
			this.o = o;
		}

		@Override
		public String insertText() {
			return adapter.icon(o) + ":" + adapter.label(o);
		}

		@Override
		public String displayText() {
			return adapter.icon(o) + ":" + adapter.label(o);
		}
	}

	private IFlowPanel panel;
	private ISuggestField input;
	private IMultiSelectionAdapter<Object> adapter;

	MultiSelectionWidgetImpl(IContainer container) {
		panel = container.panel().flow().spacing(2);
		Heights.INSTANCE.decorate(panel);
		panel.border().width(1).color().gray();
		input = panel.add().suggestField().width(100);
		input.border().remove();
		input.source(new ISource() {
			@Override
			public void query(String prefix,
					final ICallback<List<ISuggestion>> callback) {
				adapter.query(prefix, new CallbackTemplate<List<Object>>(
						callback) {
					@Override
					public void onSuccess(List<Object> result) {
						List<ISuggestion> sgs = new LinkedList<ISuggestion>();
						for (final Object o : result) {
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
				Object o = ((Suggestion) value).o;
				append(o);
			}
		});
		input.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				Object create = adapter.create(input.text());
				append(create);
			}
		}).enter();
	}

	private void append(Object o) {
		input.text("");
		if (o == null)
			return;
		input.remove();
		final IHorizontalPanel hp = panel.add().panel().horizontal().spacing(4);
		hp.border().style().rounded();
		hp.color().gray(248);
		hp.add().image().resource(adapter.icon(o));
		String label = adapter.label(o);
		hp.add()
				.label()
				.text(label.length() < 32 ? label : label.substring(0, 28)
						+ "...");
		hp.add().image().resource("cancel.png")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						hp.remove();
					}
				});
		panel.add().element(input);
		input.focus(true);
	}

	@Override
	public IMultiSelectionWidget<Object> adapter(
			IMultiSelectionAdapter<Object> adapter) {
		this.adapter = adapter;
		return this;
	}
}
