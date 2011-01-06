package co.fxl.gui.api.template;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel.IGridCell;

public interface IScrollGrid {

	public interface ILazyGridDecorator {

		IElement<?> decorate(int column, int row, IGridCell cell);
	}

	IScrollGrid columns(int columns);

	IScrollGrid rows(int rows);

	IScrollGrid height(int height);

	IScrollGrid decorator(ILazyGridDecorator decorator);

	IScrollGrid visible(boolean visible);
}
