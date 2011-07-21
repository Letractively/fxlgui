package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;

class AddEntityDialog {

	static IScrollTableWidget.IDecorator show(
			final IScrollTableWidget.IDecorator dec) {
		return new IScrollTableWidget.IDecorator() {

			private List<IClickListener> cls = new LinkedList<IClickListener>();

			@Override
			public IClickable<?> decorate(final IContainer c) {
				c.image().resource("add.png")
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								final IDialog dialog = c.display().showDialog()
										.title("ADD ENTITY");
								dialog.addButton().text("Back")
										.imageResource("back.png")
										.addClickListener(new IClickListener() {
											@Override
											public void onClick() {
												dialog.visible(false);
											}
										});
								ILinearPanel<?> p = dialog.container().panel()
										.vertical().spacing(6).add().panel()
										.vertical();
								IClickable<?> c = dec.decorate(p.add());
								for (IClickListener cl : cls)
									c.addClickListener(cl);
								dialog.visible(true);
							}
						}).mouseLeft();
				return new IClickable<Object>() {

					@Override
					public Object clickable(boolean clickable) {
						return this;
					}

					@Override
					public boolean clickable() {
						return false;
					}

					@Override
					public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
							co.fxl.gui.api.IClickable.IClickListener clickListener) {
						cls.add(clickListener);
						return null;
					}

				};
			}
		};
	};
}
