///**
// * This file is part of FXL GUI API.
// *  
// * FXL GUI API is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *  
// * FXL GUI API is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *  
// * You should have received a copy of the GNU General Public License
// * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
// *
// * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
// */
//package co.fxl.gui.swing;
//
//import java.awt.event.MouseEvent;
//
//import co.fxl.gui.impl.ElementListener;
//
//privileged aspect SwingGridPanelAutomation {
//
//	before(SwingGridPanel e, int column, int row) :
//	execution(void SwingGridPanel.fireClickListeners(MouseEvent, int, int))
//	&& this(e)
//	&& args(.., column, row)
//	&& if(ElementListener.active) {
//		ElementListener.instance().notifyClick(e, column, row);
//	}
//
//}
