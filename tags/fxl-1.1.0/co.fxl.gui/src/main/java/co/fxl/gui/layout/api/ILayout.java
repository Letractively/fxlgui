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


import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;

public interface ILayout {

	INavigationLayout navigationMain();

	INavigationLayout navigationSub();

	IFormLayout form();

	ILoginLayout login();

	IActionMenuLayout actionMenu();

	ILabel createWindowButton(boolean commandsOnTop, IHorizontalPanel panel,
			String text);

	ILinearPanel<?> createLinearPanel(IContainer c);

	ILinearPanel<?> createLinearPanel(IContainer c, boolean isForFilterQuery);

	IDialogLayout dialog(DialogType type);

	ITreeLayout tree();

	IMDTLayout mdt();

	IImage logo(IImage image);

}
