package co.fxl.gui.layout.api;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILinearPanel;


public interface IActionMenuLayout {
    IActionMenuLayout sidePanel(ILinearPanel<?> panel);

    IActionMenuLayout grid(IGridPanel grid);

    IActionMenuLayout container(IContainer container);

    IActionMenuLayout showContent();
}
