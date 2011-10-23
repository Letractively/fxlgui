/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.fxl.gui.register.impl;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.register.api.IRegister;

class RegisterImpl extends LazyClickListener implements IRegister {

	private class Title implements ITitle {

		@Override
		public IFont font() {
			return buttonLabel.font();
		}

		@Override
		public IColor color() {
			return buttonPanel.color();
		}

		@Override
		public ITitle text(String title) {
			buttonLabel.text(title);
			return this;
		}

		@Override
		public IBorder border() {
			return buttonPanel.border();
		}
	}

	RegisterWidgetImpl widget;
	int index;
	private ILabel separator;
	IHorizontalPanel buttonPanel;
	ILabel buttonLabel;
	private IVerticalPanel content;
	private IRegisterListener listener = null;
	private boolean init = false;
	private IHorizontalPanel subPanel;
	private IImage buttonImage;
	boolean disabled = false;
	private IVerticalPanel verticalContainer;

	// private String imageResource = "document.png";

	RegisterImpl(RegisterWidgetImpl widget, int index) {
		this.widget = widget;
		this.index = index;
		init();
	}

	private void init() {
		if (init)
			return;
		if (index > 0 && widget.separators) {
			addSeparator();
		}
		verticalContainer = widget.headerPanel.add().panel().vertical();
		verticalContainer.spacing().left(widget.outerSpacing).right(0).top(0)
				.bottom(widget.outerSpacing);
		buttonPanel = verticalContainer.add().panel().horizontal();
		buttonPanel.spacing(widget.spacing);// .align().center();
		buttonPanel.addSpace(3);
		subPanel = buttonPanel.add().panel().horizontal().align().center();
		buttonImage = subPanel.add().image().resource("loading_white.gif")
				.visible(false);
		buttonLabel = subPanel.
		// addSpace(4).
				add().label();
		buttonPanel.addClickListener(this);
		buttonPanel.addSpace(3);
		content = widget.cardPanel.add().panel().vertical();
		init = true;
		notifyVisible(false);
	}

	@Override
	public IRegister toggleLoading(boolean loading) {
		int width = subPanel.width();
		int height = subPanel.height();
		buttonImage.visible(loading);
		buttonLabel.visible(!loading);
		subPanel.size(width, height);
		if (isActive()) {
			buttonImage.resource("loading_black.gif");
		} else {
			buttonImage.resource("loading_white.gif");
		}
		// if (loading)
		// buttonImage.resource("loading_2.gif");
		// else
		// buttonImage.resource(imageResource);
		return this;
	}

	private void addSeparator() {
		separator = widget.headerPanel.add().label();
		separator.text("|");
		separator.font().color().gray();
	}

	@Override
	public ITitle title() {
		return new Title();
	}

	@Override
	public RegisterImpl top() {
		if (disabled)
			enabled(true);
		return updateActive();
	}

	public RegisterImpl updateActive() {
		for (RegisterImpl register : widget.registers) {
			register.notifyVisible(register == this);
		}
		return this;
	}

	void notifyVisible(final boolean visible) {
		if (disabled)
			return;
		if (listener != null) {
			toggleLoading(true);
			listener.onTop(visible, new CallbackTemplate<Void>() {
				@Override
				public void onSuccess(Void result) {
					toggleLoading(false);
					if (visible)
						widget.top(RegisterImpl.this);
					if (visible) {
						buttonPanel.clickable(false);
					} else {
						buttonPanel.clickable(true);
					}
				}
			});
		}
	}

	@Override
	public IVerticalPanel contentPanel() {
		return content;
	}

	@Override
	public void onAllowedClick() {
		top();
	}

	@Override
	public IRegister listener(IRegisterListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public RegisterImpl visible(boolean visible) {
		return updateVisible(visible);
	}

	public RegisterImpl updateVisible(boolean visible) {
		if (separator != null)
			separator.visible(visible);
		verticalContainer.visible(visible);
		return this;
	}

	@Override
	public RegisterImpl enabled(boolean enabled) {
		if (!enabled) {
			buttonLabel.font().color().white();
			verticalContainer.visible(false);
			disabled = true;
		} else {
			if (disabled) {
				buttonLabel.font().color().white();
				verticalContainer.visible(true);
				disabled = false;
			}
		}
		return this;
	}

	@Override
	public boolean isActive() {
		return widget.selection == index;
	}

	@Override
	public boolean enabled() {
		return !disabled;
	}

	@Override
	public IRegister imageResource(String imageResource) {
		// if (imageResource != null)
		// this.imageResource = imageResource;
		// buttonImage.resource(this.imageResource);
		return this;
	}
}
