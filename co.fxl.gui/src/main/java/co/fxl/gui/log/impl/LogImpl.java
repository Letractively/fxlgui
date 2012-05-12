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
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.log.api.ILog;

class LogImpl implements ILog, IClickListener {

	private static final int MAX_SIZE = 500;

	private class Entry {

		private Date date = new Date();
		private String level;
		private String message;
		private Long duration;
		private Throwable[] stacktrace;

		public Entry(String string, String message) {
			level = string;
			this.message = message;
		}

		public Entry(String string, String message, long duration) {
			this(string, message);
			this.duration = duration;
		}

		public Entry(String string, String message, long duration,
				Throwable... stacktrace) {
			this(string, message, duration);
			this.stacktrace = stacktrace;
		}

	}

	private static final int SPACING = 20;
	private List<Entry> lines = new LinkedList<Entry>();
	private Map<String, Long> timestamps = new HashMap<String, Long>();
	protected Entry details;
	private CommandLink cancel;

	@Override
	public ILog container(IContainer c) {
		c.label().text("Trace").hyperlink().addClickListener(this);
		return this;
	}

	@Override
	public ILog debug(String message) {
		ensureSize();
		lines.add(new Entry("DEBUG", message));
		return this;
	}

	private void ensureSize() {
		if (lines.size() > MAX_SIZE)
			lines.remove(0);
	}

	@Override
	public void onClick() {
		IDisplay d = Display.instance();
		final IPopUp popUp = d.showPopUp()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2)
				.offset(SPACING, SPACING).modal(true);
		popUp.border().remove().style().shadow().color().black();
		WidgetTitle panel = new WidgetTitle(popUp.container()).spacing(0)
				.sideWidget(true).commandsOnTop();
		panel.addTitle("Log Trace");
		final IScrollPane scrollPane = panel.content().scrollPane()
				.size(d.width() - SPACING * 2, d.height() - SPACING * 2 - 33);
		final IVerticalPanel content = scrollPane.viewPort().panel().vertical()
				.spacing(10).add().panel().vertical();
		cancel = panel.addHyperlink("cancel.png", "Clear");
		cancel.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				lines.clear();
				content.clear();
			}
		});
		panel.addHyperlink("back.png", "Close").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						if (details == null)
							popUp.visible(false);
						else {
							details = null;
							showLog(scrollPane, content);
						}
					}
				});
		showLog(scrollPane, content);
		popUp.visible(true);
	}

	private void showLog(final IScrollPane scrollPane,
			final IVerticalPanel content) {
		cancel.visible(true);
		content.clear();
		if (lines.size() > 0) {
			IGridPanel g = content.add().panel().grid().spacing(4);
			int i = 0;
			ILabel lbl = null;
			for (final Entry l : lines) {
				lbl = g.cell(0, i).label();
				addDate(l, lbl);
				ILabel ll = g.cell(1, i).label();
				addLevel(l, ll);
				ILabel lm = g.cell(2, i).label();
				addMessage(l, lm);
				ILabel ld = g.cell(3, i).align().end().label();
				addDuration(l, ld);
				if (l.stacktrace != null)
					g.cell(4, i).align().end().label().text("Stacktrace")
							.hyperlink().addClickListener(new IClickListener() {
								@Override
								public void onClick() {
									showException(scrollPane, content, l);
								}
							}).mouseLeft().font().pixel(11);
				i++;
			}
			g.column(2).expand();
			scrollPane.scrollIntoView(content.add().label());
		}
	}

	private void addDuration(Entry l, ILabel ld) {
		ld.text(l.duration != null ? l.duration + "ms" : "").font().pixel(11)
				.weight().bold();
	}

	private void addMessage(Entry l, ILabel lm) {
		lm.text(l.message).font().pixel(12).family().courier();
	}

	private void addLevel(Entry l, ILabel ll) {
		ll.text(l.level).font().pixel(11).weight().bold();
	}

	private void addDate(Entry l, ILabel lbl) {
		lbl.text(l.date.toString());
		lbl.font().pixel(11).color().gray();
	}

	private void showException(IScrollPane p, IVerticalPanel content, Entry l) {
		cancel.visible(false);
		details = l;
		content.clear();
		IHorizontalPanel h = content.add().panel().horizontal().spacing(4);
		addLevel(l, h.add().label());
		addMessage(l, h.add().label());
		addDate(l, h.add().label());
		addDuration(l, h.add().label());
		StringBuilder b = new StringBuilder();
		for (Throwable t : l.stacktrace)
			if (t != null)
				addException(t, b);
		ITextArea ta = content.add().textArea()
				.size(p.width() - 40, p.height() - 60).text(b.toString())
				.editable(false);
		ta.border().width(1);
		Heights.INSTANCE.decorate(ta);
	}

	private void addException(Throwable stacktrace, StringBuilder b) {
		b.append(stacktrace.toString() + "\n");
		// content.add().label().text(stacktrace.toString()).font().weight()
		// .bold().family().courier();
		for (StackTraceElement e : stacktrace.getStackTrace()) {
			b.append("\t" + e.toString() + "\n");
			// ILabel l = content.add().label().text(e.toString());
			// l.margin().left(10);
			// l.font().family().courier();
		}
		if (stacktrace.getCause() != null) {
			addException(stacktrace.getCause(), b);
		}
	}

	@Override
	public ILog start(String message) {
		timestamps.put(message, System.currentTimeMillis());
		return this;
	}

	@Override
	public ILog stop(String message) {
		if (timestamps.containsKey(message))
			debug(message,
					(System.currentTimeMillis() - timestamps.remove(message)));
		return this;
	}

	@Override
	public ILog debug(String message, long duration) {
		ensureSize();
		lines.add(new Entry("DEBUG", message, duration));
		return this;
	}

	@Override
	public ILog debug(String message, long duration, Throwable... stacktrace) {
		ensureSize();
		lines.add(new Entry("DEBUG", message, duration, stacktrace));
		return this;
	}

	@Override
	public ILog deobfuscator(IDeobfuscator deobfuscator) {
		throw new UnsupportedOperationException();
	}
}
