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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IFocusable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;

public class ComboBox extends ComboBoxAdp implements RuntimeConstants {

	public interface IColorAdapter {

		String color(String value);
	}

	private static final boolean ACTIVE = true;
	private IGridPanel grid;
	private ILabel label;
	private IColorAdapter colorAdapter;
	private IImage button;
	private String text;
	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	private IFocusPanel focus;
	private List<String> texts = new LinkedList<String>();

	private ComboBox(IContainer container, IColorAdapter colorAdapter,
			final boolean center) {
		focus = container.panel().focus();
		addFocusListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (value)
					border().color().rgb(70, 184, 255);
				else
					border().color().gray(211);
			}
		});
		grid = focus.add().panel().grid();
		grid.column(0).expand();
		IGridCell cell = grid.cell(0, 0);
		if (center) {
			cell.align().center();
			cell.padding().left(14);
		}
		label = cell.valign().center().label();
		label.margin().left(3);
		button = grid.cell(1, 0).image().resource("more_chrome.png").width(7);
		button.margin().right(6).left(5);
		this.colorAdapter = colorAdapter;
		IClickListener cl = new IClickListener() {
			@Override
			public void onClick() {
				focus(true);
				final IPopUp popup = Display
						.instance()
						.showPopUp()
						.autoHide(true)
						.offset(focus.offsetX(),
								focus.offsetY() + focus.height())
						.width(grid.width() - 2);
				popup.border().remove().color().rgb(127, 157, 185);
				IVerticalPanel vertical = popup.container().panel().vertical();
				for (final String text : texts) {
					IFocusPanel f = vertical.add().panel().focus();
					final IVerticalPanel line = f.add().panel().vertical()
							.height(22);
					if (center)
						line.align().center();
					line.spacing().left(4).right(4).top(2).bottom(2);
					ILabel l = line.add().label();
					text(l, text);
					f.addMouseOverListener(new IMouseOverListener() {

						@Override
						public void onMouseOver() {
							line.color().rgb(70, 184, 255);
						}

						@Override
						public void onMouseOut() {
							line.color().remove();
						}
					});
					line.addClickListener(new IClickListener() {

						@Override
						public void onClick() {
							popup.visible(false);
							text(text);
						}
					});
				}
				popup.visible(true);
				if (popup.offsetY() + popup.height() > Display.instance()
						.height()) {
					int height = Display.instance().height() - popup.offsetY();
					popup.container().scrollPane().height(height).viewPort()
							.element(vertical);
				}

			}
		};
		focus.addClickListener(cl);
		button.addClickListener(cl);
		label.addClickListener(cl);
	}

	@Override
	protected IElement<?> basicElement() {
		return grid;
	}

	@Override
	public IColor color() {
		return new ColorTemplate() {

			@Override
			public IColor remove() {
				grid.color().remove();
				grid.cell(0, 0).color().remove();
				grid.cell(1, 0).color().remove();
				return this;
			}

			@Override
			protected IColor setRGB(int r, int g, int b) {
				grid.color().rgb(r, g, b);
				grid.cell(0, 0).color().rgb(r, g, b);
				grid.cell(1, 0).color().rgb(r, g, b);
				return this;
			}
		};
	}

	@Override
	protected IFocusable<?> focusElement() {
		return focus;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			IUpdateListener<String> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IComboBox editable(boolean editable) {
		focus.clickable(editable);
		button.clickable(editable);
		label.clickable(editable);
		return this;
	}

	@Override
	public boolean editable() {
		return button.clickable();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IKey<IComboBox> addKeyListener(IClickListener listener) {
		return (IKey) focus.addKeyListener(listener);
	}

	@Override
	public IComboBox addText(String... texts) {
		this.texts.addAll(Arrays.asList(texts));
		return this;
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public IComboBox text(String text) {
		this.text = text;
		text(label, text);
		for (IUpdateListener<String> l : listeners)
			l.onUpdate(text);
		return this;
	}

	private void text(ILabel l, final String text) {
		l.html(text == null ? null : HTMLText.styledText(text,
				colorAdapter == null ? null : colorAdapter.color(text)));
	}

	public static IComboBox create(IContainer c, IColorAdapter colorAdapter,
			boolean center) {
		if (ACTIVE)
			return new ComboBox(c, colorAdapter, center);
		else
			return create(c);
	}

	public static IComboBox create(IContainer c) {
		return c.comboBox();
	}
}
