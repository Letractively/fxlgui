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
package co.fxl.gui.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.impl.Style;

public class UserPanel {

	public interface Weight {

		UserPanel weight(int weight);
	}

	public interface Decorator {

		void decorate(IPanel<?> panel);

		boolean isVisible();
	}

	private class DecoratorAdp implements Weight, Comparable<DecoratorAdp> {

		private Decorator decorator;
		private int weight = 0;

		private DecoratorAdp(Decorator decorator) {
			this.decorator = decorator;
			decorators.add(this);
		}

		@Override
		public UserPanel weight(int weight) {
			this.weight = weight;
			return UserPanel.this;
		}

		@Override
		public int compareTo(DecoratorAdp o) {
			return weight - o.weight;
		}

	}

	private static final UserPanel INSTANCE = new UserPanel();
	private List<DecoratorAdp> decorators = new LinkedList<DecoratorAdp>();
	private IHorizontalPanel panel;

	public UserPanel container(IContainer container) {
		panel = container.panel().horizontal().spacing(4).align().end();
		Style.instance().userPanel().background(panel);
		return this;
	}

	public UserPanel update() {
		if (panel == null)
			return this;
		Collections.sort(decorators);
		panel.clear();
		boolean first = true;
		for (DecoratorAdp d : decorators) {
			if (!d.decorator.isVisible())
				continue;
			if (!first) {
				Style.instance().login().addSeparator(panel);
			}
			first = false;
			d.decorator.decorate(panel);
		}
		return this;
	}

	public Weight add(Decorator decorator) {
		DecoratorAdp d = new DecoratorAdp(decorator);
		return d;
	}

	public static UserPanel instance() {
		return INSTANCE;
	}

	public void clear() {
		decorators.clear();
	}
}
