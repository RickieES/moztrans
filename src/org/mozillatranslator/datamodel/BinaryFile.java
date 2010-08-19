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

package org.mozillatranslator.datamodel;

import org.mozillatranslator.io.file.FileToXmlAccess;
import org.mozillatranslator.io.file.FileAccess;
import org.mozillatranslator.io.file.BinaryAccess;
import org.mozillatranslator.io.common.*;

public class BinaryFile extends MozFile {
    private static FileAccess binaryaccess = new BinaryAccess();
    private static FileToXmlAccess xmlAccess = new FileToXmlAccess();
    private byte[] binaryContent;
    private String md5Hash;
    /** Holds value of property translatedContent. */
    private byte[] translatedContent;
    
    BinaryFile(String n, TreeNode p) {
        super(n, p);
        licenseFile = null;
    }
    
    public void load(ImportExportDataObject dataObject) {
        super.load(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                binaryaccess.load(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.load(dataObject);
                break;
        }
    }
    
    public void save(ImportExportDataObject dataObject) {
        super.save(dataObject);
        
        switch (dataObject.getFormat()) {
            case ImportExportDataObject.FORMAT_MOZILLA:
                binaryaccess.save(dataObject);
                break;
            case ImportExportDataObject.FORMAT_XML:
                xmlAccess.save(dataObject);
                break;
        }
    }
    
    
    /** Getter for property binaryContent.
     * @return Value of property binaryContent.
     */
    public byte[] getBinaryContent() {
        return this.binaryContent;
    }
    
    /** Setter for property binaryContent.
     * @param binaryContent New value of property binaryContent.
     */
    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }
    
    /** Getter for property translatedContent.
     * @return Value of property translatedContent.
     */
    public byte[] getTranslatedContent() {
        return this.translatedContent;
    }
    
    /** Setter for property translatedContent.
     * @param translatedContent New value of property translatedContent.
     */
    public void setTranslatedContent(byte[] translatedContent) {
        this.translatedContent = translatedContent;
    }
    
    public String getTypeName() {
        return "Binary";
    }

    public String getMd5Hash() {
        return (md5Hash == null) ? "" : md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }
}
