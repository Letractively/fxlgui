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
package co.fxl.gui.navigation.group.impl;

import co.fxl.gui.api.IImage;
import co.fxl.gui.style.impl.Style;

privileged aspect NavigationItemImplStyle {

	after(NavigationItemImpl item) :
	execution(void NavigationItemImpl.showLabelAsInactive(boolean)) 
	&& this(item) 
	&& if(Style.ENABLED) {
		Style.instance().navigation().group().item()
				.inactive(item.buttonPanel, item.button);
	}

	after(NavigationItemImpl item) :
	execution(private void NavigationItemImpl.showLabelAsActive(..)) 
	&& this(item) 
	&& if(Style.ENABLED) {
		Style.instance().navigation().group().item()
				.active(item.buttonPanel, item.button);
	}

	IImage around(String resource) :
	call(public IImage IImage.resource(String))
	&& within(NavigationItemImpl)
	&& args(resource)
	&& if(Style.ENABLED) {
		return proceed(Style.instance().navigation().group().item().image(resource));
	}
}
