/**
 * This file is part of  GUI API.
 *  
 *  GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 *  GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with  GUI API.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.gwt;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IRegistry.IServiceProvider;
import co.fxl.gui.automation.api.IAutomationAdapter;
import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.gwt.GWTClickHandler.ClickEventAdp;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

class GWTElementAdapter implements IAutomationAdapter,
		IServiceProvider<IAutomationAdapter> {

	@Override
	public Class<IAutomationAdapter> serviceType() {
		return IAutomationAdapter.class;
	}

	@Override
	public IAutomationAdapter getService() {
		return this;
	}

	@Override
	public ILabel findLabel(IPanel<?> panel) {
		Panel p = panel.nativeElement();
		return findLabel(p);
	}

	private ILabel findLabel(Panel p) {
		for (Widget c : p) {
			if (c instanceof Panel) {
				ILabel findLabel = findLabel((Panel) c);
				if (findLabel != null)
					return findLabel;
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
		GWTElement<?, ?> e = (GWTElement<?, ?>) clickable;
		ClickEventAdp click = new ClickEventAdp(null);
		e.fireClickListenersSingleClick(click);
	}

	@Override
	public void click(IGridPanel g, int x, int y, Key key) {
		GWTGridPanel gg = (GWTGridPanel) g;
		throw new UnsupportedOperationException();
		// NativeAutomationEvent automationEvent = new
		// NativeAutomationEvent(key,
		// x, y);
		// ClickEvent c = new ClickAutomationEvent(automationEvent);
		// gg.container.widget.fireEvent(c);
	}

}
