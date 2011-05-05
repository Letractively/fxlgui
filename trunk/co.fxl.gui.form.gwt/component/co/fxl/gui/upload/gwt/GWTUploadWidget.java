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
package co.fxl.gui.upload.gwt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.form.api.IFormField;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;
import co.fxl.gui.gwt.GWTContainer;
import co.fxl.gui.gwt.GWTDisplay;
import co.fxl.gui.gwt.WidgetParent;
import co.fxl.gui.upload.api.IUpload;
import co.fxl.gui.upload.api.IUploadListener;
import co.fxl.gui.upload.api.IUploadWidget;
import co.fxl.gui.upload.impl.UploadImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTUploadWidget implements IUploadWidget {

	private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL()
			+ "upload";
	private List<IUploadListener> uploadListeners;
	private TextBox tb;
	private TextArea ta;
	private ITextField url;
	private boolean isFileUpload = false;
	private Map<String, String> keyValues;
	private IContainer c0;

	GWTUploadWidget(final IContainer c0) {
		this(c0, new LinkedList<IUploadListener>(),
				new HashMap<String, String>());
	}

	GWTUploadWidget(final IContainer c0, List<IUploadListener> uploadListeners,
			Map<String, String> keyValues) {
		this.c0 = c0;
		this.uploadListeners = uploadListeners;
		this.keyValues = keyValues;
	}

	@Override
	public IUploadWidget visible(boolean visible) {
		if (!visible)
			throw new MethodNotImplementedException();
		final FormPanel formp = new FormPanel();
		formp.setAction(UPLOAD_ACTION_URL);
		formp.setEncoding(FormPanel.ENCODING_MULTIPART);
		formp.setMethod(FormPanel.METHOD_POST);
		assert keyValues.size() != 0 : "no key values set!";
		WidgetParent parent = new WidgetParent() {

			@Override
			public void add(Widget widget) {
				widget.setWidth("100%");
				formp.setWidget(widget);
			}

			@Override
			public void remove(Widget widget) {
				throw new MethodNotImplementedException();
			}

			@Override
			public GWTDisplay lookupDisplay() {
				GWTDisplay instance = (GWTDisplay) GWTDisplay.instance();
				return instance;
			}

			@Override
			public IWidgetProvider<?> lookupWidgetProvider(
					Class<?> interfaceClass) {
				return lookupDisplay().lookupWidgetProvider(interfaceClass);
			}
		};
		GWTContainer<VerticalPanel> container = new GWTContainer<VerticalPanel>(
				parent);
		IVerticalPanel vertical = container.panel().vertical();
		for (String key : keyValues.keySet()) {
			Hidden hidden = new Hidden();
			hidden.setName(key);
			hidden.setValue(keyValues.get(key));
			vertical.add().nativeElement(hidden);
		}
		final IFormWidget form = (IFormWidget) vertical.add().widget(
				IFormWidget.class);
		form.validate(true);
		form.saveListener("Submit", new ISaveListener() {

			@Override
			public void save(final ICallback<Boolean> cb) {
				final IUpload upload = getUpload();
				if (upload.isFileUpload()) {
					formp.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
						public void onSubmitComplete(SubmitCompleteEvent event) {
							for (IUploadListener l : GWTUploadWidget.this.uploadListeners) {
								if (isOK(event.getResults()))
									l.onUpload(upload);
								else
									l.onUploadFailed(upload, event.getResults());
							}
							cb.onSuccess(true);
							new GWTUploadWidget(c0.clear(),
									GWTUploadWidget.this.uploadListeners,
									GWTUploadWidget.this.keyValues)
									.visible(true);
						}

						private boolean isOK(String results) {
							return results == null || results.trim().equals("");
						}
					});
					formp.submit();
				} else {
					for (IUploadListener l : GWTUploadWidget.this.uploadListeners) {
						l.onUpload(upload);
					}
					cb.onSuccess(true);
					new GWTUploadWidget(c0.clear(),
							GWTUploadWidget.this.uploadListeners,
							GWTUploadWidget.this.keyValues).visible(true);
				}
			}

			@Override
			public void cancel(ICallback<Boolean> cb) {
				throw new MethodNotImplementedException();
			}

			@Override
			public boolean allowsCancel() {
				return false;
			}
		});
		form.addTitle("Add Attachment").font().pixel(WidgetTitle.LARGE_FONT);
		tb = (TextBox) form.addTextField("Name").required().valueElement()
				.nativeElement();
		tb.setName("name");
		final IFormField<ITextField> uRLField = form.addTextField("URL")
				.required();
		url = uRLField.valueElement();
		IContainer c = uRLField.addContainer();
		final FileUpload object = new FileUpload();
		object.setName("uploadFormElement");
		object.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				uRLField.valueElement().text(object.getFilename())
						.editable(false);
				isFileUpload = true;
				form.notifyUpdate();
			}
		});
		c.nativeElement(object);
		ta = (TextArea) form.addTextArea("Description").valueElement()
				.nativeElement();
		ta.setName("description");
		form.visible(true);
		c0.nativeElement(formp);
		return this;
	}

	private IUpload getUpload() {
		UploadImpl gwtUpload = new UploadImpl(url.text(), tb.getText(),
				ta.getText(), isFileUpload);
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
