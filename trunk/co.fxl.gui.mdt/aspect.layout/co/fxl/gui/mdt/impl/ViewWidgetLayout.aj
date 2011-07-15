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
package co.fxl.gui.mdt.impl;

import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.layout.impl.Layout;
import co.fxl.gui.mdt.api.IViewConfiguration;

public aspect ViewWidgetLayout {

	after(ViewWidget viewWidget) : 
	execution(public ViewWidget.new(..)) 
	&& this(viewWidget) {
		viewWidget.addUpdateListener(new IUpdateListener<IViewConfiguration>() {
			@Override
			public void onUpdate(IViewConfiguration value) {
				Layout.instance().actionMenu().showContent();
			}
		});
	}
}
