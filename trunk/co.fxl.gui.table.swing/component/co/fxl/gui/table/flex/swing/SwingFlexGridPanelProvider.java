package co.fxl.gui.table.flex.swing;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPanelProvider;
import co.fxl.gui.table.flex.IFlexGridPanel;

public class SwingFlexGridPanelProvider implements
		IPanelProvider<IFlexGridPanel> {

	@Override
	public Class<IFlexGridPanel> layoutType() {
		return IFlexGridPanel.class;
	}

	@Override
	public IFlexGridPanel createPanel(IContainer container) {
		return new SwingFlexGridPanel(container);
	}

}
