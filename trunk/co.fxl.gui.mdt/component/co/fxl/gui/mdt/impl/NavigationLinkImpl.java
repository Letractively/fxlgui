package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.template.NavigationView.Link;
import co.fxl.gui.mdt.api.INavigationLink;

class NavigationLinkImpl implements INavigationLink<Object> {

	String name;
	boolean inTable = true;
	boolean asDetail = true;
	List<INavigationLinkListener<Object>> listeners = new LinkedList<INavigationLinkListener<Object>>();
	boolean requiresSelection = true;
	Class<?> typeConstraint;
	String imageResource;
	private Link label;

	NavigationLinkImpl(String name) {
		this.name = name;
	}

	void setLabel(Link label) {
		this.label = label;
		if (requiresSelection || typeConstraint != null)
			label.clickable(false);
	}

	void updateLabel(MasterDetailTableWidgetImpl widget) {
		boolean clickable = (!requiresSelection && typeConstraint == null)
				|| (!widget.selection.isEmpty() && (typeConstraint == null || typeConstraint
						.equals(widget.selection.get(0).getClass())));
		if (widget.activeView instanceof DetailView && !asDetail)
			clickable = false;
		if (widget.activeView instanceof TableView && !inTable)
			clickable = false;
		label.clickable(clickable);
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

	@Override
	public INavigationLink<Object> requiresSelection(boolean requiresSelection) {
		this.requiresSelection = requiresSelection;
		return this;
	}

	@Override
	public INavigationLink<Object> typeConstraint(Class<?> typeConstraint) {
		this.typeConstraint = typeConstraint;
		return this;
	}

	@Override
	public INavigationLink<Object> imageResource(String imageResource) {
		this.imageResource = imageResource;
		return this;
	}

}
