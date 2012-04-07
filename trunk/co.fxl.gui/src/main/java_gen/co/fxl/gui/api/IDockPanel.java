package co.fxl.gui.api;

public interface IDockPanel extends IPanel<co.fxl.gui.api.IDockPanel> {
    IDockPanel spacing(int pixel);

    IContainer center();

    IContainer top();

    IContainer left();

    IContainer right();

    IContainer bottom();
}
