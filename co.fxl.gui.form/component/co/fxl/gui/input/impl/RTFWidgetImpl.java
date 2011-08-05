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

import co.fxl.gui.input.api.IRTFWidget;

public abstract class RTFWidgetImpl implements IRTFWidget {

	protected List<Object> tokens = new LinkedList<Object>();

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

}
