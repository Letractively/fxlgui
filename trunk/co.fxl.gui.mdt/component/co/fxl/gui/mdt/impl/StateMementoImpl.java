package co.fxl.gui.mdt.impl;

import java.util.LinkedList;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IStateMemento;

class StateMementoImpl implements IStateMemento {

	boolean showDetailView;
	IFilterConstraints constraints;
	String configuration;
	LinkedList<Object> selection;

	StateMementoImpl(MasterDetailTableWidgetImpl widget) {
		showDetailView = widget.activeView instanceof DetailView;
		constraints = widget.constraints;
		configuration = widget.configuration;
		selection = new LinkedList<Object>(widget.selection);
	}
}
