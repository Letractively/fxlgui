package co.fxl.gui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.swing.HorizontalLayoutManager.Stretch;

class SwingFlowPanel extends SwingPanel<IFlowPanel> implements IFlowPanel,
		Stretch {

	// TODO does not work! temporary fix! implement working flow layout as in
	// gwt

	Component stretch;

	SwingFlowPanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(new HorizontalLayoutManager(this));
		flowLayout().setAlignment(FlowLayout.LEFT);
		flowLayout().setHgap(0);
		flowLayout().setVgap(0);
	}

	private FlowLayout flowLayout() {
		return (FlowLayout) container.component.getLayout();
	}

	@Override
	public Component stretch() {
		return stretch;
	}

	@Override
	public IFlowPanel spacing(int spacing) {
		flowLayout().setHgap(spacing);
		flowLayout().setVgap(spacing);
		return this;
	}

	@Override
	public IFlowPanel addSpace(int pixel) {
		container.component.add(Box.createRigidArea(new Dimension(pixel, 1)));
		return this;
	}

	@Override
	public IAlignment<IFlowPanel> align() {
		return new IAlignment<IFlowPanel>() {

			@Override
			public IFlowPanel begin() {
				// TODO SWING-FXL: IMPL: ...
				return SwingFlowPanel.this;
			}

			@Override
			public IFlowPanel center() {
				// TODO SWING-FXL: IMPL: ...
				return SwingFlowPanel.this;
			}

			@Override
			public IFlowPanel end() {
				// TODO SWING-FXL: IMPL: ...
				return SwingFlowPanel.this;
			}

		};
	}
}
