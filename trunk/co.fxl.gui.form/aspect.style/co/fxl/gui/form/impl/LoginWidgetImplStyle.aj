package co.fxl.gui.form.impl;


privileged aspect LoginWidgetImplStyle {

	// after(ILabel label) :
	// execution(private ILabel LoginWidgetImpl.decorate(ILabel))
	// && args(label)
	// && if(Style.ENABLED) {
	// Style.instance().login().label(label);
	// }
	//
	// void around(ILabel label) :
	// execution(void LoginWidgetImpl.hyperlink(ILabel))
	// && args(label)
	// && if(Style.ENABLED) {
	// Style.instance().login().hyperlink(label);
	// }
}
