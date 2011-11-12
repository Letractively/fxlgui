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
package co.fxl.gui.tree.gwt;

import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.gwt.GWTDisplay;
import co.fxl.gui.gwt.GWTImage;
import co.fxl.gui.gwt.WidgetParent;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.DiscardChangesDialog;
import co.fxl.gui.impl.HTMLText;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.impl.LazyTreeWidgetTemplate;
import co.fxl.gui.tree.impl.TreeNode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTLazyTreeWidget extends LazyTreeWidgetTemplate {

	// TODO BUG: Opera: unnecessary horizontal scrollbar for left splitpane
	// content

	public static boolean IS_CHROME_15_PLUS = GWTDisplay.isChrome()
			&& GWTDisplay.getBrowserVersion() >= 15;
	public static String IMAGE_PATH = "images/";
	public static String HTML = "<table cellspacing=\"0\" cellpadding=\"0\" "
			+ "style=\"width: 100%; cursor: pointer; border: 1px solid rgb(255, 255, 255);\">"
			+ "<tbody>"
			+ "<tr>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<table cellspacing=\"2\" cellpadding=\"0\">"
			+ "<tbody>"
			+ "<tr>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<div "
			+ "style=\"position: relative; overflow: hidden; width: ${INDENT}px; height: 1px;\">&nbsp;</div>"
			+ "</td>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<img class=\"gwt-Image\" "
			+ "src=\""
			+ "${STATE_ICON}\" width=\"8\" height=\"16\"  "
			+ "style=\"cursor: pointer;\">"
			+ "</td>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<img class=\"gwt-Image\" "
			+ "src=\""
			+ "${ICON}\" width=\"16\" height=\"16\" "
			+ "style=\"cursor: pointer;\">"
			+ "</td>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<div "
			+ "style=\"position: relative; overflow: hidden; width: 2px; height: 1px;\"></div>"
			+ "</td>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<div class=\"gwt-HTML gwt-Label-FXL font-family-arial font-size-12px\" "
			+ "style=\"white-space: nowrap; -moz-user-select: none; cursor: pointer;\">"
			+ "<div class=\"unselectable\" unselectable=\"on\">${LABEL}</div>"
			+ "</div>" + "</td>" + "</td>" + "</tr>" + "</tbody>" + "</table>"
			+ "</td>" + "</tr>" + "</tbody>" + "</table>";
	private int firstRow;
	private int lastRow;
	private IContainer container;
	private FocusPanel fp;
	private VerticalPanel p0;

	public GWTLazyTreeWidget(IContainer c) {
		super(c);
	}

	@Override
	public void decorate(IContainer container, final int firstRow, int lastRow) {
		this.container = container;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		fp = new FocusPanel();
		fp.setWidth("100%");
		fp.addStyleName("nooutline");
		fp.addMouseWheelHandler(new MouseWheelHandler() {
			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				if (event.isNorth()) {
					pane.onUp(Math.abs(event.getDeltaY()));
				} else if (event.isSouth()) {
					pane.onDown(Math.abs(event.getDeltaY()));
				}
			}
		});
		p0 = new VerticalPanel();
		p0.setWidth("100%");
		p0.setSpacing(spacing);
		p0.setHeight(height() + "px");
		p0.getElement().getStyle().setProperty("overflowY", "hidden");
		VerticalPanel p = new VerticalPanel();
		p.setWidth("100%");
		p.setSpacing(0);
		if (selectionIndex > firstRow && selectionIndex < lastRow) {
			p.add(getHTML(firstRow, selectionIndex - 1));
			decorator.decorate(getContainer(p), selectionIndex);
			p.add(getHTML(selectionIndex + 1, lastRow));
		} else if (selectionIndex == firstRow) {
			decorator.decorate(getContainer(p), selectionIndex);
			if (firstRow + 1 <= lastRow)
				p.add(getHTML(firstRow + 1, lastRow));
		} else if (selectionIndex == lastRow) {
			p.add(getHTML(firstRow, lastRow - 1));
			decorator.decorate(getContainer(p), selectionIndex);
		} else {
			p.add(getHTML(firstRow, lastRow));
		}
		p0.add(p);
		fp.setWidget(p0);
		fp.setHeight(height() + "px");
		fp.getElement().getStyle().setProperty("overflowY", "hidden");
		container.nativeElement(fp);
		super.decorate(container, firstRow, lastRow);
	}

	@SuppressWarnings("rawtypes")
	private IContainer getContainer(final VerticalPanel p) {
		return new GWTContainer(new WidgetParent() {

			@Override
			public void add(Widget widget) {
				widget.setWidth("100%");
				p.add(widget);
			}

			@Override
			public void remove(Widget widget) {
				p.remove(widget);
			}

			@Override
			public GWTDisplay lookupDisplay() {
				return (GWTDisplay) c.display();
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return lookupDisplay().lookupWidgetProvider(interfaceClass);
			}
		});
	}

	private void update() {
		container.element().remove();
		decorate(container, firstRow, lastRow);
	}

	HTML getHTML(final int firstRow, int lastRow) {
		final List<ITree<Object>> rows = tree.rows(firstRow, lastRow);
		if (rows.isEmpty())
			throw new RuntimeException("illegal range: " + firstRow + " - "
					+ lastRow + ", selection=" + selectionIndex);
		StringBuilder b = new StringBuilder(
				"<table cellspacing=\"0\" cellpadding=\"0\">");
		for (int i = firstRow; i <= lastRow; i++) {
			ITree<Object> row = rows.get(i - firstRow);
			String hTML = HTML.replace("${INDENT}",
					String.valueOf(1 + tree.indent(row) * 10));
			if (isMarked(i))
				hTML = hTML.replace("1px solid rgb(255, 255, 255)",
						"1px dotted rgb(175, 175, 175)");
			String stateURL = getImageURL(IMAGE_PATH,
					TreeNode.treeIcon(this, row));
			hTML = hTML.replace("${STATE_ICON}", stateURL);
			String iconURL = getImageURL(IMAGE_PATH, TreeNode.entityIcon(row));
			hTML = hTML.replace("${ICON}", iconURL);
			String name = row.name();
			if (name == null) {
				name = "<font weight=\"light\" color=\"gray\">unnamed</font>";
			} else
				name = HTMLText.html(name.replace("<", "&#060;"));
			hTML = hTML.replace("${LABEL}", name);
			b.append("<tr>" + hTML + "</tr>");
		}
		b.append("</table>");
		HTML html = new HTML(b.toString());
		html.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				handleClick(rows, firstRow, event.getY());
			}
		});
		html.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(final DoubleClickEvent event) {
				// TODO FEATURE: Usability: expand node before selection
				handleClick(rows, firstRow, event.getY());
			}
		});
		return html;
	}

	private String getImageURL(String path, String treeIcon) {
		if (IS_CHROME_15_PLUS) {
			ImageResource ir = GWTImage.resolve(treeIcon);
			if (ir != null)
				return ir.getURL();
		}
		return path + treeIcon;
	}

	private void handleClick(final List<ITree<Object>> rows,
			final int firstRow, final int y) {
		DiscardChangesDialog.show(new CallbackTemplate<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				selectionIndex = firstRow + (y / heightElement);
				if (!fp.isAttached() || !fp.isVisible())
					return;
				lazyLoad(rows, firstRow, selectionIndex,
						new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								update();
							}
						});
			}
		});
	}

	@Override
	public int width() {
		return p0.getOffsetWidth();
	}
}
