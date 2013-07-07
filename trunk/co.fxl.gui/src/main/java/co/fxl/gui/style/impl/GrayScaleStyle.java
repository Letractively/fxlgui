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
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.style.api.IStyle;

public class GrayScaleStyle implements IStyle {

	public static final String NAME = "GrayScale";

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {

			@Override
			public ILinearPanel<?>[] create(IContainer c) {
				ILinearPanel<?> v = c.panel().vertical();
				v.color().rgb(45, 45, 45);
				v.border().style().bottom();
				return new ILinearPanel<?>[] { v, null };
			}

			@Override
			public void itemPanel(IHorizontalPanel p) {
				p.border().style().left().style().right().color()
						.rgb(45, 45, 45);
			}
		};
	}

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public boolean useMoreButton() {
				return false;
			}

			@Override
			public void addSeparator(ILinearPanel<?> panel) {
				panel.add().label().text("|").font().color().gray();
			}

			@Override
			public void logout(ILinearPanel<?> panel, String userName,
					Decorator[] decorators, IClickListener listener) {
				panel.add().label().text("Logged in as").font().color().mix()
						.gray().black();
				panel.add().label().text(userName).font().weight().bold()
						.color().mix().gray().black();
				panel.add().label().text("Logout").hyperlink()
						.addClickListener(listener);
			}
		};
	}

	@Override
	public int fontSize() {
		return 12;
	}

	@Override
	public String fontFamily() {
		return "Arial, Helvetica, sans-serif";
	}

	@Override
	public IUserPanel userPanel() {
		return new IUserPanel() {

			@Override
			public void background(IHorizontalPanel panel) {
			}

			@Override
			public IClickable<?> profileButton(IPanel<?> panel) {
				return panel.add().label().text("Profile").hyperlink();
			}

			@Override
			public IClickable<?> enterAdminButton(IPanel<?> panel) {
				return panel.add().label().text("Administration").hyperlink();
			}

			@Override
			public IClickable<?> exitAdminButton(IPanel<?> panel) {
				ILabel l = panel.add().label().text("Administration");
				l.font().weight().bold();
				return panel.add().label().text("Back to Application")
						.hyperlink();
			}

			@Override
			public IClickable<?> traceButton(IContainer c) {
				return c.label().text("Trace").hyperlink();
			}
		};
	}

}
