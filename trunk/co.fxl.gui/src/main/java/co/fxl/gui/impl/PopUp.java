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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.style.impl.Style;

public class PopUp implements RuntimeConstants {

	// private static final int POPUP_ROW = Env.runtime().leq(Env.IE, 8) ? 1 :
	// 0;

	public interface HistoryAdp {

		void notifyShowPopUp(Runnable l);

		void notifyClosePopUp();
	}

	public static class TransparentPopUp {

		public boolean ignoreNotify = false;
		public IPopUp popUp;
		public IVerticalPanel panel;
		public Runnable closeListener;
		protected boolean destroyed;

		public void show() {
			if (adp != null) {
				adp.notifyShowPopUp(closeListener != null ? closeListener
						: new Runnable() {
							@Override
							public void run() {
								if (!destroyed)
									popUp.visible(false);
							}
						});
			}
		}
	}

	private static final boolean ALLOW_CLOSABLE_POPUP = true;
	private static IDisplay display = Display.instance();
	private static Set<IPopUp> visiblePopUps = new HashSet<IPopUp>();
	private static boolean active = true;
	public static HistoryAdp adp = null;

	public static void activate() {
		active = true;
	}

	public static void closeAll() {
		for (IPopUp popUp : new LinkedList<IPopUp>(visiblePopUps))
			popUp.visible(false);
	}

	public static IPopUp showClosablePopUp() {
		return showPopUp(false);
	}

	public static IPopUp showPopUp(boolean withBorder) {
		return showPopUp(withBorder, true);
	}

	public static IPopUp showPopUp(boolean withBorder, boolean bulkClose) {
		final IPopUp popUp = display.showPopUp();
		Style.instance().popUp().background(popUp);
		if (active && bulkClose) {
			visiblePopUps.add(popUp);
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						visiblePopUps.remove(popUp);
					} else
						visiblePopUps.add(popUp);
				}
			});
		}
		if (withBorder) {
			IBorder border = popUp.border();
			border.remove().style().shadow();
			Style.instance().popUp(border);
		}
		return popUp;
	}

	public static TransparentPopUp showClosablePopUp(boolean closable,
			final Runnable closeListener, boolean noDiscardChangesDialog) {
		return showClosablePopUp(closable, closeListener, true,
				noDiscardChangesDialog);
	}

	public static TransparentPopUp showClosablePopUp(boolean closable,
			final Runnable closeListener, boolean pushState,
			boolean noDiscardChangesDialog) {
		final TransparentPopUp t = new TransparentPopUp();
		t.closeListener = closeListener;
		final IPopUp popUp = display.showPopUp();
		if (pushState)
			t.show();
		if (closeListener == null) {
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {

				@Override
				public void onUpdate(Boolean value) {
					if (!value && !t.ignoreNotify && !t.destroyed) {
						adp.notifyClosePopUp();
					}
				}
			});
		}
		if (!ALLOW_CLOSABLE_POPUP || !closable) {
			popUp.border().remove().style().shadow();
			Style.instance().popUp().background(popUp);
			t.popUp = popUp;
			t.panel = popUp.container().panel().vertical();
			return t;
		}
		// popUp.color().remove();
		popUp.border().remove();
		popUp.transparent(true);
		final IGridPanel panel = popUp.container().panel().grid();
		panel.color().remove();
		// panel.addSpace(LABEL_DISTANCE);
		final IVerticalPanel p = panel.cell(0, 0).panel().vertical();
		IBorder b = p.border().remove();
		b.style().shadow();
		b.style().rounded().width(6);
		b.width(1).color().gray();
		Style.instance().popUp().background(p);
		IGridCell cell = panel.cell(1, 0).align().end().valign().begin();
		if (IE_LEQ_8) {
			cell.padding().left(2).top(2).right(2);
			b = cell.border();
			b.style().top().style().right().style().bottom();
			b.width(1).color().gray();
			cell.color().gray(222);
		}
		cell.image()
				.resource("close_24x24.png")
				// .label().text("Close")
				.addClickListener(
						new LazyClickListener(noDiscardChangesDialog) {
							@Override
							protected void onAllowedClick() {
								t.ignoreNotify = true;
								popUp.visible(false);
								t.ignoreNotify = false;
								if (adp != null && !t.destroyed) {
									adp.notifyClosePopUp();
								}
								// if (closeListener != null)
								// closeListener.run();
								// else
							}
						}).mouseLeft().margin()
				.top(Env.runtime().leq(Env.IE, 8) ? 0 : -12)
				.left(Env.runtime().leq(Env.IE, 8) ? 0 : -12);// .font().underline(true).pixel(13).color().white();
		t.popUp = popUp;
		t.panel = p;
		return t;
	}

	public static IDialog showDialog() {
		return display.showDialog();
	}

	public static void autoHideOnResize(final TransparentPopUp p) {
		Display.instance().addResizeListener(new IResizeListener() {
			@Override
			public void onResize(int width, int height) {
				p.popUp.visible(false);
			}
		}).linkLifecycle(p.popUp);
	}
}
