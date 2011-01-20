/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.navigation.group.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.group.api.INavigationItem;

class NavigationItemImpl implements INavigationItem, IClickListener {

	private ILabel button;
	private IDecorator decorator;
	private IHorizontalPanel buttonPanel;
	private IColor borderColor;
	private NavigationWidgetImpl widget;
	private IHorizontalPanel itemPanel;

	NavigationItemImpl(NavigationGroupImpl group) {
		this(group.widget, group.itemPanel);
	}

	NavigationItemImpl(NavigationWidgetImpl widget, IHorizontalPanel itemPanel) {
		this.widget = widget;
		this.itemPanel = itemPanel;
	}

	void initButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = itemPanel.add().panel().horizontal().spacing(3);
			buttonPanel.spacing(3).align().center();
			buttonPanel.addSpace(3);
			borderColor = buttonPanel.border().width(2).color();
			button = buttonPanel.add().label();
			button.font().pixel(14).weight().bold();
			buttonPanel.addSpace(3);
			buttonPanel.addClickListener(this);
			showLabelAsInactive();
		}
	}

	@Override
	public INavigationItem name(String name) {
		initButtonPanel();
		button.text(name);
		return this;
	}

	void showLabelAsInactive() {
		if (buttonPanel == null)
			return;
		buttonPanel.clickable(true);
		applyColor(buttonPanel.color(), widget.colorInactive);
		applyColor(borderColor, widget.colorBackground);
	}

	@Override
	public IVerticalPanel addExtraPanel() {
		showLabelAsActive();
		return widget.panel1().clear();
	}

	@Override
	public INavigationItem initDecorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public void onClick() {
		widget.notifyChange(new co.fxl.gui.api.template.CallbackTemplate<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					active();
			}
		});
	}

	@Override
	public INavigationItem active() {
		showLabelAsActive();
		widget.panel0().clear();
		applyColor(widget.panel0().color(), widget.colorActive);
		decorator.decorate(widget.panel0());
		return this;
	}

	private void showLabelAsActive() {
		buttonPanel.clickable(false);
		widget.active(this);
		if (buttonPanel == null)
			return;
		applyColor(buttonPanel.color(), widget.colorActive);
		applyColor(borderColor, widget.colorActive);
	}

	private void applyColor(IColor color, int[] rgb) {
		color.rgb(rgb[0], rgb[1], rgb[2]);
	}

	@Override
	public INavigationItem back() {
		widget.panel1().clear();
		widget.panel0();
		return this;
	}
}
