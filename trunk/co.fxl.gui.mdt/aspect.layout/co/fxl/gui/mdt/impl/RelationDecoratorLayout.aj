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
package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

privileged aspect RelationDecoratorLayout {

	void around(final IScrollTableWidget.IDecorator dec) : 
	call(void RelationDecorator.listenOnAdd(IScrollTableWidget.IDecorator)) 
	&& args(dec) {
		proceed(new IScrollTableWidget.IDecorator() {

			private List<IClickListener> cls = new LinkedList<IClickListener>();

			@Override
			public IClickable<?> decorate(final IContainer c) {
				c.image().resource("add.png")
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								final IDialog dialog = c.display().showDialog()
										.title("ADD ENTITY");
								dialog.addButton().text("Back")
										.imageResource("back.png")
										.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												dialog.visible(false);
											}
										});
								ILinearPanel<?> p = dialog.container().panel()
										.vertical().spacing(6).add().panel()
										.vertical();
								IClickable<?> c = dec.decorate(p.add());
								for (IClickListener cl : cls)
									c.addClickListener(cl);
								dialog.visible(true);
							}
						}).mouseLeft();
				return new IClickable<Object>() {

					@Override
					public Object clickable(boolean clickable) {
						return this;
					}

					@Override
					public boolean clickable() {
						return false;
					}

					@Override
					public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
							co.fxl.gui.api.IClickable.IClickListener clickListener) {
						cls.add(clickListener);
						return null;
					}

				};
			}
		});
	}
}
