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
package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IResizable.IResizeListener;

public class ResizableWidgetTemplate implements IResizableWidget {

	private Map<String, ResizableWidgetTemplate> children = new HashMap<String, ResizableWidgetTemplate>();
	protected Size size = new Size(0, 0, 0, 0);

	@Override
	public IResizableWidget size(Size size) {
		this.size = size;
		return this;
	}

	@Override
	public IResizableWidget addResizableWidgetToDisplay() {
		return addResizableWidgetToDisplay(null);
	}

	@Override
	public IResizableWidget addResizableWidgetToDisplay(IElement<?> link) {
		IResizeConfiguration cfg = StatusDisplay.instance().addResizeListener(
				new IResizeListener() {
					@Override
					public void onResize(int width, int height) {
						recursiveResize(rwidth(), rheight());
					}
				});
		if (link != null)
			cfg.linkLifecycle(link);
		return this;
	}

	protected final int rwidth() {
		int w = StatusDisplay.instance().width() - size.widthDecrement;
		return w < size.minWidth ? size.minWidth : w;
	}

	protected final int rheight() {
		int h = StatusDisplay.instance().height() - size.heightDecrement;
		return h < size.minHeight ? size.minHeight : h;
	}

	@Override
	public IResizableWidget setResizableWidget(IResizableWidget widget,
			String id) {
		ResizableWidgetTemplate child = (ResizableWidgetTemplate) widget;
		child.size = size;
		children.put(id, child);
		return this;
	}

	public void recursiveResize(int width, int height) {
		ResizableWidgetTemplate.this.onResize(width, height);
		for (ResizableWidgetTemplate child : children.values()) {
			child.recursiveResize(width, height);
		}
	}

	@Override
	public void onResize(int width, int height) {
	}

	public void autoResize(final IElement<?> e, String id) {
		autoResize(e, 0, id);
	}

	private void autoResize(final IElement<?> e, final int dec, String id) {
		autoResize(e, dec, false, id);
	}

	public void autoResize(final IElement<?> e, final int dec, boolean b,
			String id) {
		final IResizeListener listener = new IResizeListener() {
			@Override
			public void onResize(int width, int height) {
				int offsetY = size.defined() ? 0 : StatusDisplay.instance()
						.offsetY(e, 100);
				int h = rheight() - offsetY - 10 - dec;
				if (h > 0)
					e.height(h);
			}
		};
		setResizableWidget(new ResizableWidgetTemplate() {
			@Override
			public void onResize(int width, int height) {
				listener.onResize(width, height);
			}
		}, id);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				StatusDisplay.instance().fire(listener);
			}
		};
		if (b) {
			Display.instance().invokeLater(runnable);
		} else
			runnable.run();
	}
}
