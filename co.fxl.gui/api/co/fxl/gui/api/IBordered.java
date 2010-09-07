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

public interface IBordered {

	public interface IBorder extends IColored {

		public interface IBorderStyle {

			IBorder dotted();

			IBorder solid();

			IBorder etched();
			
			IBorder top();
			
			IBorder bottom();
		}
 
		IBorderStyle style();

		IBorder width(int width);

		IBorder title(String title);
		
		void remove();
	}

	IBorder border();
}
