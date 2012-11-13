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
package co.fxl.gui.swing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import co.fxl.gui.impl.Display;
import co.fxl.gui.log.impl.Log;

class LinearPanelOptimization {

	static final boolean ENABLED = true;
	private static LinearPanelOptimization instance = new LinearPanelOptimization();
	private Map<SwingPanel<?>, Exception> queue = new HashMap<SwingPanel<?>, Exception>();
	private Set<String> history = new HashSet<String>();

	private LinearPanelOptimization() {
	}

	void register(SwingPanel<?> panel) {
		if (queue.isEmpty())
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					for (SwingPanel<?> p : queue.keySet()) {
						if (p.container.component.getComponentCount() == 1) {
							Exception exception = queue.get(p);
							String identifier = identifier(exception);
							if (!history.contains(identifier)) {
								Log.instance().warn(
										"1-Child-Panel: "
												+ identifier, queue.get(p));
								history.add(identifier);
							}
						}
					}
					queue.clear();
				}

				private String identifier(Exception exception) {
					for (StackTraceElement e : exception.getStackTrace())
						if (!e.getClassName().startsWith("co.fxl.gui.swing."))
							return e.toString();
					return exception.getStackTrace()[0].toString();
				}
			});
		queue.put(panel, new RuntimeException());
	}

	static LinearPanelOptimization instance() {
		return instance;
	}
}
