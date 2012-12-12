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
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.ITextInputElement;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.impl.Heights;

public class TooltipTextInput implements IUpdateable<String> {

	private ITextInputElement<?> ti;
	private String tooltip;

	public TooltipTextInput(final ITextInputElement<?> ti, final String tooltip) {
		this.ti = ti;
		this.tooltip = tooltip;
		clear();
		ti.addFocusListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (tooltip == null)
					return;
				if (value) {
					if (isTooltipActive()) {
						ti.text("");
					}
				} else {
					if (ti.text().equals("")) {
						setTooltip();
					}
				}
				updateColor();
			}
		});
		Heights.INSTANCE.decorate(ti);
	}

	protected boolean isTooltipActive() {
		return ti.text().equals(tooltip);
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			final IUpdateListener<String> listener) {
		ti.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (!isTooltipActive())
					listener.onUpdate(text());
				else
					listener.onUpdate("");
			}
		});
		return this;
	}

	public String text() {
		if (isTooltipActive())
			return "";
		else
			return ti.text();
	}

	public TooltipTextInput clear() {
		if (TooltipTextInput.this.tooltip == null) {
			ti.text("");
			return this;
		}
		setTooltip();
		return this;
	}

	private void setTooltip() {
		ti.text(tooltip);
		updateColor();
	}

	void updateColor() {
		IColor color = ti.font().color();
		if (isTooltipActive()) {
			color.gray();
		} else {
			color.black();
		}
	}

	public IFont font() {
		return ti.font();
	}

	public TooltipTextInput focus(boolean b) {
		ti.focus(b);
		return this;
	}

}
