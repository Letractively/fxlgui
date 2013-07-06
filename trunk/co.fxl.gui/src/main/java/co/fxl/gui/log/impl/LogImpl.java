package co.fxl.gui.log.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.FullscreenPopUp;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.log.api.ILog;
import co.fxl.gui.style.impl.Style;

class LogImpl implements ILog, IClickListener {

	// TODO FullscreenPopUp verwenden

	private class Measurement implements IMeasurement {

		private long start = System.currentTimeMillis();
		private String message;
		private boolean stopped;

		private Measurement(String message) {
			this.message = message;
		}

		@Override
		public ILog stop() {
			assert !stopped;
			stopped = true;
			return debug(message + " executed", System.currentTimeMillis()
					- start);
		}
	}

	private static final String WARNING = "WARNING";
	private static final String ERROR = "ERROR";

	private class Entry {

		private Date date = new Date();
		private String level;
		private String message;
		private Long duration;
		private Throwable clientStacktrace;
		private Throwable serverStacktrace;

		public Entry(String string, String message) {
			level = string;
			this.message = message;
		}

		public Entry(String string, String message, Long duration) {
			this(string, message);
			this.duration = duration;
		}

		public Entry(String string, String message, Long duration,
				Throwable clientStacktrace, Throwable serverStacktrace) {
			this(string, message, duration);
			this.clientStacktrace = clientStacktrace;
			this.serverStacktrace = serverStacktrace;
		}

		@Override
		public String toString() {
			return date.toString()
					+ " "
					+ level
					+ " "
					+ message
					+ (duration != null ? " executed in " + duration + "ms"
							: "");
		}

	}

	private static final int MAX_SIZE = 500;
	private static final int SPACING = 20;
	private List<Entry> lines = new LinkedList<Entry>();
	protected Entry details;
	private CommandLink cancel;
	private IDeobfuscator deobfuscator;

	@Override
	public ILog container(IContainer c) {
		if (Style.ENABLED && Style.instance().login().useMore()) {
			new ImageButton(c).imageResource("trace.png").text("Trace")
					.addClickListener(this);
		} else
			c.label().text("Trace").hyperlink().addClickListener(this);
		return this;
	}

	@Override
	public ILog debug(String message) {
		ensureSize();
		Entry e = new Entry("DEBUG", message);
		addLine(e);
		return this;
	}

	@Override
	public ILog debug(String message, long duration) {
		return addEntry(new Entry("DEBUG", message, duration));
	}

	@Override
	public ILog debug(String message, long duration,
			Throwable clientStacktrace, Throwable serverStacktrace) {
		return addEntry(new Entry("DEBUG", message, duration, clientStacktrace,
				serverStacktrace));
	}

	private ILog addEntry(Entry e) {
		ensureSize();
		addLine(e);
		// for (String key : timestamps.keySet()) {
		// timestamps.put(key, timestamps.get(key) + e.duration);
		// }
		return this;
	}

	private void addLine(Entry entry) {
		lines.add(0, entry);
		System.out.println("CLIENT> " + entry);
	}

	@Override
	public ILog test(String message) {
		ensureSize();
		Entry e = new Entry("TEST", message);
		addLine(e);
		return this;
	}

	private void ensureSize() {
		if (lines.size() > MAX_SIZE)
			lines.remove(lines.size() - 1);
	}

	@Override
	public void onClick() {
		final IPopUp popUp = PopUp.showPopUp().modal(true)
				.offset(SPACING, SPACING).autoHide(true);
		popUp.border().remove().style().shadow().color().black();
		WidgetTitle panel = new WidgetTitle(popUp.container()).spacing(0)
				.sideWidget(false).commandsOnTop().spacing(0);
		panel.addLargeTitle("Log Trace");
		final IScrollPane scrollPane = panel.content().scrollPane();
		final IVerticalPanel content = scrollPane.viewPort().panel().vertical()
				.spacing(10).add().panel().vertical();
		resize(Display.instance(), popUp, scrollPane);
		cancel = panel.addHyperlink("cancel.png", "Clear");
		cancel.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				lines.clear();
				content.clear();
			}
		});
		final IResizeConfiguration cfg = Display.instance()
				.addResizeListener(new IResizeListener() {
					@Override
					public void onResize(int width, int height) {
						resize(Display.instance(), popUp, scrollPane);
					}
				}).linkLifecycle(popUp);
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value)
					cfg.remove();
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

	public void resize(IDisplay d, final IPopUp popUp, IScrollPane scrollPane) {
		popUp.size(d.width() - SPACING * 2, d.height() - SPACING * 2);
		scrollPane.size(d.width() - SPACING * 2, d.height() - SPACING * 2
				- FullscreenPopUp.HEIGHT_TOP);
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
				if (l.clientStacktrace != null || l.serverStacktrace != null)
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
		ld.text(l.duration != null ? l.duration + " ms" : "").font().pixel(11)
				.weight().bold();
	}

	private void addMessage(Entry l, ILabel lm) {
		lm.autoWrap(true).text(l.message).font().pixel(12).family().courier();
	}

	private void addLevel(Entry l, ILabel ll) {
		ll.text(l.level).font().pixel(11).weight().bold();
	}

	private void addDate(Entry l, ILabel lbl) {
		lbl.text(l.date.toString());
		lbl.font().pixel(11).color().gray();
	}

	private void showException(final IScrollPane p, IVerticalPanel content,
			final Entry l) {
		cancel.visible(false);
		details = l;
		content.clear();
		IHorizontalPanel h = content.add().panel().horizontal().spacing(4);
		addLevel(l, h.add().label());
		addMessage(l, h.add().label());
		addDate(l, h.add().label());
		addDuration(l, h.add().label());
		if (l.clientStacktrace != null && deobfuscator != null
		// && deobfuscator.isDeobfuscated(l.clientStacktrace)
		) {
			final IVerticalPanel content2 = content.add().panel().vertical();
			content2.add().image().resource("loading_black.png").size(16, 16);
			deobfuscator.deobfuscate(l.clientStacktrace,
					new CallbackTemplate<String>() {
						@Override
						public void onSuccess(String clientTrace) {
							showExceptionsDeobfuscated(p, content2.clear(), l,
									clientTrace);
						}
					});
		} else {
			showExceptionsDeobfuscated(p, content, l,
					getExceptionString(l.clientStacktrace));
		}
	}

	private void showExceptionsDeobfuscated(IScrollPane p,
			IVerticalPanel content, Entry l, String clientTrace) {
		ITextArea ta = content.add().textArea()
				.size(p.width() - 40, p.height() - 60)
				.text(getExceptionString(l.serverStacktrace) + clientTrace)
				.editable(false);
		ta.border().width(1);
		Heights.INSTANCE.decorate(ta);
	}

	private String getExceptionString(Throwable t) {
		if (t == null)
			return "";
		StringBuilder b = new StringBuilder();
		addException(t, b);
		String string = b.toString();
		return string;
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
	public IMeasurement start(String message) {
		debug("Start " + message);
		return new Measurement(message);
	}

	@Override
	public ILog deobfuscator(IDeobfuscator deobfuscator) {
		this.deobfuscator = deobfuscator;
		return this;
	}

	@Override
	public ILog error(String message) {
		ensureSize();
		addLine(new Entry(ERROR, message, null, new RuntimeException(), null));
		return this;
	}

	@Override
	public ILog warn(String message) {
		return warn(message, new RuntimeException());
	}

	@Override
	public ILog warn(String message, Throwable e) {
		ensureSize();
		addLine(new Entry(WARNING, message, null, e, null));
		return this;
	}

	@Override
	public ILog error(String message, Throwable e) {
		ensureSize();
		addLine(new Entry(ERROR, message, null, e, null));
		e.printStackTrace();
		return this;
	}
}
