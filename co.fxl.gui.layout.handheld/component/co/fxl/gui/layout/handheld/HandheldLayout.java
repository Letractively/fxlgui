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
package co.fxl.gui.layout.handheld;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.layout.api.ILayout;
import co.fxl.gui.layout.impl.Layout;

public class HandheldLayout implements ILayout {

	private HandheldActionMenu actionMenu = new HandheldActionMenu();

	@Override
	public IActionMenu actionMenu() {
		assertEnabled();
		return actionMenu;
	}

	void assertEnabled() {
		assert Layout.ENABLED;
	}

	@Override
	public ILinearPanel<?> createLinearPanel(IContainer c) {
		assertEnabled();
		return c.panel().vertical();
	}

	private ILabel createButtonLabel(IPanel<?> panel) {
		return panel.add().label().visible(false);
	}

	@Override
	public ILayoutDialog dialog(DialogType type) {
		assertEnabled();
		throw new MethodNotImplementedException();
	}

	@Override
	public ILabel createWindowButton(boolean commandsOnTop,
			IHorizontalPanel iPanel, String text) {
		assertEnabled();
		if (commandsOnTop)
			return createButtonLabel(iPanel);
		else
			return WidgetTitle.addHyperlinkLabel2Panel(text, iPanel);
	}

	@Override
	public ILogin login() {
		return new HandheldLogin();
	}

	@Override
	public IForm form() {
		return new HandheldForm();
	}

	@Override
	public ITree tree() {
		return new HandheldTree();
	}

	@Override
	public IImage logo(IImage image) {
		return image.resource("android.png");
	}

	@Override
	public ILinearPanel<?> createLinearPanel(IContainer c,
			boolean isForFilterQuery) {
		return !(isForFilterQuery) ? createLinearPanel(c) : c.panel()
				.horizontal();
	}

	@Override
	public INavigation navigation() {
		return new HandheldNavigation();
	}
}
