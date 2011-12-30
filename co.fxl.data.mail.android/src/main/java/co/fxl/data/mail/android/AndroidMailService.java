package co.fxl.data.mail.android;

import co.fxl.data.mail.api.IMail;
import co.fxl.data.mail.api.IMailService;

public class AndroidMailService implements IMailService {

	@Override
	public IMail createMail() {
		return new AndroidMail();
	}

}
