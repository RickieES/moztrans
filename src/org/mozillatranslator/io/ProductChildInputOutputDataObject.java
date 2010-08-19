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

import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  Henrik Lynggaard
 */
public class ProductChildInputOutputDataObject implements JarXpiDataObject {
    public static final int FORMAT_JAR = 1;

    private int format;
    private ProductChild productChild;
    private String l10n;
    private String author;
    private String display;
    private String fileName;
    private File realFile;

    /** Holds value of property previewUrl. */
    private String previewUrl;

    /** Holds value of property version. */
    private String version;

    /** Holds value of property localeDisplay. */
    private String localeDisplay;

    /** Holds value of property fileStream. */
    private JarOutputStream jarOutStream;

    /** Holds value of property changeList. */
    private List changeList;

    /** Holds value of property jarInStream. */
    private JarInputStream jarInStream;

    /** Holds value of property buffOutStream. */
    private BufferedOutputStream buffOutStream;

    /** Holds value of property buffInStream. */
    private BufferedInputStream buffInStream;

    /** Marks if external calls to ZIP binaries will be done */
    private boolean useExternalZIP;

    /** Creates a new instance of ProductChildInputOutputDataObject */
    public ProductChildInputOutputDataObject() {
    }

    /** Getter for property format.
     * @return Value of property format.
     */
    public int getFormat() {
        return this.format;
    }

    /** Setter for property format.
     * @param format New value of property format.
     */
    public void setFormat(int format) {
        this.format = format;
    }

    /** Getter for property productChild.
     * @return Value of property productChild.
     */
    public ProductChild getProductChild() {
        return this.productChild;
    }

    /** Setter for property productChild.
     * @param productChild New value of property productChild.
     */
    public void setProductChild(ProductChild productChild) {
        this.productChild = productChild;
    }

    /** Getter for locale code (i.e.: ab-CD)
     * @return Value of property l10n
     */
    public String getL10n() {
        return this.l10n;
    }

    /** Setter for property locale code (i.e.: ab-CD)
     * @param l10n New value of property l10n.
     */
    public void setL10n(String l10n) {
        this.l10n = l10n;
    }

    /** Getter for property author.
     * @return Value of property author.
     */
    public String getAuthor() {
        return this.author;
    }

    /** Setter for property author.
     * @param author New value of property author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /** Getter for property display.
     * @return Value of property display.
     */
    public String getDisplay() {
        return this.display;
    }

    /** Setter for property display.
     * @param display New value of property display.
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /** Getter for property realFile.
     * @return Value of property realFile.
     */
    public File getRealFile() {
        if ((this.getFileName() != null) && (this.realFile == null)) {
            realFile = new File(this.getFileName());
        }
        return realFile;
    }

    /** Setter for property realFile.
     * @param realFile New value of property realFile.
     */
    public void setRealFile(File realFile) {
        this.realFile = realFile;
    }

    /** Getter for property previewUrl.
     * @return Value of property previewUrl.
     */
    public String getPreviewUrl() {
        return this.previewUrl;
    }

    /** Setter for property previewUrl.
     * @param previewUrl New value of property previewUrl.
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /** Getter for property version.
     * @return Value of property version.
     */
    public String getVersion() {
        return this.version;
    }

    /** Setter for property version.
     * @param version New value of property version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getJarFileInXpi() {
        return productChild.getJarInXpiFile(l10n);
    }

    /** Getter for property localeDisplay.
     * @return Value of property localeDisplay.
     */
    public String getLocaleDisplay() {
        return productChild.getLocaleDisplay(l10n);
    }

    /** Getter for property fileStream.
     * @return Value of property fileStream.
     */
    public JarOutputStream getJarOutStream() {
        return this.jarOutStream;
    }

    /** Setter for property fileStream.
     * @param fileStream New value of property fileStream.
     */
    public void setJarOutStream(JarOutputStream fileStream) {
        this.jarOutStream = fileStream;
    }

    /** Getter for property changeList.
     * @return Value of property changeList.
     */
    public List getChangeList() {
        return this.changeList;
    }

    /** Setter for property changeList.
     * @param changeList New value of property changeList.
     */
    public void setChangeList(List changeList) {
        this.changeList = changeList;
    }

    /** Getter for property jarInStream.
     * @return Value of property jarInStream.
     */
    public JarInputStream getJarInStream() {
        return this.jarInStream;
    }

    /** Setter for property jarInStream.
     * @param jarInStream New value of property jarInStream.
     */
    public void setJarInStream(JarInputStream jarInStream) {
        this.jarInStream = jarInStream;
    }

    /** Getter for property buffOutStream.
     * @return Value of property buffOutStream.
     */
    public BufferedOutputStream getBuffOutStream() {
        return this.buffOutStream;
    }

    /** Setter for property buffOutStream.
     * @param buffOutStream New value of property buffOutStream.
     */
    public void setBuffOutStream(BufferedOutputStream buffOutStream) {
        this.buffOutStream = buffOutStream;
    }

    /** Getter for property buffInStream.
     * @return Value of property buffInStream.
     */
    public BufferedInputStream getBuffInStream() {
        return this.buffInStream;
    }

    /** Setter for property buffInStream.
     * @param buffInStream New value of property buffInStream.
     */
    public void setBuffInStream(BufferedInputStream buffInStream) {
        this.buffInStream = buffInStream;
    }

    public boolean isUseExternalZIP() {
        return useExternalZIP;
    }

    public void setUseExternalZIP(boolean useExternalZIP) {
        this.useExternalZIP = useExternalZIP;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        if ((this.realFile != null) && (this.fileName != null) &&
                (this.realFile.getPath().compareTo(fileName) != 0)) {
            realFile = null;
        }
    }
}
