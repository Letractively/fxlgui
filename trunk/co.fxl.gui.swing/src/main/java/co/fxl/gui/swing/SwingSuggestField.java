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
package co.fxl.gui.swing;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Display;

@SuppressWarnings("unchecked")
class SwingSuggestField extends SwingTextInput<JTextField, ISuggestField>
		implements ISuggestField {

	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();
	private IPopUp popup;
	private IVerticalPanel panel;
	private List<IUpdateListener<ISuggestion>> l = new LinkedList<IUpdateListener<ISuggestion>>();

	SwingSuggestField(SwingContainer<JTextField> container) {
		super(container);
		addFocusListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					hidePopUp();
				}
			}
		});
	}

	private void hidePopUp() {
		if (popup != null) {
			popup.visible(false);
			popup = null;
			panel = null;
		}
	}

	@Override
	public ISuggestField height(int height) {
		return super.height(height + 1);
	}

	@Override
	public int height() {
		return super.height() - 1;
	}

	@Override
	public ISuggestField text(String text) {
		if (text == null)
			text = "";
		setTextOnComponent(text);
		font.updateFont();
		for (IUpdateListener<String> l : updateListeners)
			l.onUpdate(text);
		return this;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			IUpdateListener<String> listener) {
		updateListeners.add(listener);
		return super.addStringUpdateListener(listener);
	}

	void addActionListener(ActionListener actionListener) {
		container.component.addActionListener(actionListener);
	}

	@Override
	public ISuggestField source(final ISource source) {
		addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				source.query(value, new CallbackTemplate<List<ISuggestion>>() {

					@Override
					public void onSuccess(List<ISuggestion> result) {
						if (result.isEmpty()) {
							hidePopUp();
						} else {
							if (popup == null) {
								popup = Display.instance().showPopUp();
								panel = popup.container().panel().vertical()
										.spacing(4);
							}
							panel.clear();
							for (final ISuggestion sg : result) {
								panel.add().label().text(sg.displayText())
										.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												notifyClick(sg);
											}
										});
							}
							popup.offset(offsetX(), offsetY() + height());
							popup.visible(true);
						}
					}
				});
			}
		});
		// TODO SWING-FXL: IMPL: ...
		return this;
	}

	private void notifyClick(ISuggestion sg) {
		for (IUpdateListener<ISuggestion> s : l)
			s.onUpdate(sg);
	}

	@Override
	public ISuggestField addSuggestionListener(
			IUpdateListener<ISuggestion> selection) {
		l.add(selection);
		return this;
	}

	@Override
	public ISuggestField autoSelect(boolean autoSelect) {
		// TODO SWING-FXL: IMPL: ...
		return this;
	}

	@Override
	public ISuggestField requestOnFocus(boolean requestOnFocus) {
		return this;
	}

}
