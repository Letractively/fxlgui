package co.fxl.gui.format.gwt;

import java.util.Date;

import co.fxl.gui.format.api.IFormat;
import co.fxl.gui.format.impl.Format;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class GWTFormat {

	public static void setUp() {
		Format.registerTime(new IFormat<Date>() {

			private DateTimeFormat impl = DateTimeFormat
					.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);

			@Override
			public String format(Date object) {
				return impl.format(object);
			}

			@Override
			public Date parse(String format) {
				return impl.parse(format);
			}
		});
		Format.register(Date.class, new IFormat<Date>() {

			private DateTimeFormat impl = DateTimeFormat
					.getFormat(PredefinedFormat.DATE_SHORT);

			@Override
			public String format(Date object) {
				return impl.format(object);
			}

			@Override
			public Date parse(String format) {
				return impl.parse(format);
			}
		});
		Format.register(Long.class, new IFormat<Long>() {

			private NumberFormat impl = NumberFormat.getDecimalFormat();

			@Override
			public String format(Long object) {
				return impl.format(object);
			}

			@Override
			public Long parse(String format) {
				return new Double(impl.parse(format)).longValue();
			}
		});
		Format.register(Integer.class, new IFormat<Integer>() {

			private NumberFormat impl = NumberFormat.getDecimalFormat();

			@Override
			public String format(Integer object) {
				return impl.format(object);
			}

			@Override
			public Integer parse(String format) {
				return new Double(impl.parse(format)).intValue();
			}
		});
	}
}
