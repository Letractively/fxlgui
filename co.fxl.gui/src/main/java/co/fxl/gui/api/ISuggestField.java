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
package co.fxl.gui.api;

import java.util.List;

import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;

public interface ISuggestField extends ITextInputElement<ISuggestField>,
		IKeyRecipient<ISuggestField> {

	public interface ISource {

		public interface ISuggestion {

			String insertText();

			String displayText();
		}

		void query(String prefix, ICallback<List<ISuggestion>> callback);
	}

	ISuggestField source(ISource source);

	ISuggestField addSuggestionListener(IUpdateListener<ISuggestion> selection);

	ISuggestField autoSelect(boolean autoSelect);

	ISuggestField requestOnFocus(boolean requestOnFocus);

	ISuggestField outline(boolean outline);

}
