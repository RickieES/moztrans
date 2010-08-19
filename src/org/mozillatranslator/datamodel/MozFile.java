/*
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
 * Henrik Lynggaard Hansen (Initial Code)
 *
 */
package org.mozillatranslator.datamodel;

import javax.swing.tree.*;
import org.mozillatranslator.io.common.*;

/**
 * A MozFile is any file under a L10N directory structure
 * @author rpalomares
 */
public abstract class MozFile extends MozTreeNode implements GenericFile {

    /**
     * the original file license, together with the contributor identification
     */
    private MozLicense licenseBlock;
    /**
     * A file containing a license to be prepended when writing the translated
     * file. Obsolete
     */
    protected String licenseFile;
    private String realFilename;
    private String relativeFilename;
    /** Holds value of property referenceCount. */
    private int referenceCount = 0;

    MozFile(String n, TreeNode p) {
        super(n, p, TreeNode.LEVEL_FILE);
        realFilename = "";
        relativeFilename = "";
        licenseFile = "";
    }

    /** This builds thee part of the JTree that this file takes part in
     * @param myself The DefaultMutableTreeNode that holds this file
     */
    public void buildTree(DefaultMutableTreeNode myself) {
        // do nothing as there is no children
    }

    /**
     * Getter for real filename property
     * @return a string with the real filename
     */
    @Override
    public String getRealFilename() {
        return realFilename;
    }

    @Override
    public void setRealFilename(String value) {
        realFilename = value;
        touch(); // Update last modified time on changing the real filename
    }

    @Override
    public String getRelativeFilename() {
        return relativeFilename;
    }

    @Override
    public void setRelativeFilename(String value) {
        relativeFilename = value;
        touch(); // Update last modified time on changing the relative filename
    }

    @Override
    public String getLicenseFile() {
        return this.licenseFile;
    }

    @Override
    public void setLicenseFile(String file) {
        this.licenseFile = file;
        touch(); // Updates last modified time on changing the license file
    }

    @Override
    public MozLicense getLicenseBlock() {
        return this.licenseBlock;
    }

    @Override
    public void setLicenseBlock(MozLicense license) {
        this.licenseBlock = license;
    }

    @Override
    public void save(ImportExportDataObject dataObject) {
        dataObject.setNode(this);
    }

    @Override
    public void load(ImportExportDataObject dataObject) {
        dataObject.setNode(this);
    }

    /** Getter for property referenceCount.
     */
    @Override
    public void increaseReferenceCount() {
        referenceCount++;
    }

    /** Setter for property referenceCount.
     */
    @Override
    public void decreaseReferenceCount() {

        referenceCount--;

        if (referenceCount < 0) {
            referenceCount = 0;
        }
    }

    @Override
    public void removeAllChildren() {
        children.clear();
    }

    public void setDirty() {
    }

    @Override
    public void startRefFromOne() {
        referenceCount = 1;
    }
}
