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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILayout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

class GWTDialog implements IDialog {

	public class Type implements IType {

		@Override
		public IDialog error() {
			type = "Error";
			return GWTDialog.this;
		}

		@Override
		public IDialog info() {
			type = "Information";
			return GWTDialog.this;
		}

		@Override
		public IDialog warn() {
			type = "Warning";
			return GWTDialog.this;
		}

	}

	private String title;
	private String type = "Information";

	@Override
	public IDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IDialog message(String message) {
		final DialogBox dialogbox = new DialogBox(false);
		dialogbox.setText(title);
		VerticalPanel panel = new VerticalPanel();
		HTML html = new HTML(type + ": " + message);
		Button button = new Button("Ok");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogbox.hide();
			}
		});
		SimplePanel holder = new SimplePanel();
		holder.add(button);
		panel.add(html);
		panel.add(holder);
		dialogbox.setWidget(panel);
		dialogbox.center();
		return this;
	}

	@Override
	public ILayout panel() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IType type() {
		return new Type();
	}

	@Override
	public IQuestionDialog question() {
		// TODO Auto-generated method stub
		return null;
	}
}
