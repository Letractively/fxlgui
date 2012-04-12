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

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;

public interface IStyle {

	public interface ITop {

		String imageResource();

		ITop panel(IPanel<?> panel);

		int spacing();
	}

	public interface IOptionMenu {

		ILabel addCommand(IPanel<?> panel, String text);

		IOptionMenu label(ILabel label);

		IOptionMenu searchButton(IHorizontalPanel buttonPanel);

	}

	public interface ITree {

		ITree panel(IPanel<?> panel);

	}

	public interface ITable {

		public interface IColumnSelection {

			IColumnSelection panel(IPanel<?> panel, boolean visible);

			IColumnSelection label(ILabel label, boolean visible);
		}

		ITable statusPanel(IPanel<?> panel);

		IColumnSelection columnSelection();

		ITable topPanel(IPanel<?> topPanel);

	}

	public interface IRegister {

		IRegister cardPanel(IPanel<?> panel);

		IRegister topPanel(IPanel<?> panel);

	}

	public interface ILogin {

		ILogin label(ILabel text);

		ILogin hyperlink(ILabel text);

	}

	public interface IWindow {

		IWindow main(IPanel<?> panel, boolean addBorder, boolean plainContent);

		IWindow header(IPanel<?> panel, boolean isSide, boolean plainContent);

		IWindow footer(IPanel<?> panel);

		IWindow title(ILabel label, String title, boolean isSideWidget);

		IWindow navigationEntry(ILinearPanel<?> panel);

		IWindow viewEntry(ILinearPanel<?> panel, boolean first);

		IWindow button(IPanel<?> panel, boolean isSideWidget);

		ILabel addCommandLabel(ILinearPanel<?> panel, String text,
				boolean isSideWidget);

		boolean commandsOnTop();

		String moreImage();
	}

	public interface INavigation {

		public interface INavigationGroup {

			public interface INavigationItem {

				INavigation active(ILinearPanel<?> panel, ILabel label);

				INavigation inactive(ILinearPanel<?> panel, ILabel label);

				String image(String resource);
			}

			INavigation groupPanel(ILinearPanel<?> panel);

			INavigationItem item();

			INavigation mainPanel(IPanel<?> panel);
		}

		INavigation background(IColored color);

		INavigationGroup group();
	}

	ITop top();

	IOptionMenu optionMenu();

	ITable table();

	ITree tree();

	INavigation navigation();

	IRegister register();

	IWindow window();

	ILogin login();

	IStyle background(IPanel<?> panel);

	IStyle hyperlink(ILabel label);

	IStyle side(ILinearPanel<?> panel);

}
