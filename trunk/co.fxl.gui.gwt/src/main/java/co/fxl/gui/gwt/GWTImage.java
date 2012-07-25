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

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IImage;
import co.fxl.gui.impl.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class GWTImage extends GWTElement<Image, IImage> implements IImage {

	private static final String IMAGES = "images/";
	public static boolean IS_CHROME_15_PLUS = GWTDisplay.isChrome()
			&& GWTDisplay.getBrowserVersion() >= 15;
	public static String IMAGE_PATH = Constants.get(
			"GWTLazyTreeWidget.IMAGE_PATH",
			(IS_CHROME_15_PLUS ? "" : GWT.getModuleBaseURL()) + "images/");
	private static Map<String, ImageResource> map = new HashMap<String, ImageResource>();

	public static ImageResource resolve(String resource) {
		return map.get(resource);
	}

	public static void register(String name, ImageResource resource) {
		map.put(name, resource);
	}

	public static String getImageURL(String path, String treeIcon) {
		if (IS_CHROME_15_PLUS) {
			ImageResource ir = GWTImage.resolve(treeIcon);
			if (ir != null)
				return ir.getURL();
		}
		return path + treeIcon;
	}

	public static String getImageURL(String string) {
		return getImageURL(IMAGE_PATH, string);
	}

	private String resource;

	GWTImage(GWTContainer<Image> container) {
		super(container);
	}

	@Override
	public IImage clickable(boolean enable) {
		if (GWTDisplay.isInternetExplorer8OrBelow()) {
			ImageResource resolve = resolve(resource);
			if (resource != null && resolve != null && !enable) {
				String p50resource = resource.substring(0,
						resource.indexOf(".png"))
						+ "_50p.png";
				if (resolve(p50resource) != null) {
					container.widget.setResource(resolve(p50resource));
				}
			} else if (resource != null) {
				if (resolve != null)
					container.widget.setResource(resolve);
				else
					localURI(IMAGES + resource);
			}
		} else
			container.widget.getElement().getStyle()
					.setOpacity(enable ? 1 : 0.5);
		return super.clickable(enable);
	}

	@Override
	public IImage uRI(String uri) {
		container.widget.setUrl(uri);
		return this;
	}

	@Override
	public IImage localURI(String uri) {
		return uRI(getURI(uri));
	}

	public static String getURI(String uri) {
		return GWT.getModuleBaseURL() + uri;
	}

	public static String getResourceURI(String uri) {
		return GWT.getModuleBaseURL() + IMAGES + uri;
	}

	@Override
	public IImage resource(String name) {
		this.resource = name;
		if (name == null) {
			container.widget.setVisible(false);
			return this;
		}
		ImageResource prototype = resolve(name);
		if (prototype != null) {
			container.widget.setResource(prototype);
			return this;
		}
		return localURI(IMAGES + name);
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
