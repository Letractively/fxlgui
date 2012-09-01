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

	private static final String _0 = "";
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
			setText(title);
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

		@Override
		public boolean isClickable() {
			return allowClick;
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
	private boolean allowClick = true;

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
		buttonLabel.addClickListener(this);
		buttonPanel.addSpace(3);
		newCardPanel();
		notifyVisible(false, DummyCallback.voidInstance());
	}

	public void newCardPanel() {
		content = widget.cardPanel.add().panel().vertical();
	}

	@Override
	public IRegister toggleLoading(boolean loading) {
		int width = buttonPanel.width();
		int w = subPanel.width();
		if (w != 0)
			subPanel.width(w);
		buttonImage.visible(loading);
		buttonPanel.spacing(loading ? widget.spacing - SPACING_DEC_LOADING
				: widget.spacing);
		buttonLabel.visible(!loading);
		if (width == 0)
			buttonPanel.height(24);
		else
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
	public RegisterImpl top(ICallback<Void> cb) {
		// if (widget.isActive(this))
		// return this;
		if (disabled)
			enabled(true);
		return updateActive(cb);
	}

	public RegisterImpl updateActive(ICallback<Void> cb) {
		Iterator<RegisterImpl> rit = widget.registers.iterator();
		recurse(this, rit, cb);
		return this;
	}

	private void recurse(final RegisterImpl active,
			final Iterator<RegisterImpl> rit, final ICallback<Void> cb) {
		if (!rit.hasNext()) {
			cb.onSuccess(null);
			return;
		} else {
			RegisterImpl reg = rit.next();
			reg.notifyVisible(reg == active, new CallbackTemplate<Void>(cb) {
				@Override
				public void onSuccess(Void result) {
					recurse(active, rit, cb);
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
						updateButtonPanel(false);
					} else {
						updateButtonPanel(true);
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
		top(DummyCallback.voidInstance());
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
			setButtonLabelWhite();
			updateButtonPanel(true);
			verticalContainer.visible(false);
			disabled = true;
		} else {
			if (disabled) {
				setButtonLabelWhite();
				verticalContainer.visible(true);
				disabled = false;
			}
		}
		return this;
	}

	void setButtonLabelWhite() {
		buttonLabel.font().color().white();
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
			if (_0.length() > 0) {
				if (empty) {
					buttonLabel.text(buttonLabel.text() + _0);
				} else {
					buttonLabel.text(buttonLabel.text().substring(0,
							buttonLabel.text().length() - _0.length()));
				}
			}
		}
		this.empty = empty;
	}

	@Override
	public IVerticalPanel newContentPanel() {
		content.remove();
		newCardPanel();
		// toggleLoading(true);
		return content;
	}

	@Override
	public void showNewContentPanel() {
		// toggleLoading(false);
		widget.cardPanel.show(content);
	}

	@Override
	public IRegister clickable(boolean b) {
		allowClick = b;
		// if (allowClick || isActive()) {
		// buttonLabel.font().color().black();
		// } else
		// buttonLabel.font().color().gray();
		updateButtonPanel(b);
		return this;
	}

	private void updateButtonPanel(boolean b) {
		buttonPanel.clickable(b && allowClick);
		buttonLabel.clickable(b && allowClick);
	}

	void setText(String title) {
		buttonLabel.text(title);
		toggleLoading(false);
	}
}
