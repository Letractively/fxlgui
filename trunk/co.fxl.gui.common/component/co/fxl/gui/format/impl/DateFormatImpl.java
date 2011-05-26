package co.fxl.gui.format.impl;

import java.text.DateFormat;
import java.util.Date;

import co.fxl.gui.format.api.IFormat;

class DateFormatImpl implements IFormat<Date> {

	private DateFormat impl;

	DateFormatImpl(DateFormat impl) {
		this.impl = impl;
	}

	@Override
	public String format(Date object) {
		return impl.format(object);
	}

	@Override
	public Date parse(String format) {
		try {
			return impl.parse(format);
		} catch (Exception e) {
			return null;
		}
	}
}