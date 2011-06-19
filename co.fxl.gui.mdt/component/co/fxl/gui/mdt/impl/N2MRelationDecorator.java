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

import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ResizeListener;
import co.fxl.gui.n2m.api.IN2MWidget;
import co.fxl.gui.n2m.api.IN2MWidget.IN2MRelationListener;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

final class N2MRelationDecorator implements IDecorator<Object>, IResizeListener {

	private final N2MRelationImpl relation;
	private IN2MWidget<Object> table;

	N2MRelationDecorator(N2MRelationImpl relation) {
		this.relation = relation;
	}

	@Override
	public boolean clear(IVerticalPanel panel) {
		panel.clear();
		return true;
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			ITree<Object> tree) {
		decorate(panel, bottom, tree.object());
	}

	@Override
	public void decorate(final IVerticalPanel panel, IVerticalPanel bottom,
			final Object node) {
		// IBorder border = panel.border();
		// border.color().gray();
		// border.style().top();
		relation.adapter.domain(node, new CallbackTemplate<List<Object>>() {

			@Override
			public void onSuccess(final List<Object> domain) {
				relation.adapter.valueOf(node,
						new CallbackTemplate<List<Object>>() {

							@Override
							public void onSuccess(final List<Object> result) {
								update(panel, node, result);
							}

							@SuppressWarnings("unchecked")
							private void update(final IVerticalPanel panel,
									final Object node, final List<Object> result) {
								panel.clear();
								Object o = table != null ? table.selectedItem()
										: null;
								table = (IN2MWidget<Object>) panel.spacing(8)
										.add().widget(IN2MWidget.class);
								table.selectedItem(o);
								table.itemImage(relation.itemImage);
								table.itemImageProvider(relation.itemImageProvider);
								table.allowReorder(relation.allowReorder);
								table.domain(domain);
								table.selection(result);
								table.editable(relation.adapter.editable(node));
								ResizeListener.setup(panel.display(),
										N2MRelationDecorator.this);
								onResize(-1, panel.display().height());
								table.listener(new IN2MRelationListener<Object>() {
									@Override
									public void onChange(List<Object> selection) {
										relation.adapter
												.valueOf(
														node,
														selection,
														new CallbackTemplate<List<Object>>() {

															@Override
															public void onSuccess(
																	List<Object> result) {
																CallbackTemplate<List<Object>> callback = new CallbackTemplate<List<Object>>() {

																	@Override
																	public void onSuccess(
																			List<Object> result) {
																		update(panel,
																				node,
																				result);
																	}
																};
																relation.adapter
																		.valueOf(
																				node,
																				result,
																				callback);
															}
														});
									}
								});
							}
						});
			}
		});
	}

	@Override
	public void onResize(int width, int height) {
		int offsetY = table.offsetY();
		// TODO ... un-hard-code
		if (offsetY == 0)
			offsetY = 139;
		int maxFromDisplay = height - offsetY - 60;
		if (maxFromDisplay > 0)
			table.height(maxFromDisplay);
	}
}