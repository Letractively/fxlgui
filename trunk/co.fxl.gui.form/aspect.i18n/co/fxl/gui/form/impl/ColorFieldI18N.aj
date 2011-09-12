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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.i18n.impl.Translate;
import co.fxl.gui.i18n.impl.I18NAspect;
import co.fxl.gui.i18n.impl.I18N;

public aspect ColorFieldI18N extends I18NAspect {

	declare @type : ColorField.PopUp : @Translate;

	IFont around(int pixel) :
	call(public IFont IFont.pixel(int)) 
	&& within(ColorField.PopUp) 
	&& args(pixel) 
	&& if(I18N.ENABLED) {
		return proceed(8);
	}
}
