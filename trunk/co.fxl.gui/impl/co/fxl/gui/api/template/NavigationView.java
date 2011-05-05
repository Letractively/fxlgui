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
package co.fxl.gui.api.template;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;

public class NavigationView {

	public class Link implements IClickable<Object> {

		IImage image;
		private ILabel label;
		private IHorizontalPanel panel;
		private List<ILabel> additionalLabels = new LinkedList<ILabel>();

		private Link(IHorizontalPanel panel, IImage image, ILabel textLabel) {
			this.panel = panel;
			this.image = image;
			this.label = textLabel;
			clickable(true);
			// label.hyperlink();
		}

		public Link text(String text) {
			label.text(text);
			return this;
		}

		public Link clickable(boolean clickable) {
			if (image != null)
				image.clickable(clickable);
			label.clickable(clickable);
			for (ILabel l : additionalLabels)
				l.clickable(clickable);
			if (clickable)
				label.font().color().rgb(0, 87, 141);
			else
				label.font().color().mix().gray().lightgray();
			return this;
		}

		@Override
		public boolean clickable() {
			return label.clickable();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			label.addClickListener(clickListener);
			return null;
		}

		public ILabel addHyperlink(String text) {
			panel.addSpace(4);
			panel.add().label().text("|").font().color().gray();
			ILabel l = panel.addSpace(4).add().label().text(text).hyperlink();
			additionalLabels.add(l);
			return l;
		}

		public void imageResource(String string) {
			image.resource(string);
		}
	}

	private static final String LINK_PNG = "link.png";
	private static final boolean SHOW_NUMBERS = false;
	private static final boolean SHOW_TRIANGLE = true;
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private int index = 1;
	private boolean hasLinks;

	public NavigationView(ILayout layout) {
		widgetTitle = new WidgetTitle(layout, true);
		widgetTitle.space(2);
		widgetTitle.grayBackground();
	}

	private void setUp() {
		if (panel != null)
			return;
		widgetTitle.addTitle("NAVIGATION");
		panel = widgetTitle.content().panel().vertical().spacing(6);
	}

	public NavigationView navigationViewListener(final INavigationListener l) {
		if (l == null)
			return this;
		IClickable<?> back = widgetTitle.addHyperlink("back.png", "Back");
		back.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				l.previous();
			}
		});
		back.clickable(l.hasPrevious());
		IClickable<?> forward = widgetTitle.addHyperlink("forward.png",
				"Forward");
		forward.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				l.next();
			}
		});
		forward.clickable(l.hasNext());
		return this;
	}

	public Link addHyperlink() {
		return addHyperlink(null);
	}

	public NavigationView addLine() {
		setUp();
		panel.add().line().color().rgb(172, 197, 213);
		return this;
	}

	public Link addHyperlink(String imageResource) {
		setUp();
		IHorizontalPanel panel = addPanel();
		IImage image = addImage(panel, imageResource);
		ILabel textLabel = addTextLabel(panel);
		return new Link(panel, image, textLabel);
	}

	protected ILabel addTextLabel(IHorizontalPanel panel) {
		final ILabel textLabel = panel.add().label();// .hyperlink();
		textLabel.font();// .pixel(13);// .weight().bold();
		return textLabel;
	}

	public IUpdateable<String> addComboBoxLink(String title, String text,
			String... options) {
		setUp();
		IHorizontalPanel panel = addPanel();
		addImage(panel, null);
		ILabel textLabel = addTextLabel(panel);
		textLabel.font().weight().bold();
		textLabel.text(title);
		IComboBox cb = panel.addSpace(8).add().comboBox();
		cb.width(202);
		cb.addText(options);
		cb.text(text);
		return cb;
	}

	protected IImage addImage(IHorizontalPanel panel, String imageResource) {
		IImage image = null;
		if (SHOW_NUMBERS) {
			String s = String.valueOf(index++) + ".";
			panel.add().label().text(s).font().pixel(13).color().gray();
			panel.addSpace(4);
		} else if (SHOW_TRIANGLE) {
			image = panel.add().image()
					.resource(imageResource == null ? LINK_PNG : imageResource);
			panel.addSpace(4);
		}
		return image;
	}

	protected IHorizontalPanel addPanel() {
		IVerticalPanel p = this.panel.add().panel().vertical().spacing(2);
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
		return panel;
	}

	public NavigationView foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}
}