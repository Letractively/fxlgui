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
package co.fxl.gui.layout.api;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextField;

public interface ILayout {

	public interface ITree {

		int defaultSplitPosition();

	}

	public interface IActionMenu {

		IActionMenu sidePanel(ILinearPanel<?> panel);

		IActionMenu grid(IGridPanel grid);

		IActionMenu container(IContainer container);

		IActionMenu showContent();

	}

	public interface ILayoutDialog {

		public interface IClickTarget {

			void onOK();

			void onCancel();

		}

		public interface IDecorator {

			void decorate(IContainer container);

		}

		ILayoutDialog decorator(IDecorator decorator);

		IClickTarget addClickListener(IClickListener clickListener);

		IPanel<?> createButton(IPanel<?> panel);

	}

	public interface ILogin {

		ILogin id(ITextField id);

		ILogin password(IPasswordField password);

		ILogin panel(IHorizontalPanel panel);

		ILogin label(ILabel loginLabel);

		ILogin loginListener(IClickListener l);

		ILogin loggedInAs(ILabel label);

		ILogin loginPanel(IHorizontalPanel p);

	}

	public interface IForm {

		IGridPanel.IGridCell middleCell(IGridPanel grid, int row, int column);

		IGridPanel.IGridCell outerCell(IGridPanel grid, int row);

		IGridPanel.IGridCell cell(IGridPanel.IGridCell cell);

		IForm grid(IGridPanel grid);

	}

	public enum DialogType {

		LOGIN, FILTER;
	}

	IForm form();

	ILogin login();

	IActionMenu actionMenu();

	ILabel createWindowButton(boolean commandsOnTop, IHorizontalPanel panel,
			String text);

	ILinearPanel<?> createLinearPanel(IContainer c);

	ILinearPanel<?> createLinearPanel(IContainer c, boolean isForFilterQuery);

	ILayoutDialog dialog(DialogType type);

	ITree tree();

	IImage logo(IImage image);

}
