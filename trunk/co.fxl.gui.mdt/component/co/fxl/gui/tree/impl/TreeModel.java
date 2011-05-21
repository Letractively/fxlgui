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
package co.fxl.gui.tree.impl;

import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.tree.api.ITree;

class TreeModel<T> {

	Node<T> node;
	boolean expand = false;
	Map<T, Node<T>> object2node = new HashMap<T, Node<T>>();
	T selection;
	Node<T> last;
	ITree<T> root;
	protected boolean isCopy;
	Node<T> cutted;

}
