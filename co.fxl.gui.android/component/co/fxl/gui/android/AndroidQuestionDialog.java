///**
// * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
// *  
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
// */
//package co.fxl.gui.android;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import co.fxl.gui.api.IDialog.IQuestionDialog;
//
//class AndroidQuestionDialog implements IQuestionDialog {
//
//	AlertDialog.Builder builder;
//	private String question;
//	private List<IQuestionDialogListener> ls = new LinkedList<IQuestionDialogListener>();
//
//	AndroidQuestionDialog(AndroidDisplay androidDisplay) {
//		builder = new AlertDialog.Builder(androidDisplay.activity);
//	}
//
//	private void update() {
//		if (question == null || ls.isEmpty())
//			return;
//		builder.setMessage(question).setCancelable(false).setPositiveButton(
//				"Yes", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						for (IQuestionDialogListener l : ls) {
//							l.onYes();
//						}
//					}
//				}).setNegativeButton("No",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						for (IQuestionDialogListener l : ls) {
//							l.onNo();
//						}
//					}
//				}).show();
//	}
//
//	@Override
//	public IQuestionDialog question(String question) {
//		this.question = question;
//		update();
//		return this;
//	}
//
//	@Override
//	public IQuestionDialog addQuestionListener(IQuestionDialogListener l) {
//		ls.add(l);
//		update();
//		return this;
//	}
//}
