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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class ColorField {

	private static final String[] COLORS = new String[] { "black", "#000000",
			"gray", "#808080", "maroon", "#800000", "red", "#FF0000", "green",
			"#008000", "lime", "#00FF00", "olive", "#808000", "yellow",
			"#FFFF00", "navy", "#000080", "blue", "#0000FF", "purple",
			"#800080", "fuchsia", "#FF00FF", "teal", "#008080", "aqua",
			"#00FFFF", "silver", "#C0C0C0", "white", "#FFFFFF" };

	private class PopUp implements IClickListener {

		@Override
		public void onClick() {
			final IPopUp popUp = button.display().showPopUp().autoHide(true).width(180);
			popUp.border().style().shadow();
			int height = button.height();
			popUp.offset(button.offsetX(), button.offsetY() + height + 4);
			IGridPanel grid = popUp.container().panel().grid().spacing(4).width(180);
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
							.font().pixel(10).color().gray();
					p.addGridClickListener(new IGridClickListener() {
						@Override
						public void onClick(int column, int row) {
							tf.text(colorValue);
							updateColor(colorValue);
							popUp.visible(false);
						}
					});
				}
			}
			popUp.visible(true);
		}
	}

	private ITextField tf;
	private IVerticalPanel button;

	public ColorField(ITextField tf, IContainer c) {
		this.tf = tf;
		button = c.panel().vertical().spacing(2).add().panel().vertical()
				.size(16, 16);
		button.add().label().text("&#160;");
		button.border().width(1);
		button.addClickListener(new PopUp());
		tf.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				updateColor(value);
			}
		});
	}

	public void updateColor(String colorValue) {
		if (colorValue == null || colorValue.equals(""))
			colorValue = "#FFFFFF";
		try {
			button.color().rgb(r(colorValue), g(colorValue), b(colorValue));
		} catch (Exception e) {
		}
	}

	private int hex(String colorValue, int i1, int i2) {
		int n = hex(colorValue, i1);
		int m = hex(colorValue, i2);
		return n * 16 + m;
	}

	private int hex(String colorValue, int i1) {
		char c = colorValue.charAt(i1);
		if (c >= '0' && c <= '9')
			return c - '0';
		return c - 'A' + 10;
	}

	private int b(String colorValue) {
		return hex(colorValue, 5, 6);
	}

	private int r(String colorValue) {
		return hex(colorValue, 1, 2);
	}

	private int g(String colorValue) {
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
}
