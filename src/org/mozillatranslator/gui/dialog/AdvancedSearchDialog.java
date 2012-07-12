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
package org.mozillatranslator.gui.dialog;

import org.mozillatranslator.filter.AdvancedSearch;
import org.mozillatranslator.filter.Filter;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.util.GuiTools;

/**
 *
 * @author  Serhiy Brytskyy
 * @version 1.0
 */
public class AdvancedSearchDialog extends javax.swing.JDialog {
    private boolean okay;
    private boolean all;
    private AdvancedSearch dao;

    /** Creates new form AdvancedSearchDialog */
    public AdvancedSearchDialog() {
        super(Kernel.mainWindow, "Advanced Search", true);
        initComponents();
        JDialogHelper.setupOKCancelHotkeys(this, anyButton, cancelButton);
        GuiTools.placeFrameAtCenter(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        enableLabel = new javax.swing.JLabel();
        fieldLabel = new javax.swing.JLabel();
        ruleLabel = new javax.swing.JLabel();
        textLabel = new javax.swing.JLabel();
        caseLabel = new javax.swing.JLabel();
        firstEnable = new javax.swing.JCheckBox();
        firstField = new javax.swing.JComboBox();
        firstRule = new javax.swing.JComboBox();
        firstText = new javax.swing.JTextField();
        firstCase = new javax.swing.JCheckBox();
        secondEnable = new javax.swing.JCheckBox();
        secondField = new javax.swing.JComboBox();
        secondRule = new javax.swing.JComboBox();
        secondText = new javax.swing.JTextField();
        secondCase = new javax.swing.JCheckBox();
        thirdEnable = new javax.swing.JCheckBox();
        thirdField = new javax.swing.JComboBox();
        thirdRule = new javax.swing.JComboBox();
        thirdText = new javax.swing.JTextField();
        thirdCase = new javax.swing.JCheckBox();
        buttonPanel = new javax.swing.JPanel();
        anyButton = new javax.swing.JButton();
        allButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setName("dialog0"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        enableLabel.setText("Enabled?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(enableLabel, gridBagConstraints);

        fieldLabel.setText("Field");
        fieldLabel.setMaximumSize(new java.awt.Dimension(30, 15));
        fieldLabel.setMinimumSize(new java.awt.Dimension(30, 15));
        fieldLabel.setPreferredSize(new java.awt.Dimension(30, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(fieldLabel, gridBagConstraints);

        ruleLabel.setText("Rule ");
        ruleLabel.setMaximumSize(new java.awt.Dimension(31, 15));
        ruleLabel.setMinimumSize(new java.awt.Dimension(31, 15));
        ruleLabel.setPreferredSize(new java.awt.Dimension(31, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(ruleLabel, gridBagConstraints);

        textLabel.setText("Text");
        textLabel.setMaximumSize(new java.awt.Dimension(24, 15));
        textLabel.setMinimumSize(new java.awt.Dimension(24, 15));
        textLabel.setPreferredSize(new java.awt.Dimension(24, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(textLabel, gridBagConstraints);

        caseLabel.setText("Case Sensitive");
        caseLabel.setMaximumSize(new java.awt.Dimension(100, 15));
        caseLabel.setMinimumSize(new java.awt.Dimension(84, 15));
        caseLabel.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(caseLabel, gridBagConstraints);

        firstEnable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        firstEnable.setMaximumSize(new java.awt.Dimension(74, 23));
        firstEnable.setMinimumSize(new java.awt.Dimension(74, 23));
        firstEnable.setPreferredSize(new java.awt.Dimension(74, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(firstEnable, gridBagConstraints);

        firstField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Key", "Original Text", "Translated text", "Comment", "Translation Status" }));
        firstField.setMinimumSize(new java.awt.Dimension(122, 24));
        firstField.setPreferredSize(new java.awt.Dimension(140, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(firstField, gridBagConstraints);

        firstRule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Is", "Is not", "Contains", "Does not contain", "Starts with", "Ends with" }));
        firstRule.setMinimumSize(new java.awt.Dimension(135, 24));
        firstRule.setPreferredSize(new java.awt.Dimension(135, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(firstRule, gridBagConstraints);

        firstText.setColumns(15);
        firstText.setText("empty");
        firstText.setMinimumSize(new java.awt.Dimension(100, 27));
        firstText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstTextFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(firstText, gridBagConstraints);

        firstCase.setMaximumSize(new java.awt.Dimension(46, 23));
        firstCase.setMinimumSize(new java.awt.Dimension(46, 23));
        firstCase.setPreferredSize(new java.awt.Dimension(46, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        getContentPane().add(firstCase, gridBagConstraints);

        secondEnable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        secondEnable.setMaximumSize(new java.awt.Dimension(74, 23));
        secondEnable.setMinimumSize(new java.awt.Dimension(74, 23));
        secondEnable.setPreferredSize(new java.awt.Dimension(74, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(secondEnable, gridBagConstraints);

        secondField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Key", "Original Text", "Translated text", "Comment", "Translation Status" }));
        secondField.setMinimumSize(new java.awt.Dimension(122, 24));
        secondField.setPreferredSize(new java.awt.Dimension(140, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(secondField, gridBagConstraints);

        secondRule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Is", "Is not", "Contains", "Does not contain", "Starts with", "Ends with" }));
        secondRule.setMinimumSize(new java.awt.Dimension(135, 24));
        secondRule.setPreferredSize(new java.awt.Dimension(135, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(secondRule, gridBagConstraints);

        secondText.setColumns(15);
        secondText.setText("empty");
        secondText.setMinimumSize(new java.awt.Dimension(100, 27));
        secondText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                secondTextFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(secondText, gridBagConstraints);

        secondCase.setMaximumSize(new java.awt.Dimension(46, 23));
        secondCase.setMinimumSize(new java.awt.Dimension(46, 23));
        secondCase.setPreferredSize(new java.awt.Dimension(46, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        getContentPane().add(secondCase, gridBagConstraints);

        thirdEnable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thirdEnable.setMaximumSize(new java.awt.Dimension(74, 23));
        thirdEnable.setMinimumSize(new java.awt.Dimension(74, 23));
        thirdEnable.setPreferredSize(new java.awt.Dimension(74, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        getContentPane().add(thirdEnable, gridBagConstraints);

        thirdField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Key", "Original Text", "Translated text", "Comment", "Translation Status" }));
        thirdField.setMinimumSize(new java.awt.Dimension(122, 24));
        thirdField.setPreferredSize(new java.awt.Dimension(140, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(thirdField, gridBagConstraints);

        thirdRule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Is", "Is not", "Contains", "Does not contain", "Starts with", "Ends with" }));
        thirdRule.setMinimumSize(new java.awt.Dimension(135, 24));
        thirdRule.setPreferredSize(new java.awt.Dimension(135, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(thirdRule, gridBagConstraints);

        thirdText.setColumns(15);
        thirdText.setText("empty");
        thirdText.setMinimumSize(new java.awt.Dimension(100, 27));
        thirdText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                thirdTextFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        getContentPane().add(thirdText, gridBagConstraints);

        thirdCase.setMaximumSize(new java.awt.Dimension(46, 23));
        thirdCase.setMinimumSize(new java.awt.Dimension(46, 23));
        thirdCase.setPreferredSize(new java.awt.Dimension(46, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        getContentPane().add(thirdCase, gridBagConstraints);

        buttonPanel.setMinimumSize(new java.awt.Dimension(368, 35));
        buttonPanel.setPreferredSize(new java.awt.Dimension(368, 35));

        anyButton.setText("Any of the above");
        anyButton.setMaximumSize(new java.awt.Dimension(140, 25));
        anyButton.setMinimumSize(new java.awt.Dimension(140, 25));
        anyButton.setPreferredSize(new java.awt.Dimension(140, 25));
        anyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anyButtonPressed(evt);
            }
        });
        buttonPanel.add(anyButton);

        allButton.setText("All of the above");
        allButton.setMaximumSize(new java.awt.Dimension(133, 25));
        allButton.setMinimumSize(new java.awt.Dimension(133, 25));
        allButton.setPreferredSize(new java.awt.Dimension(133, 25));
        allButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allButtonPressed(evt);
            }
        });
        buttonPanel.add(allButton);

        cancelButton.setText("Cancel");
        cancelButton.setMaximumSize(new java.awt.Dimension(75, 25));
        cancelButton.setMinimumSize(new java.awt.Dimension(75, 25));
        cancelButton.setPreferredSize(new java.awt.Dimension(75, 25));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonPressed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonPressed
        all = false;
        okay = false;
        setVisible(false);
    }//GEN-LAST:event_cancelButtonPressed

    private void allButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allButtonPressed
        all = true;
        okay = true;
        setVisible(false);
    }//GEN-LAST:event_allButtonPressed

    private void anyButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anyButtonPressed
        all = false;
        okay = true;
        setVisible(false);
    }//GEN-LAST:event_anyButtonPressed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        okay = false;
        setVisible(false);
    }//GEN-LAST:event_closeDialog

    private void firstTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstTextFocusGained
        firstText.selectAll();
    }//GEN-LAST:event_firstTextFocusGained

    private void secondTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_secondTextFocusGained
        secondText.selectAll();
    }//GEN-LAST:event_secondTextFocusGained

    private void thirdTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_thirdTextFocusGained
        thirdText.selectAll();
    }//GEN-LAST:event_thirdTextFocusGained

    /**
     * Returns the DAO object with all information gathered from this dialog
     * @return a DAO object that will be used by a Filter object
     */
    public Filter getDataObject() {
        return dao;
    }

    /**
     * Show the Advanced Search Dialog, waits until it is closed and passes
     * all data gathered from the dialog to the DAO object
     * @return true if the dialog was closed with any of the "Proceed" buttons
     *         (Any of the above and All of the above), or false if Cancel or
     *         dialog Close buttons were pressed
     */
    public boolean showDialog() {
        Settings settings = Kernel.settings;

        // Set enabled flags
        firstEnable.setSelected(settings.getBoolean(Settings.ADV_SEARCH_ENABLE_1));
        secondEnable.setSelected(settings.getBoolean(Settings.ADV_SEARCH_ENABLE_2));
        thirdEnable.setSelected(settings.getBoolean(Settings.ADV_SEARCH_ENABLE_3));
        // Set field combo
        firstField.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_FIELD_1));
        secondField.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_FIELD_2));
        thirdField.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_FIELD_3));
        // Set rule combo
        firstRule.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_RULE_1));
        secondRule.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_RULE_2));
        thirdRule.setSelectedIndex(settings.getInteger(Settings.ADV_SEARCH_RULE_3));
        // Set the text fields
        firstText.setText(settings.getString(Settings.ADV_SEARCH_TEXT_1));
        secondText.setText(settings.getString(Settings.ADV_SEARCH_TEXT_2));
        thirdText.setText(settings.getString(Settings.ADV_SEARCH_TEXT_3));

        // Set case flags
        firstCase.setSelected(settings.getBoolean(Settings.ADV_SEARCH_CASE_1));
        secondCase.setSelected(settings.getBoolean(Settings.ADV_SEARCH_CASE_1));
        thirdCase.setSelected(settings.getBoolean(Settings.ADV_SEARCH_CASE_1));

        okay = false;
        GuiTools.placeFrameAtCenter(this);
        setVisible(true);

        if (okay) {
            String ln = Kernel.settings.getString(Settings.STATE_L10N);

            settings.setBoolean(Settings.ADV_SEARCH_ENABLE_1,
                    firstEnable.isSelected());
            settings.setBoolean(Settings.ADV_SEARCH_ENABLE_2,
                    secondEnable.isSelected());
            settings.setBoolean(Settings.ADV_SEARCH_ENABLE_3,
                    thirdEnable.isSelected());
            settings.setInteger(Settings.ADV_SEARCH_FIELD_1,
                    firstField.getSelectedIndex());
            settings.setInteger(Settings.ADV_SEARCH_FIELD_2,
                    secondField.getSelectedIndex());
            settings.setInteger(Settings.ADV_SEARCH_FIELD_3,
                    thirdField.getSelectedIndex());
            settings.setInteger(Settings.ADV_SEARCH_RULE_1,
                    firstRule.getSelectedIndex());
            settings.setInteger(Settings.ADV_SEARCH_RULE_2,
                    secondRule.getSelectedIndex());
            settings.setInteger(Settings.ADV_SEARCH_RULE_3,
                    thirdRule.getSelectedIndex());
            settings.setString(Settings.ADV_SEARCH_TEXT_1, firstText.getText());
            settings.setString(Settings.ADV_SEARCH_TEXT_2, secondText.getText());
            settings.setString(Settings.ADV_SEARCH_TEXT_3, thirdText.getText());
            settings.setBoolean(Settings.ADV_SEARCH_CASE_1,
                    firstCase.isSelected());
            settings.setBoolean(Settings.ADV_SEARCH_CASE_2,
                    secondCase.isSelected());
            settings.setBoolean(Settings.ADV_SEARCH_CASE_3,
                    thirdCase.isSelected());

            boolean b1 = firstEnable.isSelected();
            String v1 = firstText.getText();
            int r1 = firstRule.getSelectedIndex();
            int w1 = firstField.getSelectedIndex();
            boolean cc1 = firstCase.isSelected();

            boolean b2 = secondEnable.isSelected();
            String v2 = secondText.getText();
            int r2 = secondRule.getSelectedIndex();
            int w2 = secondField.getSelectedIndex();
            boolean cc2 = secondCase.isSelected();

            boolean b3 = thirdEnable.isSelected();
            String v3 = thirdText.getText();
            int r3 = thirdRule.getSelectedIndex();
            int w3 = thirdField.getSelectedIndex();
            boolean cc3 = thirdCase.isSelected();

            dao = new AdvancedSearch(all, ln, b1, r1, w1, v1, cc1, b2, r2, w2, v2,
                    cc2, b3, r3, w3, v3, cc3);
        }
        return okay;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allButton;
    private javax.swing.JButton anyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel caseLabel;
    private javax.swing.JLabel enableLabel;
    private javax.swing.JLabel fieldLabel;
    private javax.swing.JCheckBox firstCase;
    private javax.swing.JCheckBox firstEnable;
    private javax.swing.JComboBox firstField;
    private javax.swing.JComboBox firstRule;
    private javax.swing.JTextField firstText;
    private javax.swing.JLabel ruleLabel;
    private javax.swing.JCheckBox secondCase;
    private javax.swing.JCheckBox secondEnable;
    private javax.swing.JComboBox secondField;
    private javax.swing.JComboBox secondRule;
    private javax.swing.JTextField secondText;
    private javax.swing.JLabel textLabel;
    private javax.swing.JCheckBox thirdCase;
    private javax.swing.JCheckBox thirdEnable;
    private javax.swing.JComboBox thirdField;
    private javax.swing.JComboBox thirdRule;
    private javax.swing.JTextField thirdText;
    // End of variables declaration//GEN-END:variables
}
