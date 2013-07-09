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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.impl.ContextMenu.Entry;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.style.api.IStyle;
import co.fxl.gui.style.api.IStyle.IViewSelection.IView;

public abstract class StyleTemplate implements IStyle {

	public static String PLEASE_CHOOSE_FILTER_STRING = "Click to choose filter";
	static final String VIEWS = "Views";
	static int WIDTH_COMBOBOX = 200;

	abstract class ViewTemplate extends LazyClickListener implements IView {

		IComboBox cb;
		IHorizontalPanel cancelP;
		private IUpdateListener<String> ul;
		boolean comboBoxVisible;
		private final IHorizontalPanel panel0;
		private IHorizontalPanel loadingPanel;
		private IUpdateListener<String> updateListener;
		private boolean firstTimeLoad = true;
		private Entry entry;
		private List<IClickListener> clickListeners = new LinkedList<IClickListener>();

		ViewTemplate(IHorizontalPanel panel0, boolean isDiscardChanges) {
			super(isDiscardChanges);
			this.panel0 = panel0;
		}

		@Override
		protected final void onAllowedClick() {
			showActive();
			for (IClickListener l : clickListeners)
				l.onClick();
		}

		@Override
		public final co.fxl.gui.api.IClickable.IKey<IView> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			clickListeners.add(clickListener);
			return null;
		}

		@Override
		public IView clickable(boolean clickable) {
			entry.clickable(clickable);
			return this;
		}

		void addEntry(String text, String widgetTitle, String imageResource) {
			entry = co.fxl.gui.impl.Page.instance().contextMenu()
					.group(widgetTitle).addEntry(text);
			entry.imageResource(imageResource);
			entry.addClickListener(this);
		}

		@Override
		public final boolean isActive() {
			return !clickable();
		}

		@Override
		public final void comboBox(final IComboBox cb,
				final IHorizontalPanel p, boolean comboBoxVisible) {
			this.cb = cb;
			this.comboBoxVisible = comboBoxVisible;
			cb.visible(comboBoxVisible);
			if (p != null) {
				p.addSpace(4).add().image().resource("cancel.png").size(16, 16)
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								cb.text(PLEASE_CHOOSE_FILTER_STRING);
							}
						});
				ul = new IUpdateListener<String>() {
					@Override
					public void onUpdate(String value) {
						if (value.equals(PLEASE_CHOOSE_FILTER_STRING)) {
							p.visible(false);
							cb.width(WIDTH_COMBOBOX);
						} else {
							p.visible(true);
							cb.width(WIDTH_COMBOBOX - 20);
						}
					}
				};
				cb.addUpdateListener(ul);
				ul.onUpdate(cb.text());
			}
			if (updateListener != null) {
				cb.addUpdateListener(updateListener);
				updateListener = null;
			}
			removeLoadingIcon();
			style(cb);
		}

		void style(IComboBox cb) {
		}

		@Override
		public final IUpdateable<String> addUpdateListener(
				co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
			if (cb != null)
				cb.addUpdateListener(listener);
			else
				updateListener = listener;
			return this;
		}

		@Override
		public final String cbText() {
			if (cb == null)
				return null;
			if (cb.text() == null
					|| cb.text().equals(PLEASE_CHOOSE_FILTER_STRING))
				return null;
			return cb.text();
		}

		@Override
		public final void showLoadingIcon() {
			loadingPanel = panel0.add().panel().horizontal().addSpace(4);
			loadingPanel.add().image().resource("loading_black.gif");
		}

		@Override
		public final void removeLoadingIcon() {
			if (loadingPanel != null)
				loadingPanel.clear().remove();
			loadingPanel = null;
			firstTimeLoad = false;
		}

		@Override
		public IComboBox addComboBox() {
			return cb = panel0.addSpace(space()).add().comboBox();
		}

		int space() {
			return 6;
		}

		@Override
		public final IHorizontalPanel panel0() {
			return panel0;
		}

		@Override
		public final IComboBox cb() {
			return cb;
		}

		@Override
		public final boolean firstTimeLoad() {
			return firstTimeLoad;
		}
	}

}
