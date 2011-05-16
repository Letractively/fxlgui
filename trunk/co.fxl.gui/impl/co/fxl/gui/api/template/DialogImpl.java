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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;

public class DialogImpl implements IDialog {

	public class Type implements IType {

		@Override
		public IDialog error() {
			type = "Error";
			return DialogImpl.this;
		}

		@Override
		public IDialog info() {
			type = "Information";
			return DialogImpl.this;
		}

		@Override
		public IDialog warn() {
			type = "Warning";
			return DialogImpl.this;
		}
	}

	private IDisplay display;
	private boolean modal = true;
	private String title = "Dialog";
	private String message;
	private String type = "Information";
	private IPopUp popUp;

	public DialogImpl(IDisplay display) {
		this.display = display;
	}

	@Override
	public IDialog modal(boolean modal) {
		this.modal = modal;
		return this;
	}

	@Override
	public IDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IDialog message(String message) {
		this.message = message;
		return this;
	}

	@Override
	public IType type() {
		return new Type();
	}

	@Override
	public IContainer container() {
		popUp = display.showPopUp();
		return popUp.container();
	}

	@Override
	public IQuestionDialog question() {
		return new QuestionDialogImpl(display, true);
	}

	@Override
	public IDialog visible(boolean visible) {
		if (popUp == null) {
			QuestionDialogImpl qd = new QuestionDialogImpl(display, false);
			qd.modal(modal);
			qd.title(title != null ? title : type);
			qd.imageResource(image(type));
			qd.question(message);
			qd.addQuestionListener(new IQuestionDialogListener() {

				@Override
				public void onYes() {
				}

				@Override
				public void onNo() {
					throw new MethodNotImplementedException();
				}

				@Override
				public void onCancel() {
					throw new MethodNotImplementedException();
				}
			});
			popUp = qd.popUp;
		}
		popUp.visible(visible);
		return this;
	}

	private String image(String type2) {
		if (type.equals("Information"))
			return "info.png";
		else if (type.equals("Warning"))
			return "skip.png";
		else if (type.equals("Error"))
			return "cancel.png";
		else
			throw new MethodNotImplementedException();
	}
}
