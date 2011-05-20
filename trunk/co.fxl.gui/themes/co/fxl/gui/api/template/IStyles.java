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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;

public interface IStyles {

	public interface IInput {

		public interface IField {

			IField background(IColored c);

			IField border(IBordered b);

		}

		IField field();
	}

	public interface IWindow {

		public interface ITitle {

			void small(ILabel label);

		}

		public interface IHeader {

			ITitle title();

		}

		public interface IViewWindow {

			public interface IEntry {

				IEntry active(IPanel<?> panel, boolean active);

				IEntry active(ILabel label, boolean active);
			}

			IEntry entry();

		}

		public interface INavigationWindow {

			public interface IChoice {

				void title(ILabel label);

			}

			IChoice choice();

			void number(ILabel label);

		}

		INavigationWindow navigation();

		IViewWindow view();

		IHeader header();

		void button(ILabel label);

	}

	public interface IHyperlink {

		void clickable(ILabel label, boolean clickable);

		void highlight(ILabel label, boolean highlighted);
	}

	public interface IDialog {

		public interface IErrorDialog {

			public interface IHeader {

				void stacktrace(ILabel label);

			}

			IHeader header();

		}

		public interface IButton {

			void clickable(ILabel label, boolean clickable);

		}

		IErrorDialog error();

		IButton button();

	}

	IDialog dialog();

	void separator(ILabel label);

	IHyperlink hyperlink();

	IWindow window();

	IInput input();

}
