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

public interface IColored {

	public interface IGradient {

		IColor vertical();

		IGradient fallback(int r, int g, int b);
	}

	public interface IColor {

		IColor black();

		IColor gray();

		IColor lightgray();

		IColor white();

		IColor red();

		IColor green();

		IColor blue();

		IColor yellow();

		IColor rgb(int r, int g, int b);

		IColor mix();

		IGradient gradient();

		IColor remove();

		IColor gray(int rgb);
	}

	IColor color();
}