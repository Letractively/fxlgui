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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.impl.DialogImpl;
import co.fxl.gui.impl.DiscardChangesDialog;

public class SwingDisplay implements IDisplay, ComponentParent {

	SwingContainer<JComponent> container;
	JFrame frame = new JFrame();
	private int widthPixel = 320;
	private int heightPixel = 240;
	private Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();
	private SwingUncaughtExceptionHandler uncaughtExceptionHandler;
	boolean waiting;
	private Map<IResizeListener, ComponentAdapter> resizeListeners = new HashMap<IResizeListener, ComponentAdapter>();
	private static SwingPopUp popUp;
	private static SwingDisplay instance = null;
	static int lastClickX = 0;
	static int lastClickY = 0;

	private SwingDisplay() {
		container = new SwingContainer<JComponent>(this) {

			void setComponent(JComponent component) {
				super.component = component;
				if (component instanceof JScrollPane)
					((JScrollPane) component)
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.setContentPane(container.component);
				container.component.setBackground(Color.WHITE);
				updateSize();
			}
		};
		// layout = container.panel();
		// frame.setSize(widthPixel, heightPixel);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(widthPixel, heightPixel));
		// scrollPane = new JScrollPane(container.component);
		// scrollPane
		// .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		DiscardChangesDialog.display = this;
		ContextMenu.instance(this);
		// fullscreen();
		// Style.setUp();
	}

	private void resize() {
		frame.pack();
		if (container.component != null) {
			updateSize();
		}
	}

	@Override
	public IDisplay visible(boolean visible) {
		// scrollPane.setVisible(visible);
		container.component.setVisible(visible);
		frame.setVisible(visible);
		return this;
	}

	@Override
	public IDisplay height(int pixel) {
		heightPixel = pixel;
		resize();
		return this;
	}

	@Override
	public IDisplay width(int pixel) {
		widthPixel = pixel;
		resize();
		return this;
	}

	IDisplay display() {
		return this;
	}

	@Override
	public IDisplay title(String title) {
		frame.setTitle(title);
		return this;
	}

	@Override
	public IContainer container() {
		return container;
	}

	@Override
	public IDisplay fullscreen() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		return this;
	}

	public static IDisplay instance() {
		if (instance == null)
			instance = new SwingDisplay();
		return instance;
	}

	@Override
	public IDisplay register(IWidgetProvider<?>... widgetProviders) {
		for (IWidgetProvider<?> widgetProvider : widgetProviders)
			this.widgetProviders.put(widgetProvider.widgetType(),
					widgetProvider);
		return this;
	}

	@Override
	public IDialog showDialog() {
		return new DialogImpl(this);
	}

	@Override
	public IWebsite showWebsite() {
		return new SwingWebsite();
	}

	@Override
	public IColor color() {
		return new SwingColor() {

			@Override
			protected void setColor(Color color) {
				frame.setBackground(color);
				// scrollPane.setBackground(color);
			}
		};
	}

	@Override
	public IDisplay addExceptionHandler(IExceptionHandler handler) {
		SwingUncaughtExceptionHandler h = setUpUncaughtExceptionHandler();
		h.add(handler);
		return this;
	}

	private SwingUncaughtExceptionHandler setUpUncaughtExceptionHandler() {
		if (uncaughtExceptionHandler == null) {
			uncaughtExceptionHandler = new SwingUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
		}
		return uncaughtExceptionHandler;
	}

	@Override
	public void add(JComponent component) {
		throw new MethodNotImplementedException();
	}

	@Override
	public SwingDisplay lookupSwingDisplay() {
		return this;
	}

	@Override
	public void remove(JComponent component) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		return widgetProviders.get(interfaceClass);
	}

	@Override
	public IDisplay addResizeListener(final IResizeListener listener) {
		ComponentAdapter adp = new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				boolean active = listener.onResize(width(), height());
				if (!active) {
					removeResizeListener(listener);
				}
			}
		};
		resizeListeners.put(listener, adp);
		container.component.addComponentListener(adp);
		return this;
	}

	@Override
	public int height() {
		if (container.component == null) {
			return Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		return container.component.getHeight();
	}

	@Override
	public int width() {
		if (container.component == null) {
			return Toolkit.getDefaultToolkit().getScreenSize().width;
		}
		return container.component.getWidth();
	}

	@Override
	public JComponent getComponent() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ICursor cursor() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay block(boolean waiting) {
		this.waiting = waiting;
		frame.setCursor(new Cursor(waiting ? Cursor.WAIT_CURSOR
				: Cursor.DEFAULT_CURSOR));
		// TODO ...
		return this;
	}

	@Override
	public IDisplay invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
		return this;
	}

	@Override
	public boolean supports(Class<?> widgetClass) {
		return widgetProviders.containsKey(widgetClass);
	}

	@Override
	public IPopUp showPopUp() {
		return new SwingPopUp(this);
	}

	@Override
	public IDisplay removeResizeListener(IResizeListener listener) {
		ComponentAdapter adp = resizeListeners.get(listener);
		resizeListeners.remove(listener);
		container.component.removeComponentListener(adp);
		return this;
	}

	@Override
	public IDisplay size(int width, int height) {
		return width(width).height(height);
	}

	private void updateSize() {
		Dimension dim = new Dimension(widthPixel, heightPixel);
		frame.setMinimumSize(dim);
		container.component.setPreferredSize(dim);
		container.component.setSize(widthPixel, heightPixel);
	}

	static void lastClick(MouseEvent e) {
		Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				instance.frame);
		lastClick(p.x, p.y);
	}

	private static void lastClick(int x, int y) {
		if (popUp != null) {
			Rectangle bounds = SwingUtilities.convertRectangle(popUp.p,
					popUp.p.getBounds(), instance.frame);
			if (!bounds.contains(new Point(x, y))) {
				popUp.visible(false);
				popUp = null;
			}
		}
		lastClickX = x;
		lastClickY = y;
	}

	static void popUp(SwingPopUp popUp) {
		SwingDisplay.popUp = popUp;
	}

	public static SwingPopUp popUp() {
		return popUp;
	}

	@Override
	public IDisplay runAsync(Runnable runnable) {
		runnable.run();
		return this;
	}
}
