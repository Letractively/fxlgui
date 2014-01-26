package co.fxl.gui.layout.api;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.impl.IToolbar;

public interface IMDTLayoutDecorator {

	IClickable<?> decorate(IToolbar c);
}