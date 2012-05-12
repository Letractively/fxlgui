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

import java.util.Iterator;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.register.api.IRegister;

public class RegisterImpl extends LazyClickListener implements IRegister {

	private static final String _0 = " (0)";
	private static int SPACING_DEC_LOADING = Constants.get(
			"RegisterImpl.SPACING_DEC_LOADING", 0);

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
			toggleLoading(false);
			return this;
		}

		@Override
		public IBorder border() {
			return buttonPanel.border();
		}

		@Override
		public boolean isEmpty() {
			return empty;
		}
	}

	RegisterWidgetImpl widget;
	int index;
	private ILabel separator;
	IHorizontalPanel buttonPanel;
	ILabel buttonLabel;
	private IVerticalPanel content;
	private IRegisterListener listener = null;
	private IHorizontalPanel subPanel;
	private IImage buttonImage;
	boolean disabled = false;
	private IVerticalPanel verticalContainer;
	private boolean empty = false;

	// private String imageResource = "document.png";

	RegisterImpl(RegisterWidgetImpl widget, int index) {
		this.widget = widget;
		this.index = index;
		if (index > 0 && widget.separators) {
			addSeparator();
		}
		verticalContainer = widget.headerPanel.add().panel().vertical();
		int spc = widget.outerSpacing / 2;
		verticalContainer.spacing(spc);
		buttonPanel = verticalContainer.add().panel().horizontal();
		buttonPanel.spacing(widget.spacing);// .align().center();
		buttonPanel.addSpace(3);
		subPanel = buttonPanel.add().panel().horizontal().align().center();
		buttonImage = subPanel.add().image().resource("loading_white.gif")
				.visible(false);
		buttonLabel = subPanel.add().label();
		buttonPanel.addClickListener(this);
		buttonPanel.addSpace(3);
		content = widget.cardPanel.add().panel().vertical();
		notifyVisible(false, DummyCallback.voidInstance());
	}

	@Override
	public IRegister toggleLoading(boolean loading) {
		int width = buttonPanel.width();
		subPanel.width(subPanel.width());
		buttonImage.visible(loading);
		buttonPanel.spacing(loading ? widget.spacing - SPACING_DEC_LOADING
				: widget.spacing);
		buttonLabel.visible(!loading);
		buttonPanel.size(width, 24);
		if (isActive()) {
			buttonImage.resource("loading_black.gif");
		} else {
			buttonImage.resource("loading_white.gif");
		}
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
		// if (widget.isActive(this))
		// return this;
		if (disabled)
			enabled(true);
		return updateActive();
	}

	public RegisterImpl updateActive() {
		Iterator<RegisterImpl> rit = widget.registers.iterator();
		recurse(this, rit);
		return this;
	}

	private void recurse(final RegisterImpl active,
			final Iterator<RegisterImpl> rit) {
		if (!rit.hasNext())
			return;
		else {
			RegisterImpl reg = rit.next();
			reg.notifyVisible(reg == active, new CallbackTemplate<Void>() {
				@Override
				public void onSuccess(Void result) {
					recurse(active, rit);
				}
			});
		}
	}

	void notifyVisible(final boolean visible, final ICallback<Void> cb) {
		if (disabled) {
			cb.onSuccess(null);
			return;
		}
		if (listener != null) {
			toggleLoading(true);
			listener.onTop(visible, new CallbackTemplate<Void>(cb) {
				@Override
				public void onSuccess(Void result) {
					finish(visible);
					cb.onSuccess(null);
				}

				void finish(final boolean visible) {
					toggleLoading(false);
					if (visible)
						widget.top(RegisterImpl.this);
					if (visible) {
						buttonPanel.clickable(false);
					} else {
						buttonPanel.clickable(true);
					}
				}

				@Override
				public void onFail(Throwable throwable) {
					finish(visible);
					IVerticalPanel panel = contentPanel();
					showFailureLoadingRegister(panel);
					super.onFail(throwable);
				}
			});
		} else
			cb.onSuccess(null);
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
			buttonPanel.clickable(true);
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

	public static void showFailureLoadingRegister(IVerticalPanel panel) {
		panel.clear().add().panel().vertical().add().panel().horizontal()
				.spacing(10).add().label().text("FAILURE LOADING REGISTER")
				.font().pixel(10).color().gray();
	}

	@Override
	public void showTitleAsEmpty(boolean empty) {
		if (empty != this.empty) {
			buttonPanel.width(-1);
			subPanel.width(-1);
			if (empty) {
				buttonLabel.text(buttonLabel.text() + _0);
			} else {
				buttonLabel.text(buttonLabel.text().substring(0,
						buttonLabel.text().length() - _0.length()));
			}
		}
		this.empty = empty;
	}
}
