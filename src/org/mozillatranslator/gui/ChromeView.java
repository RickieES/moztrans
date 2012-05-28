/*
 * ChromeView.java
 *
 * Created on 12 December 2006, 18:37
 *
 *
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
 * Tsahi Asher <tsahi_75@yahoo.com>
 *
 */
package org.mozillatranslator.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.gui.model.ComplexColumn;
import org.mozillatranslator.gui.model.ComplexTableModel;
import org.mozillatranslator.gui.model.ComplexTableSorter;
import org.mozillatranslator.gui.model.KeyColumnRenderer;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * internal frame for the main window's MDI container
 * @author Henrik Lynggaard
 * @version 1.8
 */
/**
 *
 * @author  Tsahi Asher
 */
public class ChromeView extends javax.swing.JInternalFrame implements MozFrame {
    private boolean isExportToDirty = false;
    private String currentLocalization;
    private JTable table;
    private ComplexTableModel model;
    private ComplexTableSorter sorter;
    private List columns;
    private org.mozillatranslator.datamodel.TreeNode ourObject;
    private DefaultMutableTreeNode root;
    private static DefaultMutableTreeNode lastSelected = null;
    private static Phrase selectedPhrase = null;
    private static final Logger fLogger = Logger.getLogger(
                                        ChromeView.class.getPackage().getName());
    private static final String CLICK_CALCULATE = "(click 'Calculate')";
    private static final String NULL_PCT = "(--.-- %)";
    private static final String CONTENT_PANE_TITLE = "Content";
    private static final String CV4 = "Chrome view for ";

    /**
     * Creates new ChromeView
     * @param l locale
     * @param cols list of columns selected to display
     */
    public ChromeView(List cols, String l) {
        this(cols, l, null);
    }

    /**
     * Creates new ChromeView
     * @param l locale
     * @param cols collection of columns to display
     * @param ph the selected phrase to highlight (or null to not hightlight any)
     */
    public ChromeView(List cols, String l, Phrase ph) {
        super(CV4 + l);
        currentLocalization = l;
        columns = cols;
        selectedPhrase = ph;

        // build the tree pane
        root = Kernel.datamodel.getTree(TreeNode.LEVEL_FILE);
        if (lastSelected == null) {
            lastSelected = root;
        }

        initComponents();

        //add toolbar
        MozFrameToolbar tool = new ChromeViewToolbar();
        tool.init(this);
        
        // For setFocusTraversalPolicy below
        final JToolBar tb = tool.getToolbar();
        getContentPane().add(tb, BorderLayout.NORTH);

        // Add to main window
        Kernel.mainWindow.addWindow(this);

        // Adjust the splitter
        try {
            // GUI builder fails to generate this in initComponents()
            setMaximum(true);
        } catch (PropertyVetoException e) {
            fLogger.log(Level.WARNING, "The JInternalFrame with title {0} has "
                        + "vetoed its own maximization", getTitle());
        }

        if (null != ph) {
            Kernel.appLog.log(Level.FINE, "phrase", ph);
            int level = ph.getDepth();
            DefaultMutableTreeNode[] obj = new DefaultMutableTreeNode[level];
            TreeNode parent = ph;
            for (int i = level; i >= TreeNode.LEVEL_PRODUCT; i--) {
                parent = parent.getParent();
                obj[i - 1] = new DefaultMutableTreeNode(parent);
            }
            obj[0] = root;
            TreePath path = new TreePath(obj);
            expandTreeAndSelectNode(tree, true, path);
        } else {
            TreePath lsp = new TreePath(lastSelected.getPath());
            expandTreeAndSelectNode(tree, true, lsp);
        }
    }

    /**
     * If expand is true, expands all nodes in the tree.
     * Otherwise, collapses all nodes in the tree.
     */
    private void expandTreeAndSelectNode(JTree ptree, boolean expand,
            TreePath sparam) {
        javax.swing.tree.TreeNode localRoot = (javax.swing.tree.TreeNode)
                ptree.getModel().getRoot();

        // Traverse tree from root
        expandTreeAndSelectNode(ptree, new TreePath(localRoot), expand, sparam);
    }

    private void expandTreeAndSelectNode(JTree ptree, TreePath parent,
            boolean expand, TreePath sparam) {
        // Traverse children
        javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode)
                parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                javax.swing.tree.TreeNode n = (javax.swing.tree.TreeNode)
                        e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                if (sparam.toString().indexOf(path.toString()) > -1) {
                    ptree.setSelectionPath(path);
                    ptree.scrollPathToVisible(path);
                    Kernel.appLog.log(Level.FINE, "expandTreeAndSelectNode", path);
                }
                expandTreeAndSelectNode(ptree, path, expand, sparam);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (null != sparam.getParentPath() &&
                sparam.getParentPath().toString().indexOf(parent.toString()) > -1) {
            if (expand) {
                ptree.expandPath(parent);
            } else {
                ptree.collapsePath(parent);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        split = new javax.swing.JSplitPane();
        treeScroll = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        fileTabbedPane = new javax.swing.JTabbedPane();
        contentPane = new javax.swing.JScrollPane();
        infoPane = new javax.swing.JScrollPane();
        infoPanel = new javax.swing.JPanel();
        staticInfoPanel = new javax.swing.JPanel();
        productLabel = new javax.swing.JLabel();
        productValueLabel = new javax.swing.JLabel();
        pcLabel = new javax.swing.JLabel();
        pcValueLabel = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        pathValueLabel = new javax.swing.JLabel();
        fileLabel = new javax.swing.JLabel();
        fileValueLabel = new javax.swing.JLabel();
        exportToLabel = new javax.swing.JLabel();
        exportToField = new javax.swing.JTextField();
        dontExportCheckBox = new javax.swing.JCheckBox();
        infoSeparator = new javax.swing.JSeparator();
        statsPanel = new javax.swing.JPanel();
        statsTitleLabel = new javax.swing.JLabel();
        calculateButton = new javax.swing.JButton();
        totalStLabel = new javax.swing.JLabel();
        totalStValueLabel = new javax.swing.JLabel();
        trnsStLabel = new javax.swing.JLabel();
        trnsStValueLabel = new javax.swing.JLabel();
        koStLabel = new javax.swing.JLabel();
        koStValueLabel = new javax.swing.JLabel();
        untrnsStLabel = new javax.swing.JLabel();
        untrnStValueLabel = new javax.swing.JLabel();
        fuzzyStLabel = new javax.swing.JLabel();
        fuzzyStValueLabel = new javax.swing.JLabel();
        trnsStPctLabel = new javax.swing.JLabel();
        koStPctLabel = new javax.swing.JLabel();
        untrnsPctLabel = new javax.swing.JLabel();
        fuzzyPctLabel = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setVisible(true);

        split.setDividerLocation(0.30);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);

        tree.setModel(new DefaultTreeModel(root));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });
        treeScroll.setViewportView(tree);

        split.setLeftComponent(treeScroll);

        //this is not really used. it is removed when any node
        //is selected, and added again iff the user selects a
        //dtd/properties file.
        fileTabbedPane.addTab(CONTENT_PANE_TITLE, contentPane);

        infoPanel.setLayout(new java.awt.GridBagLayout());

        staticInfoPanel.setLayout(new java.awt.GridBagLayout());

        productLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        productLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        productLabel.setText("Product:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(productLabel, gridBagConstraints);

        productValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        productValueLabel.setText("(product name)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(productValueLabel, gridBagConstraints);

        pcLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pcLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        pcLabel.setText("Platform/region:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(pcLabel, gridBagConstraints);

        pcValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        pcValueLabel.setText("(platform/region)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(pcValueLabel, gridBagConstraints);

        pathLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        pathLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        pathLabel.setText("Path:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(pathLabel, gridBagConstraints);

        pathValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        pathValueLabel.setText("(path)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(pathValueLabel, gridBagConstraints);

        fileLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        fileLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        fileLabel.setText("File name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(fileLabel, gridBagConstraints);

        fileValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        fileValueLabel.setText("(name of file)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(fileValueLabel, gridBagConstraints);

        exportToLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        exportToLabel.setText("On Export SCM, export to:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(exportToLabel, gridBagConstraints);

        exportToField.setToolTipText("<html>\n  <p>Use this field to change the path where this component and all<br>\n          of their children (subcomponents and files) will be exported to.</p>\n  <p>The value you write will replace the Path above and will be<br>\n          appended to the SCM export translation directory. It MUST start<br>\n          with a leading \"/\".</p>\n  <p>You can use \"[:locale:]\" to represent your locale code.</p>\n</html>");
        exportToField.setMinimumSize(new java.awt.Dimension(175, 27));
        exportToField.setPreferredSize(new java.awt.Dimension(175, 27));
        exportToField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                exportToFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                exportToFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        staticInfoPanel.add(exportToField, gridBagConstraints);

        dontExportCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dontExportCheckBox.setMnemonic('D');
        dontExportCheckBox.setText("Do not export");
        dontExportCheckBox.setToolTipText("Do not export this file or component");
        dontExportCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dontExportCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dontExportCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        staticInfoPanel.add(dontExportCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        infoPanel.add(staticInfoPanel, gridBagConstraints);

        infoSeparator.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        infoPanel.add(infoSeparator, gridBagConstraints);

        statsPanel.setLayout(new java.awt.GridBagLayout());

        statsTitleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        statsTitleLabel.setText("Translation Statistics");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        statsPanel.add(statsTitleLabel, gridBagConstraints);

        calculateButton.setFont(new java.awt.Font("Dialog", 0, 12));
        calculateButton.setMnemonic('C');
        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        statsPanel.add(calculateButton, gridBagConstraints);

        totalStLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        totalStLabel.setText("Total strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(totalStLabel, gridBagConstraints);

        totalStValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        totalStValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        totalStValueLabel.setText(this.CLICK_CALCULATE);
        totalStValueLabel.setMaximumSize(new java.awt.Dimension(97, 15));
        totalStValueLabel.setMinimumSize(new java.awt.Dimension(97, 15));
        totalStValueLabel.setPreferredSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(totalStValueLabel, gridBagConstraints);

        trnsStLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        trnsStLabel.setText("Translated strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(trnsStLabel, gridBagConstraints);

        trnsStValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        trnsStValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        trnsStValueLabel.setText(this.CLICK_CALCULATE);
        trnsStValueLabel.setMaximumSize(new java.awt.Dimension(97, 15));
        trnsStValueLabel.setMinimumSize(new java.awt.Dimension(97, 15));
        trnsStValueLabel.setPreferredSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(trnsStValueLabel, gridBagConstraints);

        koStLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        koStLabel.setText("Keep Original strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(koStLabel, gridBagConstraints);

        koStValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        koStValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        koStValueLabel.setText(this.CLICK_CALCULATE);
        koStValueLabel.setMaximumSize(new java.awt.Dimension(97, 15));
        koStValueLabel.setPreferredSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(koStValueLabel, gridBagConstraints);

        untrnsStLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        untrnsStLabel.setText("Untranslated strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(untrnsStLabel, gridBagConstraints);

        untrnStValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        untrnStValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        untrnStValueLabel.setText(this.CLICK_CALCULATE);
        untrnStValueLabel.setMaximumSize(new java.awt.Dimension(97, 15));
        untrnStValueLabel.setMinimumSize(new java.awt.Dimension(97, 15));
        untrnStValueLabel.setPreferredSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(untrnStValueLabel, gridBagConstraints);

        fuzzyStLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        fuzzyStLabel.setText("Fuzzy strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(fuzzyStLabel, gridBagConstraints);

        fuzzyStValueLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        fuzzyStValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        fuzzyStValueLabel.setText(this.CLICK_CALCULATE);
        fuzzyStValueLabel.setMaximumSize(new java.awt.Dimension(97, 15));
        fuzzyStValueLabel.setMinimumSize(new java.awt.Dimension(97, 15));
        fuzzyStValueLabel.setPreferredSize(new java.awt.Dimension(97, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(fuzzyStValueLabel, gridBagConstraints);

        trnsStPctLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        trnsStPctLabel.setText(this.NULL_PCT);
        trnsStPctLabel.setMaximumSize(new java.awt.Dimension(66, 15));
        trnsStPctLabel.setMinimumSize(new java.awt.Dimension(66, 15));
        trnsStPctLabel.setPreferredSize(new java.awt.Dimension(66, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(trnsStPctLabel, gridBagConstraints);

        koStPctLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        koStPctLabel.setText(this.NULL_PCT);
        koStPctLabel.setMaximumSize(new java.awt.Dimension(66, 15));
        koStPctLabel.setMinimumSize(new java.awt.Dimension(66, 15));
        koStPctLabel.setPreferredSize(new java.awt.Dimension(66, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(koStPctLabel, gridBagConstraints);

        untrnsPctLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        untrnsPctLabel.setText(this.NULL_PCT);
        untrnsPctLabel.setMaximumSize(new java.awt.Dimension(66, 15));
        untrnsPctLabel.setMinimumSize(new java.awt.Dimension(66, 15));
        untrnsPctLabel.setPreferredSize(new java.awt.Dimension(66, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(untrnsPctLabel, gridBagConstraints);

        fuzzyPctLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        fuzzyPctLabel.setText(this.NULL_PCT);
        fuzzyPctLabel.setMaximumSize(new java.awt.Dimension(66, 15));
        fuzzyPctLabel.setMinimumSize(new java.awt.Dimension(66, 15));
        fuzzyPctLabel.setPreferredSize(new java.awt.Dimension(66, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        statsPanel.add(fuzzyPctLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        infoPanel.add(statsPanel, gridBagConstraints);

        infoPane.setViewportView(infoPanel);

        fileTabbedPane.addTab("Information", infoPane);

        split.setRightComponent(fileTabbedPane);

        getContentPane().add(split, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportToFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_exportToFieldFocusGained
        isExportToDirty = true;
    }//GEN-LAST:event_exportToFieldFocusGained

    private void exportToFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_exportToFieldFocusLost
        if (ourObject instanceof Component) {
            Component ourComponent = (Component) ourObject;

            if (exportToField.getText().trim().length() != 0) {
                ourComponent.setExportedToDir(exportToField.getText());
            } else {
                ourComponent.setExportedToDir(null);
            }
        }

        isExportToDirty = false;
    }//GEN-LAST:event_exportToFieldFocusLost

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        float percent;
        float totalStrings;
        TreePath tp = tree.getSelectionPath();
        DefaultMutableTreeNode chosen;
        MozTreeNode curNode;
        StatisticsTraverse statsTraverse;
        DecimalFormat dfFormatter = new DecimalFormat("(00.00 %)");

        if (tp != null) {
            chosen = (DefaultMutableTreeNode) tp.getLastPathComponent();
            curNode = (MozTreeNode) chosen.getUserObject();

            statsTraverse = new StatisticsTraverse(currentLocalization);
            curNode.traverse(statsTraverse, TreeNode.LEVEL_TRANSLATION);
            statsTraverse.calculateUntranslated();

            totalStValueLabel.setText("" + statsTraverse.getTotalStrings());
            trnsStValueLabel.setText("" + statsTraverse.getTranslatedStrings());
            koStValueLabel.setText("" + statsTraverse.getKeepOrigStrings());
            untrnStValueLabel.setText("" + statsTraverse.getUntranslatedStrings());
            fuzzyStValueLabel.setText("" + statsTraverse.getFuzzyStrings());

            totalStrings = (statsTraverse.getTotalStrings() != 0) ?
                    (float) statsTraverse.getTotalStrings() :
                    (float) 1;
            percent = ((float) statsTraverse.getTranslatedStrings() /
                    (float) totalStrings);
            trnsStPctLabel.setText(dfFormatter.format(percent));
            percent = ((float) statsTraverse.getKeepOrigStrings() /
                    (float) totalStrings);
            koStPctLabel.setText(dfFormatter.format(percent));
            percent = ((float) statsTraverse.getUntranslatedStrings() /
                    (float) totalStrings);
            untrnsPctLabel.setText(dfFormatter.format(percent));
            percent = ((float) statsTraverse.getFuzzyStrings() /
                    (float) totalStrings);
            fuzzyPctLabel.setText(dfFormatter.format(percent));
        }
    }//GEN-LAST:event_calculateButtonActionPerformed

    /**
     * listens to node selection event in the tree
     * @param evt event
     */
  private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeValueChanged
      ComplexTableModel newModel = null;
      GenericFile oldLoadedFile = null;
      TreePath tp;
      DefaultMutableTreeNode chosen;

      if (isExportToDirty) {
          this.exportToFieldFocusLost(null);
      }

      tp = evt.getNewLeadSelectionPath();
      if (tp != null) {
          ourObject = null;
          chosen = (DefaultMutableTreeNode) tp.getLastPathComponent();
          lastSelected = chosen;

          fLogger.log(Level.FINE, "Selected node {0}", chosen.toString());
          fLogger.log(Level.FINE, "Selected node path {0}", tp.toString());

          calculateButton.setEnabled(true);

          try {
              ourObject = (org.mozillatranslator.datamodel.TreeNode)
                      chosen.getUserObject();
          } catch (java.lang.ClassCastException ex) {
              ourObject = null;
              calculateButton.setEnabled(false);
          }

          tree.scrollPathToVisible(tp);

          if (ourObject != null) {
              newModel = new ComplexTableModel(ourObject.getAllChildren(),
                      columns, currentLocalization);
              this.model = newModel;
          }

          if (ourObject instanceof BinaryFile) { // image file
              createPhraseTable();
              table.setRowSelectionInterval(0, 0); //for getSelectedPhrases()
              //put edit image dialog in right pane of splitter
              showFileContent(new EditImagePanel((BinaryFile) ourObject, tree,
                      model));
              setTitle(ChromeView.CV4 + currentLocalization + " (" +
                      ourObject + ")");
          } else if (ourObject instanceof GenericFile) { //any other file
              if (oldLoadedFile != null) {
                  oldLoadedFile.decreaseReferenceCount();
              }
              oldLoadedFile = (GenericFile) ourObject;
              oldLoadedFile.increaseReferenceCount();
              createPhraseTable();
              contentPane.setViewportView(table);
              showFileContent(contentPane);

              split.setDividerLocation(0.30);

              if (selectedPhrase != null) {
                  for (int i = 0; i < newModel.getRowCount(); i++) {
                      Phrase p = newModel.getRow(i);
                      if (selectedPhrase == p) {
                          selectTableRow(i);
                      }
                  }
              }
              setTitle(ChromeView.CV4 + currentLocalization + " (" +
                      ourObject + ")");
          } else { //not a file
              JPanel dummy = new JPanel();
              dummy.setBorder(BorderFactory.createLoweredBevelBorder());
              showFileContent(dummy); //new JPanel());
              setTitle(ChromeView.CV4 + currentLocalization);
          }

          // Reset the info panel data
          String[] parentList = {"", "", "", "", "", "", "", "", "", ""};
          if (ourObject != null) {
              ourObject.fillParentArray(parentList);
          }
          productValueLabel.setText(parentList[TreeNode.LEVEL_PRODUCT]);
          pcValueLabel.setText(parentList[TreeNode.LEVEL_PRODUCTCHILD]);
          pathValueLabel.setText(parentList[TreeNode.LEVEL_COMPONENT]);
          fileValueLabel.setText(parentList[TreeNode.LEVEL_FILE]);

          if (ourObject instanceof GenericFile) {
              dontExportCheckBox.setEnabled(true);
              dontExportCheckBox.setSelected(((GenericFile) ourObject).isDontExport());
          } else {
              dontExportCheckBox.setSelected(false);
              dontExportCheckBox.setEnabled(false);
          }

          if (ourObject instanceof Component) {
              exportToField.setText(((Component) ourObject).getExportedToDir());
              exportToField.setEditable(true);
          } else {
              exportToField.setText("");
              exportToField.setEditable(false);
          }

          totalStValueLabel.setText(CLICK_CALCULATE);
          trnsStValueLabel.setText(CLICK_CALCULATE);
          trnsStPctLabel.setText(NULL_PCT);
          koStValueLabel.setText(CLICK_CALCULATE);
          koStPctLabel.setText(NULL_PCT);
          untrnStValueLabel.setText(CLICK_CALCULATE);
          untrnsPctLabel.setText(NULL_PCT);
          fuzzyStValueLabel.setText(CLICK_CALCULATE);
          fuzzyPctLabel.setText(NULL_PCT);
      }
  }//GEN-LAST:event_treeValueChanged

  private void dontExportCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dontExportCheckBoxActionPerformed
      ((GenericFile) ourObject).setDontExport(dontExportCheckBox.isSelected());
  }//GEN-LAST:event_dontExportCheckBoxActionPerformed

    /**
     * initialize a JTable with the current file. the
     * <code>model</code> should be set before calling this method.
     */
    private void createPhraseTable() {
        Font f = new Font(Kernel.settings.getString(Settings.FONT_TABLEVIEW_NAME),
                Kernel.settings.getInteger(Settings.FONT_TABLEVIEW_STYLE),
                Kernel.settings.getInteger(Settings.FONT_TABLEVIEW_SIZE));
        // build the table pane
        sorter = new ComplexTableSorter(model);
        table = new JTable(sorter, model.getTableColumnModel());
        model.setJTableReference(table);
        sorter.addMouseListenerToHeaderInTable(table);

        table.setDefaultRenderer(Phrase.class, new KeyColumnRenderer(this.currentLocalization));
        table.setFont(f);

        // do column inits
        Iterator columnIterator = columns.iterator();
        while (columnIterator.hasNext()) {
            ComplexColumn currentInit = (ComplexColumn) columnIterator.next();
            currentInit.init(table);
        }
    }

    /**
     *shows the content of the file in the right side of the splitepane
     *@param comp an AWT component representing the file content - a jtable, a
     *panel, or whatever.
     */
    private void showFileContent(java.awt.Component comp) {
        fileTabbedPane.remove(0);
        fileTabbedPane.insertTab(CONTENT_PANE_TITLE, null, comp, null, 0);
        fileTabbedPane.setSelectedIndex(0);
    }

    /*
     * MozFrame methods
     */
    /**
     * get the locale code
     * @return a {@link String} of the locale code, in the form lang-REG
     */
    @Override
    public String getLocalization() {
        return currentLocalization;
    }

    /**
     * get the table of strings. called from
     * org.mozillatranslator.action.RowBatchAction, when the fuzzy/unfuzzy
     * button in ChromeViewToolbar is clicked.
     * @return the table of strings currently displayed.
     */
    @Override
    public JTable getTable() {
        return table;
    }

    @Override
    public Phrase getSelectedPhrase() {
        int rowIndex;
        Phrase ph = null;
        rowIndex = table.getSelectedRow();
        if (rowIndex > -1) {
            ph = (Phrase) sorter.getRow(rowIndex);
        }
        return ph;
    }

    /**
     * Get the phrases selected in the strings table. this is called from
     * org.mozillatranslator.action.RowBatchAction, when the fuzzy/unfuzzy
     * button in ChromeViewToolbar is clicked.
     *
     *@return a list of the phrases who's rows are selected.
     */
    @Override
    public ArrayList<Phrase> getSelectedPhrases() {
        ArrayList<Phrase> phraseList = null;
        int[] idxList;

        try {
            idxList = table.getSelectedRows();
            if (idxList.length > 0) {
                phraseList = new ArrayList();
                for (int idx = 0; idx < idxList.length; idx++) {
                    phraseList.add((Phrase) sorter.getRow(idxList[idx]));
                }
            }
        } catch (NullPointerException e) {
        }
        return phraseList;
    }

    /**
     * Highlight row in table
     */
    private void selectTableRow(int num) {
        fLogger.log(Level.INFO, "Selected table row: {0}", num);

        if (num > 0) {
            table.scrollRectToVisible(
                    new Rectangle(
                    0,
                    (num + 2) * (table.getRowHeight() + table.getRowMargin()),
                    1,
                    1));
            table.setRowSelectionInterval(num, num);
        } else if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    @Override
    public boolean hasTreeStructure() {
        return true;
    }

    @Override
    public TreePath getTreeSelection() throws HasNoTreeException {
        return tree.getLeadSelectionPath();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JScrollPane contentPane;
    private javax.swing.JCheckBox dontExportCheckBox;
    private javax.swing.JTextField exportToField;
    private javax.swing.JLabel exportToLabel;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JTabbedPane fileTabbedPane;
    private javax.swing.JLabel fileValueLabel;
    private javax.swing.JLabel fuzzyPctLabel;
    private javax.swing.JLabel fuzzyStLabel;
    private javax.swing.JLabel fuzzyStValueLabel;
    private javax.swing.JScrollPane infoPane;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JSeparator infoSeparator;
    private javax.swing.JLabel koStLabel;
    private javax.swing.JLabel koStPctLabel;
    private javax.swing.JLabel koStValueLabel;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JLabel pathValueLabel;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JLabel pcValueLabel;
    private javax.swing.JLabel productLabel;
    private javax.swing.JLabel productValueLabel;
    private javax.swing.JSplitPane split;
    private javax.swing.JPanel staticInfoPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JLabel statsTitleLabel;
    private javax.swing.JLabel totalStLabel;
    private javax.swing.JLabel totalStValueLabel;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeScroll;
    private javax.swing.JLabel trnsStLabel;
    private javax.swing.JLabel trnsStPctLabel;
    private javax.swing.JLabel trnsStValueLabel;
    private javax.swing.JLabel untrnStValueLabel;
    private javax.swing.JLabel untrnsPctLabel;
    private javax.swing.JLabel untrnsStLabel;
    // End of variables declaration//GEN-END:variables
}
