package co.fxl.gui.api;

public interface IToggleButton extends ITextElement<IToggleButton>,
		IUpdateable<Boolean> {

	IToggleButton down(boolean down);

	boolean down();
}
