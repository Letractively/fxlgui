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
package co.fxl.gui.swing;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import co.fxl.gui.api.IImage;

class SwingImage extends SwingElement<JLabel, IImage> implements IImage {

	private Icon icon;
	private String resource;

	SwingImage(SwingContainer<JLabel> container) {
		super(container);
	}

	@Override
	public IImage uRI(String uri) {
		icon = new ImageIcon(uri);
		return getIcon(uri);
	}

	@Override
	public IImage resource(String name) {
		resource = name;
		if (name == null) {
			container.component.setIcon(null);
			return this;
		}
		URL uri = SwingImage.class.getResource("/public/images/" + name);
		if (uri == null)
			throw new UnsupportedOperationException(name);
		icon = new ImageIcon(uri);
		return getIcon(uri.toString());
	}

	private IImage getIcon(String uri) {
		if (icon.getIconHeight() == -1)
			throw new RuntimeException("image " + uri + " not found");
		container.component.setIcon(icon);
		return this;
	}

	@Override
	public IImage localURI(String uRI) {
		if (uRI.startsWith("upload?name="))
			uRI = uRI.substring("upload?name=".length());
		try {
			File f = new File(System.getProperty("java.io.tmpdir") + uRI);
			URL url = f.toURI().toURL();
			icon = new ImageIcon(url);
			return getIcon(url.toString());
		} catch (MalformedURLException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public String resource() {
		return resource;
	}

	@Override
	public IColor color() {
		return new SwingColor() {
			@Override
			protected void setColor(Color color) {
				container.component.setBackground(color);
			}
		};
	}
}
