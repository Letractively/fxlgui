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
import android.widget.TextView;
import co.fxl.gui.api.ILabel;

class AndroidLabel extends AndroidElement<TextView, ILabel> implements ILabel {

	private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
	private boolean isHyperlink;

	AndroidLabel(AndroidContainer container) {
		super(container);
		view = new TextView(container.parent.androidDisplay().activity);
		container.layout(view);
		container.parent.add(view);
	}

	@Override
	public ILabel text(String text) {
		view.setText(text);
		return this;
	}

	@Override
	public ILabel autoWrap(boolean autoWrap) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILabel hyperlink() {
		isHyperlink = true;
		font().color().blue();
		font().underline(true);
		return this;
	}

	@Override
	public ILabel clickable(boolean enable) {
		if (isHyperlink) {
			if (enable) {
				font().underline(true);
				font().color().blue();
			} else {
				font().underline(false);
				font().color().gray();
			}
		}
		return this;
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<ILabel> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
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
		return new AndroidKey<ILabel>(this);
	}

	@Override
	public String text() {
		return (String) view.getText();
	}

	@Override
	public boolean clickable() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ILabel tooltip(String text) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFont font() {
		return new AndroidFont(view);
	}
}
