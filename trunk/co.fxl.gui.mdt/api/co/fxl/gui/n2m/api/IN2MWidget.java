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
package co.fxl.gui.n2m.api;

import java.util.List;

public interface IN2MWidget<T> {

	public interface IItemImageProvider<T> {

		String itemImage(T token);
	}

	public interface IN2MRelationListener<T> {

		void onChange(List<T> selection);
	}

	IN2MWidget<T> titles(String left, String right);

	IN2MWidget<T> domain(List<T> tokens);

	IN2MWidget<T> selection(List<T> tokens);

	IN2MWidget<T> itemImage(String image);

	IN2MWidget<T> itemImageProvider(IItemImageProvider<T> imageProvider);

	List<T> selection();

	IN2MWidget<T> listener(IN2MRelationListener<T> listener);

	int offsetY();

	IN2MWidget<T> height(int maxFromDisplay);

	IN2MWidget<T> editable(boolean editable);
}
