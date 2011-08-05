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
package co.fxl.gui.input.test;

import org.junit.Test;

import co.fxl.gui.input.api.IRTFWidget;
import co.fxl.gui.input.impl.RTFWidgetImpl;

public class RTFWidgetTest extends RTFWidgetImpl {

	@Test
	public void testAPI() {
		addToken("#", "{#}");
		IComposite c = addComposite();
		IComposite rc = c.addComposite("ReleaseCycle");
		rc.addComposite("id").token("ReleaseCycle.id");
		rc.addComposite("version").token("ReleaseCycle.version");
		IComposite rf = c.addComposite("ReleaseFolder");
		rf.addComposite("id").token("ReleaseFolder.id");
		rf.addComposite("version").token("ReleaseFolder.version");
		visible(true);
	}

	@Override
	public IRTFWidget visible(boolean visible) {
		System.out.println(this);
		return this;
	}

	@Override
	public String html() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IRTFWidget html(String html) {
		throw new MethodNotImplementedException();
	}
}
