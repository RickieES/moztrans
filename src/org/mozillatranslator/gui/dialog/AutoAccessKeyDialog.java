/*
 * AutoAccessKeyDialog.java
 *
 * Created on 13 de febrero de 2009, 20:45
 */

package org.mozillatranslator.gui.dialog;

import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import org.mozillatranslator.datamodel.AutoAccessKeyAssign.AccessKeyBundleList;
import org.mozillatranslator.gui.model.AutoAccessKeyTableModel;
import org.mozillatranslator.kernel.Kernel;

/**
 * Accesskey auto-assignment dialog
 * @author  rpalomares
 */
public class AutoAccessKeyDialog extends javax.swing.JDialog {
    /**
     * Default Constructor
     * @param data an autoAccessKeyBundleList object
     */
    public AutoAccessKeyDialog(AccessKeyBundleList data) {
        super(Kernel.mainWindow, true);
        aaKeysList = data;
        myTableModel = new AutoAccessKeyTableModel(data);
        initComponents();
        setTitle(Kernel.translate("AutoAssignAccesskeysDialogTitle.label"));
        for(int i = 0; i < myTableModel.getColumnCount(); i++) {
            TableColumn col = aaKeysTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(myTableModel.getColumnDefaultWidth(i));
        }
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

        legendPanel = new javax.swing.JPanel();
        legendLabel = new javax.swing.JLabel();
        tableScrPane = new javax.swing.JScrollPane();
        aaKeysTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        prefButton = new javax.swing.JButton();
        autoAssignButtom = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        legendLabel.setText("<html>Select your options using the Preferences button, then try Auto assign.<br>\nIf you are not satisfied with result, you can change preferences and try again, or<br>\nmanually edit the assignments. Confirm with OK, discard changes with Cancel.</html>");
        legendPanel.add(legendLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(legendPanel, gridBagConstraints);

        aaKeysTable.setAutoCreateRowSorter(true);
        aaKeysTable.setModel(myTableModel);
        tableScrPane.setViewportView(aaKeysTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(tableScrPane, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        prefButton.setFont(new java.awt.Font("Dialog", 0, 12));
        prefButton.setMnemonic('P');
        prefButton.setText("Preferences");
        prefButton.setMaximumSize(new java.awt.Dimension(240, 29));
        prefButton.setMinimumSize(new java.awt.Dimension(80, 29));
        prefButton.setPreferredSize(new java.awt.Dimension(120, 29));
        prefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(prefButton, gridBagConstraints);

        autoAssignButtom.setFont(new java.awt.Font("Dialog", 0, 12));
        autoAssignButtom.setMnemonic('A');
        autoAssignButtom.setText("Auto assign");
        autoAssignButtom.setMaximumSize(new java.awt.Dimension(100, 29));
        autoAssignButtom.setMinimumSize(new java.awt.Dimension(88, 29));
        autoAssignButtom.setPreferredSize(new java.awt.Dimension(120, 29));
        autoAssignButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAssignButtomActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(autoAssignButtom, gridBagConstraints);

        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(100, 29));
        okButton.setMinimumSize(new java.awt.Dimension(88, 29));
        okButton.setPreferredSize(new java.awt.Dimension(120, 29));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(okButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        cancelButton.setText("Cancel");
        cancelButton.setMaximumSize(new java.awt.Dimension(100, 29));
        cancelButton.setMinimumSize(new java.awt.Dimension(88, 29));
        cancelButton.setPreferredSize(new java.awt.Dimension(120, 29));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void prefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefButtonActionPerformed
    PrefsAutoAccessKeyDialog dlg = new PrefsAutoAccessKeyDialog();
    
    if (dlg.showDialog()) {
        JOptionPane.showMessageDialog(Kernel.mainWindow, "Changes saved");
    }
}//GEN-LAST:event_prefButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    setVisible(false);
}//GEN-LAST:event_cancelButtonActionPerformed

private void autoAssignButtomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAssignButtomActionPerformed
    aaKeysList.autoAssign();
    myTableModel.fireTableDataChanged();
}//GEN-LAST:event_autoAssignButtomActionPerformed

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    aaKeysList.applyAutoAssign();
    setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable aaKeysTable;
    private javax.swing.JButton autoAssignButtom;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel legendLabel;
    private javax.swing.JPanel legendPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton prefButton;
    private javax.swing.JScrollPane tableScrPane;
    // End of variables declaration//GEN-END:variables
    private AutoAccessKeyTableModel myTableModel;
    private AccessKeyBundleList aaKeysList;
}
