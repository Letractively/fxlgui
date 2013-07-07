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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.api.IStyle;

class NinetyNineDesignsStyle implements IStyle {

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
		throw new UnsupportedOperationException();
	}

	@Override
	public ITable table() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ITree tree() {
		throw new UnsupportedOperationException();
	}

	@Override
	public INavigation navigation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IRegister register() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IWindow window() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public void label(ILabel text) {
				text.font().color().white();
			}

			@Override
			public void hyperlink(ILabel text) {
				throw new UnsupportedOperationException();
			}

			@Override
			public IClickable<?> addDecoration(ILinearPanel<?> panel) {
				return panel.add().image().resource("user_white.png");
			}

			@Override
			public String moreImage() {
				return "more_white_10x16.png";
			}

			@Override
			public boolean useMore() {
				return true;
			}

			@Override
			public boolean addSeparators() {
				return false;
			}

		};
	}

	@Override
	public IStyle background(IPanel<?> panel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IStyle hyperlink(ILabel label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IStyle side(ILinearPanel<?> panel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IStyle activate(boolean activate) {
		return this;
	}

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {
			@Override
			public void background(IPanel<?> background) {
				background.color().rgb(29, 59, 89).gradient().vertical()
						.rgb(57, 84, 110);
				background.border().style().bottom();
			}

			@Override
			public boolean containsUserPanel() {
				return true;
			}
		};
	}

	@Override
	public int fontSize() {
		return 12;
	}

	@Override
	public String fontFamily() {
		return "'Open Sans', Arial, Helvetica, sans-serif";
	}

}
