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
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;

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

	@Override
	public T height(int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public Object nativeElement() {
		return view;
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
		throw new MethodNotImplementedException();
	}

	@Override
	public T size(int width, int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public T visible(boolean visible) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean visible() {
		throw new MethodNotImplementedException();
	}

	@Override
	public int width() {
		return view.getWidth();
	}

	@Override
	public T width(int width) {
		throw new MethodNotImplementedException();
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
		throw new MethodNotImplementedException();
	}

	public boolean clickable() {
		return view.isClickable();
	}
}
