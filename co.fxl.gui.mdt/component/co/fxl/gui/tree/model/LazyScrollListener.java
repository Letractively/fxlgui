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
package co.fxl.gui.tree.model;

import java.util.Stack;

import co.fxl.gui.api.IScrollPane.IScrollListener;

class LazyScrollListener<T> implements IScrollListener {

	boolean active = false;
	private ModelTreeWidget<T> widget;
	private Stack<ModelTreeNode<T>> todo = new Stack<ModelTreeNode<T>>();
	private boolean running = false;
	private int lastStart = -1;

	LazyScrollListener(ModelTreeWidget<T> widget) {
		this.widget = widget;
	}

	@Override
	public void onScroll(int top) {
		if (!active)
			return;
		lastStart = top;
		if (running) {
			return;
		}
		running = true;
		execute(top);
		running = false;
	}

	public void execute(int top) {
		int bottom = top + widget.splitPane.height();
		System.out.println("drawing " + top + " - " + bottom);
		for (ModelTreeNode<T> n : widget.topLevelNodes) {
			if (n.drawn)
				continue;
			if (n.bottom() > top && n.top() < bottom) {
				todo.add(n);
			}
			if (n.top() > bottom)
				break;
		}
		run();
		if (top != lastStart)
			execute(lastStart);
	}

	private void run() {
		while (!todo.isEmpty()) {
			ModelTreeNode<T> n = todo.pop();
			n.draw();
			widget.topLevelNodes.remove(n);
		}
	}
}
