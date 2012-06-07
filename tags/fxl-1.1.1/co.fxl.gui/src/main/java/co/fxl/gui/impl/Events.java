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

import java.util.LinkedList;
import java.util.List;

public class Events {

	public static String SERVER_CALL_START = "SCS";
	public static String SERVER_CALL_RETURN = "SCR";

	public interface EventListener {

		void notifyEvent();
	}

	public interface ListenerRegistration {

		void remove();
	}

	private class ListenerRegistrationAdp implements ListenerRegistration {

		private EventListener l;
		private String event;

		private ListenerRegistrationAdp(String event, EventListener l) {
			this.event = event;
			this.l = l;
			listeners.add(this);
		}

		@Override
		public void remove() {
			listeners.remove(this);
		}
	}

	private static final Events INSTANCE = new Events();
	private List<ListenerRegistrationAdp> listeners = new LinkedList<ListenerRegistrationAdp>();

	public void notifyEvent(String event) {
		for (ListenerRegistrationAdp adp : listeners) {
			if (adp.event.equals(event))
				adp.l.notifyEvent();
		}
	}

	public Events clear() {
		listeners.clear();
		return this;
	}

	public ListenerRegistration register(String event, EventListener l) {
		return new ListenerRegistrationAdp(event, l);
	}

	public static Events instance() {
		return INSTANCE;
	}

}
