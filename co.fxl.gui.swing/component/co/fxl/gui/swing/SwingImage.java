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
package co.fxl.gui.swing;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import co.fxl.gui.api.IImage;

class SwingImage extends SwingElement<JLabel, IImage> implements IImage {

	private Icon icon;

	SwingImage(SwingContainer<JLabel> container) {
		super(container);
	}

	@Override
	public IImage uRI(String uri) {
		icon = new ImageIcon(uri);
		return getIcon(uri);
	}

	@Override
	public IKey<IImage> addClickListener(IClickListener listener) {
		return super.addClickListener(listener);
	}

	@Override
	public IImage resource(String name) {
		if (name == null) {
			container.component.setIcon(null);
			return this;
		}
		URL uri = SwingImage.class.getResource("/public/images/" + name);
		if (uri == null)
			throw new MethodNotImplementedException(name);
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
		throw new MethodNotImplementedException();
	}
}
