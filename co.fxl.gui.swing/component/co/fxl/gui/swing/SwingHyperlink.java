package co.fxl.gui.swing;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IHyperlink;
import co.fxl.gui.api.ILabel;

class SwingHyperlink implements IHyperlink {

	private ILabel label;

	SwingHyperlink(SwingContainer<?> container) {
		label = container.label().hyperlink();
	}

	@Override
	public IHyperlink text(String text) {
		label.text(text);
		return this;
	}

	@Override
	public IHyperlink uRI(final String uRI) {
		label.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				label.display().showWebsite().uRI(uRI);
			}
		});
		return this;
	}

	@Override
	public IHyperlink localURI(String string) {
		throw new MethodNotImplementedException();
	}
}
