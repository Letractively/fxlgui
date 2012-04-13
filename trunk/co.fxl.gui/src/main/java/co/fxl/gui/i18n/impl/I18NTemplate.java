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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.i18n.api.II18N;

@SuppressWarnings("serial")
public class I18NTemplate extends HashMap<String, String> implements II18N {

	// private Set<String> constants = new HashSet<String>();

	private boolean active = true;
	private List<String> constants = new LinkedList<String>();
	private Map<String, String> rules = new HashMap<String, String>();

	protected I18NTemplate() {
	}

	protected void dontTranslate(String string) {
		put(string, string);
	}

	protected void dontTranslateIgnoreCase(String string) {
		putIgnoreCase(string, string);
	}

	@Override
	public String translate(String text) {
		if (!active)
			return text;
		if (text.length() <= 1)
			return text;
		if (Character.isDigit(text.charAt(0)))
			return text;
		String translation = get(text);
		if (translation == null) {
			// translation = translateComposite(text);
			// if (translation == null) {
			String e = "no translation found for '" + text + "'";
			System.err.println(e);
			throw new RuntimeException(e);
			// return text;
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

	// @Override
	// public void addHelp(String iD, ILabel label) {
	// }

	@Override
	public String put(String text, String translation) {
		super.put(text.toUpperCase(), translation.toUpperCase());
		return super.put(text, translation);
	}

	@Override
	public boolean active(boolean active) {
		boolean state = this.active;
		this.active = active;
		return state;
	}

	// @Override
	public void addConstant(String token) {
		dontTranslate(token);
		constants.add(token);
		handleRules(token);
		if (subString(token) != null)
			handleRules(subString(token));
	}

	private String subString(String token) {
		if (!token.contains(" "))
			return null;
		int c = 0;
		for (int i = 0; i < token.length(); i++)
			if (token.charAt(i) == ' ')
				c++;
		if (c >= 2) {
			String substring = token
					.substring(token.indexOf(" ", token.indexOf(" ") + 1) + 1);
			return substring;
		}
		return token.substring(token.indexOf(" ") + 1);
	}

	private void handleRules(String token) {
		for (String r : rules.keySet()) {
			handleRule(token, r, rules.get(r));
		}
	}

	private void handleRule(String token, String template,
			String translationTemplate) {
		put(template.replace("$", token),
				translationTemplate.replace("$", token));
	}

	// @Override
	public void addRule(String template, String translationTemplate) {
		rules.put(template, translationTemplate);
		for (String c : constants) {
			handleRule(c, template, translationTemplate);
		}
	}

	protected void putIgnoreCase(String string, String string2) {
		put(string, string2);
		put(string.toUpperCase(), string2.toUpperCase());
	}

//	@Override
//	public II18N notifyEvent(String event) {
//		return this;
//	}

	// @Override
	// public void addConstant(String token) {
	// constants.add(token);
	// }

}
