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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.navigation.api;

import co.fxl.gui.api.IVerticalPanel;

public interface ITab<T extends ITab<T>> extends IActivateable {

	T decorator(ITabDecorator listener);

	T name(String text);

	boolean isActive();

	T visible(boolean visible);

	T toggleLoading(boolean toggleLoading);

	T enabled(boolean enabled);

	boolean isEnabled();

	void showTitleAsEmpty(boolean empty);

	IVerticalPanel newContentPanel();

	void showNewContentPanel();

	T clickable(boolean b);

	void notifyFailureLoad();

}
