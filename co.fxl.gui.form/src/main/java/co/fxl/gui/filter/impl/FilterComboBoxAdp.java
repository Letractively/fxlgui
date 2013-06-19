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
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.impl.ComboBoxAdp;

class FilterComboBoxAdp extends ComboBoxAdp {

	private String nullValue;

	FilterComboBoxAdp(IComboBox c, String nullValue) {
		super(c);
		this.nullValue = nullValue;
		c.addText(nullValue);
	}

	@Override
	public IComboBox addText(String... strings) {
		for (String s : strings) {
			if (nullValue == null || !isNull(s))
				super.addText(s);
		}
		return this;
	}

	private boolean isNull(String s) {
		return s == null || s.equals("");
	}

	@Override
	public IComboBox addUpdateListener(final IUpdateListener<String> l) {
		if (nullValue == null)
			super.addUpdateListener(l);
		else
			super.addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					l.onUpdate(text());
				}
			});
		return this;
	}

	@Override
	public IComboBox addNull() {
		if (nullValue == null)
			super.addNull();
		return this;
	}

	@Override
	public IComboBox text(String text) {
		if (isNull(text))
			super.text(nullValue);
		else
			super.text(text);
		return this;
	}

	@Override
	public String text() {
		if (nullValue != null && super.text().equals(nullValue)) {
			return "";
		}
		return super.text();
	}

}
