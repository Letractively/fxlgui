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
		put("Refresh", "Neu laden");
		put("Update", "Filter");
		put("Clear", "Zur\u00fccksetzen");
		put("Delete", "L\u00f6schen");
		put("New $", "Erzeuge $");
		put("Show $", "Zeige $");
		put("Compare $", "Vergleiche $");
		put("NAVIGATION", "NAVIGATION");
		put("VIEWS", "SICHTEN");
		put("FILTER", "FILTER");
		put("Compare", "Vergleichen");
		put("Compare History", "Vergleiche Verlauf");
		put("Generate Report", "Bericht erzeugen");
		put("Report", "Bericht");
		put("Export Grid", "Tabelle exportieren");
		put("Table", "Tabelle");
		put("Detail", "Details");
		put("Bulk Update", "Massen\u00e4nderung");
		put("Move", "Verschieben");
		put("Cut", "Ausschneiden");
		put("Copy", "Kopieren");
		put("Paste", "Einf\u00fcgen");
		put("Add", "Hinzuf\u00fcgen");
		put("Remove", "Entfernen");
		put("Save", "Speichern");
		put("Cancel", "Abbrechen");
		put("Show", "Anzeigen");
		put("Close", "Schlie\u00dfen");
		put("Assign Entities", "Entit\u00e4ten hinzuf\u00fcgen");
		put("Assign $", "F\u00fcge $ hinzu");
		put("Show Statistic", "Zeige Statistik");
		put("black", "schwarz");
		put("gray", "grau");
		put("maroon", "rotbraun");
		put("red", "rot");
		put("green", "gr\u00fcn");
		put("lime", "hellgr\u00fcn");
		put("olive", "olivgr\u00fcn");
		put("yellow", "gelb");
		put("navy", "marineblau");
		put("blue", "blau");
		put("purple", "lila");
		put("fuchsia", "pink");
		put("teal", "blaugr\u00fcn");
		put("aqua", "aquamarin");
		put("silver", "silber");
		put("white", "weiss");
		put("Technical Exception", "Technische Ausnahme");
		put("Error Details", "Fehlerdetails");
		put("Edit", "\u00c4ndern");
		put("Top", "Anfang");
		put("Down", "Abw\u00e4rts");
		put("Bottom", "Ende");
		put("Up", "Aufw\u00e4rts");
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
