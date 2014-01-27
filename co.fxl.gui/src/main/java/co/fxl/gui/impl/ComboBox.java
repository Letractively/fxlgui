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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IBordered;
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

	private class ComboBoxPopUp implements IUpdateListener<Boolean>,
			IClickListener, IKeyListener {

		private IPopUp popup;
		private IVerticalPanel lastLine;
		private String lastLineText;
		private IVerticalPanel nullLine;
		private Map<String, IVerticalPanel> lines = new HashMap<String, IVerticalPanel>();

		protected void clearLines() {
			lastLine = null;
			lastLineText = null;
			nullLine = null;
			lines.clear();
		}

		private void highlight(String text) {
			lastLineText = isNull(text) ? null : text;
			IVerticalPanel line = isNull(text) ? nullLine : lines.get(text);
			if (lastLine == line)
				return;
			if (lastLine != null) {
				lastLine.color().remove();
				lastLine = null;
			}
			lastLine = line;
			if (line != null)
				line.color().rgb(70, 184, 255);
		}

		@Override
		public void onClick() {
			if (popup != null)
				return;
			focus(true);
			int width = grid.width() - 2;
			popup = PopUp.showPopUp(false).autoHide(true)
					.offset(focus.offsetX(), focus.offsetY() + focus.height())
					.width(width);
			PopUp.autoHideOnResize(popup);
			popup.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						Display.instance().invokeLater(new Runnable() {
							@Override
							public void run() {
								popup = null;
								clearLines();
							}
						}, 250);
					}
				}
			});
			popup.border().remove().color().rgb(127, 157, 185);
			IVerticalPanel vertical = popup.container().panel().vertical();
			clearLines();
			for (final String text : texts) {
				IFocusPanel f = vertical.add().panel().focus();
				final IVerticalPanel line = f.add().panel().vertical()
						.height(22);
				if (center)
					line.align().center();
				line.spacing().left(4).right(4).top(2).bottom(2);
				ILabel l = line.add().label();
				ComboBox.this.text(l, text);
				f.addMouseOverListener(new IMouseOverListener() {

					@Override
					public void onMouseOver() {
						highlight(text);
					}

					@Override
					public void onMouseOut() {
					}
				});
				IClickListener clickListener = new IClickListener() {

					@Override
					public void onClick() {
						submit(text);
					}
				};
				line.addClickListener(clickListener);
				if (Env.is(Env.SWING))
					l.addClickListener(clickListener);
				if (isNull(text))
					nullLine = line;
				else
					lines.put(text, line);
			}
			highlight(ComboBox.this.text());
			popup.visible(true);
			if (popup.offsetY() + popup.height() > Display.instance().height()) {
				int height = Display.instance().height() - popup.offsetY() - 10;
				if (height > 0)
					popup.container().scrollPane().width(width).height(height)
							.viewPort().element(vertical);
			}

		}

		private void notifyEnter() {
			if (lastLine != null)
				submit(lastLineText);
		}

		private void submit(String text) {
			closePopUp();
			focus(true);
			text(text);
		}

		private boolean isNull(String text) {
			return text == null || text.equals("");
		}

		@Override
		public void onUpdate(Boolean value) {
			if (!value && popup != null && popup.visible())
				Display.instance().invokeLater(new Runnable() {
					@Override
					public void run() {
						closePopUp();
					}
				}, 250);
		}

		@Override
		public void onCharacterKey(char character) {
			if (character == ' ' && texts.size() > 0 && isNull(texts.get(0)))
				ComboBox.this.text(null);
			else {
				int index = texts.indexOf(text()) + 1;
				int num = texts.size();
				for (int i = 0; i < num; i++) {
					String text = texts.get(index++ % num);
					if (text != null && text.trim().length() > 0
							&& text.trim().toLowerCase().charAt(0) == character) {
						setText(text);
						return;
					}
				}
			}
		}

		@Override
		public void onControlKey(co.fxl.gui.api.IKeyRecipient.ControlKey key) {
			String text = ComboBox.this.text();
			if (popup != null && popup.visible() && lastLineText != null)
				text = lastLineText;
			int index = isNull(text) && isNull(texts.get(0)) ? 0 : texts
					.indexOf(text);
			switch (key) {
			case UP:
				if (index > 0)
					select(index - 1);
				break;
			case DOWN:
				if (index < texts.size() - 1)
					select(index + 1);
				break;
			default:
			}
		}

		private void select(int i) {
			String text = texts.get(i);
			text = isNull(text) ? null : text;
			setText(text);
		}

		private void setText(String text) {
			if (closePopUpOnChange)
				closePopUp();
			ComboBox.this.text(text);
		}

		private void closePopUp() {
			if (popup != null)
				popup.visible(false);
		}
	}

	public interface IColorAdapter {

		String color(String value);
	}

	public static final boolean ACTIVE = false;
	private static final boolean ALWAYS_USE_COLORED_COMBOBOX = false;
	private IGridPanel grid;
	private ILabel label;
	private IColorAdapter colorAdapter;
	private IImage button;
	private String text;
	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	private IFocusPanel focus;
	private List<String> texts = new LinkedList<String>();
	private final ComboBoxPopUp cbp = new ComboBoxPopUp();
	private boolean center;
	public boolean closePopUpOnChange;

	private ComboBox(IContainer container, IColorAdapter colorAdapter,
			boolean center, boolean closePopUpOnChange) {
		this.center = center;
		this.closePopUpOnChange = closePopUpOnChange;
		focus = container.panel().focus().outline(true);
		focus.border().style().rounded().width(3);
		addFocusListener(cbp);
		focus.addKeyListener((IKeyListener) cbp);
		focus.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				cbp.notifyEnter();
			}
		}).enter();
		grid = focus.add().panel().grid();
		grid.border().style().rounded().width(3);
		grid.column(0).expand();
		IGridCell cell = grid.cell(0, 0);
		if (center) {
			cell.align().center();
			cell.padding().left(13);
		}
		label = cell.valign().center().label();
		label.margin().left(3);
		button = grid.cell(1, 0).image().resource("more_chrome.png").width(7);
		button.margin().right(6).left(5);
		this.colorAdapter = colorAdapter;
		focus.addClickListener(cbp);
		button.addClickListener(cbp);
		label.addClickListener(cbp);
		grid.cell(0, 0).border().style().rounded().width(3).right(false);
		grid.cell(1, 0).border().style().rounded().width(3).left(false);
	}

	@Override
	protected IElement<?> basicElement() {
		return grid;
	}

	@Override
	protected IBordered borderElement() {
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
		cbp.highlight(text);
		for (IUpdateListener<String> l : listeners)
			l.onUpdate(text);
		return this;
	}

	private void text(ILabel l, final String text) {
		if (text == null || text.equals(""))
			l.text("");
		else
			l.html(HTMLText.styledText(text, colorAdapter == null ? null
					: colorAdapter.color(text)));
	}

	public static IComboBox create(IContainer c, IColorAdapter colorAdapter,
			boolean center) {
		return create(c, colorAdapter, center, false);
	}

	public static IComboBox create(IContainer c, IColorAdapter colorAdapter,
			boolean center, boolean closePopUpOnChange) {
		if (ACTIVE)
			return new ComboBox(c, colorAdapter, center, closePopUpOnChange);
		else
			return create(c, center);
	}

	public static IComboBox create(IContainer c, boolean center) {
		if (ALWAYS_USE_COLORED_COMBOBOX)
			return new ComboBox(c, null, center, false);
		else
			return c.comboBox();
	}

	public static IComboBox create(IContainer c) {
		return create(c, false);
	}
}
