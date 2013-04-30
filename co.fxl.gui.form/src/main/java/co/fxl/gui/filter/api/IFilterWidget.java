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
package co.fxl.gui.filter.api;

import java.util.Arrays;
import java.util.List;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.WidgetTitle;

public interface IFilterWidget {

	public interface IFilterListener {

		void onApply(IFilterConstraints constraints, ICallback<Void> cb);
	}

	public interface IFilter {

		public interface IGlobalValue {

			String value();

			void value(String value);

		}

		IFilter required();

		IFilter name(String name);

		IFieldType type();

		IFilter type(IFieldType type);

		IFilter updateListener(IUpdateListener<String> l);

		IFilter text(String c);

		IFilter directApply(boolean directApply);

		IFilter globalValue(IGlobalValue value);
	}

	public interface IRelationFilter<S, R> extends IFilter {

		public interface IAdapter<S, R> {

			String name(S entity);

			R id(S entity);
		}

		IRelationFilter<S, R> name(String name);

		IRelationFilter<S, R> preset(List<S> entities);

		IRelationFilter<S, R> adapter(IAdapter<S, R> adapter);
	}

	String WILDCARD_TOOLTIP = "Use * and ? as wildcards. Use \\ to escape wildcard tokens.";
	public static final Integer MIN_FILTER_SIZE = 50;
	public static final Integer MAX_FILTER_SIZE = 5000;
	public static final Integer DEFAULT_FILTER_SIZE = MIN_FILTER_SIZE;
	public static final List<Object> DEFAULT_SIZES = Arrays
			.asList(new Object[] { DEFAULT_FILTER_SIZE, 100, 500, 1000,
					MAX_FILTER_SIZE });

	IFilterWidget addConfiguration(String config);

	IFilterWidget addConfigurationListener(IUpdateListener<String> l);

	IFilterWidget showConfiguration(boolean show);

	IFilterWidget setConfiguration(String config);

	IFilter addFilter();

	IRelationFilter<?, ?> addRelationFilter();

	IFilterWidget addSizeFilter();

	IFilterWidget addFilterListener(IFilterListener listener);

	IFilterWidget addLiveFilterListener(IFilterListener listener);

	IFilterWidget holdFilterClicks(boolean holdFilterClicks);

	IFilterWidget addCancelListener(IClickListener cancelListener);

	IFilterWidget apply(ICallback<Void> cb);

	IFilterWidget clear();

	IFilterWidget visible(boolean visible);

	IFilterWidget constraints(IFilterConstraints constraints);

	IFilterConstraints constraints();

	IFilterWidget firstConfiguration(String firstConfiguration);

	IFilterWidget noDiscardChangesDialog(boolean noDiscardChangesDialog);

	WidgetTitle widgetTitle();

	IFilterWidget clearRowIndex();

	IFilterWidget suggestionAdp(ISuggestionAdp adp);

}
