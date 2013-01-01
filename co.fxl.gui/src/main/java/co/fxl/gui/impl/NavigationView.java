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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;

public class NavigationView implements IColored {

	static String FORWARD = "Forward";
	static String BACK = "Back";
	static String NAVIGATION = "Actions";
	private static final boolean SHOW_NUMBERS = false;
	private static final boolean SHOW_TRIANGLE = true;
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private int index = 1;
	private boolean hasLinks;
	private INavigationListener nl;
	private IClickable<?> back;
	private IClickable<?> forward;
	private boolean addToContextMenu = false;
	private String title;
	private List<IVerticalPanel> parts = new LinkedList<IVerticalPanel>();

	public NavigationView(ILayout layout) {
		this(layout, true);
	}

	public NavigationView(ILayout layout, boolean addToContextMenu) {
		this(layout, addToContextMenu, NAVIGATION);
	}

	public NavigationView(ILayout layout, boolean addToContextMenu, String title) {
		widgetTitle = new WidgetTitle(layout, true).sideWidget(true);
//		widgetTitle.background().gray(248);
		this.addToContextMenu = addToContextMenu;
		if (addToContextMenu)
			widgetTitle.addToContextMenu(addToContextMenu);
		widgetTitle.spacing(2);
		this.title = title;
		setUp();
	}

	private void setUp() {
		if (panel != null)
			return;
		widgetTitle.addTitle(title);
		panel = widgetTitle.content().panel().vertical();
	}

	public NavigationView navigationViewListener(final INavigationListener nl) {
		if (nl == null)
			return this;
		this.nl = nl;
		back = widgetTitle.addHyperlink(Icons.NAVIGATION_BACK, BACK);
		back.addClickListener(new LazyClickListener() {

			@Override
			protected void onAllowedClick() {
				nl.previous();
			}
		});
		forward = widgetTitle.addHyperlink(Icons.NAVIGATION_FORWARD, FORWARD);
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
		ImageButton imageButton = new ImageButton((IVerticalPanel) panels[0],
				panel, image, textLabel).lazyHyperlink();
		if (addToContextMenu)
			imageButton = imageButton.addToContextMenu(title);
		return imageButton;
	}

	protected ILabel addTextLabel(IHorizontalPanel panel) {
		final ILabel textLabel = panel.add().label();
		return textLabel;
	}

	public IUpdateable<String> addComboBoxLink(String title, String text,
			String... options) {
		return addComboBoxLink(null, title, text, options);
	}

	public IUpdateable<String> addComboBoxLink(String img, String title,
			String text, String... options) {
		setUp();
		IPanel<?>[] panels = addPanel();
		IHorizontalPanel panel = (IHorizontalPanel) panels[1];
		addImage(panel, img);
		ILabel label = addTextLabel(panel);
		label.clickable(false);
		// styleContentChoice(label);
		label.font().color().black();
		label.text(title);
		IComboBox cb = addComboBox(text, panel, options);
		return cb;
	}

	public IComboBox addComboBox(String text, IHorizontalPanel panel,
			String... options) {
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
							imageResource == null ? Icons.LINK : imageResource)
					.size(16, 16);
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
		parts.add(p);
		p.padding().left(6).right(6).top(6);
		p.margin().bottom(6);
		// if (hasLinks) {
		// p.addSpace(3);
		// }
		IHorizontalPanel panel = p.add().panel().horizontal();
		if (hasLinks) {
			addBorder(p);
		}
		hasLinks = true;
		IPanel<?>[] panels = new IPanel<?>[] { p, panel };
		return panels;
	}

	private void addBorder(IVerticalPanel p) {
		IBorder border = p.border();
		border.color().rgb(172, 197, 213);
		border.style().top();
	}

	public void updateSeparatorBorders() {
		boolean isFirst = true;
		for (IVerticalPanel p : parts) {
			if (p.visible()) {
				if (isFirst) {
					p.border().remove();
				} else {
					addBorder(p);
				}
				isFirst = false;
			}
		}
	}

	public NavigationView foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}

	@Override
	public IColor color() {
		return widgetTitle.color();
	}
}