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

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class ViewList {

	public interface NewListener {

		void onNew(ViewImpl view, ICallback<Void> cb);

		void onRemove(ViewImpl view);
	}

	public class ViewImpl extends LazyClickListener {

		private ILabel label;
		private IGridPanel grid;
		private IHorizontalPanel labelPanel0;
		private ViewDecorator decorator;
		private IContainer content;
		private Object bo;
		private IImage removeImage;
		private IImage image;
		private IHorizontalPanel labelPanel;
		private String imageResource;

		ViewImpl(String imageResource) {
			this(imageResource, false);
		}

		ViewImpl(String imageResource, boolean isNew) {
			this.imageResource = imageResource;
			grid = ViewList.this.panel.add().panel().grid();
			labelPanel0 = grid.cell(0, 0).panel().horizontal();
			labelPanel = labelPanel0.add().panel().horizontal().spacing(2);
			decorate(isNew);
		}

		protected void decorate(boolean isNew) {
			if (imageResource != null) {
				image = labelPanel.addSpace(4).add().image()
						.resource(imageResource);
				image.addClickListener(this);
				labelPanel.addSpace(2);
			} else
				labelPanel.addSpace(4);
			if (!isNew) {
				label = labelPanel.add().label();
				label.addClickListener(this);
				label.font().pixel(13);
				label.font().weight().plain().color().rgb(0, 87, 141);
				labelPanel.addSpace(4);
				content = widget.contentPanel().add();
				if (newListener != null) {
					removeImage = grid.cell(1, 0).valign().center().width(30)
							.align().end().panel().horizontal().addSpace(4)
							.add().image();
					removeImage.resource("cancel.png").addClickListener(
							new LazyClickListener() {
								@Override
								public void onAllowedClick() {
									remove(grid, ViewImpl.this);
								}
							});
					removeImage.visible(false);
				}
			} else {
				final ITextField tf = labelPanel.add().textField();
				new Heights(0).decorate(tf);
				tf.width(200);
				final IImage accept = labelPanel.addSpace(4).add().image()
						.resource("accept.png")
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								decorate(false);
								title(tf.text().trim());
								newListener.onNew(ViewImpl.this,
										new CallbackTemplate<Void>() {

											@Override
											public void onSuccess(Void result) {
												onAllowedClick();
											}
										});
							}
						}).mouseLeft();
				accept.clickable(false);
				tf.addUpdateListener(new IUpdateListener<String>() {

					@Override
					public void onUpdate(String value) {
						accept.clickable(check(value));
					}
				});
				labelPanel.addSpace(4).add().image().resource("cancel.png")
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								remove(grid, ViewImpl.this);
							}
						}).mouseLeft();
			}
		}

		private boolean check(String value) {
			if (value.trim().length() == 0)
				return false;
			for (int i = 0; i < value.length(); i++)
				if (!(Character.isLetterOrDigit(value.charAt(i)) || Character
						.isSpaceChar(value.charAt(i))))
					return false;
			return true;
		}

		public ViewImpl businessObject(Object bo) {
			this.bo = bo;
			return this;
		}

		public String title() {
			if (label == null)
				return null;
			return label.text();
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
			ICardPanel contentPanel = widget.contentPanel();
			assert contentPanel != null : "ViewList: contentPanel is null";
			IElement<?> element = content.element();
			assert element != null : "ViewList: element is null";
			contentPanel.show(element);
		}

		private void clickable(boolean clickable) {
			label.clickable(clickable);
			if (image != null)
				image.clickable(clickable);
			if (!clickable) {
				labelPanel0.color().red();
				label.font().weight().bold().color().white();
				if (removeImage != null) {
					removeImage.visible(true);
				}
			} else {
				labelPanel0.color().white();
				label.font().weight().plain().color().rgb(0, 87, 141);
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
		widgetTitle = new WidgetTitle(layout, true).space(4).grayBackground();
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
		if (panel == null)
			panel = widgetTitle.content().panel().vertical().spacing(2);
		ViewImpl view = new ViewImpl(imageResource, isNew);
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

	protected void remove(IGridPanel grid, ViewImpl view) {
		grid.remove();
		if (view.title() != null)
			newListener.onRemove(view);
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
