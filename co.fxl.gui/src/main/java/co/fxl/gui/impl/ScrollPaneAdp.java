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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IScrollPane;

public class ScrollPaneAdp extends ElementAdp<IScrollPane> implements
		IScrollPane {

	public ScrollPaneAdp() {
	}

	public ScrollPaneAdp(IScrollPane pane) {
		super(pane);
	}

	@Override
	public IScrollPane horizontal() {
		element.horizontal();
		return this;
	}

	@Override
	public IContainer viewPort() {
		return element.viewPort();
	}

	@Override
	public IScrollPane addScrollListener(IScrollListener listener) {
		element.addScrollListener(listener);
		return this;
	}

	@Override
	public IScrollPane scrollTo(int pos) {
		element.scrollTo(pos);
		return this;
	}

	@Override
	public IScrollPane scrollIntoView(IElement<?> element) {
		super.element.scrollIntoView(element);
		return this;
	}

	@Override
	public int scrollOffset() {
		return element.scrollOffset();
	}

	@Override
	public IScrollPane bidirectional() {
		element.bidirectional();
		return this;
	}

	@Override
	public IScrollBars scrollBars() {
		return new IScrollBars() {

			@Override
			public IScrollPane always() {
				element.scrollBars().always();
				return ScrollPaneAdp.this;
			}

			@Override
			public IScrollPane never() {
				element.scrollBars().never();
				return ScrollPaneAdp.this;
			}

		};
	}

}
