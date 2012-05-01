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

package org.mozillatranslator.dataobjects;

import java.io.File;
import java.util.List;
import org.mozillatranslator.datamodel.TreeNode;

/**
 *
 * @author  Henrik Lynggaard
 */
public class ImportExportDataObject implements FileDataObject {
    public static final int FORMAT_MOZILLA = 0;
    public static final int FORMAT_XML = 1;
    public static final int FORMAT_PARTIALGLOSSARY = 2;

    private int format;
    private byte[] fileContent;
    private TreeNode node;
    private String fileName;
    private File realFile;
    private String l10n;
    private List changeList;

    /** Creates a new instance of ImportExportDataObject */
    public ImportExportDataObject() {
    }

    public int getFormat() {
        return this.format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public TreeNode getNode() {
        return this.node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public File getRealFile() {
        if ((this.getFileName() != null) && (this.realFile == null)) {
            realFile = new File(this.getFileName());
        }
        return realFile;
    }

    public String getL10n() {
        return this.l10n;
    }

    public void setL10n(String l10n) {
        this.l10n = l10n;
    }

    public List getChangeList() {
        return this.changeList;
    }

    public void setChangeList(List changeList) {
        this.changeList = changeList;
    }
}
