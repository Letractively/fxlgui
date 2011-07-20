package co.fxl.gui.tree.impl;

privileged aspect TreeWidgetImplStyle {

	after(TreeWidgetImpl widget) :
	execution(private void TreeWidgetImpl.setUpRegisters()) 
	&& this(widget) {
		widget.panel.color().remove();
	}
}
