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
package co.fxl.gui.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IRegistry.IServiceProvider;
import co.fxl.gui.automation.api.IAutomationAdapter;
import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.impl.HTMLText;

public class SwingElementAdapter implements IAutomationAdapter,
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
	public void click(IElement<?> clickable) {
		SwingElement<?, ?> e = (SwingElement<?, ?>) clickable;
		Component el = (Component) clickable.nativeElement();
		if (e instanceof SwingButton) {
			SwingButton b = (SwingButton) e;
			ActionEvent ae = new ActionEvent(el, 0, null);
			b.fireClickListeners(ae);
		} else {
			MouseEvent evt = getMouseEvent(el, Key.LEFT);
			e.fireClickListeners(evt);
		}
	}

	MouseEvent getMouseEvent(Object el, Key key) {
		MouseEvent evt = new MouseEvent((Component) el,
				MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK
						+ getMask(key), 0, 0, getClickCount(key), false);
		if (getMask(key) == MouseEvent.CTRL_MASK)
			assert evt.isControlDown();
		return evt;
	}

	private int getClickCount(Key key) {
		if (key.equals(Key.DOUBLE))
			return 2;
		return 1;
	}

	private int getMask(Key key) {
		switch (key) {
		case CTRL:
			return MouseEvent.CTRL_MASK;
		case SHIFT:
			return MouseEvent.SHIFT_MASK;
		}
		return 0;
	}

	@Override
	public void click(IGridPanel g, int x, int y, Key key) {
		SwingGridPanel gp = (SwingGridPanel) g;
		gp.fireClickListeners(getMouseEvent(g.nativeElement(), key), x, y);
	}

	@Override
	public ILabel findLabel(IPanel<?> panel) {
		PanelComponent p = panel.nativeElement();
		return findLabel(p);
	}

	private ILabel findLabel(PanelComponent p) {
		for (Component c : p.getComponents()) {
			if (c instanceof PanelComponent) {
				ILabel findLabel = findLabel((PanelComponent) c);
				if (findLabel != null)
					return findLabel;
			} else if (isClickableLabel(c)) {
				SwingContainer<JLabel> ct = new SwingContainer<JLabel>(null);
				JLabel lbl = (JLabel) c;
				ct.setComponent(lbl);
				SwingLabel l = new SwingLabel(ct, false);
				l.html.setText(HTMLText.removeHTML(lbl.getText()));
				return l;
			}
		}
		return null;
	}

	private boolean isClickableLabel(Component c) {
		if (!(c instanceof JLabel))
			return false;
		return c.isEnabled() && c.getMouseListeners().length > 0;
	}

}
