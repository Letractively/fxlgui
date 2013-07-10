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
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IFontElement.IFont.IWeight;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.impl.WidgetTitle;

class NinetyNineDesignsStyle extends StyleTemplate {

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
		private IHorizontalPanel comboBoxPanel = Display.instance()
				.newContainer().panel().horizontal();

		private class View extends ViewTemplate implements IView {

			private IPanel<?> panel;
			private IImage image;
			private ILabel label;
			private ViewType type;
			private IGridCell cell;

			private View(String text, ViewType type, boolean isLast) {
				super(comboBoxPanel.add().panel().horizontal(),
						isDiscardChangesDialog);
				this.type = type;
				int column = views.size();
				cell = grid.cell(column, 0).align().center().valign().center();
				panel = cell.panel().vertical().align().center()
						.height(VIEW_SELECTION_HEIGHT);
				IBorder border = cell.border();
				border.color().gray(218);
				if (!isLast)
					border.style().top().style().bottom().style().left();
				setBorderRounding(isLast, border);
				border.width(1);
				setBorderRounding(isLast, panel.border());
				IHorizontalPanel hp = panel.add().panel().horizontal();
				hp.margin().top((VIEW_SELECTION_HEIGHT - 16) / 2);
				image = hp.add().image();
				label = hp.addSpace(4).add().label().text(text);
				blue(label);
				grid.column(column).expand();
				// decorate(active);
				panel.addClickListener(this);
				image.addClickListener(this);
				label.addClickListener(this);
				addEntry(text, title(), image.resource());
			}

			private void setBorderRounding(boolean isLast, IBorder border) {
				if (views.isEmpty())
					border.style().rounded().width(6).right(false);
				else if (isLast)
					border.style().rounded().width(6).left(false);
			}

			@Override
			int space() {
				return 0;
			}

			@Override
			void style(final IComboBox cb) {
				cb.width(150).border().color().white();
				cb.color().white();
				cb.addMouseOverListener(new IMouseOverListener() {
					@Override
					public void onMouseOver() {
						cb.border().color().lightgray();
					}

					@Override
					public void onMouseOut() {
						cb.border().color().white();
					}
				});
			}

			@Override
			public IView clickable(boolean clickable) {
				boolean active = !clickable;
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
				panel0().visible(!clickable);
				return super.clickable(clickable);
			}

			@Override
			public boolean clickable() {
				return panel.clickable();
			}

			@Override
			public void showActive() {
				for (View view : views) {
					view.clickable(view != View.this);
				}
			}
		}

		private IGridPanel grid;
		private List<View> views = new LinkedList<View>();
		private boolean isDiscardChangesDialog;

		public ViewSelection(IContainer c, boolean isDiscardChangesDialog) {
			this.isDiscardChangesDialog = isDiscardChangesDialog;
			grid = c.panel().grid().height(VIEW_SELECTION_HEIGHT);
			grid.margin().top(ADD_DISTANCE_MDT_FIRST_ROW);
		}

		@Override
		public IView addView(String label, ViewType type, boolean isLast) {
			View view = new View(label, type, isLast);
			views.add(view);
			return view;
		}

		@Override
		public String title() {
			return VIEWS;
		}

		@Override
		public WidgetTitle widgetTitle() {
			return null;
		}

		@Override
		public IHorizontalPanel comboBoxPanel() {
			return comboBoxPanel;
		}

		@Override
		public void visible(boolean b) {
			grid.visible(b);
		}
	}

	public static final String NAME = "Standard";
	private static final int ADD_DISTANCE_MDT_FIRST_ROW = 3;

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
	public IViewSelection createSelection(IContainer c,
			boolean isDiscardChangesDialog) {
		return new ViewSelection(c, isDiscardChangesDialog);
	}

	@Override
	public void background(IPanel<?> panel) {
	}

	@Override
	public void display(IDisplay display) {
		display.color().white();
	}

	@Override
	public IWindow window() {
		return new IWindow() {

			@Override
			public void title(ILabel label, boolean sideWidget) {
				blue(label);
				label.font().weight().bold();
				if (sideWidget)
					label.font().pixel(14);
			}

			@Override
			public boolean useUpperCase(boolean sideWidget) {
				return false;
			}

			@Override
			public void moreButton(CommandLink more) {
				more.label().font().color().black();
				more.image("more_small_black.png");
			}

			@Override
			public void newButton(CommandLink commandLink, int index) {
				int inc = index * 14;
				commandLink.border().color().rgb(14 + inc, 151 + inc, 35 + inc);
				commandLink.background().rgb(40 + inc, 194 + inc, 64 + inc)
						.gradient().vertical()
						.rgb(25 + inc, 173 + inc, 48 + inc);
				white(commandLink);
			}

			private void white(CommandLink commandLink) {
				commandLink.label().font().color().white();
			}

			@Override
			public void showButton(CommandLink commandLink) {
				commandLink.border().color().rgb(147, 131, 24);
				commandLink.background().rgb(217, 189, 54).gradient()
						.vertical().rgb(179, 162, 29);
				white(commandLink);
			}

			@Override
			public void button(CommandLink commandLink) {
				commandLink.border().color().gray(195);
				commandLink.background().gray(254).gradient().vertical()
						.gray(222);
				fontSize(commandLink);
				commandLink.panel().padding().left(4);
				// commandLink.iPanel().padding().left(4).right(4);
			}

			private IFont fontSize(CommandLink commandLink) {
				return commandLink.label().font().pixel(13);
			}

			@Override
			public void header(IGridPanel headerPanel, boolean sideWidget) {
				if (sideWidget)
					background(headerPanel);
				else
					headerPanel.color().remove();
				headerPanel.padding().bottom(
						sideWidget ? 0 : 7 + ADD_DISTANCE_MDT_FIRST_ROW);
				if (sideWidget)
					headerPanel.border().remove();
			}

			@Override
			public void background(IGridPanel panel, boolean addBorder,
					boolean plainContent, boolean sideWidget) {
				if (!plainContent && sideWidget) {
					panel.margin().top(4);
					if (addBorder) {
						IBorder border = panel.border();
						border.style().rounded().width(6);
						border.width(1).color().rgb(172, 197, 213);
					}
					panel.padding(3);
					background(panel);
				}
			}

			private void background(IGridPanel panel) {
				panel.color().gray(248);
			}

			@Override
			public void footer(IPanel<?> vertical, boolean sideWidget) {
			}
		};
	}

	@Override
	public IMDT mdt() {
		return new IMDT() {
			@Override
			public boolean showQuickSearchOnTop() {
				return true;
			}

			@Override
			public void quickSearch(ISuggestField sf) {
				sf.height(28);
				sf.margin().top(4).left(3);
				sf.padding().left(27).right(3);
				IBorder border = sf.border();
				border.style().rounded();
				border.width(1).color().lightgray();
				sf.addStyle("quickSearchShadow");
			}
		};
	}

	@Override
	public IPopUpWindow popUp() {
		return new IPopUpWindow() {

			@Override
			public void background(IColored c) {
				c.color().white();
			}
		};
	}
}
