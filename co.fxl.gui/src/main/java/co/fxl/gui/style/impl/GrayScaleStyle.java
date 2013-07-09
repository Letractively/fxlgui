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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.style.api.IStyle;

class GrayScaleStyle implements IStyle {

	private class ViewSelection implements IViewSelection {

		@Override
		public IViewSelection addView(String label, ViewType viewType,
				IClickListener c, boolean active, boolean isLast) {
			return ViewSelection.this;
		}

		@Override
		public boolean containsQuerySelection() {
			return true;
		}

	}

	public static final String NAME = "Grayscale";

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

			@Override
			public int marginLeft() {
				return 6;
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

	private static int[] colorActive = new int[] { 245, 245, 245 };
	// private static int[] colorBackground = new int[] { 199, 224, 241 };
	private static int[] colorInactive = new int[] { 111, 111, 111 };
	private static int[] colorInactiveGradient = new int[] { 63, 63, 63 };

	@Override
	public INavigation navigation() {
		return new INavigation() {

			@Override
			public void background(IColored colored) {
				IColor color = colored.color();
				color.remove();
				color.rgb(colorActive[0], colorActive[1], colorActive[2]);
			}

			@Override
			public void active(IHorizontalPanel buttonPanel, ILabel button) {
				background((IColored) buttonPanel);
				IBorder border = buttonPanel.border();
				border.color().gray();
				border.style().noBottom();
				button.font().color().black();
			}

			@Override
			public void inactive(IHorizontalPanel buttonPanel, ILabel button) {
				IBorder border = buttonPanel.border();
				border.color()
						.mix()
						.rgb(colorInactive[0], colorInactive[1],
								colorInactive[2])
						.rgb(colorInactiveGradient[0],
								colorInactiveGradient[1],
								colorInactiveGradient[2]);
				border.style().noBottom();
				buttonPanel
						.color()
						.rgb(colorInactive[0], colorInactive[1],
								colorInactive[2])
						.gradient()
						.vertical()
						.rgb(colorInactiveGradient[0],
								colorInactiveGradient[1],
								colorInactiveGradient[2]);
				button.font().color().white();
			}

			@Override
			public boolean hasSegmentedBorder() {
				return true;
			}

			@Override
			public String activeRefreshImage() {
				return "loading_black.gif";
			}

			@Override
			public String inactiveRefreshImage() {
				return "loading_white.gif";
			}

			@Override
			public void init(IHorizontalPanel buttonPanel, ILabel button) {
			}

			@Override
			public void group(ILabel header) {
			}

			@Override
			public void activeMore(IHorizontalPanel buttonPanel, ILabel button,
					IImage refresh) {
				buttonPanel.color().remove();
				buttonPanel.color().white();
				IBorder border = buttonPanel.border();
				border.style().noBottom();
				border.color().gray();
			}

			@Override
			public boolean isActiveMoreDark() {
				return false;
			}

			@Override
			public void backgroundRegisters(IPanel<?> panel) {
				panel.color().rgb(235, 235, 235).gradient()
						.fallback(235, 235, 235).vertical().rgb(211, 211, 211);

			}

			@Override
			public void backgroundCards(IPanel<?> history) {
			}

			@Override
			public int offsetX() {
				return 0;
			}

			@Override
			public void moreItem(ILabel label) {
			}
		};
	}

	@Override
	public ILogoPanel logoPanel() {
		return new ILogoPanel() {

			@Override
			public String resource() {
				return "logo_xoricon.png";
			}

			@Override
			public void background(IPanel<?> panel) {
				panel.color().rgb(249, 249, 249).gradient()
						.fallback(235, 235, 235).vertical().rgb(235, 235, 235);
			}

			@Override
			public int marginLeft() {
				return 0;
			}
		};
	}

	@Override
	public IViewSelection createSelection(IContainer c) {
		return new ViewSelection();
	}

	@Override
	public void background(IPanel<?> panel) {
		panel.color().gray(245);
	}

	@Override
	public void display(IDisplay display) {
		display.color().gray(245);
	}

}
