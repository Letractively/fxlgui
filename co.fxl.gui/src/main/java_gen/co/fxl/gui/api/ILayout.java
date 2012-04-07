package co.fxl.gui.api;

public interface ILayout {
    IHorizontalPanel horizontal();

    IVerticalPanel vertical();

    IFlowPanel flow();

    IGridPanel grid();

    IDockPanel dock();

    ICardPanel card();

    IFocusPanel focus();

    IAbsolutePanel absolute();

    IPanel<?> plugIn(Class<?> layoutType);
}
