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

import org.aspectj.lang.Signature;

import co.fxl.gui.api.IElement;

public aspect StyleAspect {

	private static final boolean ACTIVE = false;

	public StyleAspect() {
		StyleImpl.setUp();
	}

	void around(IElement<?> element) : execution(@Style * *.*(IElement+)) 
	&& args(element) && if(ACTIVE) {
		Signature sig = thisJoinPoint.getSignature();
		Class<?> c = sig.getDeclaringType();
		String methodName = sig.getName();
		boolean proceedStyle = StyleImpl.apply(c, methodName, element);
		if (proceedStyle)
			proceed(element);
	}
}
