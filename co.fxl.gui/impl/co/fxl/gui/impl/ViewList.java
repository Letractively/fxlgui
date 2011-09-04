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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

public class ViewList {

	public interface NewListener {

		void onNew(ViewImpl view, ICallback<Void> cb);

		boolean isRemovable(ViewImpl view);

		void onRemove(ViewImpl view, ICallback<Boolean> remove);
	}

	MetaViewList widget;
	WidgetTitle widgetTitle;
	IVerticalPanel panel;
	List<ViewImpl> views = new LinkedList<ViewImpl>();
	NewListener newListener = null;
	boolean hasLinks;

	public ViewList(MetaViewList widget, ILayout layout) {
		this.widget = widget;
		widgetTitle = new WidgetTitle(layout, true).space(2);// .grayBackground();
	}

	public ViewImpl addView() {
		return addView(null);
	}

	public ViewImpl addView(String imageResource) {
		return addView(imageResource, false);
	}

	public ViewImpl addView(String imageResource, boolean isNew) {
		if (widgetTitle.headerLabel == null)
			title("Views");
		if (panel == null) {
			final IGridPanel grid = widgetTitle.content().panel().grid();
			grid.cell(0, 0).width(4).label();
			IVerticalPanel vertical = grid.cell(1, 0).panel().vertical();
			grid.cell(2, 0).width(4).label();
			vertical.addSpace(2);
			panel = vertical.add().panel().vertical().spacing(2);
			vertical.addSpace(4);
		}
		ViewImpl view = new ViewImpl(this, imageResource, isNew);
		views.add(view);
		return view;
	}

	public ViewList modifiable(String icon, final String typeIcon, String name,
			NewListener listener) {
		newListener = listener;
		widgetTitle
				.addHyperlink(icon, "New" + (name == null ? "" : " " + name))
				.addClickListener(new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						addView(typeIcon, true);
					}
				});
		return this;
	}

	public ViewList title(String title) {
		widgetTitle.addTitle(title);
		return this;
	}

	public ViewList foldable(boolean foldable) {
		widgetTitle.foldable(foldable);
		return this;
	}

	protected void remove(final IGridPanel grid, final ViewImpl view,
			final ICallback<Boolean> remove) {
		if (view.title() != null)
			newListener.onRemove(view, new CallbackTemplate<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result)
						removeView(grid, view);
					remove.onSuccess(result);
				}
			});
		else
			removeView(grid, view);
	}

	protected void removeView(IGridPanel grid, ViewImpl view) {
		grid.remove();
		int i = views.indexOf(view);
		views.remove(view);
		if (i < views.size()) {
			views.get(i).onClick();
		} else if (!views.isEmpty()) {
			views.get(views.size() - 1).onClick();
		} else
			widget.selectFirst();
	}
}
