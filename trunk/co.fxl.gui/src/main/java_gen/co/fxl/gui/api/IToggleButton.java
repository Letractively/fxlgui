package co.fxl.gui.api;

public interface IToggleButton extends IFocusable<co.fxl.gui.api.IToggleButton>,
    ITextElement<co.fxl.gui.api.IToggleButton>, IUpdateable<java.lang.Boolean> {
    IToggleButton down(boolean down);

    boolean down();
}
