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

public interface IDialog {

	public interface IQuestionDialog {

		public interface IQuestionDialogListener {

			void onYes();

			void onNo();

			void onCancel();
		}

		IQuestionDialog modal(boolean modal);

		IQuestionDialog title(String title);

		IQuestionDialog allowCancel();

		IQuestionDialog question(String message);

		IQuestionDialog addQuestionListener(IQuestionDialogListener l);

		IQuestionDialog imageResource(String imageResource);
	}

	public interface IType {

		IDialog info();

		IDialog warn();

		IDialog error();
	}

	IDialog modal(boolean modal);

	IDialog title(String title);

	IDialog message(String message);

	IType type();

	IContainer container();

	IQuestionDialog question();

	IDialog visible(boolean visible);
}
