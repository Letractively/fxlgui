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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IResizeRegistry;

public class ResizeRegistryImpl<T> implements IResizeRegistry<T> {

	public class ResizeConfiguration implements IResizeConfiguration {

		protected final IResizeListener listener;
		private Object lifecycleLink;

		public ResizeConfiguration(IResizeListener listener) {
			this.listener = listener;
		}

		// @Override
		// public IResizeConfiguration singleton() {
		// Iterator<ResizeConfiguration> it = resizeListeners.iterator();
		// while (it.hasNext()) {
		// ResizeConfiguration next = it.next();
		// if (next != this
		// && next.listener.getClass().equals(listener.getClass()))
		// it.remove();
		// }
		// return this;
		// }

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
			listener.onResize(Display.instance().width(), Display.instance()
					.height());
		}

		@Override
		public void remove() {
			ResizeRegistryImpl.this.remove(this);
		}
	}

	private List<ResizeConfiguration> resizeListeners = new LinkedList<ResizeConfiguration>();
	protected T owner;

	ResizeRegistryImpl(T owner) {
		this.owner = owner;
	}

	ResizeRegistryImpl() {
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
		if (resizeListeners.size() > 10)
			throw new RuntimeException("Warning: Too many resize listeners");
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
	public final T removeResizeListener(IResizeListener listener) {
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
		return owner;
	}

	@Override
	public final T notifyResizeListeners() {
		for (ResizeConfiguration rc : new LinkedList<ResizeConfiguration>(
				resizeListeners)) {
			if (!isInvisible(rc))
				rc.fire();
			else
				remove(rc);
		}
		return owner;
	}

	@Override
	public final T notifyResizeListener(IResizeListener listener) {
		for (ResizeConfiguration rc : new LinkedList<ResizeConfiguration>(
				resizeListeners)) {
			if (rc.listener == listener && !isInvisible(rc))
				rc.fire();
		}
		return owner;
	}

}
