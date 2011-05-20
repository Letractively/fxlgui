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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;

class StylesImpl implements IStyles {

	@Override
	public IDialog dialog() {
		return new IDialog() {

			@Override
			public IError error() {
				return new IError() {

					@Override
					public IHeader header() {
						return new IHeader() {

							@Override
							public void stacktrace(ILabel label) {
								label.font().weight().italic();
							}
						};
					}
				};
			}

			@Override
			public IButton button() {
				return new IButton() {

					@Override
					public void clickable(ILabel label, boolean clickable) {
						if (clickable) {
							label.font().color().black();
						} else {
							label.font().color().gray();
						}
					}
				};
			}
		};
	}

	@Override
	public void separator(ILabel label) {
		label.font().color().gray();
	}

	@Override
	public IHyperlink hyperlink() {
		return new IHyperlink() {

			@Override
			public void clickable(ILabel label, boolean clickable) {
				if (clickable)
					label.font().color().rgb(0, 87, 141);
				else
					label.font().color().gray();
			}

			@Override
			public void highlight(ILabel label, boolean highlighted) {
				label.font().underline(highlighted);
			}
		};
	}

	@Override
	public IWindow window() {
		return new IWindow() {

			@Override
			public INavigationWindow navigation() {
				return new INavigationWindow() {

					@Override
					public IChoice choice() {
						return new IChoice() {

							@Override
							public void title(ILabel label) {
								label.font().weight().bold();
							}

						};

					}

					@Override
					public void number(ILabel label) {
						label.font().pixel(13);
						label.font().color().gray();
					}
				};
			}

			@Override
			public IHeader header() {
				return new IHeader() {

					@Override
					public ITitle title() {
						return new ITitle() {

							@Override
							public void small(ILabel label) {
								label.font().weight().bold().pixel(12);
								label.font().color().white();
							}

						};
					}
				};
			}

			@Override
			public IViewWindow view() {
				return new IViewWindow() {

					@Override
					public IEntry entry() {
						return new IEntry() {

							@Override
							public IEntry active(IPanel<?> panel, boolean active) {
								if (active) {
									panel.color().rgb(0xD0, 0xE4, 0xF6);
								} else {
									panel.color().remove();
								}
								return this;
							}

							@Override
							public IEntry active(ILabel label, boolean active) {
								label.font().pixel(13);
								if (active) {
									label.font().color().mix().black().gray();
								}
								return this;
							}
						};
					}
				};
			}

			@Override
			public void button(ILabel label) {
				label.font().pixel(12);
			}
		};
	}

	@Override
	public IInput input() {
		return new IInput() {

			@Override
			public IField field() {
				return new IField() {

					@Override
					public IField background(IColored colored) {
						colored.color().rgb(249, 249, 249);
						return this;
					}

					@Override
					public IField border(IBordered bordered) {
						bordered.border().color().rgb(211, 211, 211);
						return this;
					}
				};
			}
		};
	}
}
