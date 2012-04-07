package co.fxl.gui.api;

public interface IFlowPanel extends IPanel<co.fxl.gui.api.IFlowPanel> {
    IFlowPanel spacing(int spacing);

    IFlowPanel addSpace(int space);

    IAlignment<co.fxl.gui.api.IFlowPanel> align();
}
