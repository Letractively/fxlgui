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

import android.view.ViewGroup;
import android.widget.LinearLayout;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;

class AndroidPanel<R extends ViewGroup, T extends IElement<T>> extends
		AndroidElement<R, T> implements IPanel<T> {

	AndroidPanel(AndroidContainer container) {
		super(container);
	}

	@Override
	public IContainer add() {
		throw new MethodNotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T height(int height) {
		view.setLayoutParams(new LinearLayout.LayoutParams(view
				.getLayoutParams().width, height));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T clear() {
		view.removeAllViews();
		return (T) this;
	}

	@Override
	public IDisplay display() {
		return container.parent.androidDisplay();
	}

	@Override
	public ILayout layout() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IBorder border() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColor color() {
		return new AndroidColor() {
			@Override
			void setAndroidColor(int c) {
				view.setBackgroundColor(c);
			}
		};
	}
}
