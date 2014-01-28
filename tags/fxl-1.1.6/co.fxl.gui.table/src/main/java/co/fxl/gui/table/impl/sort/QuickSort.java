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
package co.fxl.gui.table.impl.sort;

public class QuickSort implements ISort {

	@Override
	public void sort(IComparableList a) {
		sort(a, 0, a.size() - 1);
	}

	private void sort(IComparableList a, int lo, int hi) {
		if (hi <= lo)
			return;
		int j = partition(a, lo, hi);
		sort(a, lo, j - 1);
		sort(a, j + 1, hi);
	}

	private int partition(IComparableList a, int lo, int hi) {
		int i = lo;
		int j = hi + 1;
		while (true) {
			while (a.compare(++i, lo) < 0)
				if (i == hi)
					break;
			while (a.compare(lo, --j) < 0)
				if (j == lo)
					break;
			if (i >= j)
				break;
			if (i != j) {
				a.swap(i, j);
			}
		}
		if (lo != j) {
			a.swap(lo, j);
		}

		return j;
	}
}
