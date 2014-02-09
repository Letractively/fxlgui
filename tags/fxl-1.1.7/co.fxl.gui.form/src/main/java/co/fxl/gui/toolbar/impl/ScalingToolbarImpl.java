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
package co.fxl.gui.toolbar.impl;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.toolbar.api.IScalingToolbar;

class ScalingToolbarImpl implements IScalingToolbar {

	private IFlowPanel panel;
	private String thumbnail;

	ScalingToolbarImpl(IContainer container) {
		this(container.panel().flow().spacing(4));
	}

	ScalingToolbarImpl(IFlowPanel panel) {
		this.panel = panel;
	}

	@Override
	public ICommandButton addButton() {
		return new ICommandButton() {

			@Override
			public IImage image(String resource) {
				return addImage().resource(resource);
			}

			@Override
			public ILabel text(String text) {
				return addLabel().text(text);
			}
		};
	}

	@Override
	public IScalingToolbar thumbnail(String imageResource) {
		thumbnail = imageResource;
		return this;
	}

	@Override
	public IImage addImage() {
		return panel.add().image();
	}

	@Override
	public ILabel addLabel() {
		return panel.add().label();
	}

	@Override
	public IScalingToolbar addComposite() {
		return new ScalingToolbarImpl(panel);
	}

	@Override
	public IComboBox addComboBox() {
		IComboBox comboBox = panel.add().comboBox();
		Heights.INSTANCE.decorate(comboBox);
		return comboBox;
	}

	@Override
	public ITextField addTextField() {
		ITextField textField = panel.add().textField();
		Heights.INSTANCE.decorate(textField);
		return textField;
	}

}
