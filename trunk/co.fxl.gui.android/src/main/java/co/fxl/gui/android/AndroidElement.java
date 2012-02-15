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
package co.fxl.gui.android;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

class AndroidElement<R extends View, T> implements IElement<T> {

	AndroidContainer container;
	R view;
	private List<IClickListener> clickListeners = new LinkedList<IClickListener>();

	AndroidElement(AndroidContainer container) {
		this.container = container;
	}

	@Override
	public int height() {
		return view.getHeight();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(int height) {
		view.getLayoutParams().height = height;
		return (T) this;
	}

	@Override
	public <N> N nativeElement() {
		return (N) view;
	}

	@Override
	public int offsetX() {
		return view.getLeft();
	}

	@Override
	public int offsetY() {
		return view.getTop();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T size(int width, int height) {
		width(width);
		return height(height);
	}

	@Override
	public T visible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visible() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int width() {
		return view.getWidth();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T width(int width) {
		view.getLayoutParams().width = width;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T clickable(boolean enable) {
		view.setEnabled(enable);
		view.setClickable(enable);
		return (T) this;
	}

	public IClickable.IKey<T> addClickListener(
			IClickable.IClickListener clickListener) {
		if (clickListeners.isEmpty()) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					for (IClickListener l : clickListeners)
						l.onClick();
				}
			});
		}
		clickListeners.add(clickListener);
		return newAndroidKey(this);
	}

	IKey<T> newAndroidKey(AndroidElement<R, T> androidElement) {
		throw new UnsupportedOperationException();
	}

	public boolean clickable() {
		return view.isClickable();
	}

	public IDisplay display() {
		throw new UnsupportedOperationException();
	}

	public T focus(boolean focus) {
		throw new UnsupportedOperationException();
	}

	public T addFocusListener(IUpdateListener<Boolean> hasFocus) {
		throw new UnsupportedOperationException();
	}

	public IBorder border() {
		throw new UnsupportedOperationException();
	}

	public T addCarriageReturnListener(IClickListener listener) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T tooltip(final String tooltip) {
		view.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				IPopUp popUp = display().showPopUp();
				popUp.container().label().text(tooltip);
				popUp.autoHide(true);
				popUp.visible(true);
				return false;
			}
		});
		return (T) this;
	}

	@Override
	public T offset(int x, int y) {
		throw new UnsupportedOperationException();
	}

	public co.fxl.gui.api.IKeyRecipient.IKey<T> addKeyListener(
			IClickListener listener) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public T draggable(boolean draggable) {
		// TODO ... throw new UnsupportedOperationException();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T addDragStartListener(co.fxl.gui.api.IDraggable.IDragStartListener l) {
		// TODO ... throw new UnsupportedOperationException();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T addDragOverListener(co.fxl.gui.api.IDropTarget.IDragMoveListener l) {
		// TODO ... throw new UnsupportedOperationException();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T addDropListener(co.fxl.gui.api.IDropTarget.IDropListener l) {
		// TODO ... throw new UnsupportedOperationException();
		return (T) this;
	}

	@Override
	public IPadding padding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T padding(int padding) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMargin margin() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T margin(int margin) {
		throw new UnsupportedOperationException();
	}
}
