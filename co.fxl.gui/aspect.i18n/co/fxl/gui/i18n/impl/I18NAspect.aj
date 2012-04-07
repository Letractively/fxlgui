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

import co.fxl.gui.i18n.api.DontTranslate;
import co.fxl.gui.i18n.api.Translate;
import co.fxl.gui.i18n.impl.I18N;

public abstract aspect I18NAspect {

	String around() :
	get(@Translate static String *.*)
	&& !within(I18NAspect+)
	&& if(I18N.ENABLED) {
		return I18N.instance().translate(proceed());
	}

	Object around() :
	execution(@Translate * *.*(..)) 
	&& if(I18N.ENABLED) {
		boolean activeState = I18N.instance().active(true);
		Object o = proceed();
		I18N.instance().active(activeState);
		return o;
	}

	Object around() :
	execution(@DontTranslate * *.*(..)) 
	&& if(I18N.ENABLED) {
		boolean activeState = I18N.instance().active(false);
		Object o = proceed();
		I18N.instance().active(activeState);
		return o;
	}
}
