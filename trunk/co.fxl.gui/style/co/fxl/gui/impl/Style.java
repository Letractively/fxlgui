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

public class Style {

	// public enum Window {
	// DIALOG, VIEWLIST, HEADER, CONTENT, FOOTER, TITLE, MAIN, SIDE;
	// }
	//
	// public enum List {
	// CHOICE, NUMBER, ENTRY;
	// }
	//
	// public enum Element {
	// HYPERLINK, SEPARATOR, INPUT, BUTTON, BORDER, BACKGROUND, LABEL, COMBOBOX;
	// }
	//
	// public enum Status {
	// ACTIVE, INACTIVE, HIGHLIGHT, UNHIGHLIGHT, ERROR;
	// }

	// private static boolean setUp = false;
	//
	// public static void setUp() {
	// if (setUp)
	// return;
	// setUp = true;
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().weight().italic();
	// }
	// }, Window.DIALOG, Status.ERROR);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().color().gray();
	// }
	// }, Element.SEPARATOR);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// if (label.clickable())
	// label.font().color().black();
	// else
	// label.font().color().gray();
	// }
	// }, Window.DIALOG, Element.BUTTON);
	// Styles.instance().register(new IStyle<IColored>() {
	// @Override
	// public void style(IColored label) {
	// label.color().rgb(249, 249, 249);
	// }
	// }, Element.INPUT, Element.BACKGROUND);
	// Styles.instance().register(new IStyle<IBordered>() {
	// @Override
	// public void style(IBordered bordered) {
	// bordered.border().color().rgb(211, 211, 211);
	// }
	// }, Element.INPUT, Element.BORDER);
	// Styles.instance().register(new IStyle<IComboBox>() {
	// @Override
	// public void style(IComboBox e) {
	// Styles.instance().style(e, Element.INPUT, Element.BACKGROUND);
	// Styles.instance().style(e, Element.INPUT, Element.BORDER);
	// }
	// }, Element.INPUT, Element.COMBOBOX);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().color().rgb(0, 87, 141);
	// }
	// }, Element.HYPERLINK, Status.ACTIVE);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().color().gray();
	// }
	// }, Element.HYPERLINK, Status.INACTIVE);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().pixel(12);
	// }
	// }, Window.HEADER, Element.BUTTON);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().weight().bold().pixel(12).color().white();
	// }
	// }, Window.HEADER, Window.TITLE, Window.SIDE);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().weight().bold();
	// }
	// }, Window.CONTENT, List.CHOICE);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().pixel(13).color().gray();
	// }
	// }, Window.CONTENT, List.NUMBER);
	// Styles.instance().register(new IStyle<IElement<?>>() {
	// @Override
	// public void style(IElement<?> e) {
	// if (e instanceof IPanel<?>) {
	// IPanel<?> panel = (IPanel<?>) e;
	// panel.color().rgb(0xD0, 0xE4, 0xF6);
	// } else {
	// ILabel label = (ILabel) e;
	// label.font().pixel(13);
	// label.font().color().mix().black().gray();
	// }
	// }
	// }, Window.VIEWLIST, List.ENTRY, Status.ACTIVE);
	// Styles.instance().register(new IStyle<IElement<?>>() {
	// @Override
	// public void style(IElement<?> e) {
	// if (e instanceof IPanel<?>) {
	// IPanel<?> panel = (IPanel<?>) e;
	// panel.color().remove();
	// } else {
	// ILabel label = (ILabel) e;
	// label.font().pixel(13);
	// }
	// }
	// }, Window.VIEWLIST, List.ENTRY, Status.INACTIVE);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().underline(false);
	// }
	// }, Element.HYPERLINK, Status.UNHIGHLIGHT);
	// Styles.instance().register(new IStyle<ILabel>() {
	// @Override
	// public void style(ILabel label) {
	// label.font().underline(true);
	// }
	// }, Element.HYPERLINK, Status.HIGHLIGHT);
	// }
}
