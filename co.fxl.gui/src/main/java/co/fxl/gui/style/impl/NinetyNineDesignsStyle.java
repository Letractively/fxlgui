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
package co.fxl.gui.style.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IFontElement.IFont.IWeight;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextElement;
import co.fxl.gui.api.ITextInputElement;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.HyperlinkDecorator;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.Icons;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.NavigationView;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.impl.StylishButton;
import co.fxl.gui.impl.UserPanel.Decorator;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.style.api.IStyle.IUserPanel.IAdminRightGroup.IAdminRight;

class NinetyNineDesignsStyle extends StyleTemplate implements RuntimeConstants {

	private static final int COMMENT_PANEL_INDENT = 6;
	private static final String CLEAR_FILTERS = "Clear Filters";
	private IGridPanel applicationPanel;

	@Override
	public void hyperlink(ILabel label) {
		blue(label);
	}

	private IColor blue(IFontElement element) {
		return blue(element.font());
	}

	private IColor blue(IColored colored) {
		return blue(colored.color());
	}

	private IColor blue(IColor color) {
		return color.rgb(29, 59, 89);
	}

	private class ViewSelection implements IViewSelection {

		private static final int VIEW_SELECTION_HEIGHT = 40;
		private IHorizontalPanel comboBoxPanel = Display.instance()
				.newContainer().panel().horizontal();

		private class View extends ViewTemplate implements IView {

			private IPanel<?> panel;
			private IImage image;
			private ILabel label;
			private ViewType type;
			private IGridCell cell;

			private View(String text, ViewType type, boolean isLast) {
				super(comboBoxPanel.add().panel().horizontal(),
						isDiscardChangesDialog);
				this.type = type;
				int column = views.size();
				cell = grid.cell(column, 0).align().center().valign().center();
				panel = cell.panel().vertical().align().center()
						.height(VIEW_SELECTION_HEIGHT);
				IBorder border = cell.border();
				border.color().gray(218);
				if (!isLast)
					border.style().top().style().bottom().style().left();
				setBorderRounding(isLast, border);
				border.width(1);
				setBorderRounding(isLast, panel.border());
				IHorizontalPanel hp = panel.add().panel().horizontal();
				hp.margin().top((VIEW_SELECTION_HEIGHT - 16) / 2);
				image = hp.add().image();
				label = hp.addSpace(4).add().label().text(text);
				blue(label);
				grid.column(column).expand();
				// decorate(active);
				panel.addClickListener(this);
				image.addClickListener(this);
				label.addClickListener(this);
				addEntry(text, title(), image(true));
			}

			private void setBorderRounding(boolean isLast, IBorder border) {
				if (views.isEmpty())
					border.style().rounded().width(6).right(false);
				else if (isLast)
					border.style().rounded().width(6).left(false);
			}

			@Override
			int space() {
				return 0;
			}

			@Override
			void style(final IComboBox cb) {
				cb.width(120).border().color().white();
				cb.color().white();
				cb.addMouseOverListener(new IMouseOverListener() {
					@Override
					public void onMouseOver() {
						cb.border().color().lightgray();
					}

					@Override
					public void onMouseOut() {
						cb.border().color().white();
					}
				});
				cb.addUpdateListener(new IUpdateListener<String>() {
					@Override
					public void onUpdate(String value) {
						cb.border().color().white();
					}
				});
			}

			@Override
			public IView clickable(boolean clickable) {
				boolean active = !clickable;
				panel.color().remove().gray(active ? 231 : 248).gradient()
						.vertical().gray(active ? 248 : 231);
				IWeight weight = label.font().weight();
				if (active) {
					weight.bold();
				} else {
					weight.plain();
				}
				String res = image(active);
				panel.clickable(!active);
				label.clickable(!active);
				image.clickable(!active);
				image.resource(res).opacity(1.0);
				panel0().visible(!clickable);
				return super.clickable(clickable);
			}

			private String image(boolean active) {
				return "view_" + type.name().toLowerCase() + "_"
						+ (active ? "active" : "inactive") + ".png";
			}

			@Override
			public boolean clickable() {
				return panel.clickable();
			}

			@Override
			public void showActive() {
				for (View view : views) {
					view.clickable(view != View.this);
				}
			}
		}

		private IGridPanel grid;
		private List<View> views = new LinkedList<View>();
		private boolean isDiscardChangesDialog;

		public ViewSelection(IContainer c, boolean isDiscardChangesDialog) {
			this.isDiscardChangesDialog = isDiscardChangesDialog;
			grid = c.panel().grid().height(VIEW_SELECTION_HEIGHT);
			grid.margin().top(ADD_DISTANCE_MDT_FIRST_ROW).bottom(4);
		}

		@Override
		public IView addView(String label, ViewType type, boolean isLast) {
			View view = new View(label, type, isLast);
			views.add(view);
			return view;
		}

		@Override
		public String title() {
			return VIEWS;
		}

		@Override
		public WidgetTitle widgetTitle() {
			return null;
		}

		@Override
		public IHorizontalPanel comboBoxPanel() {
			return comboBoxPanel;
		}

		@Override
		public void visible(boolean b) {
			grid.visible(b);
		}
	}

	public static final String NAME = "Standard";
	private static final int ADD_DISTANCE_MDT_FIRST_ROW = 3;

	@Override
	public ILogin login() {
		return new ILogin() {

			@Override
			public boolean useMoreButton() {
				return true;
			}

			@Override
			public void addSeparator(ILinearPanel<?> panel) {
				panel.addSpace(10);
			}

			@Override
			public void logout(final ILinearPanel<?> panel, String userName,
					final Decorator[] decorators, final IClickListener listener) {
				new UserPanelImageButton(panel, "user_white", userName, true) {
					@Override
					void decorate(IVerticalPanel p) {
						p.spacing(12).margin().top(2);
						boolean addLine = false;
						for (Decorator d : decorators) {
							if (d.isVisible()) {
								d.decorate(p.add().panel().horizontal());
								addLine = true;
							}
						}
						if (addLine)
							p.add().line();
						ImageButton ib = new ImageButton(p.add());
						ib.imageResource("logout.png").text("Logout")
								.addClickListener(listener);
						ib.label().hyperlink().font().weight().bold();
					}
				};
			}
		};
	}

	@Override
	public IApplicationPanel applicationPanel() {
		return new IApplicationPanel() {

			@Override
			public ILinearPanel<?>[] create(IContainer c) {
				applicationPanel = c.panel().grid().spacing(1);
				applicationPanel.visible(!Style.instance().embedded());
				blue(applicationPanel).gradient().vertical().rgb(57, 84, 110);
				applicationPanel.border().style().bottom();
				applicationPanel.column(0).expand();
				ILinearPanel<?> v = applicationPanel.cell(0, 0).valign()
						.center().panel().vertical();
				ILinearPanel<?> end = applicationPanel.cell(1, 0).valign()
						.center().align().end().panel().horizontal().align()
						.end();
				end.padding().top(1);
				return new ILinearPanel<?>[] { v, end };
			}

			@Override
			public int marginLeft() {
				return -4;
			}

			@Override
			public void itemPanel(IHorizontalPanel p) {
			}
		};
	}

	private class UserPanelImageButton implements IClickListener,
			IMouseOverListener, IClickable<UserPanelImageButton> {

		private final IImage more;
		private IPopUp popUp;
		private boolean scheduledClose;
		private ImageButton button;
		private boolean scheduledOpen;
		private int width;

		private UserPanelImageButton(IPanel<?> panel, String image,
				String label, boolean hasPopUp) {
			this(panel, image, label, hasPopUp, 200, 4);
		}

		private UserPanelImageButton(IPanel<?> panel, String image,
				String label, boolean hasPopUp, int w, int s) {
			button = new ImageButton(panel.add(), s);
			button.imageResource(image + ".png");
			button.text(label);
			button.label().font().pixel(fontSize()).weight().bold().color()
					.white();
			new HyperlinkMouseOverListener(button.label());
			more = panel.add().image().resource("more_white_10x16.png");
			if (mobile()) {
				button.label().remove();
				more.remove();
			}
			more.margin().right(4);
			if (hasPopUp) {
				button.addClickListener(this);
				more.addClickListener(this);
				button.addMouseOverListener(this);
				more.addMouseOverListener(this);
			} else
				more.visible(false);
			width = w;
		}

		void decorate(IVerticalPanel panel) {
			throw new UnsupportedOperationException();
		}

		public IClickable<?> tooltip(String string) {
			button.tooltip(string);
			more.tooltip(string);
			return this;
		}

		@Override
		public void onMouseOver() {
			scheduledClose = false;
			if (!scheduledOpen) {
				scheduledOpen = true;
				Display.instance().invokeLater(new Runnable() {
					@Override
					public void run() {
						if (scheduledOpen) {
							scheduledOpen = false;
							showPopUp();
						}
					}
				}, 200);
			}
		}

		@Override
		public void onMouseOut() {
			scheduledOpen = false;
			scheduleClose();
		}

		private void scheduleClose() {
			// if (!openedByMouseOver)
			// return;
			if (scheduledClose)
				return;
			scheduledClose = true;
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					if (scheduledClose && popUp != null && !SWING) {
						popUp.visible(false);
					}
				}
			}, 500);
		}

		@Override
		public void onClick() {
			scheduledClose = false;
			showPopUp();
		}

		private void showPopUp() {
			if (popUp != null)
				return;
			popUp = PopUp.showPopUp(true).autoHide(true);
			// if (!Env.is(Env.SAFARI))
			popUp.width(width);
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						final Runnable runnable = new Runnable() {
							@Override
							public void run() {
								scheduledClose = false;
								popUp = null;
							}
						};
						if (!scheduledClose)
							runnable.run();
						else
							Display.instance().invokeLater(runnable, 300);
					}
				}
			});
			popUp.border().color().gray(190);
			IFocusPanel focus = popUp.container().panel().focus();
			// if (SAFARI)
			focus.width(width);
			focus.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					scheduledClose = false;
				}

				@Override
				public void onMouseOut() {
					scheduleClose();
				}
			});
			IVerticalPanel p = focus.add().panel().vertical();
			// if (SAFARI)
			p.width(width);
			decorate(p);
			if (SWING)
				setOffset(p);
			popUp.visible(true);
			setOffset(p);
		}

		void setOffset(IVerticalPanel p) {
			int x = more.offsetX() - p.width() + 10;
			int y = applicationPanel.offsetY() + applicationPanel.height();
			if (popUp != null)
				popUp.offset(x, y);
		}

		void closePopUp() {
			popUp.visible(false);
		}

		@Override
		public UserPanelImageButton clickable(boolean clickable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean clickable() {
			throw new UnsupportedOperationException();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<UserPanelImageButton> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			button.addClickListener(clickListener);
			// more.addClickListener(clickListener);
			return null;
		}

	}

	@Override
	public IUserPanel userPanel() {
		return new IUserPanel() {

			@Override
			public void background(IHorizontalPanel panel) {
				panel.margin().right(10);
			}

			@Override
			public IClickable<?> profileButton(IPanel<?> panel) {
				ImageButton ib = new ImageButton(panel.add());
				ib.label().hyperlink();
				return ib.imageResource("user.png").text("My Profile");
			}

			@Override
			public void helpButton(IPanel<?> panel, final String pn,
					final String supportPage, final String overviewPage,
					final IClickListener cl) {
				new UserPanelImageButton(panel, "help", "Help", true, 200, 4) {
					@Override
					void decorate(IVerticalPanel p) {
						String productName = pn;
						p.padding(4).spacing(8);
						header(p.add().label().text(productName.toUpperCase()));
						addButton(p, "support.png", "Support",
								new IClickListener() {
									@Override
									public void onClick() {
										Display.instance().showWebsite()
												.uRI("http://" + supportPage);
									}
								});
						addButton(p, "html.png", "Homepage",
								new IClickListener() {
									@Override
									public void onClick() {
										Display.instance().showWebsite()
												.uRI("http://" + overviewPage);
									}
								});
						p.add().line().color().lightgray();
						addButton(p, "info.png", "About", cl);
					}

					private void addButton(IVerticalPanel p, String image,
							String label, final IClickListener cl) {
						ImageButton text = new ImageButton(p.add());
						text.imageResource(image);
						text.addClickListener(new IClickListener() {
							@Override
							public void onClick() {
								closePopUp();
								cl.onClick();
							}
						});
						text.label().html(label).hyperlink();
					}
				};
			}

			@Override
			public IClickable<?> enterAdminButton(IPanel<?> panel,
					final List<IAdminRightGroup> rights) {
				return new UserPanelImageButton(panel, "settings",
						"Administration", !IE_QUIRKS) {
					@Override
					void decorate(IVerticalPanel p) {
						p.spacing().left(12).top(12).right(12).bottom(12)
								.inner(4);
						// p.margin().top(2);
						boolean first = true;
						for (IAdminRightGroup g : rights) {
							if (!first) {
								p.addSpace(4);
								p.add().line().color().lightgray();
								p.addSpace(4);
							}
							ILabel l = p.add().label()
									.text(g.label().toUpperCase());
							header(l);
							for (final IAdminRight right : g.rights()) {
								ImageButton text = new ImageButton(p.add());
								text.imageResource(right.image()).text(
										right.label());
								text.addClickListener(new LazyClickListener() {
									@Override
									protected void onAllowedClick() {
										closePopUp();
										right.onClick();
									}
								});
								new HyperlinkMouseOverListener(text.label());
							}
							first = false;
						}
					}
				}.tooltip("Click on Administration to enter admin area");
			}

			@Override
			public IClickable<?> exitAdminButton(IPanel<?> panel) {
				panel.add().image().resource("settings.png");
				ILabel l = panel.add().label().text("Administration");
				l.font().weight().bold();
				l.font().pixel(fontSize()).color().lightgray();
				ILabel text = panel.add().label().text("Back to Application");
				text.font().pixel(fontSize()).weight().bold().color().white();
				new HyperlinkMouseOverListener(text);
				return text;
			}

			@Override
			public IClickable<?> traceButton(IContainer c) {
				ImageButton ib = new ImageButton(c).imageResource("trace.png");
				ib.text("Show Trace");
				ib.label().hyperlink();
				return ib;
			}

			private void header(ILabel l) {
				l.font().weight().bold().pixel(10).color().gray();
			}
		};
	}

	@Override
	public int fontSize() {
		return 12;
	}

	@Override
	public String fontFamily() {
		return "'Open Sans', Arial, Helvetica, sans-serif";
	}

	@Override
	public INavigation navigation() {
		return new INavigation() {

			@Override
			public void morePopUp(IPopUp popUp) {
			}

			@Override
			public void init(IHorizontalPanel buttonPanel, ILabel button) {
			}

			@Override
			public void moreItem(ILabel label, boolean isEntityLink) {
				color(label.font());
				if (isEntityLink)
					label.font().pixel(14);
			}

			private void color(IColored colored) {
				colored.color().rgb(28, 59, 89);
			}

			@Override
			public boolean isActiveMoreDark() {
				return true;
			}

			@Override
			public void active(IHorizontalPanel buttonPanel, ILabel button) {
				color(buttonPanel);
				button.font().color().white();
				buttonPanel.border().remove().width(0).style().rounded()
						.width(6);
			}

			@Override
			public void inactive(IHorizontalPanel buttonPanel, ILabel button) {
				buttonPanel.color().remove();
				moreItem(button, false);
				buttonPanel.border().remove().width(0).style().rounded()
						.width(6);
			}

			@Override
			public void activeMore(IHorizontalPanel buttonPanel, ILabel button,
					IImage refresh) {
				color(buttonPanel);
				button.font().color().white();
				buttonPanel.border().style().rounded().width(6).bottom(false);
				refresh.resource("more_white.png");
			}

			@Override
			public String activeRefreshImage() {
				return "loading_white.gif";
			}

			@Override
			public String inactiveRefreshImage() {
				return "loading_black.gif";
			}

			@Override
			public void background(IColored colored) {

				// TODO ...

			}

			@Override
			public boolean hasSegmentedBorder() {
				return false;
			}

			@Override
			public void group(ILabel header) {
				header.font().color().gray();
			}

			@Override
			public void backgroundRegisters(IPanel<?> panel) {
				if (!Style.instance().mobile())
					panel.margin().top(20).bottom(20);
			}

			@Override
			public void backgroundCards(IPanel<?> history) {
				if (embedded())
					return;
				IBorder b = history.border();
				b.style().top();
				b.color().lightgray();
			}

			@Override
			public int offsetX() {
				return 273;
			}
		};
	}

	@Override
	public ILogoPanel logoPanel() {
		return new ILogoPanel() {

			@Override
			public String resource() {
				return null;
			}

			@Override
			public void background(IPanel<?> panel) {
				panel.padding().top(4).bottom(4);
				panel.color().rgb(253, 254, 254).gradient()
						.fallback(242, 246, 249).vertical().rgb(247, 250, 251);
			}

			@Override
			public int marginLeft() {
				return 6;
			}
		};
	}

	@Override
	public IViewSelection createSelection(IContainer c,
			boolean isDiscardChangesDialog) {
		return new ViewSelection(c, isDiscardChangesDialog);
	}

	@Override
	public void background(IPanel<?> panel) {
	}

	@Override
	public void display(IDisplay display) {
		display.color().white();
	}

	@Override
	public IWindow window() {
		return new IWindow() {

			@Override
			public int bulkUpdateDecrement() {
				return 48;
			}

			@Override
			public IContainer logPanel(IPopUp popUp) {
				return popUp.container().panel().vertical()
						.spacing(logPanelSpacing()).add();
			}

			@Override
			public boolean editPageHasBorder() {
				return true;
			}

			@Override
			public int logPanelSpacing() {
				return 10;
			}

			@Override
			public void title(ILabel label, boolean sideWidget) {
				blue(label);
				label.font().weight().bold();
				if (sideWidget)
					label.font().pixel(14);
			}

			@Override
			public void scrollPane(IScrollPane scrollPane) {
			}

			@Override
			public void addImageToStylishButton(StylishButton button, int blue) {
				if (blue > 0)
					button.image("save_blue.png");
			}

			@Override
			public void stylishButton(StylishButton button, boolean green,
					int blue, boolean clickable) {
				IColor newColor = button.buttonPanel().color().remove();
				if (clickable) {
					addBorder(button, 120);
					if (green) {
						newButton(0, button.buttonPanel().color().remove());
					} else if (blue > 0) {
						int inc = (blue - 1) * 14;
						button.buttonPanel().color().remove()
								.rgb(79 + inc, 122 + inc, 201 + inc).gradient()
								.vertical().rgb(63 + inc, 106 + inc, 188 + inc);
					} else {
						button.buttonPanel().color().remove().gray(111)
								.gradient().vertical().gray(63);
					}
				} else {
					addBorder(button, 160);
					newColor.gray(140);
				}
			}

			@Override
			public void prepareStylishButton(StylishButton stylishButton) {
				stylishButton.buttonPanel().margin(-1);
			}

			private void addBorder(StylishButton button, int gray) {
				button.buttonPanel().border().color().gray(gray);
			}

			@Override
			public void moreButtonActive(CommandLink more) {
				border(more).gray(113);
				blue(more.background());
				more.image().resource("more_small_white.png");
				more.label().font().color().white();
			}

			private IColor border(CommandLink more) {
				more.border().remove().style().rounded().bottom(false);
				return more.border().width(1).color();
			}

			@Override
			public void moreButton(CommandLink more) {
				border(more).white();
				more.label().font().color().black();
				more.image("more_small_black.png");
			}

			@Override
			public void listItemPanel(IVerticalPanel p) {
				IBorder border = p.border();
				border.color().rgb(222, 227, 213);
				border.style().top();
			}

			@Override
			public String editImage() {
				return "edit_gray.png";
			}

			@Override
			public int headerSpacingLeft(boolean sideWidget) {
				return sideWidget ? 10 : 0;
			}

			@Override
			public int heightFooter() {
				return 40;
			}

			@Override
			public void buttonFooter(final CommandLink cl) {
				if (cl.label().text().equals(CLEAR_FILTERS)) {
					// cl.image().remove();
					blue(cl.label());
					cl.label().font().weight().bold();// .underline(true);
					// cl.label().addMouseOverListener(new IMouseOverListener()
					// {
					//
					// @Override
					// public void onMouseOver() {
					// }
					//
					// @Override
					// public void onMouseOut() {
					// cl.label().font().underline(true);
					// }
					// });
					return;
				}
				button(cl);
				cl.iPanel().padding(4);
				// cl.iPanel().margin().top(-3).bottom(-3);
				IBorder border = cl.border();
				border.style().rounded();
				border.width(1).color().lightgray();
			}

			@Override
			public String moreImage() {
				return "more_black.png";
			}

			@Override
			public boolean useUpperCase(boolean sideWidget) {
				return false;
			}

			@Override
			public int[] saveButton(CommandLink commandLink, int index) {
				int inc = index * 10;
				IBorder border = commandLink.border();
				IColor background = commandLink.background();
				saveButton(inc, border, background);
				white(commandLink);
				return new int[] { 19 + inc, 161 + inc, 61 + inc };
			}

			private void saveButton(int inc, IBorder border, IColor background) {
				border.color().gray(120);
				saveButton(inc, background);
			}

			void saveButton(int inc, IColor background) {
				background
						.rgb(colorRange(79 + inc), colorRange(122 + inc),
								colorRange(201 + inc))
						.gradient()
						.vertical()
						.rgb(colorRange(63 + inc), colorRange(106 + inc),
								colorRange(188 + inc));
			}

			private int colorRange(int i) {
				return Math.min(i, 255);
			}

			@Override
			public int[] newButton(CommandLink commandLink, int index) {
				int inc = index * 10;
				IBorder border = commandLink.border();
				IColor background = commandLink.background();
				newButton(inc, border, background);
				white(commandLink);
				return new int[] { 19 + inc, 161 + inc, 61 + inc };
			}

			private void newButton(int inc, IBorder border, IColor background) {
				border.color().rgb(14 + inc, 151 + inc, 35 + inc);
				newButton(inc, background);
			}

			void newButton(int inc, IColor background) {
				background.rgb(40 + inc, 194 + inc, 64 + inc).gradient()
						.vertical().rgb(25 + inc, 173 + inc, 48 + inc);
			}

			private void white(CommandLink commandLink) {
				commandLink.label().font().color().white();
			}

			@Override
			public int[] showButton(CommandLink commandLink) {
				commandLink.border().color().rgb(147, 131, 24);
				commandLink.background().rgb(217, 189, 54).gradient()
						.vertical().rgb(179, 162, 29);
				white(commandLink);
				return new int[] { 203, 175, 41 };
			}

			@Override
			public void button(CommandLink commandLink) {
				commandLink.border().color().gray(195);
				commandLink.background().gray(254).gradient()
						.fallback(238, 238, 238).vertical().gray(222);
				fontSize(commandLink);
				commandLink.panel().padding().left(4);
				// commandLink.iPanel().padding().left(4).right(4);
			}

			private IFont fontSize(CommandLink commandLink) {
				return commandLink.label().font().pixel(13);
			}

			@Override
			public void header(IGridPanel headerPanel, boolean sideWidget) {
				if (sideWidget)
					background(headerPanel);
				else
					headerPanel.color().remove();
				headerPanel.padding().bottom(
						sideWidget ? 0 : 7 + ADD_DISTANCE_MDT_FIRST_ROW);
				if (sideWidget)
					headerPanel.border().remove();
			}

			@Override
			public void background(IGridPanel panel, boolean addBorder,
					boolean plainContent, boolean sideWidget) {
				if (!plainContent && sideWidget) {
					if (addBorder) {
						IBorder border = panel.border();
						border.style().rounded().width(6);
						border.width(1).color().gray(218);// .rgb(172, 197,
															// 213);
					}
					panel.padding().top(7).bottom(3).left(3).right(3);
					background(panel);
				}
			}

			private void background(IGridPanel panel) {
				panel.color().gray(248);
			}

			@Override
			public void footer(IPanel<?> vertical, boolean sideWidget) {
			}

		};
	}

	@Override
	public ITree tree() {
		return new ITree() {

			@Override
			public boolean useColoredButtons() {
				return true;
			}

			@Override
			public void statusPanel(IPanel<?> panel) {
				panel.border().color().rgb(172, 197, 213);
				panel.color().gray(249);
			}

			@Override
			public String backgroundColor() {
				return "rgb(249,249,249)";
			}

			@Override
			public void background(IVerticalPanel vertical) {
				vertical.color().gray(249);
			}
		};
	}

	@Override
	public IMDT mdt() {
		return new IMDT() {

			@Override
			public boolean showQuickSearchOnTop() {
				return true;
			}

			@Override
			public void actionPanel(NavigationView navigationView) {
				navigationView.widgetTitle.background().white();
				IBorder border = navigationView.widgetTitle.border();
				border.remove();
				border.style().rounded().remove();
			}

			@Override
			public void quickSearch(ISuggestField sf) {
				sf.height(28);
				sf.margin().top(4).left(3);
				sf.padding().left(27).right(3);
				if (IE_QUIRKS)
					sf.padding().top(5).bottom(1);
				IBorder border = sf.border();
				border.style().rounded();
				border.width(1).color().lightgray();
				sf.addStyle("quickSearchShadow");
			}
		};
	}

	@Override
	public IPopUpWindow popUp() {
		return new IPopUpWindow() {

			@Override
			public void background(IPopUp p) {
				p.color().white();
				// if (embedded())
				// p.border().style().rounded().width(6);
			}

			@Override
			public void background(IVerticalPanel p) {
				p.color().white();
				// if (embedded()) {
				p.addStyle("embeddedpopup");
				p.border().style().rounded().width(6);
				// }
			}
		};
	}

	@Override
	public IFilterPanel filter() {
		return new FilterPanelTemplate() {

			@Override
			public void removeErrorColor(ITextElement<?> textField) {
				((IColored) textField).color().white();
			}

			@Override
			public String title(boolean isMini) {
				return isMini ? "FILTER:" : "Search Filter";
			}

			@Override
			public void decorate(IGridCell cell) {
				Heights.INSTANCE.decorate(cell);
			}

			@Override
			public String acceptImage(boolean isMiniFilter) {
				return isMiniFilter ? Icons.ACCEPT : "search_white.png";
			}

			@Override
			public boolean containsRefreshButton() {
				return true;
			}

			@Override
			public String acceptTitle() {
				return "Filter results";
			}

			@Override
			public String clearTitle() {
				return CLEAR_FILTERS;
			}

			@Override
			public void decorate(IComboBox input) {
				decorate((ITextElement<?>) input);
			}

			private void decorate(ITextElement<?> input) {
				input.height(Heights.COMBOBOX_HEIGHT);
				inputBorder((IBordered) input);
			}

			@Override
			public void decorate(ITextInputElement<?> input) {
				decorate((ITextElement<?>) input);
			}

		};
	}

	@Override
	public ITable table() {
		return new ITable() {

			@Override
			public int fontSizeHeader() {
				return 12;
			}

			@Override
			public boolean headerCapitals() {
				return false;
			}

			@Override
			public void gridPlainContent(IBordered element) {
				IBorder border = element.border();
				border.color().rgb(172, 197, 213);
				border.style().top().style().bottom();
			}

			@Override
			public boolean isTitleUpperCase() {
				return false;
			}

			@Override
			public boolean useColoredButtons() {
				return true;
			}

			@Override
			public void statusPanel(IGridPanel statusPanel) {
				NinetyNineDesignsStyle.this.statusPanel(statusPanel);
			}

			@Override
			public void selectedColumn(IHorizontalPanel b, boolean visible,
					boolean isFirst, boolean isLast) {
				visible = !visible;
				b.padding().left(6).right(6);
				if (visible)
					b.color().remove().gray(161);
				else
					b.color().remove().gray(248).gradient().vertical()
							.gray(230);
				if (isFirst)
					b.border().style().rounded().right(false);
				else if (isLast)
					b.border().style().rounded().left(false);
				b.border().width(1).color().gray(218);
			}

			@Override
			public void selectedColumn(ILabel l, boolean visible) {
				visible = !visible;
				if (visible)
					l.font().color().white();
				else
					l.font().color().gray(102);
				l.font().weight().bold().pixel(TABLE_SELECTION_PIXEL);
			}

			@Override
			public void background(IVerticalPanel container, boolean border) {
				if (border) {
					IBorder b = container.border();
					b.style().left().style().right();
					b.color().rgb(172, 197, 213);
				}
			}

			@Override
			public int paddingSide() {
				return 5;
			}

			@Override
			public int marginTop() {
				return 0;
			}

			@Override
			public String headerRowStyle(boolean isTreeTable) {
				return isTreeTable ? "headerRowHL99DTreeTable"
						: "headerRowHL99D";
			}

			@Override
			public String contentRowStyle(boolean isTreeTable) {
				return isTreeTable ? "contentRowHL99" : "contentRowHL99D";
			}

			@Override
			public ILabel statusHeader(IPanel<?> p, String text) {
				ILabel l = p.add().label().text(text);
				l.font().pixel(TABLE_SELECTION_PIXEL);// .weight().bold();
				statusHeader(l);
				return l;
			}

			private void statusHeader(ILabel l) {
				l.font().color().gray(97);
			}

			@Override
			public void statusLink(ILabel l, boolean active) {
				if (active) {
					HyperlinkDecorator.styleHyperlinkActive(l);
				} else
					statusHeader(l);
				l.font().underline(active);
			}

			private IFont decorate(ILabel text) {
				return text.font().weight().bold().pixel(TABLE_SELECTION_PIXEL);
			}

			@Override
			public IClickable<?> selectLink(IHorizontalPanel p, final boolean b) {
				final IHorizontalPanel panel = p.add().panel().horizontal();
				panel.padding().top(3).bottom(3);
				if (b)
					panel.margin().left(5);
				else
					panel.margin().right(5);
				final ILabel select = panel.add().label();
				decorate(select.text(b ? "All" : "None"));
				IClickable<Object> c = new IClickable<Object>() {

					@Override
					public Object clickable(boolean clickable) {
						selectedColumn(panel, clickable, b, !b);
						selectedColumn(select, clickable);
						panel.clickable(clickable);
						if (!clickable) {
							panel.color().remove().gray(250);
							select.font().color().gray(190);
						}
						select.clickable(clickable);
						return this;
					}

					@Override
					public boolean clickable() {
						return panel.clickable();
					}

					@Override
					public IKey<Object> addClickListener(
							IClickListener clickListener) {
						panel.addClickListener(clickListener);
						select.addClickListener(clickListener);
						return null;
					}
				};
				if (b)
					c.clickable(true);
				return c;
			}

			@Override
			public void selectAllNoneBackground(IHorizontalPanel p) {
			}

			@Override
			public boolean separateSelectAllNone() {
				return false;
			}
		};
	}

	private void statusPanel(IPanel<?> statusPanel) {
		IBorder border2 = statusPanel.border();
		border2.color().rgb(172, 197, 213);
		border2.style().top().style().bottom();
		statusPanel.color().remove().gray(249);
	}

	@Override
	public IRegisterStyle register() {
		return new IRegisterStyle() {
			@Override
			public void background(IColor color) {
				color.rgb(249, 249, 249);
			}

			@Override
			public void inactive(IColor color, IBorder border, IFont font,
					boolean isClickable, boolean isEmpty) {
				color.remove();
				IFont f = font.underline(false);
				if (!isClickable) {
					f.color().gray();
				} else if (isEmpty) {
					f.color().gray(190);
				} else
					blue(f.color());// .color().black();
			}

			@Override
			public void active(IColor color, IBorder border, IFont font) {
				blue(color);
				border.style().rounded();
				font.underline(false).color().white();// .color().white();
			}

			@Override
			public String loadingWhite() {
				return "loading_black.gif";
			}

			@Override
			public String loadingBlack() {
				return "loading_white.gif";
			}
		};
	}

	private void inputBorder(IBordered input) {
		IBorder border = input.border();
		border.style().rounded();
		border.width(1);
		border.color().gray(211);
	}

	@Override
	public IN2MStyle n2m() {
		return new IN2MStyle() {

			@Override
			public void title(IVerticalPanel p, ILabel l) {
				p.color().gray(248).gradient().vertical().gray(230);
				blue(l);
			}

			@Override
			public void item(IVerticalPanel p, boolean editable) {
				IColor c = p.color().remove();
				if (editable)
					c.gray(252);
				else
					c.gray(244);
			}

		};
	}

	@Override
	public IFormStyle form() {
		return new FormStyleTemplate() {
			@Override
			public void inputField(IBordered input) {
				inputBorder(input);
			}

			@Override
			public void commentField(ITextArea valuePanel) {
				inputField(valuePanel);
				valuePanel.color().remove().gray(253);
				valuePanel.font().color().gray();
			}

			@Override
			public int commentItem(IVerticalPanel vertical, int i) {
				if (i % 2 == 1) {
					IBorder border = vertical.border();
					border.color().gray(237);
					border.style().top().style().bottom();
					vertical.color().gray(247);
					vertical.padding().bottom(4);
				}
				vertical.padding().left(COMMENT_PANEL_INDENT);
				return COMMENT_PANEL_INDENT;
			}

			@Override
			public void commentHeader(ILabel upperLabel) {
				blue(upperLabel);
			}
		};
	}

	@Override
	public IExportHtml exportHtml() {
		return new IExportHtml() {

			@Override
			public int decrementY() {
				return 13;
			}

			@Override
			public int decrementX() {
				return 6;
			}
		};
	}

	@Override
	public void popUp(IBorder border) {
		blue(border);
	}

	@Override
	public IButtonImage button() {
		return new IButtonImage() {

			@Override
			public String run() {
				return "run_white.png";
			}

			@Override
			public String continueRun() {
				return "continue_run_white.png";
			}

			@Override
			public String add() {
				return "add_white.png";
			}

			@Override
			public String unassign() {
				return "unassign_white.png";
			}

		};
	}
}
