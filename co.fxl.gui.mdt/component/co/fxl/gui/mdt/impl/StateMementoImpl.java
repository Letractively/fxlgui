package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IStateMemento;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;

class StateMementoImpl implements IStateMemento {

	boolean showDetailView;
	IFilterConstraints constraints;
	String configuration;
	LinkedList<Object> selection;
	String registerSelection;
	Object relationRegisterSelection;
	List<String> hiddenColumns = new LinkedList<String>();

	StateMementoImpl(MasterDetailTableWidgetImpl widget) {
		showDetailView = widget.activeView instanceof DetailView;
		if (widget.activeView instanceof DetailView) {
			registerSelection = ((DetailView) widget.activeView).tree
					.activeDetailView();
			relationRegisterSelection = widget.relationRegisterSelection
					.get(registerSelection);
		} else {
			TableView tv = (TableView) widget.activeView;
			for (IScrollTableColumn<Object> c : tv.property2column.values()) {
				if (!c.visible()) {
					hiddenColumns.add(c.name());
				}
			}
		}
		constraints = widget.constraints;
		configuration = widget.configuration;
		selection = new LinkedList<Object>(widget.selection);
	}

	StateMementoImpl() {
	}

	@Override
	public IStateMemento selection(Object selection) {
		this.selection = new LinkedList<Object>();
		this.selection.add(selection);
		return this;
	}

	@Override
	public IStateMemento constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IStateMemento showDetailView() {
		this.showDetailView = true;
		return this;
	}
}
