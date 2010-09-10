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

package org.mozillatranslator.io.file;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class BinaryAccess implements FileAccess {
    // FIXME: the entire binary vs text io needs refactoring
    
    /** Creates new BinaryAccess */
    public BinaryAccess() {
    }
    
    /**
     * Saves the file
     * @param dataObject an object with all needed information to perform the
     *        persistence
     */
    @Override
    public void save(ImportExportDataObject dataObject) {
        // Decide where to put it
        if (dataObject.getL10n().equals(Kernel.ORIGINAL_L10N)) {
            BinaryFile binFile = (BinaryFile) dataObject.getNode();
            dataObject.setFileContent(binFile.getBinaryContent());
        } else {
            BinaryFile binFile = (BinaryFile) dataObject.getNode();
            Phrase binaryPhrase = (Phrase) binFile.getChildByName("MT_UknownFileType");
            if (binFile.getTranslatedContent() != null && !binaryPhrase.isKeepOriginal()) {
                dataObject.setFileContent(binFile.getTranslatedContent());
            } else {
                dataObject.setFileContent(binFile.getBinaryContent());
            }
        }
    }
    
    /**
     * Loads the file
     * @param dataObject an object with all needed information to perform the
     *        persistence
     */
    @Override
    public void load(ImportExportDataObject dataObject) {
        Phrase currentPhrase;
        
        // Decide where to put it
        if (dataObject.getL10n().equals(Kernel.ORIGINAL_L10N)) {
            BinaryFile binFile = (BinaryFile) dataObject.getNode();
            binFile.setBinaryContent(dataObject.getFileContent());
            MessageDigest digest;
            byte[] hash;
            
            try {
                digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(binFile.getBinaryContent());
                hash = digest.digest();
            } catch (NoSuchAlgorithmException ex) {
                hash = new byte[0];
            }
            
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < hash.length; i++) {
                String s = Integer.toHexString(0xFF & hash[i]).toUpperCase();
                sb.append(hash[i] < 0x10 ? "0" : "").append(s);
            }
            
            currentPhrase = (Phrase) binFile.getChildByName("MT_UknownFileType");
            if (currentPhrase == null) {
                currentPhrase = new Phrase("MT_UknownFileType", binFile,
                                           "Unknown filetype!!!");
                binFile.addChild(currentPhrase);
                currentPhrase.setText("Use Binary edit dialog to Translate");
            }
            
            // Has the binary file changed?
            if (!binFile.getMd5Hash().equals(sb.toString())) {
                dataObject.getChangeList().add(currentPhrase);
                binFile.setMd5Hash(sb.toString());
            }
            currentPhrase.setMark();
        } else {
            BinaryFile binFile = (BinaryFile) dataObject.getNode();
            binFile.setTranslatedContent(dataObject.getFileContent());
        }
    }
}
