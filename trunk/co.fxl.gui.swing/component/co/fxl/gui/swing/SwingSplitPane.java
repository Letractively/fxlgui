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

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ISplitPane;

class SwingSplitPane extends SwingElement<JSplitPane, ISplitPane> implements
		ISplitPane {

	SwingSplitPane(SwingContainer<JSplitPane> container) {
		super(container);
		container.component.setBackground(Color.WHITE);
		container.component.setOpaque(false);
		container.component.setBorder(null);
	}

	@Override
	public ISplitPane vertical() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IContainer first() {
		return new SwingContainer<JComponent>(container.parent) {
			void setComponent(JComponent component) {
				super.component = component;
				container.component.setLeftComponent(component);
			}
		};
	}

	@Override
	public IContainer second() {
		return new SwingContainer<JComponent>(container.parent) {
			void setComponent(JComponent component) {
				super.component = component;
				container.component.setRightComponent(component);
			}
		};
	}

	@Override
	public ISplitPane splitPosition(int pixel) {
		container.component.setDividerLocation(pixel);
		return this;
	}

	@Override
	public IBorder border() {
		return new SwingBorder(container.component);
	}

	@Override
	public ISplitPane addResizeListener(final ISplitPaneResizeListener l) {
		container.component
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						if (arg0.getPropertyName().equals(
								JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
							l.onResize((Integer) arg0.getNewValue());
						}
					}
				});
		return this;
	}

	@Override
	public int splitPosition() {
		return container.component.getDividerLocation();
	}
}
