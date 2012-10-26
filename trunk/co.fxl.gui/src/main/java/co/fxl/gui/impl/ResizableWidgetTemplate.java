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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IResizable.IResizeListener;

public class ResizableWidgetTemplate implements IResizableWidget {

	private List<ResizableWidgetTemplate> children = new LinkedList<ResizableWidgetTemplate>();

	@Override
	public IResizableWidget addResizableWidgetToDisplay() {
		StatusDisplay.instance().addResizeListener(new IResizeListener() {
			@Override
			public void onResize(int width, int height) {
				recursiveResize(width, height);
			}
		});
		return this;
	}

	@Override
	public IResizableWidget addResizableWidget(IResizableWidget widget) {
		children.add((ResizableWidgetTemplate) widget);
		return this;
	}

	@Override
	public IResizableWidget setResizableWidget(IResizableWidget widget) {
		children.clear();
		return addResizableWidget(widget);
	}

	public void recursiveResize(int width, int height) {
		ResizableWidgetTemplate.this.onResize(width, height);
		for (ResizableWidgetTemplate child : children) {
			child.recursiveResize(width, height);
		}
	}

	@Override
	public void onResize(int width, int height) {
	}

	public void autoResize(final IElement<?> e) {
		autoResize(e, 0);
	}

	private void autoResize(final IElement<?> e, final int dec) {
		autoResize(e, dec, false);
	}

	public void autoResize(final IElement<?> e, final int dec, boolean b) {
		final IResizeListener listener = new IResizeListener() {
			@Override
			public void onResize(int width, int height) {
				int offsetY = StatusDisplay.instance().offsetY(e, 100);
				int h = height - offsetY - 10 - dec;
				if (h > 0)
					e.height(h);
			}
		};
		// if (!SINGLE_RESIZE_LISTENER)
		addResizableWidget(new ResizableWidgetTemplate() {
			@Override
			public void onResize(int width, int height) {
				listener.onResize(width, height);
			}
		});
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
