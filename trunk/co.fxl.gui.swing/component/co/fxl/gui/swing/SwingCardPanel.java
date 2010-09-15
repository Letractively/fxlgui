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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;

class SwingCardPanel extends SwingPanel<ICardPanel> implements ICardPanel {

	private static Random RANDOM = new Random();
	private String uIDPrefix = RANDOM.nextLong() + "-"
			+ System.currentTimeMillis() + "-";
	private CardLayout cardLayout;
	private Map<JComponent, String> panel2uID = new HashMap<JComponent, String>();
	JComponent chosen;
	private String currentID;

	SwingCardPanel(SwingContainer<JPanel> container) {
		super(container);
		setLayout(cardLayout = new ResizeCardLayout(this));
	}

	@Override
	public ICardPanel clear() {
		panel2uID.clear();
		chosen = null;
		currentID = null;
		return super.clear();
	}

	@Override
	public IContainer add() {
		IContainer child = super.add();
		currentID = nextID();
		return child;
	}

	@Override
	public ICardPanel show(IElement<?> panel) {
		SwingElement<?, ?> swingElement = (SwingElement<?, ?>) panel;
		chosen = swingElement.container.component;
		String uID = panel2uID.get(chosen);
		cardLayout.show(container.component, uID);
		return this;
	}

	@Override
	void add(JComponent component) {
		container.component.add(component, currentID);
		panel2uID.put(component, currentID);
	}

	@Override
	void remove(JComponent component) {
		super.remove(component);
		panel2uID.remove(component);
	}

	private String nextID() {
		return uIDPrefix + panel2uID.size();
	}
}
