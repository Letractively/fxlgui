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
package co.fxl.gui.table.util.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;

public class HTMLDecorator implements IDecorator {

	public interface Resolver {

		String resolve(int rowIndex);

	}

	public class Parameter {

		private String name;
		private Resolver resolver;

		private Parameter(String name) {
			this.name = name;
		}

		public Parameter resolver(Resolver resolver) {
			this.resolver = resolver;
			return this;
		}
	}

	private String template;
	private List<Parameter> parameters = new LinkedList<Parameter>();
	private Map<Integer, IElement<?>> elements = new HashMap<Integer, IElement<?>>();

	public HTMLDecorator template(String template) {
		this.template = template;
		return this;
	}

	public Parameter addParameter(String name) {
		Parameter parameter = new Parameter(name);
		parameters.add(parameter);
		return parameter;
	}

	@Override
	public void decorate(IContainer container, int firstRow, int lastRow) {
		elements.clear();
		IVerticalPanel p = container.panel().vertical();
		for (int i = firstRow; i <= lastRow; i++) {
			String text = template;
			for (Parameter pm : parameters) {
				text = text.replace(pm.name, pm.resolver.resolve(i));
			}
			ILabel l = p.add().label().html(text);
			elements.put(i, l);
		}
	}

	@Override
	public int rowHeight(int rowIndex) {
		return elements.get(rowIndex).height();
	}

	@Override
	public boolean checkIndex(int rowIndex) {
		return true;
	}
}
