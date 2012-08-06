package co.fxl.gui.table.bulk.impl;

import co.fxl.gui.api.IGridPanel.IGridClickListener;
import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.automation.impl.Automation;

class AutomationGridClickListener implements IGridClickListener {

	private BulkTableWidgetImpl e;
	private Key key;

	AutomationGridClickListener(BulkTableWidgetImpl e, Key key) {
		this.e = e;
		this.key = key;
	}

	@Override
	public void onClick(int column, int row) {
		if (Automation.ENABLED) {
			Automation.listener().notifyClick(e.grid, column, row, key);
		}
	}

}
