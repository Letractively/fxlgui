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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

public class ViewList {

	public interface NewListener {

		void onNew(ViewImpl view);

		void onRemove(ViewImpl view);
	}

	public class ViewImpl extends LazyClickListener {

		private ILabel label;
		private IGridPanel grid;
		private IHorizontalPanel labelPanel;
		private ViewDecorator decorator;
		private IContainer content;
		private Object bo;
		private IImage removeImage;
		private IImage image;

		ViewImpl(String imageResource) {
			grid = ViewList.this.panel.add().panel().grid();
			labelPanel = grid.cell(0, 0).panel().horizontal().add().panel()
					.horizontal().spacing(2);
			if (imageResource != null) {
				image = labelPanel.addSpace(4).add().image()
						.resource(imageResource);
				image.addClickListener(this);
			}
			labelPanel.addSpace(4);
			label = labelPanel.add().label().hyperlink();
			label.addClickListener(this);
			label.font().pixel(13);
			labelPanel.addSpace(4);
			content = widget.contentPanel().add();
			if (newListener != null) {
				removeImage = grid.cell(1, 0).width(30).align().end().panel()
						.horizontal().add().image();
				removeImage.resource("remove.png").addClickListener(
						new IClickListener() {
							@Override
							public void onClick() {
								grid.remove();
								newListener.onRemove(ViewImpl.this);
								int i = views.indexOf(ViewImpl.this);
								views.remove(ViewImpl.this);
								if (i < views.size()) {
									views.get(i).onClick();
								} else if (!views.isEmpty()) {
									views.get(views.size() - 1).onClick();
								} else
									widget.selectFirst();
							}
						});
				removeImage.visible(false);
			}
		}

		public ViewImpl businessObject(Object bo) {
			this.bo = bo;
			return this;
		}

		public Object businessObject() {
			return bo;
		}

		public ViewImpl title(String title) {
			label.text(title);
			return this;
		}

		@Override
		public void onAllowedClick() {
			for (ViewList viewList : widget.viewLists) {
				for (ViewImpl view : viewList.views) {
					view.clickable(view != this);
				}
			}
			content.clear();
			decorator.decorate(content);
			widget.contentPanel().show(content.element());
		}

		private void clickable(boolean clickable) {
			label.clickable(clickable);
			if (image != null)
				image.clickable(clickable);
			if (!clickable) {
				labelPanel.color().red();
				label.font().weight().bold().color().white();
				if (removeImage != null) {
					removeImage.visible(true);
				}
			} else {
				labelPanel.color().rgb(245, 245, 245);
				label.font().weight().plain().color().blue();
				if (removeImage != null) {
					removeImage.visible(false);
				}
			}
		}

		public ViewImpl decorator(ViewDecorator decorator) {
			this.decorator = decorator;
			return this;
		}
	}

	private MetaViewList widget;
	WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	List<ViewImpl> views = new LinkedList<ViewImpl>();
	private NewListener newListener = null;

	public ViewList(MetaViewList widget, ILayout layout) {
		this.widget = widget;
		widgetTitle = new WidgetTitle(layout).grayBackground();
	}

	public ViewImpl addView() {
		return addView(null);
	}

	public ViewImpl addView(String imageResource) {
		if (widgetTitle.headerLabel == null)
			title("Views");
		if (panel == null)
			panel = widgetTitle.content().panel().vertical().spacing(2);
		ViewImpl view = new ViewImpl(imageResource);
		views.add(view);
		return view;
	}

	public ViewList modifiable(NewListener listener) {
		newListener = listener;
		widgetTitle.addHyperlink("New").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				newListener.onNew(addView());
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
}
