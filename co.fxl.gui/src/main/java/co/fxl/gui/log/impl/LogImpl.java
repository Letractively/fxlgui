package co.fxl.gui.log.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;
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
		IVerticalPanel panel = popUp.container().scrollPane()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2)
				.viewPort().panel().vertical().spacing(10).add().panel()
				.vertical();
		IGridPanel grid = panel.add().panel().grid();
		grid.cell(0, 0).label().text("LOG").font().weight().bold().pixel(14);
		grid.cell(1, 0).align().end().label().text("[x] Close").hyperlink()
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
					}
				});
		panel.addSpace(2);
		if (lines.size() > 0) {
			IGridPanel g = panel.add().panel().grid().spacing(4);
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
}
