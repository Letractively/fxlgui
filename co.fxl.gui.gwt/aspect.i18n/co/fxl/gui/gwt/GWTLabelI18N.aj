package co.fxl.gui.gwt;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.impl.I18N;

aspect GWTLabelI18N {

	ILabel around(String text) :
	execution(public ILabel GWTLabel.text(String)) 
	&& args(text)
	&& if(I18N.ENABLED) {
		return proceed(I18N.instance().translate(text));
	}
}
