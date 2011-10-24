package co.fxl.gui.table.flex.swing;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.swing.PanelComponent;
import co.fxl.gui.swing.SwingContainer;
import co.fxl.gui.swing.SwingPanel;
import co.fxl.gui.table.flex.IFlexGridPanel;

public class SwingFlexGridPanel extends SwingPanel<IFlexGridPanel> implements
		IFlexGridPanel {

	private class FlexCell implements IFlexCell {

		private int x;
		private int y;

		FlexCell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public IFlexCell width(int columns) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IFlexCell height(int rows) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IContainer container() {
			throw new MethodNotImplementedException();
		}

	}

	@SuppressWarnings("unchecked")
	SwingFlexGridPanel(IContainer container) {
		super((SwingContainer<PanelComponent>) container);
	}

	@Override
	public IFlexCell cell(int x, int y) {
		return new FlexCell(x, y);
	}

}
