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
import co.fxl.gui.gwt.WidgetParent;
import co.fxl.gui.tree.impl.LazyTreeAdp;
import co.fxl.gui.tree.impl.LazyTreeWidgetTemplate;
import co.fxl.gui.tree.impl.TreeNode;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTLazyTreeWidget extends LazyTreeWidgetTemplate {

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
			+ "src=\"test_gwt/images/${STATE_ICON}\" "
			+ "style=\"cursor: pointer;\">"
			+ "</td>"
			+ "<td align=\"left\" style=\"vertical-align: middle;\">"
			+ "<img class=\"gwt-Image\" "
			+ "src=\"test_gwt/images/${ICON}\" "
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
	private HTML html;
	private int firstRow;

	public GWTLazyTreeWidget(IContainer c) {
		super(c);
	}

	private void decorate(final int index) {
		IContainer c = new GWTContainer<Widget>(new WidgetParent() {

			@Override
			public void add(Widget widget) {
				child().removeFromParent();
				DOM.insertChild(html.getElement(), widget.getElement(), index());
			}

			Node child() {
				return html.getElement().getChild(index());
			}

			private int index() {
				return index - firstRow;
			}

			@Override
			public void remove(Widget widget) {
				child().removeFromParent();
			}

			@Override
			public GWTDisplay lookupDisplay() {
				return (GWTDisplay) GWTLazyTreeWidget.this.c.display();
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return lookupDisplay().lookupWidgetProvider(interfaceClass);
			}
		});
		decorator.decorate(c, index);
	}

	@Override
	public void decorate(IContainer container, final int firstRow, int lastRow,
			boolean notify) {
		this.firstRow = firstRow;
		List<LazyTreeAdp> rows = tree.rows(firstRow, lastRow);
		VerticalPanel p = new VerticalPanel();
		p.setSpacing(spacing);
		p.setHeight(height + "px");
		StringBuilder b = new StringBuilder(
				"<table cellspacing=\"0\" cellpadding=\"0\">");
		for (int i = firstRow; i <= lastRow; i++) {
			LazyTreeAdp row = rows.get(i - firstRow);
			String hTML = HTML.replace("${INDENT}",
					String.valueOf(row.indent * 10));
			hTML = hTML.replace("${STATE_ICON}", TreeNode.treeIcon(row.tree));
			hTML = hTML.replace("${ICON}", TreeNode.entityIcon(row.tree));
			hTML = hTML.replace("${LABEL}", row.tree.name());
			b.append("<tr>" + hTML + "</tr>");
		}
		b.append("</table>");
		html = new HTML(b.toString());
		html.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		html.setHeight((height - 2 * spacing) + "px");
		html.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				elementAt = firstRow + (event.getY() / heightElement);
				decorate(elementAt);
			}
		});
		p.add(html);
		container.nativeElement(p);
		if (elementAt >= firstRow && elementAt <= lastRow) {
			decorate(elementAt);
		}
		super.decorate(container, firstRow, lastRow, notify);
	}
}
