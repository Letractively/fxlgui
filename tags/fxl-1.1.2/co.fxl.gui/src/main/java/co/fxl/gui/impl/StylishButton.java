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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;

public class StylishButton {

	public static IClickable<?> add(IHorizontalPanel p, String text) {
		IHorizontalPanel buttonPanel = p.spacing(3);
		buttonPanel.color().rgb(111, 111, 111).gradient().vertical()
				.rgb(63, 63, 63);
		IBorder br = buttonPanel.border();
		br.color().black();
		br.style().rounded();
		ILabel b = buttonPanel.addSpace(4).add().label().text(text);
		b.font().weight().bold().color().white();
		buttonPanel.addSpace(4);
		return new ClickableMultiplexer(buttonPanel, b);
	}
}
