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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.register.api.IRegister;
import co.fxl.gui.register.api.IRegisterWidget;

public class RegisterWidgetImpl implements IRegisterWidget {

	public interface ColorDecorator {

		void decorate(IColor color);
	}

	public IFlowPanel headerPanel;
	ICardPanel cardPanel;
	List<RegisterImpl> registers = new LinkedList<RegisterImpl>();
	int selection = -1;
	boolean separators = true;
	// private IHorizontalPanel stretch;
	private IDockPanel mainBorders;
	private ColorDecorator background;
	public int spacing = 6;
	public int fontSize = 14;
	private List<IRegisterListener> registerListeners = new LinkedList<IRegisterListener>();
	public IVerticalPanel backgroundPanel;
	int outerSpacing = 4;
	private boolean setUp = false;

	public RegisterWidgetImpl(ILayout panel) {
		mainBorders = panel.dock();
	}

	private void setUp() {
		if (setUp)
			return;
		setUp = true;
		backgroundPanel = mainBorders.top().panel().vertical()
				.spacing(outerSpacing / 2);
		headerPanel = backgroundPanel.add().panel().vertical().add().panel()
				.flow();
		cardPanel = mainBorders.center().panel().card();
	}

	public void background(ColorDecorator background) {
		setUp();
		background.decorate(backgroundPanel.color());
		this.background = background;
	}

	@Override
	public IRegister addRegister() {
		setUp();
		RegisterImpl register = new RegisterImpl(this, registers.size());
		registers.add(register);
		return register;
	}

	@Override
	public RegisterWidgetImpl visible(boolean visible, ICallback<Void> cb) {
		if (visible) {
			show(cb);
		} else {
			throw new UnsupportedOperationException();
		}
		return this;
	}

	@Override
	public IRegisterWidget outerSpacing(int outerSpacing) {
		this.outerSpacing = outerSpacing;
		return this;
	}

	private void show(ICallback<Void> cb) {
		mainBorders.visible(true);
		if (selection != -1)
			registers.get(selection).top(cb);
		else
			cb.onSuccess(null);
	}

	void top(RegisterImpl registerImpl) {
		selection = registerImpl.index;
		cardPanel.show(registerImpl.contentPanel());
		for (IRegisterListener l : registerListeners)
			l.onTop(registerImpl);
	}

	public IRegisterWidget separators(boolean separators) {
		this.separators = separators;
		return this;
	}

	// public IPanel<?> addFillerPanel() {
	// if (stretch == null) {
	// throw new UnsupportedOperationException();
	// }
	// return stretch;
	// }

	public IRegisterWidget topBorder() {
		setUp();
		IBorder border = cardPanel.border();
		border.color().rgb(172, 197, 213);
		border.style().top();
		return this;
	}

	public void background(IColor color) {
		background.decorate(color);
	}

	public int heightMenu() {
		return backgroundPanel.height();
	}

	@Override
	public IRegisterWidget addRegisterListener(IRegisterListener l) {
		registerListeners.add(l);
		return this;
	}

	boolean isActive(RegisterImpl registerImpl) {
		return selection == registerImpl.index;
	}

	public void showRegisterPanel(boolean showRegisterPanel) {
		setUp();
		backgroundPanel.visible(showRegisterPanel);
		if (!showRegisterPanel)
			backgroundPanel.spacing(0);
	}
}
