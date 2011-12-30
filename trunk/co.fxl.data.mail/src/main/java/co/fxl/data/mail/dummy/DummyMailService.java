package co.fxl.data.mail.dummy;

import co.fxl.data.mail.api.IMail;
import co.fxl.data.mail.api.IMailService;

public class DummyMailService implements IMailService {

	@Override
	public IMail createMail() {
		return new DummyMail();
	}

}
