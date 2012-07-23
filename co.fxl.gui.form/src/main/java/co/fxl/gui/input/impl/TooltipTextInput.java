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
package co.fxl.gui.input.impl;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.ITextInput;
import co.fxl.gui.api.IUpdateable;

public class TooltipTextInput implements IUpdateable<String> {

	private ITextInput<?> ti;
	private String tooltip;
	private boolean isTooltipActive;

	public TooltipTextInput(final ITextInput<?> ti, String tooltip) {
		this.ti = ti;
		this.tooltip = tooltip;
		clear();
		ti.addFocusListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (value) {
					if (isTooltipActive) {
						ti.text("");
						setTooltipActive(false);
					}
				} else {
					if (ti.text().equals("")) {
						clear();
					} else
						setTooltipActive(false);
				}
			}
		});
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			final IUpdateListener<String> listener) {
		ti.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (!isTooltipActive)
					listener.onUpdate(value);
			}
		});
		return this;
	}

	public TooltipTextInput clear() {
		setTooltipActive(true);
		ti.text(tooltip);
		return this;
	}

	void setTooltipActive(Boolean value) {
		isTooltipActive = value;
		IColor color = ti.font().color();
		if (isTooltipActive) {
			color.gray();
		} else
			color.black();
	}

}
