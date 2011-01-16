package co.fxl.gui.api.template;

import java.util.Date;

public class SimpleDateFormat {

	public SimpleDateFormat() {
	}

	@SuppressWarnings("deprecation")
	public Date parse(String string) {
		if (string == null || string.equals(""))
			return null;
		String[] s = string.split("\\.");
		return new Date(Integer.valueOf(s[2]), Integer.valueOf(s[1]),
				Integer.valueOf(s[0]));
	}

	@SuppressWarnings("deprecation")
	public String format(Date date) {
		if (date == null)
			return "";
		int day = date.getDate();
		int month = date.getMonth() + 1;
		int year = date.getYear() + 1900;
		String string = l(day, 2) + "." + l(month, 2) + "." + l(year, 4);
		return string;
	}

	private String l(int date, int i) {
		String s = String.valueOf(date);
		while (s.length() < i)
			s = "0" + s;
		return s;
	}
}
