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
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.impl.ContextMenu.Entry;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.LazyUpdateListener;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.mdt.api.IViewConfiguration;
import co.fxl.gui.mdt.api.IViewConfiguration.ActionType;
import co.fxl.gui.mdt.api.IViewConfiguration.ViewType;

public class ViewWidget implements IUpdateable<IViewConfiguration> {

	// TODO ...

	public class Link implements IClickable<Object>, IUpdateable<String> {

		IImage image;
		private ILabel label;
		private IHorizontalPanel panel;
		private List<ILabel> additionalLabels = new LinkedList<ILabel>();
		private IComboBox cb;
		private Entry entry;

		private Link(IHorizontalPanel panel, IImage image, ILabel textLabel) {
			this.panel = panel;
			this.image = image;
			this.label = textLabel;
		}

		public Link text(String text) {
			label.text(text);
			entry = ContextMenu.instance().addEntry(text);
			entry.imageResource(image.resource());
			return this;
		}

		public Link clickable(boolean clickable) {
			if (image != null)
				image.clickable(clickable);
			label.clickable(clickable);
			styleMDTView(label);
			for (ILabel l : additionalLabels)
				l.clickable(clickable);
			panel.clickable(clickable);
			if (cb != null)
				cb.visible(!clickable);
			entry.clickable(clickable);
			return this;
		}

		public void styleMDTView(ILabel label) {
			// Styles.instance().style(label, Style.MDT.VIEW);
			if (!label.clickable()) {
				label.font().weight().bold().color().black();
			} else {
				label.font().weight().plain();
			}
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
					showActive();
					c0.onClick();
				}
			};
			label.addClickListener(clickListener);
			if (image != null)
				image.addClickListener(clickListener);
			panel.addClickListener(clickListener);
			entry.addClickListener(clickListener);
			return null;
		}

		public void imageResource(String string) {
			image.resource(string);
			entry.imageResource(string);
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

		public void showActive() {
			for (Link l : links)
				l.clickable(l != Link.this);
		}
	}

	private class ViewConfiguration implements IViewConfiguration {

		private ViewType viewType;
		private String configuration;
		private ActionType viewChanged;

		@Override
		public String toString() {
			return viewType + " " + configuration + " " + viewChanged;
		}

		@Override
		public String configuration() {
			return configuration;
		}

		@Override
		public ViewType viewType() {
			return viewType;
		}

		@Override
		public ActionType actionType() {
			return viewChanged;
		}
	}

	private static final String PLEASE_CHOOSE_FILTER = "Click to choose filter";
	private static final Heights HEIGHTS = new Heights(0);
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private List<Link> links = new LinkedList<Link>();
	private List<IUpdateListener<IViewConfiguration>> listeners = new LinkedList<IUpdateListener<IViewConfiguration>>();
	private Link details;
	private Link table;
	private boolean ignoreFire = false;
	private MasterDetailTableWidgetImpl widget;

	public ViewWidget(MasterDetailTableWidgetImpl widget, ILayout layout,
			List<String> configurations, String configuration,
			final boolean optionalForDetail, boolean neverShowFilter) {
		this.widget = widget;
		widgetTitle = new WidgetTitle(layout, true);
		widgetTitle.addToContextMenu(true);
		widgetTitle.space(2);
		widgetTitle.addTitle("VIEWS");
		panel = widgetTitle.content().panel().vertical().spacing(6);
		String tableC = configuration;
		if (tableC == null)
			tableC = configurations.isEmpty() ? null : configurations.get(0);
		table = addComboBoxLink("grid.png", "Table", tableC, configurations);
		List<String> options = new LinkedList<String>(configurations);
		boolean hasOptional = optionalForDetail && !options.isEmpty();
		if (hasOptional)
			options.add(0, PLEASE_CHOOSE_FILTER);
		if (neverShowFilter)
			options.clear();
		String detailC = configuration;
		if (detailC == null)
			detailC = options.isEmpty() ? null : options.get(0);
		details = addComboBoxLink("detail.png", "Detail", detailC, options);
		links.add(table);
		table.addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				fire(ViewType.TABLE, table, ActionType.VIEW_CHANGED);
			}
		});
		table.addUpdateListener(new LazyUpdateListener<String>() {
			@Override
			public void onAllowedUpdate(String value) {
				fire(ViewType.TABLE, table, ActionType.CONFIGURATION_CHANGED);
				if (!optionalForDetail)
					copyComboBox(table, details);
			}
		});
		links.add(details);
		details.addClickListener(new LazyClickListener() {
			@Override
			public void onAllowedClick() {
				fire(ViewType.DETAILS, details, ActionType.VIEW_CHANGED);
			}
		});
		details.addUpdateListener(new LazyUpdateListener<String>() {
			@Override
			public void onAllowedUpdate(String value) {
				fire(ViewType.DETAILS, details,
						ActionType.CONFIGURATION_CHANGED);
				// if (!optionalForDetail)
				// copyComboBox(details, table);
			}
		});
		table.clickable(false);
		details.clickable(true);
		widgetTitle.addHyperlink("refresh.png", "Refresh").addClickListener(
				new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						fire(details.clickable() ? ViewType.TABLE
								: ViewType.DETAILS, details.clickable() ? table
								: details, ActionType.REFRESH);
					}
				});
	}

	protected void copyComboBox(Link l1, Link l2) {
		ignoreFire = true;
		l2.cb.text(l1.cbText());
		ignoreFire = false;
	}

	private void fire(ViewType viewType, Link link, ActionType b) {
		if (ignoreFire)
			return;
		for (IUpdateListener<IViewConfiguration> listener : listeners) {
			ViewConfiguration cfg = new ViewConfiguration();
			cfg.viewType = viewType;
			cfg.configuration = link.cbText();
			cfg.viewChanged = b;
			listener.onUpdate(cfg);
		}
	}

	@Override
	public IUpdateable<IViewConfiguration> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<IViewConfiguration> listener) {
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
		IPanel<?> dummy = panel.add().panel().horizontal().width(1);
		HEIGHTS.decorate(dummy);
		if (!options.isEmpty()) {
			IComboBox cb = panel.addSpace(6).add().comboBox();
			HEIGHTS.decorate(cb);
			cb.width(220);
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

	public ViewWidget visible(boolean b) {
		panel.visible(b);
		return this;
	}

	public void showDetails(boolean notifyListeners) {
		details.showActive();
		if (notifyListeners)
			notifyListeners();
	}

	public void showTable(boolean notifyListeners) {
		table.showActive();
		if (notifyListeners)
			notifyListeners();
	}

	void notifyListeners() {
		ViewConfiguration vc = new ViewConfiguration();
		vc.viewChanged = ActionType.VIEW_CHANGED;
		widget.notifyViewListeners(vc);
	}
}