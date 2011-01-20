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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IVerticalPanel;

class SwingVerticalPanel extends SwingPanel<IVerticalPanel> implements
		IVerticalPanel {

	private VerticalLayoutManager layoutManager;

	SwingVerticalPanel(SwingContainer<JPanel> container) {
		super(container);
		setLayout(layoutManager = new VerticalLayoutManager());
	}

	@Override
	public IVerticalPanel addSpace(int pixel) {
		container.component.add(Box.createRigidArea(new Dimension(1, pixel)));
		return this;
	}

	@Override
	public IVerticalPanel spacing(int pixel) {
		layoutManager.vgap = pixel;
		layoutManager.insets = new Insets(pixel, pixel, pixel, pixel);
		return this;
	}

	@Override
	public void add(JComponent component) {
		super.add(component);
	}

	@Override
	public IVerticalPanel stretch(boolean stretch) {
		layoutManager.stretch = stretch;
		return this;
	}

	@Override
	public IAlignment<IVerticalPanel> align() {
		return new IAlignment<IVerticalPanel>() {

			@Override
			public IVerticalPanel begin() {
				// TODO ... throw new MethodNotImplementedException();
				return SwingVerticalPanel.this;
			}

			@Override
			public IVerticalPanel center() {
				// TODO ... throw new MethodNotImplementedException();
				return SwingVerticalPanel.this;
			}

			@Override
			public IVerticalPanel end() {
				// TODO ... throw new MethodNotImplementedException();
				return SwingVerticalPanel.this;
			}
		};
	}
}
