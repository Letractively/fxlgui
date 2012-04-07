package co.fxl.gui.api;

public interface IPanelProvider<T extends co.fxl.gui.api.IPanel<T>> {
    Class<T> panelType();

    T createPanel(IContainer container);
}
