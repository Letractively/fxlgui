package co.fxl.gui.i18n;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.ITextElement;

public class I18N {

	private static final I18N INSTANCE = new I18N();
	private Map<String, String> translations = new HashMap<String, String>();

	private I18N() {
	}

	public void register(String text, String translation) {
		translations.put(text, translation);
	}

	public void translate(ITextElement<?> text) {
		text.text(translate(text.text()));
	}

	public String translate(String text) {
		String translation = translations.get(text);
		if (translation == null) {
			System.err.println("No translation found for: " + text);
			return text;
		}
		return translation;
	}

	public static I18N instance() {
		return INSTANCE;
	}
}
