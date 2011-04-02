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
package co.fxl.gui.mdt.api;

import java.util.List;

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.api.template.INavigationListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;

public interface IMasterDetailTableWidget<T> {

	String DETAILS = "Details";

	public interface IContent<T> {

		void queryTree(IFilterConstraints constraints, T selection,
				ICallback<ITree<T>> callback);

		void queryList(IFilterConstraints constraints,
				ICallback<IDeletableList<T>> callback);

		String[] getDefaultCreatableTypes();

		String[] getCreatableTypes(T object);
	}

	IMasterDetailTableWidget<T> sidePanel(IVerticalPanel panel);

	IMasterDetailTableWidget<T> title(String title);

	IMasterDetailTableWidget<T> addConfiguration(String configuration);

	String getActiveConfiguration();

	IMDTFilterList<T> filterList();

	IMasterDetailTableWidget<T> content(IContent<T> source);

	IPropertyGroup<T> defaultPropertyGroup();

	IPropertyPage<T> overviewPage();

	IPropertyGroup<T> addPropertyGroup(String name);

	IRelation<T, ?> addRelation(String name);

	IN2MRelation<T, ?> addN2MRelation(String name);

	IMasterDetailTableWidget<T> showDetailViewByDefault();

	IMasterDetailTableWidget<T> visible(boolean visible);

	INavigationLink<T> addNavigationLink(String name);

	IPropertyPage<T> addPropertyPage(String string);

	IMasterDetailTableWidget<T> allowCreate(boolean allowCreate);

	IMasterDetailTableWidget<T> addCreatableType(String type);

	IMasterDetailTableWidget<T> addCreatableType(String type,
			String imageResource);

	IMasterDetailTableWidget<T> hideDetailRoot();

	IMasterDetailTableWidget<T> refresh(ICallback<Boolean> cb);

	List<T> selection();

	IMasterDetailTableWidget<T> selection(List<T> list);

	IMasterDetailTableWidget<T> allowMultiSelection(boolean multiSelection);

	IMasterDetailTableWidget<T> showCommands(boolean showCommands);

	IMasterDetailTableWidget<T> filterable(boolean filterable);

	IMasterDetailTableWidget<T> allowGridView(boolean allowGridView);

	ITreeWidget<T> tree();

	IMasterDetailTableWidget<T> allowCutPaste(boolean allowCutPaste);

	IMasterDetailTableWidget<T> navigationListener(INavigationListener l);
}
