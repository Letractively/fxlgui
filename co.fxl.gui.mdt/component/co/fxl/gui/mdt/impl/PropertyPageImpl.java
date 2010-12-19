package co.fxl.gui.mdt.impl;

import co.fxl.gui.mdt.api.IPropertyPage;

class PropertyPageImpl implements IPropertyPage<Object> {

	IDecorator<Object> dec;
	String name;
	Class<?> clazz;

	PropertyPageImpl(String name) {
		this.name = name;
	}

	@Override
	public IPropertyPage<Object> decorator(
			co.fxl.gui.mdt.api.IPropertyPage.IDecorator<Object> dec) {
		this.dec = dec;
		return this;
	}

	@Override
	public IPropertyPage<Object> typeConstraint(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

}
