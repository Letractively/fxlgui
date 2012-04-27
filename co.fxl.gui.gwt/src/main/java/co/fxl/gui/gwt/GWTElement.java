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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDraggable;
import co.fxl.gui.api.IDraggable.IDragStartListener.IDragStartEvent;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDragMoveListener;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

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
import com.google.gwt.user.client.ui.Widget;

public class GWTElement<T extends Widget, R> implements IElement<R> {

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

		private final IClickListener listener;
		private char targetCode = '\r';
		private int nativeKeyCode = -1;

		private KeyPressHandlerAdapter(IClickListener listener) {
			this.listener = listener;
		}

		@SuppressWarnings("unchecked")
		public R listenOnKeyPress(char c) {
			// DOM.sinkEvents(container.widget.getElement(),
			// com.google.gwt.user.client.Event.ONKEYPRESS);
			targetCode = c;
			return (R) GWTElement.this;
		}

		@SuppressWarnings("unchecked")
		public R listenOnKeyDown(int keyRight) {
			DOM.sinkEvents(container.widget.getElement(),
					com.google.gwt.user.client.Event.ONKEYDOWN);
			nativeKeyCode = keyRight;
			return (R) GWTElement.this;
		}

		@Override
		public void onKeyPress(KeyPressEvent event) {
			if (nativeKeyCode != -1)
				return;
			char charCode = event.getCharCode();
			if (charCode == targetCode) {
				listener.onClick();
			}
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (nativeKeyCode == -1)
				return;
			int charCode = event.getNativeKeyCode();
			if (charCode == nativeKeyCode) {
				listener.onClick();
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
			return listenOnKeyDown(KeyCodes.KEY_UP);
		}

		@Override
		public R down() {
			return listenOnKeyDown(KeyCodes.KEY_DOWN);
		}

		@Override
		public R left() {
			return listenOnKeyDown(KeyCodes.KEY_LEFT);
		}

		@Override
		public R right() {
			return listenOnKeyDown(KeyCodes.KEY_RIGHT);
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

	public GWTContainer<T> container;
	protected HandlerRegistration registration;
	protected HandlerRegistration registration2;
	protected HandlerRegistration registration3;
	protected List<GWTClickHandler<R>> handlers = new LinkedList<GWTClickHandler<R>>();
	private String visibleStyle = null;
	private boolean hasDragListener;
	private HandlerRegistration dummyDragListener;
	public Injector injector;

	public GWTElement() {
	}

	public GWTElement(GWTContainer<T> container) {
		assert container != null : "GWTElement.new";
		this.container = container;
	}

	// IFont defaultFont() {
	// return ((IFontElement) this).font().pixel(12).family().arial();
	// }

	public IBorder border() {
		return new GWTWidgetBorder(container.widget);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R visible(boolean visible) {
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

	@SuppressWarnings("unchecked")
	@Override
	public R width(int width) {
		if (isUndefined())
			return (R) this;
		if (width < 1)
			style().clearWidth();
		else
			container.widget.setWidth(width + "px");
		return (R) this;
	}

	protected boolean isUndefined() {
		return container == null || container.widget == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(int height) {
		if (isUndefined())
			return (R) this;
		if (height < 1)
			style().clearHeight();
		else
			container.widget.setHeight(height + "px");
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
		return container.widget.getOffsetWidth();
	}

	@Override
	public int offsetX() {
		return container.widget.getAbsoluteLeft();
	}

	@Override
	public int offsetY() {
		return container.widget.getAbsoluteTop();
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
						for (GWTClickHandler<R> handler : handlers) {
							handler.onClick(event);
						}
					}
				});
		if (container.widget instanceof HasDoubleClickHandlers)
			registration2 = ((HasDoubleClickHandlers) container.widget)
					.addDoubleClickHandler(new DoubleClickHandler() {
						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							for (GWTClickHandler<R> handler : handlers) {
								handler.onDoubleClick(event);
							}
						}
					});
		if (container.widget instanceof HasKeyPressHandlers)
			registration3 = ((HasKeyPressHandlers) container.widget)
					.addKeyPressHandler(new KeyPressHandler() {

						@Override
						public void onKeyPress(KeyPressEvent event) {
							for (GWTClickHandler<R> handler : handlers) {
								handler.onClick(event);
							}
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

	@SuppressWarnings("unchecked")
	public R focus(final boolean focus) {
		// display().invokeLater(new Runnable() {
		// @Override
		// public void run() {
		((Focusable) container.widget).setFocus(focus);
		// }
		// });
		return (R) this;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public R addFocusListener(final IUpdateListener<Boolean> l) {
		((HasFocus) container.widget).addFocusListener(new FocusListener() {
			@Override
			public void onFocus(Widget sender) {
				l.onUpdate(true);
			}

			@Override
			public void onLostFocus(Widget sender) {
				l.onUpdate(false);
			}
		});
		return (R) this;
	}

	public IKeyRecipient.IKey<R> addKeyListener(final IClickListener listener) {
		KeyPressHandlerAdapter adp = new KeyPressHandlerAdapter(listener);
		((HasKeyPressHandlers) container.widget).addKeyPressHandler(adp);
		return adp;
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

	Style style() {
		return container.widget.getElement().getStyle();
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
		if (!GWTDisplay.isInternetExplorer8())
			container.widget.getElement().getStyle().setOpacity(opacity);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <N> R nativeElement(N nativeElement) {
		injector.inject((Widget) nativeElement);
		return (R) this;
	}

	// @Override
	// public <N> R nativeElement(N nativeElement) {
	// throw new UnsupportedOperationException();
	// }

}