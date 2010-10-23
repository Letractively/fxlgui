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
package co.fxl.gui.mdt.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.impl.FilterWidgetImplProvider;
import co.fxl.gui.form.impl.FormWidgetImplProvider;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget;
import co.fxl.gui.mdt.api.IProperty;
import co.fxl.gui.mdt.api.IPropertyGroup;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.mdt.impl.MasterDetailTableWidgetImplProvider;
import co.fxl.gui.register.impl.RegisterWidgetImplProvider;
import co.fxl.gui.table.filter.impl.FilterTableWidgetImplProvider;
import co.fxl.gui.tree.impl.FilterTreeWidgetImplProvider;

class MasterDetailTableWidgetTest {

	static final String DESCRIPTION = "Description";
	static final String NAME = "Name";

	void run(IDisplay display) {
		display.register(new MasterDetailTableWidgetImplProvider());
		display.register(new RegisterWidgetImplProvider());
		display.register(new FilterTableWidgetImplProvider());
		display.register(new FilterWidgetImplProvider());
		display.register(new FilterTreeWidgetImplProvider());
		display.register(new FormWidgetImplProvider());
		IVerticalPanel panel = display.layout().vertical();
		panel.color().rgb(245, 245, 245);
		@SuppressWarnings("unchecked")
		IMasterDetailTableWidget<String> widget = (IMasterDetailTableWidget<String>) panel
				.add().widget(IMasterDetailTableWidget.class);
		widget.title("Entities");
		IFilterList<String> filterList = widget.filterList();
		IPropertyGroup<String> g = widget.defaultPropertyGroup();
		@SuppressWarnings("unchecked")
		IProperty<String, String> p1 = (IProperty<String, String>) g
				.addProperty(NAME).sortable().inTable().asDetail();
		p1.type().text();
		p1.adapter(new IAdapter<String, String>() {
			@Override
			public String valueOf(String entity) {
				return nameOf(entity);
			}
		});
		filterList.addPropertyFilter(p1);
		@SuppressWarnings("unchecked")
		IProperty<String, String> p2 = (IProperty<String, String>) g
				.addProperty(DESCRIPTION).sortable().inTable().asDetail();
		p2.type().longText();
		p2.adapter(new IAdapter<String, String>() {
			@Override
			public String valueOf(String entity) {
				return descriptionOf(entity);
			}
		});
		filterList.addPropertyFilter(p2);
		widget.content(new TestContent());
		widget.visible(true);
		display.fullscreen().visible(true);
	}

	static String nameOf(String entity) {
		return "Name of " + entity;
	}

	static String descriptionOf(String entity) {
		return "Description of " + entity;
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new MasterDetailTableWidgetTest().run(display);
	}
}
