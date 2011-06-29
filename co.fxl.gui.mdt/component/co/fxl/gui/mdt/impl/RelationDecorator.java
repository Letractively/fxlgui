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

import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.ResizeListener;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.table.api.IColumn.IColumnUpdateListener;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.INavigationPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

final class RelationDecorator implements IDecorator<Object>, IResizeListener,
		ISelectionListener<Object> {

	// TODO Optimization / Potential Bug: Relation Page seems to get painted
	// twice on each register change

	private final RelationImpl relation;
	private IScrollTableWidget<Object> table;
	private Object node;
	int selectionIndex = -1;
	Object selection;
	private IVerticalPanel panel;
	private DetailView detailView;

	RelationDecorator(DetailView detailView, RelationImpl relation) {
		this.detailView = detailView;
		this.relation = relation;
	}

	void updateSelection(DetailView detailView, RelationImpl relation) {
		selection = detailView.widget.relationRegisterSelection
				.get(relation.viewID);
	}

	@Override
	public boolean clear(IVerticalPanel panel) {
		panel.clear();
		return true;
	}

	@Override
	public void decorate(final IVerticalPanel panel, IVerticalPanel bottom,
			final Object node) {
		if (this.node != null && !node.equals(this.node)) {
			selectionIndex = -1;
			selection = null;
		}
		this.node = node;
		decoratePanel(panel, bottom, null, node);
	}

	@Override
	public void onSelection(int index, Object selection) {
		this.selectionIndex = index;
		this.selection = selection;
		assert relation.viewID != null;
		detailView.widget.relationRegisterSelection.put(relation.viewID,
				selection);
	}

	private void decoratePanel(final IVerticalPanel panel,
			final IVerticalPanel bottom, final IFilterConstraints constraints,
			final Object node) {
		this.panel = panel;
		ICallback<IDeletableList<Object>> callback = new CallbackTemplate<IDeletableList<Object>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(final IDeletableList<Object> result) {
				panel.clear();
				table = (IScrollTableWidget<Object>) panel.add().widget(
						IScrollTableWidget.class);
				if (relation.views != null) {
					table.addViewComboBox(relation.views, relation.view,
							relation.viewListener);
				}
				table.statusPanel(bottom);
				final ISelection<Object> selection0 = table.selection();
				ISingleSelection<Object> selection = selection0.single();
				if (selectionIndex != -1
						|| RelationDecorator.this.selection != null)
					selection0.add(selectionIndex,
							RelationDecorator.this.selection);
				selection.addSelectionListener(RelationDecorator.this);
				for (final PropertyImpl property : relation.properties) {
					IScrollTableColumn<Object> c = table.addColumn();
					c.name(property.name).type(property.type);
					if (relation.hidden.contains(property)) {
						c.visible(false);
					}
					if (relation.sortable)
						c.sortable();
					if (property.filterable)
						c.filterable();
					boolean editable = relation.editable && property.editable;
					if (relation.editableAdapter != null)
						editable &= relation.editableAdapter.isEditable(node);
					if (editable)
						c.editable();
					if (editable && property.type.clazz.equals(Boolean.class)) {
						c.updateListener(new IColumnUpdateListener<Object, Boolean>() {

							@Override
							public boolean onUpdate(Object o, Boolean value) {
								return property.adapter.valueOf(o, value);
							}

							@Override
							public boolean isEditable(Object entity) {
								return property.adapter.editable(entity);
							}
						});
					}
				}
				table.allowColumnSelection(relation.allowColumnSelection);
				table.constraints(constraints);
				addButtons(panel, node, result, selection0, selection);
				table.rows(toRows(result));
				table.addFilterListener(new IFilterListener() {

					@Override
					public void onApply(IFilterConstraints constraints) {
						detailView.relationDecoratorFilterConstraints = constraints;
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
				ResizeListener.setup(panel.display(), RelationDecorator.this);
				panel.display().invokeLater(new Runnable() {

					@Override
					public void run() {
						onResize(-1, panel.display().height());
						table.visible(true);
					}
				});
			}

			private void addButtons(final IVerticalPanel panel,
					final Object node, final IDeletableList<Object> result,
					final ISelection<Object> selection0,
					final ISingleSelection<Object> selection) {
				boolean editable = relation.editable;
				if (relation.editableAdapter != null)
					editable &= relation.editableAdapter.isEditable(node);
				if (editable) {
					addAddButton(node);
					addRemoveButton(panel, result, selection0);
					addUpDownButtons(node, panel, result, selection0);
				}
				if (relation.navigation != null)
					addNavigation(node);
				addShowButton();
				if (editable) {
					addEditButton();
				}
			}
		};
		relation.adapter.valueOf(node, constraints, callback);
	}

	private void addNavigation(final Object node) {
		table.navigationPanel(new INavigationPanelDecorator() {
			@Override
			public void decorate(IContainer container) {
				final IHorizontalPanel panel = container.panel().horizontal()
						.align().end().add().panel().horizontal().align().end()
						.spacing(8);
				NavigationImpl navigation = new NavigationImpl();
				relation.navigation.decorate(node, navigation);
				for (NavigationButtonImpl b : navigation.navigationButtons) {
					ImageButton l = new ImageButton(panel.add());
					l.imageResource(b.imageResource);
					l.text(b.text);
					l.clickable(b.clickable != null);
					if (b.clickable != null)
						l.addClickListener(b.clickable);
				}
			}
		});
	}

	IRows<Object> toRows(final IDeletableList<Object> result) {
		return new IRows<Object>() {

			@Override
			public Object identifier(int i) {
				Object object = result.get(i);
				return object;
			}

			@Override
			public Object[] row(int i) {
				Object e = identifier(i);
				Object[] row = result.tableValues(e);
				if (row != null)
					return row;
				row = new Object[relation.properties.size()];
				int j = 0;
				for (PropertyImpl p : relation.properties) {
					Object valueOf = p.adapter.valueOf(e);
					row[j++] = valueOf;
				}
				return row;
			}

			@Override
			public int size() {
				return result.size();
			}

			@Override
			public boolean deletable(int i) {
				return result.isDeletable(identifier(i));
			}
		};
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = Math.max(panel.offsetY(), 180);
		int maxFromDisplay = height - offsetY - 80;
		maxFromDisplay = Math.max(maxFromDisplay, 60);
		if (detailView.tree.heightRegisterPanel() > 40) {
			maxFromDisplay -= 35;
		}
		table.height(maxFromDisplay);
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			ITree<Object> tree) {
		decorate(panel, bottom, tree.object());
	}

	private void addAddButton(final Object node) {
		IRowListener<IRows<Object>> addListener = new IRowListener<IRows<Object>>() {
			@Override
			public void onClick(Object identifier, int rowIndex,
					final ICallback<IRows<Object>> callback) {
				relation.addRemoveListener.onAdd(node, identifier == null ? -1
						: rowIndex, identifier, new CallbackTemplate<Boolean>(
						callback) {

					@Override
					public void onSuccess(Boolean result) {
						callback.onSuccess(null);
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
			table.commandButtons().listenOnRemove(
					new IRowListener<IRows<Object>>() {

						@Override
						public void onClick(Object identifier, int rowIndex,
								final ICallback<IRows<Object>> callback) {
							String msg = "Remove Entity?";
							IDialog dl = panel.display().showDialog()
									.message(msg).warn().confirm();
							dl.addButton().yes()
									.addClickListener(new IClickListener() {
										@Override
										public void onClick() {
											Map<Integer, Object> r = selection0
													.indexedResult();
											int index = r.keySet().iterator()
													.next();
											result.delete(
													index,
													r.get(index),
													new CallbackTemplate<IDeletableList<Object>>(
															callback) {

														@Override
														public void onSuccess(
																IDeletableList<Object> result) {
															callback.onSuccess(toRows(result));
														}
													});
										}
									});
							dl.addButton().no()
									.addClickListener(new IClickListener() {

										@Override
										public void onClick() {
											callback.onSuccess(null);
										}
									});
							dl.visible(true);
						}
					});
		}
	}

	private void addUpDownButtons(final Object node,
			final IVerticalPanel panel, final IDeletableList<Object> result,
			final ISelection<Object> selection0) {
		if (relation.upDownListener != null) {
			table.commandButtons().listenOnMoveUp(
					new IMoveRowListener<IRows<Object>>() {

						@Override
						public void onClick(int rowIndex, Object identifier,
								boolean maxMove,
								final ICallback<IRows<Object>> callback) {
							relation.upDownListener.onUp(node, rowIndex,
									identifier, maxMove,
									new CallbackTemplate<Boolean>(callback) {

										@Override
										public void onSuccess(Boolean result) {
											callback.onSuccess(null);
										}
									});
						}
					});
			table.commandButtons().listenOnMoveDown(
					new IMoveRowListener<IRows<Object>>() {

						@Override
						public void onClick(int rowIndex, Object identifier,
								boolean maxMove,
								final ICallback<IRows<Object>> callback) {
							relation.upDownListener.onDown(node, rowIndex,
									identifier, maxMove,
									new CallbackTemplate<Boolean>(callback) {

										@Override
										public void onSuccess(Boolean result) {
											callback.onSuccess(null);
										}
									});
						}
					});
		}
	}

	private void addShowButton() {
		if (relation.showListener != null)
			table.commandButtons().listenOnShow(
					new IRowListener<IRows<Object>>() {
						@Override
						public void onClick(Object identifier, int rowIndex,
								ICallback<IRows<Object>> callback) {
							relation.showListener.onShow(identifier);
						}
					});
	}

	private void addEditButton() {
		if (relation.editListener != null)
			table.commandButtons().listenOnEdit(
					new IRowListener<IRows<Object>>() {
						@Override
						public void onClick(Object identifier, int rowIndex,
								final ICallback<IRows<Object>> callback) {
							relation.editListener.onEdit(node, rowIndex,
									identifier,
									new CallbackTemplate<Boolean>() {
										@Override
										public void onSuccess(Boolean result) {
											callback.onSuccess(null);
										}
									});
						}
					});
	}
}