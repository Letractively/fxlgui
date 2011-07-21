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
package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;

public class NavigationView {

	private static final String NAVIGATION = "Navigation";
	private static final boolean SHOW_NUMBERS = false;
	private static final boolean SHOW_TRIANGLE = true;
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private int index = 1;
	private boolean hasLinks;
	private INavigationListener nl;
	private IClickable<?> back;
	private IClickable<?> forward;

	public NavigationView(ILayout layout) {
		widgetTitle = new WidgetTitle(layout, true).sideWidget(true);
		widgetTitle.addToContextMenu(true);
		widgetTitle.space(2);
		setUp();
	}

	private void setUp() {
		if (panel != null)
			return;
		widgetTitle.addTitle(NAVIGATION);
		panel = widgetTitle.content().panel().vertical().spacing(6);
	}

	public NavigationView navigationViewListener(final INavigationListener nl) {
		if (nl == null)
			return this;
		this.nl = nl;
		back = widgetTitle.addHyperlink(Icons.NAVIGATION_BACK, "Back");
		back.addClickListener(new LazyClickListener() {

			@Override
			protected void onAllowedClick() {
				nl.previous();
			}
		});
		forward = widgetTitle.addHyperlink(Icons.NAVIGATION_FORWARD, "Forward");
		forward.addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				nl.next();
			}
		});
		updateNavigationButtons();
		return this;
	}

	public void updateNavigationButtons() {
		if (back == null)
			return;
		back.clickable(nl.hasPrevious());
		forward.clickable(nl.hasNext());
	}

	public ImageButton addHyperlink() {
		return addHyperlink(null);
	}

	public ImageButton addHyperlink(String imageResource) {
		setUp();
		IPanel<?>[] panels = addPanel();
		IHorizontalPanel panel = (IHorizontalPanel) panels[1];
		IImage image = addImage(panel, imageResource);
		ILabel textLabel = addTextLabel(panel);
		return new ImageButton((IVerticalPanel) panels[0], panel, image,
				textLabel).addToContextMenu(true);
	}

	protected ILabel addTextLabel(IHorizontalPanel panel) {
		final ILabel textLabel = panel.add().label().hyperlink();
		return textLabel;
	}

	public IUpdateable<String> addComboBoxLink(String title, String text,
			String... options) {
		setUp();
		IPanel<?>[] panels = addPanel();
		IHorizontalPanel panel = (IHorizontalPanel) panels[1];
		addImage(panel, null);
		ILabel label = addTextLabel(panel);
		styleContentChoice(label);
		label.text(title);
		IComboBox cb = panel.addSpace(8).add().comboBox();
		new Heights(0).decorate(cb);
		cb.width(202);
		cb.addText(options);
		cb.text(text);
		return cb;
	}

	public void styleContentChoice(ILabel label) {
		// Styles.instance().style(label, Style.Window.CONTENT,
		// Style.List.CHOICE);
		label.font().weight().bold();
	}

	protected IImage addImage(IHorizontalPanel panel, String imageResource) {
		IImage image = null;
		if (SHOW_NUMBERS) {
			String s = String.valueOf(index++) + ".";
			ILabel label = panel.add().label().text(s);
			styleContentNumber(label);
			panel.addSpace(4);
		} else if (SHOW_TRIANGLE) {
			image = panel
					.add()
					.image()
					.resource(
							imageResource == null ? Icons.LINK : imageResource);
			panel.addSpace(4);
		}
		return image;
	}

	public void styleContentNumber(ILabel label) {
		// Styles.instance().style(label, Style.Window.CONTENT,
		// Style.List.NUMBER);
		label.font().pixel(13).color().gray();
	}

	protected IPanel<?>[] addPanel() {
		IVerticalPanel p = panel.add().panel().vertical().spacing(2);
		if (hasLinks) {
			p.addSpace(3);
		}
		IHorizontalPanel panel = p.add().panel().horizontal().align().begin()// .addSpace(6)
				.add().panel().horizontal().align().begin();
		if (hasLinks) {
			IBorder border = p.border();
			border.color().rgb(172, 197, 213);
			border.style().top();
		}
		hasLinks = true;
		IPanel<?>[] panels = new IPanel<?>[] { p, panel };
		return panels;
	}

	public NavigationView foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}
}