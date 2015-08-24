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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;

public class ColorField {

	public interface Adapter {

		void text(String text);
	}

	private static final String[] COLORS = new String[] { "black", "#000000",
			"gray", "#808080", "maroon", "#800000", "red", "#FF0000", "green",
			"#008000", "lime", "#00FF00", "olive", "#808000", "yellow",
			"#FFFF00", "navy", "#000080", "blue", "#0000FF", "purple",
			"#800080", "fuchsia", "#FF00FF", "teal", "#008080", "aqua",
			"#00FFFF", "silver", "#C0C0C0", "white", "#FFFFFF" };

	private class PopUp implements IClickListener {

		@Override
		public void onClick() {
			final IPopUp popUp = co.fxl.gui.impl.PopUp.showPopUp(true)
					.autoHide(true).width(180);
			int height = button.height();
			int y = button.offsetY() + height + 4;
			y = Math.min(y, Display.instance().height() - 190);
			int x = button.offsetX();
			x = Math.min(x, Display.instance().width() - 190);
			popUp.offset(x, y);
			IGridPanel grid = popUp.container().panel().grid().spacing(4)
					.width(180);
			grid.resize(4, 4);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					int n = i * 4 + j;
					IGridPanel p = grid.cell(j, i).valign().begin().panel()
							.grid();
					grid.spacing(4);
					IVerticalPanel p0 = p.cell(0, 0).panel().vertical()
							.size(40, 40);
					p0.border().color().black();
					p0.add().label().text("&#160;");
					final String colorName = COLORS[n * 2];
					final String colorValue = COLORS[n * 2 + 1];
					p0.color().rgb(r(colorValue), g(colorValue), b(colorValue));
					p.tooltip(colorName);
					p.cell(0, 1).align().center().label().text(colorName)
							.font().pixel(8).color().gray();
					p.addGridClickListener(new IGridClickListener() {
						@Override
						public void onClick(int column, int row) {
							a.text(colorValue);
							updateColor(colorValue);
							popUp.visible(false);
						}
					});
				}
			}
			popUp.visible(true);
		}
	}

	private IVerticalPanel button;
	private Adapter a;

	public ColorField(final ITextField tf, IContainer c) {
		this(new Adapter() {
			@Override
			public void text(String text) {
				tf.text(text);
			}
		}, c, 16);
		tf.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				updateColor(value);
			}
		});
	}

	public ColorField(Adapter a, IContainer c, int size) {
		this.a = a;
		button = c.panel().vertical().size(size, size);
		button.add().image().resource("empty_14x14.png")
				.size(size - 2, size - 2);
		button.border().width(1);
		button.addClickListener(new PopUp());
	}

	public void updateColor(String colorValue) {
		if (colorValue == null || colorValue.equals(""))
			colorValue = "#FFFFFF";
		try {
			button.color().rgb(r(colorValue), g(colorValue), b(colorValue));
		} catch (Exception e) {
		}
	}

	public static int hex(String colorValue, int i1, int i2) {
		int n = hex(colorValue, i1);
		int m = hex(colorValue, i2);
		return n * 16 + m;
	}

	public static int hex(String colorValue, int i1) {
		char c = colorValue.charAt(i1);
		if (c >= '0' && c <= '9')
			return c - '0';
		return c - 'A' + 10;
	}

	public static int b(String colorValue) {
		return hex(colorValue, 5, 6);
	}

	public static int r(String colorValue) {
		return hex(colorValue, 1, 2);
	}

	public static int g(String colorValue) {
		return hex(colorValue, 3, 4);
	}

	public ColorField clickable(boolean clickable) {
		button.clickable(clickable);
		if (clickable)
			button.border().color().black();
		else
			button.border().color().lightgray();
		return this;
	}

	public void visible(boolean editable) {
		button.visible(editable);
	}

	public IColored border() {
		return button.border();
	}
}
