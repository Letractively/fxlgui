package co.fxl.gui.swing;

import java.awt.event.MouseEvent;

import co.fxl.gui.impl.ElementListener;

public aspect SwingElementAutomation {

	@SuppressWarnings("rawtypes")
	after(SwingElement e) :
	execution(SwingElement.new(SwingContainer))
	&& this(e)
	&& if(ElementListener.active) {
		ElementListener.instance().notifyNew(e);
	}

	@SuppressWarnings("rawtypes")
	void around(SwingElement e) :
	execution(void SwingElement.fireClickListeners(MouseEvent))
	&& this(e)
	&& if(ElementListener.active) {
		boolean execute = ElementListener.instance().notifyClick(e);
		if (execute)
			proceed(e);
	}

}
