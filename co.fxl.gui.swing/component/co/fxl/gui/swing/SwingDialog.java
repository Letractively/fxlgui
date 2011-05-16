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
package co.fxl.gui.swing;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.template.QuestionDialogImpl;

class SwingDialog implements IDialog {

	public class QuestionDialog implements IQuestionDialog {

		private String message;
		private List<IQuestionDialogListener> questionListeners = new LinkedList<IQuestionDialogListener>();
		private String title;
		private boolean allowCancel = false;

		public void update() {
			if (message != null && !questionListeners.isEmpty()) {
				int result = JOptionPane.showConfirmDialog(panel.frame,
						message, title,
						allowCancel ? JOptionPane.YES_NO_CANCEL_OPTION
								: JOptionPane.YES_NO_OPTION);
				for (IQuestionDialogListener l : questionListeners) {
					if (result == JOptionPane.OK_OPTION)
						l.onYes();
					else if (result == JOptionPane.CANCEL_OPTION)
						l.onCancel();
					else
						l.onNo();
				}
			}
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
	}

	public class Type implements IType {

		@Override
		public IDialog error() {
			messageType = JOptionPane.ERROR_MESSAGE;
			return SwingDialog.this;
		}

		@Override
		public IDialog info() {
			messageType = JOptionPane.INFORMATION_MESSAGE;
			return SwingDialog.this;
		}

		@Override
		public IDialog warn() {
			messageType = JOptionPane.WARNING_MESSAGE;
			return SwingDialog.this;
		}
	}

	private SwingDisplay panel;
	private String title;
	private int messageType = JOptionPane.INFORMATION_MESSAGE;

	SwingDialog(SwingDisplay panel) {
		this.panel = panel;
	}

	@Override
	public IDialog title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public IDialog message(String message) {
		JOptionPane.showMessageDialog(panel.frame, message, title, messageType);
		return this;
	}

	private JDialog dialog;
	private boolean modal = false;

	@Override
	public IContainer container() {
		return new SwingContainer<JComponent>(new ComponentParent() {

			@Override
			public void add(JComponent component) {
				dialog = new JDialog();
				dialog.setModal(true);
				dialog.setTitle(title);
				dialog.getContentPane().add(component);
				dialog.requestFocus();
			}

			@Override
			public void remove(JComponent component) {
				dialog.setVisible(false);
			}

			@Override
			public JComponent getComponent() {
				throw new MethodNotImplementedException();
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return panel.lookupWidgetProvider(interfaceClass);
			}

			@Override
			public SwingDisplay lookupSwingDisplay() {
				return panel;
			}
		});
	}

	@Override
	public IDialog visible(boolean visible) {
		assert dialog != null;
		if (visible) {
			dialog.pack();
			dialog.setLocationRelativeTo(panel.frame);
			dialog.setVisible(true);
			dialog.setModal(modal);
		} else {
			dialog.setVisible(false);
		}
		return this;
	}

	@Override
	public IType type() {
		return new Type();
	}

	@Override
	public IQuestionDialog question() {
		return new QuestionDialogImpl(panel);
	}

	@Override
	public IDialog modal(boolean modal) {
		this.modal = modal;
		return this;
	}

}
