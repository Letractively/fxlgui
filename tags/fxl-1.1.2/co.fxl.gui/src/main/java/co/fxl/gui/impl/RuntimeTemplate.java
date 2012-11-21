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

import co.fxl.gui.api.IDisplay.IRuntime;

public class RuntimeTemplate implements IRuntime {

	private String name;
	private double version;

	public RuntimeTemplate(String name, double version) {
		this.name = name;
		this.version = version;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public double version() {
		return version;
	}

	@Override
	public boolean is(String... names) {
		for (String name : names) {
			if (this.name.equals(name))
				return true;
		}
		return false;
	}

	@Override
	public boolean leq(String name, double version) {
		return is(name) && leq(version);
	}

	@Override
	public boolean leq(double version) {
		return this.version <= version;
	}

	@Override
	public boolean geq(String name, double version) {
		return is(name) && geq(version);
	}

	@Override
	public boolean geq(double version) {
		return this.version >= version;
	}

}
