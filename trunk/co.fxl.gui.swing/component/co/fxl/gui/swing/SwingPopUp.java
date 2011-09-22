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
package co.fxl.gui.swing;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.LineBorder;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IWidgetProvider;

class SwingPopUp implements IPopUp {

	private SwingDisplay panel;
	private Popup dialog;
	private int x = 0;
	private int y = 0;
	private boolean center = false;
	protected JComponent component;
	private int w = -1;
	private int h = -1;
	private boolean autoHide = false;
	JPanel p;
	private boolean fitInScreen;
	private List<IUpdateListener<Boolean>> visibleListeners = new LinkedList<IUpdateListener<Boolean>>();
	private boolean visible;

	SwingPopUp(SwingDisplay panel) {
		this.panel = panel;
		p = new JPanel();
		// p.getInsets().set(3, 3, 3, 3);
		p.setBackground(Color.WHITE);
		p.setBorder(new LineBorder(new Color(195, 217, 255), 3));
	}

	@Override
	public IPopUp offset(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public IContainer container() {
		return new SwingContainer<JComponent>(new ComponentParent() {

			@Override
			public void add(JComponent component) {
				SwingPopUp.this.component = component;
			}

			@Override
			public void remove(JComponent component) {
				dialog.hide();
			}

			@Override
			public JComponent getComponent() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return panel.lookupWidgetProvider(interfaceClass);
			}

			@Override
			public SwingDisplay lookupSwingDisplay() {
				return panel;
			}
		});
	}

	@Override
	public IPopUp visible(boolean visible) {
		if (autoHide) {
			if (SwingDisplay.popUp() != null && SwingDisplay.popUp() != this) {
				SwingDisplay.popUp().visible(false);
				SwingDisplay.popUp(null);
			}
			SwingDisplay.popUp(this);
		}
		if (dialog == null)
			if (component != null) {
				if (w > 0)
					component.getPreferredSize().width = w - 12;
				else if (h > 0)
					component.getPreferredSize().height = h;
				PopupFactory factory = PopupFactory.getSharedInstance();
				p.add(component);
				if (center) {
					x = panel.width() / 2
							- Math.max(80, p.getPreferredSize().width / 2);
					y = panel.height() / 2
							- Math.max(40, p.getPreferredSize().height / 2);
				}
				if (fitInScreen) {
					if (y + 20 + p.getPreferredSize().height > SwingDisplay
							.instance().height()) {
						y = SwingDisplay.instance().height() - 30
								- p.getPreferredSize().height;
					}
					if (x + p.getPreferredSize().width > SwingDisplay
							.instance().width()) {
						y = SwingDisplay.instance().width() - 10
								- p.getPreferredSize().width;
					}
				}
				dialog = factory.getPopup(panel.frame, p, x, y + 20);
			}
		assert dialog != null;
		if (visible) {
			dialog.show();
		} else {
			dialog.hide();
			SwingDisplay.popUp(null);
		}
		notifyVisible(visible);
		this.visible = visible;
		return this;
	}

	private void notifyVisible(boolean visible) {
		for (IUpdateListener<Boolean> l : visibleListeners)
			l.onUpdate(visible);
	}

	@Override
	public IPopUp modal(boolean modal) {
		// TODO ...
		return this;
	}

	@Override
	public IPopUp center() {
		this.center = true;
		return this;
	}

	@Override
	public int offsetX() {
		return x;
	}

	@Override
	public int offsetY() {
		return y;
	}

	@Override
	public int width() {
		return component.getWidth();
	}

	@Override
	public int height() {
		return component.getHeight();
	}

	@Override
	public IPopUp size(int w, int h) {
		if (component != null)
			component.setSize(w, h);
		else {
			this.w = w;
			this.h = h;
		}
		return this;
	}

	@Override
	public IPopUp autoHide(boolean autoHide) {
		this.autoHide = autoHide;
		return this;
	}

	@Override
	public IBorder border() {
		return new SwingBorder(p) {

			@Override
			public void remove() {
				super.remove();
			}
		};
	}

	@Override
	public IPopUp width(int width) {
		if (component != null)
			component.getPreferredSize().width = width;
		else {
			this.w = width;
		}
		return this;
	}

	@Override
	public IPopUp height(int height) {
		if (component != null)
			component.getPreferredSize().height = height;
		else {
			this.h = height;
		}
		return this;
	}

	@Override
	public IPopUp atLastClick() {
		offset(SwingDisplay.lastClickX, SwingDisplay.lastClickY);
		return this;
	}

	@Override
	public IPopUp fitInScreen(boolean fitInScreen) {
		this.fitInScreen = fitInScreen;
		return this;
	}

	@Override
	public IPopUp addVisibleListener(IUpdateListener<Boolean> l) {
		visibleListeners.add(l);
		return this;
	}

	@Override
	public boolean visible() {
		return visible;
	}
}
