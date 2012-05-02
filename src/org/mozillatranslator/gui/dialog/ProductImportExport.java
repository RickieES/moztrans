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
 * Ricardo Palomares (initial code)
 */
package org.mozillatranslator.gui.dialog;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.mozillatranslator.datamodel.Product;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/**
 * Implements a product import/export dialog intended to be used with
 * non-JAR based products
 *
 * @author rpalomares
 */
public class ProductImportExport extends javax.swing.JPanel {
    private class ProductBundle {
        Product product;
        JCheckBox productCB;
    }

    /**
     * Import/update the original strings
     */
    public static final int TYPE_IMPORT_ORIGINAL = 1;
    /**
     * Import the translated strings
     */
    public static final int TYPE_IMPORT_TRANSLATION = 2;
    /**
     * Export the translated strings
     */
    public static final int TYPE_EXPORT_TRANSLATION = 3;
    private boolean okay = false;
    private ArrayList<ProductBundle> pbList;
    int typeAction;

    /**
     * Creates a new ProductImportExport JPanel
     * @param typeAction The type of action to be performed through this panel. See TYPE_* constants
     */
    public ProductImportExport(int typeAction) {
        pbList = new ArrayList<ProductBundle>();
        this.typeAction = typeAction;
        Settings set = Kernel.settings;

        initComponents();

        for (Product p : Kernel.datamodel.toArray()) {
            ProductBundle pb = new ProductBundle();
            pb.product = p;
            pb.productCB = new JCheckBox(p.getName());
            prodListPane.add(pb.productCB);
            pb.productCB.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    productCheckBoxActionPerformed(evt);
                }
            });
            pbList.add(pb);
        }

        switch (typeAction) {
            case TYPE_IMPORT_ORIGINAL:
                optionCheckBox.setVisible(true);
                optionCheckBox.setText("Auto-translate after updating");
                optionCheckBox.setMnemonic('u');
                optionCheckBox.setToolTipText("Run Auto-translate on new/modified strings");
                break;
            case TYPE_IMPORT_TRANSLATION:
                optionCheckBox.setVisible(false);
                break;
            case TYPE_EXPORT_TRANSLATION:
                optionCheckBox.setVisible(true);
                optionCheckBox.setText("Export only modified files");
                optionCheckBox.setMnemonic('x');
                optionCheckBox.setToolTipText("Export only files modified since last export");
                break;
        }
        productCheckBoxActionPerformed(null);
        optionCheckBox.setSelected(set.getBoolean((this.typeAction == TYPE_EXPORT_TRANSLATION) ?
                                                   Settings.EXPORT_ONLY_MODIFIED : Settings.AUTOTRANSLATE_ON_UPDATE));
        invalidate();
    }

    public boolean isOkPressed() {
        return okay;
    }

    /**
     * Returns the value of the Export Only Modified files flag
     * @return true if the user has chosen to export just modified files
     */
    public boolean isExportOnlyModified() {
        return this.optionCheckBox.isSelected();
    }

    /**
     * Returns the value of the Run Auto-translate check box un updating
     * @return true if the user wants to run auto-translate on upate
     */
    public boolean isRunAutoTranslate() {
        return this.optionCheckBox.isSelected();
    }

    /**
     * Returns the value of the Import/Export Path text field
     * @return the value of the Import/Export Path text field
     */
    public String getImpExpPath() {
        return impExpPathTextField.getText();
    }

    private int getNumberOfSelectedProducts() {
        int result = 0;

        for(ProductBundle pb : pbList) {
            if (pb.productCB.isSelected())
                result++;
        }
        return result;
    }

    /**
     * Returns a Product array with every selected product by the user
     * @return a Product array with every selected product by the user
     */
    public Product[] getSelectedProducts() {
        List<Product> selectedProductList = new ArrayList<Product>();

        for(ProductBundle pb : pbList) {
            if (pb.productCB.isSelected()) {
                selectedProductList.add(pb.product);
            }
        }
        if (selectedProductList.size() > 0) {
            return selectedProductList.toArray(new Product[1]);
        } else {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productSelectLabel = new javax.swing.JLabel();
        okCancelPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        prodListScrollPane = new javax.swing.JScrollPane();
        prodListPane = new javax.swing.JPanel();
        optionCheckBox = new javax.swing.JCheckBox();
        impExpPathLabel = new javax.swing.JLabel();
        impExpPathTextField = new javax.swing.JTextField();
        choosePathButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        selectNoneButton = new javax.swing.JButton();

        productSelectLabel.setText("Select one or more products");

        okCancelPanel.setPreferredSize(new java.awt.Dimension(353, 33));

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(75, 31));
        okButton.setMinimumSize(new java.awt.Dimension(75, 31));
        okButton.setPreferredSize(new java.awt.Dimension(75, 31));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setMaximumSize(new java.awt.Dimension(75, 31));
        cancelButton.setMinimumSize(new java.awt.Dimension(75, 31));
        cancelButton.setPreferredSize(new java.awt.Dimension(75, 31));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout okCancelPanelLayout = new javax.swing.GroupLayout(okCancelPanel);
        okCancelPanel.setLayout(okCancelPanelLayout);
        okCancelPanelLayout.setHorizontalGroup(
            okCancelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(okCancelPanelLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        okCancelPanelLayout.setVerticalGroup(
            okCancelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(okCancelPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(okCancelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        prodListPane.setLayout(new javax.swing.BoxLayout(prodListPane, javax.swing.BoxLayout.Y_AXIS));
        prodListScrollPane.setViewportView(prodListPane);

        optionCheckBox.setMnemonic('x');
        optionCheckBox.setSelected(true);
        optionCheckBox.setText("Export only modified");

        impExpPathLabel.setDisplayedMnemonic('m');
        impExpPathLabel.setLabelFor(impExpPathTextField);
        impExpPathLabel.setText("Import/Export Path");
        impExpPathLabel.setToolTipText("");

        choosePathButton.setMnemonic('C');
        choosePathButton.setText("Choose path");
        choosePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePathButtonActionPerformed(evt);
            }
        });

        selectAllButton.setMnemonic('a');
        selectAllButton.setText("Select all");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        selectNoneButton.setMnemonic('n');
        selectNoneButton.setText("Select none");
        selectNoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNoneButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(prodListScrollPane)
                            .addComponent(productSelectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(selectAllButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(selectNoneButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(choosePathButton)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(optionCheckBox)
                                            .addComponent(impExpPathLabel))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(impExpPathTextField))
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okCancelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productSelectLabel)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(optionCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impExpPathLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(impExpPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(prodListScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choosePathButton)
                    .addComponent(selectAllButton)
                    .addComponent(selectNoneButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okCancelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        int selectedProducts = getNumberOfSelectedProducts();

        switch (selectedProducts) {
            case 0:
                // No product selected, so no point in clicking OK
                okay = false;
                break;
            case 1:
                // When there is exactly one product selected, the import/export path may have
                // been changed by user, so we have to check that it is a valid path, except
                // of course if the product is JAR based
                Product prod = (Product) this.getSelectedProducts()[0];
                boolean isJarBased = ((prod.getNeutralPlatform().getJarFile() != null)
                        && (prod.getNeutralPlatform().getJarFile().length() > 0));
                File selectedDir;

                if (isJarBased) {
                    okay = true;
                    Container c = this.getTopLevelAncestor();
                    c.setVisible(false);
                } else {
                    try {
                        selectedDir = new File(impExpPathTextField.getText());
                        okay = selectedDir.exists() && selectedDir.isDirectory();
                    } catch (java.lang.NullPointerException e) {
                        okay = false;
                    }

                    // If the path can be parsed as a valid dir, exists and is a directory
                    if (okay) {
                        Container c = this.getTopLevelAncestor();
                        c.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "You MUST enter a valid path!",
                                "Invalid path specified", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            default:
                // When there are more than one selected product, the default import/export
                // paths will be used for non-JAR based products, so there is nothing else to check
                okay = true;
                Container c = this.getTopLevelAncestor();
                c.setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        okay = false;
        Container c = this.getTopLevelAncestor();
        c.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void choosePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePathButtonActionPerformed
        File defaultFile;
        File f;
        JFileChooser chooser;
        Product prod;
        int result;

        prod = (Product) this.getSelectedProducts()[0];
        defaultFile = new File(impExpPathTextField.getText());
        if (defaultFile.isDirectory()) {
            f = defaultFile;
        } else {
            try {
                f = new File(prod.getCVSImportOriginalPath());
            } catch (java.lang.NullPointerException e) {
                f = new File("");
            }
        }

        chooser = new JFileChooser(f);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        switch (this.typeAction) {
            case TYPE_IMPORT_ORIGINAL:
            case TYPE_IMPORT_TRANSLATION:
                chooser.setDialogTitle("Select directory to import from");
                break;
            case TYPE_EXPORT_TRANSLATION:
                chooser.setDialogTitle("Select directory to export to");
                break;
        }

        result = chooser.showDialog(this, "Choose directory");
        if (result == JFileChooser.APPROVE_OPTION) {
            impExpPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_choosePathButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        for(ProductBundle pb : pbList) {
            pb.productCB.setSelected(true);
        }
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void selectNoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNoneButtonActionPerformed
        for(ProductBundle pb : pbList) {
            pb.productCB.setSelected(false);
        }
    }//GEN-LAST:event_selectNoneButtonActionPerformed

    private void productCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Product prod;

        if (this.getNumberOfSelectedProducts() == 1) {
            JCheckBox productCheckBox = (evt == null) ? null : ((JCheckBox) evt.getSource());

            for(ProductBundle pb : pbList) {
                if (pb.productCB == productCheckBox) {
                    prod = pb.product;

                    boolean isJarBased = ((prod.getNeutralPlatform().getJarFile() != null) &&
                            (prod.getNeutralPlatform().getJarFile().length() > 0));

                    impExpPathTextField.setEnabled(!isJarBased);
                    choosePathButton.setEnabled(!isJarBased);
                    if (isJarBased) {
                        impExpPathTextField.setText("");
                    } else {
                        switch (typeAction) {
                            case TYPE_IMPORT_ORIGINAL:
                                impExpPathTextField.setText(prod.getCVSImportOriginalPath());
                                break;
                            case TYPE_IMPORT_TRANSLATION:
                                impExpPathTextField.setText(prod.getCVSImportTranslationPath());
                                break;
                            case TYPE_EXPORT_TRANSLATION:
                                impExpPathTextField.setText(prod.getCVSExportTranslationPath());
                                break;
                        }
                    }
                }
            }
        } else {
            impExpPathTextField.setText("");
            impExpPathTextField.setEnabled(false);
            choosePathButton.setEnabled(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton choosePathButton;
    private javax.swing.JLabel impExpPathLabel;
    private javax.swing.JTextField impExpPathTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel okCancelPanel;
    private javax.swing.JCheckBox optionCheckBox;
    private javax.swing.JPanel prodListPane;
    private javax.swing.JScrollPane prodListScrollPane;
    private javax.swing.JLabel productSelectLabel;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectNoneButton;
    // End of variables declaration//GEN-END:variables
}
