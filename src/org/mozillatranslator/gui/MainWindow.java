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

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.*;
import org.mozillatranslator.action.*;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.util.GuiTools;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class MainWindow extends javax.swing.JFrame {

    /** Creates new form MainWindow */
    public MainWindow() {
        JMenuItem aMenuItem;
        JMenu exportXmlMenu;
        JMenu qaMenu;
        
        initComponents();
       addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        setTitle("MozillaTranslator, Version " +
                Kernel.settings.getString("system.version", "(Unknown version)"));

        // File menu
        aMenuItem = addMenuItem(fileMenu, new ManageProductsAction(), "ctrl M");
        aMenuItem = addMenuItem(fileMenu, new UpdateProductAction(), "ctrl U");
        fileMenu.addSeparator();
        aMenuItem = addMenuItem(fileMenu, new SaveDatamodelAction(), "ctrl S");
        aMenuItem = addMenuItem(fileMenu, new SaveSettingsAction(), "ctrl T");
        fileMenu.addSeparator();
        aMenuItem = addMenuItem(fileMenu, new  SetupAction(), null);
        fileMenu.addSeparator();
        aMenuItem = addMenuItem(fileMenu, new  QuitAction(), "ctrl Q");

        // Import menu
        aMenuItem = addMenuItem(importMenu, new ImportComponentAction(), null);
        aMenuItem = addMenuItem(importMenu, new ImportFileAction(), "ctrl O");
        importMenu.addSeparator();
        aMenuItem = addMenuItem(importMenu, new ImportTranslationAction(), null);
        aMenuItem = addMenuItem(importMenu,new ImportProductTranslationFromCvsAction(), null);
        
        // Export menu
        aMenuItem = addMenuItem(exportMenu, new ExportComponentAction(), null);
        aMenuItem = addMenuItem(exportMenu, new ExportFileAction(), "ctrl L");
        exportMenu.addSeparator();
        aMenuItem = addMenuItem(exportMenu, new WriteXPIAction(), "ctrl I");
        aMenuItem = addMenuItem(exportMenu, new WriteJarAction(), "ctrl J");
        exportMenu.addSeparator();
        exportXmlMenu = new JMenu(Kernel.translate("menu.export.xml.label"));
        aMenuItem = addMenuItem(exportXmlMenu, new ExportFileToXmlAction(), null);
        aMenuItem = addMenuItem(exportXmlMenu, new ExportComponentToXmlAction(), null);
        aMenuItem = addMenuItem(exportXmlMenu, new ExportGlossaryToXmlAction(), null);
        exportMenu.add(exportXmlMenu);
        aMenuItem = addMenuItem(exportMenu, new ExportProductToCvsAction(),null);

        // Edit menu
        aMenuItem = addMenuItem(editMenu, new ChromeViewAction(), "ctrl H");
        editMenu.addSeparator();
        qaMenu = new JMenu(Kernel.translate("menu.edit.qa.label"));
        aMenuItem = addMenuItem(qaMenu, new ViewUntranslated(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewKeepOriginal() , null);
        aMenuItem = addMenuItem(qaMenu, new ViewIdentical(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewCheckVariables(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewCheckKeybinding(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewDontTranslate(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewDifferentEndings(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewDuplicated(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewAllAtOnce(), "ctrl shift A");
        qaMenu.addSeparator();
        aMenuItem = addMenuItem(qaMenu, new ViewUnpaired(), null);
        aMenuItem = addMenuItem(qaMenu, new ViewRedundant(), null);
        aMenuItem = addMenuItem(qaMenu, new TranslationInconsistency(), null);
        aMenuItem = addMenuItem(qaMenu, new SearchSimilar(), null);
        editMenu.add(qaMenu);
        editMenu.addSeparator();
        aMenuItem = addMenuItem(editMenu, new RelinkAction(), null);
        aMenuItem = addMenuItem(editMenu, new ClearFuzzy(this), null);
        aMenuItem = addMenuItem(editMenu, new ViewFuzzy(), null);
        aMenuItem = addMenuItem(editMenu, new ViewBinary(), null);
        editMenu.addSeparator();
        aMenuItem = addMenuItem(editMenu, new SearchAction(), "ctrl F");
        aMenuItem = addMenuItem(editMenu, new AdvancedSearchAction(), "ctrl shift F");
        aMenuItem = addMenuItem(editMenu, new ReplaceAction(), "ctrl R");
        aMenuItem = addMenuItem(editMenu, new EditPhraseAction(), "ctrl E");
        editMenu.addSeparator();
        aMenuItem = addMenuItem(editMenu, new AutoTranslate(), null);

        // Advanced menu
        aMenuItem = addMenuItem(advancedMenu, new MigrateProductAction(), null);
        aMenuItem = addMenuItem(advancedMenu, new AssignLicenseAction(),null);
        aMenuItem = addMenuItem(advancedMenu, new ResetLastModifiedTimeAction(), null);

        // Help menu
        aMenuItem = addMenuItem(helpMenu, new AboutAction(), null);
        loadIcon();
        setIconImage(winIcon);
            setSize(Kernel.settings.getInteger(Settings.GUI_MAIN_WINDOW_WIDTH),
                Kernel.settings.getInteger(Settings.GUI_MAIN_WINDOW_HEIGHT));
        GuiTools.placeFrameAtCenter(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        status = new javax.swing.JProgressBar();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importMenu = new javax.swing.JMenu();
        exportMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        advancedMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeWindow(evt);
            }
        });
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        status.setToolTipText("Current status");
        status.setString("Ready");
        status.setStringPainted(true);
        getContentPane().add(status, java.awt.BorderLayout.SOUTH);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        menuBar.add(fileMenu);

        importMenu.setMnemonic('I');
        importMenu.setText("Import");
        menuBar.add(importMenu);

        exportMenu.setMnemonic('x');
        exportMenu.setText("Export");
        menuBar.add(exportMenu);

        editMenu.setMnemonic('E');
        editMenu.setText("Edit");
        menuBar.add(editMenu);

        advancedMenu.setMnemonic('A');
        advancedMenu.setText("Advanced");
        menuBar.add(advancedMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        setSize(new java.awt.Dimension(664, 392));
    }// </editor-fold>//GEN-END:initComponents

	private void closeWindow(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeWindow
	{//GEN-HEADEREND:event_closeWindow
            QuitAction qa = new QuitAction();
            qa.doExit();
	}//GEN-LAST:event_closeWindow

        private void formComponentResized(java.awt.event.ComponentEvent evt) {
            Kernel.settings.setInteger(Settings.GUI_MAIN_WINDOW_WIDTH, this.getWidth());
            Kernel.settings.setInteger(Settings.GUI_MAIN_WINDOW_HEIGHT, this.getHeight());
        }

        /**
         * Adds an internal frame to the desktop pane (the "canvas" of MT window)
         * @param win the JInternalFrame to add to the desktop pane
         */
        public void addWindow(JInternalFrame win) {
            desktopPane.add(win);
        }

        private JMenuItem addMenuItem(JMenu adMenu, Action action, String key) {
            JMenuItem item;
            item = adMenu.add(action);
            if (key != null) {
                item.setAccelerator(KeyStroke.getKeyStroke(key));
            }
            return item;
        }

        private void loadIcon() {
            URL url = MainWindow.class.getResource("/org/mozillatranslator/resource/mt_icon_small.gif");
            Toolkit tk = Toolkit.getDefaultToolkit();

            Image img = tk.getImage(url);
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
                // TODO: refactor exception handling
                Kernel.appLog.log(Level.SEVERE, "Logging under development", e);
            }
            winIcon = img;
        }

        /**
         * Returns the selected internal frame
         * @return The selected internal JInternalFrame casted as MozFrame
         */
        public MozFrame getInnerFrame() {
            return (MozFrame) desktopPane.getSelectedFrame();
        }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu advancedMenu;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu importMenu;
    private javax.swing.JMenuBar menuBar;
    public javax.swing.JProgressBar status;
    // End of variables declaration//GEN-END:variables

    // Icon
    private Image winIcon;
}
