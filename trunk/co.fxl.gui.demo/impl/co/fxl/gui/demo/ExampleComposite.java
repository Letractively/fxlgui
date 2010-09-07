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
package co.fxl.gui.demo;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IBordered.IBorder;

class ExampleComposite {

	private IVerticalPanel panel;
	private ITextArea output;
	private ILabel outputHeader;
	private boolean isComposite;
	private boolean active = false;

	ExampleComposite(IVerticalPanel parent) {
		this(parent, false);
	}

	ExampleComposite(IVerticalPanel parent, boolean isComposite) {
		this(parent, isComposite, false);
	}

	ExampleComposite(IVerticalPanel parent, boolean isComposite, boolean stretch) {
		this(parent, isComposite, stretch, true);
	}

	ExampleComposite(IVerticalPanel parent, boolean isComposite,
			boolean stretch, boolean showBorder) {
		parent.color().white();
		panel = parent.add().panel().vertical().spacing(10);
		if (showBorder) {
			IBorder border = parent.border();
			border.color().lightgray();
			border.style().top();
		}
		panel.stretch(stretch);
		this.isComposite = isComposite;
	}

	IContainer title(String name) {
		panel.add().label().text(name + " Example").font().weight().bold()
				.pixel(14);
		panel.add().label().text(
				"The " + (isComposite ? "composite " : "") + "GUI element ");
		return panel.add();
	}

	void codeFragment(String code) {
		String text = "can be added to a panel using the code fragment";
		codeFragment(code, text);
	}

	void codeFragment(String code, String text) {
		panel.add().label().text(text);
		panel.add().label().text(code).font().family().courier();
		active = true;
	}

	void append(String text) {
		StringBuilder b = new StringBuilder();
		if (!setUpOutput())
			b.append(output.text() + "\n");
		b.append(text);
		output.text(b.toString());
	}

	boolean setUpOutput() {
		if (output != null)
			return false;
		outputHeader = panel.add().label();
		outputHeader.text("Output:").font().weight().bold();
		output = panel.add().textArea();
		return true;
	}

	void output(String text) {
		if (!active)
			return;
		setUpOutput();
		output.text(text);
	}

	void clearOutput() {
		if (output != null) {
			outputHeader.remove();
			outputHeader = null;
			output.remove();
			output = null;
		}
	}
}
