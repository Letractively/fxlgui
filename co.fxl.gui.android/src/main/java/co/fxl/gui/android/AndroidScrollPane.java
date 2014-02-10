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
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IShell;

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
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane height(int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int offsetX() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int offsetY() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane size(int width, int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane visible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean visible() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int width() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane width(int width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBorder border() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IColor color() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane tooltip(String tooltip) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IDisplay display() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int scrollOffset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane offset(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane horizontal() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IPadding padding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane padding(int padding) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMargin margin() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane margin(int margin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane opacity(double opacity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <N> IScrollPane nativeElement(N nativeElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane width(double width) {
		// TODO ... throw new UnsupportedOperationException();
		return this;
	}

	@Override
	public IScrollPane height(double height) {
		// TODO ... throw new UnsupportedOperationException();
		return this;
	}

	@Override
	public <N> N nativeElement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane bidirectional() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane iD(String iD) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String iD() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollBars scrollBars() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IShell shell() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane addStyle(String style) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICursor<IScrollPane> cursor() {
		return new ICursor<IScrollPane>() {

			@Override
			public IScrollPane waiting() {
				return (IScrollPane) AndroidScrollPane.this;
			}

			@Override
			public IScrollPane hand() {
				return (IScrollPane) AndroidScrollPane.this;
			}

			@Override
			public IScrollPane pointer() {
				return (IScrollPane) AndroidScrollPane.this;
			}

		};
	}

	@Override
	public IScrollPane removeStyle(String style) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IScrollPane minWidth(int minWidth) {
		throw new UnsupportedOperationException();
	}

	// @Override
	// public IScrollbar scrollbar() {
	// throw new UnsupportedOperationException();
	// }
}
