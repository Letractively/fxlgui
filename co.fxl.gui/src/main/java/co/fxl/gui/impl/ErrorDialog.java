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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;

public class ErrorDialog {

	private static final int DEFAULT_WIDTH = 420;
	private static boolean showing = false;
	private Runnable runnable;

	private ErrorDialog() {
	}

	public static void create(String pTitle, String pMessage,
			String pStacktrace, Runnable pRunnable) {
		if (showing) {
			return;
		}
		ErrorDialog lErrorDialog = new ErrorDialog();
		lErrorDialog.runnable = pRunnable;
		lErrorDialog.show(pTitle, pMessage, pStacktrace);
	}

	public static void create(String pTitle, String pMessage, String pStacktrace) {
		create(pTitle, pMessage, pStacktrace, null);
	}

	public static void createAlways(String pTitle, String pMessage,
			String pStacktrace) {
		boolean remember = showing;
		showing = false;
		create(pTitle, pMessage, pStacktrace, null);
		showing = remember;
	}

	public void show(final String pTitle, final String pMessage,
			final String pStacktrace) {
		showing = true;
		StatusPopUp.instance().close();
		IDialog dialog = PopUp.showDialog().glass(true).width(DEFAULT_WIDTH)
				.title(pTitle).message(pMessage).error();
		if (pStacktrace != null) {
			dialog.addButton().text("Details").imageResource(Icons.DETAIL)
					.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							IDialog detailDialog = Dialog.newInstance();
							detailDialog.width(DEFAULT_WIDTH);
							detailDialog.title("Error Details");
							detailDialog.addButton().close()
									.addClickListener(new IClickListener() {
										@Override
										public void onClick() {
											onClose();
										}
									});
							IContainer container = detailDialog.container();
							IVerticalPanel detailPanel = container.panel()
									.vertical();
							IVerticalPanel panel = detailPanel.spacing(10)
									.add().panel().vertical();
							ILabel label = panel.add().label()
									.text("Stacktrace:");
							styleDialogError(label);
							String trace = pStacktrace.trim().equals("") ? pMessage
									: pStacktrace;
							if (trace.trim().equals("null"))
								trace = pMessage;
							if (trace.trim().equals("null"))
								trace = pTitle;
							ITextArea textArea = panel.add().textArea()
									.size(400, 100).text(trace);
							styleInputBorder(textArea);
							detailDialog.visible(true);
						}

						private void styleInputBorder(ITextArea textArea) {
							textArea.border().color().rgb(211, 211, 211);
						}

						private void styleDialogError(ILabel label) {
							label.font().weight().italic();
						}
					});
		}
		dialog.addButton().close().imageResource(Icons.CANCEL)
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						onClose();
					}
				}).defaultButton();
		dialog.visible(true);
	}

	protected void onClose() {
		showing = false;
		if (runnable != null) {
			runnable.run();
		}
	}
}
