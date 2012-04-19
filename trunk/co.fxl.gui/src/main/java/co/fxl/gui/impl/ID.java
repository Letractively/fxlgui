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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ID {

	private String entityName;
	private Long iD;
	private String label;
	private boolean appendID = false;

	public ID(String entityName, Long iD, String label) {
		this.iD = iD;
		this.label = label;
	}

	public String entityName() {
		return entityName;
	}

	public ID appendID(boolean appendID) {
		this.appendID = appendID;
		return this;
	}

	public String label() {
		return label + (appendID ? " (" + iD + ")" : "");
	}

	public Long iD() {
		return iD;
	}

	@Override
	public String toString() {
		return entityName + ": " + label + " (" + iD + ")";
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

	public static void uniquify(Collection<ID> iDs) {
		Map<String, Integer> notUnique = new HashMap<String, Integer>();
		for (ID iD : iDs) {
			if (notUnique.containsKey(iD.label)) {
				notUnique.put(iD.label, notUnique.get(iD.label) + 1);
			} else {
				notUnique.put(iD.label, 1);
			}
		}
		for (ID iD : iDs) {
			iD.appendID(notUnique.get(iD.label) > 1);
		}
	}
}
