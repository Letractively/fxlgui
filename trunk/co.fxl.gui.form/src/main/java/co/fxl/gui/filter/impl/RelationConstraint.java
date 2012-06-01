package co.fxl.gui.filter.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.filter.impl.IFilterConstraint.IRelationConstraint;

public class RelationConstraint implements IRelationConstraint {

	public List<Object> values = new LinkedList<Object>();
	public String name;
	public String toString;

	public RelationConstraint(String name, List<Object> values, String toString) {
		this.name = name;
		this.values = values;
		this.toString = toString;
	}

	@Override
	public List<Object> values() {
		return values;
	}

	@Override
	public String column() {
		return name;
	}

	@Override
	public String toString() {
		return toString;
	}
}