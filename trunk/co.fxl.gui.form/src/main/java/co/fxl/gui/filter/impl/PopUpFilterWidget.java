/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IPopUpFilterWidget;
import co.fxl.gui.impl.ButtonAdp;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.PopUp.TransparentPopUp;
import co.fxl.gui.impl.ToolbarImpl;

public class PopUpFilterWidget extends FilterWidgetImpl implements
		IPopUpFilterWidget {

	private IToolbar p;
	private IElement<?> element;
	private ButtonAdp button;

	PopUpFilterWidget(IContainer panel) {
		p = new ToolbarImpl(panel);
		element = setUp(p.add());
		element.remove();
		button = new ImageButton(p.add()).imageResource("filter.png").text(
				"Filter");
		button.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				final TransparentPopUp p = PopUp.showClosablePopUp(true, null,
						true);
				p.panel.padding(10);
				IVerticalPanel labelPanel = p.panel;
				int y = labelPanel.offsetY() + labelPanel.height();
				p.popUp.offset(labelPanel.offsetX() - 4, y);
				IVerticalPanel holder = p.panel.add().panel().vertical();
				holder.add().element(element);
				p.popUp.modal(false).autoHide(true).visible(true);
				if (y + p.panel.height() > Display.instance().height() - 30) {
					p.panel.clear().add().scrollPane()
							.height(Display.instance().height() - 30 - y)
							.viewPort().element(holder);
				}
				PopUp.autoHideOnResize(p);
			}
		});
	}

}
