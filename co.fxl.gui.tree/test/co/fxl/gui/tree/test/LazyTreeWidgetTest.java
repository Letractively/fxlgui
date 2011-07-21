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
package co.fxl.gui.tree.test;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;
import co.fxl.gui.tree.api.ILazyTreeWidget;
import co.fxl.gui.tree.api.ILazyTreeWidget.IDecorator;

public class LazyTreeWidgetTest implements IDecorator {

	private ILazyTreeWidget<Object> tree;

	@SuppressWarnings("unchecked")
	public LazyTreeWidgetTest(IDisplay display) {
		display.register(new LazyScrollPanelImplWidgetProvider());
		IVerticalPanel panel = display.container().panel().vertical();
		tree = (ILazyTreeWidget<Object>) panel.add().widget(
				ILazyTreeWidget.class);
		tree.selectionDecorator(this);
		tree.tree(new TestLazyTree(5));
		tree.height(600);
		tree.visible(true);
		display.fullscreen().visible(true);
	}

	@Override
	public void decorate(IContainer c, int index) {
		c.label().text("# " + index);
	}
}
