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
package co.fxl.gui.register.api;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IVerticalPanel;

public interface IRegister {

	public interface ITitle {

		ITitle text(String title);

		IFont font();

		IColor color();

		IBorder border();

		boolean isEmpty();
	}

	public interface IRegisterListener {

		void onTop(boolean visible, ICallback<Void> cb);
	}

	ITitle title();

	IVerticalPanel contentPanel();

	IRegister listener(IRegisterListener listener);

	IRegister top(ICallback<Void> cb);

	IRegister visible(boolean visible);

	IRegister enabled(boolean enabled);

	boolean isActive();

	boolean enabled();

	IRegister imageResource(String imageResource);

	IRegister toggleLoading(boolean loading);

	void showTitleAsEmpty(boolean empty);

	IVerticalPanel newContentPanel();

	void showNewContentPanel();
}
