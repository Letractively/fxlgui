package co.fxl.gui.automation.api;

import co.fxl.gui.automation.api.IAutomationListener.Key;

public interface ILazyTreeWidgetListener<T> {

	void notifyClick(T tree, int row, int px, Key key);

}