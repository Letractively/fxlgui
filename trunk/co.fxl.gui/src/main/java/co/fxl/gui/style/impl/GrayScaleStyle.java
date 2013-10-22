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

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.HyperlinkDecorator;
import co.fxl.gui.impl.Icons;
import co.fxl.gui.impl.NavigationView;
import co.fxl.gui.impl.StylishButton;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.impl.WidgetTitle;

class GrayScaleStyle extends StyleTemplate {

	private final class FilterPanel extends Heights implements IFilterPanel {

		FilterPanel() {
			super(0);
		}

		@Override
		public String acceptImage(boolean isMiniFilter) {
			return Icons.ACCEPT;
		}

		@Override
		public String acceptTitle() {
			return "Update";
		}

		@Override
		public String clearTitle() {
			return "Clear";
		}

		@Override
		public boolean containsRefreshButton() {
			return false;
		}

		@Override
		public String title(boolean isMini) {
			return "SEARCH FILTER";
		}

		@Override
		public void removeErrorColor(ITextElement<?> textField) {
			((IColored) textField).color().rgb(249, 249, 249);
		}
	}

	private class ViewSelection implements IViewSelection {

		public boolean noDiscardChangesDialog;

		public class View extends ViewTemplate {

			private IImage image;
			private ILabel label;
			private IHorizontalPanel panel;
			private List<ILabel> additionalLabels = new LinkedList<ILabel>();

			private View(IHorizontalPanel panel0, IHorizontalPanel panel,
					IImage image, ILabel textLabel) {
				super(panel0, noDiscardChangesDialog);
				this.panel = panel;
				this.image = image;
				this.label = textLabel;
				label.addClickListener(this);
				if (image != null)
					image.addClickListener(this);
				panel.addClickListener(this);
			}

			private View text(String text) {
				label.text(text);
				addEntry(text, title(), image.resource());
				return this;
			}

			@Override
			public IView clickable(boolean clickable) {
				if (image != null)
					image.clickable(clickable);
				label.clickable(clickable);
				styleMDTView(label);
				for (ILabel l : additionalLabels)
					l.clickable(clickable);
				panel.clickable(clickable);
				if (cb != null) {
					cb.visible(comboBoxVisible && !clickable);
				}
				if (cancelP != null) {
					cancelP.visible(comboBoxVisible
							&& !clickable
							&& !cb.text().equals(
									StyleTemplate.PLEASE_CHOOSE_FILTER_STRING));
				}
				return super.clickable(clickable);
			}

			private void styleMDTView(ILabel label) {
				// Styles.instance().style(label, Style.MDT.VIEW);
				if (!label.clickable()) {
					label.font().weight().bold().color().black();
				} else {
					label.font().weight().plain();
				}
			}

			@Override
			public boolean clickable() {
				return label.clickable();
			}

			@Override
			public void showActive() {
				for (View l : links)
					l.clickable(l != View.this);
			}
		}

		private WidgetTitle widgetTitle;
		private IVerticalPanel panel;
		private List<View> links = new LinkedList<View>();

		private ViewSelection(IContainer c, boolean noDiscardChanges) {
			widgetTitle = new WidgetTitle(c.panel(), true).sideWidget(true);
			widgetTitle.addToContextMenu(true);
			widgetTitle.spacing(2);
			widgetTitle.addTitle(VIEWS);
			panel = widgetTitle.content().panel().vertical().spacing(6);
			noDiscardChangesDialog = noDiscardChanges;
		}

		private IImage addImage(IHorizontalPanel panel, String imageResource) {
			IImage image = null;
			image = panel.add().image().resource(imageResource);
			panel.addSpace(4);
			return image;
		}

		private ILabel addTextLabel(IHorizontalPanel panel) {
			return panel.add().label().hyperlink();
		}

		@Override
		public IView addView(String label, ViewType viewType, boolean isLast) {
			IVerticalPanel p = panel.add().panel().vertical().height(24);
			IHorizontalPanel panel2 = p.add().panel().horizontal();
			IHorizontalPanel panel = panel2;
			IHorizontalPanel panel1 = panel.add().panel().horizontal();
			IImage image = addImage(panel1, imageResource(viewType));
			ILabel textLabel = addTextLabel(panel);
			View l = new View(panel, panel1, image, textLabel);
			l.text(label);
			IPanel<?> dummy = panel.add().panel().horizontal().width(1);
			Heights.INSTANCE.decorate(dummy);
			links.add(l);
			return l;
		}

		private String imageResource(ViewType viewType) {
			switch (viewType) {
			case TABLE:
				return "grid.png";
			case GRID:
				return "treetable.png";
			case LIST:
				return "listtemplate.png";
			case DETAIL:
				return "listdetail.png";
			}
			throw new UnsupportedOperationException();
		}

		@Override
		public WidgetTitle widgetTitle() {
			return widgetTitle;
		}

		@Override
		public IHorizontalPanel comboBoxPanel() {
			return null;
		}

		@Override
		public String title() {
			return VIEWS;
		}

		@Override
		public void visible(boolean b) {
			panel.visible(b);
		}

	}

	public static final String NAME = "Grayscale";

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {

			@Override
			public ILinearPanel<?>[] create(IContainer c) {
				ILinearPanel<?> v = c.panel().vertical();
				v.color().rgb(45, 45, 45);
				v.border().style().bottom();
				return new ILinearPanel<?>[] { v, null };
			}

			@Override
			public void itemPanel(IHorizontalPanel p) {
				p.border().style().left().style().right().color()
						.rgb(45, 45, 45);
			}

			@Override
			public int marginLeft() {
				return 6;
			}
		};
	}

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public boolean useMoreButton() {
				return false;
			}

			@Override
			public void addSeparator(ILinearPanel<?> panel) {
				panel.add().label().text("|").font().color().gray();
			}

			@Override
			public void logout(ILinearPanel<?> panel, String userName,
					Decorator[] decorators, IClickListener listener) {
				panel.add().label().text("Logged in as").font().color().mix()
						.gray().black();
				panel.add().label().text(userName).font().weight().bold()
						.color().mix().gray().black();
				panel.add().label().text("Logout").hyperlink()
						.addClickListener(listener);
			}
		};
	}

	@Override
	public int fontSize() {
		return 12;
	}

	@Override
	public String fontFamily() {
		return "Arial, Helvetica, sans-serif";
	}

	@Override
	public IUserPanel userPanel() {
		return new IUserPanel() {

			@Override
			public void background(IHorizontalPanel panel) {
			}

			@Override
			public IClickable<?> profileButton(IPanel<?> panel) {
				return panel.add().label().text("Profile").hyperlink();
			}

			@Override
			public IClickable<?> enterAdminButton(IPanel<?> panel,
					List<IAdminRightGroup> rights) {
				return panel.add().label().text("Administration").hyperlink();
			}

			@Override
			public void helpButton(IPanel<?> panel, String productName,
					String supportPage, String overviewPage) {
				// panel.add().label().text("Help").hyperlink();
				throw new UnsupportedOperationException();
			}

			@Override
			public IClickable<?> exitAdminButton(IPanel<?> panel) {
				ILabel l = panel.add().label().text("Administration");
				l.font().weight().bold();
				return panel.add().label().text("Back to Application")
						.hyperlink();
			}

			@Override
			public IClickable<?> traceButton(IContainer c) {
				return c.label().text("Trace").hyperlink();
			}
		};
	}

	private static int[] colorActive = new int[] { 245, 245, 245 };
	// private static int[] colorBackground = new int[] { 199, 224, 241 };
	private static int[] colorInactive = new int[] { 111, 111, 111 };
	private static int[] colorInactiveGradient = new int[] { 63, 63, 63 };

	@Override
	public INavigation navigation() {
		return new INavigation() {

			@Override
			public void background(IColored colored) {
				IColor color = colored.color();
				color.remove();
				color.rgb(colorActive[0], colorActive[1], colorActive[2]);
			}

			@Override
			public void active(IHorizontalPanel buttonPanel, ILabel button) {
				background((IColored) buttonPanel);
				IBorder border = buttonPanel.border();
				border.color().gray();
				border.style().noBottom();
				button.font().color().black();
			}

			@Override
			public void inactive(IHorizontalPanel buttonPanel, ILabel button) {
				IBorder border = buttonPanel.border();
				border.color()
						.mix()
						.rgb(colorInactive[0], colorInactive[1],
								colorInactive[2])
						.rgb(colorInactiveGradient[0],
								colorInactiveGradient[1],
								colorInactiveGradient[2]);
				border.style().noBottom();
				buttonPanel
						.color()
						.rgb(colorInactive[0], colorInactive[1],
								colorInactive[2])
						.gradient()
						.vertical()
						.rgb(colorInactiveGradient[0],
								colorInactiveGradient[1],
								colorInactiveGradient[2]);
				button.font().color().white();
			}

			@Override
			public boolean hasSegmentedBorder() {
				return true;
			}

			@Override
			public String activeRefreshImage() {
				return "loading_black.gif";
			}

			@Override
			public String inactiveRefreshImage() {
				return "loading_white.gif";
			}

			@Override
			public void init(IHorizontalPanel buttonPanel, ILabel button) {
			}

			@Override
			public void group(ILabel header) {
			}

			@Override
			public void activeMore(IHorizontalPanel buttonPanel, ILabel button,
					IImage refresh) {
				buttonPanel.color().remove();
				buttonPanel.color().white();
				IBorder border = buttonPanel.border();
				border.style().noBottom();
				border.color().gray();
			}

			@Override
			public boolean isActiveMoreDark() {
				return false;
			}

			@Override
			public void backgroundRegisters(IPanel<?> panel) {
				panel.color().rgb(235, 235, 235).gradient()
						.fallback(235, 235, 235).vertical().rgb(211, 211, 211);

			}

			@Override
			public void backgroundCards(IPanel<?> history) {
			}

			@Override
			public int offsetX() {
				return 0;
			}

			@Override
			public void moreItem(ILabel label, boolean isEntityLink) {
				label.hyperlink();
			}

			@Override
			public void morePopUp(IPopUp popUp) {
				popUp.border().width(0);
			}
		};
	}

	@Override
	public ILogoPanel logoPanel() {
		return new ILogoPanel() {

			@Override
			public String resource() {
				return "logo_xoricon.png";
			}

			@Override
			public void background(IPanel<?> panel) {
				panel.color().rgb(249, 249, 249).gradient()
						.fallback(235, 235, 235).vertical().rgb(235, 235, 235);
			}

			@Override
			public int marginLeft() {
				return 0;
			}
		};
	}

	@Override
	public IViewSelection createSelection(IContainer c, boolean noDiscardChanges) {
		return new ViewSelection(c, noDiscardChanges);
	}

	@Override
	public void background(IPanel<?> panel) {
		panel.color().gray(245);
	}

	@Override
	public void display(IDisplay display) {
		display.color().gray(245);
	}

	@Override
	public IWindow window() {
		return new IWindow() {

			@Override
			public void title(ILabel label, boolean sideWidget) {
				label.font().weight().bold().color().white();
			}

			@Override
			public void moreButton(CommandLink more) {
				more.label().font().color().white();
				more.border().remove().style().rounded().bottom(false);
			}

			@Override
			public int[] newButton(CommandLink commandLink, int index) {
				return null;
			}

			@Override
			public int[] saveButton(CommandLink commandLink, int index) {
				return null;
			}

			@Override
			public int[] showButton(CommandLink commandLink) {
				return null;
			}

			@Override
			public void button(CommandLink commandLink) {
			}

			@Override
			public void header(IGridPanel headerPanel, boolean sideWidget) {
				headerPanel.color().rgb(136, 136, 136).gradient().vertical()
						.rgb(113, 113, 113);
			}

			@Override
			public void background(IGridPanel panel, boolean addBorder,
					boolean plainContent, boolean sideWidget) {
				if (addBorder && !plainContent)
					panel.border().color().rgb(172, 197, 213);
			}

			@Override
			public void footer(IPanel<?> vertical, boolean sideWidget) {
				vertical.color().rgb(249, 249, 249).gradient().vertical()
						.rgb(216, 216, 216);
				IBorder border2 = vertical.border();
				IColor c = border2.color();
				WidgetTitle.decorateBorder(c);
				border2.style().top();
			}

			@Override
			public boolean useUpperCase(boolean sideWidget) {
				return sideWidget;
			}

			@Override
			public String moreImage() {
				return "more.png";
			}

			@Override
			public void buttonFooter(CommandLink cl) {
			}

			@Override
			public int heightFooter() {
				return 28;
			}

			@Override
			public int headerSpacingLeft(boolean sideWidget) {
				return 10;
			}

			@Override
			public String editImage() {
				return "edit_white.png";
			}

			@Override
			public void listItemPanel(IVerticalPanel p) {
				IBorder border = p.border();
				border.color().rgb(172, 197, 213);
				border.style().top();
			}

			@Override
			public void moreButtonActive(CommandLink more) {
			}

			@Override
			public void stylishButton(StylishButton button, boolean green,
					int blue, boolean clickable) {
				IColor newColor = button.buttonPanel().color().remove();
				if (clickable)
					newColor.rgb(111, 111, 111).gradient().vertical()
							.rgb(63, 63, 63);
				else
					newColor.rgb(140, 140, 140);
			}

			@Override
			public void scrollPane(IScrollPane scrollPane) {
				scrollPane.border().color().lightgray();
			}

			@Override
			public IContainer logPanel(IPopUp popUp) {
				return popUp.container();
			}

			@Override
			public int logPanelSpacing() {
				return 0;
			}

			@Override
			public void prepareStylishButton(StylishButton stylishButton) {
			}

			@Override
			public void addImageToStylishButton(StylishButton button, int blue) {
			}

			@Override
			public boolean editPageHasBorder() {
				return true;
			}

			@Override
			public int bulkUpdateDecrement() {
				return 40;
			}

		};
	}

	@Override
	public IMDT mdt() {
		return new IMDT() {
			@Override
			public boolean showQuickSearchOnTop() {
				return false;
			}

			@Override
			public void quickSearch(ISuggestField sf) {
				sf.height(26);
			}

			@Override
			public void actionPanel(NavigationView navigationView) {
				navigationView.widgetTitle.panel.border().remove().color()
						.gray(113);
			}
		};
	}

	@Override
	public IPopUpWindow popUp() {
		return new IPopUpWindow() {

			@Override
			public void background(IColored popUp) {
				popUp.color().gray(245);
			}
		};
	}

	@Override
	public IFilterPanel filter() {
		return new FilterPanel();
	}

	@Override
	public ITable table() {
		return new ITable() {
			@Override
			public boolean isTitleUpperCase() {
				return true;
			}

			@Override
			public String headerRowStyle(boolean isTreeTable) {
				return "headerRowHL";
			}

			@Override
			public int paddingSide() {
				return 5;
			}

			@Override
			public String contentRowStyle(boolean isTreeTable) {
				return "contentRowHL";
			}

			@Override
			public int marginTop() {
				return 6;
			}

			@Override
			public void background(IVerticalPanel container, boolean border) {
			}

			@Override
			public void statusPanel(IGridPanel statusPanel) {
				GrayScaleStyle.this.statusPanel(statusPanel);
			}

			@Override
			public ILabel statusHeader(IPanel<?> p, String text) {
				ILabel l = p.add().label().text(text.toUpperCase());
				l.font().pixel(TABLE_SELECTION_PIXEL).weight().bold().color()
						.mix().black().gray();
				return l;
			}

			@Override
			public void statusLink(ILabel l, boolean active) {
				if (active) {
					HyperlinkDecorator.styleHyperlinkActive(l);
					l.font().underline(true);
				} else
					l.font().underline(false).color().mix().black().gray();
			}

			@Override
			public void selectedColumn(IHorizontalPanel b, boolean visible,
					boolean isFirst, boolean isLast) {
				if (visible)
					b.color().gray();
				else
					b.color().white();
			}

			@Override
			public void selectedColumn(ILabel l, boolean visible) {
				if (visible)
					l.font().color().white();
				else
					l.font().color().rgb(102, 102, 102);
			}

			private IFont decorate(ILabel text) {
				return text.font().weight().bold().pixel(TABLE_SELECTION_PIXEL);
			}

			@Override
			public boolean separateSelectAllNone() {
				return true;
			}

			@Override
			public IClickable<?> selectLink(IHorizontalPanel p, boolean b) {
				ILabel select = p.add().label().hyperlink();
				decorate(select.text(b ? "ALL" : "NONE"));
				return select;
			}

			@Override
			public void selectAllNoneBackground(IHorizontalPanel p) {
				p.spacing(5);
			}

			@Override
			public boolean useColoredButtons() {
				return false;
			}

			@Override
			public void gridPlainContent(IBordered element) {
			}

			@Override
			public int fontSizeHeader() {
				return 11;
			}

			@Override
			public boolean headerCapitals() {
				return true;
			}
		};
	}

	@Override
	public ITree tree() {
		return new ITree() {
			@Override
			public void statusPanel(IPanel<?> panel) {
				GrayScaleStyle.this.statusPanel(panel);
			}

			@Override
			public boolean useColoredButtons() {
				return false;
			}

			@Override
			public void background(IVerticalPanel vertical) {
			}

			@Override
			public String backgroundColor() {
				return "white";
			}
		};
	}

	@Override
	public IRegisterStyle register() {
		return new IRegisterStyle() {
			@Override
			public void background(IColor color) {
				color.rgb(249, 249, 249).gradient().fallback(215, 215, 215)
						.vertical().rgb(215, 215, 215);
			}

			@Override
			public void inactive(IColor color, IBorder border, IFont font,
					boolean isClickable, boolean isEmpty) {
				color.rgb(112, 112, 112).gradient().vertical().rgb(63, 63, 63);
				border.color().rgb(87, 87, 87);
				IFont f = font.underline(false).weight().plain();
				if (!isClickable) {
					f.color().gray();
				} else if (isEmpty) {
					f.color().lightgray();
				} else
					f.color().white();// .color().black();

			}

			@Override
			public void active(IColor color, IBorder border, IFont font) {
				color.rgb(255, 255, 255).gradient().vertical()
						.rgb(245, 245, 245);
				border.color().gray();
				font.underline(false).weight().plain().color().black();// .color().white();
			}

			@Override
			public String loadingWhite() {
				return "loading_white.gif";
			}

			@Override
			public String loadingBlack() {
				return "loading_black.gif";
			}
		};
	}

	@Override
	public IFormStyle form() {
		return new IFormStyle() {
			@Override
			public void inputField(IBordered valuePanel) {
			}

			@Override
			public void commentField(ITextArea valuePanel) {
			}

			@Override
			public void commentHeader(ILabel upperLabel) {
			}

			@Override
			public int commentItem(IVerticalPanel vertical, int i) {
				if (i > 0) {
					IBorder border = vertical.border();
					border.color().gray();
					border.style().top();
				}
				return 0;
			}
		};
	}

	@Override
	public IN2MStyle n2m() {
		return new IN2MStyle() {

			@Override
			public void title(IVerticalPanel p, ILabel l) {
				p.color().rgb(136, 136, 136).gradient().vertical()
						.rgb(113, 113, 113);
				l.font().color().white();
			}

			@Override
			public void item(IVerticalPanel p, boolean editable) {
				if (editable)
					p.color().rgb(248, 248, 248).gradient().vertical()
							.rgb(216, 216, 216);
				else
					p.color().remove().rgb(248, 248, 248).gradient().vertical()
							.rgb(240, 240, 240);
			}

		};
	}

	@Override
	public void hyperlink(ILabel label) {
		label.font().color().rgb(0, 87, 141);
	}

	void statusPanel(IPanel<?> statusPanel) {
		IBorder border2 = statusPanel.border();
		border2.color().rgb(172, 197, 213);
		border2.style().top();
		statusPanel.color().rgb(249, 249, 249).gradient()
				.fallback(240, 240, 240).vertical().rgb(216, 216, 216);
	}

	@Override
	public void popUp(IBorder border) {
		border.color().black();
	}

	@Override
	public IExportHtml exportHtml() {
		return new IExportHtml() {

			@Override
			public int decrementY() {
				return 0;
			}

			@Override
			public int decrementX() {
				return 0;
			}
		};
	}

	@Override
	public IButtonImage button() {
		return new IButtonImage() {

			@Override
			public String run() {
				return "run.png";
			}

			@Override
			public String continueRun() {
				return "continue_run.png";
			}

			@Override
			public String add() {
				return "add.png";
			}

			@Override
			public String unassign() {
				return "cancel.png";
			}

		};
	}

}
