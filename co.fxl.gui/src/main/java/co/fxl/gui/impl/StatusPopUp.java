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
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;

public class StatusPopUp implements IResizeListener, Runnable {

	static final int YELLOW_B = 190;
	static final int YELLOW_G = 237;
	static final int YELLOW_R = 249;

	public interface Status {

		void hide(boolean lazy);

		void hide(long ms);
	}

	private static class StatusImpl implements Status {

		private Long id = StatusPopUp.CURRENT_ID++;
		private Long showAt = null;
		private Long hideAt = null;
		private String message;
		private boolean lazy;
		private String tooltip;

		private StatusImpl(String message, String tooltip) {
			this.message = message;
			this.tooltip = tooltip != null ? tooltip : message;
		}

		private void show(boolean lazy) {
			this.lazy = lazy;
			if (!lazy) {
				StatusPopUp.instance.run();
			} else {
				showAt = System.currentTimeMillis() + SHOW_LAZY_TOLERANCE;
				StatusPopUp.instance.schedule(SHOW_LAZY_TOLERANCE);
			}
		}

		@Override
		public void hide(boolean lazy) {
			if (!lazy) {
				hide();
			} else {
				if (showAt != null) {
					queue().remove(this);
					return;
				}
				long currentTimeMillis = System.currentTimeMillis();
				long remaining = showAt != null ? LAZY_POPUP_TIME_MINIMUM
						- (currentTimeMillis - showAt)
						: LAZY_POPUP_TIME_MINIMUM;
				hideAt = currentTimeMillis + remaining;
				StatusPopUp.instance.schedule(remaining);
			}
		}

		@Override
		public void hide(long ms) {
			hideAt = System.currentTimeMillis() + ms;
			StatusPopUp.instance.schedule(ms);
		}

		private void hide() {
			boolean active = active();
			queue().remove(this);
			if (active)
				StatusPopUp.instance.run();
		}

		private boolean active() {
			return StatusPopUp.instance.active() == this;
		}

		List<StatusImpl> queue() {
			return StatusPopUp.instance.queue;
		}

		@Override
		public int hashCode() {
			return id.intValue();
		}

		@Override
		public boolean equals(Object o) {
			return ((StatusImpl) o).id.equals(id);
		}

	}

	private static StatusPopUp instance = new StatusPopUp();
	private static final long SHOW_LAZY_TOLERANCE = 1000;
	private static final long LAZY_POPUP_TIME_MINIMUM = 500;
	private static Long CURRENT_ID = 0l;
	private IPopUp popUp;
	private List<StatusImpl> queue = new LinkedList<StatusImpl>();
	private ILabel label;
	private IHorizontalPanel panel;

	private StatusPopUp() {
		Display.instance().addResizeListener(this);
	}

	private StatusImpl active() {
		for (int i = queue.size() - 1; i >= 0; i--) {
			StatusImpl status = queue.get(i);
			if (status.showAt == null && !status.lazy)
				return status;
		}
		for (int i = queue.size() - 1; i >= 0; i--) {
			StatusImpl status = queue.get(i);
			if (status.showAt == null)
				return status;
		}
		return null;
	}

	@Override
	public void run() {
		updateStatus();
		setStatus(active());
	}

	private void setStatus(StatusImpl active) {
		if (active != null) {
			ensurePopUp();
			label.text(active.message);
			panel.tooltip(active.tooltip);
			label.tooltip(active.tooltip);
			if (!Env.is(Env.SWING))
				popUp.visible(true);
			updateSize();
			popUp.visible(true);
		} else if (popUp != null) {
			popUp.visible(false);
			popUp = null;
		}
	}

	private IPopUp ensurePopUp() {
		if (popUp == null) {
			popUp = Display.instance().showPopUp().modal(false).autoHide(false)
					.glass(false);
			popUp.border().remove().style().shadow(2).color()
					.rgb(240, 195, 109);
			panel = popUp.container().panel().horizontal().spacing(5);
			yellow(panel);
			label = panel.addSpace(4).add().label();
			label.font().pixel(11);
			panel.addSpace(4);
		}
		return popUp;
	}

	static void yellow(IColored panel) {
		panel.color().rgb(YELLOW_R, YELLOW_G, YELLOW_B);
	}

	private void updateStatus() {
		for (int i = queue.size() - 1; i >= 0; i--) {
			StatusImpl status = queue.get(i);
			if (status.hideAt != null
					&& System.currentTimeMillis() > status.hideAt) {
				queue.remove(status);
			} else if (status.showAt != null
					&& System.currentTimeMillis() > status.showAt) {
				status.showAt = null;
			}
		}
	}

	private void schedule(long showAt) {
		Display.instance().invokeLater(this, showAt + 100);
	}

	void updateSize() {
		onResize(Display.instance().width(), -1);
	}

	@Override
	public void onResize(int width, int height) {
		if (popUp == null)
			return;
		int x = (width - popUp.width()) / 2;
		popUp.offset(x, DisplayResizeAdapter.decrement() + 4);
	}

	public Status show(String message, String tooltip, boolean lazy) {
		StatusImpl status = new StatusImpl(message, tooltip);
		queue.add(status);
		status.show(lazy);
		return status;
	}

	public static StatusPopUp instance() {
		return instance;
	}
}
