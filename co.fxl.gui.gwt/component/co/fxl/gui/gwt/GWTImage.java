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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class GWTImage extends GWTElement<Image, IImage> implements IImage {

	public interface ImageResourceProvider {

		ImageResource resolve(String resource);
	}

	private static ImageResourceProvider imageResourceProvider;

	public static void abstractImagePrototypeProvider(
			ImageResourceProvider imageResourceProvider) {
		GWTImage.imageResourceProvider = imageResourceProvider;
	}

	private String resource;

	GWTImage(GWTContainer<Image> container) {
		super(container);
	}

	@Override
	public IImage clickable(boolean enable) {
		container.widget.getElement().getStyle().setOpacity(enable ? 1 : 0.5);
		return super.clickable(enable);
	}

	@Override
	public IImage uRI(String uri) {
		Image.prefetch(uri);
		container.widget.setUrl(uri);
		return this;
	}

	@Override
	public IImage localURI(String uri) {
		uRI(GWT.getModuleBaseURL() + uri);
		return this;
	}

	@Override
	public IImage resource(String name) {
		this.resource = name;
		if (name == null) {
			container.widget.setVisible(false);
			return this;
		}
		if (imageResourceProvider != null) {
			ImageResource prototype = imageResourceProvider.resolve(name);
			if (prototype != null) {
				container.widget.setResource(prototype);
				return this;
			}
		}
		String path = GWT.getModuleBaseURL();
		return uRI(path + "images/" + name);
	}

	@Override
	GWTClickHandler<IImage> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IImage>(this, clickListener);
	}

	@Override
	public String resource() {
		return resource;
	}
}
