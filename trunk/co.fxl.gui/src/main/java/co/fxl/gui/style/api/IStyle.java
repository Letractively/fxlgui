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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.impl.WidgetTitle;

public interface IStyle {

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

		void moreItem(ILabel label);

	}

	public interface IUserPanel {

		void background(IHorizontalPanel panel);

		IClickable<?> profileButton(IPanel<?> panel);

		IClickable<?> enterAdminButton(IPanel<?> panel);

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

}
