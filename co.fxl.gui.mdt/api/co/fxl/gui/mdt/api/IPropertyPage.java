package co.fxl.gui.mdt.api;

import co.fxl.gui.api.IContainer;

public interface IPropertyPage<T> {

	interface IDecorator<T> {

		void decorate(IContainer container, T entity);
	}

	IPropertyPage<T> decorator(IDecorator<T> dec);
}
