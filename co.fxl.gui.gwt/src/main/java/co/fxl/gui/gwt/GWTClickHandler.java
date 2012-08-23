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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.impl.KeyTemplate;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.Widget;

class GWTClickHandler<T> extends KeyTemplate<T> {

	static class DoubleClickEventAdp extends DomEventAdp {

		DoubleClickEventAdp(DomEvent<?> original) {
			super(original);
		}

	}

	static class ClickEventAdp extends DomEventAdp {

		ClickEventAdp(DomEvent<?> original) {
			super(original);
		}

	}

	static class KeyPressAdp extends DomEventAdp {

		KeyPressAdp(DomEvent<?> original) {
			super(original);
		}

	}

	static class NativeEventAdp {

		private boolean shift;
		private boolean ctrl;
		private boolean alt;

		private NativeEventAdp(DomEvent<?> original) {
			if (original == null)
				return;
			shift = original.getNativeEvent().getShiftKey();
			ctrl = original.getNativeEvent().getCtrlKey();
			alt = original.getNativeEvent().getAltKey();
		}

		boolean getShiftKey() {
			return shift;
		}

		boolean getCtrlKey() {
			return ctrl;
		}

		boolean getAltKey() {
			return alt;
		}

	}

	static class DomEventAdp {

		private NativeEventAdp nativeEvent;
		private DomEvent<?> original;

		DomEventAdp(DomEvent<?> original) {
			this.original = original;
		}

		DomEvent<?> original() {
			return original;
		}

		NativeEventAdp getNativeEvent() {
			if (nativeEvent == null)
				nativeEvent = new NativeEventAdp(original);
			return nativeEvent;
		}

		void preventDefault() {
			if (original != null)
				original.preventDefault();
		}

		void stopPropagation() {
			if (original != null)
				original.stopPropagation();
		}

	}

	private IClickListener clickListener;
	private boolean stopPropagation = true;

	GWTClickHandler(T element, IClickListener clickListener) {
		this(element, clickListener, true);
	}

	GWTClickHandler(T element, IClickListener clickListener,
			boolean stopPropagation) {
		super(element);
		this.clickListener = clickListener;
		this.stopPropagation = stopPropagation;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public T mouseRight() {
		Widget widget = ((GWTElement) element).container.widget;
		widget.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				if (GWTDisplay.waiting)
					return;
				GWTDisplay.notifyEvent(event);
				clickListener.onClick();
				event.preventDefault();
			}
		}, ContextMenuEvent.getType());
		return (T) super.mouseRight();
	}

	public void onClick(ClickEventAdp event) {
		for (KeyType pressedKey : pressedKeys.keySet()) {
			Boolean check = pressedKeys.get(pressedKey);
			if (keyMatches(pressedKey, event.getNativeEvent()) != check)
				return;
		}
		if (!buttonMatches(event))
			return;
		if (isDoubleClick)
			return;
		if (stopPropagation) {
			event.preventDefault();
			event.stopPropagation();
		}
		if (GWTDisplay.waiting)
			return;
		GWTDisplay.notifyEvent(event.original());
		clickListener.onClick();
	}

	public void onClick(KeyPressAdp event) {
		event.preventDefault();
		event.stopPropagation();
		if (GWTDisplay.waiting)
			return;
		clickListener.onClick();
	}

	public void onDoubleClick(DoubleClickEventAdp event) {
		for (KeyType pressedKey : pressedKeys.keySet()) {
			Boolean check = pressedKeys.get(pressedKey);
			if (keyMatches(pressedKey, event.getNativeEvent()) != check)
				return;
		}
		if (!buttonMatches(event))
			return;
		if (!isDoubleClick)
			return;
		event.preventDefault();
		event.stopPropagation();
		if (GWTDisplay.waiting)
			return;
		clickListener.onClick();
	}

	private boolean buttonMatches(ClickEventAdp event) {
		if (buttonType == ButtonType.LEFT)
			return true;
		else
			return false;
	}

	private boolean buttonMatches(DoubleClickEventAdp event) {
		if (buttonType == ButtonType.LEFT)
			return true;
		else
			return false;
	}

	boolean keyMatches(KeyType key, NativeEventAdp nativeEvent) {
		switch (key) {
		case SHIFT_KEY:
			return nativeEvent.getShiftKey();
		case CTRL_KEY:
			return nativeEvent.getCtrlKey();
		default:
			return nativeEvent.getAltKey();
		}
	}
}