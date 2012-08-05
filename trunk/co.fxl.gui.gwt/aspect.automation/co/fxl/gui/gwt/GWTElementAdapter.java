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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.IElementAdapter;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

class GWTElementAdapter implements IElementAdapter {

	@Override
	public ILabel findLabel(IPanel<?> panel) {
		Panel p = panel.nativeElement();
		return findLabel(p);
	}

	private ILabel findLabel(Panel p) {
		for (Widget c : p) {
			if (c instanceof Panel) {
				return findLabel((Panel) c);
			} else if (isClickableLabel(c)) {
				GWTContainer<HTML> ct = new GWTContainer<HTML>(null);
				ct.setComponent((HTML) c);
				return new GWTLabel(ct);
			}
		}
		return null;
	}

	private boolean isClickableLabel(Widget c) {
		return c instanceof HTML;
	}

	@Override
	public void click(IElement<?> clickable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void click(IGridPanel g, int x, int y) {
		throw new UnsupportedOperationException();
	}

}
