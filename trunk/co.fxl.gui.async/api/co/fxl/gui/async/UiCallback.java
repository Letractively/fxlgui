package co.fxl.gui.async;

public abstract class UiCallback<T> implements ICallback<T> {

	public void onFail(Throwable throwable) {
		// TODO: show error in ui
	}
	
}
