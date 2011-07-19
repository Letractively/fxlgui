package co.fxl.gui.style.api;

import co.fxl.gui.api.IPanel;

public interface IWindow {

	IWindow main(IPanel<?> panel);

	IWindow header(IPanel<?> panel);

	IWindow conent(IPanel<?> panel);

	IWindow footer(IPanel<?> panel);
}
