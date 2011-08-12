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
package co.fxl.gui.style.gplus;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.api.IStyle;
import co.fxl.gui.style.impl.Style;

public class GPlusStyle implements IStyle {

	public class GPlusLogin implements ILogin {

		@Override
		public ILogin label(ILabel label) {
			label.font().color().mix().gray().lightgray();
			return this;
		}

		@Override
		public ILogin hyperlink(ILabel label) {
			label.font().weight().bold().color().white();
			return this;
		}

	}

	class GPlusWindow implements IWindow {

		@Override
		public IWindow main(IPanel<?> panel) {
			panel.color().white();
			panel.border().remove();
			return this;
		}

		@Override
		public IWindow header(IPanel<?> panel) {
			panel.color().remove();
			panel.border().color().mix().lightgray().gray();
			panel.border().width(1);
			panel.border().style().bottom();
			return this;
		}

		@Override
		public IWindow footer(IPanel<?> panel) {
			panel.color().remove();
			panel.border().remove();
			return this;
		}

		@Override
		public IWindow title(ILabel label, String title, boolean isSideWidget) {
			label.text(title);
			if (isSideWidget)
				label.font().color().gray();
			else
				label.font().color().black();
			return this;
		}

		@Override
		public IWindow navigationEntry(ILinearPanel<?> panel) {
			panel.spacing(0).border().remove();
			return this;
		}

		@Override
		public IWindow button(IPanel<?> panel, boolean isSideWidget) {
			if (isSideWidget) {
				panel.color().remove();
				panel.border().remove();
			} else {
				panel.color().mix().white().lightgray();
				panel.border().color().lightgray();
			}
			return this;
		}

		@Override
		public ILabel addCommandLabel(ILinearPanel<?> panel, String text,
				boolean isSideWidget) {
			if (isSideWidget)
				return panel.add().label().visible(false);
			else {
				ILabel l = panel.addSpace(4).add().label().text(text);
				l.font().pixel(12);
				return l;
			}
		}

		@Override
		public boolean commandsOnTop() {
			return true;
		}
	}

	private GPlusWindow window = new GPlusWindow();

	@Override
	public IWindow window() {
		assertEnabled();
		return window;
	}

	private void assertEnabled() {
		assert Style.ENABLED;
	}

	@Override
	public IStyle background(IPanel<?> panel) {
		assertEnabled();
		panel.color().white();
		return this;
	}

	@Override
	public IStyle side(ILinearPanel<?> panel) {
		assertEnabled();
		panel.spacing().left(10);
		IBorder border = panel.border();
		border.color().lightgray();
		border.width(1);
		border.style().left();
		panel.color().white();
		return this;
	}

	@Override
	public IStyle hyperlink(ILabel label) {
		label.font().color().rgb(51, 102, 204);
		return this;
	}

	@Override
	public ILogin login() {
		return new GPlusLogin();
	}
}
