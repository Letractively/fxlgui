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

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.IStyle;
import co.fxl.gui.style.Styles;

public class Style {

	private static boolean setUp = false;

	public static void setUp() {
		if (setUp)
			return;
		setUp = true;
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().weight().italic();
			}
		}, ID.DIALOG, ID.ERROR, ID.HEADER, ID.STACKTRACE);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().color().gray();
			}
		}, ID.SEPARATOR);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				if (label.clickable())
					label.font().color().black();
				else
					label.font().color().gray();
			}
		}, ID.DIALOG, ID.BUTTON);
		Styles.instance().register(new IStyle<IColored>() {
			@Override
			public void style(IColored label) {
				label.color().rgb(249, 249, 249);
			}
		}, ID.INPUT, ID.FIELD, ID.BACKGROUND);
		Styles.instance().register(new IStyle<IBordered>() {
			@Override
			public void style(IBordered bordered) {
				bordered.border().color().rgb(211, 211, 211);
			}
		}, ID.INPUT, ID.FIELD, ID.BORDER);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().color().rgb(0, 87, 141);
			}
		}, ID.HYPERLINK, ID.ACTIVE);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().color().gray();
			}
		}, ID.HYPERLINK, ID.INACTIVE);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().pixel(12);
			}
		}, ID.WINDOW, ID.BUTTON);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().weight().bold().pixel(12).color().white();
			}
		}, ID.WINDOW, ID.HEADER, ID.TITLE, ID.SMALL);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().weight().bold();
			}
		}, ID.WINDOW, ID.NAVIGATION, ID.CHOICE, ID.TITLE);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().pixel(13).color().gray();
			}
		}, ID.WINDOW, ID.NAVIGATION, ID.NUMBER);
		Styles.instance().register(new IStyle<IElement<?>>() {
			@Override
			public void style(IElement<?> e) {
				if (e instanceof IPanel<?>) {
					IPanel<?> panel = (IPanel<?>) e;
					panel.color().rgb(0xD0, 0xE4, 0xF6);
				} else {
					ILabel label = (ILabel) e;
					label.font().pixel(13);
					label.font().color().mix().black().gray();
				}
			}
		}, ID.WINDOW, ID.VIEW, ID.ENTRY, ID.ACTIVE);
		Styles.instance().register(new IStyle<IElement<?>>() {
			@Override
			public void style(IElement<?> e) {
				if (e instanceof IPanel<?>) {
					IPanel<?> panel = (IPanel<?>) e;
					panel.color().remove();
				} else {
					ILabel label = (ILabel) e;
					label.font().pixel(13);
				}
			}
		}, ID.WINDOW, ID.VIEW, ID.ENTRY, ID.INACTIVE);

		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().underline(false);
			}
		}, ID.HYPERLINK, ID.UNHIGHLIGHT);
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				label.font().underline(true);
			}
		}, ID.HYPERLINK, ID.HIGHLIGHT);
	}

	public enum ID {
		HYPERLINK, WINDOW, NAVIGATION, CHOICE, NUMBER, SEPARATOR, VIEW, ENTRY, ACTIVE, INACTIVE, ERROR, DIALOG, HEADER, STACKTRACE, INPUT, FIELD, BORDER, BACKGROUND, HIGHLIGHT, UNHIGHLIGHT, BUTTON, TITLE, SMALL;
	}
}
