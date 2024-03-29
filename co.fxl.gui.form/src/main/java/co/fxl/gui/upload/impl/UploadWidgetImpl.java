/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
package co.fxl.gui.upload.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.impl.SaveListenerTemplate;
import co.fxl.gui.upload.api.IUpload;
import co.fxl.gui.upload.api.IUploadListener;
import co.fxl.gui.upload.api.IUploadWidget;

public class UploadWidgetImpl implements IUploadWidget {

	private List<IUploadListener> uploadListeners;
	private ITextField url;
	private boolean isFileUpload = false;
	private Map<String, String> keyValues;
	private IContainer c0;
	private IFormField<ITextField, String> uRLField;
	private ITextField textField;
	private ITextArea textArea;

	UploadWidgetImpl(final IContainer c0) {
		this(c0, new LinkedList<IUploadListener>(),
				new HashMap<String, String>());
	}

	UploadWidgetImpl(final IContainer c0,
			List<IUploadListener> uploadListeners, Map<String, String> keyValues) {
		this.c0 = c0;
		this.uploadListeners = uploadListeners;
		this.keyValues = keyValues;
	}

	@Override
	public IUploadWidget visible(boolean visible) {
		if (!visible)
			throw new UnsupportedOperationException();
		IVerticalPanel vertical = c0.panel().vertical();
		final IFormWidget form = (IFormWidget) vertical.add().widget(
				IFormWidget.class);
		form.validate(true);
		form.saveListener("Submit", new SaveListenerTemplate() {

			@Override
			public void save(boolean b, final ICallback<Boolean> cb) {
				final IUpload upload = getUpload();
				if (upload.isFileUpload()) {
					throw new UnsupportedOperationException();
				} else {
					for (IUploadListener l : UploadWidgetImpl.this.uploadListeners) {
						l.onUpload(upload);
					}
					cb.onSuccess(true);
					new UploadWidgetImpl(c0.clear(),
							UploadWidgetImpl.this.uploadListeners,
							UploadWidgetImpl.this.keyValues).visible(true);
				}
			}
		});
		form.addLargeTitle("Add Attachment");
		textField = form.addTextField(false, "Name", null).required()
				.valueElement();
		uRLField = form.addTextField(false, "URL", null).required();
		url = uRLField.valueElement();
		textArea = form.addTextArea("Description").valueElement();
		form.visible(true);
		return this;
	}

	private IUpload getUpload() {
		UploadImpl gwtUpload = new UploadImpl(url.text(), textField.text(),
				textArea.text(), isFileUpload);
		isFileUpload = false;
		return gwtUpload;
	}

	@Override
	public IUploadWidget addUploadListener(IUploadListener l) {
		uploadListeners.add(l);
		return this;
	}

	@Override
	public IUploadWidget addKeyValuePair(String key, String value) {
		keyValues.put(key, value);
		return this;
	}
}
