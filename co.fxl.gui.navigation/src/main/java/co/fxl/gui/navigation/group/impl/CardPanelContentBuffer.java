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
package co.fxl.gui.navigation.group.impl;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.impl.ColorMemento;
import co.fxl.gui.impl.IContentPage;

public class CardPanelContentBuffer implements IContentBuffer {

	private final class ContentPage implements IContentPage {

		@Override
		public void back() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void preview() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IContainer next() {
			next = this;
			throw new UnsupportedOperationException();
		}

		@Override
		public void flip() {
			last = this;
			throw new UnsupportedOperationException();
		}
	}

	private ICardPanel cards;
	private IColor color = new ColorMemento();
	private ContentPage next;
	private ContentPage last;

	public CardPanelContentBuffer(IContainer add) {
		cards = add.panel().card();
	}

	@Override
	public IContentPage newPage() {
		return new ContentPage();
	}

	@Override
	public IColor color() {
		return color;
	}

	@Override
	public void preview() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void back() {
		throw new UnsupportedOperationException();
	}

}
