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
package co.fxl.gui.api;

class FontsImpl implements IFonts {

	@Override
	public IDialog dialog() {
		return new IDialog() {

			@Override
			public IErrorDialog error() {
				return new IErrorDialog() {

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
			public void active(ILabel label) {
				label.font().color().rgb(0, 87, 141);
			}

			@Override
			public void inactive(ILabel label) {
				label.font().color().gray();
			}

			@Override
			public void highlight(ILabel label) {
				label.font().underline(true);
			}

			@Override
			public void unhighlight(ILabel label) {
				label.font().underline(false);
			}
		};
	}
}
