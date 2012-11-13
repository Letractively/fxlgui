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
import java.util.Map;

import co.fxl.gui.impl.Display;
import co.fxl.gui.log.impl.Log;

class LinearPanelOptimization {

	// TODO replace all one-child-panel calls
	// add().panel().vertical()
	// with:
	// add().widget(ISimplePanel.class).vertical()

	// for Swing: dummy
	// for GWT: optimized, no actual panels but decorations

	static final boolean ENABLED = true;
	private static LinearPanelOptimization instance = new LinearPanelOptimization();
	private Map<SwingPanel<?>, Exception> queue = new HashMap<SwingPanel<?>, Exception>();
	private Map<String, Integer> history = new HashMap<String, Integer>();

	// private int allLinearPanels = 0;
	// private int singleChild = 0;

	private LinearPanelOptimization() {
	}

	void register(SwingPanel<?> panel) {
		if (queue.isEmpty())
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					for (SwingPanel<?> p : queue.keySet()) {
						// allLinearPanels++;
						if (p.container.component.getComponentCount() == 1) {
							Exception exception = queue.get(p);
							String identifier = identifier(exception);
							if (identifier
									.startsWith("co.fxl.gui.impl.SimplePanelImpl"))
								continue;
							if (!history.containsKey(identifier)) {
								Log.instance().warn(
										"1-Child-Panel: " + identifier,
										queue.get(p));
								history.put(identifier, 1);
							} else {
								history.put(identifier,
										history.get(identifier) + 1);
							}
							// singleChild++;
						}
					}
					// Log.instance().debug(
					// "All linear panels: " + allLinearPanels
					// + ", single-child: " + singleChild);
					// List<String> s = new
					// LinkedList<String>(history.keySet());
					// Collections.sort(s, new Comparator<String>() {
					// @Override
					// public int compare(String o1, String o2) {
					// return history.get(o2) - history.get(o1);
					// }
					// });
					// for (int i = 0; i < s.size() && i < 20; i++) {
					// Log.instance().debug(
					// "Top-" + (i + 1) + "-Single-Child: " + s.get(i)
					// + " (" + history.get(s.get(i)) + ")");
					// }
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
