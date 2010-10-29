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

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;

public class ViewList {

	public class ViewImpl implements IClickListener {

		private ILabel label;
		private IHorizontalPanel labelPanel;
		private WidgetTitle gadget;
		private WidgetTitleDecorator decorator;

		ViewImpl() {
			labelPanel = ViewList.this.panel.add().panel().horizontal().add()
					.panel().horizontal().spacing(2);
			labelPanel.addSpace(4);
			label = labelPanel.add().label().hyperlink();
			label.addClickListener(this);
			label.font().pixel(13);
			labelPanel.addSpace(4);
			ILayout content = widget.contentPanel().add().panel();
			gadget = new WidgetTitle(content);
		}

		public ViewImpl title(String title) {
			label.text(title);
			return this;
		}

		@Override
		public void onClick() {
			for (ViewList viewList : widget.viewLists) {
				for (ViewImpl view : viewList.views) {
					view.clickable(view != this);
				}
			}
			widget.contentPanel().show(gadget.panel);
			if (decorator != null) {
				decorator.decorate(gadget);
				decorator = null;
			}
		}

		private void clickable(boolean clickable) {
			label.clickable(clickable);
			if (!clickable) {
				labelPanel.color().red();
				label.font().weight().bold().color().white();
			} else {
				labelPanel.color().rgb(245, 245, 245);
				label.font().weight().plain().color().blue();
			}
		}

		public ViewImpl decorator(WidgetTitleDecorator decorator) {
			this.decorator = decorator;
			return this;
		}
	}

	private MetaViewList widget;
	WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private List<ViewImpl> views = new LinkedList<ViewImpl>();

	public ViewList(MetaViewList widget, ILayout layout) {
		this.widget = widget;
		widgetTitle = new WidgetTitle(layout);
	}

	public ViewImpl addView() {
		if (widgetTitle.headerLabel == null)
			title("Views");
		if (panel == null)
			panel = widgetTitle.content().panel().vertical().spacing(2);
		ViewImpl view = new ViewImpl();
		views.add(view);
		return view;
	}

	public ViewList modifiable() {
		throw new MethodNotImplementedException();
	}

	public ViewList title(String title) {
		widgetTitle.addTitle(title);
		return this;
	}

	public ViewList foldable(boolean foldable) {
		widgetTitle.foldable(foldable);
		return this;
	}
}
