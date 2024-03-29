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
package co.fxl.gui.swing;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IClickable.IKey;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDropTarget.IDragMoveListener;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IKeyRecipient.IKeyListener;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPadding;
import co.fxl.gui.api.IShell;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.StatusDisplay;

class SwingElement<T extends JComponent, R> implements IElement<R>, HasUID {

	protected SwingContainer<T> container;
	private List<ClickListenerMouseAdapter<R>> adapters = new LinkedList<ClickListenerMouseAdapter<R>>();
	private boolean clickListenerAdded = false;
	private Boolean disabled;
	private SwingBorder swingBorder;
	private boolean focusListenerSet;
	private List<IUpdateListener<Boolean>> focusListeners = new LinkedList<IUpdateListener<Boolean>>();
	private IShell shell;

	SwingElement(SwingContainer<T> container) {
		this.container = container;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R visible(boolean visible) {
		if (container.component.isVisible() != visible)
			container.component.setVisible(visible);
		return (R) this;
	}

	@Override
	public int height() {
		return container.component.getPreferredSize().height;
	}

	@Override
	public int width() {
		Container parent = container.component.getParent();
		if (!(container.component instanceof JPanel)
				&& parent instanceof JPanel
				&& ((JPanel) parent).getLayout() instanceof VerticalLayoutManager) {
			return parent.getWidth();
		}
		return container.component.getPreferredSize().width;
	}

	@Override
	public void remove() {
		assert container.component != null : "Component not set on container "
				+ container;
		container.parent.remove(container.component);
		container.parent.getComponent().repaint();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(int height) {
		if (height < 1)
			container.component.setPreferredSize(null);
		else
			container.component
					.setPreferredSize(new Dimension(width(), height));

		// TODO set height & width exclusively, not always together

		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R width(int width) {
		if (width < 1)
			container.component.setPreferredSize(null);
		else
			container.component.setPreferredSize(new Dimension(width,
					container.component.getPreferredSize().height));

		// TODO set height & width exclusively, not always together

		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R size(int width, int height) {
		container.component.setPreferredSize(new Dimension(width, height));
		JComponent parent = container.parent.getComponent();
		if (parent instanceof JPanel && parent.getLayout() == null) {
			container.component.setBounds(container.component.getLocation().x,
					container.component.getLocation().y,
					container.component.getPreferredSize().width,
					container.component.getPreferredSize().height);
		}
		return (R) this;
	}

	public IBorder border() {
		if (swingBorder == null)
			swingBorder = new SwingBorder(this);
		return swingBorder;
	}

	public boolean clickable() {
		return container.component.isEnabled();
	}

	@SuppressWarnings({ "unchecked" })
	public R clickable(boolean clickable) {
		// for (ClickListenerMouseAdapter adapter : adapters) {
		// container.component.removeMouseListener(adapter.adapter);
		// if (clickable) {
		// container.component.addMouseListener(adapter.adapter);
		// }
		// }
		disabled = !clickable;
		container.component.setEnabled(clickable);
		setCursor(clickable);
		return (R) this;
	}

	void setCursor(boolean clickable) {
		container.component
				.setCursor(clickable ? new Cursor(Cursor.HAND_CURSOR)
						: new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@SuppressWarnings("unchecked")
	public IKey<R> addClickListener(IClickListener listener) {
		// assert !clickListenerAdded :
		// "Multiple click listeners are not yet supported";
		if (disabled == null)
			clickable(true);
		else
			setCursor(true);
		// container.component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ClickListenerMouseAdapter<R> adapter = new ClickListenerMouseAdapter<R>(
				(R) this, listener);
		adapters.add(adapter);
		if (!clickListenerAdded) {
			container.component.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					fireClickListeners(e);
				}
			});
			clickListenerAdded = true;
		}
		return adapter;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public R nativeElement() {
		return (R) container.component;
	}

	@Override
	public boolean visible() {
		boolean visible = container.component.isDisplayable()
				&& container.component.isVisible();
		return visible;
	}

	@Override
	public int offsetX() {
		return (int) point().getX();
	}

	@Override
	public int offsetY() {
		return (int) point().getY();
	}

	private Point point() {
		Point pt = SwingUtilities.convertPoint(container.component, new Point(
				0, 0), SwingDisplay.instance.container.component);
		return pt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R tooltip(String tooltip) {
		container.component.setToolTipText(tooltip);
		return (R) this;
	}

	@Override
	public IDisplay display() {
		return container.display();
	}

	@SuppressWarnings("unchecked")
	public final R focus(boolean focus) {
		if (focus) {
			container.component.requestFocus();
		} else {
			container.component.setFocusable(false);
			container.component.setFocusable(true);
		}
		notifyFocusListeners(focus);
		return (R) this;
	}

	private void notifyFocusListeners(boolean b) {
		for (IUpdateListener<Boolean> l : focusListeners)
			l.onUpdate(b);
	}

	public boolean focus() {
		return container.component.hasFocus();
	}

	@SuppressWarnings("unchecked")
	public R addFocusListener(final IUpdateListener<Boolean> l) {
		if (!focusListenerSet) {
			container.component.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent arg0) {
					notifyFocusListeners(true);
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					notifyFocusListeners(false);
				}
			});
		}
		focusListeners.add(l);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addKeyListener(IKeyListener changeListener) {
		// TODO ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public IKeyRecipient.IKey<R> addKeyListener(
			final IClickListener changeListener) {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeListener.onClick();
			}
		});
		return new IKeyRecipient.IKey<R>() {

			@Override
			public R enter() {
				return (R) SwingElement.this;
			}

			@Override
			public R tab() {
				throw new UnsupportedOperationException();
			}

			@Override
			public R up() {
				throw new UnsupportedOperationException();
			}

			@Override
			public R down() {
				throw new UnsupportedOperationException();
			}

			@Override
			public R left() {
				throw new UnsupportedOperationException();
			}

			@Override
			public R right() {
				throw new UnsupportedOperationException();
			}

			@Override
			public co.fxl.gui.api.IKeyRecipient.IKey<R> ctrl() {
				throw new UnsupportedOperationException();
			}

			@Override
			public R character(char c) {
				throw new UnsupportedOperationException();
			}

			@Override
			public R backspace() {
				return (R) this;
			}
		};
	}

	void addActionListener(final ActionListener actionListener) {
		container.component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				char keyChar = arg0.getKeyChar();
				if (keyChar == '\n')
					actionListener.actionPerformed(null);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public R offset(int x, int y) {
		container.component.setLocation(x, y);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addMouseOverListener(final IMouseOverListener l) {
		container.component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				l.onMouseOver();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				l.onMouseOut();
			}
		});
		return (R) this;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public R attach(boolean attach) {
	// if (attach) {
	// container.parent.add(container.component);
	// } else {
	// if (isInCardPanel()) {
	// getCardPanelParent().remove(container.component);
	// } else
	// container.parent.remove(container.component);
	// }
	// return (R) this;
	// }
	//
	// JPanel getCardPanelParent() {
	// if (!(container.parent.getComponent() instanceof JPanel))
	// return null;
	// JPanel p = (JPanel) container.parent.getComponent();
	// if (p.getLayout() instanceof ResizeCardLayout)
	// return p;
	// return null;
	// }
	//
	// boolean isInCardPanel() {
	// return getCardPanelParent() != null;
	// }

	@SuppressWarnings("unchecked")
	public R draggable(boolean draggable) {
		// TODO SWING-FXL: IMPL: ... throw new UnsupportedOperationException();
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDragStartListener(co.fxl.gui.api.IDraggable.IDragStartListener l) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDragOverListener(IDragMoveListener l) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addDropListener(IDropListener l) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R padding(int padding) {
		if (container.component instanceof JPanel) {

			// TODO don't remove old border (e.g. for Top-Button WidgetTitle)

			container.component.setBorder(new EmptyBorder(padding, padding,
					padding, padding));
		}
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R margin(int margin) {
		Insets insets = container.component.getInsets();
		insets.bottom += margin;
		insets.top += margin;
		insets.left += margin;
		insets.right += margin;
		return (R) this;
	}

	@Override
	public IPadding padding() {
		return new IPadding() {

			@Override
			public IPadding left(int pixel) {
				// TODO SWING-FXL: IMPL: ...
				return this;
			}

			@Override
			public IPadding right(int pixel) {
				// TODO SWING-FXL: IMPL: ...
				return this;
			}

			@Override
			public IPadding top(int pixel) {
				// TODO SWING-FXL: IMPL: ...
				return this;
			}

			@Override
			public IPadding bottom(int pixel) {
				// TODO SWING-FXL: IMPL: ...
				return this;
			}

		};
	}

	@Override
	public IMargin margin() {
		return new IMargin() {

			@Override
			public IMargin left(int pixel) {
				container.component.getInsets().left = pixel;
				return this;
			}

			@Override
			public IMargin right(int pixel) {
				container.component.getInsets().right = pixel;
				return this;
			}

			@Override
			public IMargin top(int pixel) {
				container.component.getInsets().top = pixel;
				return this;
			}

			@Override
			public IMargin bottom(int pixel) {
				container.component.getInsets().bottom = pixel;
				return this;
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public R opacity(double opacity) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@Override
	public <N> R nativeElement(N nativeElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUID() {
		return container.getUID();
	}

	@SuppressWarnings("unchecked")
	@Override
	public R width(double width) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R height(double height) {
		// TODO SWING-FXL: IMPL: ...
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R iD(String iD) {
		container.component.setName(iD);
		return (R) this;
	}

	void fireClickListeners(MouseEvent event) {
		if (container.component.isEnabled())
			for (ClickListenerMouseAdapter<R> adapter : adapters) {
				adapter.adapter.mouseReleased(event);
			}
	}

	@Override
	public String iD() {
		return container.component.getName();
	}

	@Override
	public IShell shell() {
		if (shell == null) {
			shell = find(container.parent);
		}
		return shell;
	}

	private IShell find(ComponentParent parent) {
		if (parent.getParent() == null) {
			if (parent instanceof SwingDisplay)
				return StatusDisplay.instance();
			return (IShell) parent;
		} else {
			ComponentParent p = parent.getParent();
			assert p != parent;
			return find(p);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public R addStyle(String style) {
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICursor<R> cursor() {
		return new ICursor<R>() {

			@Override
			public R waiting() {
				return (R) SwingElement.this;
			}

			@Override
			public R hand() {
				return (R) SwingElement.this;
			}

			@Override
			public R pointer() {
				return (R) SwingElement.this;
			}

		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public R removeStyle(String style) {
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public R minWidth(int minWidth) {
		container.component.getMinimumSize().width = minWidth;
		return (R) this;
	}

}