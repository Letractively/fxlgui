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
package co.fxl.gui.rtf.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFocusable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.ISuggestField.ISource;
import co.fxl.gui.api.ITextInputElement;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.PopUp.TransparentPopUp;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.rtf.api.IHTMLArea;
import co.fxl.gui.rtf.api.IHTMLArea.Formatting;
import co.fxl.gui.rtf.api.IHTMLArea.IHTMLAreaButton;
import co.fxl.gui.rtf.api.ITokenButton;
import co.fxl.gui.style.impl.Style;

public class RichTextToolbarImpl implements RuntimeConstants {

	private class TokenButtonImpl extends ToolbarButton implements ITokenButton {

		private String[] values;
		private String title;
		private ILabel label;
		private IHorizontalPanel labelPanel;

		@Override
		public ITokenButton title(String title) {
			this.title = title;
			labelPanel = panel.add().panel().horizontal();
			labelPanel.padding().left(3).right(3).top(1).bottom(2);
			label = labelPanel.add().label().text(title);
			if (IE_STANDARD)
				label.margin().top(4);
			label.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					handleClick();
					update();
					htmlArea.notifyChange();
				}
			});
			labelPanel.border().color().white();
			label.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					if (editable)
						labelPanel.border().color().rgb(219, 180, 104);
				}

				@Override
				public void onMouseOut() {
					update();
				}
			});
			label.tooltip(tooltip());
			return this;
		}

		@Override
		void editable(boolean editable) {
			this.editable = editable;
			labelPanel.visible(editable);
		}

		@Override
		void update() {
			labelPanel.border().color().white();
		}

		@Override
		public ITokenButton values(String... values) {
			this.values = values;
			return this;
		}

		@Override
		String tooltip() {
			return title;
		}

		@Override
		void handleClick() {
			final TransparentPopUp p = PopUp
					.showClosablePopUp(true, null, true);
			p.panel.padding(10);
			int y = labelPanel.offsetY() + labelPanel.height();
			p.popUp.offset(labelPanel.offsetX() - 4, y);
			IVerticalPanel holder = p.panel.add().panel().vertical();
			for (final String v : values)
				holder.add().label().text(v).hyperlink()
						.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								p.popUp.visible(false);
								htmlArea.insertHTML("${" + v + "}");
							}
						});
			p.popUp.modal(false).autoHide(true).visible(true);
			if (y + p.panel.height() > Display.instance().height() - 30) {
				p.panel.clear().add().scrollPane()
						.height(Display.instance().height() - 30 - y)
						.viewPort().element(holder);
			}
			PopUp.autoHideOnResize(p);
		}
	}

	private class PushButton extends ToolbarButton {

		private PushButton(Formatting f) {
			super(f);
		}

		@Override
		void handleClick() {
			htmlArea.apply(f);
		}

	}

	private class HTMLAreaButtonImpl extends InsertButton implements
			IHTMLAreaButton {

		private String title;
		private ISource source;

		private HTMLAreaButtonImpl() {
			super();
		}

		void execute(final String tf) {
			htmlArea.insertImage(tf);
		}

		@Override
		public IHTMLAreaButton title(String title) {
			this.title = title;
			return this;
		}

		@Override
		public IHTMLAreaButton imageResource(String resource) {
			setImage(panel, resource);
			return this;
		}

		@Override
		String tooltip() {
			return title;
		}

		ITextInputElement<?> addTextField(IHorizontalPanel h) {
			return h.add().suggestField().source(source);
		}

		@Override
		public IHTMLAreaButton suggestSource(ISource source) {
			this.source = source;
			return this;
		}
	}

	private class InsertImageButton extends InsertButton {

		private InsertImageButton(Formatting f) {
			super(f);
		}

		void execute(final String tf) {
			htmlArea.insertImage(tf);
		}
	}

	private class CreateLinkButton extends InsertButton {

		private CreateLinkButton(Formatting f) {
			super(f);
		}

		void execute(final String tf) {
			htmlArea.insertHyperlink(tf);
		}
	}

	private abstract class InsertButton extends ToolbarButton {

		private InsertButton() {
			super();
		}

		private InsertButton(Formatting f) {
			super(f);
		}

		abstract void execute(final String tf);

		@Override
		void handleClick() {
			final IPopUp p = PopUp.showClosablePopUp().center().autoHide(true)
					.atLastClick();
			p.border().remove().style().shadow().color().gray();
			IHorizontalPanel h = p.container().panel().horizontal().spacing(4);
			final ITextInputElement<?> tf = addTextField(h);
			tf.width(300);
			((IFocusable<?>) tf).focus(true);
			Heights.INSTANCE.decorate(tf);
			IClickListener clickListener = new IClickListener() {
				@Override
				public void onClick() {
					if (tf.text().trim().isEmpty())
						return;
					p.visible(false);
					execute(tf.text());
				}
			};
			((IKeyRecipient<?>) tf).addKeyListener(clickListener).enter();
			final IImage accept = h.add().image().resource("accept.png")
					.addClickListener(clickListener).mouseLeft()
					.clickable(false);
			h.add().image().resource("cancel.png")
					.addClickListener(new IClickListener() {
						@Override
						public void onClick() {
							p.visible(false);
						}
					}).mouseLeft();
			tf.addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					accept.clickable(value.trim().length() > 0);
				}
			});
			p.visible();
		}

		ITextInputElement<?> addTextField(IHorizontalPanel h) {
			return h.add().textField();
		}

	}

	private abstract class ToolbarElement {

		void updateStatus() {
		}

		abstract void editable(boolean editable);

	}

	private abstract class ToolbarButton extends ToolbarElement {

		IImage image;
		Formatting f;
		boolean editable = true;

		private ToolbarButton() {
		}

		private ToolbarButton(final Formatting f) {
			this.f = f;
			String resource = imageName(f);
			setImage(panel, resource);
		}

		@Override
		void editable(boolean editable) {
			this.editable = editable;
			image.visible(editable);
		}

		void setImage(IToolbar panel, String resource) {
			image = panel.add().image().resource(resource).size(20, 20);
			if (IE_STANDARD)
				image.margin().top(4);
			image.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					handleClick();
					update();
					htmlArea.notifyChange();
				}
			});
			image.border().color().white();
			image.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					if (editable)
						image.border().color().rgb(219, 180, 104);
				}

				@Override
				public void onMouseOut() {
					update();
				}
			});
			image.tooltip(tooltip());
		}

		String tooltip() {
			return format(f.name());
		}

		String imageName(final Formatting f) {
			return f.name().toLowerCase().replaceAll("_", "") + ".gif";
		}

		abstract void handleClick();

		void update() {
			image.border().color().white();
		}

		String format(String n) {
			StringBuilder b = new StringBuilder();
			boolean first = true;
			for (int i = 0; i < n.length(); i++) {
				if (first) {
					b.append(n.charAt(i));
					first = false;
				} else if (n.charAt(i) == '_') {
					b.append(" ");
					first = true;
				} else {
					b.append(Character.toLowerCase(n.charAt(i)));
				}
			}
			return b.toString();
		}
	}

	private class ToggleButton extends ToolbarButton {

		private boolean active;

		private ToggleButton(final Formatting f) {
			super(f);
		}

		@Override
		String imageName(final Formatting f) {
			return f.name().substring(IHTMLArea.TOGGLE_PREFIX.length())
					.toLowerCase()
					+ ".gif";
		}

		@Override
		void handleClick() {
			active = !active;
			htmlArea.toggle(f);
		}

		@Override
		void updateStatus() {
			active = htmlArea.is(f);
			update();
		}

		@Override
		void update() {
			if (active)
				image.border().color().gray();
			else
				super.update();
		}
	}

	private class TagButton extends ToolbarButton {

		private boolean active;
		private String tag;

		private TagButton(String tag) {
			this.tag = tag;
			setImage(panel, "tag_" + tag.toLowerCase() + IMAGE_SUFFIX);
		}

		@Override
		void updateStatus() {
			active = htmlArea.is(tag);
			update();
		}

		@Override
		void update() {
			if (active)
				image.border().color().gray();
			else
				super.update();
			image.tooltip(tooltip());
		}

		@Override
		String tooltip() {
			return (!active ? "Start  " : "End ") + format(tag);
		}

		@Override
		void handleClick() {
			active = !active;
			if (active)
				htmlArea.insertImage(OPEN_IMAGE_PREFIX + tag.toLowerCase()
						+ IMAGE_SUFFIX);
			else
				htmlArea.insertImage(CLOSE_IMAGE_PREFIX + tag.toLowerCase()
						+ IMAGE_SUFFIX);
		}

	}

	private static final String IMAGE_SUFFIX = ".png";
	private static final String CLOSE_IMAGE_PREFIX = "tag_close_";
	private static final String OPEN_IMAGE_PREFIX = "tag_open_";
	private static String[] TAGS = new String[] {};// "Section", "Title" };
	private static final int SPACING = 200;
	private static final int SPACING_TOP = 100;
	private IToolbar panel;
	private List<ToolbarElement> buttons = new LinkedList<ToolbarElement>();
	private RichTextToolbarTextAdp htmlArea;
	private ToolbarButton zoomButton;
	private IClickListener closeListener;
	private IGridPanel grid;

	public RichTextToolbarImpl(IContainer c,
			final RichTextToolbarTextAdp htmlArea) {
		grid = c.panel().grid();
		panel = new ToolbarImpl(grid.cell(0, 0).align().begin()).align()
				.begin().height(32).spacing(2);
		grid.color().gray(240);
		grid.border().style().noBottom().color().gray(211);
		this.htmlArea = htmlArea;
		for (Formatting f : IHTMLArea.Formatting.values()) {
			if (htmlArea.supports(f)) {
				ToolbarElement b;
				if (f.name().startsWith(IHTMLArea.TOGGLE_PREFIX)) {
					b = new ToggleButton(f);
				} else if (f.equals(IHTMLArea.Formatting.INSERT_IMAGE)) {
					b = new InsertImageButton(f);
				} else if (f.equals(IHTMLArea.Formatting.CREATE_LINK)) {
					b = new CreateLinkButton(f);
				} else {
					b = new PushButton(f);
				}
				buttons.add(b);
			}
		}
		for (String tag : TAGS) {
			TagButton b = new TagButton(tag);
			buttons.add(b);
		}
		grid.column(0).expand();
		if (htmlArea instanceof IHTMLArea)
			addZoomButton((IHTMLArea) htmlArea);
	}

	public IHTMLAreaButton addButton() {
		HTMLAreaButtonImpl b = new HTMLAreaButtonImpl();
		buttons.add(b);
		return b;
	}

	private void addZoomButton(final IHTMLArea htmlArea) {
		IToolbar panelRight = new ToolbarImpl(grid.cell(1, 0).align().end())
				.height(28).spacing(2);
		zoomButton = new ToolbarButton() {
			@Override
			void handleClick() {
				if (closeListener != null) {
					closeListener.onClick();
					return;
				}
				final IPopUp p = PopUp.showClosablePopUp().autoHide(true)
						.glass(Style.instance().glass());
				p.border().remove();
				p.border().style().shadow();
				IVerticalPanel spacing2 = p.container().panel().vertical()
						.spacing(10);
				spacing2.border().color().gray();
				final IHTMLArea ha = spacing2.add().widget(IHTMLArea.class);
				copy(htmlArea, ha);
				for (ToolbarElement e : buttons)
					if (e instanceof TokenButtonImpl) {
						TokenButtonImpl t = (TokenButtonImpl) e;
						ha.addTokenButton().title(t.title).values(t.values);
					}
				Heights.INSTANCE.decorate(ha);
				ha.closeListener(new IClickListener() {
					@Override
					public void onClick() {
						p.visible(false);
					}
				});
				p.addVisibleListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						if (!value) {
							copy(ha, htmlArea);
						}
					}
				});
				adjust(p, ha);
				p.visible(true);
				Display.instance().addResizeListener(new IResizeListener() {
					@Override
					public void onResize(int width, int height) {
						adjust(p, ha);
					}
				}).linkLifecycle(p);
			}

			private void copy(final IHTMLArea htmlArea, final IHTMLArea ha) {
				ha.html(htmlArea.html());
				ha.editable(htmlArea.editable());
				// ha.cursorPosition(htmlArea.cursorPosition());
			}

			private void adjust(IPopUp p, IHTMLArea ha) {
				int width = Math.max(100, Display.instance().width() - SPACING);
				int height = Math.max(100, Display.instance().height()
						- SPACING_TOP);
				p.offset(SPACING / 2, SPACING_TOP / 2).size(width, height);
				ha.height(height - 45);
			}

			@Override
			String tooltip() {
				return "Zoom In";
			}
		};
		zoomButton.setImage(panelRight, "zoom_in.png");
	}

	public void updateStatus() {
		for (ToolbarElement e : buttons)
			e.updateStatus();
	}

	public void closeListener(IClickListener l) {
		zoomButton.image.resource("zoom_out.png").size(20, 20)
				.tooltip("Zoom Out");
		closeListener = l;
	}

	public int height() {
		return grid.height();
	}

	public static boolean[] parse(String[] css, String body,
			int htmlCursorPosition, String imagePath) {
		String openPrefix = "<img src=\"" + imagePath + OPEN_IMAGE_PREFIX;
		String closePrefix = "<img src=\"" + imagePath + CLOSE_IMAGE_PREFIX;
		String suffix = IMAGE_SUFFIX + "\">";
		List<String> stack = new LinkedList<String>();
		Map<String, Integer> count = new HashMap<String, Integer>();
		for (int i = 0; i < htmlCursorPosition; i++) {
			if (containsTokenAt(openPrefix, i, body)) {
				int indexOf = body.indexOf(suffix, i);
				if (indexOf == -1)
					break;
				i += openPrefix.length();
				String last = body.substring(i, indexOf);
				stack.add(last);
				Integer integer = count.get(last);
				if (integer == null)
					integer = 0;
				count.put(last, integer + 1);
			} else if (containsTokenAt(closePrefix, i, body)) {
				int indexOf = body.indexOf(suffix, i);
				if (indexOf == -1)
					break;
				i += closePrefix.length();
				String last = body.substring(i, indexOf);
				for (int k = stack.size() - 1; k >= 0; k--) {
					if (stack.get(k).equals(last)) {
						stack.remove(k);
						count.put(last, count.get(last) - 1);
						i += closePrefix.length() - 1;
					}
				}
			}
		}
		boolean[] result = new boolean[css.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = count.containsKey(css[i]) && count.get(css[i]) > 0;
		}
		return result;
	}

	static boolean containsTokenAt(String open, int i, String text) {
		for (int j = 0; j < open.length() && i + j < text.length(); j++) {
			if (text.charAt(i + j) != open.charAt(j))
				return false;
		}
		return true;
	}

	public void editable(boolean editable) {
		for (ToolbarElement e : buttons)
			e.editable(editable);
	}

	public ITokenButton addTokenButton() {
		TokenButtonImpl b = new TokenButtonImpl();
		buttons.add(b);
		return b;
	}

}
