package co.fxl.gui.dnd.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.dnd.api.IDNDWidget;
import co.fxl.gui.impl.ImageButton;

class DNDWidgetImpl implements IDNDWidget, IClickListener {

	private ImageButton button;
	private IElement<?> element;
	private IDNDModel model;

	DNDWidgetImpl(IContainer container) {
		button = new ImageButton(container);
		button.imageResource("dnd.png");
		button.text("D&D");
		button.addClickListener(this);
	}

	@Override
	public IDNDWidget element(IElement<?> element) {
		this.element = element;
		return this;
	}

	@Override
	public IDNDWidget model(IDNDModel model) {
		this.model = model;
		return this;
	}

	@Override
	public void onClick() {
		assert element != null;
		assert model != null;
		int x = element.offsetX() + element.width() + 4;
		int y = element.offsetY() - 20;
		int height = element.height() + 40;
		button.clickable(false);
		IPopUp popUp = element.display().showPopUp();
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					button.clickable(true);
				}
			}
		});
		popUp.offset(x, y);
		IVerticalPanel v = popUp.container().panel().vertical();
		v.add().image().resource("up.png");
		v.size(24, height);
		popUp.autoHide(true);
		popUp.visible(true);
	}

}
