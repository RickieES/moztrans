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

import org.mozillatranslator.io.common.ImportExportDataObject;

/**
 * General interface for all files in datamodel
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface GenericFile extends TreeNode {

    public String getRealFilename();

    public void setRealFilename(String value);

    public String getRelativeFilename();

    public void setRelativeFilename(String value);

    public String getLicenseFile();

    public void setLicenseFile(String file);

    public MozLicense getLicenseBlock();

    public void setLicenseBlock(MozLicense License);

    public void setDontExport(boolean dontExport);

    public boolean isDontExport();

    /**
     * Checks if any of the children Phrase or grandchildren Translation objects
     * has a newer alteredTime value than the file itself (meaning it needs to
     * be exported)
     * @param onlyPhrases if true, it checks just Phrases
     * @return true if a children or grandchildren is newer than the file itself
     */
    public boolean isModified(boolean onlyPhrases);

    /**
     * Checks if any of the children Phrase or grandchildren Translation objects
     * has a newer alteredTime value than the reference time, meaning it needs
     * to be exported
     * @param onlyPhrases if true, it checks just Phrases
     * @param referenceTime the time to which compare the nodes
     * @return true if a children or grandchildren is newer than the file itself
     */
    public boolean isModified(boolean onlyPhrases, long referenceTime);

    public void save(ImportExportDataObject dataObject);

    public void load(ImportExportDataObject dataObject);

    public void decreaseReferenceCount();

    public void increaseReferenceCount();

    public void removeAllChildren();

    public void startRefFromOne();

    public String getTypeName();
}
