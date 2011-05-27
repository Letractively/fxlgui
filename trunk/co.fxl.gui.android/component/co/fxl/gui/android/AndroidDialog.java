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
package co.fxl.gui.android;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;

class AndroidDialog implements IDialog {

	AndroidDialog(AndroidDisplay androidDisplay) {
	}

	@Override
	public IDialog modal(boolean modal) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDialog title(String title) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IType message(String message) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDialogButton addButton() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IContainer container() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDialog visible(boolean visible) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDialog confirm() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDialog size(int width, int height) {
		throw new MethodNotImplementedException();
	}
}
