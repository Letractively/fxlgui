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
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.style.api.IStyle;

class NinetyNineDesignsStyle implements IStyle {

	public static final String NAME = "99designs";

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public boolean useMoreButton() {
				return true;
			}

			@Override
			public void addSeparator(ILinearPanel<?> panel) {
				panel.addSpace(10);

			}

			@Override
			public void logout(ILinearPanel<?> panel, String userName,
					final Decorator[] decorators, final IClickListener listener) {
				IClickable<?> prefix = panel.add().image()
						.resource("user_white.png");
				ILabel loggedInHead = panel.add().label().text(userName);
				loggedInHead.font().pixel(fontSize()).weight().bold().color()
						.white();
				new HyperlinkMouseOverListener(loggedInHead);
				final IImage image = panel.add().image()
						.resource("more_white_10x16.png");
				IClickListener clickListener = new IClickListener() {

					@Override
					public void onClick() {
						final IPopUp popUp = PopUp.showPopUp(true).width(140)
								.autoHide(true);
						popUp.offset(image.offsetX() - 140 + 12,
								image.offsetY() + 25);
						IVerticalPanel p = popUp.container().panel().vertical()
								.spacing(8);
						p.margin().top(2);
						boolean addLine = false;
						for (Decorator d : decorators) {
							if (d.isVisible()) {
								d.decorate(p.add().panel().horizontal());
								addLine = true;
							}
						}
						if (addLine)
							p.add().line();
						ImageButton ib = new ImageButton(p.add());
						ib.imageResource("logout.png").text("Logout")
								.addClickListener(listener);
						ib.label().hyperlink();
						popUp.visible(true);
					}
				};
				prefix.addClickListener(clickListener);
				loggedInHead.addClickListener(clickListener);
				image.addClickListener(clickListener);
			}
		};
	}

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {

			@Override
			public ILinearPanel<?>[] create(IContainer c) {
				IGridPanel grid = c.panel().grid().spacing(1);
				grid.color().rgb(29, 59, 89).gradient().vertical()
						.rgb(57, 84, 110);
				grid.border().style().bottom();
				grid.column(0).expand();
				ILinearPanel<?> v = grid.cell(0, 0).valign().center().panel()
						.vertical();
				ILinearPanel<?> end = grid.cell(1, 0).valign().center().align()
						.end().panel().horizontal().align().end();
				end.padding().top(1);
				return new ILinearPanel<?>[] { v, end };
			}

			@Override
			public void itemPanel(IHorizontalPanel p) {
			}
		};
	}

	@Override
	public IUserPanel userPanel() {
		return new IUserPanel() {

			@Override
			public void background(IHorizontalPanel panel) {
				panel.margin().right(10);
			}

			@Override
			public IClickable<?> profileButton(IPanel<?> panel) {
				ImageButton ib = new ImageButton(panel.add());
				ib.label().hyperlink();
				return ib.imageResource("user.png").text("My Profile");
			}

			@Override
			public IClickable<?> enterAdminButton(IPanel<?> panel) {
				ImageButton text = new ImageButton(panel.add());
				text.imageResource("settings.png").text("Administration");
				text.label().font().pixel(fontSize()).weight().bold().color()
						.white();
				new HyperlinkMouseOverListener(text.label());
				return text;
			}

			@Override
			public IClickable<?> exitAdminButton(IPanel<?> panel) {
				panel.add().image().resource("settings.png");
				ILabel l = panel.add().label().text("Administration");
				l.font().weight().bold();
				l.font().pixel(fontSize()).color().lightgray();
				ILabel text = panel.add().label().text("Back to Application");
				text.font().pixel(fontSize()).weight().bold().color().white();
				new HyperlinkMouseOverListener(text);
				return text;
			}

			@Override
			public IClickable<?> traceButton(IContainer c) {
				ImageButton ib = new ImageButton(c).imageResource("trace.png");
				ib.text("Show Trace");
				ib.label().hyperlink();
				return ib;
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

	@Override
	public INavigation navigation() {
		return new INavigation() {

			@Override
			public void init(IHorizontalPanel buttonPanel, ILabel button) {
			}

			@Override
			public void active(IHorizontalPanel buttonPanel, ILabel button) {
				buttonPanel.color().rgb(28, 59, 89);
				button.font().color().white();
				buttonPanel.border().remove().style().rounded().width(6);
			}

			@Override
			public void inactive(IHorizontalPanel buttonPanel, ILabel button) {
				buttonPanel.color().remove();
				button.font().color().rgb(28, 59, 89);
				buttonPanel.border().remove().style().rounded().width(6);
			}

			@Override
			public String activeRefreshImage() {
				return "refresh_white_new.png";
			}

			@Override
			public String inactiveRefreshImage() {
				return "refresh_blue_new.png";
			}

			@Override
			public void background(IColored colored) {

				// TODO ...

			}

			@Override
			public boolean hasSegmentedBorder() {
				return false;
			}

			@Override
			public void group(ILabel header) {
				header.font().color().gray();
			}
		};
	}

}
