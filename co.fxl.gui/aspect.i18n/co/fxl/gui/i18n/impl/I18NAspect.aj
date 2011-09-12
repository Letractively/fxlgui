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
package co.fxl.gui.i18n.impl;

import co.fxl.gui.api.ILabel;

public abstract aspect I18NAspect {

	String around() :
	get(@Translate static String *.*) 
	&& if(I18N.ENABLED) {
		return I18N.instance().translate(proceed());
	}

	ILabel around(String text) :
	call(* *.text(String))
	&& withincode(@Translate * *.*(..)) 
	&& args(text)
	&& if(I18N.ENABLED) {
		return proceed(I18N.instance().translate(text));
	}

	ILabel around(String text) :
	call(* ILabel.text(String))
	&& within(@Translate *) 
	&& args(text)
	&& if(I18N.ENABLED) {
		return proceed(I18N.instance().translate(text));
	}

}
