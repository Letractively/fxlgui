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
package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IUpdateable;

public class ToggleImageButton implements IUpdateable<Boolean> {

	private CommandLink c;
	private String img;
	private String text;
	private String tImg;
	private String tText;
	private boolean toggled = false;

	public ToggleImageButton(CommandLink c, String img, String text) {
		this.c = c;
		this.img = img;
		this.text = text;
		c.clickable(true);
	}

	public ToggleImageButton toggleImageText(String img, String text) {
		this.tImg = img;
		this.tText = text;
		return this;
	}

	@Override
	public IUpdateable<Boolean> addUpdateListener(
			final co.fxl.gui.api.IUpdateable.IUpdateListener<Boolean> listener) {
		c.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				toggled = !toggled;
				update();
				listener.onUpdate(toggled);
			}
		});
		return this;
	}

	public ToggleImageButton toggled(boolean editable) {
		toggled = editable;
		update();
		return this;
	}

	void update() {
		c.text(!toggled ? text : tText);
		c.image(!toggled ? img : tImg);
	}
}