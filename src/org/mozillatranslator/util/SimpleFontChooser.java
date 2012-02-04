/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
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
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Implements a font chooser dialog, modeled after JFileChooser
 * 
 * @author  rpalomares
 * @version 1.0
 */
public class SimpleFontChooser extends javax.swing.JDialog {

    /** Creates new dialog SimpleFontChooser
     * @param parent the parent frame to which this dialog is bound
     * @param modal true if the dialog should be modal
     */
    public SimpleFontChooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts = ge.getAvailableFontFamilyNames();
        initComponents();
    }
    
    /**
     * Creates a new modal dialog SimpleFontChooser with a pre-selected font
     * @param parent the parent frame to which this dialog is bound
     * @param f the pre-selected font; useful to provide a "previous choice"
     */
    public SimpleFontChooser(java.awt.Frame parent, Font f) {
        this(parent, true);

        int i;
        
        for(i = 0; i < fonts.length; i++) {
            if (fonts[i].equals(f.getFamily())) {
                break;
            }
        }
        
        if (i != fonts.length) {
            this.fontListCombo.setSelectedIndex(i);
        } else {
            this.fontListCombo.setSelectedIndex(0);
        }
        
        this.sizeSpinner.setValue(f.getSize());
        this.boldCheckBox.setSelected(f.isBold());
        this.italicCheckBox.setSelected(f.isItalic());
        this.sampleTextArea.setFont(f);
    }
    
    public SimpleFontChooser(java.awt.Frame parent, Font f, String sampleText) {
        this(parent, f);
        setSampleText(sampleText);
    }
    
    public final void setSampleText(String s) {
        sampleTextArea.setText(s);
        sampleTextArea.repaint();
    }
    
    public void resetSampleText() {
        sampleTextArea.setText(SimpleFontChooser.DEFAULT_SAMPLE_TEXT);
        sampleTextArea.repaint();
    }
    
    /**
     * Shows the font chooser dialog; it works in a similar way to
     * JFileChooser.showOpenDialog()
     * @return SimpleFontChooser.APPROVE_OPTION if the user quit the dialog
     * pressing OK, SimpleFontChooser.CANCEL_OPTION otherwise
     */
    public int showFontDialog() {
        this.setVisible(true);
        
        return result;
    }
    
    /**
     * Returns the user-selected font, including size and style; this works
     * in a similar way to JFileChooser.getSelectedFile()
     * @return
     */
    public Font getSelectedFont() {
        return selectedFont;
    }
    
    private Font adjustFontProperties() {
        Font f;
        int fontStyle = (boldCheckBox.isSelected()) ? Font.BOLD : Font.PLAIN;
        fontStyle = fontStyle | ((italicCheckBox.isSelected()) ? Font.ITALIC
                                                                : Font.PLAIN);
        
        f = new Font((String) fontListCombo.getSelectedItem(),
                               fontStyle, (Integer) sizeSpinner.getValue());
        sampleTextArea.setFont(f);
        return f;
}
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        okButton = new javax.swing.JButton();
        fontListCombo = new javax.swing.JComboBox(fonts);
        comboLabel = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        sizeSpinner = new javax.swing.JSpinner();
        boldCheckBox = new javax.swing.JCheckBox();
        italicCheckBox = new javax.swing.JCheckBox();
        sampleTextPanel = new javax.swing.JPanel();
        sampleTextScroll = new javax.swing.JScrollPane();
        sampleTextArea = new javax.swing.JTextArea();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose font");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(80, 27));
        okButton.setMinimumSize(new java.awt.Dimension(80, 27));
        okButton.setPreferredSize(new java.awt.Dimension(80, 27));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(okButton, gridBagConstraints);

        fontListCombo.setModel(new javax.swing.DefaultComboBoxModel(fonts));
        fontListCombo.setMinimumSize(new java.awt.Dimension(100, 27));
        fontListCombo.setPreferredSize(new java.awt.Dimension(100, 27));
        fontListCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontListComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(fontListCombo, gridBagConstraints);

        comboLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        comboLabel.setText("Choose font:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(comboLabel, gridBagConstraints);

        sizeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sizeLabel.setText("Size:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(sizeLabel, gridBagConstraints);

        sizeSpinner.setValue(new Integer(12));
        sizeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizeSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(sizeSpinner, gridBagConstraints);

        boldCheckBox.setText("Bold");
        boldCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                boldCheckBoxStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(boldCheckBox, gridBagConstraints);

        italicCheckBox.setText("Italic");
        italicCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                italicCheckBoxStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(italicCheckBox, gridBagConstraints);

        sampleTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sample text"));
        sampleTextPanel.setLayout(new java.awt.GridBagLayout());

        sampleTextScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sampleTextScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sampleTextScroll.setMinimumSize(new java.awt.Dimension(450, 64));
        sampleTextScroll.setPreferredSize(new java.awt.Dimension(450, 64));

        sampleTextArea.setColumns(20);
        sampleTextArea.setEditable(false);
        sampleTextArea.setRows(1);
        sampleTextArea.setText("The quick brown fox jumps over the lazy dog");
        sampleTextScroll.setViewportView(sampleTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        sampleTextPanel.add(sampleTextScroll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(sampleTextPanel, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setMaximumSize(new java.awt.Dimension(80, 27));
        cancelButton.setMinimumSize(new java.awt.Dimension(80, 27));
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 27));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void fontListComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontListComboActionPerformed
    adjustFontProperties();
}//GEN-LAST:event_fontListComboActionPerformed

private void sizeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizeSpinnerStateChanged
    adjustFontProperties();
}//GEN-LAST:event_sizeSpinnerStateChanged

private void boldCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_boldCheckBoxStateChanged
    adjustFontProperties();
}//GEN-LAST:event_boldCheckBoxStateChanged

private void italicCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_italicCheckBoxStateChanged
    adjustFontProperties();
}//GEN-LAST:event_italicCheckBoxStateChanged

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    result = CANCEL_OPTION;
    setVisible(false);
}//GEN-LAST:event_cancelButtonActionPerformed

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    result = APPROVE_OPTION;
    selectedFont = adjustFontProperties();
    setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox boldCheckBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel comboLabel;
    private javax.swing.JComboBox fontListCombo;
    private javax.swing.JCheckBox italicCheckBox;
    private javax.swing.JButton okButton;
    private javax.swing.JTextArea sampleTextArea;
    private javax.swing.JPanel sampleTextPanel;
    private javax.swing.JScrollPane sampleTextScroll;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JSpinner sizeSpinner;
    // End of variables declaration//GEN-END:variables
    public static final int APPROVE_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    public static final String DEFAULT_SAMPLE_TEXT = "The quick brown fox jumps over the lazy dog";
    private String[] fonts;
    private int result = 0;
    private Font selectedFont = null;

}
