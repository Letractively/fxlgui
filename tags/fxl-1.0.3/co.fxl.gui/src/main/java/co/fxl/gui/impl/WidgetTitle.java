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

import java.util.LinkedList;
import java.util.List;

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
import co.fxl.gui.impl.ContextMenu.Group;

//import co.fxl.style.impl.Style;

public class WidgetTitle implements IClickListener {

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
	private int space0 = 10;
	private IContainer bottomContainer;
	private IHorizontalPanel commandPanelTop;
	private boolean commandsOnTop = false;
	private boolean addToContextMenu = false;
	private boolean sideWidget = false;
	private String title;
	private IGridPanel bPanel;
	private boolean hyperlinkVisible = true;
	private IDockPanel footer;
	private IImage more;
	private boolean addBorder;
	private boolean plainContent = false;

	public WidgetTitle() {
	}

	public WidgetTitle(IContainer c) {
		this(c.panel());
	}

	public WidgetTitle(ILayout layout) {
		this(layout, false);
	}

	public WidgetTitle(ILayout layout, boolean addBorder) {
		this(layout, addBorder, false);
	}

	public WidgetTitle(ILayout layout, boolean addBorder, boolean plainContent) {
		panel = layout.grid();
		panel.color().white();
		this.addBorder = addBorder;
		this.plainContent = plainContent;
		setUp();
	}

	private int spaceBottom() {
		return sideWidget && commandsOnTop ? 6 : 0;
	}

	public WidgetTitle plainContent(boolean plainContent) {
		this.plainContent = plainContent;
		if (plainContent && headerPanel != null) {
			headerPanel.visible(false);
			headerPanel.border().remove();
			panel.border().remove();
		}
		if (plainContent)
			spacing(0);
		return this;
	}

	private void setUp() {
		headerPanel = panel.cell(0, 0).panel().grid();
		headerPanel.color().rgb(136, 136, 136).gradient().vertical()
				.rgb(113, 113, 113);
		if (plainContent)
			headerPanel.visible(false);
		bPanel = panel.cell(0, 1).panel().grid();
		if (!plainContent) {
			IBorder border = headerPanel.border();
			border.color().rgb(172, 197, 213);
			border.style().bottom();
		}
		headerPanel.visible(false);
		if (addBorder && !plainContent)
			panel.border().color().rgb(172, 197, 213);
	}

	// public void styleHeader(IPanel<?> headerPanel) {
	// if (!plainContent) {
	// headerPanel.color().rgb(136, 136, 136).gradient().vertical()
	// .rgb(113, 113, 113);
	// IBorder border = headerPanel.border();
	// border.color().rgb(172, 197, 213);
	// border.style().bottom();
	// }
	// }

	public WidgetTitle spacing(int space) {
		this.space0 = space;
		return this;
	}

	int space() {
		return plainContent ? 0 : space0;
	}

	public WidgetTitle triangleIcons() {
		return this;
	}

	public WidgetTitle foldable(boolean foldable) {
		this.foldable = foldable;
		return this;
	}

	public WidgetTitle commandsOnTop() {
		commandsOnTop = true;
		return this;
	}

	private void initHeader() {
		if (hasHeaderPanel)
			return;
		headerPanel.visible(!plainContent);
		IHorizontalPanel horizontal = headerPanel.cell(0, 0).panel()
				.horizontal();// .addSpace(3);
		titlePanel = horizontal.add().panel().horizontal();
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
			footer = bottom().panel().dock();
			footer.visible(!plainContent);
			footer.center().panel().vertical();
			IContainer cell = footer.right();
			commandPanel = cell.panel().horizontal().spacing(6);
			styleFooter(footer);
		}
	}

	// @Style(window = Window.SIDE, outline = Outline.FOOTER)
	public void styleFooter(IPanel<?> vertical) {
		vertical.color().rgb(249, 249, 249).gradient().vertical()
				.rgb(216, 216, 216);
		IBorder border2 = vertical.border();
		IColor c = border2.color();
		decorateBorder(c);
		border2.style().top();
	}

	public static void decorateBorder(IColor c) {
		c.rgb(172, 197, 213);
	}

	@Override
	public void onClick() {
		open = !open;
		bPanel.visible(open);
		// for (ILabel l : labels)
		// l.visible(open);
		// for (IImage l : images)
		// l.visible(open);
		more.visible(!open);
		// headerLabel.text(open ? headerLabel.text().substring(2) : "+ "
		// + headerLabel.text());
	}

	public ILabel addTitle(String title) {
		initHeader();
		String text = sideWidget ? title.toUpperCase() : title;
		ILabel label = titlePanel.add().label().text(text);
		String r = "more.png";
		addMoreIcon(r);
		styleHeaderTitleSide(label);
		headerLabel = label;
		if (foldable) {
			headerLabel.addClickListener(this);
			headerLabel.tooltip(FOLDABLE);
		}
		this.title = title;
		if (addToContextMenu) {
			ContextMenu instance = ContextMenu.instance();
			Group group = instance.group(title);
			group.clear();
		}
		return headerLabel;
	}

	private void addMoreIcon(String r) {
		more = titlePanel.add().image();
		more.resource(r);
		more.visible(false);
	}

	public WidgetTitle addTitleSpace() {
		titlePanel.addSpace(10);
		return this;
	}

	// @Style(window = Window.SIDE, outline = Outline.HEADER)
	public void styleHeaderTitleSide(ILabel label) {
		label.font().weight().bold().pixel(12).color().white();
	}

	public CommandLink addHyperlink(String text) {
		return addHyperlink(null, text);
	}

	private ILabel addHyperlinkLabel(String text, IHorizontalPanel iPanel) {
		return addHyperlinkLabel2Panel(text, iPanel);
	}

	public CommandLink addHyperlink(String imageResource, String text) {
		initHeader();
		IHorizontalPanel cp = commandPanel();
		if (hasCommands && imageResource == null) {
			ILabel label = addSeparator(cp);
			labels.add(label);
		}
		hasCommands = true;
		IHorizontalPanel iPanel0 = cp.add().panel().horizontal();
		IHorizontalPanel iPanel = iPanel0;
		if (commandsOnTop) {
			styleWindowHeaderButton(iPanel);
			iPanel.spacing(4);
			iPanel = iPanel.add().panel().horizontal();
		}
		IImage image = null;
		if (imageResource != null) {
			image = iPanel.add().image().resource(imageResource).size(16, 16);
			images.add(image);
		}
		if (text == null) {
			ILabel label = iPanel.add().label().visible(false);
			return createCommandLink(iPanel0, image, label);
		} else {
			ILabel label = addHyperlinkLabel(text, iPanel);
			return createCommandLink(iPanel0, image, label);
		}
	}

	private CommandLink createCommandLink(IHorizontalPanel iPanel0,
			IImage image, ILabel label) {
		labels.add(label);
		CommandLink cl = new CommandLink(this, iPanel0, image, label);
		cl.clickable(hyperlinkVisible);
		if (addToContextMenu)
			cl.addToContextMenu(title);
		return cl;
	}

	public ToggleImageButton addToggleButton(String imageResource, String text) {
		return new ToggleImageButton(addHyperlink(imageResource, text),
				imageResource, text);
	}

	// @Style(window = Window.SIDE, outline = Outline.HEADER, element =
	// Element.BUTTON)
	public void styleWindowHeaderButton(IHorizontalPanel iPanel) {
		iPanel.color().rgb(248, 248, 248).gradient().vertical()
				.rgb(216, 216, 216);
		IBorder b = iPanel.border();
		b.color().rgb(172, 197, 213);
		b.style().rounded();
	}

	public IClickable<?> addHyperlink(String imageResource, String name,
			String toolTipClickable, String toolTipNotClickable) {
		return addHyperlink(imageResource, name).tooltips(toolTipClickable,
				toolTipNotClickable);
	}

	public static ILabel addHyperlinkLabel2Panel(String text,
			IHorizontalPanel iPanel) {
		ILabel l = iPanel.addSpace(4).add().label();
		ILabel label = l.text(text);
		label.font().pixel(12);
		return label;
	}

	public IImage addImage(String image) {
		initHeader();
		IHorizontalPanel cp = commandPanel();
		if (hasCommands) {
			addSeparator(cp);
		}
		hasCommands = true;
		IImage label = cp.add().image().resource(image);
		return label;
	}

	protected ILabel addSeparator(IHorizontalPanel cp) {
		ILabel label = cp.add().label().text("|");
		styleSeparator(label);
		return label;
	}

	// @Style(element = Element.SEPARATOR)
	public void styleSeparator(ILabel label) {
		label.font().color().gray();
	}

	public IContainer content() {
		if (contentContainer != null)
			return contentContainer;
		IVerticalPanel vertical = bPanel.cell(0, 0).panel().vertical();
		IVerticalPanel v = vertical;
		if (space() == 0 && spaceBottom() == 0)
			return contentContainer = v.add();
		contentContainer = v.addSpace(space()).add().panel().vertical().add();
		if (spaceBottom() != 0)
			vertical.addSpace(spaceBottom());
		return contentContainer;
	}

	public IContainer bottom() {
		content();
		if (bottomContainer != null)
			return bottomContainer;
		int space = space();
		return bottomContainer = space == 0 ? bPanel.cell(0, 1) : bPanel
				.cell(0, 1).panel().vertical().addSpace(space).add();
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

	public WidgetTitle addToContextMenu(boolean b) {
		addToContextMenu = b;
		return this;
	}

	public WidgetTitle sideWidget(boolean sideWidget) {
		this.sideWidget = sideWidget;
		return this;
	}

	public WidgetTitle hyperlinkVisible(boolean hyperlinkVisible) {
		this.hyperlinkVisible = hyperlinkVisible;
		return this;
	}

	public void adjustHeader() {
		// headerPanel.color().rgb(225, 225, 225).gradient().vertical()
		// .rgb(245, 245, 245);
		// headerLabel.font().color().black();
		// footer.color().white().gradient().vertical().white();
		// footer.border().remove();
		if (headerPanel != null && footer != null)
			headerPanel.height(footer.height());
	}

	public IPanel<?> headerPanel() {
		return headerPanel;
	}
}
