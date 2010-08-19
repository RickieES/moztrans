/*
 * JarXpiDataObject.java
 *
 * Created on 14 de diciembre de 2006, 18:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mozillatranslator.io;

import org.mozillatranslator.kernel.DataObject;

/**
 *
 * @author rpalomares
 */
public interface JarXpiDataObject extends FileDataObject {
    /**
     * Getter for property author.
     * 
     * @return Value of property author.
     */
    String getAuthor();

    /**
     * Getter for property display.
     * 
     * @return Value of property display.
     */
    String getDisplay();

    /**
     * Getter for property previewUrl.
     * 
     * @return Value of property previewUrl.
     */
    String getPreviewUrl();

    /**
     * Getter for property version.
     * 
     * @return Value of property version.
     */
    String getVersion();

    boolean isUseExternalZIP();

    /**
     * Setter for property author.
     * 
     * @param author New value of property author.
     */
    void setAuthor(String author);

    /**
     * Setter for property display.
     * 
     * @param display New value of property display.
     */
    void setDisplay(String display);

    /**
     * Setter for property previewUrl.
     * 
     * @param previewUrl New value of property previewUrl.
     */
    void setPreviewUrl(String previewUrl);

    void setUseExternalZIP(boolean useExternalZIP);

    /**
     * Setter for property version.
     * 
     * @param version New value of property version.
     */
    void setVersion(String version);
}
