/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.rtf.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IImage;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.IHTMLArea.Formatting;

public class RichTextToolbarImpl {

	private abstract class ToolbarElement {

		abstract void updateStatus();

	}

	private class ToggleButton extends ToolbarElement {

		private IImage image;
		private Formatting f;

		private ToggleButton(final Formatting f) {
			this.f = f;
			image = panel.add().image()
					.resource(f.name().toLowerCase() + ".gif");
			image.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					updateImage();
					htmlArea.toggle(f);
				}
			});
			image.border().color().white();
		}

		@Override
		void updateStatus() {
			updateImage();
		}

		private void updateImage() {
			if (htmlArea.is(f))
				image.border().color().gray();
			else
				image.border().color().white();
		}
	}

	private IToolbar panel;
	private List<ToolbarElement> buttons = new LinkedList<ToolbarElement>();
	private IHTMLArea htmlArea;

	public RichTextToolbarImpl(IContainer c, IHTMLArea htmlArea) {
		panel = new ToolbarImpl(c).height(28).spacing(2);
		panel.color().white();
		panel.border().style().noBottom().color().gray(211);
		this.htmlArea = htmlArea;
		for (Formatting f : IHTMLArea.Formatting.values()) {
			ToggleButton b = new ToggleButton(f);
			buttons.add(b);
		}
	}

	public void updateStatus() {
		for (ToolbarElement e : buttons)
			e.updateStatus();
	}

}
