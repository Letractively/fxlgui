package co.fxl.gui.swing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ILocalStorage;

class SwingLocalStorage implements ILocalStorage {

	static final SwingLocalStorage instance = new SwingLocalStorage();
	private Map<String, String> map = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	private SwingLocalStorage() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream("local.storage"));
			map = (Map<String, String>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			CallbackTemplate.rethrow(e);
		} catch (ClassNotFoundException e) {
			CallbackTemplate.rethrow(e);
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					ObjectOutputStream oos = new ObjectOutputStream(
							new FileOutputStream("local.storage"));
					oos.writeObject(map);
					oos.flush();
					oos.close();
				} catch (FileNotFoundException e) {
					CallbackTemplate.rethrow(e);
				} catch (IOException e) {
					CallbackTemplate.rethrow(e);
				}

			}
		});
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}

	@Override
	public ILocalStorage put(String key, String value) {
		map.put(key, value);
		return this;
	}

}
