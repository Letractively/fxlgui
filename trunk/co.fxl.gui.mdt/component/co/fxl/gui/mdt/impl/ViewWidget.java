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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.Heights;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.mdt.impl.ViewWidget.ViewConfiguration;

public class ViewWidget implements IUpdateable<ViewConfiguration> {

	// TODO ...

	public class Link implements IClickable<Object>, IUpdateable<String> {

		IImage image;
		private ILabel label;
		private IHorizontalPanel panel;
		private List<ILabel> additionalLabels = new LinkedList<ILabel>();
		private IComboBox cb;

		private Link(IHorizontalPanel panel, IImage image, ILabel textLabel) {
			this.panel = panel;
			this.image = image;
			this.label = textLabel;
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
			if (cb != null)
				cb.visible(!clickable);
			return this;
		}

		@Override
		public boolean clickable() {
			return label.clickable();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<Object> addClickListener(
				final co.fxl.gui.api.IClickable.IClickListener c0) {
			IClickListener clickListener = new IClickListener() {
				@Override
				public void onClick() {
					for (Link l : links)
						l.clickable(l != Link.this);
					c0.onClick();
				}
			};
			label.addClickListener(clickListener);
			if (image != null)
				image.addClickListener(clickListener);
			panel.addClickListener(clickListener);
			return null;
		}

		public void imageResource(String string) {
			image.resource(string);
		}

		public void width(int i) {
			panel.width(i);
		}

		void comboBox(IComboBox cb) {
			this.cb = cb;
		}

		@Override
		public IUpdateable<String> addUpdateListener(
				co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
			if (cb != null)
				cb.addUpdateListener(listener);
			return this;
		}

		public String cbText() {
			if (cb == null)
				return null;
			if (cb.text().equals(PLEASE_CHOOSE_FILTER))
				return null;
			return cb.text();
		}
	}

	public enum ViewType {
		TABLE, DETAILS;
	}

	public enum ActionType {
		VIEW_CHANGED, CONFIGURATION_CHANGED, REFRESH;
	}

	public class ViewConfiguration {

		public ViewType viewType;
		public String configuration;
		public ActionType viewChanged;

		public String toString() {
			return viewType + " " + configuration + " " + viewChanged;
		}
	}

	private static final String PLEASE_CHOOSE_FILTER = "Please choose filter";
	private static final Heights HEIGHTS = new Heights(0);
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private List<Link> links = new LinkedList<Link>();
	private List<IUpdateListener<ViewConfiguration>> listeners = new LinkedList<IUpdateListener<ViewConfiguration>>();

	public ViewWidget(ILayout layout, List<String> configurations) {
		widgetTitle = new WidgetTitle(layout, true);
		widgetTitle.space(2);
		widgetTitle.addTitle("VIEWS");
		panel = widgetTitle.content().panel().vertical().spacing(6);
		final Link table = addComboBoxLink("grid.png", "Table",
				configurations.get(0), configurations);
		List<String> options = new LinkedList<String>(configurations);
		if (!options.isEmpty())
			options.add(0, PLEASE_CHOOSE_FILTER);
		final Link details = addComboBoxLink("details.png", "Detail",
				PLEASE_CHOOSE_FILTER, options);
		links.add(table);
		table.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				fire(ViewType.TABLE, table, ActionType.VIEW_CHANGED);
			}
		});
		table.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				fire(ViewType.TABLE, table, ActionType.CONFIGURATION_CHANGED);
			}
		});
		links.add(details);
		details.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				fire(ViewType.DETAILS, details, ActionType.VIEW_CHANGED);
			}
		});
		details.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				fire(ViewType.DETAILS, details,
						ActionType.CONFIGURATION_CHANGED);
			}
		});
		table.clickable(false);
		details.clickable(true);
		widgetTitle.addHyperlink("refresh.png", "Refresh").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						fire(details.clickable() ? ViewType.TABLE
								: ViewType.DETAILS, details.clickable() ? table
								: details, ActionType.REFRESH);
					}
				});
	}

	private void fire(ViewType viewType, Link link, ActionType b) {
		for (IUpdateListener<ViewConfiguration> listener : listeners) {
			ViewConfiguration cfg = new ViewConfiguration();
			cfg.viewType = viewType;
			cfg.configuration = link.cbText();
			cfg.viewChanged = b;
			listener.onUpdate(cfg);
		}
	}

	@Override
	public IUpdateable<ViewConfiguration> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<ViewConfiguration> listener) {
		listeners.add(listener);
		return this;
	}

	protected ILabel addTextLabel(IHorizontalPanel panel) {
		return panel.add().label().hyperlink();
	}

	Link addComboBoxLink(String imageResource, String title, String option,
			List<String> options) {
		IHorizontalPanel panel = addPanel();
		IHorizontalPanel panel1 = panel.add().panel().horizontal();
		IImage image = addImage(panel1, imageResource);
		ILabel textLabel = addTextLabel(panel);
		Link l = new Link(panel1, image, textLabel);
		l.text(title);
		if (!options.isEmpty()) {
			IComboBox cb = panel.addSpace(8).add().comboBox();
			HEIGHTS.decorate(cb);
			cb.width(222);
			for (String o : options)
				cb.addText(o);
			cb.text(option);
			l.comboBox(cb);
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
		IVerticalPanel p = panel.add().panel().vertical();
		IHorizontalPanel panel = p.add().panel().horizontal().align().begin()
				.add().panel().horizontal().align().begin();
		return panel;
	}

	public ViewWidget foldable(boolean b) {
		widgetTitle.foldable(b);
		return this;
	}
}