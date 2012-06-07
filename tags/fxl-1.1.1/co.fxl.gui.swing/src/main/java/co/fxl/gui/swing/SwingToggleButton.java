package co.fxl.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import co.fxl.gui.api.IToggleButton;

class SwingToggleButton extends SwingTextElement<JToggleButton, IToggleButton>
		implements IToggleButton {

	SwingToggleButton(SwingContainer<JToggleButton> container) {
		super(container);
		container.component.setOpaque(false);
	}

	@Override
	public IToggleButton down(boolean checked) {
		container.component.setSelected(checked);
		return this;
	}

	@Override
	public IToggleButton addUpdateListener(
			final IUpdateListener<Boolean> listener) {
		container.component.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.onUpdate(down());
			}
		});
		return this;
	}

	@Override
	protected void setTextOnComponent(String text) {
		container.component.setText(text);
	}

	@Override
	public boolean down() {
		return container.component.isSelected();
	}

	@Override
	public String text() {
		return container.component.getText();
	}

	@Override
	public IToggleButton text(String text) {
		setText(text);
		return this;
	}
}
