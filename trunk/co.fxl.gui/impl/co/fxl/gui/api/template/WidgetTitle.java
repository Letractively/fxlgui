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
package co.fxl.gui.api.template;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;

public class WidgetTitle implements IClickListener {

	private static final int SMALL_FONT = 12;

	public class CommandLink implements IClickable<IClickable<?>> {

		private IHorizontalPanel iPanel;
		private ILabel label;
		private IImage image;

		public CommandLink(IHorizontalPanel iPanel, IImage image,
				ILabel headerLabel) {
			this.iPanel = iPanel;
			this.image = image;
			this.label = headerLabel;
		}

		public CommandLink label(String l) {
			label.text(l);
			return this;
		}

		@Override
		public IClickable<?> clickable(boolean clickable) {
			label.clickable(clickable);
			if (image != null)
				image.clickable(clickable);
			if (clickable) {
				label.font().color().black();
			} else {
				label.font().color().gray();
			}
			return this;
		}

		@Override
		public boolean clickable() {
			return label.clickable();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<IClickable<?>> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			label.addClickListener(clickListener);
			if (image != null)
				image.addClickListener(clickListener);
			iPanel.addClickListener(clickListener);
			return null;
		}
	}

	public static final int LARGE_FONT = 18;
	private static final String FOLDABLE = "Click to minimize/maximize";
	public IGridPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel commandPanel;
	private boolean hasCommands = false;
	private boolean hasHeaderPanel = false;
	private IGridPanel headerPanel;
	private IContainer contentContainer;
	private boolean open = true;
	private boolean foldable = true;
	public ILabel headerLabel;
	private List<ILabel> labels = new LinkedList<ILabel>();
	private List<IImage> images = new LinkedList<IImage>();
	private int space = 10;
	private Map<ILabel, Boolean> clickableState = new HashMap<ILabel, Boolean>();
	private boolean holdOnClicks = false;
	private IContainer bottomContainer;
	private IHorizontalPanel commandPanelTop;
	private boolean commandsOnTop = false;

	public WidgetTitle(ILayout layout) {
		this(layout, false);
	}

	public WidgetTitle(ILayout layout, boolean addBorder) {
		panel = layout.grid();
		panel.color().white();
		headerPanel = panel.cell(0, 0).panel().grid();
		headerPanel.color().rgb(136, 136, 136).gradient().vertical()
				.rgb(113, 113, 113);
		headerPanel.visible(false);
		IBorder border = headerPanel.border();
		border.color().rgb(172, 197, 213);
		border.style().bottom();
		if (addBorder)
			panel.border().color().rgb(172, 197, 213);
	}

	public WidgetTitle holdOnClick() {
		holdOnClicks = true;
		return this;
	}

	public WidgetTitle reset() {
		for (ILabel l : clickableState.keySet()) {
			l.clickable(clickableState.get(l));
		}
		return this;
	}

	public WidgetTitle space(int space) {
		this.space = space;
		return this;
	}

	public WidgetTitle triangleIcons() {
		return this;
	}

	public WidgetTitle foldable(boolean foldable) {
		this.foldable = foldable;
		return this;
	}

	static IColor decorateGradient(IVerticalPanel titlePanel) {
		IColor color = titlePanel.color().rgb(136, 136, 136).gradient()
				.vertical().rgb(113, 113, 113);
		return color;
	}

	public WidgetTitle commandsOnTop() {
		commandsOnTop = true;
		return this;
	}

	private void initHeader() {
		if (hasHeaderPanel)
			return;
		headerPanel.visible(true);
		IHorizontalPanel horizontal = headerPanel.cell(0, 0).align().begin()
				.valign().center().panel().horizontal().align().begin();// .addSpace(3);
		titlePanel = horizontal.add().panel().horizontal().align().begin();
		titlePanel.spacing().left(10).top(6).bottom(6).right(6);
		hasHeaderPanel = true;
	}

	IHorizontalPanel commandPanel() {
		setUpCommandPanel();
		if (commandsOnTop)
			return commandPanelTop;
		else
			return commandPanel;
	}

	void setUpCommandPanel() {
		if (commandsOnTop) {
			if (commandPanelTop != null)
				return;
			IContainer cell = headerPanel.cell(1, 0).align().end().valign()
					.center();
			commandPanelTop = cell.panel().horizontal().align().end().add()
					.panel().horizontal().align().end().spacing(4);
		} else {
			if (commandPanel != null)
				return;
			IDockPanel vertical = bottom().panel().dock();
			vertical.center().panel().vertical();
			IContainer cell = vertical.right();
			commandPanel = cell.panel().horizontal().spacing(6);
			decorateGradient(vertical);
		}
	}

	public static IBorder decorateGradient(IPanel<?> vertical) {
		vertical.color().rgb(249, 249, 249).gradient().vertical()
				.rgb(216, 216, 216);
		IBorder border2 = vertical.border();
		IColor c = border2.color();
		decorateBorder(c);
		border2.style().top();
		return border2;
	}

	public static void decorateBorder(IColor c) {
		c.rgb(172, 197, 213);
	}

	@Override
	public void onClick() {
		open = !open;
		contentContainer.element().visible(open);
		for (ILabel l : labels)
			l.visible(open);
		for (IImage l : images)
			l.visible(open);
	}

	public ILabel addTitle(String title) {
		initHeader();
		headerLabel = titlePanel.add().label().text(title);
		if (foldable) {
			headerLabel.addClickListener(this);
			headerLabel.tooltip(FOLDABLE);
		}
		headerLabel.font().weight().bold().pixel(SMALL_FONT);
		headerLabel.font().color().white();
		return headerLabel;
	}

	public CommandLink addHyperlink(String text) {
		return addHyperlink(null, text);
	}

	public CommandLink addHyperlink(String imageResource, String text) {
		initHeader();
		if (hasCommands && imageResource == null) {
			ILabel label = commandPanel().add().label().text("|");
			label.font().color().lightgray();
			labels.add(label);
		}
		hasCommands = true;
		IHorizontalPanel iPanel0 = commandPanel().add().panel().horizontal();
		IHorizontalPanel iPanel = iPanel0;
		if (commandsOnTop) {
			iPanel.color().rgb(248, 248, 248).gradient().vertical()
					.rgb(216, 216, 216);
			IBorder b = iPanel.border();
			b.color().rgb(172, 197, 213);
			b.style().rounded();
			iPanel.spacing(4);
			iPanel = iPanel.add().panel().horizontal();
		}
		IImage image = null;
		if (imageResource != null) {
			image = iPanel.add().image().resource(imageResource);
			images.add(image);
		}
		final ILabel label = iPanel.addSpace(4).add().label().text(text);
		label.font().pixel(12);
		labels.add(label);
		if (holdOnClicks) {
			IClickListener clickListener = new IClickListener() {

				@Override
				public void onClick() {
					LinkedList<ILabel> ls = new LinkedList<ILabel>(
							clickableState.keySet());
					for (ILabel hyperlink : ls) {
						clickableState.put(hyperlink, label.clickable());
						hyperlink.clickable(false);
					}
				}
			};
			label.addClickListener(clickListener);
			iPanel.addClickListener(clickListener);
			image.addClickListener(clickListener);
			clickableState.put(label, true);
		}
		CommandLink cl = new CommandLink(iPanel0, image, label);
		cl.clickable(true);
		return cl;
	}

	public IImage addImage(String image) {
		initHeader();
		if (hasCommands) {
			commandPanel().add().label().text("|").font().color().gray();
		}
		hasCommands = true;
		IImage label = commandPanel().add().image().resource(image);
		return label;
	}

	public IContainer content() {
		if (contentContainer != null)
			return contentContainer;
		return contentContainer = space == 0 ? panel.cell(0, 1).panel()
				.vertical().add() : panel.cell(0, 1).panel().vertical()
				.addSpace(space).add();
	}

	public IContainer bottom() {
		content();
		if (bottomContainer != null)
			return bottomContainer;
		return bottomContainer = space == 0 ? panel.cell(0, 2) : panel
				.cell(0, 2).panel().vertical().addSpace(space).add();
	}

	public WidgetTitle clearHyperlinks() {
		commandPanel().clear();
		hasCommands = false;
		return this;
	}

	public WidgetTitle visible(boolean b) {
		panel.visible(b);
		return this;
	}
}
