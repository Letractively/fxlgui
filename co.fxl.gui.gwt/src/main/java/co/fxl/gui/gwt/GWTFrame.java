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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IFrame;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Frame;

class GWTFrame extends GWTElement<Frame, IFrame> implements IFrame {

	GWTFrame(GWTContainer<Frame> container) {
		super(container);
	}

	@Override
	public IFrame uRI(String uRI) {
		container.widget.setUrl(uRI);
		return this;
	}

	@Override
	public IFrame addLoadListener(final ILoadListener l) {
		container.widget.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				l.onLoad();
			}
		});
		return this;
	}
}
