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
package co.fxl.gui.upload.impl;

import co.fxl.gui.upload.api.IUpload;

public class UploadImpl implements IUpload {

	private String url;
	private String name;
	private String description;
	private boolean isFileUpload;

	public UploadImpl(String text, String text2, String text3,
			boolean isFileUpload) {
		url = text;
		name = text2;
		description = text3;
		this.isFileUpload = isFileUpload;
	}

	@Override
	public String uRL() {
		return url;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public boolean isFileUpload() {
		return isFileUpload;
	}
}
