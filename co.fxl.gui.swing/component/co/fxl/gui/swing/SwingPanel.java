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

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IWidgetProvider;

class SwingPanel<R> extends SwingElement<JPanel, R> implements IPanel<R>,
		ComponentParent {

	private final class SwingPanelColor extends SwingColor {

		@Override
		protected void setColor(Color color) {
			container.component.setOpaque(true);
			container.component.setBackground(color);
		}
	}

	SwingPanel(SwingContainer<JPanel> container) {
		super(container);
		container.component.setOpaque(false);
	}

	void setLayout(LayoutManager layoutManager) {
		container.component.setLayout(layoutManager);
	}

	@Override
	public IContainer add() {
		return new SwingContainer<JComponent>(this);
	}

	@Override
	public ILayout layout() {
		return new SwingLayout(this);
	}

	@Override
	public IColor color() {
		return new SwingPanelColor();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R clear() {
		for (Component child : container.component.getComponents()) {
			child.setVisible(false);
			container.component.remove(child);
		}
		return (R) this;
	}

	@Override
	public void add(JComponent component) {
		container.component.add(component);
	}

	@Override
	public void remove(JComponent component) {
		container.component.remove(component);
	}

	JFrame frame() {
		return container.lookupSwingDisplay().frame;
	}

	@Override
	public IDisplay display() {
		return container.lookupSwingDisplay();
	}

	@Override
	public SwingDisplay lookupSwingDisplay() {
		return container.lookupSwingDisplay();
	}

	@Override
	public IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		return container.lookupWidgetProvider(interfaceClass);
	}
}