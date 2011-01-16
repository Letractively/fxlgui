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
package co.fxl.gui.mdt.impl;

import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.table.api.IColumn;
import co.fxl.gui.table.api.IColumn.IColumnUpdateListener;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.tree.api.ICallback;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;
import co.fxl.gui.tree.impl.CallbackTemplate;

final class RelationDecorator implements IDecorator<Object>, IResizeListener {

	private final RelationImpl relation;
	private IScrollTableWidget<Object> table;

	RelationDecorator(RelationImpl relation) {
		this.relation = relation;
	}

	@Override
	public void clear(IVerticalPanel panel) {
		panel.clear();
	}

	@Override
	public void decorate(final IVerticalPanel panel, final Object node) {
		panel.clear();
		IBorder border = panel.border();
		border.color().gray();
		border.style().top();
		ICallback<List<Object>> callback = new CallbackTemplate<List<Object>>() {

			private IButton details;
			private IButton add;
			private IButton remove;

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(final List<Object> result) {
				table = (IScrollTableWidget<Object>) panel.add().widget(
						IScrollTableWidget.class);
				final ISelection<Object> selection0 = table.selection();
				ISingleSelection<Object> selection = selection0.single();
				IHorizontalPanel buttonPanel = panel.add().panel().horizontal()
						.add().panel().horizontal();
				if (relation.addRemoveListener != null) {
					if (relation.addRemoveListener.isDetailedAdd()) {
						add = relation.addRemoveListener.decorateAdd(node,
								buttonPanel.add());
					} else
						add = buttonPanel.add().button().text("Add");
					remove = buttonPanel.addSpace(10).add().button()
							.text("Remove").clickable(false);
				}
				if (relation.showListener != null)
					details = buttonPanel.addSpace(10).add().button()
							.text("Show").clickable(false);
				ISelectionListener<Object> listener = new ISelectionListener<Object>() {
					@Override
					public void onSelection(Object selection) {
						if (remove != null) {
							remove.clickable(selection != null);
						}
						if (details != null)
							details.clickable(selection != null);
					}
				};
				if (add != null)
					add.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							List<Object> r = selection0.result();
							Object c = null;
							if (r.size() > 0)
								c = r.get(0);
							relation.addRemoveListener.onAdd(node, c);
						}
					});
				if (remove != null)
					remove.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							List<Object> r = selection0.result();
							relation.addRemoveListener.onRemove(node, r.get(0));
						}
					});
				if (details != null)
					details.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							List<Object> r = selection0.result();
							relation.showListener.onShow(r);
						}
					});
				selection.addSelectionListener(listener);
				for (final PropertyImpl property : relation.properties) {
					IColumn<Object> c = table.addColumn().name(property.name)
							.type(property.type.clazz).sortable();
					if (property.type.clazz.equals(Boolean.class)) {
						c.updateListener(new IColumnUpdateListener<Object, Boolean>() {

							@Override
							public void onUpdate(Object o, Boolean value) {
								property.adapter.valueOf(o, value);
							}
						});
					}
				}
				table.rows(new IRows<Object>() {

					@Override
					public Object identifier(int i) {
						return result.get(i);
					}

					@Override
					public Object[] row(int i) {
						Object e = identifier(i);
						Object[] row = new Object[relation.properties.size()];
						int j = 0;
						for (PropertyImpl p : relation.properties) {
							row[j++] = (Comparable<?>) p.adapter.valueOf(e);
						}
						return row;
					}

					@Override
					public int size() {
						return result.size();
					}
				});
				ResizeListener.setup(panel.display(), RelationDecorator.this);
				onResize(-1, panel.display().height());
			}
		};
		relation.adapter.valueOf(node, callback);
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = table.offsetY();
		// TODO ... un-hard-code
		if (offsetY == 0)
			offsetY = 139;
		int maxFromDisplay = height - offsetY - 110;
		if (maxFromDisplay > 0)
			table.height(maxFromDisplay);
	}

	@Override
	public void decorate(IVerticalPanel panel, ITree<Object> tree) {
		decorate(panel, tree.object());
	}
}