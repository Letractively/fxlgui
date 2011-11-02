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

import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.tree.api.ITree;

class LazyTreeWidgetImpl extends LazyTreeWidgetTemplate {

	private IVerticalPanel vertical;

	// TODO USABILITY: Swing: Mouse-Wheel not active on content panel

	LazyTreeWidgetImpl(IContainer container) {
		super(container);
	}

	LazyTreeWidgetImpl(IContainer container, boolean showRoot) {
		super(container, showRoot);
	}

	@Override
	public void decorate(final IContainer container, final int firstRow,
			final int lastRow) {
		vertical = container.panel().vertical();
		IVerticalPanel panel = vertical.spacing(spacing).add().panel()
				.vertical();
		TreeNode<Object> decorator = new TreeNode<Object>();
		List<ITree<Object>> rows = tree.rows(firstRow, lastRow);
		int index = firstRow;
		for (final ITree<Object> row : rows) {
			if (index == selectionIndex) {
				IVerticalPanel v = panel.add().panel().vertical();
				v.height(heightElement);
				this.decorator.decorate(v.add(), index);
			} else {
				IVerticalPanel p = decorator.setUp(this, panel, row,
						tree.indent(row), isMarked(index));
				decorator.panel.height(heightElement);
				IClickable<?> label = decorator.decorateCore();
				final int fIndex = index;
				IClickListener clickListener = new LazyClickListener() {
					@Override
					public void onAllowedClick() {
						if (!vertical.visible())
							return;

						// TODO selection is lost after new+cancel, parent is
						// selected, the actual tree click is lost

						selectionIndex = fIndex;
						container.element().remove();
						decorate(container, firstRow, lastRow);
					}
				};
				p.addClickListener(clickListener);
				label.addClickListener(clickListener);
			}
			index++;
		}
		super.decorate(container, firstRow, lastRow);
	}

	@Override
	public int width() {
		return vertical.width();
	}
}
