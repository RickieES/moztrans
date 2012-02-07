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

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.util.GuiTools;
import org.mozillatranslator.util.SimpleFontChooser;

/**
 * The new Setup Dialog for MozillaTranslator
 * @author rpalomares
 */
public class SetupDialog extends javax.swing.JDialog {
    private boolean okay;
    private List<String> availableLafs;
  
    /**
     * Creates new form SetupDialog
     */
    public SetupDialog() {
        super(Kernel.mainWindow, "Setup MozillaTranslator", true);
        availableLafs = Kernel.getAvailableLookAndFeels();
        initComponents();
        JDialogHelper.setupOKCancelHotkeys(this, okButton, cancelButton);
        GuiTools.placeFrameAtCenter(this);
    }

/**
 * Shows the dialog (in a modal way)
 */
public void showDialog() {
        okay = false;
        Settings set = Kernel.settings;

        // Display Tab
        Font f;
        f = new Font(set.getString(Settings.FONT_EDITPHRASE_NAME),
                     set.getInteger(Settings.FONT_EDITPHRASE_STYLE),
                     set.getInteger(Settings.FONT_EDITPHRASE_SIZE));
        editPhraseFontLabel.setFont(f);
        f = new Font(set.getString(Settings.FONT_TABLEVIEW_NAME),
                     set.getInteger(Settings.FONT_TABLEVIEW_STYLE),
                     set.getInteger(Settings.FONT_TABLEVIEW_SIZE));
        tableViewFontLabel.setFont(f);
        editPhraseFontLabel.repaint();
        tableViewFontLabel.repaint();
        guiShowWhatCheck.setSelected(set.getBoolean(Settings.GUI_SHOW_WHAT_DIALOG));
        lookAndFeelCombo.setSelectedItem(set.getString(Settings.GUI_LOOK_AND_FEEL));

        // Input/Output Tab
        replaceEnUSCheck.setSelected(set.getBoolean(Settings.EXPORT_REPLACE_ENUS));
        useExtZipCheck.setSelected(set.getBoolean(Settings.USE_EXTERNAL_ZIP));
        zipPathTextField.setText(set.getString(Settings.EXTERNAL_ZIP_PATH));
        unzipPathTextField.setText(set.getString(Settings.EXTERNAL_UNZIP_PATH));
        exportOnlyModifFilesCheck.setSelected(set.getBoolean(Settings.EXPORT_ONLY_MODIFIED));

        // Translation Assistance Tab
        useSuggCheckBox.setSelected(set.getBoolean(Settings.USE_SUGGESTIONS));
        percentCoincidenceTextField.setValue(set.getInteger(Settings.SUGGESTIONS_MATCH_VALUE));
        autoTranslateCheck.setSelected(set.getBoolean(Settings.AUTOTRANSLATE_ON_UPDATE));

        // Key Connection Tab
        akeyCaseCheck.setSelected(set.getBoolean(Settings.CONN_AKEYS_CASESENSE));
        akeyPatternField.setText(set.getString(Settings.CONN_AKEYS_PATTERNS));
        ckeyCaseCheck.setSelected(set.getBoolean(Settings.CONN_CKEYS_CASESENSE));
        ckeyPatternField.setText(set.getString(Settings.CONN_CKEYS_PATTERNS));
        labelCaseCheck.setSelected(set.getBoolean(Settings.CONN_LABEL_CASESENSE));
        labelPatternField.setText(set.getString(Settings.CONN_LABEL_PATTERNS));

        // Automated Tests tab
        origDTDEntField.setText(set.getString(Settings.QA_DTD_ORIG_ENTITIES_IGNORED));
        trnsDTDEntField.setText(set.getString(Settings.QA_DTD_TRNS_ENTITIES_IGNORED));
        endingCheckedCharsField.setText(set.getString(Settings.QA_ENDING_CHECKED_CHARS));
        useSuggCheckBox.setSelected(set.getBoolean(Settings.USE_SUGGESTIONS));
        pairedCharsListField.setText(set.getString(Settings.QA_PAIRED_CHARS_LIST));

        setVisible(true);

        if (okay) {
            // Set the new parameters

            // General tab
            f = editPhraseFontLabel.getFont();
            Kernel.editPhrase.setFontForOriginal(f);
            Kernel.editPhrase.setFontForTranslated(f);
            set.setString(Settings.FONT_EDITPHRASE_NAME, f.getFontName());
            set.setInteger(Settings.FONT_EDITPHRASE_SIZE, f.getSize());
            set.setInteger(Settings.FONT_EDITPHRASE_STYLE, f.getStyle());
            f = tableViewFontLabel.getFont();
            set.setString(Settings.FONT_TABLEVIEW_NAME, f.getFontName());
            set.setInteger(Settings.FONT_TABLEVIEW_SIZE, f.getSize());
            set.setInteger(Settings.FONT_TABLEVIEW_STYLE, f.getStyle());
            set.setBoolean(Settings.GUI_SHOW_WHAT_DIALOG, guiShowWhatCheck.isSelected());
            set.setString(Settings.GUI_LOOK_AND_FEEL, lookAndFeelCombo.getSelectedItem().toString());

            // Input/Output Tab
            set.setBoolean(Settings.EXPORT_REPLACE_ENUS, replaceEnUSCheck.isSelected());
            set.setBoolean(Settings.USE_EXTERNAL_ZIP, useExtZipCheck.isSelected());
            set.setString(Settings.EXTERNAL_ZIP_PATH, zipPathTextField.getText());
            set.setString(Settings.EXTERNAL_UNZIP_PATH, unzipPathTextField.getText());
            set.setBoolean(Settings.EXPORT_ONLY_MODIFIED, exportOnlyModifFilesCheck.isSelected());

            // Translation Assistance Tab
            set.setBoolean(Settings.USE_SUGGESTIONS, useSuggCheckBox.isSelected());
            set.setInteger(Settings.SUGGESTIONS_MATCH_VALUE, (Integer) percentCoincidenceTextField.getValue());
            set.setBoolean(Settings.AUTOTRANSLATE_ON_UPDATE, autoTranslateCheck.isSelected());
            
            // Key Connection Tab
            set.setBoolean(Settings.CONN_AKEYS_CASESENSE, akeyCaseCheck.isSelected());
            set.setString(Settings.CONN_AKEYS_PATTERNS, akeyPatternField.getText());
            set.setBoolean(Settings.CONN_CKEYS_CASESENSE, ckeyCaseCheck.isSelected());
            set.setString(Settings.CONN_CKEYS_PATTERNS, ckeyPatternField.getText());
            set.setBoolean(Settings.CONN_LABEL_CASESENSE, labelCaseCheck.isSelected());
            set.setString(Settings.CONN_LABEL_PATTERNS, labelPatternField.getText());

            // Automated tests tab
            set.setString(Settings.QA_DTD_ORIG_ENTITIES_IGNORED, origDTDEntField.getText());
            set.setString(Settings.QA_DTD_TRNS_ENTITIES_IGNORED, trnsDTDEntField.getText());
            set.setString(Settings.QA_ENDING_CHECKED_CHARS, endingCheckedCharsField.getText());
            set.setBoolean(Settings.USE_SUGGESTIONS, useSuggCheckBox.isSelected());
            set.setString(Settings.QA_PAIRED_CHARS_LIST, pairedCharsListField.getText());
        }
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        prefDisplayPanel = new javax.swing.JPanel();
        appearanceLabel = new javax.swing.JLabel();
        editPhraseFontLabel = new javax.swing.JLabel();
        editPhraseChooseButton = new javax.swing.JButton();
        tableViewFontLabel = new javax.swing.JLabel();
        tableViewChooseButton = new javax.swing.JButton();
        behaviourLabel = new javax.swing.JLabel();
        guiShowWhatCheck = new javax.swing.JCheckBox();
        lookAndFeelLabel = new javax.swing.JLabel();
        lookAndFeelCombo = new javax.swing.JComboBox();
        prefIOPanel = new javax.swing.JPanel();
        importExportLabel = new javax.swing.JLabel();
        replaceEnUSCheck = new javax.swing.JCheckBox();
        useExtZipCheck = new javax.swing.JCheckBox();
        zipPathLabel = new javax.swing.JLabel();
        zipPathTextField = new javax.swing.JTextField();
        zipPathButton = new javax.swing.JButton();
        unzipPathLabel = new javax.swing.JLabel();
        unzipPathTextField = new javax.swing.JTextField();
        unzipPathButton = new javax.swing.JButton();
        exportOnlyModifFilesCheck = new javax.swing.JCheckBox();
        prefTrnsAssistPanel = new javax.swing.JPanel();
        translationSuggestionsLabel = new javax.swing.JLabel();
        useSuggCheckBox = new javax.swing.JCheckBox();
        provideSuggFor1Label = new javax.swing.JLabel();
        provideSuggFor2Label = new javax.swing.JLabel();
        autoTranslateCheck = new javax.swing.JCheckBox();
        percentCoincidenceTextField = new javax.swing.JSpinner();
        prefKeyConnPanel = new javax.swing.JPanel();
        keyConnLabel = new javax.swing.JLabel();
        labelPatternLabel = new javax.swing.JLabel();
        labelPatternField = new javax.swing.JTextField();
        labelCaseCheck = new javax.swing.JCheckBox();
        ckeyPatternLabel = new javax.swing.JLabel();
        ckeyPatternField = new javax.swing.JTextField();
        ckeyCaseCheck = new javax.swing.JCheckBox();
        akeyPatternLabel = new javax.swing.JLabel();
        akeyPatternField = new javax.swing.JTextField();
        akeyCaseCheck = new javax.swing.JCheckBox();
        suffixInfoLabel = new javax.swing.JLabel();
        prefAutoTestsPanel = new javax.swing.JPanel();
        descripLabel = new javax.swing.JLabel();
        ignoredOrigEntLabel = new javax.swing.JLabel();
        origDTDEntField = new javax.swing.JTextField();
        ignoredTrnsEntLabel = new javax.swing.JLabel();
        trnsDTDEntField = new javax.swing.JTextField();
        endingCheckedCharsLabel = new javax.swing.JLabel();
        endingCheckedCharsField = new javax.swing.JTextField();
        pairedCharsListLabel = new javax.swing.JLabel();
        pairedCharsListField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(54, 27));
        okButton.setMinimumSize(new java.awt.Dimension(54, 27));
        okButton.setPreferredSize(new java.awt.Dimension(54, 27));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonPressed(evt);
            }
        });
        buttonPanel.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonPressed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        prefDisplayPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        appearanceLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        appearanceLabel.setText("Appearance");

        editPhraseFontLabel.setText("Font for Edit Phrase dialog");

        editPhraseChooseButton.setText("Choose...");
        editPhraseChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPhraseChooseButtonActionPerformed(evt);
            }
        });

        tableViewFontLabel.setText("Font for table views");

        tableViewChooseButton.setText("Choose...");
        tableViewChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableViewChooseButtonActionPerformed(evt);
            }
        });

        behaviourLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        behaviourLabel.setText("Behaviour");

        guiShowWhatCheck.setMnemonic('M');
        guiShowWhatCheck.setText("Make ShowWhat dialog visible");
        guiShowWhatCheck.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lookAndFeelLabel.setText("Look And Feel");

        lookAndFeelCombo.setModel(new DefaultComboBoxModel(availableLafs.toArray()));
        lookAndFeelCombo.setToolTipText("Graphical appearance of the application");

        javax.swing.GroupLayout prefDisplayPanelLayout = new javax.swing.GroupLayout(prefDisplayPanel);
        prefDisplayPanel.setLayout(prefDisplayPanelLayout);
        prefDisplayPanelLayout.setHorizontalGroup(
            prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appearanceLabel)
                    .addComponent(behaviourLabel)
                    .addGroup(prefDisplayPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(guiShowWhatCheck)
                            .addGroup(prefDisplayPanelLayout.createSequentialGroup()
                                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tableViewFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(editPhraseFontLabel))
                                    .addComponent(lookAndFeelLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(editPhraseChooseButton)
                                    .addComponent(tableViewChooseButton)
                                    .addComponent(lookAndFeelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(279, Short.MAX_VALUE))
        );
        prefDisplayPanelLayout.setVerticalGroup(
            prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appearanceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editPhraseFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPhraseChooseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableViewFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tableViewChooseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(prefDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lookAndFeelLabel)
                    .addComponent(lookAndFeelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(behaviourLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guiShowWhatCheck)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/mozillatranslator/resource/pref-display.png")), prefDisplayPanel, "Display"); // NOI18N

        importExportLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        importExportLabel.setText("Import/Export");

        replaceEnUSCheck.setMnemonic('R');
        replaceEnUSCheck.setText("Replace en-US directories name by ab-CD on exporting/writing");
        replaceEnUSCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        useExtZipCheck.setMnemonic('U');
        useExtZipCheck.setText("Use external ZIP binaries when exporting");
        useExtZipCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useExtZipCheck.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                useExtZipCheckStateChanged(evt);
            }
        });

        zipPathLabel.setText("Path to ZIP binary:");

        zipPathButton.setMnemonic('C');
        zipPathButton.setText("Choose...");
        zipPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zipPathButtonActionPerformed(evt);
            }
        });

        unzipPathLabel.setText("Path to UNZIP binary:");

        unzipPathButton.setMnemonic('h');
        unzipPathButton.setText("Choose...");
        unzipPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unzipPathButtonActionPerformed(evt);
            }
        });

        exportOnlyModifFilesCheck.setMnemonic('E');
        exportOnlyModifFilesCheck.setText("Export only modified files");

        javax.swing.GroupLayout prefIOPanelLayout = new javax.swing.GroupLayout(prefIOPanel);
        prefIOPanel.setLayout(prefIOPanelLayout);
        prefIOPanelLayout.setHorizontalGroup(
            prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefIOPanelLayout.createSequentialGroup()
                .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(prefIOPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(replaceEnUSCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(useExtZipCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(prefIOPanelLayout.createSequentialGroup()
                                .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(zipPathLabel)
                                    .addComponent(unzipPathLabel))
                                .addGap(7, 7, 7)
                                .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(prefIOPanelLayout.createSequentialGroup()
                                        .addComponent(unzipPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(unzipPathButton))
                                    .addGroup(prefIOPanelLayout.createSequentialGroup()
                                        .addComponent(zipPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(zipPathButton))))
                            .addComponent(exportOnlyModifFilesCheck)))
                    .addGroup(prefIOPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(importExportLabel)))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        prefIOPanelLayout.setVerticalGroup(
            prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, prefIOPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(importExportLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(replaceEnUSCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useExtZipCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zipPathLabel)
                    .addComponent(zipPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zipPathButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(prefIOPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unzipPathLabel)
                    .addComponent(unzipPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unzipPathButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportOnlyModifFilesCheck)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/mozillatranslator/resource/pref-io.png")), prefIOPanel, "Input/Output"); // NOI18N

        translationSuggestionsLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        translationSuggestionsLabel.setText("Translation Suggestions");

        useSuggCheckBox.setMnemonic('U');
        useSuggCheckBox.setText("Use translation suggestions on Edit Phrase Dialog");
        useSuggCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useSuggCheckBoxItemStateChanged(evt);
            }
        });

        provideSuggFor1Label.setDisplayedMnemonic('P');
        provideSuggFor1Label.setLabelFor(percentCoincidenceTextField);
        provideSuggFor1Label.setText("Provide suggestions for ");

        provideSuggFor2Label.setText("% coincidence or more");

        autoTranslateCheck.setMnemonic('A');
        autoTranslateCheck.setText("Auto-translate on Product Update");

        percentCoincidenceTextField.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

        javax.swing.GroupLayout prefTrnsAssistPanelLayout = new javax.swing.GroupLayout(prefTrnsAssistPanel);
        prefTrnsAssistPanel.setLayout(prefTrnsAssistPanelLayout);
        prefTrnsAssistPanelLayout.setHorizontalGroup(
            prefTrnsAssistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefTrnsAssistPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(prefTrnsAssistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(translationSuggestionsLabel)
                    .addGroup(prefTrnsAssistPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(prefTrnsAssistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(prefTrnsAssistPanelLayout.createSequentialGroup()
                                .addComponent(provideSuggFor1Label)
                                .addGap(4, 4, 4)
                                .addComponent(percentCoincidenceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(provideSuggFor2Label))
                            .addComponent(useSuggCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(autoTranslateCheck))))
                .addContainerGap(186, Short.MAX_VALUE))
        );
        prefTrnsAssistPanelLayout.setVerticalGroup(
            prefTrnsAssistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefTrnsAssistPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(translationSuggestionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useSuggCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefTrnsAssistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provideSuggFor1Label)
                    .addComponent(provideSuggFor2Label)
                    .addComponent(percentCoincidenceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoTranslateCheck)
                .addContainerGap(138, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/mozillatranslator/resource/pref-trns-asist.png")), prefTrnsAssistPanel, "Translation Assistance"); // NOI18N

        keyConnLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        keyConnLabel.setText("Key Connection");

        labelPatternLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPatternLabel.setText("Label suffixes:");
        labelPatternLabel.setMaximumSize(new java.awt.Dimension(152, 17));
        labelPatternLabel.setMinimumSize(new java.awt.Dimension(152, 17));
        labelPatternLabel.setPreferredSize(new java.awt.Dimension(152, 17));

        labelPatternField.setText("Label patterns");

        labelCaseCheck.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        labelCaseCheck.setText("C.S.");
        labelCaseCheck.setToolTipText("Check to make label suffixes case-sensitive.");
        labelCaseCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        ckeyPatternLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ckeyPatternLabel.setText("Commandkeys suffixes:");

        ckeyPatternField.setText("Commandkeys patterns");

        ckeyCaseCheck.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ckeyCaseCheck.setText("C.S.");
        ckeyCaseCheck.setToolTipText("Check to make commandkeys suffixes case-sensitive.");
        ckeyCaseCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        akeyPatternLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        akeyPatternLabel.setText("Accesskeys suffixes:");
        akeyPatternLabel.setMaximumSize(new java.awt.Dimension(152, 17));
        akeyPatternLabel.setMinimumSize(new java.awt.Dimension(152, 17));
        akeyPatternLabel.setPreferredSize(new java.awt.Dimension(152, 17));

        akeyPatternField.setText("Accesskeys patterns");

        akeyCaseCheck.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        akeyCaseCheck.setText("C.S.");
        akeyCaseCheck.setToolTipText("Check to make accesskeys suffixes case-sensitive.");
        akeyCaseCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        suffixInfoLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        suffixInfoLabel.setText("<html>\n<p><b>Tip</b>: enter suffixes used for labels (incl. buttons and menu options), accesskeys and commandkeys.</p>\n<ul>\n<li>Separate multiples suffixes with | (vertical pipe).</li>\n<li>Include the empty suffix (useful for labels, don't use for others!) using [:empty:].</li>\n<li>NO leading dot is automatically prepended.</li>\n</ul>\n<p>Example: |.label|.button|.nameCmd||</p>\n</html>\n");

        javax.swing.GroupLayout prefKeyConnPanelLayout = new javax.swing.GroupLayout(prefKeyConnPanel);
        prefKeyConnPanel.setLayout(prefKeyConnPanelLayout);
        prefKeyConnPanelLayout.setHorizontalGroup(
            prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suffixInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                        .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(keyConnLabel)
                            .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(labelPatternLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(akeyPatternLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(ckeyPatternLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ckeyPatternField, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                                    .addComponent(labelPatternField)
                                    .addComponent(akeyPatternField, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckeyCaseCheck)
                            .addComponent(akeyCaseCheck)
                            .addComponent(labelCaseCheck))
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        prefKeyConnPanelLayout.setVerticalGroup(
            prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefKeyConnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keyConnLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPatternField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPatternLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCaseCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckeyPatternField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckeyPatternLabel)
                    .addComponent(ckeyCaseCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefKeyConnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(akeyPatternField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(akeyPatternLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(akeyCaseCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suffixInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/mozillatranslator/resource/autoassign-ak.png")), prefKeyConnPanel, "Key Connections"); // NOI18N

        descripLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        descripLabel.setText("DTD entities ignored in Check Variables (example: &one;|&two;|&three;)");

        ignoredOrigEntLabel.setDisplayedMnemonic('O');
        ignoredOrigEntLabel.setText("Original:");

        ignoredTrnsEntLabel.setDisplayedMnemonic('T');
        ignoredTrnsEntLabel.setText("Translated:");

        endingCheckedCharsLabel.setDisplayedMnemonic('E');
        endingCheckedCharsLabel.setText("Ending checked chars:");

        endingCheckedCharsField.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N

        pairedCharsListLabel.setDisplayedMnemonic('h');
        pairedCharsListLabel.setText("Character pairs to check:");

        pairedCharsListField.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        pairedCharsListField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pairedCharsListFieldFocusLost(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel1.setText("Parameters for other automated tests");

        javax.swing.GroupLayout prefAutoTestsPanelLayout = new javax.swing.GroupLayout(prefAutoTestsPanel);
        prefAutoTestsPanel.setLayout(prefAutoTestsPanelLayout);
        prefAutoTestsPanelLayout.setHorizontalGroup(
            prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ignoredOrigEntLabel)
                            .addComponent(ignoredTrnsEntLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(trnsDTDEntField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(origDTDEntField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(descripLabel))
                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                                        .addComponent(pairedCharsListLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pairedCharsListField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                                        .addComponent(endingCheckedCharsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(endingCheckedCharsField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel1))))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        prefAutoTestsPanelLayout.setVerticalGroup(
            prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descripLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(origDTDEntField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ignoredOrigEntLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trnsDTDEntField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ignoredTrnsEntLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(prefAutoTestsPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(endingCheckedCharsLabel))
                    .addComponent(endingCheckedCharsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefAutoTestsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pairedCharsListField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pairedCharsListLabel))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/mozillatranslator/resource/pref-qa-tests.png")), prefAutoTestsPanel, "Automated Tests"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editPhraseChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPhraseChooseButtonActionPerformed
        SimpleFontChooser sfc = new SimpleFontChooser(Kernel.mainWindow,
                editPhraseFontLabel.getFont());

        if (sfc.showFontDialog() == SimpleFontChooser.APPROVE_OPTION) {
            editPhraseFontLabel.setFont(sfc.getSelectedFont());
        }
    }//GEN-LAST:event_editPhraseChooseButtonActionPerformed

    private void tableViewChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableViewChooseButtonActionPerformed
        SimpleFontChooser sfc = new SimpleFontChooser(Kernel.mainWindow,
                tableViewFontLabel.getFont());

        if (sfc.showFontDialog() == SimpleFontChooser.APPROVE_OPTION) {
            tableViewFontLabel.setFont(sfc.getSelectedFont());
        }
    }//GEN-LAST:event_tableViewChooseButtonActionPerformed

    private void okButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonPressed
        okay = true;
        setVisible(false);
    }//GEN-LAST:event_okButtonPressed

    private void cancelButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonPressed
        okay = false;
        setVisible(false);
    }//GEN-LAST:event_cancelButtonPressed

    private void useExtZipCheckStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_useExtZipCheckStateChanged
        unzipPathTextField.setEnabled(useExtZipCheck.isSelected());
        unzipPathButton.setEnabled(useExtZipCheck.isSelected());
        zipPathTextField.setEnabled(useExtZipCheck.isSelected());
        zipPathButton.setEnabled(useExtZipCheck.isSelected());
    }//GEN-LAST:event_useExtZipCheckStateChanged

    private void zipPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zipPathButtonActionPerformed
        File defaultFile;
        JFileChooser chooser;
        int result;

        defaultFile = new File(zipPathTextField.getText());
        chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select a ZIP utility accepting parameters ala InfoZIP style");
        chooser.setSelectedFile(defaultFile);
        result = chooser.showDialog(this, "Choose");
        if (result == JFileChooser.APPROVE_OPTION) {
            defaultFile = chooser.getSelectedFile();
            zipPathTextField.setText(defaultFile.toString());
        }
    }//GEN-LAST:event_zipPathButtonActionPerformed

    private void unzipPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unzipPathButtonActionPerformed
        File defaultFile;
        JFileChooser chooser;
        int result;

        defaultFile = new File(unzipPathTextField.getText());
        chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select a UNZIP utility accepting parameters ala InfoZIP style");
        chooser.setSelectedFile(defaultFile);
        result = chooser.showDialog(this, "Choose");
        if (result == JFileChooser.APPROVE_OPTION) {
            defaultFile = chooser.getSelectedFile();
            unzipPathTextField.setText(defaultFile.toString());
        }
    }//GEN-LAST:event_unzipPathButtonActionPerformed

    private void pairedCharsListFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pairedCharsListFieldFocusLost
        if (pairedCharsListField.getText().length() % 2 != 0) {
            JOptionPane.showMessageDialog(this, "Please, specify a string with even "
                    + "length (or 0 to disable)",
                    "String length error", JOptionPane.ERROR_MESSAGE);
            pairedCharsListField.requestFocusInWindow();
        }
    }//GEN-LAST:event_pairedCharsListFieldFocusLost

    private void useSuggCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_useSuggCheckBoxItemStateChanged
        this.percentCoincidenceTextField.setEnabled((evt.getStateChange() == ItemEvent.SELECTED));
    }//GEN-LAST:event_useSuggCheckBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox akeyCaseCheck;
    private javax.swing.JTextField akeyPatternField;
    private javax.swing.JLabel akeyPatternLabel;
    private javax.swing.JLabel appearanceLabel;
    private javax.swing.JCheckBox autoTranslateCheck;
    private javax.swing.JLabel behaviourLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox ckeyCaseCheck;
    private javax.swing.JTextField ckeyPatternField;
    private javax.swing.JLabel ckeyPatternLabel;
    private javax.swing.JLabel descripLabel;
    private javax.swing.JButton editPhraseChooseButton;
    private javax.swing.JLabel editPhraseFontLabel;
    private javax.swing.JTextField endingCheckedCharsField;
    private javax.swing.JLabel endingCheckedCharsLabel;
    private javax.swing.JCheckBox exportOnlyModifFilesCheck;
    private javax.swing.JCheckBox guiShowWhatCheck;
    private javax.swing.JLabel ignoredOrigEntLabel;
    private javax.swing.JLabel ignoredTrnsEntLabel;
    private javax.swing.JLabel importExportLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel keyConnLabel;
    private javax.swing.JCheckBox labelCaseCheck;
    private javax.swing.JTextField labelPatternField;
    private javax.swing.JLabel labelPatternLabel;
    private javax.swing.JComboBox lookAndFeelCombo;
    private javax.swing.JLabel lookAndFeelLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField origDTDEntField;
    private javax.swing.JTextField pairedCharsListField;
    private javax.swing.JLabel pairedCharsListLabel;
    private javax.swing.JSpinner percentCoincidenceTextField;
    private javax.swing.JPanel prefAutoTestsPanel;
    private javax.swing.JPanel prefDisplayPanel;
    private javax.swing.JPanel prefIOPanel;
    private javax.swing.JPanel prefKeyConnPanel;
    private javax.swing.JPanel prefTrnsAssistPanel;
    private javax.swing.JLabel provideSuggFor1Label;
    private javax.swing.JLabel provideSuggFor2Label;
    private javax.swing.JCheckBox replaceEnUSCheck;
    private javax.swing.JLabel suffixInfoLabel;
    private javax.swing.JButton tableViewChooseButton;
    private javax.swing.JLabel tableViewFontLabel;
    private javax.swing.JLabel translationSuggestionsLabel;
    private javax.swing.JTextField trnsDTDEntField;
    private javax.swing.JButton unzipPathButton;
    private javax.swing.JLabel unzipPathLabel;
    private javax.swing.JTextField unzipPathTextField;
    private javax.swing.JCheckBox useExtZipCheck;
    private javax.swing.JCheckBox useSuggCheckBox;
    private javax.swing.JButton zipPathButton;
    private javax.swing.JLabel zipPathLabel;
    private javax.swing.JTextField zipPathTextField;
    // End of variables declaration//GEN-END:variables
}
