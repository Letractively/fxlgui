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
import android.widget.LinearLayout;
import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;

class AndroidVerticalPanel extends AndroidPanel<LinearLayout, IVerticalPanel>
		implements IVerticalPanel, Parent {

	private Parent internalContainer;
	private boolean hasChildren = false;
	private int spacing = 0;
	private int additionalSpace = 0;

	AndroidVerticalPanel(AndroidContainer container) {
		super(container);
		this.internalContainer = container.parent;
		view = new LinearLayout(container.parent.androidDisplay().activity);
		view.setOrientation(LinearLayout.VERTICAL);
		container.parent.add(view);
		container.layout(view);
	}

	IKey<IVerticalPanel> newAndroidKey(
			AndroidElement<LinearLayout, IVerticalPanel> androidElement) {
		return new AndroidKey<IVerticalPanel>(this);
	}

	@Override
	public IVerticalPanel addSpace(int pixel) {
		if (hasChildren) {
			View c = view.getChildAt(view.getChildCount() - 1);
			c.setPadding(c.getPaddingLeft(), c.getPaddingTop(), c
					.getPaddingRight(), pixel + c.getPaddingBottom());
		} else
			additionalSpace = pixel;
		return this;
	}

	@Override
	public IVerticalPanel spacing(int pixel) {
		this.spacing = pixel;
		view.setPadding(pixel, pixel, pixel, pixel);
		return this;
	}

	@Override
	public IAlignment<IVerticalPanel> align() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IVerticalPanel stretch(boolean stretch) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IContainer add() {
		AndroidContainer androidContainer = new AndroidContainer(this);
		if (hasChildren) {
			androidContainer.paddingTop += spacing;
		}
		if (additionalSpace != 0) {
			androidContainer.paddingTop += additionalSpace;
			additionalSpace = 0;
		}
		hasChildren = true;
		return androidContainer;
	}

	@Override
	public AndroidDisplay androidDisplay() {
		return internalContainer.androidDisplay();
	}

	@Override
	public void add(View view) {
		super.view.addView(view);
	}

	@Override
	public void remove(View view) {
		super.view.removeView(view);
	}

	@Override
	public View element() {
		return view;
	}

	@Override
	public void remove() {
		internalContainer.remove(view);
	}
}
