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

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.CallbackTemplate;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.ResizeListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.table.api.IColumn.IColumnUpdateListener;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

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
		decorate(panel, null, node);
	}

	private void decorate(final IVerticalPanel panel,
			final IFilterConstraints constraints, final Object node) {
		panel.clear();
		// IBorder border = panel.border();
		// border.color().gray();
		// border.style().top();
		ICallback<IDeletableList<Object>> callback = new CallbackTemplate<IDeletableList<Object>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(final IDeletableList<Object> result) {
				table = (IScrollTableWidget<Object>) panel.add().widget(
						IScrollTableWidget.class);
				final ISelection<Object> selection0 = table.selection();
				ISingleSelection<Object> selection = selection0.single();
				for (final PropertyImpl property : relation.properties) {
					IScrollTableColumn<Object> c = table.addColumn();
					c.name(property.name).type(property.type).sortable();
					if (property.filterable)
						c.filterable();
					if (property.type.clazz.equals(Boolean.class)) {
						c.updateListener(new IColumnUpdateListener<Object, Boolean>() {

							@Override
							public void onUpdate(Object o, Boolean value) {
								property.adapter.valueOf(o, value);
							}
						});
					}
				}
				table.allowColumnSelection(relation.allowColumnSelection);
				table.addFilterListener(new IFilterListener() {

					@Override
					public void onApply(IFilterConstraints constraints) {
						relation.adapter.valueOf(node, constraints,
								new CallbackTemplate<IDeletableList<Object>>() {

									@Override
									public void onSuccess(
											IDeletableList<Object> r) {
										table.rows(toRows(r));
									}
								});
					}
				});
				table.constraints(constraints);
				addButtons(panel, node, result, selection0, selection);
				table.rows(toRows(result));
				ResizeListener.setup(panel.display(), RelationDecorator.this);
				onResize(-1, panel.display().height());
			}

			private void addButtons(final IVerticalPanel panel,
					final Object node, final IDeletableList<Object> result,
					final ISelection<Object> selection0,
					final ISingleSelection<Object> selection) {
				addAddButton(node);
				addRemoveButton(panel, result, selection0);
				addShowButton();
			}

		};
		relation.adapter.valueOf(node, constraints, callback);
	}

	IRows<Object> toRows(final IDeletableList<Object> result) {
		final List<Object> lresult = result.asList();
		return new IRows<Object>() {

			@Override
			public Object identifier(int i) {
				return lresult.get(i);
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
				return lresult.size();
			}
		};
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = table.offsetY();
		// TODO ... un-hard-code
		if (offsetY == 0)
			offsetY = 139;
		int maxFromDisplay = height - offsetY - 124;
		if (maxFromDisplay > 0)
			table.height(maxFromDisplay);
	}

	@Override
	public void decorate(IVerticalPanel panel, ITree<Object> tree) {
		decorate(panel, tree.object());
	}

	private void addAddButton(final Object node) {
		IRowListener<Boolean> addListener = new IRowListener<Boolean>() {
			@Override
			public void onClick(Object identifier, int rowIndex,
					ICallback<Boolean> callback) {
				relation.addRemoveListener.onAdd(node, identifier,
						new CallbackTemplate<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								table.refresh();
							}
						});
			}
		};
		if (relation.addRemoveListener != null) {
			if (relation.addRemoveListener.isDetailedAdd()) {
				table.commandButtons().listenOnAdd(
						new IScrollTableWidget.IDecorator() {
							@Override
							public IClickable<?> decorate(IContainer c) {
								return relation.addRemoveListener.decorateAdd(
										node, c);
							}
						}, addListener);
			} else {
				table.commandButtons().listenOnAdd(addListener);
			}
		}
	}

	private void addRemoveButton(final IVerticalPanel panel,
			final IDeletableList<Object> result,
			final ISelection<Object> selection0) {
		if (relation.addRemoveListener != null) {
			table.commandButtons().listenOnRemove(new IRowListener<Boolean>() {

				@Override
				public void onClick(Object identifier, int rowIndex,
						final ICallback<Boolean> callback) {
					String msg = "Remove Entity?";
					panel.display().showDialog().question().question(msg)
							.title("Warning")
							.addQuestionListener(new IQuestionDialogListener() {

								@Override
								public void onYes() {
									List<Object> r = selection0.result();
									result.delete(
											r.get(0),
											new CallbackTemplate<IDeletableList<Object>>() {

												@Override
												public void onSuccess(
														IDeletableList<Object> result) {
													table.rows(toRows(result));
													callback.onSuccess(true);
												}
											});
								}

								@Override
								public void onNo() {
									callback.onSuccess(false);
								}

								@Override
								public void onCancel() {
									throw new MethodNotImplementedException();
								}
							});
				}
			});
		}
	}

	private void addShowButton() {
		if (relation.showListener != null)
			table.commandButtons().listenOnShow(new IRowListener<Boolean>() {
				@Override
				public void onClick(Object identifier, int rowIndex,
						ICallback<Boolean> callback) {
					relation.showListener.onShow(identifier);
				}
			});
	}
}