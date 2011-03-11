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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
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
			label.hyperlink();
		}

		public Link text(String text) {
			label.text(text);
			return this;
		}

		public Link clickable(boolean clickable) {
			label.clickable(clickable);
			for (ILabel l : additionalLabels)
				l.clickable(clickable);
			// if (clickable)
			// label.font().color().blue();
			// else
			// label.font().color().gray();
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

	public NavigationView(ILayout layout) {
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.grayBackground();
	}

	public Link addHyperlink() {
		return addHyperlink(null);
	}

	public NavigationView addLine() {
		setUp();
		panel.addSpace(2);
		// IVerticalPanel p = panel.add().panel().vertical().height(1);
		// p.color().lightgray();
		panel.addSpace(2);
		return this;
	}

	public Link addHyperlink(String imageResource) {
		setUp();
		IHorizontalPanel panel = this.panel.add().panel().horizontal().align().begin().add()
				.panel().horizontal().align().begin();
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
		final ILabel textLabel = panel.add().label();// .hyperlink();
		textLabel.font().pixel(13);// .weight().bold();
		return new Link(panel, image, textLabel);
	}

	private void setUp() {
		if (panel != null)
			return;
		widgetTitle.addTitle("Navigation");
		panel = widgetTitle.content().panel().vertical().spacing(2);
	}

	public NavigationView foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}
}