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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDialog.IQuestionDialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

class GWTQuestionDialog implements IQuestionDialog {

	// TODO translate

	private String message;
	private List<IQuestionDialogListener> questionListeners = new LinkedList<IQuestionDialogListener>();
	private String title;
	private boolean allowCancel = false;

	private void update() {
		if (message == null || questionListeners.isEmpty())
			return;
		final DialogBox dialog = new DialogBox(false, true);
		dialog.setText(title.toUpperCase());
		VerticalPanel v = new VerticalPanel();
		v.setSpacing(10);
		v.add(new Label(message));
		HorizontalPanel panel = new HorizontalPanel();
		v.add(panel);
		panel.setSpacing(10);
		final Button ok = new Button("Yes");
		ok.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
				for (IQuestionDialogListener l : questionListeners)
					l.onYes();
			}
		});
		panel.add(ok);
		Button no = new Button("No");
		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
				for (IQuestionDialogListener l : questionListeners)
					l.onNo();
			}
		});
		panel.add(no);
		if (allowCancel) {
			Button cancel = new Button("Cancel");
			cancel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					dialog.hide();
					for (IQuestionDialogListener l : questionListeners)
						l.onCancel();
				}
			});
			panel.add(cancel);
		}
		dialog.setWidget(v);
		dialog.center();
		Scheduler s = new SchedulerImpl();
		s.scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				ok.setFocus(true);
			}
		});
	}

	@Override
	public IQuestionDialog question(String message) {
		this.message = message;
		update();
		return this;
	}

	@Override
	public IQuestionDialog addQuestionListener(IQuestionDialogListener l) {
		questionListeners.add(l);
		update();
		return this;
	}

	@Override
	public IQuestionDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IQuestionDialog allowCancel() {
		allowCancel = true;
		return this;
	}

	@Override
	public IQuestionDialog modal(boolean modal) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IQuestionDialog imageResource(String imageResource) {
		throw new MethodNotImplementedException();
	}

}
