package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IKeyRecipient;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FocusPanel;

public class FocusPanelKeyAdapter implements IKeyRecipient<Object> {

	private List<GWTKeyRecipientKeyTemplate> listeners = new LinkedList<GWTKeyRecipientKeyTemplate>();
	private FocusPanel focusPanel;

	public FocusPanelKeyAdapter(FocusPanel focusPanel) {
		this.focusPanel = focusPanel;
		focusPanel.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				for (GWTKeyRecipientKeyTemplate l : listeners)
					l.onKeyDown(event);
			}
		});
		focusPanel.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				for (GWTKeyRecipientKeyTemplate l : listeners)
					l.onKeyUp(event);
			}
		});
	}

	@Override
	public co.fxl.gui.api.IKeyRecipient.IKey<Object> addKeyListener(
			IClickListener listener) {
		GWTKeyRecipientKeyTemplate l = new GWTKeyRecipientKeyTemplate(listener);
		l.element = focusPanel;
		listeners.add(l);
		return l;
	}

	public void setFocus(boolean b) {
		focusPanel.setFocus(b);
	}

}
