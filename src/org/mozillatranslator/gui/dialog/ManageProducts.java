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

import javax.swing.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.util.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class ManageProducts extends JDialog
{

	/** Creates new form ManageProducts */
	public ManageProducts()
	{
		super(Kernel.mainWindow, "Manage products", true);
		initComponents();
                getRootPane().setDefaultButton(exitButton);
		GuiTools.placeFrameAtCenter(this);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonPanel = new javax.swing.JPanel();
        exitButton = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        listScroll = new javax.swing.JScrollPane();
        productList = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowClose(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonPressed(evt);
            }
        });

        buttonPanel.add(exitButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        listPanel.setLayout(new java.awt.GridBagLayout());

        listPanel.setBorder(new javax.swing.border.TitledBorder(null, "Known installations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10)));
        productList.setModel(Kernel.datamodel);
        productList.setPrototypeCellValue("Netscape Communicator 6.00 Preview Release 10");
        productList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listScroll.setViewportView(productList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        listPanel.add(listScroll, gridBagConstraints);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        listPanel.add(addButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        listPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonPresssed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        listPanel.add(removeButton, gridBagConstraints);

        getContentPane().add(listPanel, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

	 private void windowClose(java.awt.event.WindowEvent evt)//GEN-FIRST:event_windowClose
	 {//GEN-HEADEREND:event_windowClose
		 setVisible(false);
		 dispose();

	 }//GEN-LAST:event_windowClose

	 private void removeButtonPresssed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeButtonPresssed
	 {//GEN-HEADEREND:event_removeButtonPresssed
		 Product prod = (Product) productList.getSelectedValue();
		 if (prod != null)
		 {
			 int result = JOptionPane.showConfirmDialog(this, "Really remove: " + prod, "Remove Installation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			 if (result == JOptionPane.OK_OPTION)
			 {
				 Kernel.datamodel.removeProduct(prod);
			 }
		 }
	 }//GEN-LAST:event_removeButtonPresssed

	 private void editButtonPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editButtonPressed
	 {//GEN-HEADEREND:event_editButtonPressed
		 Product prod = (Product) productList.getSelectedValue();
		 if (prod != null) {
			 EditProduct ep = new EditProduct(prod);
			 ep.showDialog();
		 }
	 }//GEN-LAST:event_editButtonPressed

	 private void addButtonPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonPressed
	 {//GEN-HEADEREND:event_addButtonPressed
		 Product prod = new Product("");
		 EditProduct ep = new EditProduct(prod);

		 if (ep.showDialog()) {
			 Kernel.datamodel.addProduct(prod);
		 }
	 }//GEN-LAST:event_addButtonPressed

	 private void exitButtonPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitButtonPressed
	 {//GEN-HEADEREND:event_exitButtonPressed
		 setVisible(false);
		 dispose();
	 }//GEN-LAST:event_exitButtonPressed

	 public void showDialog() {
            okay = false;
            setVisible(true);
            dispose();
	 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel listPanel;
    private javax.swing.JScrollPane listScroll;
    private javax.swing.JList productList;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
	 private boolean okay;
}
