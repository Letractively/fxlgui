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

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.log.impl.Log;

public class FlipPage implements IContentPage, IColored {

	private ICardPanel cardPanel;
	private IVerticalPanel page1;
	private IVerticalPanel page2;
	private IVerticalPanel active;
	private boolean nextCalled;

	public FlipPage(IContainer c) {
		cardPanel = c.panel().card();
		page1 = cardPanel.add().panel().vertical();
		page2 = cardPanel.add().panel().vertical();
		active = null;
		cardPanel.show(page1);
	}

	@Override
	public IContainer next() {
		if (active == null)
			return page1.add();
		nextCalled = true;
		if (active == page2) {
			return page1.clear().add();
		} else {
			return page2.clear().add();
		}
	}

	@Override
	public void flip() {
		if (active == null) {
			active = page1;
			return;
		}
		if (!nextCalled)
			return;
		IVerticalPanel inactive = active;
		if (active == page2) {
			active = page1;
		} else
			active = page2;
		cardPanel.show(active);
		inactive.clear();
		nextCalled = false;
	}

	@Override
	public void preview() {
		if (active == null)
			return;
		if (active == page2) {
			cardPanel.show(page1);
		} else
			cardPanel.show(page2);
	}

	@Override
	public void back() {
		if (active == null)
			return;
		cardPanel.show(active);
	}

	public void width(int width) {
		cardPanel.width(width);
		page1.width(width);
		page2.width(width);
	}

	public void height(int height) {
		cardPanel.height(height);
		page1.height(height);
		page2.height(height);
	}

	@Override
	public IColor color() {
		return new ColorTemplate() {

			@Override
			public IColor remove() {
				page1.color().remove();
				page2.color().remove();
				return this;
			}

			@Override
			protected IColor setRGB(int r, int g, int b) {
				page1.color().rgb(r, g, b);
				page2.color().rgb(r, g, b);
				return this;
			}
		};
	}

}
