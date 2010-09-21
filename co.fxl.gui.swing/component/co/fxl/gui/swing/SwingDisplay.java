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
import java.awt.Dimension;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.IColored.IColor;

public class SwingDisplay implements IDisplay {

	private SwingContainer<JPanel> container;
	JFrame frame = new JFrame();
	private int widthPixel = 800;
	private int heightPixel = 600;
	private Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();
	private JScrollPane scrollPane;
	private ILayout layout;
	private static SwingDisplay instance = null;

	private SwingDisplay() {
		container = new SwingContainer<JPanel>(null) {
			@Override
			IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
				return SwingDisplay.this.widgetProviders.get(interfaceClass);
			}

			@Override
			SwingDisplay lookupSwingDisplay() {
				return SwingDisplay.this;
			}
		};
		layout = container.panel();
		frame.setSize(widthPixel, heightPixel);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scrollPane = new JScrollPane(container.component);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.setContentPane(scrollPane);
		scrollPane.setPreferredSize(new Dimension(widthPixel, heightPixel));
		container.component.setBackground(Color.WHITE);
	}

	private void resize() {
		frame.pack();
		frame.setSize(widthPixel, heightPixel);
	}

	@Override
	public IDisplay visible(boolean visible) {
		scrollPane.setVisible(visible);
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
	public ILayout layout() {
		return layout;
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
		return new SwingDialog(this);
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
				scrollPane.setBackground(color);
			}
		};
	}

	@Override
	public IDisplay addExceptionHandler(IExceptionHandler handler) {
		throw new MethodNotImplementedException();
	}
}
