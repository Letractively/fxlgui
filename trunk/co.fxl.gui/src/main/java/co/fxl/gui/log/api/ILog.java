package co.fxl.gui.log.api;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;

public interface ILog {

	public interface IDeobfuscator {

		void deobfuscate(Throwable t, ICallback<String> deobfuscated);
	}

	ILog start(String message);

	ILog stop(String message);

	ILog debug(String message);

	ILog test(String message);

	ILog debug(String message, long duration);

	ILog debug(String message, long duration, Throwable clientStacktrace,
			Throwable serverStacktrace);

	ILog deobfuscator(IDeobfuscator deobfuscator);

	ILog container(IContainer c);

	ILog error(String string);

	ILog warn(String string);

	ILog warn(String string, Exception exception);

}
