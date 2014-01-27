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
package co.fxl.gui.swing;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.DialogImpl;

class SwingDialog extends DialogImpl {

	SwingDialog() {
		super();
	}

	@Override
	protected void createLabel(IContainer content) {
		if (width != -1) {
			IVerticalPanel grid = content.panel().vertical();
			IHorizontalPanel p = grid.add().panel().horizontal().spacing(10);
			int height = 16 * (1 + (message.length() / 64));
			p.add().image().resource(image(type)).size(16, 16);
			ITextArea ta = p.add().textArea().text(message);
			ta.width(width - 3 * 10 - 16);
			ta.height(height);
		} else
			super.createLabel(content);
	}

}
