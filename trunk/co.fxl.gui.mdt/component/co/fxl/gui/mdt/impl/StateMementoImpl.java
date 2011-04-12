package co.fxl.gui.mdt.impl;

import co.fxl.gui.mdt.api.IStateMemento;

class StateMementoImpl implements IStateMemento {

	boolean showDetailView;

	StateMementoImpl(MasterDetailTableWidgetImpl widget) {
		showDetailView = widget.activeView instanceof DetailView;
	}
}
