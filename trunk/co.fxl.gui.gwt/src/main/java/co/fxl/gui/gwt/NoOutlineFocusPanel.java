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
package co.fxl.gui.gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NoOutlineFocusPanel {

	// TODO Look: GWT: IE9: Outline bei Focus-Panel beim Mouse-Wheel-Scrolling

	private FocusPanel p;

	public NoOutlineFocusPanel() {
		if (GWTDisplay.isInternetExplorer()) {
			p = new FocusPanel() {
				@Override
				public void onBrowserEvent(Event event) {
					int eventGetType = DOM.eventGetType(event);
					if (eventGetType == Event.ONFOCUS) {
						setFocus(false);
						return;
					}
					if (eventGetType == Event.ONBLUR)
						return;
					super.onBrowserEvent(event);
				}
			};
			p.addFocusHandler(new FocusHandler() {

				@Override
				public void onFocus(FocusEvent event) {
				}
			});
			p.unsinkEvents(Event.FOCUSEVENTS);
			p.getElement().setAttribute("hideFocus", "true");
		} else {
			p = new FocusPanel();
		}
		p.addStyleName("nooutline");
	}

	public Element getElement() {
		return p.getElement();
	}

	public FocusPanel getWidget() {
		return p;
	}

	public void add(Widget w) {
		p.add(w);
	}

	public void addMouseWheelHandler(MouseWheelHandler mouseWheelHandler) {
		p.addMouseWheelHandler(mouseWheelHandler);
	}

	public void sinkEvents(int onmousewheel) {
		p.sinkEvents(onmousewheel);
	}

	public void setFocus(boolean b) {
		p.setFocus(b);
	}

	public void addKeyUpHandler(KeyUpHandler gwtKeyRecipientKeyTemplate) {
		p.addKeyUpHandler(gwtKeyRecipientKeyTemplate);
	}

	public void addKeyDownHandler(KeyDownHandler gwtKeyRecipientKeyTemplate) {
		p.addKeyDownHandler(gwtKeyRecipientKeyTemplate);
	}

	public void setWidth(String string) {
		p.setWidth(string);
	}

	public void setWidget(VerticalPanel p0) {
		p.setWidget(p0);
	}

	public void setHeight(String string) {
		p.setHeight(string);
	}

	public boolean isVisible() {
		return p.isVisible();
	}

	public boolean isAttached() {
		return p.isAttached();
	}

}
