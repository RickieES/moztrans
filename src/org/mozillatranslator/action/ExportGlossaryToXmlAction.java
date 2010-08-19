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
package org.mozillatranslator.action;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.mozillatranslator.io.common.XmlExporter;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import org.mozillatranslator.kernel.*;
import org.w3c.dom.DOMException;

/**
 *
 * @author Henrik Lynggaard
 * @version 1.00
 */
public class ExportGlossaryToXmlAction extends AbstractAction {
    /** Creates new AboutAction */
    public ExportGlossaryToXmlAction() {
        super(Kernel.translate("menu.export.xml.glossary.label"), null);
    }
    
    /** this gets called when the event happends
     * @param evt the event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        FileOutputStream fos;
        OutputStreamWriter osw;
        BufferedWriter bw;
        File selectedFile;
        
        JFileChooser chooser;
        File f = new File(Kernel.settings.getString(Settings.GUI_EXPORT_FILE_CHOOSER_PATH));
        chooser = new JFileChooser(f);
        
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select file to export into");
        
        int result = chooser.showSaveDialog(Kernel.mainWindow);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Kernel.settings.setString(Settings.GUI_EXPORT_FILE_CHOOSER_PATH,
                        chooser.getCurrentDirectory().getAbsolutePath());
                selectedFile = chooser.getSelectedFile();
                fos = new FileOutputStream(selectedFile);
                osw = new OutputStreamWriter(fos, "UTF-8");
                bw = new BufferedWriter(osw);
                
                XmlExporter.saveGlossary(bw);
                bw.close();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DOMException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExportGlossaryToXmlAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
