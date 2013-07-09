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
package co.fxl.gui.style.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont.IWeight;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.style.api.IStyle;

class NinetyNineDesignsStyle implements IStyle {

	private IColor blue(IFontElement element) {
		return blue(element.font());
	}

	private IColor blue(IColored colored) {
		return blue(colored.color());
	}

	private IColor blue(IColor color) {
		return color.rgb(29, 59, 89);
	}

	private class ViewSelection implements IViewSelection {

		private static final int VIEW_SELECTION_HEIGHT = 40;

		private class View extends LazyClickListener {

			private IPanel<?> panel;
			private IImage image;
			private ILabel label;
			private ViewType type;
			private IClickListener clickListener;

			private View(String text, ViewType type, IClickListener c,
					boolean active, boolean isLast) {
				this.type = type;
				clickListener = c;
				int column = views.size();
				IGridCell cell = grid.cell(column, 0).align().center().valign()
						.center();
				panel = cell.panel().vertical().align().center()
						.height(VIEW_SELECTION_HEIGHT);
				IBorder border = cell.border();
				border.color().gray(218);
				if (!isLast)
					border.style().top().style().bottom().style().left();
				if (views.isEmpty())
					border.style().rounded().width(6).right(false);
				else if (isLast)
					border.style().rounded().width(6).left(false);
				border.width(1);
				IHorizontalPanel hp = panel.add().panel().horizontal();
				hp.margin().top((VIEW_SELECTION_HEIGHT - 16) / 2);
				image = hp.add().image();
				label = hp.addSpace(4).add().label().text(text);
				blue(label);
				grid.column(column).expand();
				decorate(active);
				panel.addClickListener(this);
				image.addClickListener(this);
				label.addClickListener(this);
			}

			@Override
			protected void onAllowedClick() {
				for (View view : views) {
					view.decorate(view == this);
				}
				clickListener.onClick();
			}

			private void decorate(boolean active) {
				panel.color().remove().gray(active ? 231 : 248).gradient()
						.vertical().gray(active ? 248 : 231);
				IWeight weight = label.font().weight();
				if (active) {
					weight.bold();
				} else {
					weight.plain();
				}
				String res = "view_" + type.name().toLowerCase() + "_"
						+ (active ? "active" : "inactive") + ".png";
				panel.clickable(!active);
				label.clickable(!active);
				image.clickable(!active);
				image.resource(res).opacity(1.0);
			}
		}

		private IGridPanel grid;
		private List<View> views = new LinkedList<View>();

		public ViewSelection(IContainer c) {
			grid = c.panel().grid().height(VIEW_SELECTION_HEIGHT);
			grid.margin().bottom(10);
		}

		@Override
		public IViewSelection addView(String label, ViewType type,
				IClickListener c, boolean active, boolean isLast) {
			View view = new View(label, type, c, active, isLast);
			views.add(view);
			return this;
		}

		@Override
		public boolean containsQuerySelection() {
			return false;
		}
	}

	public static final String NAME = "Standard";

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public boolean useMoreButton() {
				return true;
			}

			@Override
			public void addSeparator(ILinearPanel<?> panel) {
				panel.addSpace(10);

			}

			@Override
			public void logout(ILinearPanel<?> panel, String userName,
					final Decorator[] decorators, final IClickListener listener) {
				IClickable<?> prefix = panel.add().image()
						.resource("user_white.png");
				ILabel loggedInHead = panel.add().label().text(userName);
				loggedInHead.font().pixel(fontSize()).weight().bold().color()
						.white();
				new HyperlinkMouseOverListener(loggedInHead);
				final IImage image = panel.add().image()
						.resource("more_white_10x16.png");
				IClickListener clickListener = new IClickListener() {

					@Override
					public void onClick() {
						final IPopUp popUp = PopUp.showPopUp(true).width(140)
								.autoHide(true);
						popUp.offset(image.offsetX() - 140 + 12,
								image.offsetY() + 25);
						IVerticalPanel p = popUp.container().panel().vertical()
								.spacing(12);
						p.margin().top(2);
						boolean addLine = false;
						for (Decorator d : decorators) {
							if (d.isVisible()) {
								d.decorate(p.add().panel().horizontal());
								addLine = true;
							}
						}
						if (addLine)
							p.add().line();
						ImageButton ib = new ImageButton(p.add());
						ib.imageResource("logout.png").text("Logout")
								.addClickListener(listener);
						ib.label().hyperlink().font().weight().bold();
						popUp.visible(true);
					}
				};
				prefix.addClickListener(clickListener);
				loggedInHead.addClickListener(clickListener);
				image.addClickListener(clickListener);
			}
		};
	}

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {

			@Override
			public ILinearPanel<?>[] create(IContainer c) {
				IGridPanel grid = c.panel().grid().spacing(1);
				blue(grid).gradient().vertical().rgb(57, 84, 110);
				grid.border().style().bottom();
				grid.column(0).expand();
				ILinearPanel<?> v = grid.cell(0, 0).valign().center().panel()
						.vertical();
				ILinearPanel<?> end = grid.cell(1, 0).valign().center().align()
						.end().panel().horizontal().align().end();
				end.padding().top(1);
				return new ILinearPanel<?>[] { v, end };
			}

			@Override
			public int marginLeft() {
				return -4;
			}

			@Override
			public void itemPanel(IHorizontalPanel p) {
			}
		};
	}

	@Override
	public IUserPanel userPanel() {
		return new IUserPanel() {

			@Override
			public void background(IHorizontalPanel panel) {
				panel.margin().right(10);
			}

			@Override
			public IClickable<?> profileButton(IPanel<?> panel) {
				ImageButton ib = new ImageButton(panel.add());
				ib.label().hyperlink();
				return ib.imageResource("user.png").text("My Profile");
			}

			@Override
			public IClickable<?> enterAdminButton(IPanel<?> panel) {
				ImageButton text = new ImageButton(panel.add());
				text.imageResource("settings.png").text("Administration");
				text.label().font().pixel(fontSize()).weight().bold().color()
						.white();
				new HyperlinkMouseOverListener(text.label());
				return text;
			}

			@Override
			public IClickable<?> exitAdminButton(IPanel<?> panel) {
				panel.add().image().resource("settings.png");
				ILabel l = panel.add().label().text("Administration");
				l.font().weight().bold();
				l.font().pixel(fontSize()).color().lightgray();
				ILabel text = panel.add().label().text("Back to Application");
				text.font().pixel(fontSize()).weight().bold().color().white();
				new HyperlinkMouseOverListener(text);
				return text;
			}

			@Override
			public IClickable<?> traceButton(IContainer c) {
				ImageButton ib = new ImageButton(c).imageResource("trace.png");
				ib.text("Show Trace");
				ib.label().hyperlink();
				return ib;
			}
		};
	}

	@Override
	public int fontSize() {
		return 12;
	}

	@Override
	public String fontFamily() {
		return "'Open Sans', Arial, Helvetica, sans-serif";
	}

	@Override
	public INavigation navigation() {
		return new INavigation() {

			@Override
			public void init(IHorizontalPanel buttonPanel, ILabel button) {
			}

			@Override
			public void moreItem(ILabel label) {
				color(label.font());
			}

			private void color(IColored colored) {
				colored.color().rgb(28, 59, 89);
			}

			@Override
			public boolean isActiveMoreDark() {
				return true;
			}

			@Override
			public void active(IHorizontalPanel buttonPanel, ILabel button) {
				color(buttonPanel);
				button.font().color().white();
				buttonPanel.border().remove().width(0).style().rounded()
						.width(6);
			}

			@Override
			public void inactive(IHorizontalPanel buttonPanel, ILabel button) {
				buttonPanel.color().remove();
				moreItem(button);
				buttonPanel.border().remove().width(0).style().rounded()
						.width(6);
			}

			@Override
			public void activeMore(IHorizontalPanel buttonPanel, ILabel button,
					IImage refresh) {
				color(buttonPanel);
				button.font().color().white();
				buttonPanel.border().style().rounded().width(6).bottom(false);
				refresh.resource("more_white.png");
			}

			@Override
			public String activeRefreshImage() {
				return "loading_white.gif";
			}

			@Override
			public String inactiveRefreshImage() {
				return "loading_black.gif";
			}

			@Override
			public void background(IColored colored) {

				// TODO ...

			}

			@Override
			public boolean hasSegmentedBorder() {
				return false;
			}

			@Override
			public void group(ILabel header) {
				header.font().color().gray();
			}

			@Override
			public void backgroundRegisters(IPanel<?> panel) {
				panel.margin().top(20).bottom(20);
			}

			@Override
			public void backgroundCards(IPanel<?> history) {
				IBorder b = history.border();
				b.style().top();
				b.color().lightgray();
			}

			@Override
			public int offsetX() {
				return 263;
			}
		};
	}

	@Override
	public ILogoPanel logoPanel() {
		return new ILogoPanel() {

			@Override
			public String resource() {
				return "xoricon_requirements.png";
			}

			@Override
			public void background(IPanel<?> panel) {
				panel.color().rgb(253, 254, 254).gradient()
						.fallback(242, 246, 249).vertical().rgb(247, 250, 251);
			}

			@Override
			public int marginLeft() {
				return 6;
			}
		};
	}

	@Override
	public IViewSelection createSelection(IContainer c) {
		return new ViewSelection(c);
	}

	@Override
	public void background(IPanel<?> panel) {
	}

	@Override
	public void display(IDisplay display) {
		display.color().white();
	}

}