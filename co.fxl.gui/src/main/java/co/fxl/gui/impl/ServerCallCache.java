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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServerCallCache {

	private static ServerCallCache instance = new ServerCallCache();
	public boolean record = false;
	public boolean replay = false;
	private Map<Object, Object> cache = new HashMap<Object, Object>();

	public static ServerCallCache instance() {
		return instance;
	}

	public ServerCallCache record(boolean record) {
		assert !replay;
		if (record)
			reset();
		this.record = record;
		return this;
	}

	public ServerCallCache replay(boolean replay) {
		this.replay = replay;
		return this;
	}

	public ServerCallCache reset() {
		cache.clear();
		return this;
	}

	public void put(Object pCommand, Serializable result) {
		cache.put(pCommand, result);
	}

	public Object get(Object pCommand) {
		Object remove = cache.get(pCommand);
		return remove;
	}

	@Override
	public String toString() {
		return cache.keySet().toString();
	}

}
