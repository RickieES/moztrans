/*
 * TranslatedImagePanel2.java
 * 
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
 * Tsahi Asher <tsahi_75@yahoo.com>
 *
 * Created on 10 December 2006, 02:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mozillatranslator.gui;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.mozillatranslator.datamodel.BinaryFile;
import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.kernel.Kernel;

/**
 * extends ImagePanel to add a few buttons relevant for the translated image
 *panel in EditImagePanel.
 *
 * @author Tsahi Asher
 */
public class TranslatedImagePanel extends ImagePanel {
  /**
   * the original image loaded from glossary
   */
  protected byte[] presetImage;

  /**
   * Creates a new instance of TranslatedImagePanel2
   * @param image a byte array of the data of the image to display
   */
  public TranslatedImagePanel(byte[] image) {
    super(image);
    presetImage = image;
    initComponents();
    if (imageData == null) {
      clearButton.setEnabled(false);
    }
  }
  
  /**
   * get the data of the translated image
   * @return the translated image data
   */
  public byte[] getImageData() {
    return imageData;
  }
  
  /**this is needed because of netBeans inability to design a form that inherits
   *components already placed on a parent class.
   */
  private void initComponents() {
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveButtonActionPerformed(e);
      }
    });
    
    loadButton = new JButton("Load");
    loadButton.setFont(new Font("Dialog", Font.PLAIN, 12));
    loadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadButtonActionPerformed(e);
      }
    });
    buttonsPanel.add(loadButton, 0);
    
    clearButton = new JButton("Clear");
    clearButton.setFont(new Font("Dialog", Font.PLAIN, 12));
    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clearButtonActionPerformed(e);
      }
    });
    buttonsPanel.add(clearButton);
    
    resetButton = new JButton("Cancel");
    resetButton.setFont(new Font("Dialog", Font.PLAIN, 12));
    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resetButtonActionPerformed(e);
      }
    });
    buttonsPanel.add(resetButton);
    
    titleLabel.setText("Translated Image");
  }
  
  /**
   *loadButton ActionPerformed (click) event handler
   *@param evt event
   */
  private void loadButtonActionPerformed(ActionEvent evt) {
    File fileSelected;
    JFileChooser chooser = new JFileChooser(".");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Load translated picture");
    int result = chooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        fileSelected = chooser.getSelectedFile();
        FileInputStream fis = new FileInputStream(fileSelected);
        imageData = FileUtils.loadFile(fis);
        fis.close();
      }
      catch (IOException e) {
        Kernel.appLog.severe("Cannot save original picture " + e);
        JOptionPane.showMessageDialog(Kernel.mainWindow, "Cannot load translated" +
          " picture " + e, "Error during load", JOptionPane.ERROR_MESSAGE);
      }
      if (imageData != null) {
        setImage(imageData);
        clearButton.setEnabled(true);
      }
      else {
        removeImage();
      }
    }
  }

  /**
   *event handler for the save button inherited from ImagePanel.
   *massively redundant code from ImagePanel.saveButtonActionPerformed. the only
   *difference is the change from "original" to "translated".
   *@param evt event
   */
  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (imageData != null) {
      File fileSelected;
      JFileChooser chooser = new JFileChooser(".");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setDialogTitle("Save translated picture");
      int result = chooser.showSaveDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
        try {
          fileSelected = chooser.getSelectedFile();
          FileOutputStream fos = new FileOutputStream(fileSelected);
          FileUtils.saveFile(fos, imageData);
          fos.close();
        }
        catch (IOException e) {
          Kernel.appLog.severe("Cannot save translated picture " + e);
          JOptionPane.showMessageDialog(Kernel.mainWindow, "Cannot save" +
            " translated picture " + e, "Error during save",
            JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
 
  /**
   *event handler for clearButton click
   *@param evt event
   */
  private void clearButtonActionPerformed(ActionEvent evt) {
    int res = JOptionPane.showConfirmDialog(this, "If you clear the translated " +
      "image, you will have to load it again in order to restore it. Are you sure?",
      "Clear Translated Image?", JOptionPane.OK_CANCEL_OPTION);
    if (res == JOptionPane.OK_OPTION) {
      removeImage();
      clearButton.setEnabled(false);
    }
  }
  
  /**
   *event handler for resetButton click
   *@param evt event
   */
  private void resetButtonActionPerformed(ActionEvent evt) {
    if (presetImage != null) {
      setImage(presetImage);
      clearButton.setEnabled(true);
    }
    else {
      removeImage();
    }
  }
  
  //gui vars
  private JButton loadButton; //loads image from disk
  private JButton clearButton; //clears the translated image
  private JButton resetButton; //resets the translated image to image loaded before
                               //editing started
}
