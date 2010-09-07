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
package co.fxl.gui.swing;

import javax.swing.JOptionPane;

import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILayout;

class SwingDialog implements IDialog {

	public class Type implements IType {

		@Override
		public IDialog error() {
			messageType = JOptionPane.ERROR_MESSAGE;
			return SwingDialog.this;
		}

		@Override
		public IDialog info() {
			messageType = JOptionPane.INFORMATION_MESSAGE;
			return SwingDialog.this;
		}

		@Override
		public IDialog warn() {
			messageType = JOptionPane.WARNING_MESSAGE;
			return SwingDialog.this;
		}
	}

	private SwingDisplay panel;
	private String title;
	private int messageType = JOptionPane.INFORMATION_MESSAGE;

	SwingDialog(SwingDisplay panel) {
		this.panel = panel;
	}

	@Override
	public IDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IDialog message(String message) {
		JOptionPane.showMessageDialog(panel.frame, message, title, messageType);
		return this;
	}

	@Override
	public ILayout panel() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IType type() {
		return new Type();
	}
}
