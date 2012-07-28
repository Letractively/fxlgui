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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.impl.HTMLText;
import co.fxl.gui.impl.HyperlinkDecorator;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HTML;

class GWTLabel extends GWTElement<HTML, ILabel> implements ILabel {

	private HTMLText html = new HTMLText();
	// private boolean selectionDisabled = false;
	private HyperlinkDecorator hyperlinkDecorator;

	GWTLabel(GWTContainer<HTML> container) {
		super(container);
		container.widget.addStyleName("gwt-Label-FXL");
		// container.widget.setWordWrap(false);
		// defaultFont();
	}

	private void update() {
		container.widget.setHTML(html.toString());
	}

	@Override
	public ILabel text(String text) {
		setLabelText(text);
		return this;
	}

	private void setLabelText(String text) {
		html.allowHTML(false);
		html.setText(text);
		container.widget.setHTML(html.toString());
	}

	@Override
	public ILabel html(String string) {
		html.allowHTML(true);
		html.setText(string);
		container.widget.setHTML(html.toString());
		return this;
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public IClickable.IKey<ILabel> addClickListener(IClickListener clickListener) {
		IClickable.IKey<ILabel> key = super.addClickListener(clickListener);
		if (hyperlinkDecorator != null) {
			hyperlinkDecorator.clickable(clickable());
		}
		return key;
	}

	@Override
	public ILabel clickable(boolean enable) {
		ILabel clickable = super.clickable(enable);
		if (hyperlinkDecorator != null) {
			hyperlinkDecorator.clickable(enable);
		}
		return clickable;
	}

	@Override
	public ILabel hyperlink() {
		hyperlinkDecorator = new HyperlinkDecorator(this);
		return this;
	}

	// native String disableSelection(Element target)
	/*-{
	if (typeof target.onselectstart!="undefined") //IE route
	target.onselectstart=function(){return false}
	else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
	target.style.MozUserSelect="none"
	else //All other route (ie: Opera)
	target.onmousedown=function(){return false}
	target.style.cursor = "default"
	}-*/;

	@Override
	GWTClickHandler<ILabel> newGWTClickHandler(IClickListener clickListener) {
		html.selectable = false;
		// if (!selectionDisabled) {
		// disableSelection(container.widget.getElement());
		// selectionDisabled = true;
		// }
		update();
		return new GWTClickHandler<ILabel>(this, clickListener);
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public ILabel tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public ILabel autoWrap(boolean autoWrap) {
		container.widget.setWordWrap(autoWrap);
		return this;
	}

	@Override
	public ILabel breakWord(boolean breakWord) {
		if(!breakWord)
		style().clearProperty("wordWrap");
		else
		container.widget.getElement().getStyle().setProperty("wordWrap", "break-word");
		return this;
	}

	@Override
	public ILabel addMouseOverListener(final IMouseOverListener l) {
		container.widget.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				l.onMouseOver();
			}
		});
		container.widget.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				l.onMouseOut();
			}
		});
		return this;
	}
}
