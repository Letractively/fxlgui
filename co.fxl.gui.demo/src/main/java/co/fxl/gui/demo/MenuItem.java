/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.demo;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.navigation.impl.TabDecoratorTemplate;
import co.fxl.gui.navigation.menu.api.IMenuItem;
import co.fxl.gui.navigation.menu.api.IMenuWidget;

class MenuItem {

	private ExampleDecorator exampleDecorator;
	private IMenuItem parent;

	MenuItem(IMenuWidget widget, ExampleDecorator decorator, String title) {
		parent = widget.defaultGroup().addTab().name(title);
		this.exampleDecorator = decorator;
	}

	MenuItem(IMenuWidget widget, ExampleDecorator exampleDecorator,
			String title, final Decorator decorator) {
		this.exampleDecorator = exampleDecorator;
		parent = widget.defaultGroup().addTab().name(title);
		apply(decorator, parent);
	}

	IMenuItem nest(String title, final Decorator decorator) {
		final IMenuItem child = parent.addNavigationItem().name(title);
		apply(decorator, child);
		return child;
	}

	void apply(final Decorator decorator, final IMenuItem child) {
		child.decorator(new TabDecoratorTemplate() {
			@Override
			public void refresh(ICallback<Void> cb) {
				decorator.decorate(exampleDecorator, panel);
				decorator.update(panel);
				cb.onSuccess(null);
			}
		});
	}

	void active() {
		parent.active(true, DummyCallback.voidInstance());
	}
}
