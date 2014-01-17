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
package co.fxl.gui.filter.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.fxl.data.format.api.IFormat;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.impl.CellImpl.ExplicitRangeField;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.log.impl.Log;
import co.fxl.gui.style.api.IStyle.IFilterPanel;

class MiniFilterPanel implements FilterPanel {

	class ViewComboBoxImpl implements ViewComboBox {

		private IComboBox comboBox;

		ViewComboBoxImpl(IComboBox comboBox) {
			this.comboBox = comboBox;
		}

		@Override
		public void text(String c) {
			if (c != null)
				comboBox.text(c);
		}

		@Override
		public void addUpdateListener(IUpdateListener<String> l) {
			comboBox.addUpdateListener(l);
		}

		@Override
		public void addText(String c) {
			comboBox.addText(c);
		}

	}

	class FilterGridImpl implements FilterGrid, IUpdateListener<String> {

		class CellImpl implements ICell {

			// private final class CombinedRangeField implements RangeField {
			// private final ITextField tf;
			// private String separator = "-";
			//
			// private CombinedRangeField(IContainer c) {
			// this.tf = c.textField().width(
			// FilterTemplate.WIDTH_SINGLE_CELL);
			// Heights.INSTANCE.decorate(tf);
			// widget.register(tf);
			// }
			//
			// @Override
			// public String text() {
			// return texts()[0];
			// }
			//
			// private String[] texts() {
			// String[] s = tf.text().trim().split(separator);
			// if (s.length == 1)
			// return new String[] { s[0], s[0] };
			// String low = s[0].trim();
			// String high = s[1].trim();
			// return new String[] { low, high };
			// }
			//
			// private void texts(String[] strings) {
			// StringBuilder b = new StringBuilder();
			// if (!isNull(strings[0])) {
			// b.append(strings[0].trim());
			// }
			// if (!isNull(strings[0]) || !isNull(strings[1]))
			// b.append(separator);
			// if (!isNull(strings[1])) {
			// b.append(strings[1].trim());
			// }
			// tf.text(b.toString());
			// }
			//
			// private boolean isNull(String string) {
			// return string == null || string.trim().isEmpty();
			// }
			//
			// @Override
			// public String upperBoundText() {
			// return texts()[1];
			// }
			//
			// @Override
			// public void upperBoundText(String text) {
			// texts(new String[] { texts()[0], text });
			// }
			//
			// @Override
			// public void addUpdateListener(
			// final IUpdateListener<String> listener) {
			// tf.addUpdateListener(new IUpdateListener<String>() {
			// @Override
			// public void onUpdate(String value) {
			// listener.onUpdate(texts()[0]);
			// }
			// });
			// }
			//
			// @Override
			// public void upperBoundAddUpdateListener(
			// final IUpdateListener<String> listener) {
			// tf.addUpdateListener(new IUpdateListener<String>() {
			// @Override
			// public void onUpdate(String value) {
			// listener.onUpdate(texts()[1]);
			// }
			// });
			// }
			//
			// @Override
			// public void text(String text) {
			// texts(new String[] { text, texts()[1] });
			// }
			//
			// @Override
			// public void validation(Validation validation, Class<?> type) {
			// validation.linkInput(tf);
			// if (type.equals(Date.class)) {
			// tf.tooltip("Ranges can be declared as \""
			// + Format.date().format(new Date(10, 0, 1))
			// + Format.dateRangeSeparator()
			// + Format.date().format(new Date(30, 0, 1))
			// + "\"");
			// separator = Format.dateRangeSeparator();
			// } else if (type.equals(Long.class)) {
			// tf.tooltip("Ranges can be declared as \"" + 1 + "-"
			// + 100 + "\"");
			// }
			// }
			// }

			private IContainer container = null;

			CellImpl() {
				container = cardPanel.add();
			}

			@Override
			public IComboBox comboBox() {
				return container.comboBox();
			}

			@Override
			public RangeField horizontal(boolean isDateField, IFormat<Date> f) {
				// if (FIREFOX)
				// return new CombinedRangeField(container);
				// else
				return new ExplicitRangeField(widget, container, true, f, 55);
			}

			@Override
			public ITextField textField() {
				return container.textField();
			}

			@Override
			public IDockPanel dock() {
				return container.panel().dock();
			}

			public IContainer container() {
				assert container != null;
				return container;
			}
		}

		private IComboBox comboBox;
		private ICardPanel cardPanel;
		private Map<String, Integer> name2index = new HashMap<String, Integer>();
		private Map<Integer, CellImpl> index2cell = new HashMap<Integer, CellImpl>();
		int rowInc = 0;

		FilterGridImpl(IContainer container) {
			IHorizontalPanel panel = container.panel().horizontal();
			comboBox = panel.add().comboBox();
			styleInput(comboBox);
			panel.addSpace(4);
			cardPanel = panel.add().panel().card();
		}

		FilterGridImpl(IToolbar panel) {
			comboBox = panel.add().comboBox().width(100);
			styleInput(comboBox);
			// panel.addSpace(4);
			cardPanel = panel.add().panel().card();
		}

		private void styleInput(IComboBox comboBox) {
			comboBox.border().color().rgb(211, 211, 211);
			comboBox.color().rgb(249, 249, 249);
		}

		void visible() {
			CellImpl cellImpl = index2cell.get(0);
			assert cellImpl != null : index2cell.toString();
			IContainer container = cellImpl.container();
			cardPanel.show(container.element());
			if (index2cell.size() == 1)
				comboBox.editable(false);
			else
				comboBox.addUpdateListener(this);
		}

		@Override
		public ICell cell(int row) {
			CellImpl cellImpl = index2cell.get(row + rowInc);
			if (cellImpl == null) {
				cellImpl = new CellImpl();
				index2cell.put(row + rowInc, cellImpl);
			}
			return cellImpl;
		}

		@Override
		public void title(int filterIndex, String name) {
			comboBox.addText(name);
			name2index.put(name, filterIndex + rowInc);
		}

		@Override
		public void onUpdate(String value) {
			Integer key = name2index.get(value);
			if (key == null)
				Log.instance().error(
						value + " not found in " + name2index.keySet());
			CellImpl c = index2cell.get(key);
			if (c == null)
				throw new UnsupportedOperationException(key + " not found in "
						+ index2cell.keySet());
			cardPanel.show(c.container.element());
		}

		@Override
		public void register(ITextField tf) {
			widget.register(tf);
		}

		@Override
		public IFilterPanel heights() {
			return widget.heights;
		}

		@Override
		public void resize(int size) {
		}

		@Override
		public void show(FilterPart<?> firstConstraint) {
			if (firstConstraint != null)
				comboBox.text(firstConstraint.name());
		}

		@Override
		public FilterGrid rowInc(int rowInc) {
			this.rowInc = rowInc;
			return this;
		}

		@Override
		public boolean addTooltips() {
			return false;
		}

		@Override
		public void notifyComboBoxChange(boolean clickableClear) {
			widget.notifyComboBoxChange(clickableClear);
		}

		@Override
		public void clearRowIterator() {
			MiniFilterPanel.this.clearRowIterator();
		}

		@Override
		public void updateFilters() {
			widget.updateFilters();
		}

		@Override
		public void refresh() {
			widget.onApplyClick(false);
		};
	}

	private static final boolean MODIFIED_TITLE_ADD = false;// Env.is(Env.FIREFOX,
															// Env.OPERA);
	// Constants.get(
	// "MiniFilterPanel.MODIFIED_TITLE_ADD", false);
	private IToolbar panel;
	private IToolbar titlePanel;
	private IToolbar mainPanel;
	private IToolbar hyperLinkPanel;
	private IToolbar gridContainer;
	private FilterGridImpl grid;
	// private boolean hasHyperlinks = false;
	private MiniFilterWidgetImpl widget;

	// private ViewComboBox viewComboBox;

	MiniFilterPanel(MiniFilterWidgetImpl widget, IContainer c) {
		this.widget = widget;
		IContainer add = c.panel().horizontal().align().begin().add();
		panel = new ToolbarImpl(add);
		titlePanel = panel.addGroup();
		mainPanel = panel.addGroup();
		hyperLinkPanel = panel.addGroup();
	}

	@Override
	public void visible() {
		grid.visible();
		panel.adjustHeights();
	}

	@Override
	public ILabel addTitle(String string) {
		IToolbar tb = titlePanel;
		ILabel l = getContainer(tb).label().text(string);
		l.font().weight().bold();
		return l;
		// l.margin().top(-4);
	}

	public IContainer getContainer(final IToolbar tb) {
		IContainer c = tb.add();
		if (MODIFIED_TITLE_ADD)
			c = c.panel().vertical().height(26).addSpace(6).add();
		return c;
	}

	@Override
	public IClickable<?> addHyperlink(String imageResource, String string,
			boolean isApply) {
		final IImage resource = getContainer(hyperLinkPanel).image().resource(
				imageResource);
		return resource;/*
						 * new IClickable<IImage>() {
						 * 
						 * @Override public IImage clickable(boolean clickable)
						 * { resource.visible(clickable); return resource; }
						 * 
						 * @Override public boolean clickable() { return
						 * resource.visible(); }
						 * 
						 * @Override public
						 * co.fxl.gui.api.IClickable.IKey<IImage>
						 * addClickListener(
						 * co.fxl.gui.api.IClickable.IClickListener
						 * clickListener) { return
						 * resource.addClickListener(clickListener); } };
						 */
	}

	@Override
	public void linksVisible(boolean b) {
		hyperLinkPanel.visible(b);
	}

	@Override
	public FilterGrid filterGrid() {
		if (gridContainer == null) {
			gridContainer = mainPanel.addGroup();
		} else
			gridContainer.clear();
		return grid = new FilterGridImpl(gridContainer);
	}

	@Override
	public ViewComboBox viewComboBox() {
		// if (viewComboBox != null)
		// return viewComboBox;
		if (grid == null) {
			filterGrid();
		}
		grid.rowInc = 1;
		grid.title(-1, "View");
		return new ViewComboBoxImpl(grid.cell(-1).comboBox());
	}

	@Override
	public WidgetTitle widgetTitle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearRowIterator() {
		widget.clearRowIndex();
	}

	@Override
	public IContainer top() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void refreshButton(IClickListener clickListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isMiniFilter() {
		return true;
	}

}
