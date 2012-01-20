package co.fxl.gui.dnd.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.dnd.api.IDNDWidget;
import co.fxl.gui.impl.ImageButton;

class DNDWidgetImpl implements IDNDWidget, IClickListener {

	private ImageButton button;
	private IElement<?> element;
	private IHeights model;
	private int size;
	private int start;
	private int end;

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
	public IDNDWidget heights(IHeights model) {
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
		v.add().image().resource("up.png").size(16, 16);
		for (int i = 0; i <= end - start; i++) {
			int h = model.height(i + start);
			IVerticalPanel v2 = v.add().panel().vertical();
			v2.height(h).border().style().dotted();
			v2.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					throw new MethodNotImplementedException();
				}

				@Override
				public void onMouseOut() {
					throw new MethodNotImplementedException();
				}
			});
			addDragAndDrop(v2);
		}
		v.size(24, height);
		popUp.autoHide(true);
		popUp.border();
		popUp.visible(true);
	}

	protected void addDragAndDrop(IVerticalPanel v2) {
	}

	@Override
	public IDNDWidget range(int start, int end) {
		this.start = start;
		this.end = end;
		return this;
	}

	@Override
	public IDNDWidget size(int size) {
		this.size = size;
		return this;
	}

}
