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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.log.impl.LogWidgetProvider;

public abstract class DisplayTemplate extends RegistryImpl<IDisplay> implements
		IDisplay {

	public final class ResizeConfiguration implements IResizeConfiguration {

		protected final IResizeListener listener;
		private Object lifecycleLink;

		public ResizeConfiguration(IResizeListener listener) {
			this.listener = listener;
		}

		@Override
		public IResizeConfiguration singleton() {
			Iterator<ResizeConfiguration> it = resizeListeners.iterator();
			while (it.hasNext()) {
				ResizeConfiguration next = it.next();
				if (next != this
						&& next.listener.getClass().equals(listener.getClass()))
					it.remove();
			}
			return this;
		}

		@Override
		public IResizeConfiguration linkLifecycle(IElement<?> element) {
			lifecycleLink = element;
			return this;
		}

		@Override
		public IResizeConfiguration linkLifecycle(IPopUp element) {
			lifecycleLink = element;
			return this;
		}

		private void fire() {
			boolean active = listener.onResize(width(), height());
			if (!active) {
				removeResizeListener(listener);
			}
		}

		@Override
		public void remove() {
			DisplayTemplate.this.remove(this);
		}
	}

	private List<ResizeConfiguration> resizeListeners = new LinkedList<ResizeConfiguration>();

	protected DisplayTemplate() {
		register(new LogWidgetProvider());
	}

	@Override
	public final IResizeConfiguration addResizeListener(
			final IResizeListener listener) {
		List<ResizeConfiguration> toRemove = new LinkedList<ResizeConfiguration>();
		for (ResizeConfiguration cfg : resizeListeners) {
			if (isInvisible(cfg)) {
				toRemove.add(cfg);
			}
		}
		for (ResizeConfiguration cfg : toRemove)
			remove(cfg);
		ResizeConfiguration resizeHandler = new ResizeConfiguration(listener);
		resizeListeners.add(resizeHandler);
		return resizeHandler;
	}

	private boolean isInvisible(ResizeConfiguration cfg) {
		if (cfg.lifecycleLink == null)
			return false;
		if (cfg.lifecycleLink instanceof IElement<?>) {
			return !((IElement<?>) cfg.lifecycleLink).visible();
		}
		return !((IPopUp) cfg.lifecycleLink).visible();
	}

	protected void remove(ResizeConfiguration cfg) {
		resizeListeners.remove(cfg);
	}

	@Override
	public final IDisplay removeResizeListener(IResizeListener listener) {
		ResizeConfiguration toRemove = null;
		for (ResizeConfiguration cfg : resizeListeners) {
			if (cfg.listener == listener) {
				toRemove = cfg;
				break;
			}
		}
		if (toRemove != null) {
			remove(toRemove);
		}
		return this;
	}

	@Override
	public final IDisplay notifyResizeListeners() {
		for (ResizeConfiguration rc : new LinkedList<ResizeConfiguration>(
				resizeListeners)) {
			rc.fire();
		}
		return this;
	}

	@Override
	public final IDisplay notifyResizeListener(IResizeListener listener) {
		for (ResizeConfiguration rc : new LinkedList<ResizeConfiguration>(
				resizeListeners)) {
			if (rc.listener == listener)
				rc.fire();
		}
		return this;
	}
}
