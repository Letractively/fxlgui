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

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import co.fxl.data.calendar.api.IEvent;

class AndroidEvent implements IEvent {

	static final String ANDROID_CALENDAR_EVENTS = AndroidCalendars.ANDROID_CALENDAR
			+ "/events";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String ID = "_id";
	private static final String START_TIME = "dtstart";
	private static final String END_TIME = "dtend";
	private static final String RRULE = "rrule";
	static final String[] COLUMNS = new String[] { TITLE, ID, START_TIME,
			END_TIME, DESCRIPTION, RRULE };
	private static final boolean ALLOW_CHANGE = true;
	private static final boolean TO_LOG = false;
	private String title;
	private String description;
	private String iD = null;
	private Activity activity;
	private String calID;
	private long startTime;
	private String originalTitle;
	private long originalStartTime = 0;
	private boolean isRecurring = false;
	private long endTime;

	AndroidEvent(Activity activity, Cursor c, String calID) {
		this.activity = activity;
		this.calID = calID;
		for (int i = 0; c != null && i < c.getColumnCount(); i++) {
			title = getString(c, TITLE);
			originalTitle = title;
			iD = getString(c, ID);
			startTime = getLong(c, START_TIME);
			endTime = getLong(c, END_TIME);
			originalStartTime = startTime;
			description = getString(c, DESCRIPTION);
			isRecurring = getString(c, RRULE) != null;
		}
	}

	@Override
	public boolean isRecurring() {
		return isRecurring;
	}

	@Override
	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	private String getString(Cursor c, String string) {
		int column = c.getColumnIndex(string);
		return c.getString(column);
	}

	private long getLong(Cursor c, String string) {
		int column = c.getColumnIndex(string);
		return c.getLong(column);
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public long endTime() {
		return endTime;
	}

	@Override
	public String eventLocation() {
		throw new MethodNotImplementedException();
	}

	@Override
	public EventStatus eventStatus() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean hasAlarm() {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean isAllDay() {
		throw new MethodNotImplementedException();
	}

	@Override
	public long startTime() {
		return startTime;
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public EventTransparency transparency() {
		throw new MethodNotImplementedException();
	}

	@Override
	public EventVisibility visibility() {
		throw new MethodNotImplementedException();
	}

	@Override
	public String iD() {
		return iD;
	}

	@Override
	public void title(String title) {
		this.title = title;
	}

	@Override
	public boolean save() {
		if (title == null)
			return false;
		if (title.equals(originalTitle) && startTime == originalStartTime)
			return false;
		Uri eventsUri = Uri.parse(ANDROID_CALENDAR_EVENTS);
		ContentValues event = new ContentValues();
		event.put("calendar_id", calID);
		event.put("title", title);
		event.put("dtstart", startTime);
		event.put("dtend", endTime);
		if (iD == null) {
			if (ALLOW_CHANGE) {
				iD = activity.getContentResolver().insert(eventsUri, event)
						.getLastPathSegment();
			}
			if (TO_LOG)
				System.out.println("inserting " + event + " at " + eventsUri);
		} else {
			Uri uri = ContentUris.withAppendedId(eventsUri, Long.valueOf(iD));
			if (ALLOW_CHANGE) {
				activity.getContentResolver().update(uri, event, null, null);
			}
			if (TO_LOG)
				System.out.println("updating " + event + " at " + uri);
		}
		return true;
	}

	@Override
	public void delete() {
		Uri CALENDAR_URI = Uri.parse(ANDROID_CALENDAR_EVENTS);
		Uri uri = ContentUris.withAppendedId(CALENDAR_URI, Long.valueOf(iD));
		if (ALLOW_CHANGE) {
			activity.getContentResolver().delete(uri, null, null);
		}
		if (TO_LOG)
			System.out.println("deleting " + uri);
	}

	@Override
	public void startTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public void description(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return title + "\n" + description;
	}

	@Override
	public void endTime(long endTime) {
		this.endTime = endTime;
	}
}
