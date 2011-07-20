package co.fxl.gui.form.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.style.impl.Style;

privileged aspect LoginWidgetImplStyle {

	after(ILabel label) :
	execution(private ILabel LoginWidgetImpl.decorate(ILabel)) 
	&& args(label) 
	&& if(Style.ENABLED) {
		label.font().color().mix().gray().lightgray();
	}

	void around(ILabel label) :
	execution(void LoginWidgetImpl.hyperlink(ILabel)) 
	&& args(label) 
	&& if(Style.ENABLED) {
		label.font().weight().bold().color().white();
	}
}
