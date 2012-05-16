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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;
import co.fxl.gui.impl.CallbackTemplate;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

class GWTSuggestField extends GWTElement<SuggestBox, ISuggestField> implements
		ISuggestField {

	static class OracleAdapter extends SuggestOracle {

		GWTSuggestField element;

		@Override
		public void requestSuggestions(final Request arg0, final Callback arg1) {
			if (!arg0.getQuery().equals(""))
				element.source.query(arg0.getQuery(),
						new CallbackTemplate<List<ISuggestion>>() {

							@Override
							public void onSuccess(List<ISuggestion> result) {
								Collection<Suggestion> cs = new LinkedList<Suggestion>();
								for (final ISuggestion s : result) {
									cs.add(new Suggestion() {

										@Override
										public String getDisplayString() {
											return s.displayText();
										}

										@Override
										public String getReplacementString() {
											return s.insertText();
										}

									});
								}
								Response r = new Response(cs);
								arg1.onSuggestionsReady(arg0, r);
							}
						});
		}
	}

	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();
	private ISource source;

	GWTSuggestField(final GWTContainer<SuggestBox> container) {
		super(container);
		// assert container != null : "GWTTextField.new: container is null";
		DefaultSuggestionDisplay sd = (DefaultSuggestionDisplay) container.widget
				.getSuggestionDisplay();
		sd.setPopupStyleName("gwt-SuggestBoxPopup-FXL");
		container.widget.getTextBox().setStyleName("gwt-TextBox");
		// defaultFont();
		// oracle = (MultiWordSuggestOracle) ((SuggestBox) container.widget)
		// .getSuggestOracle();
	}

	@Override
	public ISuggestField autoSelect(boolean autoSelect) {
		assert autoSelect;
		container.widget.getTextBox().addFocusListener(new FocusListener() {

			@Override
			public void onFocus(Widget arg0) {
				display().invokeLater(new Runnable() {
					@Override
					public void run() {
						container.widget.getTextBox().selectAll();
					}
				});
			}

			@Override
			public void onLostFocus(Widget arg0) {
			}
		});
		return this;
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
		container.widget.getTextBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
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
	public ISuggestField editable(boolean editable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ISuggestField maxLength(int maxLength) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean editable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ISuggestField source(ISource source) {
		this.source = source;
		return this;
	}

	@Override
	public ISuggestField addSuggestionListener(
			final co.fxl.gui.api.IUpdateable.IUpdateListener<ISuggestion> selection) {
		container.widget
				.addSelectionHandler(new SelectionHandler<Suggestion>() {

					@Override
					public void onSelection(
							final SelectionEvent<Suggestion> arg0) {
						selection.onUpdate(new ISuggestion() {

							@Override
							public String insertText() {
								return arg0.getSelectedItem()
										.getReplacementString();
							}

							@Override
							public String displayText() {
								return arg0.getSelectedItem()
										.getDisplayString();
							}
						});
					}
				});
		return this;
	}
}
