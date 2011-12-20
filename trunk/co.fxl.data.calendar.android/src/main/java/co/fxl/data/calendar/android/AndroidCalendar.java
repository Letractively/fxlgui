/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.data.calendar.android;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import co.fxl.data.calendar.api.ICalendar;
import co.fxl.data.calendar.api.IEvent;

class AndroidCalendar implements ICalendar {

	private Activity activity;
	private String name;
	String iD;
	private List<IEvent> events = new LinkedList<IEvent>();

	AndroidCalendar(Activity activity, String calName, String calId) {
		this.activity = activity;
		name = calName;
		iD = calId;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<IEvent> events() {
		events.clear();
		Uri eventsUri = Uri
				.parse(AndroidEvent.ANDROID_CALENDAR_EVENTS);
		Cursor c = activity.getContentResolver().query(eventsUri,
				AndroidEvent.COLUMNS, "calendar_id=?", new String[] { iD },
				null);
		if (c.moveToFirst()) {
			do {
				AndroidEvent event = new AndroidEvent(activity, c, iD);
				this.events.add(event);
			} while (c.moveToNext());
		}
		return events;
	}

	@Override
	public IEvent createEvent() {
		return new AndroidEvent(activity, null, iD);
	}

	@Override
	public String toString() {
		return name + " (" + iD + ")";
	}
}
