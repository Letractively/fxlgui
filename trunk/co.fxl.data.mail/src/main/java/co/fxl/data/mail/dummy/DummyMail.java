package co.fxl.data.mail.dummy;

import co.fxl.data.mail.api.IMail;

class DummyMail implements IMail {

	@Override
	public IMail addTo(String email) {
		return this;
	}

	@Override
	public IMail addCC(String email) {
		return this;
	}

	@Override
	public IMail subject(String subject) {
		return this;
	}

	@Override
	public IMail text(String text) {
		return this;
	}

	@Override
	public IMail send() {
		return this;
	}

}
