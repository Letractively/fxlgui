package co.fxl.gui.filter.impl;

import co.fxl.gui.filter.impl.IFilterConstraint.ISizeConstraint;

class SizeConstraint implements ISizeConstraint {

	private int text;

	SizeConstraint(int text) {
		this.text=text;
	}

	@Override
	public int size() {
		return text;
	}

	@Override
	public String toString() {
		return String.valueOf(text);
	}
}