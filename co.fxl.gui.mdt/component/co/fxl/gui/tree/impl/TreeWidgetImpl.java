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
package co.fxl.gui.tree.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.register.api.IRegister;
import co.fxl.gui.register.api.IRegisterWidget;
import co.fxl.gui.register.api.IRegister.IRegisterListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

class TreeWidgetImpl implements ITreeWidget<Object> {

	private class DetailView {

		private IDecorator<Object> decorator;
		private Node node;
		private boolean onTop = false;
		private IVerticalPanel contentPanel;

		DetailView(String title, IDecorator<Object> decorator) {
			this.decorator = decorator;
			IRegister register = registers.addRegister();
			register.title().text(title);
			register.addListener(new IRegisterListener() {
				@Override
				public void onTop(boolean visible) {
					onTop = visible;
					if (visible) {
						update();
					}
				}
			});
			contentPanel = register.contentPanel().spacing(16);
			if (detailViews.isEmpty())
				register.top();
		}

		// DetailView(IDecorator<Object> decorator) {
		// this.decorator = decorator;
		// contentPanel = detailPanel;
		// onTop = true;
		// }

		void setNode(Node node) {
			if (node == null) {
				decorator.clear(contentPanel);
				return;
			}
			this.node = node;
			if (onTop) {
				update();
			}
		}

		protected void update() {
			if (node == null)
				return;
			decorator.decorate(contentPanel, node.tree.object());
		}
	}

	private IVerticalPanel panel;
	private Node last;
	private IRegisterWidget registers;
	private List<DetailView> detailViews = new LinkedList<DetailView>();
	IVerticalPanel detailPanel;
	private ITree<Object> root;
	private WidgetTitle widgetTitle;
	private boolean expand = false;
	private Object selection;
	Map<Object, Node> object2node = new HashMap<Object, Node>();

	TreeWidgetImpl(ILayout layout) {
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.foldable(false);
		widgetTitle.addHyperlink("New").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				last.tree.createNew();
				root(root);
			}
		});
		widgetTitle.addHyperlink("Delete").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						last.tree.delete();
						last = null;
						selection = null;
						root(root);
					}
				});
		widgetTitle.addHyperlink("Refresh").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						throw new MethodNotImplementedException();
					}
				});
	}

	@Override
	public ITreeWidget<Object> title(String title) {
		widgetTitle.addTitle(title).font().pixel(18);
		return this;
	}

	IVerticalPanel panel() {
		if (panel == null) {
			panel = widgetTitle.content().panel().vertical();
		}
		return panel;
	}

	@Override
	public ITreeWidget<Object> setDetailView(IDecorator<Object> decorator) {
		return addDetailView("Details", decorator);
		// setUpDetailPanel();
		// DetailView detailView = new DetailView(decorator);
		// detailViews.add(detailView);
		// return this;
	}

	@Override
	public ITreeWidget<Object> addDetailView(String title,
			IDecorator<Object> decorator) {
		setUpRegisters();
		DetailView detailView = new DetailView(title, decorator);
		detailViews.add(detailView);
		return this;
	}

	private void setUpRegisters() {
		if (registers != null)
			return;
		setUpDetailPanel();
		registers = (IRegisterWidget) detailPanel.add().widget(
				IRegisterWidget.class);
	}

	private void setUpDetailPanel() {
		if (detailPanel != null)
			return;
		IGridPanel grid = panel().add().panel().grid().spacing(4);
		panel = grid.cell(0, 0).width(300).panel().vertical();
		detailPanel = grid.cell(1, 0).panel().vertical().spacing(10);
		detailPanel.color().rgb(250, 250, 250);
		detailPanel.border().style().dotted();
	}

	@Override
	public ITreeWidget<Object> root(ITree<Object> tree) {
		if (this.root != null) {
			show(null);
			panel().clear();
		}
		this.root = tree;
		Node node = new Node(this, panel(), tree, 0, expand);
		if (selection != null) {
			node = object2node.get(selection);
		}
		show(node);
		return this;
	}

	void show(Node node) {
		if (last != null) {
			if (last == node)
				return;
			last.selected(false);
		}
		last = node;
		if (node != null)
			node.selected(true);
		for (DetailView view : detailViews) {
			view.setNode(node);
		}
	}

	@Override
	public ITreeWidget<Object> expand() {
		this.expand = true;
		return this;
	}

	@Override
	public IClickable<?> addHyperlink(String title) {
		return widgetTitle.addHyperlink(title);
	}

	@Override
	public ITreeWidget<Object> selection(Object selection) {
		this.selection = selection;
		return this;
	}

	@Override
	public Object selection() {
		return last.tree.object();
	}
}
