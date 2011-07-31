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
package org.mozillatranslator.gui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.TreePath;
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.gui.model.ComplexTableModel;
import org.mozillatranslator.gui.model.ComplexTableSorter;
import org.mozillatranslator.gui.model.KeyColumnRenderer;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * Implements an internal frame with a table inside (without a tree)
 * @author  Henrik Lynggaard Hansen
 * @version 4.0
 */
public class ComplexTableWindow extends JInternalFrame implements
        InternalFrameListener, MozFrame {
    private JTable table;
    private JScrollPane scroll;
    private ComplexTableModel model;
    private ComplexTableSorter sorter;
    private String l10n;
    private List phraseList;
    private MozFrameToolbar toolbar;

    /** Creates new ComplexTableWindow
     * @param title the title of the internal frame
     * @param toModel a list with the data to be presented in the table
     * @param cols the list of column names
     * @param localeName the locale for which the edition will happen
     * @param toolbar a toolbar with buttons to perform some actions on the table
     */
    public ComplexTableWindow(String title, List toModel, List cols,
            String localeName, MozFrameToolbar toolbar) {
        super(title + " (" + toModel.size() + " strings)");
        
        Font f = new Font(Kernel.settings.getString(Settings.FONT_TABLEVIEW_NAME),
                          Kernel.settings.getInteger(Settings.FONT_TABLEVIEW_STYLE),
                          Kernel.settings.getInteger(Settings.FONT_TABLEVIEW_SIZE));

        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        addInternalFrameListener(this);

        model = new ComplexTableModel(toModel, cols, localeName);
        sorter = new ComplexTableSorter(model);
        table = new JTable(sorter, model.getTableColumnModel());
        
        model.setJTableReference(table);
        sorter.addMouseListenerToHeaderInTable(table);
        
        table.setDefaultRenderer(Phrase.class, new KeyColumnRenderer());
        table.setFont(f);

        scroll = new JScrollPane(table);
        l10n = localeName;
        phraseList = toModel;
        
        this.toolbar = toolbar;
        if (this.toolbar == null) {
            this.toolbar = new SearchWindowToolbar();
        }
        this.toolbar.init(this);
        
        getContentPane().add(this.toolbar.getToolbar(), java.awt.BorderLayout.NORTH);
        getContentPane().add(scroll, "Center");
        pack();
        Kernel.mainWindow.addWindow(this);
        setVisible(true);
    }

    @Override
    public Phrase getSelectedPhrase() {
        Phrase result = null;
        int rowIndex;
        rowIndex = table.getSelectedRow();
        if (rowIndex > -1) {
            result = (Phrase) sorter.getRow(rowIndex);
        }
        return result;
    }

    @Override
    public ArrayList getSelectedPhrases() {
        ArrayList internalPhraseList = null;
        int[] idxList;

        idxList = table.getSelectedRows();
        if (idxList.length > 0) {
            internalPhraseList = new ArrayList();
            for(int idx = 0; idx < idxList.length; idx++) {
                internalPhraseList.add((Phrase) sorter.getRow(idxList[idx]));
            }
        }
        return internalPhraseList;
    }

    @Override
    public JTable getTable() {
        return table;
    }

    @Override
    public boolean hasTreeStructure() {
        return false;
    }

    @Override
    public TreePath getTreeSelection() throws HasNoTreeException {
        throw new HasNoTreeException();
    }

    @Override
    public String getLocalization() {
        return l10n;
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        List fileList = new ArrayList();

        if (toolbar != null) {
            toolbar.destroy();
        }

        Iterator modelIterator = phraseList.iterator();
        while (modelIterator.hasNext()) {
            Phrase curPhrase = (Phrase) modelIterator.next();
            GenericFile mfile = (GenericFile) curPhrase.getParent();
            if (mfile != null) {
                if (!fileList.contains(mfile)) {
                    mfile.decreaseReferenceCount();
                    fileList.add(mfile);
                }
            }
        }
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
    }

    @Override
    public void internalFrameDeiconified(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
    }

    @Override
    public void internalFrameIconified(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
    }

    @Override
    public void internalFrameOpened(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
    }

    @Override
    public int getMaxIndex() {
        return table.getRowCount();
    }

    @Override
    public int getSelectionIndex() {
        return table.getSelectedRow();
    }

    @Override
    public Phrase getPhraseByIndex(int index) {
        return (Phrase) sorter.getRow(index);
    }

    @Override
    public void tableRowsChanged(int first, int last) {
        sorter.fireTableRowsUpdated(first, last);
    }

    @Override
    public void tableDataChanged() {
        sorter.fireTableDataChanged();
    }
}
