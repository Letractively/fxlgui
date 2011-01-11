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

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.n2m.api.IN2MWidget;
import co.fxl.gui.n2m.api.IN2MWidget.IN2MRelationListener;
import co.fxl.gui.tree.api.ICallback;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

final class N2MRelationDecorator implements IDecorator<Object> {
	private final N2MRelationImpl relation;

	N2MRelationDecorator(N2MRelationImpl relation) {
		this.relation = relation;
	}

	@Override
	public void clear(IVerticalPanel panel) {
		panel.clear();
	}

	@Override
	public void decorate(IVerticalPanel panel, ITree<Object> tree) {
		decorate(panel, tree.object());
	}

	@Override
	public void decorate(final IVerticalPanel panel,
			final Object node) {
		panel.clear();
		IBorder border = panel.border();
		border.color().gray();
		border.style().top();
		relation.adapter.valueOf(node,
				new ICallback<List<Object>>() {

					@Override
					public void onFail(Throwable throwable) {
						throw new MethodNotImplementedException();
					}

					@Override
					public void onSuccess(final List<Object> result) {
						@SuppressWarnings("unchecked")
						IN2MWidget<Object> table = (IN2MWidget<Object>) panel
								.add().widget(IN2MWidget.class);
						table.domain(relation.domain);
						table.selection(result);
						table.listener(new IN2MRelationListener<Object>() {
							@Override
							public void onChange(
									List<Object> selection) {
								relation.adapter
										.valueOf(
												node,
												selection,
												new ICallback<List<Object>>() {

													@Override
													public void onSuccess(
															List<Object> result) {
													}

													@Override
													public void onFail(
															Throwable throwable) {
														throw new MethodNotImplementedException();
													}
												});
							}
						});
					}
				});
	}
}