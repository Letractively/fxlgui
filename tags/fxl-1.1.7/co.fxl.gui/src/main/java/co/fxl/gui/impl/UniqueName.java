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

public class UniqueName {

	private String label;
	private Long id;

	public UniqueName(String label, Long id) {
		this.label = label;
		this.id = id;
	}

	public Long id() {
		return id;
	}

	public String label() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UniqueName))
			return false;
		UniqueName uniqueName = (UniqueName) object;
		if (id == null)
			return uniqueName.id == null && label.equals(uniqueName.label);
		return id.equals(uniqueName.id);
	}
}
