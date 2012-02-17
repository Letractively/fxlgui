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
package co.fxl.gui.api;

public interface IElement<T> extends ILocated<T> {

	// TODO maybe include methods addStyle(String) / removeStyle(String) for CSS
	// TODO for Swing, etc.: IDisplay.registerStyle(String, IDecorator<?>)

	// T attach(boolean attach);

	T visible(boolean visible);

	boolean visible();

	void remove();

	T tooltip(String tooltip);

	<N> N nativeElement();

	IDisplay display();

	IPadding padding();

	T padding(int padding);

	IMargin margin();

	T margin(int margin);

	T opacity(double opacity);

}
