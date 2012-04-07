package co.fxl.gui.impl;

import java.util.HashMap;
import java.util.Map;


public class GridCellContainer<T> extends HashMap<java.lang.Integer, java.util.Map<java.lang.Integer, T>> {
    private static final long serialVersionUID = 5133726159517072973L;

    public T getCell(int columnIndex, int rowIndex) {
        Map<java.lang.Integer, T> row = get(rowIndex);

        if (row == null) {
            return null;
        }

        return row.get(columnIndex);
    }

    public void putCell(int columnIndex, int rowIndex, T gridCell) {
        Map<java.lang.Integer, T> row = get(rowIndex);

        if (row == null) {
            row = new HashMap<java.lang.Integer, T>();
            put(rowIndex, row);
        }

        row.put(columnIndex, gridCell);
    }

    public void removeRow(int rowIndex) {
        Map<java.lang.Integer, T> row = get(rowIndex);

        if (row != null) {
            for (int i = 0; i < (row.size()); i++) {
                handleRemovedCell(row.get(i));
            }

            remove(rowIndex);
        }

        int rows = size();

        for (int r = rowIndex + 1; r < rows; r++) {
            int columns = columns(r);

            for (int c = 0; c < columns; c++) {
                T cell = getCell(c, r);
                moveCellDown(cell, -1);
            }

            row = remove(r);
            put((r - 1), row);
        }
    }

    private int columns(int row) {
        Map<java.lang.Integer, T> map = get(row);

        if (map == null) {
            return 0;
        }

        return map.size();
    }

    public void insertRow(int rowIndex) {
        int rows = size();

        for (int r = rows - 1; r >= rowIndex; r--) {
            int columns = columns(r);

            for (int c = 0; c < columns; c++) {
                T cell = getCell(c, r);
                moveCellDown(cell, 1);
            }

            Map<java.lang.Integer, T> row = remove(r);
            put((r + 1), row);
        }
    }

    protected void moveCellDown(T cell, int i) {
    }

    protected void handleRemovedCell(T t) {
    }

    public void removeCell(int rowIndex, int columnIndex) {
        Map<java.lang.Integer, T> map = get(rowIndex);

        if (map == null) {
            return;
        }

        map.remove(columnIndex);

        if (map.isEmpty()) {
            remove(rowIndex);
        }
    }
}
