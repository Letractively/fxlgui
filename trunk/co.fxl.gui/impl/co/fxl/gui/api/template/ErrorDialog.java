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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;

public class ErrorDialog {

	private IDisplay display;

	public ErrorDialog(IDisplay display) {
		this.display = display;
	}

	public void showDialog(String pTitle, final String pMessage,
			final String pStacktrace) {
		IDialog dialog = display.showDialog();
		dialog.title(pTitle).message(pMessage).error();
		if (pStacktrace != null) {
			dialog.addButton().text("Details").imageResource(Icons.DETAIL)
					.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							IDialog detailDialog = display.showDialog().size(
									420, 160);
							detailDialog.title("Error Details");
							detailDialog.addButton().close()
									.addClickListener(new IClickListener() {
										@Override
										public void onClick() {
										}
									});
							IContainer container = detailDialog.container();
							IVerticalPanel detailPanel = container.panel()
									.vertical();
							IVerticalPanel panel = detailPanel.spacing(10)
									.add().panel().vertical();
							ILabel label = panel.add().label()
									.text("Stacktrace:");
							Styles.instance().dialog().error().header()
									.stacktrace(label);
							ITextArea textArea = panel.add().textArea()
									.size(400, 100)
									.text(pStacktrace);
							Styles.instance().input().field().border(textArea);
							detailDialog.visible(true);
						}
					});
		}
		dialog.addButton().text("Close").imageResource(Icons.CANCEL)
				.addClickListener(new IClickListener() {
					@Override
					public void onClick() {
					}
				});
		dialog.visible(true);
	}
}
