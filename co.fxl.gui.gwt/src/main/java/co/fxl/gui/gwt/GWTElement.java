/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDraggable;
import co.fxl.gui.api.IDraggable.IDragStartListener.IDragStartEvent;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDragMoveListener;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IKeyRecipient.ControlKey;
import co.fxl.gui.api.IKeyRecipient.IKeyListener;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IShell;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.gwt.GWTClickHandler.ClickEventAdp;
import co.fxl.gui.gwt.GWTClickHandler.DoubleClickEventAdp;
import co.fxl.gui.gwt.GWTClickHandler.KeyPressAdp;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.StatusDisplay;
import co.fxl.gui.log.impl.Log;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DragDropEventBase;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTElement<T extends Widget, R> implements IElement<R>,
		RuntimeConstants {

	public interface Injector {

		void inject(Widget nativeElement);

	}

	private final class GWTPoint implements IDragEvent {

		private final DragDropEventBase<?> event;

		private GWTPoint(DragDropEventBase<?> event) {
			this.event = event;
		}

		@Override
		public int offsetX() {
			return event.getNativeEvent().getClientX()
					- container.widget.getAbsoluteLeft();
		}

		@Override
		public int offsetY() {
			return event.getNativeEvent().getClientY()
					- container.widget.getAbsoluteTop();
		}

		@Override
		public String iD() {
			return event.getData("text");
		}

		@Override
		public boolean shift() {
			return event.getNativeEvent().getShiftKey();
		}

		@Override
		public boolean ctrl() {
			return event.getNativeEvent().getCtrlKey();
		}
	}

	private final class KeyPressHandlerAdapter implements KeyPressHandler,
			KeyUpHandler, IKeyRecipient.IKey<R> {

		private IClickListener listener;
		private char targetCode = '\r';
		private int nativeKeyCode = -1;
		private IKeyListener keyListener = null;

		private KeyPressHandlerAdapter(IClickListener listener) {
			this.listener = listener;
		}

		private KeyPressHandlerAdapter(IKeyListener listener) {
			this.keyListener = listener;
			listenOnKeyup();
		}

		@SuppressWarnings("unchecked")
		public R listenOnKeyPress(char c) {
			// DOM.sinkEvents(container.widget.getElement(),
			// com.google.gwt.user.client.Event.ONKEYPRESS);
			targetCode = c;
			return (R) GWTElement.this;
		}

		@SuppressWarnings("unchecked")
		public R listenOnKeyUp(int keyRight) {
			listenOnKeyup();
			nativeKeyCode = keyRight;
			return (R) GWTElement.this;
		}

		protected void listenOnKeyup() {
			DOM.sinkEvents(container.widget.getElement(),
					com.google.gwt.user.client.Event.ONKEYUP);
		}

		@Override
		public void onKeyPress(KeyPressEvent event) {
			if (nativeKeyCode != -1)
				return;
			char charCode = event.getCharCode();
			if (keyListener != null) {
				keyListener.onCharacterKey(charCode);
			} else if (charCode == targetCode) {
				listener.onClick();
			}
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			int charCode = event.getNativeKeyCode();
			if (keyListener != null) {
				keyListener.onControlKey(key(charCode));
				return;
			}
			if (nativeKeyCode == -1)
				return;
			if (charCode == nativeKeyCode) {
				listener.onClick();
			}
		}

		private ControlKey key(int charCode) {
			switch (charCode) {
			case KeyCodes.KEY_UP:
				return ControlKey.UP;
			case KeyCodes.KEY_DOWN:
				return ControlKey.DOWN;
			default:
				return ControlKey.OTHER;
			}
		}

		@Override
		public R enter() {
			return listenOnKeyPress('\r');
		}

		@Override
		public R tab() {
			return listenOnKeyPress('\t');
		}

		@Override
		public R up() {
			return listenOnKeyUp(KeyCodes.KEY_UP);
		}

		@Override
		public R down() {
			return listenOnKeyUp(KeyCodes.KEY_DOWN);
		}

		@Override
		public R backspace() {
			return listenOnKeyUp(KeyCodes.KEY_BACKSPACE);
		}

		@Override
		public R left() {
			return listenOnKeyUp(KeyCodes.KEY_LEFT);
		}

		@Override
		public R right() {
			return listenOnKeyUp(KeyCodes.KEY_RIGHT);
		}

		@Override
		public co.fxl.gui.api.IKeyRecipient.IKey<R> ctrl() {
			throw new UnsupportedOperationException();
		}

		@Override
		public R character(char c) {
			throw new UnsupportedOperationException();
		}
	}

	static final int IEDECREMENTW = IE_STANDARD ? 8 : 0;
	static final int IEDECREMENTH = IE_STANDARD ? 8 : 0;
	private static final boolean LOG_ILLEGAL_SIZES = true;
	public GWTContainer<T> container;
	protected HandlerRegistration registration;
	protected HandlerRegistration registration2;
	protected HandlerRegistration registration3;
	protected List<GWTClickHandler<R>> handlers = new LinkedList<GWTClickHandler<R>>();
	private String visibleStyle = null;
	private boolean hasDragListener;
	private HandlerRegistration dummyDragListener;
	public Injector injector;
	private Style style;
	private boolean focusListenerSet;
	private List<IUpdateListener<Boolean>> focusListeners = new LinkedList<IUpdateListener<Boolean>>();
	private IShell shell;
	private IBorder border;

	public GWTElement() {
	}

	public GWTElement(GWTContainer<T> container) {
		assert container != null : "GWTElement.new";
		this.container = container;
	}

	protected T widget() {
		return container.widget;
	}

	protected void addStyleName(String string) {
		container.widget.addStyleName(string);
	}

	protected Style style() {
		if (style == null) {
			style = container.widget.getElement().getStyle();
		}
		return style;
	}

	// IFont defaultFont() {
	// return ((IFontElement) this).font().pixel(12).family().arial();
	// }

	public IBorder border() {
		if (border != null)
			return border;
		return border = new GWTWidgetBorder(container.widget);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R visible(boolean visible) {
		boolean visible2 = container.widget.isVisible();
		if (visible2 == visible)
			return (R) this;
		if (!visible) {
			if (visible())
				visibleStyle = style().getProperty("display");
			container.widget.setVisible(false);
		} else {
			if (visibleStyle != null)
				style().setProperty("display", visibleStyle);
			else
				container.widget.setVisible(visible);
		}
		return (R) this;
	}

	@Override
	public R width(int width) {
		if (LOG_ILLEGAL_SIZES && width == 0)
			Log.instance().warn(
					"Illegal width 0 set on " + getClass().getName());
		return width(width < 0, width + "px");
	}

	@SuppressWarnings("unchecked")
	@Override
	public R minWidth(int minWidth) {
		if (isUndefined())
			return (R) this;
		if (minWidth < 0)
			style().clearProperty("minWidth");
		else {
			style().setProperty("minWidth", minWidth + "px");
		}
		return (R) this;
	}

	@Override
	public R width(double width) {
		return width(width < 0, percent(width) + "%");
	}

	private String percent(double width) {
		return String.valueOf(width * 100);
	}

	@SuppressWarnings("unchecked")
	R width(boolean isNegative, String widthString) {
		if (isUndefined())
			return (R) this;
		if (isNegative)
			style().clearWidth();
		else {
			container.widget.setWidth(widthString);
		}
		return (R) this;
	}

	protected boolean isUndefined() {
		return container == null || container.widget == null;
	}

	@Override
	public R height(int height) {
		if (LOG_ILLEGAL_SIZES && height == 0)
			Log.instance().warn(
					"Illegal height 0 set on " + getClass().getName());
		return height(height < 0, height + "px");
	}

	@Override
	public R height(double height) {
		return height(height < 0, percent(height) + "%");
	}

	@SuppressWarnings("unchecked")
	private R height(boolean isNegative, String heightString) {
		if (isUndefined())
			return (R) this;
		if (isNegative)
			style().clearHeight();
		else {
			container.widget.setHeight(heightString);
		}
		return (R) this;
	}

	@Override
	public R size(int width, int height) {
		width(width);
		return height(height);
	}

	@Override
	public int height() {
		return container.widget.getOffsetHeight();
	}

	@Override
	public int width() {
		int width = container.widget.getOffsetWidth();
		// if (LOG_ILLEGAL_SIZES && width == 0)
		// Log.instance().warn(
		// "Illegal width 0 encountered on " + getClass().getName());
		return width;
	}

	@Override
	public int offsetX() {
		return container.widget.getAbsoluteLeft();
	}

	@Override
	public int offsetY() {
		int absoluteTop = container.widget.getAbsoluteTop();
		// if (LOG_ILLEGAL_SIZES && absoluteTop == 0)
		// Log.instance().warn(
		// "Potentially illegal offsetY 0 on " + getClass().getName());
		return absoluteTop;
	}

	@Override
	public void remove() {
		container.parent.remove(container.widget);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <N> N nativeElement() {
		return (N) container.widget;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R tooltip(String tooltip) {
		container.widget.setTitle(tooltip);
		return (R) this;
	}

	@Override
	public boolean visible() {
		return container.widget.isAttached() && container.widget.isVisible();
	}

	public IKey<R> addClickListener(IClickListener clickListener) {
		// assert handlers.isEmpty() :
		// "Multiple click listeners are not yet supported";
		GWTClickHandler<R> handler = newGWTClickHandler(clickListener);
		handlers.add(handler);
		toggleClickHandler(true);
		return handler;
	}

	GWTClickHandler<R> newGWTClickHandler(IClickListener clickListener) {
		throw new UnsupportedOperationException();
	}

	private void toggleClickHandler(boolean toggle) {
		style().setCursor(
				toggle ? com.google.gwt.dom.client.Style.Cursor.POINTER
						: com.google.gwt.dom.client.Style.Cursor.DEFAULT);
		if (registration != null) {
			registration.removeHandler();
			registration = null;
		}
		if (registration2 != null) {
			registration2.removeHandler();
			registration2 = null;
		}
		if (registration3 != null) {
			registration3.removeHandler();
			registration3 = null;
		}
		if (toggle) {
			registerClickHandler();
		}
	}

	void registerClickHandler() {
		registration = ((HasClickHandlers) container.widget)
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						fireClickListenersSingleClick(event);
					}
				});
		if (container.widget instanceof HasDoubleClickHandlers)
			registration2 = ((HasDoubleClickHandlers) container.widget)
					.addDoubleClickHandler(new DoubleClickHandler() {
						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							fireClickListenersDoubleClick(event);
						}
					});
		if (container.widget instanceof HasKeyPressHandlers)
			registration3 = ((HasKeyPressHandlers) container.widget)
					.addKeyPressHandler(new KeyPressHandler() {

						@Override
						public void onKeyPress(KeyPressEvent event) {
							fireClickListenersKeyPress(event);
						}
					});
	}

	@SuppressWarnings("unchecked")
	public R clickable(boolean enable) {
		toggleClickHandler(enable);
		return (R) this;
	}

	public boolean clickable() {
		return registration != null;
	}

	@Override
	public IDisplay display() {
		return container.display();
	}

	protected IColor newBackgroundColor() {
		GWTWidgetStyle style = new GWTWidgetStyle("background-color-",
				container.widget);
		return new GWTStyleColor(style) {
			@Override
			void setColor(String color, com.google.gwt.dom.client.Style stylable) {
				stylable.setBackgroundColor(color);
			}
		};
	}

	public R focus(final boolean focus) {
		T widget = container.widget;
		return focus(focus, widget);
	}

	private void notifyFocusListeners(boolean b) {
		for (IUpdateListener<Boolean> l : focusListeners)
			l.onUpdate(b);
	}

	public R addFocusListener(final IUpdateListener<Boolean> l) {
		T widget = container.widget;
		return addFocusListener(l, widget);
	}

	@SuppressWarnings("unchecked")
	protected R focus(final boolean focus, Widget widget) {
		if (widget.isVisible() && widget.isAttached()) {
			Focusable focusable = (Focusable) widget;
			focusable.setFocus(focus);
			notifyFocusListeners(focus);
		}
		return (R) this;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected R addFocusListener(final IUpdateListener<Boolean> l, Widget widget) {
		if (!focusListenerSet) {
			((HasFocus) widget).addFocusListener(new FocusListener() {
				@Override
				public void onFocus(Widget sender) {
					notifyFocusListeners(true);
				}

				@Override
				public void onLostFocus(Widget sender) {
					notifyFocusListeners(false);
				}
			});
		}
		focusListeners.add(l);
		return (R) this;
	}

	public IKeyRecipient.IKey<R> addKeyListener(IClickListener listener) {
		KeyPressHandlerAdapter adp = new KeyPressHandlerAdapter(listener);
		addKeyPressHandler(adp);
		return adp;
	}

	@SuppressWarnings("unchecked")
	public R addKeyListener(IKeyListener listener) {
		KeyPressHandlerAdapter adp = new KeyPressHandlerAdapter(listener);
		addKeyPressHandler(adp);
		return (R) this;
	}

	protected void addKeyPressHandler(KeyPressHandlerAdapter adp) {
		((HasKeyPressHandlers) container.widget).addKeyPressHandler(adp);
		((HasKeyUpHandlers) container.widget).addKeyUpHandler(adp);
	}

	@Override
	public R offset(int x, int y) {
		throw new UnsupportedOperationException();
	}

	public IColor color() {
		return newBackgroundColor();
	}

	public IFont font() {
		return new GWTFont(container.widget);
	}

	@SuppressWarnings("unchecked")
	public R addMouseOverListener(final IMouseOverListener l) {
		((HasMouseOverHandlers) container.widget)
				.addMouseOverHandler(new MouseOverHandler() {
					@Override
					public void onMouseOver(MouseOverEvent event) {
						l.onMouseOver();
					}
				});
		((HasMouseOutHandlers) container.widget)
				.addMouseOutHandler(new MouseOutHandler() {
					@Override
					public void onMouseOut(MouseOutEvent event) {
						l.onMouseOut();
					}
				});
		return (R) this;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public R attach(boolean attach) {
	// if (attach)
	// container.parent.add(container.widget);
	// else
	// container.parent.remove(container.widget);
	// return (R) this;
	// }

	@SuppressWarnings("unchecked")
	public R draggable(boolean draggable) {
		container.widget.getElement().setDraggable(
				draggable ? Element.DRAGGABLE_TRUE : Element.DRAGGABLE_FALSE);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDragStartListener(final IDraggable.IDragStartListener l) {
		draggable(true);
		container.widget.addDomHandler(new DragStartHandler() {
			@Override
			public void onDragStart(final DragStartEvent event) {
				event.setData("text", "dragging");
				l.onDragStart(new IDragStartEvent() {

					@Override
					public int offsetX() {
						return event.getNativeEvent().getClientX()
								- container.widget.getElement()
										.getAbsoluteLeft();
					}

					@Override
					public int offsetY() {
						return event.getNativeEvent().getClientY()
								- container.widget.getElement()
										.getAbsoluteTop();
					}

					@Override
					public IDragStartEvent dragImage(IElement<?> element) {
						GWTElement<?, ?> gwtElement = (GWTElement<?, ?>) element;
						Element domElement = gwtElement.getDOMElement();
						if (domElement == null)
							throw new RuntimeException(
									"drag image element is null");
						event.getDataTransfer().setDragImage(domElement, 0, 0);
						return this;
					}

					@Override
					public IDragStartEvent iD(String iD) {
						event.setData("text", iD);
						return this;
					}
				});
			}
		}, DragStartEvent.getType());
		container.widget.addDomHandler(new DragEndHandler() {

			@Override
			public void onDragEnd(DragEndEvent event) {
				GWTDisplay.instance.resetScrollPanelTop();
				l.onDragEnd();
			}

		}, DragEndEvent.getType());
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDragOverListener(final IDragMoveListener l) {
		hasDragListener = true;
		if (dummyDragListener != null) {
			dummyDragListener.removeHandler();
		}
		container.widget.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				event.preventDefault();
				l.onDragOver(new GWTPoint(event));
			}
		}, DragOverEvent.getType());
		container.widget.addDomHandler(new DragLeaveHandler() {

			@Override
			public void onDragLeave(final DragLeaveEvent event) {
				event.preventDefault();
				l.onDragOut(new GWTPoint(event));
			}
		}, DragLeaveEvent.getType());
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDropListener(final IDropListener l) {
		if (!hasDragListener) {
			dummyDragListener = container.widget.addDomHandler(
					new DragOverHandler() {

						@Override
						public void onDragOver(DragOverEvent event) {
							event.preventDefault();
						}
					}, DragOverEvent.getType());
		}
		container.widget.addDomHandler(new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				event.preventDefault();
				// GWTDisplay.waiting-delta if (GWTDisplay.waiting)
				// return;
				l.onDropOn(new GWTPoint(event));
			}
		}, DropEvent.getType());
		return (R) this;
	}

	public Element getDOMElement() {
		return container.widget.getElement();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R padding(int padding) {
		style().setPadding(padding, Unit.PX);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R margin(int margin) {
		style().setMargin(margin, Unit.PX);
		return (R) this;
	}

	@Override
	public IPadding padding() {
		return new IPadding() {

			@Override
			public IPadding left(int pixel) {
				style().setPaddingLeft(pixel, Unit.PX);
				return this;
			}

			@Override
			public IPadding right(int pixel) {
				style().setPaddingRight(pixel, Unit.PX);
				return this;
			}

			@Override
			public IPadding top(int pixel) {
				style().setPaddingTop(pixel, Unit.PX);
				return this;
			}

			@Override
			public IPadding bottom(int pixel) {
				style().setPaddingBottom(pixel, Unit.PX);
				return this;
			}

		};
	}

	@Override
	public IMargin margin() {
		return new IMargin() {

			@Override
			public IMargin left(int pixel) {
				style().setMarginLeft(pixel, Unit.PX);
				return this;
			}

			@Override
			public IMargin right(int pixel) {
				style().setMarginRight(pixel, Unit.PX);
				return this;
			}

			@Override
			public IMargin top(int pixel) {
				style().setMarginTop(pixel, Unit.PX);
				return this;
			}

			@Override
			public IMargin bottom(int pixel) {
				style().setMarginBottom(pixel, Unit.PX);
				return this;
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public R opacity(double opacity) {
		if (!GWTDisplay.isInternetExplorer8OrBelow)
			container.widget.getElement().getStyle().setOpacity(opacity);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N> R nativeElement(N nativeElement) {
		injector.inject((Widget) nativeElement);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R iD(String iD) {
		container.widget.getElement().setId(iD);
		GWTDisplay.instance.notifyElement(this);
		return (R) this;
	}

	void fireClickListenersDoubleClick(DoubleClickEvent event) {
		DoubleClickEventAdp adp = new DoubleClickEventAdp(event);
		fireClickListenersDoubleClick(adp);
	}

	void fireClickListenersDoubleClick(DoubleClickEventAdp adp) {
		for (GWTClickHandler<R> handler : handlers) {
			handler.onDoubleClick(adp);
		}
	}

	void fireClickListenersKeyPress(KeyPressEvent event) {
		KeyPressAdp adp = new KeyPressAdp(event);
		fireClickListenersKeyPress(adp);
	}

	void fireClickListenersKeyPress(KeyPressAdp adp) {
		for (GWTClickHandler<R> handler : handlers) {
			handler.onClick(adp);
		}
	}

	void fireClickListenersSingleClick(ClickEvent event) {
		ClickEventAdp adp = new ClickEventAdp(event);
		fireClickListenersSingleClick(adp);
	}

	void fireClickListenersSingleClick(ClickEventAdp adp) {
		for (GWTClickHandler<R> handler : handlers) {
			handler.onClick(adp);
		}
	}

	@Override
	public String iD() {
		return container.widget.getElement().getId();
	}

	// void log(DragDropEventBase<?> event) {
	// Log.instance().debug(
	// "event " + event.getClass().getName() + ": "
	// + event.getNativeEvent().getClientX() + " | "
	// + event.getNativeEvent().getClientY());
	// }

	// @Override
	// public <N> R nativeElement(N nativeElement) {
	// throw new UnsupportedOperationException();
	// }

	@Override
	public IShell shell() {
		if (shell == null) {
			shell = find(container.widget);
		}
		return shell;
	}

	private IShell find(Widget widget) {
		if (widget instanceof PopupPanel)
			return new GWTPopUp((PopupPanel) widget);
		else if (widget instanceof RootPanel)
			return StatusDisplay.instance();
		else {
			Widget parent = widget.getParent();
			if (parent == null)
				return StatusDisplay.instance();
			return find(parent);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public R addStyle(String style) {
		container.widget.addStyleName(style);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R removeStyle(String style) {
		container.widget.removeStyleName(style);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICursor<R> cursor() {
		return new GWTCursor<R>((R) this, container.widget);
	}

}