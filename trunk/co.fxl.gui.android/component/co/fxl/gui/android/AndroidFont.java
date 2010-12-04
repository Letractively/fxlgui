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

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.widget.TextView;
import co.fxl.gui.api.IFontElement.IFont;

class AndroidFont implements IFont {

	class FontWeight implements IWeight {

		FontWeight(TextView view) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IFont bold() {
			throw new MethodNotImplementedException();
		}

		@Override
		public IFont italic() {
			throw new MethodNotImplementedException();
		}

		@Override
		public IFont plain() {
			throw new MethodNotImplementedException();
		}
	}

	private TextView view;

	AndroidFont(TextView view) {
		this.view = view;
	}

	@Override
	public IFamily family() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFont pixel(int pixel) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixel);
		return this;
	}

	@Override
	public IFont underline(boolean underline) {
		if (underline) {
			SpannableString content = new SpannableString(view.getText());
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			view.setText(content);
		} else
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public IWeight weight() {
		return new FontWeight(view);
	}

	@Override
	public IColor color() {
		return new AndroidColor() {

			@Override
			void setAndroidColor(int c) {
				view.setTextColor(c);
			}
		};
	}
}
