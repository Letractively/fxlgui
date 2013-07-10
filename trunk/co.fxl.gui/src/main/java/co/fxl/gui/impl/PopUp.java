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
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.style.impl.Style;

public class PopUp {

	public interface HistoryAdp {

		void notifyShowPopUp(Runnable l);

		void notifyClosePopUp();
	}

	public interface TransparentPopUp {

		IPopUp popUp();

		IVerticalPanel panel();
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

	public static IPopUp showPopUp() {
		return showPopUp(false);
	}

	public static IPopUp showPopUp(boolean withBorder) {
		final IPopUp popUp = display.showPopUp();
		if (active) {
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
			border.color().black();
		}
		return popUp;
	}

	private static boolean ignoreNotify = false;

	public static TransparentPopUp showClosablePopUp(boolean closable,
			final Runnable closeListener) {
		final IPopUp popUp = display.showPopUp();
		if (adp != null) {
			adp.notifyShowPopUp(closeListener != null ? closeListener
					: new Runnable() {
						@Override
						public void run() {
							popUp.visible(false);
						}
					});
		}
		if (closeListener == null) {
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {

				@Override
				public void onUpdate(Boolean value) {
					if (!value && !ignoreNotify) {
						adp.notifyClosePopUp();
					}
				}
			});
		}
		if (!ALLOW_CLOSABLE_POPUP || !closable) {
			popUp.border().remove().style().shadow();
			Style.instance().popUp().background(popUp);
			return new TransparentPopUp() {

				@Override
				public IPopUp popUp() {
					return popUp;
				}

				@Override
				public IVerticalPanel panel() {
					return popUp.container().panel().vertical();
				}
			};
		}
		// popUp.color().remove();
		popUp.border().remove();
		popUp.transparent(true);
		final IGridPanel panel = popUp.container().panel().grid();
		panel.color().remove();
		// panel.addSpace(LABEL_DISTANCE);
		final IVerticalPanel p = panel
				.cell(0, Env.runtime().leq(Env.IE, 8) ? 1 : 0).panel()
				.vertical();
		p.border().remove().style().shadow();
		Style.instance().popUp().background(p);
		panel.cell(1, 0).align().end().valign().begin()
				.image()
				.resource("close_24x24.png")
				// .label().text("Close")
				.addClickListener(new LazyClickListener() {
					@Override
					protected void onAllowedClick() {
						ignoreNotify = true;
						popUp.visible(false);
						ignoreNotify = false;
						if (adp != null) {
							adp.notifyClosePopUp();
						}
						// if (closeListener != null)
						// closeListener.run();
						// else
					}
				}).mouseLeft().margin()
				.top(Env.runtime().leq(Env.IE, 8) ? 0 : -12)
				.left(Env.runtime().leq(Env.IE, 8) ? 0 : -12);// .font().underline(true).pixel(13).color().white();
		return new TransparentPopUp() {
			@Override
			public IVerticalPanel panel() {
				return p;
			}

			@Override
			public IPopUp popUp() {
				return popUp;
			}
		};
	}

	public static IDialog showDialog() {
		return display.showDialog();
	}
}