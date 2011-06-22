package co.fxl.gui.tree.impl;

import co.fxl.gui.tree.api.ITree;

interface NodeRef<T> {

	ITree<T> tree();

	void selected(boolean selected);

	NodeRef<T> refresh(boolean recurse);

}
