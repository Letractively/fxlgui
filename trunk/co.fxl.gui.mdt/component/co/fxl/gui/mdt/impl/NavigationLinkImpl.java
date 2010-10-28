package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.mdt.api.INavigationLink;

class NavigationLinkImpl implements INavigationLink<Object> {

	String name;
	boolean inTable = true;
	boolean asDetail = false;
	List<INavigationLinkListener<Object>> listeners = new LinkedList<INavigationLinkListener<Object>>();

	NavigationLinkImpl(String name) {
		this.name = name;
	}

	@Override
	public INavigationLink<Object> addClickListener(
			INavigationLinkListener<Object> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public INavigationLink<Object> asDetail(boolean asDetail) {
		this.asDetail = asDetail;
		return this;
	}

	@Override
	public INavigationLink<Object> inTable(boolean inTable) {
		this.inTable = inTable;
		return this;
	}

}
