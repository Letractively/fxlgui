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

public class ID {

	private String entityName;
	private Long iD;
	private String text;

	public ID(String entityName, Long iD, String text) {
		this.iD = iD;
		this.text = text;
	}

	public String entityName() {
		return entityName;
	}

	public String text() {
		return text;
	}

	public Long iD() {
		return iD;
	}

	@Override
	public String toString() {
		return entityName + ": " + text + " (" + iD + ")";
	}

	@Override
	public int hashCode() {
		return (entityName + "|" + iD).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ID))
			return false;
		ID i = (ID) o;
		return entityName.equals(i.entityName) && iD.equals(i.iD);
	}
}
