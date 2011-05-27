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

import android.view.View;
import android.widget.ScrollView;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IScrollPane;

class AndroidScrollPane implements IScrollPane, Parent {

	private ScrollView scrollView;
	private AndroidDisplay activity;

	AndroidScrollPane(AndroidContainer container) {
		activity = container.parent.androidDisplay();
		scrollView = new ScrollView(activity.activity);
		container.parent.add(scrollView);
	}

	@Override
	public IContainer viewPort() {
		return new AndroidContainer(this);
	}

	@Override
	public int height() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane height(int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public Object nativeElement() {
		throw new MethodNotImplementedException();
	}

	@Override
	public int offsetX() {
		throw new MethodNotImplementedException();
	}

	@Override
	public int offsetY() {
		throw new MethodNotImplementedException();
	}

	@Override
	public void remove() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane size(int width, int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane visible(boolean visible) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean visible() {
		throw new MethodNotImplementedException();
	}

	@Override
	public int width() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane width(int width) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IBorder border() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public AndroidDisplay androidDisplay() {
		return activity;
	}

	@Override
	public void add(View view) {
		scrollView.addView(view);
	}

	@Override
	public void remove(View view) {
		scrollView.removeView(view);
	}

	@Override
	public View element() {
		return scrollView;
	}

	@Override
	public IScrollPane addScrollListener(IScrollListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane tooltip(String tooltip) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay display() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		throw new MethodNotImplementedException();
	}

	@Override
	public int scrollOffset() {
		throw new MethodNotImplementedException();
	}
}
