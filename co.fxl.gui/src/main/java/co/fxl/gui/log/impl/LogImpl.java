package co.fxl.gui.log.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;
import co.fxl.gui.log.api.ILog;

class LogImpl implements ILog, IClickListener {

	private static final int SPACING = 20;
	private List<String> lines = new LinkedList<String>();

	@Override
	public ILog container(IContainer c) {
		c.button().text("Log").addClickListener(this);
		return this;
	}

	@Override
	public ILog debug(String message) {
		lines.add(new Date().toString() + " DEBUG: " + message);
		return this;
	}

	@Override
	public void onClick() {
		IDisplay d = Display.instance();
		final IPopUp popUp = d.showPopUp()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2)
				.offset(SPACING, SPACING);
		IVerticalPanel panel = popUp.container().scrollPane().viewPort()
				.panel().vertical().spacing(10);
		panel.align().end().add().button().text("Close")
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
					}
				});
		for (String l : lines)
			panel.add().label().text(l);
		popUp.visible(true);
	}

}
