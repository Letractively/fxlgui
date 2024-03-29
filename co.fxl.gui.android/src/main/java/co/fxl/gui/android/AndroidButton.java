/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import android.widget.Button;
import android.widget.TextView;
import co.fxl.gui.api.IButton;

class AndroidButton extends AndroidElement<TextView, IButton> implements
		IButton {

	AndroidButton(AndroidContainer container) {
		super(container);
		view = new Button(container.parent.androidDisplay().activity);
		container.parent.add(view);
	}

	IKey<IButton> newAndroidKey(AndroidElement<TextView, IButton> androidElement) {
		return new AndroidKey<IButton>(this);
	}

	@Override
	public IButton text(String text) {
		view.setText(text);
		return this;
	}

	@Override
	public String text() {
		return (String) view.getText();
	}

	@Override
	public IFont font() {
		return new AndroidFont(view);
	}

	@Override
	public IButton addMouseOverListener(
			co.fxl.gui.api.IMouseOverElement.IMouseOverListener l) {
		throw new UnsupportedOperationException();
	}
}
