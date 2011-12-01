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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class ImageJPanel extends JPanel {

	private static final long serialVersionUID = -128447932563673636L;
	private ImageIcon imageIcon;
	private Image image;
	private Dimension size;

	ImageJPanel() {
		super(null);
	}

	public void setImageURI(String uri) {
		imageIcon = new ImageIcon(uri);
		image = imageIcon.getImage();
		size = new Dimension(image.getWidth(null), image.getHeight(null));
		setPreferredSize(size);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void setPreferredSize(Dimension size) {
		this.size = size;
		super.setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		repaint();
	}
}