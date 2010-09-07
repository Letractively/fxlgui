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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

class GWTImage extends GWTElement<Image, IImage> implements IImage {

	GWTImage(GWTContainer<Image> container) {
		super(container);
	}

	@Override
	public IImage uRI(String uri) {
		container.widget.setUrl(uri);
		return this;
	}

	@Override
	public IImage resource(String name) {
		if (name == null) {
			container.widget.setVisible(false);
			return this;
		}
		String path = GWT.getModuleBaseURL();
		return uRI(path + "images/" + name);
	}

	@Override
	GWTClickHandler<IImage> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IImage>(this, clickListener);
	}
}
