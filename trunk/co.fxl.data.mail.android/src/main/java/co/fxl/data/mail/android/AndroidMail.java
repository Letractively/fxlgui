package co.fxl.data.mail.android;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import co.fxl.data.mail.api.IMail;
import co.fxl.gui.android.AndroidDisplay;

class AndroidMail implements IMail {

	private List<String> to = new LinkedList<String>();
	private List<String> cc = new LinkedList<String>();
	private String subject = "";
	private String text = "";

	@Override
	public IMail addTo(String email) {
		to.add(email);
		return this;
	}

	@Override
	public IMail addCC(String email) {
		cc.add(email);
		return this;
	}

	@Override
	public IMail subject(String subject) {
		this.subject = subject;
		return this;
	}

	@Override
	public IMail text(String text) {
		this.text = text;
		return this;
	}

	@Override
	public IMail send() {
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				to.toArray(new String[0]));
		emailIntent.putExtra(android.content.Intent.EXTRA_CC,
				cc.toArray(new String[0]));
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		Activity context = AndroidDisplay.instance().activity;
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		return this;
	}
}
