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

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IUpdateable;

class AndroidCheckBox extends AndroidElement<CheckBox, ICheckBox> implements
		ICheckBox {

	AndroidCheckBox(AndroidContainer container) {
		super(container);
		view = new CheckBox(container.parent.androidDisplay().activity);
		container.layout(view);
		container.parent.add(view);
	}

	@Override
	public ICheckBox text(String text) {
		view.setText(text);
		return this;
	}

	@Override
	public String text() {
		return (String) view.getText();
	}

	@Override
	public ICheckBox tooltip(String text) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFont font() {
		return new AndroidFont(view);
	}

	@Override
	public ICheckBox checked(boolean checked) {
		view.setChecked(checked);
		return this;
	}

	@Override
	public boolean checked() {
		return view.isChecked();
	}

	@Override
	public ICheckBox editable(boolean editable) {
		view.setEnabled(editable);
		return this;
	}

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IUpdateable<Boolean> addUpdateListener(
			final IUpdateListener<Boolean> listener) {
		view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				listener.onUpdate(arg1);
			}
		});
		return this;
	}
}
