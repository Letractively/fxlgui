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
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;

class LazyTreeWidgetImpl extends LazyTreeWidgetTemplate {

	private Map<Integer, IVerticalPanel> panels = new HashMap<Integer, IVerticalPanel>();

	LazyTreeWidgetImpl(IContainer container) {
		super(container);
		heightElement = 22;
	}

	LazyTreeWidgetImpl(IContainer container, boolean showRoot) {
		super(container, showRoot);
		heightElement = 22;
	}

	@Override
	public IContainer elementAt(int index) {
		return panels.get(index).clear().add();
	}

	@Override
	public void decorate(IContainer container, int firstRow, int lastRow,
			boolean notify) {
		panels.clear();
		IVerticalPanel panel = container.panel().vertical().spacing(spacing)
				.add().panel().vertical();
		TreeNode<Object> decorator = new TreeNode<Object>();
		List<LazyTreeAdp> rows = tree.rows(firstRow, lastRow);
		int index = firstRow;
		for (final LazyTreeAdp row : rows) {
			IVerticalPanel p = decorator.setUp(panel, row.tree, row.indent);
			decorator.panel.height(heightElement);
			decorator.decorateCore();
			final int fIndex = index;
			p.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					for (ILazyTreeListener<Object> l : listeners) {
						l.onClick(fIndex);
					}
				}
			});
			LazyTreeWidgetImpl.this.panels.put(index, p);
			index++;
		}
		super.decorate(container, firstRow, lastRow, notify);
	}
}
