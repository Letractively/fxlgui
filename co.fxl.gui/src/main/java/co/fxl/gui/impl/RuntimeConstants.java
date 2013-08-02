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
package co.fxl.gui.impl;

public interface RuntimeConstants {

	public static final boolean SAFARI = Env.is(Env.SAFARI);
	public static final boolean CHROME = Env.is(Env.CHROME);
	public static final boolean CHROME_OR_SAFARI = CHROME || SAFARI;
	public static final boolean NOT_CHROME_OR_SAFARI = !CHROME_OR_SAFARI;
	public static final boolean OPERA = Env.is(Env.OPERA);
	public static final boolean IE = Env.is(Env.IE);
	public static final boolean SWING = Env.is(Env.SWING);
	public static final boolean NOT_SWING = !SWING;
	public static final boolean FIREFOX = Env.is(Env.FIREFOX);
	public static final boolean NOT_FIREFOX = !FIREFOX;
	public static final boolean IE_OR_FIREFOX = IE || FIREFOX;
	public static final boolean IE_LEQ_10 = Env.runtime().geq(10);
	public static final boolean FIREFOX_LEQ_12 = Env.runtime().leq(Env.FIREFOX,
			12);
	public static final boolean IE_LEQ_9 = Env.runtime().leq(Env.IE, 9);
	public static final boolean IE_LEQ_8 = Env.runtime().leq(Env.IE, 8);

}