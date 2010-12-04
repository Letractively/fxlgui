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
package co.fxl.gui.filter.test;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter.IAdapter;
import co.fxl.gui.filter.impl.FilterWidgetImplProvider;

class FilterWidgetTest implements IFilterListener {

	class Entity {

		private String s;
		private Long i;

		Entity(String string, Long i) {
			s = string;
			this.i = i;
		}

		public Long id() {
			return i;
		}

		public String name() {
			return s;
		}

	}

	private static final String DESCRIPTION = "Description";
	private static final String NAME = "Name";
	private static final String DATE = "Date";
	private static final String INT = "Int";
	private static final String STATE = "State";

	void run(IDisplay display) {
		display.register(new FilterWidgetImplProvider());
		SplitLayout split = new SplitLayout(display.container().panel()
				.vertical().add().panel());
		IFilterWidget widget = (IFilterWidget) split.sidePanel.add().widget(
				IFilterWidget.class);
		widget.addFilter().name(NAME);
		widget.addFilter().name(DESCRIPTION);
		widget.addFilter().name(DATE).type().date();
		widget.addFilter().name(INT).type().integer();
		widget.addFilter().name(STATE).type().addConstraint("Failed", "Passed");
		List<Entity> es = new LinkedList<Entity>();
		es.add(new Entity("First", 0l));
		es.add(new Entity("Second", 1l));
		es.add(new Entity("Third", 2l));
		es.add(new Entity("Fourth", 3l));
		@SuppressWarnings("unchecked")
		IRelationFilter<Entity, Long> ir = (IRelationFilter<Entity, Long>) widget
				.addRelationFilter().name("Relation");
		ir.adapter(new IAdapter<Entity, Long>() {

			@Override
			public String name(Entity entity) {
				return entity.name();
			}

			@Override
			public Long id(Entity entity) {
				return entity.id();
			}
		});
		ir.preset(es);
		widget.addSizeFilter();
		widget.addFilterListener(this);
		widget.visible(true);
		display.fullscreen().visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new FilterWidgetTest().run(display);
	}

	@Override
	public void onApply(IFilterConstraints constraints) {
		if (constraints.isAttributeConstrained(NAME))
			System.out.println(NAME + "=" + constraints.stringValue(NAME));
		if (constraints.isAttributeConstrained(DESCRIPTION))
			System.out.println(DESCRIPTION + "="
					+ constraints.stringValue(DESCRIPTION));
		if (constraints.isAttributeConstrained(DATE))
			System.out.println(DATE + "="
					+ constraints.dateRange(DATE).lowerBound() + "-"
					+ constraints.dateRange(DATE).upperBound());
		if (constraints.isAttributeConstrained(INT))
			System.out.println(INT + "="
					+ constraints.intRange(INT).lowerBound() + "-"
					+ constraints.intRange(INT).upperBound());
		if (constraints.isAttributeConstrained(STATE))
			System.out.println(STATE + "=" + constraints.stringValue(STATE));
		System.out.println("Size" + "=" + constraints.size());
	}
}
