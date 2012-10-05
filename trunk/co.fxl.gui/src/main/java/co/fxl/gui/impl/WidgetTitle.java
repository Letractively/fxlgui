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
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ContextMenu.Group;

public class WidgetTitle implements IClickListener {

	// TODO Usability: Buttons (Copy / Paste / New …). eventuell so viele,
	// dass der linke Screenbereich zu groß wird und der rechte nach außen
	// scrollt. Langfristig könnte man das evtl. so wie bei den MDTs lösen, d.h.
	// wenn es
	// zu viele sind, dann ein More anzeigen (evtl HorizontalScalingPanel
	// verwenden / einfacher: wenn Display.width()<X oder
	// ButtonPanel.widt()<cell.width() :
	// nur Icons anzeigen)

	private static final int MAX_LENGTH_SUBTITLE = 30;
	public static final int LARGE_FONT = 18;
	private static final String FOLDABLE = "Click to minimize/maximize";
	private static final boolean IGNORE_SUBTITLES = true;
	private static final boolean USE_CONFIGURE_IMAGE = true;
	public IGridPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel commandPanel;
	private boolean hasCommands = false;
	private boolean hasHeaderPanel = false;
	private IGridPanel headerPanel;
	private IContainer contentContainer;
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
	private boolean center;
	private static Map<String, Boolean> foldStatus = new HashMap<String, Boolean>();
	private IVerticalPanel subTitlePanel;
	private IHorizontalPanel configurePanel;
	private IFocusPanel baseFocusPanel;
	private IFocusPanel headerFocusPanel;

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
		baseFocusPanel = layout.focus();
		panel = baseFocusPanel.add().panel().grid();
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
		headerFocusPanel = panel.cell(0, 0).panel().focus();
		headerPanel = headerFocusPanel.add().panel().grid();
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
			IContainer cell = headerPanel.cell(2, 0).align().end().valign()
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
		foldStatus.put(title, !isOpen());
		update();
	}

	public boolean isOpen() {
		return foldStatus.get(title) == null || foldStatus.get(title);
	}

	public void update() {
		if (bPanel != null)
			bPanel.visible(isOpen());
		if (more != null)
			more.visible(!isOpen());
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
			more.addClickListener(this);
			more.tooltip(FOLDABLE);
		}
		this.title = title;
		if (addToContextMenu) {
			ContextMenu instance = ContextMenu.instance();
			Group group = instance.group(title);
			group.clear();
		}
		update();
		return headerLabel;
	}

	public void addSubTitles(String subTitle1, String subTitle2) {
		if (IGNORE_SUBTITLES || subTitle1 == null)
			return;
		initHeader();
		if (subTitlePanel == null)
			subTitlePanel = titlePanel.addSpace(8).add().panel().vertical()
					.align().begin();
		else
			subTitlePanel.clear();
		if (!title.equals(subTitle1))
			subTitlePanel.add().label().text(cutOff(subTitle1)).font().weight()
					.bold().pixel(10).color().white();
		if (subTitle2 != null && !subTitle2.equals(subTitle1)
				&& !title.equals(subTitle1))
			subTitlePanel.add().label().text(cutOff(subTitle2)).font().weight()
					.bold().pixel(10).color().lightgray();
	}

	private String cutOff(String t) {
		if (t.length() >= MAX_LENGTH_SUBTITLE)
			return t.substring(0, MAX_LENGTH_SUBTITLE - 3) + "...";
		return t;
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
		IFocusPanel iPanel0 = cp.add().panel().focus();
		IHorizontalPanel iPanel = iPanel0.add().panel().horizontal();
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
			return createCommandLink(iPanel0, iPanel, image, label);
		} else {
			ILabel label = addHyperlinkLabel(text, iPanel);
			return createCommandLink(iPanel0, iPanel, image, label);
		}
	}

	public IClickable<?> addConfigureIcon(String text) {
		if (configurePanel != null) {
			addLabel("|", false);
		}
		return addLabel(text, true);
	}

	public void addConfigureSuffix(String text) {
		addLabel(text, false);
	}

	IClickable<?> addLabel(String text, boolean underline) {
		if (USE_CONFIGURE_IMAGE) {
			IImage e = configurePanel().add().image()
					.resource("configure_small.png");
			// addFadeInOutEffect(e);
			return e;
		} else {
			final ILabel l = configurePanel().add().label().text(text);
			l.font().pixel(10).color().white();
			if (underline)
				l.addMouseOverListener(new IMouseOverListener() {

					@Override
					public void onMouseOver() {
						l.font().underline(true);
					}

					@Override
					public void onMouseOut() {
						l.font().underline(false);
					}
				});
			// addFadeInOutEffect(l);
			return l;
		}
	}

	// private void addFadeInOutEffect(final IElement<?> e) {
	// e.opacity(0);
	// IFocusPanel h = headerFocusPanel;
	// addFadeEffect(e, h);
	// }

	IHorizontalPanel configurePanel() {
		if (configurePanel == null) {
			IContainer cell = headerPanel.cell(1, 0).align().begin().valign()
					.center();
			headerPanel.column(1).expand();
			configurePanel = cell.panel().horizontal().valign().center()
					.align().end().add().panel().horizontal().valign().center()
					.align().end().spacing(2).align().center();
			configurePanel.margin().right(2);
			// addFadeEffect(configurePanel, headerFocusPanel);
		}
		return configurePanel;
	}

	private CommandLink createCommandLink(IFocusPanel fp,
			IHorizontalPanel iPanel0, IImage image, ILabel label) {
		labels.add(label);
		CommandLink cl = new CommandLink(this, fp, iPanel0, image, label);
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
	public void styleWindowHeaderButton(IPanel<?> iPanel) {
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

	public WidgetTitle center() {
		center = true;
		return this;
	}

	public IContainer content() {
		if (contentContainer != null)
			return contentContainer;
		IVerticalPanel vertical = bPanel.cell(0, 0).panel().vertical();
		if (center)
			vertical.align().center();
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
		update();
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
