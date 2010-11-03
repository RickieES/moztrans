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

package org.mozillatranslator.io;

import java.io.File;
import java.util.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class WriteXpiDataObject implements JarXpiDataObject {
    private String localeName;
    private String author;
    private String display;
    private String previewUrl;
    private String version;
    private String fileName;
    private File realFile = null;
    private List neutrals = new ArrayList();
    private List windows = new ArrayList();
    private List macs = new ArrayList();
    private List unixes = new ArrayList();
    private List regions = new ArrayList();
    private List customs = new ArrayList();
    private boolean useExternalZIP;

    /** Creates new WriteJarDataObject */
    public WriteXpiDataObject() {
    }

    /** Getter for property localeName.
     * @return Value of property localeName.
     */
    public String getLocaleName() {
        return this.localeName;
    }

    /** Setter for property localeName.
     * @param localeName New value of property localeName.
     */
    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    /** Getter for property author.
     * @return Value of property author.
     */
    @Override
    public String getAuthor() {
        return this.author;
    }

    /** Setter for property author.
     * @param author New value of property author.
     */
    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    /** Getter for property display.
     * @return Value of property display.
     */
    @Override
    public String getDisplay() {
        return this.display;
    }

    /** Setter for property display.
     * @param display New value of property display.
     */
    @Override
    public void setDisplay(String display) {
        this.display = display;
    }

    /** Getter for property previewUrl.
     * @return Value of property previewUrl.
     */
    @Override
    public String getPreviewUrl() {
        return this.previewUrl;
    }

    /** Setter for property previewUrl.
     * @param previewUrl New value of property previewUrl.
     */
    @Override
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /** Getter for property version.
     * @return Value of property version.
     */
    @Override
    public String getVersion() {
        return this.version;
    }

    /** Setter for property version.
     * @param version New value of property version.
     */
    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    @Override
    public String getFileName() {
        return this.fileName;
    }

    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
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

    /** Getter for property neutrals.
     * @return Value of property neutrals.
     */
    public List getNeutrals() {
        return this.neutrals;
    }

    /** Getter for property windows.
     * @return Value of property windows.
     */
    public List getWindows() {
        return this.windows;
    }

    /** Getter for property macs.
     * @return Value of property macs.
     */
    public List getMacs() {
        return this.macs;
    }

    /** Getter for property unixes.
     * @return Value of property unixes.
     */
    public List getUnixes() {
        return this.unixes;
    }

    /** Getter for property regions.
     * @return Value of property regions.
     */
    public List getRegions() {
        return this.regions;
    }

    /** Getter for property customs.
     * @return Value of property customs.
     */
    public List getCustoms() {
        return this.customs;
    }

    @Override
    public boolean isUseExternalZIP() {
        return useExternalZIP;
    }

    @Override
    public void setUseExternalZIP(boolean useExternalZIP) {
        this.useExternalZIP = useExternalZIP;
    }
}
