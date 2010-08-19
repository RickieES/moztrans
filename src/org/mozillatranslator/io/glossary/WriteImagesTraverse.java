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


package org.mozillatranslator.io.glossary;

import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
import java.util.zip.*;
/**
 *
 * @author  henrik
 */
public class WriteImagesTraverse extends EmptyTraverseCommand {
    private ZipOutputStream zos;
    private String l10n;
    
    /** Creates a new instance of WriteImagesTraverse */
    public WriteImagesTraverse(ZipOutputStream zos) {
        this.zos = zos;
    }
    
    public boolean action(GenericFile currentNode) {
        String fileName = "";
        BinaryFile binFile = null;
        
        // String[] parentList = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] parentList = {"", "", "", "", "", "", "", "", "", ""};
        if (currentNode instanceof BinaryFile) {
            try {
                binFile = (BinaryFile) currentNode;
                binFile.fillParentArray(parentList);
                
                for (int i = TreeNode.LEVEL_PRODUCT; i < TreeNode.LEVEL_PHRASE; i++) {
                    if (i < TreeNode.LEVEL_FILE) {
                        fileName += ((parentList[i].startsWith("/")) ? "" : "/") + parentList[i];
                    } else {
                        fileName += "/" + parentList[i];
                    }
                }
                ZipEntry ze = new ZipEntry(fileName + "_original");
                zos.putNextEntry(ze);
                FileUtils.saveFile(zos, binFile.getBinaryContent());
                if (binFile.getTranslatedContent() != null) {
                    ze = new ZipEntry(fileName + "_translated");
                    zos.putNextEntry(ze);
                    FileUtils.saveFile(zos, binFile.getTranslatedContent());
                }
            } catch (Exception e) {
                Kernel.appLog.severe("Error writing image " + binFile.getName() + "\n" + e);
            }
        }
        return true;
    }
}
