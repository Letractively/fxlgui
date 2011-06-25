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
import co.fxl.data.calendar.api.ICalendars;

public class AndroidCalendars implements ICalendars {

	private List<ICalendar> calendars = new LinkedList<ICalendar>();

	public AndroidCalendars(Activity activity) {
		String[] projection = new String[] { "_id", "name" };
		Uri calendars = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = activity.managedQuery(calendars, projection,
				"selected=1", null, null);
		if (managedCursor.moveToFirst()) {
			int nameColumn = managedCursor.getColumnIndex("name");
			int idColumn = managedCursor.getColumnIndex("_id");
			do {
				String calName = managedCursor.getString(nameColumn);
				String calId = managedCursor.getString(idColumn);
				AndroidCalendar calendar = new AndroidCalendar(activity,
						calName, calId);
				this.calendars.add(calendar);
			} while (managedCursor.moveToNext());
		}
	}

	@Override
	public List<ICalendar> calendars() {
		return calendars;
	}
}
