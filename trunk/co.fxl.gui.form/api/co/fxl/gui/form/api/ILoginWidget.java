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
package co.fxl.gui.form.api;

import co.fxl.gui.api.ILinearPanel;

public interface ILoginWidget {

	public interface IStatusPanelDecorator {

		void decorateBegin(ILinearPanel<?> panel, boolean loggedIn);

		void decorateEnd(ILinearPanel<?> panel, boolean loggedIn);
	}

	public interface Callback {

		void onAuthorization(Authorization auth);
	}

	public enum Authorization {

		OK, FAILED;
	}

	public interface IAuthorizationListener {

		void authorize(String iD, String password, Callback callback);

		void logout();
	}

	ILoginWidget listener(IAuthorizationListener listener);

	ILoginWidget login(String user, String pwd);

	ILoginWidget statusPanelDecorator(IStatusPanelDecorator decorator);

	ILoginWidget visible(boolean visible);
}
