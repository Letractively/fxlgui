package co.fxl.gui.swing;

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
		label.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				throw new UnsupportedOperationException();
			}
		});
		return this;
	}

	@Override
	public IHyperlink clickable(boolean clickable) {
		// TODO ... throw new UnsupportedOperationException();
		return this;
	}

	@Override
	public boolean clickable() {
		// TODO ... throw new UnsupportedOperationException();
		return false;
	}

	@Override
	public IKey<IHyperlink> addClickListener(IClickListener clickListener) {
		// TODO ... throw new UnsupportedOperationException();
		return new IKey<IHyperlink>() {

			@Override
			public IHyperlink mouseLeft() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IHyperlink mouseRight() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IHyperlink shiftPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IHyperlink altPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IHyperlink ctrlPressed() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IHyperlink doubleClick() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
