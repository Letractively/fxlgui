package co.fxl.gui.table.scroll.impl;

import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;

public interface TableWidgetAdp {

	IGridPanel statusPanel();

	boolean updateTable();

	List<ScrollTableColumnImpl> columnList();

	void addClickListeners(boolean addClickListeners);

	void refreshTable();

	IContainer getContainer();

}
