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
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.IHTMLArea.Formatting;

public class RichTextToolbarImpl {

	private class PushButton extends ToolbarButton {

		private PushButton(Formatting f) {
			super(f);
		}

		@Override
		void handleClick() {
			htmlArea.apply(f);
		}

	}

	private abstract class ToolbarElement {

		void updateStatus() {
		}

	}

	private abstract class ToolbarButton extends ToolbarElement {

		IImage image;
		Formatting f;

		private ToolbarButton() {
		}

		private ToolbarButton(final Formatting f) {
			this.f = f;
			String resource = imageName(f);
			setImage(panel, resource);
		}

		void setImage(IToolbar panel, String resource) {
			image = panel.add().image().resource(resource);
			image.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					handleClick();
					updateImage();
					htmlArea.notifyChange();
				}
			});
			image.border().color().white();
			image.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					image.border().color().rgb(219, 180, 104);
				}

				@Override
				public void onMouseOut() {
					updateImage();
				}
			});
		}

		String imageName(final Formatting f) {
			return f.name().toLowerCase().replaceAll("_", "") + ".gif";
		}

		abstract void handleClick();

		void updateImage() {
			image.border().color().white();
		}
	}

	private class ToggleButton extends ToolbarButton {

		private boolean active;

		private ToggleButton(final Formatting f) {
			super(f);
		}

		@Override
		String imageName(final Formatting f) {
			return f.name().substring(IHTMLArea.TOGGLE_PREFIX.length())
					.toLowerCase()
					+ ".gif";
		}

		@Override
		void handleClick() {
			active = !active;
			htmlArea.toggle(f);
		}

		@Override
		void updateStatus() {
			active = htmlArea.is(f);
			updateImage();
		}

		@Override
		void updateImage() {
			if (active)
				image.border().color().gray();
			else
				super.updateImage();
		}
	}

	protected static final int SPACING = 200;

	private IToolbar panel;
	private List<ToolbarElement> buttons = new LinkedList<ToolbarElement>();
	private IHTMLArea htmlArea;

	private ToolbarButton zoomButton;

	private IClickListener closeListener;

	private IGridPanel grid;

	public RichTextToolbarImpl(IContainer c, final IHTMLArea htmlArea) {
		grid = c.panel().grid();
		panel = new ToolbarImpl(grid.cell(0, 0)).height(28).spacing(2);
		grid.color().white();
		grid.border().style().noBottom().color().gray(211);
		this.htmlArea = htmlArea;
		for (Formatting f : IHTMLArea.Formatting.values()) {
			if (htmlArea.supports(f)) {
				if (f.name().startsWith(IHTMLArea.TOGGLE_PREFIX)) {
					ToggleButton b = new ToggleButton(f);
					buttons.add(b);
				} else {
					PushButton b = new PushButton(f);
					buttons.add(b);
				}
			}
		}
		grid.column(0).expand();
		IToolbar panelRight = new ToolbarImpl(grid.cell(1, 0).align().end())
				.height(28).spacing(2);
		zoomButton = new ToolbarButton() {
			@Override
			void handleClick() {
				if (closeListener != null) {
					closeListener.onClick();
					return;
				}
				final IPopUp p = Display.instance().showPopUp();
				p.border().remove();
				p.border().style().shadow();
				final IHTMLArea ha = p.container().widget(IHTMLArea.class);
				copy(htmlArea, ha);
				Heights.INSTANCE.decorate(ha);
				ha.closeListener(new IClickListener() {
					@Override
					public void onClick() {
						p.visible(false);
						copy(ha, htmlArea);
					}
				});
				adjust(p, ha);
				p.visible(true);
				Display.instance().addResizeListener(new IResizeListener() {
					@Override
					public void onResize(int width, int height) {
						adjust(p, ha);
					}
				}).linkLifecycle(p);
			}

			private void copy(final IHTMLArea htmlArea, final IHTMLArea ha) {
				ha.html(htmlArea.html());
				// ha.cursorPosition(htmlArea.cursorPosition());
			}

			private void adjust(IPopUp p, IHTMLArea ha) {
				p.offset(SPACING / 2, SPACING / 2).size(
						Display.instance().width() - SPACING,
						Display.instance().height() - SPACING);
				ha.height(Display.instance().height() - SPACING);
			}
		};
		zoomButton.setImage(panelRight, "zoom_in.png");
	}

	public void updateStatus() {
		for (ToolbarElement e : buttons)
			e.updateStatus();
	}

	public void closeListener(IClickListener l) {
		zoomButton.image.resource("zoom_out.png");
		closeListener = l;
	}

	public int height() {
		return grid.height();
	}

}
