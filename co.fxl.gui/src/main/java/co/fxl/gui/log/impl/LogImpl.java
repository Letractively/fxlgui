package co.fxl.gui.log.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.log.api.ILog;

class LogImpl implements ILog, IClickListener {

	private static final int MAX_SIZE = 500;

	private class Entry {

		private Date date = new Date();
		private String level;
		private String message;

		public Entry(String string, String message) {
			level = string;
			this.message = message;
		}

	}

	private static final int SPACING = 20;
	private List<Entry> lines = new LinkedList<Entry>();
	private Map<String, Long> timestamps = new HashMap<String, Long>();

	@Override
	public ILog container(IContainer c) {
		c.label().text("Trace").hyperlink().addClickListener(this);
		return this;
	}

	@Override
	public ILog debug(String message) {
		if (lines.size() > MAX_SIZE)
			lines.remove(0);
		lines.add(new Entry("DEBUG", message));
		return this;
	}

	@Override
	public void onClick() {
		IDisplay d = Display.instance();
		final IPopUp popUp = d.showPopUp()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2)
				.offset(SPACING, SPACING).modal(true);
		popUp.border().remove().style().shadow().color().black();
		WidgetTitle panel = new WidgetTitle(popUp.container()).spacing(0).sideWidget(true)
				.commandsOnTop();
		panel.addTitle("Logging Trace");
		panel.addHyperlink("cancel.png", "Close").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
					}
				});
		IVerticalPanel content = panel
				.content()
				.scrollPane()
				.size(d.width() - SPACING * 2,
						d.height() - SPACING * 2 - panel.headerPanel().height())
				.viewPort().panel().vertical().spacing(10).add().panel()
				.vertical();
		if (lines.size() > 0) {
			IGridPanel g = content.add().panel().grid().spacing(4);
			int i = 0;
			for (Entry l : lines) {
				g.cell(0, i).label().text(l.date.toString()).font().pixel(11)
						.color().gray();
				g.cell(1, i).label().text(l.level).font().pixel(11).weight()
						.bold();
				g.cell(2, i).label().text(l.message).font().pixel(12).family()
						.courier();
				i++;
			}
			g.column(2).expand();
		}
		popUp.visible(true);
	}

	@Override
	public ILog start(String message) {
		timestamps.put(message, System.currentTimeMillis());
		return this;
	}

	@Override
	public ILog stop(String message) {
		if (timestamps.containsKey(message))
			debug(message + " required "
					+ (System.currentTimeMillis() - timestamps.remove(message))
					+ "ms");
		return this;
	}
}
