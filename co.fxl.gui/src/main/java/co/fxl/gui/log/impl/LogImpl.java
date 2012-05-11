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

	private static final int SPACING = 40;
	private List<String> lines = new LinkedList<String>();

	@Override
	public ILog container(IContainer c) {
		c.label().text("Show Log").hyperlink().addClickListener(this);
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
		popUp.border().style().shadow();
		IVerticalPanel panel = popUp.container().scrollPane()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2)
				.viewPort().panel().vertical().spacing(2);
		panel.align().end().add().panel().horizontal().add().button()
				.text("Close").addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
					}
				});
		panel.align().begin();
		for (String l : lines)
			panel.add().label().text(l);
		popUp.visible(true);
	}

}
