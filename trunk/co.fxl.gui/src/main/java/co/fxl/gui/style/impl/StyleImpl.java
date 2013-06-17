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
package co.fxl.gui.style.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.style.api.IStyle;

class StyleImpl implements IStyle {

	@Override
	public ITop top() {
		return new ITop() {

			@Override
			public String imageResource() {
				return null;
			}

			@Override
			public ITop panel(IPanel<?> panel) {
				return this;
			}

			@Override
			public int spacing() {
				return 0;
			}
		};
	}

	@Override
	public IOptionMenu optionMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITable table() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITree tree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INavigation navigation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRegister register() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWindow window() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILogin login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStyle background(IPanel<?> panel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStyle hyperlink(ILabel label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStyle side(ILinearPanel<?> panel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStyle activate(boolean activate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {
			@Override
			public void background(IVerticalPanel background) {
				background.s
			}
		};
	}

}
