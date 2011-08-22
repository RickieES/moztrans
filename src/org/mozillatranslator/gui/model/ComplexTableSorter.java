/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is MozillaTranslator (Mozilla Localization Tool)
 *
 * The Initial Developer of the Original Code is Henrik Lynggaard Hansen
 *
 * Portions created by Henrik Lynggard Hansen are
 * Copyright (C) Henrik Lynggaard Hansen.
 * All Rights Reserved.
 *
 * Contributor(s):
 * Henrik Lynggaard Hansen (Initial Code)
 *
 */

package org.mozillatranslator.gui.model;

import java.util.*;
import java.util.logging.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

// Imports for picking up mouse events from the JTable.
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.mozillatranslator.datamodel.Phrase;

/**
 * Sorter for complex table, used for chrome view
 * @author Henrik Lynggaard
 */
public class ComplexTableSorter extends ComplexTableMap {
    private static final Logger fLogger = Logger.getLogger(ComplexTableSorter.class.
            getPackage().getName());

    private int indexes[];
    private int sortingColumn;
    private Class columnType;
    private boolean ascending = true;
    private int compares;
    private ArrayList columnHeaders = new ArrayList();
    private boolean sorted = false;

    /**
     * Default constructor
     */
    public ComplexTableSorter() {
        indexes = new int[0]; // for consistency
    }

    /**
     * Constructor with a provided table model
     * @param model a ComplexTableModel, which is a table model tailored for
     *                table views of Phrase items
     */
    public ComplexTableSorter(ComplexTableModel model) {
        this();
        setModel(model);
    }

    /**
     * Sets the ComplexTableModel instance holding the real data
     * @param model the ComplexTableModel instance
     */
    @Override
    public final void setModel(ComplexTableModel model) {
        super.setModel(model);
        reallocateIndexes();

        columnHeaders = new ArrayList();
        Enumeration enumeration = model.getTableColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            columnHeaders.add(((TableColumn) enumeration.nextElement()).getHeaderValue());
        }
    }

    /**
     * Compares the two rows passed as parameters
     * @param row1 the first row to compare
     * @param row2 the second row to compare
     * @return -1, 0 or 1 depending if row1 is the smaller, equal or bigger
     */
    public int compareRowsByColumn(int row1, int row2) {
        TableModel data = model;

        Object o1 = data.getValueAt(row1, sortingColumn);
        Object o2 = data.getValueAt(row2, sortingColumn);

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }

    /*
     * We copy all returned values from the getValue call in case
     * an optimised model is reusing one object to return many
     * values.  The Number subclasses in the JDK are immutable and
     * so will not be used in this way but other subclasses of
     * Number might want to do this to save space and avoid
     * unnecessary heap allocation.
     */

        if (columnType.getSuperclass() == java.lang.Number.class) {
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();

            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else if (columnType == java.util.Date.class) {
            Date d1 = (Date) o1;
            long n1 = d1.getTime();
            Date d2 = (Date) o2;
            long n2 = d2.getTime();

            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            } else {
                return 0;
            }
        } else if (columnType == String.class) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (columnType == Boolean.class) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) { // Define false < true
                return 1;
            } else {
                return -1;
            }
        } else if (columnType == Phrase.class) {
            String s1 = ((Phrase) o1).getName();
            String s2 = ((Phrase) o2).getName();
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            String s1 = o1.toString();
            String s2 = o2.toString();
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int compare(int row1, int row2) {
        compares++;
        int result = compareRowsByColumn(row1, row2);
        return ascending ? result : -result;
    }

    public void reallocateIndexes() {
        int rowCount = model.getRowCount();

        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];

        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        //System.out.println("Sorter: tableChanged");
        reallocateIndexes();

        if (sorted) {
            sort(this);
        }

        super.tableChanged(e);
    }

    public void checkModel() {
        if (indexes.length != model.getRowCount()) {
            fLogger.warning("Sorter not informed of a change in model.");
        }
        if (model.getRowCount() == 0) {
            fLogger.warning("Model is empty.");
        }
    }

    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        this.sortingColumn = column;
        columnType = model.getColumnClass(sortingColumn);
        sort(this);
        super.tableChanged(new TableModelEvent(this));
    }

    public void sort(Object sender) {
        checkModel();
        
        compares = 0;
        // This is where data comes from
        TableModel data = model;

        // Create an array with comparable nodes with indexes to the model
        int rowCount = model.getRowCount();
        ComparableIndexedObject elements[] = new ComparableIndexedObject[rowCount];
        for(int row = 0; row < rowCount; row++) {
            elements[row] = new ComparableIndexedObject(row, (Comparable)data.getValueAt(row, sortingColumn));
        }

        // Do the sort
        Arrays.sort(elements);

        // Copy new index sequence to the indexes array
        for(int row = 0; row < rowCount; row++) {
            indexes[ascending ? row : rowCount - row - 1] = elements[row].getIndex();
        }
        sorted = true;
    }

    public void n2sort() {
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = i + 1; j < rowCount; j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
            fLogger.log(Level.INFO, "{0} sorted", i);
        }
    }

    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

    /* This is an optional short-cut; at each recursive call,
    check to see if the elements in this subset are already
    ordered.  If so, no further comparisons are needed; the
    sub-array can just be copied.  The array must be copied rather
    than assigned otherwise sister calls in the recursion might
    get out of sinc.  When the number of elements is three they
    are partitioned so that the first set, [low, mid), has one
    element and and the second, [mid, high), has two. We skip the
    optimisation when the number of elements is three or less as
    the first compare in the normal merge will produce the same
    sequence of steps. This optimisation seems to be worthwhile
    for partially ordered lists but some analysis is needed to
    find out how the performance drops to Nlog(N) as the initial
    order diminishes - it may drop very quickly.  */

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            System.arraycopy(from, low, to, low, (high-low));
            return;
        }

        // A normal merge.

        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".

    @Override
    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return model.getValueAt(indexes[aRow], aColumn);
    }

    public Object getRow(int aRow) {
        checkModel();
        return model.getRow(indexes[aRow]);
    }


    @Override
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        model.setValueAt(aValue, indexes[aRow], aColumn);
        // JTable.setSelectionRow(aRow);
    }

    // There is no-where else to put this.
    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(JTable table) {
        final ComplexTableSorter sorter = this;
        final JTable tableView = table;

        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (e.getClickCount() == 1 && column != -1) {
                    //System.out.println("Sorting ...");
                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);

                    DefaultTableColumnModel newColumnModel = new DefaultTableColumnModel();

                    ArrayList columns = new ArrayList();
                    Enumeration enumeration = columnModel.getColumns();
                    while (enumeration.hasMoreElements()) {
                        columns.add(enumeration.nextElement());
                    }

                    for (int i = 0; i < columns.size(); i++) {
                        TableColumn col = (TableColumn) columns.get(i);
                        String s = (String) columnHeaders.get(i);
                        if (i == column) {
                            if (ascending) {
                                col.setHeaderValue("^ " + s);
                            } else {
                                col.setHeaderValue("v " + s);
                            }
                        } else {
                            col.setHeaderValue(s);
                        }
                        newColumnModel.addColumn(col);
                    }
                    tableView.setColumnModel(newColumnModel);

                    sorter.sortByColumn(column, ascending);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }
}
