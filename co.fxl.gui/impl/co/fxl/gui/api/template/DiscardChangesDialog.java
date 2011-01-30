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

import co.fxl.gui.api.IDialog.IQuestionDialog.IQuestionDialogListener;
import co.fxl.gui.api.IDisplay;

public class DiscardChangesDialog {

	public interface DiscardChangesListener {

		void onDiscardChanges(ICallback<Boolean> cb);

		void onKeepChanges(ICallback<Boolean> cb);
	}

	public static final String DISCARD_CHANGES = "You have made changes that have not been saved! Discard Changes?";
	public static boolean active = false;
	public static IDisplay display;
	public static DiscardChangesListener listener;

	public static void show(final ICallback<Boolean> callback) {
		if (!active)
			callback.onSuccess(true);
		else
			display.showDialog().question().question(DISCARD_CHANGES)
					.title("Warning")
					.addQuestionListener(new IQuestionDialogListener() {

						@Override
						public void onYes() {
							active = false;
							if (listener != null) {
								listener.onDiscardChanges(new CallbackTemplate<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										if (result)
											listener = null;
										callback.onSuccess(true);
									}
								});
							} else
								callback.onSuccess(true);
						}

						@Override
						public void onNo() {
							if (listener != null) {
								listener.onKeepChanges(new CallbackTemplate<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										if (result)
											listener = null;
										callback.onSuccess(false);
									}
								});
							} else
								callback.onSuccess(false);
						}

						@Override
						public void onCancel() {
						}
					});
	}
}
