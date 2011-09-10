package co.fxl.gui.tree.impl;

import co.fxl.gui.style.impl.Style;

privileged aspect TreeWidgetImplStyle {

	after(TreeWidgetImpl widget) :
	execution(private void TreeWidgetImpl.setUpRegisters()) 
	&& this(widget) 
	&& if(Style.ENABLED) {
		Style.instance().tree().panel(widget.panel);
	}
}
