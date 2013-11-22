package co.fxl.gui.api;

public interface IFrame extends IElement<IFrame>, IBordered {

	public interface ILoadListener {

		void onLoad();
	}

	IFrame uRI(String uRI);

	IFrame addLoadListener(ILoadListener l);

}
