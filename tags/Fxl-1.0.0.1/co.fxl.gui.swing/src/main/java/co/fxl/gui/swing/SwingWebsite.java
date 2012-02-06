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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import co.fxl.gui.api.IWebsite;

class SwingWebsite implements IWebsite {

	@Override
	public IWebsite uRI(String uRI) {
		try {
			Desktop.getDesktop().browse(new URI(uRI));
		} catch (IOException e) {
			throw new MethodNotImplementedException(e);
		} catch (URISyntaxException e) {
			throw new MethodNotImplementedException(e);
		}
		return this;
	}

	@Override
	public IWebsite localURI(String uRI) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IWebsite title(String title) {
		throw new MethodNotImplementedException();
	}
}
