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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;

class SwingElement<T extends JComponent, R> implements IElement<R> {

	protected SwingContainer<T> container;
	private List<ClickListenerMouseAdapter<R>> adapters = new LinkedList<ClickListenerMouseAdapter<R>>();
	private boolean clickListenerAdded = false;

	SwingElement(SwingContainer<T> container) {
		this.container = container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R visible(boolean visible) {
		container.component.setVisible(visible);
		return (R) this;
	}

	@Override
	public int height() {
		return container.component.getPreferredSize().height;
	}

	@Override
	public int width() {
		return container.component.getPreferredSize().width;
	}

	@Override
	public void remove() {
		container.parent.remove(container.component);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(int height) {
		container.component.setPreferredSize(new Dimension(width(), height));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R width(int width) {
		container.component.setPreferredSize(new Dimension(width, height()));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R size(int width, int height) {
		container.component.setPreferredSize(new Dimension(width, height));
		return (R) this;
	}

	public IBorder border() {
		return new SwingBorder(this);
	}

	public boolean clickable() {
		return container.component.isEnabled();
	}

	@SuppressWarnings({ "unchecked" })
	public R clickable(boolean clickable) {
		// for (ClickListenerMouseAdapter adapter : adapters) {
		// container.component.removeMouseListener(adapter.adapter);
		// if (clickable) {
		// container.component.addMouseListener(adapter.adapter);
		// }
		// }
		container.component.setEnabled(clickable);
		container.component
				.setCursor(clickable ? new Cursor(Cursor.HAND_CURSOR)
						: new Cursor(Cursor.DEFAULT_CURSOR));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public IKey<R> addClickListener(IClickListener listener) {
		// assert !clickListenerAdded :
		// "Multiple click listeners are not yet supported";
		clickable(true);
		// container.component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ClickListenerMouseAdapter<R> adapter = new ClickListenerMouseAdapter<R>(
				(R) this, listener);
		adapters.add(adapter);
		if (!clickListenerAdded) {
			container.component.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (container.component.isEnabled())
						for (ClickListenerMouseAdapter<R> adapter : adapters) {
							adapter.adapter.mouseReleased(e);
						}
				}
			});
			clickListenerAdded = true;
		}
		return adapter;
	}

	@Override
	public Object nativeElement() {
		return container.component;
	}

	@Override
	public boolean visible() {
		return container.component.isVisible();
	}

	@Override
	public int offsetX() {
		return (int) point().getX();
	}

	@Override
	public int offsetY() {
		return (int) point().getY();
	}

	private Point point() {
		Point pt = SwingUtilities.convertPoint(container.component, new Point(
				0, 0), container.lookupSwingDisplay().container.component);
		return pt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R tooltip(String tooltip) {
		container.component.setToolTipText(tooltip);
		return (R) this;
	}

	@Override
	public IDisplay display() {
		return container.display();
	}

	@SuppressWarnings("unchecked")
	public R focus() {
		container.component.requestFocus();
		return (R) this;
	}
}
