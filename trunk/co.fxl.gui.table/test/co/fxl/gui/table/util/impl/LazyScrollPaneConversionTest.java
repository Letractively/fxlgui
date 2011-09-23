package co.fxl.gui.table.util.impl;

import org.junit.Test;

public class LazyScrollPaneConversionTest {

	@Test
	public void run() {
		LazyScrollPaneImpl p = new LazyScrollPaneImpl(null);
		int rowIndex = 41;
		p.maxOffset = 1236;
		p.maxRowIndex = 57;
		int y = p.convertRowIndex2ScrollOffset(rowIndex);
		int newRowIndex = p.convertScrollOffset2RowIndex(y);
		assert newRowIndex == rowIndex : newRowIndex + "!=" + rowIndex;
	}
}
