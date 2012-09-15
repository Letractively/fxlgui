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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class FullscreenPopUp {

	public static final int FIXED_WIDTH = 800;
	private static final int SPACING = 20;
	private String title;
	private final int spacingTop = SPACING;
	private int spacingLeft = SPACING;
	private IVerticalPanel content;
	private IDisplay d;
	private IPopUp popUp;
	private IScrollPane scrollPane;
	private WidgetTitle panel;

	public FullscreenPopUp(String title) {
		this.title = title;
		if (FIXED_WIDTH != -1)
			spacingLeft = (Display.instance().width() - FIXED_WIDTH) / 2;
		d = Display.instance();
		popUp = d.showPopUp().modal(true).offset(spacingLeft, spacingTop)
				.autoHide(true);
		popUp.border().remove().style().shadow().color().black();
		panel = new WidgetTitle(popUp.container()).spacing(0).sideWidget(true)
				.commandsOnTop().spacing(0);
		panel.addTitle(title);
		scrollPane = panel.content().scrollPane();
		content = scrollPane.viewPort().panel().vertical().width(FIXED_WIDTH)
				.spacing(10).add().panel().vertical().width(FIXED_WIDTH - 20);
	}

	public CommandLink addHyperlink(String imageResource, String text) {
		return panel.addHyperlink(imageResource, text);
	}

	public IVerticalPanel content() {
		resize(d, popUp, scrollPane);
		final IResizeConfiguration cfg = d.addResizeListener(
				new IResizeListener() {
					@Override
					public void onResize(int width, int height) {
						resize(d, popUp, scrollPane);
					}
				}).linkLifecycle(popUp);
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value)
					cfg.remove();
			}
		});
		panel.addHyperlink("back.png", "Close").addClickListener(
				new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
					}
				});
		popUp.visible(true);
		return content;
	}

	public void resize(IDisplay d, final IPopUp popUp, IScrollPane scrollPane) {
		popUp.size(d.width() - spacingLeft * 2, d.height() - spacingTop * 2);
		scrollPane.size(d.width() - spacingLeft * 2, d.height() - spacingTop
				* 2 - 33);
	}

}
