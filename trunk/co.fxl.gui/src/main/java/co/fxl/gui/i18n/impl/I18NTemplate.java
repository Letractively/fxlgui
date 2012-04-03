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

import java.util.HashMap;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.api.II18N;

@SuppressWarnings("serial")
public class I18NTemplate extends HashMap<String, String> implements II18N {

	// private Set<String> constants = new HashSet<String>();

	private boolean active = true;

	protected I18NTemplate() {
	}

	@Override
	public String translate(String text) {
		if (!active)
			return text;
		String translation = get(text);
		if (translation == null) {
			// translation = translateComposite(text);
			// if (translation == null) {
			String e = "no translation found for " + text;
			System.err.println(e);
			throw new RuntimeException(e);
//			return text;
			// }
		}
		return translation;
	}

	// private String translateComposite(String text) {
	// for (String key : keySet()) {
	// if (key.endsWith("$")) {
	// String prefix = key.substring(0, key.length() - 1);
	// if (text.startsWith(prefix)) {
	// String suffix = text.substring(prefix.length());
	// return get(key).replace("$", suffix);
	// }
	// }
	// }
	// return null;
	// }

	@Override
	public void addHelp(String iD, ILabel label) {
	}

	@Override
	public String put(String text, String translation) {
		super.put(text.toUpperCase(), translation.toUpperCase());
		return super.put(text, translation);
	}

	@Override
	public void active(boolean active) {
		this.active = active;
	}

	@Override
	public void addConstant(String token) {
		// TODO ... throw new UnsupportedOperationException();
	}

	@Override
	public void addRule(String template, String translationTemplate) {
		// TODO ... throw new UnsupportedOperationException();
	}

	// @Override
	// public void addConstant(String token) {
	// constants.add(token);
	// }

}
