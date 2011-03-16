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
	private List<INavigationLinkSelectionListener<Object>> selectionListeners = new LinkedList<INavigationLinkSelectionListener<Object>>();
	boolean requiresSelection = true;
	Class<?> typeConstraint;
	String imageResource;
	private Link label;
	private boolean requiresRows = false;

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
		if (widget.rowsInTable == 0 && requiresRows)
			clickable = false;
		clickable(clickable);
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

	@Override
	public INavigationLink<Object> requiresRows(boolean requiresRows) {
		this.requiresRows = requiresRows;
		return this;
	}

	@Override
	public INavigationLink<Object> text(String text) {
		this.name = text;
		if (label != null)
			label.text(text);
		return this;
	}

	@Override
	public INavigationLink<Object> addSelectionListener(
			co.fxl.gui.mdt.api.INavigationLink.INavigationLinkSelectionListener<Object> l) {
		selectionListeners.add(l);
		return this;
	}

	void notifySelection(List<Object> selection) {
		for (INavigationLinkSelectionListener<Object> l : selectionListeners)
			l.onUpdate(this, selection);
	}

	@Override
	public INavigationLink<Object> clickable(boolean b) {
		label.clickable(b);
		return this;
	}
}
