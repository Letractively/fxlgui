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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IWidgetProvider;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public abstract class GWTPanel<T extends Panel, R> extends GWTElement<T, R>
		implements IPanel<R>, WidgetParent {

	private Label placeHolder;

	protected GWTPanel(GWTContainer<T> container) {
		super(container);
		container.widget.add(placeHolder = new Label("<span>&nbsp;</span>"));
	}

	@Override
	void registerClickHandler() {
		container.widget.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		}, ClickEvent.getType());
		registration = container.widget.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for (GWTClickHandler<R> handler : handlers) {
					handler.onClick(event);
				}
			}
		}, ClickEvent.getType());
	}

	@Override
	public ILayout layout() {
		return new GWTLayout(container);
	}

	@Override
	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@Override
	public IColor color() {
		GWTWidgetStyle style = new GWTWidgetStyle("background-color-",
				container.widget);
		return new GWTStyleColor(style) {

			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				stylable.setBackgroundColor(color);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public R clear() {
		container.widget.clear();
		return (R) this;
	}

	@Override
	public IContainer add() {
		if (placeHolder != null) {
			container.widget.remove(placeHolder);
			placeHolder = null;
		}
		return new GWTContainer<Widget>(this);
	}

	@Override
	public abstract void add(Widget widget);

	@Override
	public void remove(Widget widget) {
		container.widget.remove(widget);
	}

	@Override
	public IDisplay display() {
		return container.lookupDisplay();
	}

	@Override
	public GWTDisplay lookupDisplay() {
		return container.lookupDisplay();
	}

	@Override
	public IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		return container.lookupWidgetProvider(interfaceClass);
	}
}
