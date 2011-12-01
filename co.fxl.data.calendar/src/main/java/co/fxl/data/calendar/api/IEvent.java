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
package co.fxl.data.calendar.api;

public interface IEvent {

	public enum EventTransparency {

		OPAQUE, TRANSPARENT;
	}

	public enum EventVisibility {

		DEFAULT, CONFIDENTIAL, PRIVAT, PUBLIC;
	}

	public enum EventStatus {

		TENTATIVE, CONFIRMED, CANCELED;
	}

	String title();

	String description();

	void description(String description);

	String eventLocation();

	long startTime();

	void startTime(long startTime);

	long endTime();

	void endTime(long endTime);

	boolean isAllDay();

	EventStatus eventStatus();

	EventVisibility visibility();

	EventTransparency transparency();

	boolean hasAlarm();

	String iD();

	void title(String title);

	boolean save();

	void delete();

	boolean isRecurring();

	void setRecurring(boolean isRecurring);
}
