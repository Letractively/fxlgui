package co.fxl.gui.api;

public interface IRadioButton extends IFocusable<co.fxl.gui.api.IRadioButton>,
    ITextElement<co.fxl.gui.api.IRadioButton> {
    IGroup group();

    IRadioButton checked(boolean checked);

    boolean checked();

    IRadioButton addUpdateListener(
        IUpdateable.IUpdateListener<java.lang.Boolean> updateListener);

    public interface IGroup {
        IGroup add(IRadioButton... buttons);
    }
}
