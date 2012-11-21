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

import java.awt.Insets;

import javax.swing.JComponent;

import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IKeyRecipient;

class SwingFocusPanel extends SwingPanel<IFocusPanel> implements IFocusPanel {

	private VerticalLayoutManager layoutManager;

	SwingFocusPanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(layoutManager = new VerticalLayoutManager());
		container.component.setOpaque(false);
		container.component.setFocusable(true);
	}

	@Override
	void gap(int pixel) {
		layoutManager.vgap = pixel;
	}

	@Override
	Insets insets() {
		return layoutManager.insets;
	}

	@Override
	public void add(JComponent component) {
		super.add(component);
	}

	@Override
	public IKeyRecipient.IKey<IFocusPanel> addKeyListener(
			IClickListener clickListener) {
		return new KeyTemplate<IFocusPanel>(this, clickListener);
	}

	@Override
	public IFocusPanel outline(boolean outline) {

		// TODO ...

		return this;
	}

}
