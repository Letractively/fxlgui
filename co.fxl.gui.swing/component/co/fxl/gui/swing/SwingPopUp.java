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

import javax.swing.JComponent;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWidgetProvider;

class SwingPopUp implements IPopUp {

	private SwingDisplay panel;
	private Popup dialog;
	private int x = 0;
	private int y = 0;
	private boolean center = false;
	protected JComponent component;

	SwingPopUp(SwingDisplay panel) {
		this.panel = panel;
	}

	@Override
	public IPopUp offset(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public IContainer container() {
		return new SwingContainer<JComponent>(new ComponentParent() {

			@Override
			public void add(JComponent component) {
				SwingPopUp.this.component = component;
			}

			@Override
			public void remove(JComponent component) {
				dialog.hide();
			}

			@Override
			public JComponent getComponent() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return panel.lookupWidgetProvider(interfaceClass);
			}

			@Override
			public SwingDisplay lookupSwingDisplay() {
				return panel;
			}
		});
	}

	@Override
	public IPopUp visible(boolean visible) {
		if (dialog == null)
			if (component != null) {
				if (component.getWidth() < 160)
					component.setSize(160, component.getHeight());
				if (center) {
					x = panel.width() / 2
							- Math.max(80, component.getWidth() / 2);
					y = panel.height() / 2
							- Math.max(40, component.getHeight() / 2);
				}
				PopupFactory factory = PopupFactory.getSharedInstance();
				dialog = factory.getPopup(panel.frame, component, x, y);
			}
		assert dialog != null;
		if (visible) {
			dialog.show();
		} else {
			dialog.hide();
		}
		return this;
	}

	@Override
	public IPopUp modal(boolean modal) {
		// TODO ...
		return this;
	}

	@Override
	public IPopUp center() {
		this.center = true;
		return this;
	}
}
