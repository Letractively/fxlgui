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
package co.fxl.gui.mdt.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.Styles;
import co.fxl.gui.api.template.WidgetTitle;

public class ViewWidget {

	// TODO ...

	private static final String PLEASE_CHOOSE_FILTER = "Please choose filter";

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
			panel.clickable(clickable);
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
			if (image != null)
				image.addClickListener(clickListener);
			panel.addClickListener(clickListener);
			return null;
		}

		public ILabel addHyperlink(String text) {
			panel.addSpace(4);
			ILabel label = panel.add().label().text("|");
			Styles.instance().separator(label);
			ILabel l = panel.addSpace(4).add().label().text(text).hyperlink();
			additionalLabels.add(l);
			return l;
		}

		public void imageResource(String string) {
			image.resource(string);
		}

		public void width(int i) {
			panel.width(i);
		}
	}

	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private boolean hasLinks;

	public ViewWidget(ILayout layout, List<String> configurations) {
		widgetTitle = new WidgetTitle(layout, true);
		widgetTitle.space(2);
		addComboBoxLink("grid.png", "Table", configurations.get(0),
				configurations);
		List<String> options = new LinkedList<String>(configurations);
		if (!options.isEmpty())
			options.add(0, PLEASE_CHOOSE_FILTER);
		addComboBoxLink("detail.png", "Detail", PLEASE_CHOOSE_FILTER, options);
		widgetTitle.addTitle("VIEWS");
		panel = widgetTitle.content().panel().vertical().spacing(6);
	}

	public Link addHyperlink(String imageResource) {
		IHorizontalPanel panel = addPanel();
		IImage image = addImage(panel, imageResource);
		ILabel textLabel = addTextLabel(panel);
		return new Link(panel, image, textLabel);
	}

	protected ILabel addTextLabel(IHorizontalPanel panel) {
		final ILabel textLabel = panel.add().label().hyperlink();
		return textLabel;
	}

	Link addComboBoxLink(String imageResource, String title, String option,
			List<String> options) {
		IHorizontalPanel panel = addPanel();
		IHorizontalPanel panel1 = panel.add().panel().horizontal();
		IImage image = addImage(panel1, imageResource);
		ILabel textLabel = addTextLabel(panel);
		panel1.width(80);
		Link l = new Link(panel1, image, textLabel);
		l.text(title);
		if (!options.isEmpty()) {
			IComboBox cb = panel.addSpace(8).add().comboBox();
			cb.width(202);
			for (String o : options)
				cb.addText(o);
			cb.text(option);
		}
		return l;
	}

	protected IImage addImage(IHorizontalPanel panel, String imageResource) {
		IImage image = null;
		image = panel.add().image().resource(imageResource);
		panel.addSpace(4);
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

	public ViewWidget foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}
}