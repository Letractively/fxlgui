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
package co.fxl.gui.table.scroll.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseWheelListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPoint;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IMiniFilterWidget;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.ColorTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.KeyAdapter;
import co.fxl.gui.impl.ResizableWidgetTemplate;
import co.fxl.gui.impl.StatusDisplay;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.table.api.ISelection;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IColumn;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IGrouping;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ILabelMouseListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IRow;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableClickListener;
import co.fxl.gui.table.scroll.api.ICellUpdateListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableColumn.IScrollTableListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.api.IDragDropListener.IDragArea;
import co.fxl.gui.table.util.api.ILazyScrollPane;

public class ScrollTableWidgetImpl extends ResizableWidgetTemplate implements
		IScrollTableWidget<Object>, ILabelMouseListener, TableWidgetAdp {

	// TODO Usability: editable table: if illegal value has been inserted (e.g.
	// 25.13.2002), then ignore input, reset to original and show fading error
	// message (a la google docs saving...) that doesnt requrire click on accept

	// TODO SWING-FXL: Scroll Panel block increment for single click on arrow is
	// not
	// enough

	// TODO SWING-FXL: Usability: Spaltenbreiten werden unter Swing nicht
	// berücksichtigt

	class State {

		private String imageResource;
		private IClickListener clickListener;
		private boolean active;

		State(String imageResource, IClickListener clickListener, boolean active) {
			this.imageResource = imageResource;
			this.clickListener = clickListener;
			this.active = active;
		}

	}

	public class StateToggleButton implements
			co.fxl.gui.table.scroll.api.IScrollTableWidget.IStateToggleButton {

		private List<State> states = new LinkedList<State>();

		@Override
		public co.fxl.gui.table.scroll.api.IScrollTableWidget.IStateToggleButton addState(
				String imageResource, IClickListener clickListener,
				boolean active) {
			states.add(new State(imageResource, clickListener, active));
			return this;
		}

	}

	private static final int FONTSIZE_NOTHING_FOUND_FILTER = 12;
	// private static final boolean ALLOW_RESIZE = false;
	private static final int HEADER_ROW_HEIGHT = 24;
	private static final int ROW_HEIGHT = 22;
	protected static final int SCROLL_MULT = 33;
	private static final boolean ADD_DRAG_AND_DROP = true;
	private static final boolean ADD_TOP_PANEL_TOP_PADDING = Constants.get(
			"ScrollTableWidgetImpl.ADD_TOP_PANEL_TOP_PADDING", false);
	private static final boolean ADD_TOP_PANEL_SPACING = Constants.get(
			"ScrollTableWidgetImpl.ADD_TOP_PANEL_SPACING", false);
	public static int MAX_CLIENT_SORT_SIZE = IFilterWidget.MIN_FILTER_SIZE - 1;
	IVerticalPanel container;
	private int height = 400;
	private WidgetTitle widgetTitle;
	RowAdapter rows;
	private int paintedRows;
	List<ScrollTableColumnImpl> columns = new LinkedList<ScrollTableColumnImpl>();
	List<ScrollTableColumnImpl> filterColumns = new LinkedList<ScrollTableColumnImpl>();
	private SelectionImpl selection = new SelectionImpl(this);
	private IVerticalPanel contentPanel;
	int sortColumn = -1;
	int sortNegator = -1;
	IBulkTableWidget grid;
	int rowOffset = 0;
	List<IRow> highlighted = new LinkedList<IRow>();
	private IGridPanel statusPanel;
	private String tooltip = "";// Use CTRL + Click to select multiple rows.";
	private boolean visible;
	private IRows<Object> actualRows;
	private IContainer c0;
	private boolean addBorders;
	private boolean updating = false;
	private Map<ITableClickListener, KeyAdapter<Object>> listeners = new HashMap<ITableClickListener, KeyAdapter<Object>>();
	boolean selectionIsSetup = false;
	private ILazyScrollPane sp;
	private ISortListener sortListener;
	boolean addClickListeners = false;
	IFilterWidget filter;
	private boolean allowColumnSelection = true;
	private IFilterConstraints constraints;
	private IFilterListener filterListener;
	private IButtonPanelDecorator buttonDecorator;
	private INavigationPanelDecorator navigationDecorator;
	private CommandButtonsImpl commandButtons;
	List<Object> preselectedList = new LinkedList<Object>();
	// int preselectedIndex = -1;
	private boolean showDisplayedRange = true;
	private boolean externalStatusPanel;
	private IVerticalPanel bottom;
	private int buttonColumn = 1;
	String[] viewComboBoxText;
	private IUpdateListener<String> viewComboBoxUpdateListener;
	private int viewInc;
	private String viewComboBoxChoice;
	private List<IRowIndexListener> scrollListeners = new LinkedList<IRowIndexListener>();
	private int rowIndex = -1;
	private boolean addToContextMenu;
	private IUpdateListener<List<String>> hiddenColumnListener;
	private boolean filterSizeConstraint = true;
	private boolean allowInsertUnder;
	IDragDropListener dragDropListener;
	IGridPanel topPanel;
	boolean showNoRowsFound = true;
	private IVerticalPanel contentPanel0;
	private boolean reduceHeightIfEmpty = false;
	private boolean showConfiguration = true;
	private IColumnWidthInjector columnWidths = ColumnWidths.newInstance(true);

	// private IVerticalPanel editPanel;

	ScrollTableWidgetImpl(IContainer container) {
		c0 = container;
	}

	@Override
	public WidgetTitle widgetTitle() {
		if (widgetTitle == null) {
			widgetTitle = new WidgetTitle(c0.panel(), addBorders, plainContent)
					.foldable(false);
			widgetTitle.addSubTitles(subTitle1, subTitle2);
			widgetTitle.addToContextMenu(addToContextMenu);
			widgetTitle.commandsOnTop();
			widgetTitle.hyperlinkVisible(false);
			widgetTitle.spacing(0);
			container = widgetTitle.content().panel().vertical();
			// editPanel = container.add().panel().vertical().visible(false);
		}
		return widgetTitle;
	}

	@Override
	public void onResize(int width, int height) {
		updateTable();
	}

	private IVerticalPanel container() {
		if (container == null) {
			widgetTitle();
		}
		return container;
	}

	// @Override
	// public IVerticalPanel editPanel() {
	// return editPanel;
	// }

	@Override
	public int offsetY() {
		return container().offsetY();
	}

	private boolean drawing = false;
	private Integer nextHeight = null;
	private ICellUpdateListener updateListener;
	private boolean addDragAndDropDirectly;
	private boolean alwaysShowFilter;
	private StateToggleButton toggleButton;
	private boolean plainContent;
	private IVerticalPanel topPanelContainer;
	private INoEntitiesFoundDecorator noEntitiesFoundDecorator;
	private String filterQueryLabel;
	private String subTitle1;
	private String subTitle2;
	private int presetRowIndex = 0;
	private int widthDelta;
	private IGrouping grouping;
	private IClickListener configureListener;
	private boolean hasColumnSelection;
	private ILabel rightPaging;
	private ILabel statusRangeLabel;
	private ILabel leftPaging;

	// private boolean nextTimeShowPopUp;

	@Override
	public IScrollTableWidget<Object> filterQueryLabel(String filterQueryLabel) {
		this.filterQueryLabel = filterQueryLabel;
		return this;
	}

	// @Override
	// public IScrollTableWidget<Object> height(final int height) {
	// return height(height, DummyCallback.voidInstance());
	// }

	@Override
	public IScrollTableWidget<Object> height(final int height) {
		this.height = height;
		if (visible) {
			if (drawing) {
				if (nextHeight == null) {
					nextHeight = height;
					c0.display().invokeLater(new Runnable() {
						@Override
						public void run() {
							int height = nextHeight;
							nextHeight = null;
							height(height);
						}
					});
				} else {
					nextHeight = height;
					// cb.onSuccess(null);
				}
				return this;
			} else {
				drawing = true;
				visible(true);
				drawing = false;
				// cb.onSuccess(null);
			}
		} else {
			// cb.onSuccess(null);
		}
		return this;
	}

	@Override
	public IScrollTableWidget<Object> rows(IRows<Object> rows) {
		this.actualRows = rows;
		if (visible) {
			visible(true);
		}
		return this;
	}

	@Override
	public IScrollTableWidget<Object> reduceHeightIfEmpty(
			boolean reduceHeightIfEmpty) {
		this.reduceHeightIfEmpty = reduceHeightIfEmpty;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> visible(boolean visible) {
		statusRangeLabel = null;
		hasColumnSelection = false;
		if (visible) {
			rows = new RowAdapter(this, actualRows);
			drawAll();
		} else {
			this.visible = false;
			throw new UnsupportedOperationException();
		}
		return this;
	}

	private void drawAll() {
		boolean updateSelection = false;
		if (sp != null)
			sp.visible(false);
		for (ScrollTableColumnImpl c : columns)
			if (c.forceSort) {
				sortBy(c, false);
			}
		if (commandButtons != null)
			commandButtons.reset();
		if (!preselectedList.isEmpty()) {
			// if (preselectedIndex != -1) {
			// preselectedIndex = rows.find(preselectedList.get(0));
			// if (preselectedIndex == -1) {
			// preselectedList.clear();
			// } else {
			// rows.selected(preselectedIndex, preselectedList.get(0));
			// if (commandButtons != null) {
			// commandButtons.selection = preselectedList.get(0);
			// commandButtons.selectionIndex = preselectedIndex;
			// }
			// }
			// } else {
			rows.selected(preselectedList);
			if (commandButtons != null) {
				commandButtons.selectionList(preselectedList);
			}
			// }
			updateSelection = true;
		}
		this.visible = true;
		if (!externalStatusPanel)
			statusPanel = null;
		else
			resetStatusPanel();
		topPanel = null;
		buttonColumn = 1;
		selectionIsSetup = false;
		container().clear();
		topPanel();
		viewInc = 0;
		if (columns.size() == 0
				|| (rows.size() == 0 && (constraints == null
						|| !constraints.isConstraintSpecified() || !hasFilter()))) {
			showNoRowsFound();
		} else if (rows.size() == 0
				&& (constraints != null && constraints.isConstraintSpecified() && hasFilter())) {
			showNoRowsFound();
		} else {
			addFilter();
			setUpTopPanel();
			contentPanel0 = container.add().panel().vertical();
			contentPanel0.height(heightCenterPanel());
			contentPanel = contentPanel0.add().panel().vertical();
			addDragAndDropDirectly = true;
			boolean tooLarge = update();
			addDragAndDropDirectly = false;
			if (tooLarge || paintedRows != rows.size()) {
				contentPanel0.clear();
				sp = (ILazyScrollPane) contentPanel0.add().widget(
						ILazyScrollPane.class);
				if (dragDropListener != null)
					sp.dragDropListener(allowInsertUnder, dragDropListener);
				sp.minRowHeight(getRowHeight());
				if (!preselectedList.isEmpty()) {
					int rowIndex = rows.find(preselectedList);
					if (rowIndex != -1)
						sp.rowIndex(rowIndex);
					if (this.rowIndex != -1) {
						sp.rowIndex(this.rowIndex);
						this.rowIndex = -1;
					}
				}
				if (presetRowIndex > 0) {
					sp.rowIndex(presetRowIndex);
					presetRowIndex = 0;
				}
				sp.height(heightCenterPanel());
				sp.decorator(new ILazyScrollPane.IDecorator() {

					@Override
					public IKeyRecipient<Object> decorate(IContainer container,
							int firstRow, int lastRow, boolean isCalibration) {
						rowOffset = firstRow;
						paintedRows = lastRow - firstRow + 1;
						contentPanel = container.panel().vertical();
						updateWithPaintedRowsSet(isCalibration);
						return grid;
					}

					@Override
					public boolean checkIndex(int rowIndex) {
						int visibleRowIndex = convert2GridRow(rowIndex);
						if (visibleRowIndex >= grid.rowCount()
								|| visibleRowIndex < 0)
							return false;
						return true;
					}

					@Override
					public int rowHeight(int rowIndex) {
						int visibleRowIndex = convert2GridRow(rowIndex);
						return grid.rowHeight(visibleRowIndex + 1);
					}

					private IElement<?>[] elementsAt(int index) {
						IElement<?>[] elements = new IElement<?>[grid
								.columnCount()];
						for (int i = 0; i < grid.columnCount(); i++) {
							elements[i] = grid.elementAt(i, index);
						}
						return elements;
					}

					@Override
					public int headerHeight() {
						return grid.rowHeight(0);
					}

					@Override
					public IDragArea dragArea(final int index) {
						return new IDragArea() {

							@Override
							public IColor color() {
								return new ColorTemplate() {

									@Override
									public IColor remove() {
										grid.row(index).removeBackground();
										return this;
									}

									@Override
									protected IColor setRGB(int r, int g, int b) {
										grid.row(index).background(r, g, b);
										return this;
									}
								};
							}

							@Override
							public IElement<?> imageElement() {
								return elementsAt(0)[0];
							}

						};
					}

					@Override
					public IFocusPanel getFocusPanel() {
						throw new UnsupportedOperationException();
					}

					@Override
					public void contentWidth(int width) {
					}
				});
				sp.size(rows.size());
				sp.plainContent(plainContent);
				sp.visible(true);
			}
		}
		preselectedList.clear();
		if (updateSelection)
			selection.updateButtons();
	}

	private void showNoRowsFound() {
		IVerticalPanel dock = container.add().panel().vertical();
		boolean furtherReduce = false;
		if (showNoRowsFound || columns.size() == 0) {
			// topPanelCell(viewInc + 0, 0).width(10).label()
			// .text("&#160;");
			// IGridCell begin = topPanelCell(1, 0).valign().begin()
			// .align().begin();
			furtherReduce = true;
			IGridPanel nefg = addNoEntitiesFound(dock);
			buttonColumn++;
			if (noEntitiesFoundDecorator != null) {
				nefg.column(0).expand();
				noEntitiesFoundDecorator.decorate(nefg.cell(1, 0).align().end()
						.valign().begin());
			}
		} else
			addFilter();
		dock.add().label().text("&#160;");
		if (!externalStatusPanel) {
			IGridPanel statusPanel2 = statusPanel();
			statusPanel2.cell(0, 0).label().text("&#160;");
			statusPanel2.height(32);
		}
		if (hasFilter())
			addFilter();
		setUpTopPanel();
		int height = heightCenterPanel();
		// if (showNoRowsFound) {
		// hack for IE
		if (reduceHeightIfEmpty && height >= 80)
			height -= 30;
		// }
		if (furtherReduce && height >= 80 && externalStatusPanel)
			height -= 30;
		dock.height(height);
	}

	public IGridPanel addNoEntitiesFound(IVerticalPanel dock) {
		return addNoEntitiesFound(dock, columns.isEmpty(), constraints,
				filterQueryLabel);
	}

	public static IGridPanel addNoEntitiesFound(IVerticalPanel dock,
			boolean columnsIsEmpty, IFilterConstraints constraints,
			String filterQueryLabel) {
		IGridPanel nefg = dock.spacing(10).add().panel().grid();
		IVerticalPanel nef = nefg.cell(0, 0).panel().vertical();
		String text = columnsIsEmpty ? "No columns specified."
				: "No entities found.";
		nef.add().panel().vertical().spacing(4).add().label().text(text);// .font().weight().bold();//
																			// .font().pixel(10).color().gray();
		if (constraints == null)
			return nefg;
		List<String[]> description = constraints.description();
		boolean hasHeader = false;
		if (constraints.configuration() != null && !columnsIsEmpty) {
			hasHeader = true;
			description.add(0, new String[] {
					filterQueryLabel != null ? filterQueryLabel
							: "Filter Query", constraints.configuration() });
		}
		if (!description.isEmpty() && !columnsIsEmpty) {// constraints
														// !=
														// null
														// &&
			// constraints.isSpecified())
			// {
			// && constraints.isConstraintSpecified()) {
			IGridPanel gp = nef.add().panel().horizontal().add().panel().grid()
					.resize(3, description.size()).spacing(4);
			// gp.cell(0, 0).label().text("Active filter:").font()
			// .pixel(FONTSIZE_NOTHING_FOUND_FILTER).color().gray();
			int i = 0;
			for (String[] d : description) {
				i = addQueryLabel(hasHeader && i == 0, gp, i, d);
			}
		}
		return nefg;
	}

	private static int addQueryLabel(boolean bold, IGridPanel gp, int i,
			String[] d) {
		ILabel l = gp.cell(0, i).label()
				.text(d[0] + (d[1].equals("") ? "" : ":"));
		// if (bold)
		// l.font().weight().bold();
		l.font().pixel(FONTSIZE_NOTHING_FOUND_FILTER).color().gray();
		if (d[1].length() > 24)
			d[1] = d[1].substring(0, 24) + "...";
		l = gp.cell(1, i++).label().autoWrap(true).text(d[1]);
		l.font().pixel(FONTSIZE_NOTHING_FOUND_FILTER).color().gray();
		// if(bold)
		// l.font().weight().bold();
		return i;
	}

	void setUpTopPanel() {
		if (navigationDecorator != null) {
			navigationPanel(navigationDecorator);
		}
		if (buttonDecorator != null) {
			buttonPanel(buttonDecorator);
		}
		if (commandButtons != null) {
			commandButtons.updateButtons();
		}
	}

	public int getRowHeight() {
		for (ScrollTableColumnImpl c : columns) {
			if (c.type.clazz.equals(Boolean.class)) {
				return ROW_HEIGHT + 4;
			}
		}
		return ROW_HEIGHT;
	}

	boolean hasFilter() {
		// if (rows.size() >= (Integer) FilterWidgetImpl.DEFAULT_SIZES.get(0))
		// return true;
		for (ScrollTableColumnImpl c : columns)
			if (c.filterable)
				return true;
		return viewComboBoxUpdateListener != null || alwaysShowFilter;
	}

	@Override
	public IScrollTableWidget<Object> alwaysShowFilter() {
		alwaysShowFilter = true;
		return this;
	}

	protected void addFilter() {
		filter = null;
		setUpFilter();
	}

	protected void setUpFilter() {
		if (viewComboBoxText != null) {
			createFilter();
			IFilter vl = filter.addFilter().name("View").required();
			IFieldType type = vl.type().text();
			for (String s : viewComboBoxText) {
				type.addConstraint(s);
			}
			vl.text(viewComboBoxChoice);
			vl.updateListener(viewComboBoxUpdateListener);
		}
		for (ScrollTableColumnImpl c : columns) {
			if (c.filterable) {
				createFilter();
				filter.addFilter().name(c.name).type(c.type);
			}
		}
		for (ScrollTableColumnImpl c : filterColumns) {
			if (c.filterable) {
				createFilter();
				filter.addFilter().name(c.name).type(c.type);
			}
		}
		if (filter == null && alwaysShowFilter) {
			createFilter();
		}
		if (filter != null) {
			if (filterListener != null)
				filter.addFilterListener(filterListener);
			if (filterSizeConstraint)
				filter.addSizeFilter();
			if (constraints != null)
				filter.constraints(constraints);
			filter.visible(true);
		}
	}

	void createFilter() {
		if (filter == null) {
			IHorizontalPanel h = topPanelCell(0, 0).panel().horizontal()
					.addSpace(8);
			if (toggleButton != null) {
				decorateToggleButtonCell(h.add().panel().horizontal());
				h.addSpace(8);
			}
			filter = (IMiniFilterWidget) h.add()
					.widget(IMiniFilterWidget.class);
			filter.showConfiguration(showConfiguration);
			if (showConfiguration)
				filter.firstConfiguration(viewComboBoxChoice);
		}
	}

	void decorateToggleButtonCell(final IHorizontalPanel iHorizontalPanel) {
		iHorizontalPanel.clear();
		boolean first = true;
		for (final State s : toggleButton.states) {
			if (!first) {
				iHorizontalPanel.addSpace(4);
			}
			first = false;
			iHorizontalPanel.add().image().resource(s.imageResource)
					.clickable(s.active).addClickListener(s.clickListener);
		}
	}

	protected int heightCenterPanel() {
		if (buttonDecorator == null && navigationDecorator == null
				&& (topPanel == null || topPanel.rows() == 0))
			return height;
		else {
			int tHeight = topPanel.height();
			return height - tHeight;
		}
	}

	private void topPanel() {
		if (topPanel == null) {
			if (ADD_TOP_PANEL_TOP_PADDING) {
				topPanelContainer = container().add().panel().vertical();
				topPanelContainer.padding().top(5);
				topPanelContainer.color().rgb(255, 255, 255).gradient()
						.fallback(250, 250, 250).vertical().rgb(245, 245, 245);
				topPanel = topPanelContainer.add().panel().grid();
				topPanelContainer.visible(false);
			} else if (ADD_TOP_PANEL_SPACING) {
				topPanelContainer = container().add().panel().vertical();
				topPanelContainer.spacing(5);
				topPanelContainer.color().rgb(255, 255, 255).gradient()
						.fallback(250, 250, 250).vertical().rgb(245, 245, 245);
				topPanel = topPanelContainer.add().panel().grid();
				topPanelContainer.visible(false);
			} else {
				topPanel = container().add().panel().grid();
				topPanel.color().rgb(255, 255, 255).gradient()
						.fallback(250, 250, 250).vertical().rgb(245, 245, 245);
				topPanel.visible(false);
			}
		}
	}

	public IGridPanel topPanelWithBorder() {
		topPanel();
		IBorder b = (topPanelContainer != null ? topPanelContainer : topPanel)
				.border();
		b.color().lightgray();
		b.style().bottom();
		if (topPanelContainer != null)
			topPanelContainer.visible(true);
		topPanel.visible(true);
		return topPanel;
	}

	public IGridPanel statusPanel() {
		if (statusPanel == null) {
			statusPanel = container().add().panel().grid().resize(3, 1);
			if (plainContent)
				statusPanel.visible(false).remove();
			else {
				statusPanel.spacing(4);
				IBorder border2 = statusPanel.border();
				border2.color().rgb(172, 197, 213);
				border2.style().top();
				statusPanel.color().rgb(249, 249, 249).gradient()
						.fallback(240, 240, 240).vertical().rgb(216, 216, 216);
			}
		}
		return statusPanel;
	}

	@Override
	public IScrollTableColumn<Object> addColumn() {
		ScrollTableColumnImpl column = new ScrollTableColumnImpl(this,
				columns.size());
		columns.add(column);
		return column;
	}

	@Override
	public IScrollTableColumn<Object> addFilterColumn() {
		ScrollTableColumnImpl column = new ScrollTableColumnImpl(this,
				columns.size() + filterColumns.size());
		filterColumns.add(column);
		return column;
	}

	@Override
	public boolean updateTable() {
		columnWidths.notifyColumnSelectionChange();
		visible(true);
		return true;
	}

	public boolean update() {
		paintedRows = computeRowsToPaint();
		return updateWithPaintedRowsSetNoCalibration();
	}

	boolean updateWithPaintedRowsSetNoCalibration() {
		return updateWithPaintedRowsSet(false);
	}

	boolean updateWithPaintedRowsSet(boolean isCalibration) {
		if (updating)
			return false;
		updating = true;
		highlighted.clear();
		contentPanel.clear();
		IBulkTableWidget lastGrid = grid;
		IVerticalPanel vpanel = contentPanel.add().panel().vertical();
		grid = (IBulkTableWidget) vpanel.add().widget(IBulkTableWidget.class);
		grid.marginTop(plainContent ? 0 : 6);
		grid.addToContextMenu(addToContextMenu);
		final int heightMinusTopPanel = heightCenterPanel();
		grid.height(heightMinusTopPanel - 6);
		for (IRowIndexListener rowIndexL : scrollListeners)
			rowIndexL.onScroll(rowOffset);
		updateHeaderRow(grid);
		if (grouping != null)
			grid.grouping(new IGrouping() {

				@Override
				public boolean hasGroupHeader(int index) {
					return grouping.hasGroupHeader(rowOffset + index);
				}

				@Override
				public String groupHeaderHTML(int index) {
					return grouping.groupHeaderHTML(rowOffset + index);
				}

			});
		for (int r = 0; r < paintedRows; r++) {
			int index = r + rowOffset;
			updateSingleContentRow(grid, r, index);
		}
		grid.visible(true);
		if (addDragAndDropDirectly && ADD_DRAG_AND_DROP
				&& dragDropListener != null) {
			new DragAndDropGridAdapter(this);
		}
		if (sp != null)
			grid.addMouseWheelListener(new IMouseWheelListener() {

				@Override
				public void onUp(int turns) {
					sp.onUp(turns);
				}

				@Override
				public void onDown(int turns) {
					sp.onDown(turns);
				}
			});
		if (addClickListeners) {
			int current = 0;
			for (ScrollTableColumnImpl c : columns)
				if (!c.clickListeners.isEmpty() && c.visible)
					grid.labelMouseListener(current++, this);
		}
		grid.element().tooltip(tooltip);
		if (lastGrid != null)
			lastGrid.remove();
		for (int r = 0; r < paintedRows; r++) {
			if (rows.selected(r + rowOffset)) {
				IRow row = grid.row(r);
				row.highlight(true);
				highlighted.add(row);
			}
		}
		if (!isCalibration)
			addDisplayingNote();
		if (allowColumnSelection) {
			addColumnSelection();
		}
		addSorting();
		selection.update();
		for (ITableClickListener l : listeners.keySet()) {
			KeyAdapter<Object> adp = listeners.get(l);
			@SuppressWarnings("unchecked")
			IKey<Object> key = (IKey<Object>) grid.addTableListener(l);
			adp.forward(key);
		}
		updating = false;
		if (hiddenColumnListener != null) {
			List<String> hiddenColumns = new LinkedList<String>();
			for (ScrollTableColumnImpl c : columns)
				if (!c.visible)
					hiddenColumns.add(c.name);
			hiddenColumnListener.onUpdate(hiddenColumns);
		}
		int h1 = heightMinusTopPanel - 2;
		int h2 = grid.tableHeight();
		// if (h2 == 0)
		// Log.instance().error(
		// "Scroll-Table-Widget: grid-heights are 0: " + h1 + " vs. "
		// + h2 + ", " + vpanel.visible());
		if (updateListener != null)
			addCellUpdateListener();
		return h1 < h2;
	}

	private void addColumnSelection() {
		if (hasColumnSelection)
			return;
		ColumnSelection.newInstance(this, false);
		hasColumnSelection = true;
	}

	void addCellUpdateListener() {
		grid.addTableListener(new ITableClickListener() {

			@Override
			public void onClick(final int column, final int row, IPoint p) {
				if (row == 0)
					return;
				ScrollTableColumnImpl columnImpl = columns.get(column);
				if (!columnImpl.editable)
					return;
				String t = (String) rows.row(rowOffset + row - 1)[column];
				final ITextField tf = grid.cell(column, row).container()
						.textField().text(t);
				if (!columnImpl.type.clazz.equals(String.class))
					throw new UnsupportedOperationException(
							"type not supported in scrolltable "
									+ columnImpl.type.clazz.getName());
				Display.instance().invokeLater(new Runnable() {
					@Override
					public void run() {
						tf.focus(true);
					}
				});
				tf.addKeyListener(new IClickListener() {
					@Override
					public void onClick() {
						setValue(column, row, tf);
					}
				}).enter();
				tf.addFocusListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						if (!value) {
							setValue(column, row, tf);
						}
					}
				});
			}
		});
	}

	void setValue(final int column, final int row, final ITextField tf) {
		Object[] ar = rows.row(rowOffset + row - 1);
		ar[column] = tf.text();
		refresh();
		updateListener.notifyUpdate(column, rowOffset + row - 1, tf.text());
	}

	private void updateSingleContentRow(IBulkTableWidget grid,
			int visibleGridIndex, int contentIndex) {
		Object[] row = rows.row(contentIndex);
		int current = 0;
		assert columns.size() == row.length : "Wrong number of columns: "
				+ columns.size() + " vs " + row.length + " ("
				+ Arrays.toString(row) + ")";
		for (ScrollTableColumnImpl c : columns)
			if (c.visible) {
				c.decorate(rows.identifier(contentIndex),
						grid.cell(current++, visibleGridIndex), row[c.index]);
			}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateHeaderRow(IBulkTableWidget grid) {
		columnWidths.columns((List) columns);
		int current = 0;
		columnWidths.startPrepare(
				StatusDisplay.instance().width() - widthDelta, rows,
				sortColumn(), false);
		for (int c = 0; c < columns.size(); c++) {
			if (!columns.get(c).visible)
				continue;
			ScrollTableColumnImpl columnImpl = columns.get(c);
			if (columnImpl.tagSortOrder != null) {
				sortColumn = columns.indexOf(columnImpl);
				sortNegator = columnImpl.tagSortOrder ? -1 : 1;
				columnImpl.tagSortOrder = null;
			}
			String name = columnImpl.name;
			Boolean sortUp = null;
			if (sortColumn == c) {
				sortUp = sortNegator == 1;
			}
			IColumn column = grid.column(current++);
			column.title(name, sortUp);
			prepare(columnWidths, columnImpl, column);
		}
	}

	public static void prepare(IColumnWidthInjector cw,
			ScrollTableColumnImpl columnImpl, IColumn column) {
		columnImpl.decorator().prepare(column);
		if (cw != null) {
			cw.prepare(columnImpl, column);
			if (columnImpl.isAlignmentSpecified()) {
				columnImpl.forwardAlignment(column.align());
			}
		}
		if (columnImpl.alignment.isSpecified()) {
			columnImpl.alignment.forward(column.align());
		}
	}

	private void addDisplayingNote() {
		if (!showDisplayedRange)
			return;
		if (statusRangeLabel == null) {
			IGridPanel.IGridCell clear = statusPanel().cell(2, 0).clear()
					.valign().center();
			clear.align().end();
			IHorizontalPanel p = clear.panel().horizontal().align().end().add()
					.panel().horizontal().align().end();
			leftPaging = p.add().label().text("<<");
			leftPaging.margin().right(4);
			leftPaging.clickable(true);
			leftPaging.hyperlink().font().pixel(10);
			leftPaging.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					int nextPreviousRow = constraints.rowIterator()
							.nextPreviousRow();
					constraints.rowIterator().firstRow(nextPreviousRow);
					filterListener.onApply(constraints,
							DummyCallback.voidInstance());
				}
			});
			String in = "DISPLAYING ROWS";
			ILabel label = p.add().label();
			label.text(in);
			label.font().pixel(10);
			statusRangeLabel = p.addSpace(4).add().label().text(getStatusRange(paintedRows()));
			statusRangeLabel.font().weight().bold().pixel(10);
			rightPaging = p.add().label().text(">>");
			rightPaging.margin().left(4);
			rightPaging.hyperlink();
			rightPaging.clickable(true);
			rightPaging.hyperlink().font().pixel(10);
			rightPaging.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					constraints.rowIterator().firstRow(
							constraints.rowIterator().nextFirstRow());
					filterListener.onApply(constraints,
							DummyCallback.voidInstance());
				}
			});
			p.addSpace(4);
		}
		leftPaging.visible(constraints != null
				&& constraints.rowIterator().hasPrevious());
		rightPaging.visible(constraints != null
				&& constraints.rowIterator().hasNext());
		Display.instance().invokeLater(new Runnable() {
			@Override
			public void run() {
				statusRangeLabel.text(getStatusRange(paintedRows()));
			}
		});
	}

	private String getStatusRange(int px) {
		int rt = rowOffset + px;
		if (rt > rows.size())
			rt = rows.size();
		int firstRow = constraints != null ? constraints.rowIterator()
				.firstRow() : 0;
		String status = +(firstRow + rowOffset + 1) + " - " + (firstRow + rt);
		return status;
	}

	private int paintedRows() {
		int c = 0;
		int h = grid.rowCount() > 0 ? grid.rowHeight(0) : 0;
		for (c = 0; c < paintedRows; c++) {
			if (h >= grid.height())
				return c;
			h += grid.rowHeight(c + 1);
		}
		return c;
	}

	private int computeRowsToPaint() {
		int paintedRows = 0;
		int prognosedHeight = HEADER_ROW_HEIGHT;
		int heightMinusTopPanel = heightCenterPanel();
		while (prognosedHeight < heightMinusTopPanel) {
			prognosedHeight += getRowHeight();
			paintedRows++;
		}
		paintedRows--;
		if (rowOffset + paintedRows >= rows.size())
			paintedRows = rows.size() - rowOffset;
		return paintedRows;
	}

	public void highlightAll() {
		highlighted.clear();
		for (int r = 0; r < paintedRows; r++) {
			IRow row = grid.row(r);
			row.highlight(true);
			highlighted.add(row);
		}
	}

	private void addSorting() {
		boolean sortable = false;
		for (ScrollTableColumnImpl c : columns) {
			if (c.sortable) {
				sortable |= c.sortable;
			}
		}
		if (sortable) {
			grid.addTableListener(new ITableClickListener() {

				@Override
				public void onClick(int column, int row, IPoint p) {
					if (row != 0)
						return;
					ScrollTableColumnImpl columnImpl = columns
							.get(realColumn(column));
					sortBy(columnImpl, true);
				}
			});
		}
	}

	private int realColumn(int column) {
		int visibleColumns = 0;
		int c = column;
		for (int i = 0; visibleColumns <= column; i++)
			if (!columns.get(i).visible)
				c++;
			else
				visibleColumns++;
		return c;
	}

	@Override
	public ILabel addTitle(String text) {
		addBorders = true;
		return widgetTitle().addTitle(text);
	}

	@Override
	public IClickable<?> addButton(String name) {
		return widgetTitle().addHyperlink(name);
	}

	@Override
	public IClickable<?> addButton(String name, String imageResource) {
		return widgetTitle().addHyperlink(imageResource, name);
	}

	@Override
	public IClickable<?> addButton(String name, String imageResource,
			String toolTipClickable, String toolTipNotClickable) {
		return widgetTitle().addHyperlink(imageResource, name,
				toolTipClickable, toolTipNotClickable);
	}

	@Override
	public ISelection<Object> selection() {
		return selection;
	}

	int convert2TableRow(int row) {
		return rowOffset + row;
	}

	int convert2GridRow(int row) {
		return row - rowOffset;
	}

	@Override
	public IKey<?> addTableClickListener(final IScrollTableClickListener l) {
		ITableClickListener l2 = new ITableClickListener() {

			@Override
			public void onClick(int column, int row, IPoint p) {
				int convert2TableRow = convert2TableRow(row);
				if (convert2TableRow == 0) {
					return;
				}
				int rowIndex = convert2TableRow - 1;
				if (!rows.selected(rowIndex)) {
					rows.selected(rowIndex, true);
					selection.notifySelection(rowIndex,
							rows.identifier(rowIndex));
				}
				l.onClick(rows.identifier(rowIndex), rowIndex);
			}
		};
		KeyAdapter<Object> keyAdapter = new KeyAdapter<Object>();
		listeners.put(l2, keyAdapter);
		return keyAdapter;
	}

	@Override
	public IScrollTableWidget<Object> addTooltip(String tooltip) {
		// TODO SWING-FXL: tooltip doesn't work for BulkTableWidgetImpl
		this.tooltip += (this.tooltip.equals("") ? "" : "\n") + tooltip;
		if (grid != null)
			grid.element().tooltip(tooltip);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> sortListener(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.ISortListener l) {
		sortListener = l;
		return this;
	}

	@Override
	public void onOver(int column, int row) {
		if (row > 0 && !clickListeners(column).isEmpty()) {
			grid.showAsLink(column, row, true);
		}
	}

	@Override
	public void onOut(int column, int row) {
		if (row > 0 && !clickListeners(column).isEmpty()) {
			grid.showAsLink(column, row, false);
		}
	}

	@Override
	public void onClick(int column, int row) {
		if (!clickListeners(column).isEmpty()) {
			for (IScrollTableListener<Object> l : clickListeners(column))
				l.onClick(rows.identifier(convert2TableRow(row)));
		}
	}

	private List<IScrollTableListener<Object>> clickListeners(int column) {
		return columns.get(realColumn(column)).clickListeners;
	}

	@Override
	public IScrollTableWidget<Object> addFilterListener(final IFilterListener l) {
		filterListener = new IFilterListener() {

			@Override
			public void onApply(IFilterConstraints constraints,
					ICallback<Void> cb) {
				ScrollTableWidgetImpl.this.constraints = constraints;
				l.onApply(constraints, cb);
			}
		};
		if (filter != null) {
			filter.addFilterListener(filterListener);
		}
		return this;
	}

	@Override
	public IScrollTableWidget<Object> refresh() {
		if (filter != null)
			filter.apply(DummyCallback.voidInstance());
		else
			visible(true);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> allowColumnSelection(
			boolean allowColumnSelection) {
		this.allowColumnSelection = allowColumnSelection;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> navigationPanel(
			INavigationPanelDecorator dec) {
		if (topPanel != null) {
			int column = viewInc + buttonColumn++;
			dec.decorate(alignEnd(topPanelCell(column, 0), column));
		} else
			navigationDecorator = dec;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> buttonPanel(IButtonPanelDecorator dec) {
		if (topPanel != null) {
			int column = viewInc + buttonColumn;
			IGridCell cell = topPanelCell(column, 0);
			if (navigationDecorator != null)
				cell.width(100);
			dec.decorate(alignEnd(cell, column));
		} else
			buttonDecorator = dec;
		return this;
	}

	private IGridCell alignEnd(IGridCell cell, int column) {
		if (ToolbarImpl.ALLOW_ALIGN_END_FOR_FLOW_PANEL) {
			cell.align().end();

			// TODO align end for Chrome <= 13, sub-optimal design with icons in
			// the middle

		}
		return cell;
	}

	IGridCell topPanelCell(int i, int j) {
		IGridCell cell = topPanelWithBorder().cell(i, j);
		return cell;
	}

	// void editable(int gridRowIndex, boolean editable) {
	// for (ScrollTableColumnImpl c : columns) {
	// if (c.editable) {
	// IBulkTableCell cell = grid.cell(c.index, gridRowIndex);
	// IContainer container = cell.container().clear();
	// if (editable) {
	// container.textField().text("edit");
	// } else {
	// int dataRow = convert2TableRow(gridRowIndex);
	// c.decorate(rows.identifier(dataRow), cell,
	// rows.row(dataRow)[c.index]);
	// }
	// }
	// }
	// }

	@Override
	public co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons<Object> commandButtons() {
		if (commandButtons == null) {
			commandButtons = new CommandButtonsImpl(this);
			buttonDecorator = commandButtons;
		}
		return commandButtons;
	}

	void notifySelection(int selectionIndex, Object selection2) {
		this.selection.notifySelection(selectionIndex, selection2);
	}

	@Override
	public IScrollTableWidget<Object> showDisplayedRange(
			boolean showDisplayedRange) {
		this.showDisplayedRange = showDisplayedRange;
		return this;
	}

	@Override
	public boolean visible() {
		return container().visible();
	}

	@Override
	public IScrollTableWidget<Object> statusPanel(IVerticalPanel bottom) {
		this.bottom = bottom;
		externalStatusPanel = true;
		resetStatusPanel();
		return this;
	}

	private void resetStatusPanel() {
		bottom.clear();
		// bottom.addSpace(2);
		statusPanel = bottom.add().panel().grid().height(32).resize(3, 1);
		statusPanel.spacing(4);
	}

	void visible(IRows<Object> result) {
		columnWidths.notifyColumnSelectionChange();
		if (result != null)
			rows(result);
		visible(true);
	}

	@Override
	public IScrollTableWidget<Object> addViewComboBox(String[] texts,
			String viewComboBoxChoice, IUpdateListener<String> ul) {
		viewComboBoxText = texts;
		this.viewComboBoxChoice = viewComboBoxChoice;
		viewComboBoxUpdateListener = ul;
		// showNoRowsFound = false;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> addScrollListener(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowIndexListener scrollListener) {
		scrollListeners.add(scrollListener);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> rowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> addToContextMenu(boolean addToContextMenu) {
		this.addToContextMenu = addToContextMenu;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> hiddenColumnListener(
			IUpdateListener<List<String>> hiddenColumnListener) {
		this.hiddenColumnListener = hiddenColumnListener;
		return this;
	}

	void sortBy(ScrollTableColumnImpl columnImpl, final boolean update) {
		if (columnImpl.sortable) {
			if (rows.size() <= MAX_CLIENT_SORT_SIZE || sortListener == null) {
				sortColumn = columns.indexOf(columnImpl);
				sortNegator = rows.sort(columnImpl);
				if (sortListener != null)
					sortListener.onSort(columnImpl.name, sortNegator == 1,
							false, new CallbackTemplate<Void>() {
								@Override
								public void onSuccess(Void result) {
									if (update)
										updateAfterSort();
								}
							});
				else {
					updateAfterSort();
				}
			} else {
				if (sortColumn != -1) {
					sortNegator = sortColumn == columnImpl.index ? sortNegator
							* -1 : 1;
				}
				sortColumn = columnImpl.index;
				sortListener.onSort(columnImpl.name, sortNegator == 1, true,
						DummyCallback.voidInstance());
			}
		}
	}

	private void updateAfterSort() {
		drawAll();
	}

	@Override
	public IFilterConstraints constraints() {
		if (filter != null)
			return filter.constraints();
		return null;
	}

	@Override
	public IScrollTableWidget<Object> filterSizeConstraint(
			boolean filterSizeConstraint) {
		this.filterSizeConstraint = filterSizeConstraint;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> showConfiguration(
			boolean showConfiguration) {
		this.showConfiguration = showConfiguration;
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<IScrollTableColumn<Object>> columns() {
		return (List) columns;
	}

	@Override
	public IScrollTableWidget<Object> dragDropListener(
			boolean allowInsertUnder, IDragDropListener l) {
		this.allowInsertUnder = allowInsertUnder;
		this.dragDropListener = l;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> cellUpdateListener(
			ICellUpdateListener cellUpdateListener) {
		updateListener = cellUpdateListener;
		return this;
	}

	@Override
	public void addClickListeners(boolean addClickListeners) {
		this.addClickListeners = addClickListeners;
	}

	@Override
	public List<ScrollTableColumnImpl> columnList() {
		return columns;
	}

	@Override
	public void refreshTable() {
		if (sp != null)
			presetRowIndex = sp.rowIndex();
		refresh();
	}

	@Override
	public IContainer getContainer() {
		return null;
	}

	int preselectedIndex() {
		if (preselectedList == null || preselectedList.isEmpty())
			return -1;
		return rows.find(preselectedList.get(0));
	}

	@Override
	public int width() {
		return contentPanel.width();
	}

	@Override
	public co.fxl.gui.table.scroll.api.IScrollTableWidget.IStateToggleButton addToggleButton() {
		return toggleButton = new StateToggleButton();
	}

	@Override
	public IScrollTableWidget<Object> plainContent(boolean plainContent) {
		this.plainContent = plainContent;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> noEntitiesFoundDecorator(
			co.fxl.gui.table.scroll.api.IScrollTableWidget.INoEntitiesFoundDecorator d) {
		noEntitiesFoundDecorator = d;
		return this;
	}

	ScrollTableColumnImpl sortColumn() {
		if (sortColumn == -1)
			return null;
		return columns.get(sortColumn);
	}

	//
	// @Override
	// public void nextTimeShowPopUp(boolean b) {
	// nextTimeShowPopUp = b;
	// }
	//
	// @Override
	// public boolean nextTimeShowPopUp() {
	// return nextTimeShowPopUp;
	// }

	@Override
	public IScrollTableWidget<Object> subTitle(String subTitle1,
			String subTitle2) {
		if (widgetTitle != null) {
			widgetTitle.addSubTitles(subTitle1, subTitle2);
		}
		this.subTitle1 = subTitle1;
		this.subTitle2 = subTitle2;
		return this;
	}

	@Override
	public IScrollTableWidget<Object> width(int width) {
		this.widthDelta = StatusDisplay.instance().width() - width;
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IScrollTableWidget<Object> orderColumns(
			final Map<String, Integer> columnOrder) {
		if (!columnOrder.isEmpty()) {
			final List<ScrollTableColumnImpl> backup = new LinkedList<ScrollTableColumnImpl>(
					columns);
			Collections.sort(columns, new Comparator<ScrollTableColumnImpl>() {
				@Override
				public int compare(ScrollTableColumnImpl o1,
						ScrollTableColumnImpl o2) {
					Integer i1 = columnOrder.get(o1.name());
					if (i1 == null)
						i1 = backup.indexOf(o1);
					Integer i2 = columnOrder.get(o2.name());
					if (i2 == null)
						i2 = backup.indexOf(o2);
					return i1 - i2;
				}
			});
			columnWidths.columns((List) columns);
		}
		return this;
	}

	@Override
	public void notifyVisible(ScrollTableColumnImpl c) {
		columnWidths.notifyVisible(c);
	}

	@Override
	public IScrollTableWidget<Object> initialAutoComputeWidths(
			boolean autoComputeInitialWidths) {
		columnWidths.notifyColumnSelectionChange();
		return this;
	}

	@Override
	public IScrollTableWidget<Object> noAutoAdjustmentOfColumnWidths() {
		columnWidths = ColumnWidths.newInstance(false);
		return this;
	}

	@Override
	public IScrollTableWidget<Object> grouping(IGrouping grouping) {
		this.grouping = grouping;
		return this;
	}

	@Override
	public IPanel<?> mainPanel() {
		return container();
	}

	@Override
	public IScrollTableWidget<Object> configureListener(IClickListener cl) {
		configureListener = cl;
		return this;
	}

	@Override
	public IClickListener configureListener() {
		return configureListener;
	}
}
