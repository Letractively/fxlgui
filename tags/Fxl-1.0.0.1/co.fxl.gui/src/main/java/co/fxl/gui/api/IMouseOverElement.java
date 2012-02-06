package co.fxl.gui.api;

public interface IMouseOverElement<T> {

	public interface IMouseOverListener {

		void onMouseOver();

		void onMouseOut();
	}

	T addMouseOverListener(IMouseOverListener l);
}
