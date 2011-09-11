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
package co.fxl.gui.i18n.german;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.impl.I18NTemplate;

public class GermanI18N extends I18NTemplate {

	private static final long serialVersionUID = -432280219994971029L;

	public GermanI18N() {
		put("Back", "Zur\u00fcck");
		put("Forward", "Vorw\u00e4rts");
		put("Navigation", "Navigation");
		put("ID", "ID");
		put("Password", "Passwort");
	}

	@Override
	public void addHelp(final String iD, final ILabel label) {
		// label.addMouseOverListener(new IMouseOverListener() {
		//
		// private IPopUp popUp;
		//
		// @Override
		// public void onMouseOver() {
		// popUp = label.display().showPopUp();
		// popUp.container().label().text("help for " + iD);
		// popUp.visible(true);
		// }
		//
		// @Override
		// public void onMouseOut() {
		// popUp.visible(false);
		// }
		// });
	}
}
