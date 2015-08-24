/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
package co.fxl.gui.demo;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.api.ITableWidget;
import co.fxl.gui.table.filter.api.IFilterTableWidget;

class WidgetsDemo extends DemoTemplate implements Decorator {

	@SuppressWarnings("unchecked")
	@Override
	public void decorate(ExampleDecorator decorator, final IVerticalPanel vpanel) {
		vpanel.stretch(true);
		final ExampleComposite example = new ExampleComposite(decorator,
				vpanel, true, false, false);
		IContainer container = example.title("Table Widget");
		ITableWidget<String> table = (ITableWidget<String>) container
				.widget(IFilterTableWidget.class);
		table.selection().single()
				.addSelectionListener(new ISelectionListener<String>() {
					@Override
					public void onSelection(int index, String selection) {
						example.output("Selection: " + selection);
					}
				});
		table.addColumn().name("Column 1");
		table.addColumn().name("Column 2");
		table.addColumn().name("Column 3");
		table.addRow().identifier("Row 1")
				.add("Cell 1.1", "Cell 2.1", "Cell 3.1");
		table.addRow().identifier("Row 2")
				.add("Cell 1.2", "Cell 2.2", "Cell 3.2");
		table.addRow().identifier("Row 3")
				.add("Cell 1.3", "Cell 2.3", "Cell 3.3");
		table.visible(true);
		StringBuffer b = new StringBuffer();
		b.append("ITableWidget table = (ITableWidget) panel.add().widget(ITableWidget.class);");
		b.append("\ntable.selection().single().addSelectionListener(new ISelectionListener<String>() {");
		b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;@Override");
		b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;public void onSelection(int index, String selection) {");
		b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;output.text(\"Selection: \" + selection);");
		b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;}");
		b.append("\n&nbsp;&nbsp;});");
		b.append("\ntable.addColumn().name(\"Column 1\");");
		b.append("\ntable.addColumn().name(\"Column 2\");");
		b.append("\ntable.addColumn().name(\"Column 3\");");
		b.append("\ntable.addRow().identifier(\"Row 1\").add(\"Cell 1.1\", \"Cell 2.1\", \"Cell 3.3\");");
		b.append("\ntable.addRow().identifier(\"Row 2\").add(\"Cell 1.2\", \"Cell 2.2\", \"Cell 3.3\");");
		b.append("\ntable.addRow().identifier(\"Row 3\").add(\"Cell 1.3\", \"Cell 2.3\", \"Cell 3.3\");");
		b.append("\ntable.visible(true);");
		example.codeFragment(b.toString());
	}
}