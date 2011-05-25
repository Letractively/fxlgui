package co.fxl.gui.input.dummy;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.template.DateFormat;
import co.fxl.gui.input.api.ICalendarWidget;

class DummyCalendarWidget implements ICalendarWidget {

	private ILabel label;
	private List<IUpdateListener<Date>> listeners = new LinkedList<IUpdateListener<Date>>();

	DummyCalendarWidget(IContainer container) {
		this.label = container.label();
	}

	@Override
	public IUpdateable<Date> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<Date> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public ICalendarWidget date(Date date) {
		label.text(DateFormat.instance.format(date));
		for (IUpdateListener<Date> l : listeners)
			l.onUpdate(date());
		return this;
	}

	@Override
	public Date date() {
		return DateFormat.instance.parse(label.text());
	}
}
