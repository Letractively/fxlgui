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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable;

class AndroidComboBox extends AndroidElement<Spinner, IComboBox> implements
		IComboBox, OnItemSelectedListener {

	private ArrayAdapter<String> adapter;
	private Activity activity;
	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	private String[] texts;

	AndroidComboBox(AndroidContainer container) {
		super(container);
		activity = container.parent.androidDisplay().activity;
		view = new Spinner(activity);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		container.parent.add(view);
	}

	@Override
	public String text() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IComboBox tooltip(String text) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFont font() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IComboBox editable(boolean editable) {
		view.setEnabled(editable);
		return this;
	}

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IComboBox addText(String... texts) {
		if (adapter != null)
			throw new MethodNotImplementedException();
		this.texts = texts;
		adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, texts);
		view.setAdapter(adapter);
		view.setOnItemSelectedListener(this);
		return this;
	}

	@Override
	public IComboBox text(String text) {
		for (int i = 0; i < texts.length; i++) {
			if (texts[i].equals(text)) {
				view.setSelection(i);
			}
		}
		return this;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			IUpdateListener<String> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		for (IUpdateListener<String> listener : listeners) {
			listener.onUpdate(texts[arg2]);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		throw new MethodNotImplementedException();
	}
}
