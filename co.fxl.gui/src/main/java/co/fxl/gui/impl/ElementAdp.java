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
package co.fxl.gui.impl;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;

public class ElementAdp<T> implements IElement<T> {

	private IElement<T> element;

	ElementAdp(IElement<T> element) {
		this.element = element;
	}

	@Override
	public int offsetX() {
		return element.offsetX();
	}

	@Override
	public int offsetY() {
		return element.offsetY();
	}

	@Override
	public int width() {
		return element.width();
	}

	@Override
	public int height() {
		return element.height();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T offset(int x, int y) {
		element.offset(x, y);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(int width) {
		 element.width(width);
			return (T) this;
	}

	@Override
	public T height(int height) {
		return element.height(height);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T size(int width, int height) {
		element.size(width, height);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T visible(boolean visible) {
		element.visible(visible);
		return (T) this;
	}

	@Override
	public boolean visible() {
		return element.visible();
	}

	@Override
	public void remove() {
		element.remove();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T tooltip(String tooltip) {
		element.tooltip(tooltip);
		return (T) this;
	}

	@Override
	public IDisplay display() {
		return element.display();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N> N nativeElement() {
		return (N) element.nativeElement();
	}

	@Override
	public IPadding padding() {
		return element.padding();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T padding(int padding) {
		element.padding(padding);
		return (T) this;
	}

	@Override
	public IMargin margin() {
		return element.margin();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T margin(int margin) {
		element.margin(margin);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T opacity(double opacity) {
		element.opacity(opacity);
		return (T) this;
	}

	@Override
	public <N> T nativeElement(N nativeElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T width(double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T height(double height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T iD(String iD) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String iD() {
		throw new UnsupportedOperationException();
	}

}
