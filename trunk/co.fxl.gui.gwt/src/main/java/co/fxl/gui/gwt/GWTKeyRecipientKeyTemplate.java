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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IKeyRecipient.IKey;
import co.fxl.gui.impl.Display;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FocusPanel;

public class GWTKeyRecipientKeyTemplate implements IKey<Object>,
		IKeyRecipient<Object>, KeyUpHandler, KeyDownHandler, Runnable {

	public int nativeKeyCode = -1;
	public Object element;
	private List<IClickListener> ls = new LinkedList<IClickListener>();
	private boolean running = false;
	private boolean ctrl;

	public GWTKeyRecipientKeyTemplate(Object element) {
		this.element = element;
	}

	public GWTKeyRecipientKeyTemplate(FocusPanel fp) {
		element = fp;
		fp.addKeyUpHandler(this);
		fp.addKeyDownHandler(this);
	}

	public GWTKeyRecipientKeyTemplate(FocusPanel focusPanel,
			IClickListener listener) {
		this(focusPanel);
		ls.add(listener);
	}

	public GWTKeyRecipientKeyTemplate(IClickListener listener) {
		ls.add(listener);
	}

	@Override
	public Object enter() {
		nativeKeyCode = KeyCodes.KEY_ENTER;
		return element;
	}

	@Override
	public Object tab() {
		nativeKeyCode = KeyCodes.KEY_TAB;
		return element;
	}

	@Override
	public Object up() {
		nativeKeyCode = KeyCodes.KEY_UP;
		return element;
	}

	@Override
	public Object down() {
		nativeKeyCode = KeyCodes.KEY_DOWN;
		return element;
	}

	@Override
	public Object left() {
		nativeKeyCode = KeyCodes.KEY_LEFT;
		return element;
	}

	@Override
	public Object right() {
		nativeKeyCode = KeyCodes.KEY_RIGHT;
		return element;
	}

	@Override
	public co.fxl.gui.api.IKeyRecipient.IKey<Object> addKeyListener(
			IClickListener listener) {
		ls.add(listener);
		return this;
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == 17)
			return;
		assert nativeKeyCode != -1;
		int nkc = event.getNativeKeyCode();
		if (ctrl && !event.isControlKeyDown())
			return;
		if (nkc == nativeKeyCode) {
			running = false;
		}
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == 17)
			return;
		assert nativeKeyCode != -1;
		int nkc = event.getNativeKeyCode();
		if (ctrl && !event.isControlKeyDown())
			return;
		if (nkc == nativeKeyCode) {
			running = true;
			run();
		}
	}

	@Override
	public void run() {
		if (!running)
			return;
		if (!((FocusPanel) element).isVisible()
				|| !((FocusPanel) element).isAttached())
			return;
		for (IClickListener l : ls)
			l.onClick();
		Display.instance().invokeLater(this, 200);
	}

	@Override
	public co.fxl.gui.api.IKeyRecipient.IKey<Object> ctrl() {
		ctrl = true;
		return this;
	}

	@Override
	public Object character(char c) {
		nativeKeyCode = c;
		return this;
	}
}
