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

import java.util.Date;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.IResizable;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.rtf.api.IHTMLArea;

public interface IFormWidget extends IResizable<IFormWidget> {

	public interface IFormContainer {

		IFormField<ILabel, String> label();

		IFormField<ITextField, String> textField();

		IFormField<ITextField, Date> dateField();

		IFormField<IPasswordField, String> passwordField();

		IFormField<ITextArea, String> textArea();

		IFormField<ICheckBox, Boolean> checkBox();

		IFormField<IComboBox, String> comboBox();

		IImageField image();
	}

	public interface ISaveListener {

		void save(boolean isAndBack, ICallback<Boolean> cb);

		boolean allowsSaveAndBack();

		boolean allowsCancel();

		void cancel(boolean isAndBack, ICallback<Boolean> cb);
	}

	IFormWidget fixLabelColumn(int width);

	IFormWidget fixValueColumn(int width);

	ILabel addTitle(String title);

	IFormContainer add(String name);

	IFormContainer insert(int index, String name);

	IFormField<ILabel, String> addLabel(String name);

	IFormField<ITextField, String> addTextField(String name, IFieldType type);

	IFormField<ITextField, Date> addDateField(String name, boolean addCalendar);

	IRelationField addRelationField(String name);

	IFormField<ITextField, String> addColorField(String name);

	IFormField<IPasswordField, String> addPasswordField(String name);

	IFormField<ITextArea, String> addTextArea(String name);

	IFormField<ICheckBox, Boolean> addCheckBox(String name);

	IFormField<IComboBox, String> addComboBox(String name);

	IImageField addImage(String name);

	IClickable<?> addOKHyperlink();

	IClickable<?> addCancelHyperlink();

	IClickable<?> addHyperlink(String name);

	IClickable<?> addHyperlink(String imageResource, String title);

	IClickable<?> addHyperlink(String name, IClickListener clickListener);

	IFormWidget saveListener(String title, ISaveListener listener);

	IFormWidget clickable(boolean clickable);

	ILabel labelRequiredAttribute();

	IFormWidget isNew(boolean validate);

	IFormWidget validate(boolean validate);

	IFormWidget notifyUpdate();

	IFormWidget visible(boolean visible);

	IFormWidget alwaysAllowCancel();

	IFormField<IHTMLArea, String> addRichTextArea(String name);

	IFormWidget focus();

	WidgetTitle widgetTitle();

	IFormWidget buttonPanel(IVerticalPanel bottom);

	IFormWidget buttonPanelIndent(int buttonPanelIndent);

	IFormWidget addLargeTitle(String string);

	IFormWidget width(int width);

	void notifyWidthChange();

	void setWidth4Layout(int setWidth4Layout);

}
