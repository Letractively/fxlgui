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

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IEditable;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusable;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IShell;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public class ElementAdp<T extends IElement<T>> implements IElement<T>,
		IColored, IBordered, IFocusable<T>, IEditable<T> {

	protected IElement<?> element;

	ElementAdp() {
	}

	ElementAdp(T element) {
		this.element = element;
	}

	@SuppressWarnings("unchecked")
	protected T element() {
		return (T) element;
	}

	protected IElement<?> basicElement() {
		return element;
	}

	protected void element(T tf) {
		element = tf;
	}

	@Override
	public int offsetX() {
		return basicElement().offsetX();
	}

	@Override
	public int offsetY() {
		return basicElement().offsetY();
	}

	@Override
	public int width() {
		return basicElement().width();
	}

	@Override
	public int height() {
		return basicElement().height();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T offset(int x, int y) {
		basicElement().offset(x, y);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(int width) {
		basicElement().width(width);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(int height) {
		basicElement().height(height);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T size(int width, int height) {
		basicElement().size(width, height);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T visible(boolean visible) {
		basicElement().visible(visible);
		return (T) this;
	}

	@Override
	public boolean visible() {
		return basicElement().visible();
	}

	@Override
	public void remove() {
		basicElement().remove();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T tooltip(String tooltip) {
		basicElement().tooltip(tooltip);
		return (T) this;
	}

	@Override
	public IDisplay display() {
		return basicElement().display();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N> N nativeElement() {
		return (N) basicElement().nativeElement();
	}

	@Override
	public IPadding padding() {
		return basicElement().padding();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T padding(int padding) {
		basicElement().padding(padding);
		return (T) this;
	}

	@Override
	public IMargin margin() {
		return basicElement().margin();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T margin(int margin) {
		basicElement().margin(margin);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T opacity(double opacity) {
		basicElement().opacity(opacity);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N> T nativeElement(N nativeElement) {
		basicElement().nativeElement(nativeElement);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(double width) {
		basicElement().width(width);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(double height) {
		basicElement().height(height);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T iD(String iD) {
		basicElement().iD(iD);
		return (T) this;
	}

	@Override
	public String iD() {
		return basicElement().iD();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T editable(boolean editable) {
		((IEditable<T>) basicElement()).editable(editable);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean editable() {
		return ((IEditable<T>) basicElement()).editable();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T focus(boolean focus) {
		focusElement().focus(focus);
		return (T) this;
	}

	protected IFocusable<?> focusElement() {
		return (IFocusable<?>) element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addFocusListener(IUpdateListener<Boolean> hasFocus) {
		focusElement().addFocusListener(hasFocus);
		return (T) this;
	}

	@Override
	public IBorder border() {
		return ((IBordered) basicElement()).border();
	}

	@Override
	public IColor color() {
		return ((IColored) basicElement()).color();
	}

	@Override
	public IShell shell() {
		return ((IElement<?>) element).shell();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addStyle(String style) {
		basicElement().addStyle(style);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICursor<T> cursor() {
		return new ICursor<T>() {

			@Override
			public T waiting() {
				basicElement().cursor().waiting();
				return (T) ElementAdp.this;
			}

			@Override
			public T hand() {
				basicElement().cursor().hand();
				return (T) ElementAdp.this;
			}

			@Override
			public T pointer() {
				basicElement().cursor().pointer();
				return (T) ElementAdp.this;
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public T removeStyle(String style) {
		basicElement().removeStyle(style);
		return (T) this;
	}

}
