/*
 * FileDataObject.java
 *
 * Created on 14 de diciembre de 2006, 20:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mozillatranslator.dataobjects;

import java.io.File;

/** This interface marks the DataObject implementations using a java.io.File,
 *  such as a JAR, a XPI or a ZIP file in the disk
 *
 * @author rpalomares
 */
public interface FileDataObject extends DataObject {
    String getFileName();
    void setFileName(String fileName);

    File getRealFile();
}
