/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.ISpaced.ISpacing;

public class SwingPanel<R> extends SwingElement<PanelComponent, R> implements
		IPanel<R>, ComponentParent {

	final class SwingPanelColor extends SwingColor {

		@Override
		protected void setColor(Color color) {
			container.component.setOpaque(color != null);
			container.component.setBackground(color);
			container.component.fxlColor = this;
		}
	}

	protected SwingPanel(SwingContainer<PanelComponent> container) {
		super(container);
		container.component.setOpaque(false);
	}

	protected void setLayout(LayoutManager layoutManager) {
		container.component.setLayout(layoutManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(int height) {
		if (height >= 1 || height == -1)
			container.component.preferredHeight = height;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R width(int width) {
		if (width >= 1 || width == -1)
			container.component.preferredWidth = width;
		return (R) this;
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
		container.component.removeAll();
		// for (Component child : container.component.getComponents()) {
		// // child.setVisible(false);
		// container.component.remove(child);
		// }
		return (R) this;
	}

	@Override
	public void add(JComponent component) {
		container.component.add(component);
	}

	@Override
	public void remove(JComponent component) {
		container.component.requestFocus();
		container.component.remove(component);
		frame().validate();
	}

	JFrame frame() {
		return SwingDisplay.instance.frame;
	}

	@Override
	public IDisplay display() {
		return SwingDisplay.instance;
	}

	@Override
	public JComponent getComponent() {
		return container.component;
	}

	public co.fxl.gui.api.ILinearPanel.ISpacing spacing() {
		return new ISpacing() {

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing left(int pixel) {
				insets().left = pixel;
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing right(int pixel) {
				insets().right = pixel;
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing top(int pixel) {
				insets().top = pixel;
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing bottom(int pixel) {
				insets().bottom = pixel;
				return this;
			}

			@Override
			public co.fxl.gui.api.ILinearPanel.ISpacing inner(int pixel) {
				gap(pixel);
				return this;
			}

			@Override
			public ISpacing outer(int pixel) {
				return left(pixel).right(pixel).top(pixel).bottom(pixel);
			}
		};
	}

	void gap(int pixel) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R add(IElement<?> element) {
		JComponent nativeElement = (JComponent) element.nativeElement();
		add(nativeElement);
		return (R) this;
	}

	Insets insets() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R addResizeListener(
			final co.fxl.gui.api.IPanel.IResizeListener listener) {
		container.component.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				listener.onResize(width(), height());
			}
		});
		return (R) this;
	}

	@Override
	public ComponentParent getParent() {
		return container.parent;
	}
}