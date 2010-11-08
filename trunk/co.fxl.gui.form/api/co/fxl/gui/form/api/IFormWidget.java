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
package co.fxl.gui.form.api;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IClickable.IClickListener;

public interface IFormWidget {

	public interface ISaveListener {

		void onSave();
	}

	IFormWidget fixLabelColumn(int width);

	IFormWidget fixValueColumn(int width);

	ILabel addTitle(String title);

	IFormField<ILabel> addLabel(String name);

	IFormField<ITextField> addTextField(String name);

	IFormField<IPasswordField> addPasswordField(String name);

	IFormField<ITextArea> addTextArea(String name);

	IFormField<ICheckBox> addCheckBox(String name);

	IFormField<IComboBox> addComboBox(String name);

	IImageField addImage(String name);

	ILabel addOKHyperlink();

	ILabel addCancelHyperlink();

	ILabel addHyperlink(String name);

	ILabel addHyperlink(String name, IClickListener clickListener);

	IFormWidget saveListener(String title, ISaveListener listener);

	ILabel labelRequiredAttribute();

	IFormWidget visible(boolean visible);
}
