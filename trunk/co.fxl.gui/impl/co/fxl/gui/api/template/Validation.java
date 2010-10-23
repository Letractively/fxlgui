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
package co.fxl.gui.api.template;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public class Validation {

	enum State {
		EMPTY, ERROR, OK;
	}

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
	private List<IClickable<?>> clickables = new LinkedList<IClickable<?>>();
	private Map<IElement<?>, State> element2state = new HashMap<IElement<?>, State>();

	private void update() {
		boolean error = false;
		boolean ok = false;
		for (State state : element2state.values()) {
			if (state == State.OK)
				ok = true;
			if (state == State.ERROR)
				error = true;
		}
		for (IClickable<?> c : clickables) {
			c.clickable(ok && !error);
		}
	}

	public Validation linkClickable(IClickable<?> clickable) {
		clickables.add(clickable);
		return this;
	}

	public Validation validateDate(final ITextField textField) {
		element2state.put(textField, State.EMPTY);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				State state = State.EMPTY;
				if (value.length() > 0) {
					try {
						if (DATE_FORMAT.parse(value) != null)
							state = State.OK;
					} catch (Exception e) {
						state = State.ERROR;
					}
				}
				if (state == State.ERROR) {
					textField.color().mix().red().white();
				} else {
					textField.color().white();
				}
				element2state.put(textField, state);
				update();
			}
		});
		return this;
	}

	public Validation linkInput(final ITextField textField) {
		element2state.put(textField, State.EMPTY);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				element2state.put(textField,
						value.trim().length() > 0 ? State.OK : State.EMPTY);
				update();
			}
		});
		return this;
	}

	public Validation linkInput(final IComboBox comboBox,
			final String defaultValue) {
		element2state.put(comboBox, State.EMPTY);
		comboBox.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				element2state.put(comboBox,
						!value.equals(defaultValue) ? State.OK : State.EMPTY);
				update();
			}
		});
		return this;
	}

	public Validation linkInput(final IComboBox comboBox) {
		return linkInput(comboBox, "");
	}
}
