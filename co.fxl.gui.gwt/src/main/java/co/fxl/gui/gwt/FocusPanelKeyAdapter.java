package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IKeyRecipient;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FocusPanel;

public class FocusPanelKeyAdapter implements IKeyRecipient<Object> {

	private List<GWTKeyRecipientKeyTemplate> listeners = new LinkedList<GWTKeyRecipientKeyTemplate>();

	public FocusPanelKeyAdapter(FocusPanel focusPanel) {
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
		listeners.add(l);
		return l;
	}

}
