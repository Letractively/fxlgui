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
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;

class SwingElement<T extends JComponent, R> implements IElement<R> {

	SwingContainer<T> container;
	private List<ClickListenerMouseAdapter<R>> adapters = new LinkedList<ClickListenerMouseAdapter<R>>();

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
		return container.component.getHeight();
	}

	@Override
	public int width() {
		return container.component.getWidth();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public R clickable(boolean clickable) {
		for (ClickListenerMouseAdapter adapter : adapters) {
			container.component.removeMouseListener(adapter.adapter);
			if (clickable) {
				container.component.addMouseListener(adapter.adapter);
			}
		}
		container.component.setEnabled(clickable);
		container.component
				.setCursor(clickable ? new Cursor(Cursor.HAND_CURSOR)
						: new Cursor(Cursor.DEFAULT_CURSOR));
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public IKey<R> addClickListener(IClickListener listener) {
		container.component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ClickListenerMouseAdapter<R> adapter = new ClickListenerMouseAdapter<R>(
				(R) this, listener);
		adapters.add(adapter);
		container.component.addMouseListener(adapter.adapter);
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
}
