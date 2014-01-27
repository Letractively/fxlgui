package co.fxl.gui.api;

public interface IHasCursor<T> {

	int cursorPosition();

	T cursorPosition(int position);

}
