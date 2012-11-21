package co.fxl.gui.automation.api;

import co.fxl.gui.automation.api.IAutomationListener.Key;

public interface ILazyTreeWidgetListener<T> {

	void notifyNewTree(T tree);

	void notifyClick(T tree, int row, int px, Key key);

}