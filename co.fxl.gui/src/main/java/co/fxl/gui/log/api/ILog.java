package co.fxl.gui.log.api;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;

public interface ILog {

	public interface IMeasurement {

		ILog stop();
	}

	public interface IDeobfuscator {

		void deobfuscate(Throwable t, ICallback<String> deobfuscated);
	}

	IMeasurement start(String message);

	ILog debug(String message);

	ILog test(String message);

	ILog debug(String message, long duration);

	ILog debug(String message, long duration, Throwable clientStacktrace,
			Throwable serverStacktrace);

	ILog deobfuscator(IDeobfuscator deobfuscator);

	ILog container(IContainer c);

	ILog error(String string);

	ILog warn(String string);

	ILog warn(String string, Throwable exception);

	ILog error(String message, Throwable pThrowable);

}
