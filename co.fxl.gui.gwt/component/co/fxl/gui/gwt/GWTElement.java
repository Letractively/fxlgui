/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFontElement;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class GWTElement<T extends Widget, R> implements IElement<R> {

	public GWTContainer<T> container;
	protected HandlerRegistration registration;
	protected HandlerRegistration registration2;
	protected List<GWTClickHandler<R>> handlers = new LinkedList<GWTClickHandler<R>>();

	public GWTElement(GWTContainer<T> container) {
		this.container = container;
	}

	void defaultFont() {
		((IFontElement) this).font().pixel(12).family().arial();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R visible(boolean visible) {
		container.widget.setVisible(visible);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R width(int width) {
		container.widget.setWidth(width + "px");
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(int height) {
		container.widget.setHeight(height + "px");
		return (R) this;
	}

	@Override
	public R size(int width, int height) {
		width(width);
		return height(height);
	}

	@Override
	public int height() {
		return container.widget.getOffsetHeight();
	}

	@Override
	public int width() {
		return container.widget.getOffsetWidth();
	}

	@Override
	public int offsetX() {
		return container.widget.getAbsoluteLeft();
	}

	@Override
	public int offsetY() {
		return container.widget.getAbsoluteTop();
	}

	@Override
	public void remove() {
		container.parent.remove(container.widget);
	}

	@Override
	public Object nativeElement() {
		return container.widget;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R tooltip(String tooltip) {
		container.widget.setTitle(tooltip);
		return (R) this;
	}

	@Override
	public boolean visible() {
		return container.widget.isVisible();
	}

	public IKey<R> addClickListener(IClickListener clickListener) {
		toggleClickHandler(true);
		GWTClickHandler<R> handler = newGWTClickHandler(clickListener);
		handlers.add(handler);
		return handler;
	}

	GWTClickHandler<R> newGWTClickHandler(IClickListener clickListener) {
		throw new MethodNotImplementedException();
	}

	private void toggleClickHandler(boolean toggle) {
		container.widget.removeStyleName("cursor-pointer");
		if (registration != null) {
			registration.removeHandler();
			registration = null;
		}
		if (registration2 != null) {
			registration2.removeHandler();
			registration2 = null;
		}
		if (toggle) {
			container.widget.addStyleName("cursor-pointer");
			registerClickHandler();
		}
	}

	void registerClickHandler() {
		registration = ((HasClickHandlers) container.widget)
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						for (GWTClickHandler<R> handler : handlers) {
							handler.onClick(event);
						}
					}
				});
		registration2 = ((HasDoubleClickHandlers) container.widget)
				.addDoubleClickHandler(new DoubleClickHandler() {
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						for (GWTClickHandler<R> handler : handlers) {
							handler.onDoubleClick(event);
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	public R clickable(boolean enable) {
		toggleClickHandler(enable);
		return (R) this;
	}

	public boolean clickable() {
		return registration != null;
	}

	@Override
	public IDisplay display() {
		return container.display();
	}
}