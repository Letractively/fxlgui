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
import co.fxl.gui.tree.api.ITree;

class LazyTreeWidgetImpl extends LazyTreeWidgetTemplate {

	// TODO Usability: Swing: Mouse-Wheel not active on content panel

	private Map<Integer, IVerticalPanel> panels = new HashMap<Integer, IVerticalPanel>();

	LazyTreeWidgetImpl(IContainer container) {
		super(container);
	}

	LazyTreeWidgetImpl(IContainer container, boolean showRoot) {
		super(container, showRoot);
	}

	@Override
	public void decorate(IContainer container, int firstRow, int lastRow) {
		panels.clear();
		IVerticalPanel panel = container.panel().vertical().spacing(spacing)
				.add().panel().vertical();
		TreeNode<Object> decorator = new TreeNode<Object>();
		List<ITree<Object>> rows = tree.rows(firstRow, lastRow);
		int index = firstRow;
		for (final ITree<Object> row : rows) {
			IVerticalPanel p = decorator.setUp(this, panel, row,
					tree.indent(row));
			decorator.panel.height(heightElement);
			decorator.decorateCore();
			final int fIndex = index;
			p.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					elementAt = fIndex;
					decorate(fIndex);
				}
			});
			LazyTreeWidgetImpl.this.panels.put(index, p);
			index++;
		}
		if (elementAt >= firstRow && elementAt <= lastRow) {
			decorate(elementAt);
		}
	}

	void decorate(int index) {
		if (decorator == null)
			return;
		IVerticalPanel p = panels.get(index);
		assert p != null : index + " not in " + panels.keySet();
		IContainer c = p.clear().add();
		decorator.decorate(c, index);
	}
}
