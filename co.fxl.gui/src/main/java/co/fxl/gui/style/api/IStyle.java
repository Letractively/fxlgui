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
package co.fxl.gui.style.api;

import java.util.List;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
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
import co.fxl.gui.api.ITextInputElement;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.NavigationView;
import co.fxl.gui.impl.StylishButton;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.impl.WidgetTitle;

public interface IStyle {

	public interface IExportHtml {

		int decrementX();

		int decrementY();

	}

	public interface IN2MStyle {

		void title(IVerticalPanel p, ILabel l);

		void item(IVerticalPanel p, boolean editable);

	}

	public interface IFormStyle {

		void inputField(IBordered valuePanel);

		void commentHeader(ILabel upperLabel);

		void commentField(ITextArea commentField);

		int commentItem(IVerticalPanel vertical, int i);

	}

	public interface IRegisterStyle {

		void background(IColor color);

		void inactive(IColor color, IBorder border, IFont font,
				boolean clickable, boolean empty);

		void active(IColor color, IBorder border, IFont font);

		String loadingWhite();

		String loadingBlack();

	}

	public interface ITable {

		boolean isTitleUpperCase();

		String headerRowStyle(boolean isTreeTable);

		int paddingSide();

		String contentRowStyle(boolean isTreeTable);

		int marginTop();

		void background(IVerticalPanel container, boolean border);

		void statusPanel(IGridPanel statusPanel);

		ILabel statusHeader(IPanel<?> panel, String text);

		void statusLink(ILabel l, boolean active);

		void selectedColumn(IHorizontalPanel b, boolean visible,
				boolean isFirst, boolean isLast);

		void selectedColumn(ILabel l, boolean visible);

		IClickable<?> selectLink(IHorizontalPanel p, boolean b);

		boolean separateSelectAllNone();

		void selectAllNoneBackground(IHorizontalPanel p);

		boolean useColoredButtons();

		void gridPlainContent(IElement<?> element);

	}

	public interface IFilterPanel {

		void decorate(IGridCell cell);

		void decorate(IComboBox input);

		void decorate(ITextInputElement<?> input);

		String acceptImage(boolean isMiniFilter);

		String acceptTitle();

		String clearTitle();

		boolean containsRefreshButton();

		String title(boolean isMini);

		void removeErrorColor(ITextElement<?> textField);

	}

	public interface IPopUpWindow {

		void background(IColored colored);

	}

	public interface ITree {

		void statusPanel(IPanel<?> panel);

		void background(IVerticalPanel vertical);

		String backgroundColor();

		boolean useColoredButtons();
	}

	public interface IMDT {

		boolean showQuickSearchOnTop();

		void quickSearch(ISuggestField sf);

		void actionPanel(NavigationView navigationView);

	}

	public interface IWindow {

		void title(ILabel label, boolean sideWidget);

		void moreButton(CommandLink label);

		int[] newButton(CommandLink commandLink, int index);

		int[] showButton(CommandLink commandLink);

		void button(CommandLink commandLink);

		void header(IGridPanel headerPanel, boolean sideWidget);

		void background(IGridPanel panel, boolean addBorder,
				boolean plainContent, boolean sideWidget);

		void footer(IPanel<?> vertical, boolean sideWidget);

		boolean useUpperCase(boolean sideWidget);

		String moreImage();

		void buttonFooter(CommandLink cl);

		int heightFooter();

		int headerSpacingLeft(boolean sideWidget);

		String editImage();

		void listItemPanel(IVerticalPanel p);

		void moreButtonActive(CommandLink more);

		void stylishButton(StylishButton button, boolean green, int blue,
				boolean clickable);

		void scrollPane(IScrollPane scrollPane);

		IContainer logPanel(IPopUp popUp);

		int logPanelSpacing();

		void prepareStylishButton(StylishButton stylishButton);

		void addImageToStylishButton(StylishButton button, int blue);

	}

	public interface IViewSelection {

		public enum ViewType {

			TABLE, GRID, LIST, DETAIL;
		}

		public interface IView extends IClickable<IView>, IUpdateable<String> {

			IComboBox addComboBox();

			void removeLoadingIcon();

			IHorizontalPanel panel0();

			void comboBox(IComboBox cb, IHorizontalPanel p, boolean b);

			void showActive();

			IComboBox cb();

			String cbText();

			boolean firstTimeLoad();

			void showLoadingIcon();

			boolean isActive();

		}

		IView addView(String label, ViewType viewType, boolean isLast);

		WidgetTitle widgetTitle();

		IHorizontalPanel comboBoxPanel();

		String title();

		void visible(boolean b);

	}

	public interface ILogoPanel {

		String resource();

		int marginLeft();

		void background(IPanel<?> panel);

	}

	public interface INavigation {

		void background(IColored colored);

		void init(IHorizontalPanel buttonPanel, ILabel button);

		void active(IHorizontalPanel buttonPanel, ILabel button);

		void inactive(IHorizontalPanel buttonPanel, ILabel button);

		String activeRefreshImage();

		String inactiveRefreshImage();

		boolean hasSegmentedBorder();

		void group(ILabel header);

		void activeMore(IHorizontalPanel buttonPanel, ILabel button,
				IImage refresh);

		boolean isActiveMoreDark();

		void backgroundRegisters(IPanel<?> panel);

		void backgroundCards(IPanel<?> history);

		int offsetX();

		void moreItem(ILabel label, boolean isEntityLink);

		void morePopUp(IPopUp popUp);

	}

	public interface IUserPanel {

		public interface IAdminRightGroup {

			public interface IAdminRight extends IClickListener {

				String image();

				String label();
			}

			String label();

			List<IAdminRight> rights();
		}

		void background(IHorizontalPanel panel);

		IClickable<?> profileButton(IPanel<?> panel);

		IClickable<?> enterAdminButton(IPanel<?> panel,
				List<IAdminRightGroup> rights);

		IClickable<?> exitAdminButton(IPanel<?> panel);

		IClickable<?> traceButton(IContainer c);

	}

	public interface IApplicationPanel {

		ILinearPanel<?>[] create(IContainer c);

		void itemPanel(IHorizontalPanel p);

		int marginLeft();
	}

	public interface ILogin {

		boolean useMoreButton();

		void addSeparator(ILinearPanel<?> panel);

		void logout(ILinearPanel<?> panel, String userName,
				Decorator[] decorators, IClickListener listener);

	}

	IApplicationPanel applicationPanel();

	ILogin login();

	IUserPanel userPanel();

	int fontSize();

	String fontFamily();

	INavigation navigation();

	ILogoPanel logoPanel();

	IViewSelection createSelection(IContainer c, boolean noDiscardChangesDialog);

	void background(IPanel<?> panel);

	void display(IDisplay display);

	IWindow window();

	IMDT mdt();

	IPopUpWindow popUp();

	IFilterPanel filter();

	ITable table();

	ITree tree();

	IRegisterStyle register();

	IFormStyle form();

	IN2MStyle n2m();

	void hyperlink(ILabel label);

	void popUp(IBorder border);

	IExportHtml exportHtml();

}
