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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;

public class ToolbarImpl implements IToolbar {

	public static boolean ADJUST_HEIGHTS = false;
	private static int SPACING = 4;
	private IFlowPanel panel;
	private ToolbarImpl parent;
	private Map<Object, IHorizontalPanel> blockers = new HashMap<Object, IHorizontalPanel>();
	private List<Object> content = new LinkedList<Object>();
	private IHorizontalPanel blocker;
	private Map<Object, IVerticalPanel> mainPanels = new HashMap<Object, IVerticalPanel>();
	private boolean hasContent = false;
	private int spacing = SPACING;
	public static boolean ALLOW_ALIGN_END_FOR_FLOW_PANEL = true;
	private int height = 40;

	public ToolbarImpl(IContainer container) {
		panel = container.panel().flow();
		panel.margin().bottom(-4);
	}

	ToolbarImpl root() {
		if (parent != null)
			return parent.root();
		return this;
	}

	private ToolbarImpl(ToolbarImpl parent) {
		this.parent = parent;
	}

	@Override
	public ToolbarImpl height(int height) {
		this.height = height;
		return this;
	}

	@Override
	public IMargin margin() {
		return panel.margin();
	}

	@Override
	public IContainer add() {
		return addElement().container();
	}

	@Override
	public IToolbarElement addElement() {
		ToolbarImpl root = root();
		IPanel<?> rootPanel;
		IPanel<?> childPanel;
		if (ADJUST_HEIGHTS) {
			IVerticalPanel childPanel0 = root.panel.add().panel().vertical();
			rootPanel = childPanel0;
			blocker = childPanel0.add().panel().horizontal();
			blocker.add().label().text("&#160;");
			childPanel = childPanel0.add().panel().horizontal()
					.spacing(SPACING / 2).add().panel().horizontal().align()
					.center();
			blockers.put(childPanel, blocker);
			mainPanels.put(childPanel, childPanel0);
			content.add(childPanel);
		} else {
			IHorizontalPanel childPanel0 = root.panel.add().panel()
					.horizontal().height(height).align().center();
			if (!Env.is(Env.FIREFOX))
				childPanel0.margin().top(-4);
			rootPanel = childPanel0;
			childPanel0.spacing().left(root.hasContent ? 0 : spacing)
					.top(spacing).right(spacing);
			if (!Env.is(Env.CHROME))
				childPanel0.spacing().bottom(spacing);
			childPanel = childPanel0.add().panel().horizontal().align()
					.center();
			content.add(childPanel0);
		}
		final IPanel<?> p = rootPanel;
		final IContainer c = childPanel.add();
		return new IToolbarElement() {

			@Override
			public IElement<?> panel() {
				return p;
			}

			@Override
			public IContainer container() {
				return c;
			}
		};
	}

	@Override
	public ILabel addLabel() {
		ToolbarImpl root = root();
		IHorizontalPanel childPanel0 = root.panel.add().panel().horizontal()
				.height(height).align().center();
		childPanel0.spacing().left(root.hasContent ? 0 : spacing).top(spacing)
				.right(spacing);
		content.add(childPanel0);
		return childPanel0.add().label();
	}

	@Override
	public IToolbar addGroup() {
		ToolbarImpl toolbar = new ToolbarImpl(this);
		content.add(toolbar);
		return toolbar;
	}

	@Override
	public void clear() {
		for (Object o : content) {
			if (o instanceof ToolbarImpl) {
				ToolbarImpl toolbar = (ToolbarImpl) o;
				toolbar.clear();
			} else {
				IHorizontalPanel childPanel = (IHorizontalPanel) o;
				if (ADJUST_HEIGHTS)
					mainPanels.get(childPanel).remove();
				else
					childPanel.remove();
			}
		}
		blockers.clear();
		mainPanels.clear();
		content.clear();
	}

	@Override
	public IToolbar visible(boolean visible) {
		for (Object o : content) {
			if (o instanceof ToolbarImpl) {
				ToolbarImpl toolbar = (ToolbarImpl) o;
				toolbar.visible(visible);
			} else {
				IHorizontalPanel childPanel = (IHorizontalPanel) o;
				if (ADJUST_HEIGHTS)
					mainPanels.get(childPanel).visible(visible);
				else
					childPanel.visible(visible);
			}
		}
		return this;
	}

	@Override
	public IToolbar adjustHeights() {
		if (ADJUST_HEIGHTS)
			for (Object o : content) {
				if (o instanceof ToolbarImpl) {
					ToolbarImpl toolbar = (ToolbarImpl) o;
					toolbar.adjustHeights();
				} else {
					IVerticalPanel childPanel = (IVerticalPanel) mainPanels
							.get(o);
					if (childPanel.height() < 24) {
						IHorizontalPanel blocker = (IHorizontalPanel) blockers
								.get(o);
						int inc = (24 - childPanel.height()) / 2;
						blocker.height(inc);
					}
				}
			}
		return this;
	}

	@Override
	public IAlignment<IToolbar> align() {
		return new IAlignment<IToolbar>() {

			@Override
			public ToolbarImpl begin() {
				panel.align().begin();
				return ToolbarImpl.this;
			}

			@Override
			public ToolbarImpl center() {
				panel.align().center();
				return ToolbarImpl.this;
			}

			@Override
			public ToolbarImpl end() {
				panel.align().end();
				return ToolbarImpl.this;
			}

		};
	}

	@Override
	public IColor color() {
		return panel.color();
	}

	@Override
	public IBorder border() {
		return panel.border();
	}

	@Override
	public IToolbar spacing(int i) {
		spacing = i;
		return this;
	}

}
