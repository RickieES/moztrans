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


import org.mozillatranslator.io.common.*;
/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface GenericFile extends TreeNode
{

    public String getRealFilename();

    public void setRealFilename(String value);
    
    public String getRelativeFilename();    

    public void setRelativeFilename(String value);
    
    public String getLicenseFile();
    
    public void setLicenseFile(String file);
    
    public MozLicense getLicenseBlock();
    
    public void setLicenseBlock(MozLicense License);
    
    public void save(ImportExportDataObject dataObject);
    
    public void load(ImportExportDataObject dataObject);
    
    public void decreaseReferenceCount();
    
    public void increaseReferenceCount();
    
    public void removeAllChildren();
    
    public void startRefFromOne();
    
    public String getTypeName();
    
    
}

