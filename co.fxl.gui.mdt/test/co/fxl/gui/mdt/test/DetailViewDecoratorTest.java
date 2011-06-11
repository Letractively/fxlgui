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
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.form.impl.FormWidgetImplProvider;
import co.fxl.gui.mdt.api.IProperty;
import co.fxl.gui.mdt.api.IProperty.IAdapter;
import co.fxl.gui.mdt.api.IProperty.IConditionRule;
import co.fxl.gui.mdt.api.IProperty.IConditionRule.ICondition;
import co.fxl.gui.mdt.impl.DetailViewDecorator;
import co.fxl.gui.mdt.impl.PropertyGroupImpl;

class MasterDetailTableWidgetTest {

	private static final String INVISIBLE = "Invisible";
	private static final String VISIBLE = "Visible";
	private static final String EDITABLE = "Editable";
	private static final String NON_EDITABLE = "Non Editable";

	void run(IDisplay display) {
		display.register(new FormWidgetImplProvider());
		PropertyGroupImpl g = getPropertyGroup();
		DetailViewDecorator dec = new DetailViewDecorator(g) {
		};
		dec.setUpdateable(true);
		dec.decorate(display.container().panel().vertical(), null, "Entity");
		display.fullscreen().visible(true);
	}

	private PropertyGroupImpl getPropertyGroup() {
		PropertyGroupImpl g = new PropertyGroupImpl("Property Group");
		IProperty<Object, String> p1 = addP1(g);
		IProperty<Object, String> p2 = addP2(g);
		IProperty<Object, String> p3 = addP3(g);
		IProperty<Object, String> p4 = addP4(g);
		@SuppressWarnings("unchecked")
		IConditionRule<Object, String, String> c = (IConditionRule<Object, String, String>) p1
				.addConditionRule();
		c.target(p2).condition(new ICondition<Object, String>() {

			@Override
			public boolean satisfied(Object e, String value) {
				return INVISIBLE.equals(value);
			}
		}).visible(true);
		@SuppressWarnings("unchecked")
		IConditionRule<Object, String, String> c2 = (IConditionRule<Object, String, String>) p3
				.addConditionRule();
		List<String> l = new LinkedList<String>();
		l.add("1");
		l.add("3");
		c2.target(p2).condition(new ICondition<Object, String>() {

			@Override
			public boolean satisfied(Object e, String value) {
				return NON_EDITABLE.equals(value);
			}
		}).modifieable(true);
		@SuppressWarnings("unchecked")
		IConditionRule<Object, String, String> c3 = (IConditionRule<Object, String, String>) p3
				.addConditionRule();
		c3.target(p4).condition(new ICondition<Object, String>() {

			@Override
			public boolean satisfied(Object e, String value) {
				return NON_EDITABLE.equals(value);
			}
		}).targetValues(l);
		return g;
	}

	private IProperty<Object, String> addP4(PropertyGroupImpl g) {
		@SuppressWarnings("unchecked")
		IProperty<Object, String> p4 = (IProperty<Object, String>) g
				.addProperty("Property 4");
		p4.type().text().addConstraint("1", "2", "3", "4");
		p4.adapter(new IAdapter<Object, String>() {

			@Override
			public boolean hasProperty(Object entity) {
				return true;
			}

			@Override
			public String valueOf(Object entity) {
				return "1";
			}

			@Override
			public boolean valueOf(Object entity, String value) {
				throw new MethodNotImplementedException();
			}

			@Override
			public boolean editable(Object entity) {
				throw new MethodNotImplementedException();
			}
		});
		p4.editable(true);
		return p4;
	}

	private IProperty<Object, String> addP3(PropertyGroupImpl g) {
		@SuppressWarnings("unchecked")
		IProperty<Object, String> p3 = (IProperty<Object, String>) g
				.addProperty("Property 3");
		p3.type().text().addConstraint(EDITABLE, NON_EDITABLE);
		p3.adapter(new IAdapter<Object, String>() {

			@Override
			public boolean hasProperty(Object entity) {
				return true;
			}

			@Override
			public String valueOf(Object entity) {
				return EDITABLE;
			}

			@Override
			public boolean valueOf(Object entity, String value) {
				throw new MethodNotImplementedException();
			}

			@Override
			public boolean editable(Object entity) {
				throw new MethodNotImplementedException();
			}
		});
		p3.editable(true);
		return p3;
	}

	private IProperty<Object, String> addP2(PropertyGroupImpl g) {
		@SuppressWarnings("unchecked")
		IProperty<Object, String> p2 = (IProperty<Object, String>) g
				.addProperty("Property 2");
		p2.type().longText();
		p2.adapter(new IAdapter<Object, String>() {

			@Override
			public boolean hasProperty(Object entity) {
				return true;
			}

			@Override
			public String valueOf(Object entity) {
				return "Description of " + entity;
			}

			@Override
			public boolean valueOf(Object entity, String value) {
				throw new MethodNotImplementedException();
			}

			@Override
			public boolean editable(Object entity) {
				throw new MethodNotImplementedException();
			}
		});
		p2.editable(true);
		return p2;
	}

	@SuppressWarnings("unchecked")
	private IProperty<Object, String> addP1(PropertyGroupImpl g) {
		IProperty<Object, String> p1 = (IProperty<Object, String>) g
				.addProperty("Property 1").editable(true);
		p1.type().text().addConstraint(VISIBLE, INVISIBLE);
		p1.adapter(new IAdapter<Object, String>() {

			@Override
			public boolean hasProperty(Object entity) {
				return true;
			}

			@Override
			public String valueOf(Object entity) {
				return VISIBLE;
			}

			@Override
			public boolean valueOf(Object entity, String value) {
				throw new MethodNotImplementedException();
			}

			@Override
			public boolean editable(Object entity) {
				throw new MethodNotImplementedException();
			}
		});
		return p1;
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
