package co.fxl.gui.api;

public interface IAbsolutePanel extends IPanel<IAbsolutePanel> {

	public interface IResizeListener {

		void onResize(int width, int height);

		void onResize(IElement<?> element, int width, int height);
	}

	IAbsolutePanel addResizeListener(IResizeListener listener);

	IAbsolutePanel offset(int x, int y);

	IAbsolutePanel offset(IElement<?> element, int x, int y);
}
