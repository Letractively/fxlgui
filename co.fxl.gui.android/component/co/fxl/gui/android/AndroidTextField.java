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

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable;

class AndroidTextField extends AndroidElement<EditText, ITextField> implements
		ITextField {

	AndroidTextField(AndroidContainer container) {
		super(container);
		view = new EditText(container.parent.androidDisplay().activity);
		view.setLayoutParams(new LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		container.parent.add(view);
	}

	@Override
	public ITextField text(String text) {
		view.setText(text);
		return this;
	}

	@Override
	public String text() {
		return (String) view.getText().toString();
	}

	@Override
	public IFont font() {
		return new AndroidFont(view);
	}

	@Override
	public ITextField editable(boolean editable) {
		view.setEnabled(editable);
		return this;
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

	@Override
	public IBorder border() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			final IUpdateListener<String> listener) {
		view.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				listener.onUpdate(text());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});
		return this;
	}

	@Override
	public ITextField maxLength(int maxLength) {
		throw new MethodNotImplementedException();
	}
}
