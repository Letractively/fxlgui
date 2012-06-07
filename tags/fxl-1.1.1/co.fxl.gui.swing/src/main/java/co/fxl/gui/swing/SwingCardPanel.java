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

import java.awt.CardLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;

class SwingCardPanel extends SwingPanel<ICardPanel> implements ICardPanel {

	private CardLayout cardLayout;
	private List<SwingContainer<?>> added = new LinkedList<SwingContainer<?>>();
	private JComponent chosen;

	SwingCardPanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(cardLayout = new ResizeCardLayout(this));
	}

	@Override
	public ICardPanel clear() {
		return super.clear();
	}

	@Override
	public IContainer add() {
		SwingContainer<?> child = (SwingContainer<?>) super.add();
		added.add(child);
		return child;
	}

	@Override
	public ICardPanel show(IElement<?> panel) {
		chosen = panel.nativeElement();
		cardLayout.show(container.component, ((HasUID) panel).getUID());
		return this;
	}

	@Override
	public void add(JComponent component) {
		SwingContainer<?> c = (SwingContainer<?>) find(component);
		if (chosen == null)
			chosen = c.component;
		container.component.add(component, c.getUID());
	}

	@Override
	public void remove(JComponent component) {
		super.remove(component);
		added.remove(find(component));
	}

	private IContainer find(JComponent component) {
		for (SwingContainer<?> c : added) {
			if (c.component == component) {
				return c;
			}
		}
		throw new RuntimeException("component in card panel not found");
	}

	JComponent chosen() {
		return chosen;
	}
}
