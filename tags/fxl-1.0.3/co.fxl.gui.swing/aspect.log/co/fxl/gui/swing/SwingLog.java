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
package co.fxl.gui.swing;

import java.util.Date;

import co.fxl.data.format.impl.Format;
import co.fxl.gui.log.ILog;

public class SwingLog implements ILog {

	// TODO extract to aspect

	private Date lastLog = null;

	@Override
	public ILog debug(String message) {
		return log("DEBUG", message);
	}

	private ILog log(String string, String message) {
		String time = Format.time().format(new Date());
		System.out.println(time + " " + string + ": " + message + " ["
				+ getDelta() + "]");
		return this;
	}

	private String getDelta() {
		if (lastLog != null) {
			Date ll = lastLog;
			lastLog = new Date();
			return (lastLog.getTime() - ll.getTime()) + "ms";
		}
		lastLog = new Date();
		return "-";
	}

}
