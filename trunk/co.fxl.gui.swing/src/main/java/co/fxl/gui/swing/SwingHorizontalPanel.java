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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.swing.HorizontalLayoutManager.Stretch;

class SwingHorizontalPanel extends SwingPanel<IHorizontalPanel> implements
		IHorizontalPanel, Stretch {

	public class Alignment implements IAlignment<IHorizontalPanel> {

		@Override
		public IHorizontalPanel begin() {
			flowLayout().setAlignment(FlowLayout.LEFT);
			return SwingHorizontalPanel.this;
		}

		@Override
		public IHorizontalPanel center() {
			flowLayout().setAlignment(FlowLayout.CENTER);
			return SwingHorizontalPanel.this;
		}

		@Override
		public IHorizontalPanel end() {
			flowLayout().setAlignment(FlowLayout.RIGHT);
			return SwingHorizontalPanel.this;
		}
	}

	Component stretch;
	private HorizontalLayoutManager layoutManager;
	private Insets insets;

	SwingHorizontalPanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(layoutManager = new HorizontalLayoutManager(this));
		align().begin();
		spacing(0);
	}

	@Override
	public void add(JComponent component) {
		if (insets != null) {
			int l = 0;
			int r = 0;
			int t = 0;
			int b = 0;
			int cc = container.component.getComponentCount();
			if (cc == 0)
				l = insets.left;
			t = insets.top;
			b = insets.bottom;
			component.setBorder(new EmptyBorder(t, l, b, r));
		}
		super.add(component);
	}

	@Override
	public IHorizontalPanel spacing(int pixel) {
		flowLayout().setHgap(pixel);
		flowLayout().setVgap(pixel);
		return this;
	}

	private FlowLayout flowLayout() {
		return (FlowLayout) container.component.getLayout();
	}

	@Override
	public IHorizontalPanel addSpace(int pixel) {
		container.component.add(Box.createRigidArea(new Dimension(pixel, 1)));
		return this;
	}

	@Override
	public IAlignment<IHorizontalPanel> align() {
		return new Alignment();
	}

	@Override
	public Component stretch() {
		return stretch;
	}

	@Override
	void gap(int pixel) {
		layoutManager.setHgap(pixel);
	}

	@Override
	Insets insets() {
		if (insets == null)
			insets = new Insets(0, 0, 0, 0);
		return insets;
	}
}
