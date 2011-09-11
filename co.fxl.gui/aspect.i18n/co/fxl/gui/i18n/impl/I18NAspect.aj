package co.fxl.gui.i18n.impl;

import co.fxl.gui.api.ILabel;

public abstract aspect I18NAspect {

	String around() :
	get(@Translate static String *.*) 
	&& if(I18N.ENABLED) {
		return I18N.instance().translate(proceed());
	}

	ILabel around(String text) :
	call(* *.text(String))
	&& withincode(@Translate * *.*(..)) 
	&& args(text)
	&& if(I18N.ENABLED) {
		return proceed(I18N.instance().translate(text));
	}

	ILabel around(String text) :
	call(* *.text(String))
	&& within(@Translate *) 
	&& args(text)
	&& if(I18N.ENABLED) {
		return proceed(I18N.instance().translate(text));
	}

}
