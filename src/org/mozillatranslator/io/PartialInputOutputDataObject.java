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
 * Ricardo Palomares (refactoring to remove subcomponent from the datamodel)
 *
 */


package org.mozillatranslator.io;

import java.io.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  Henrik Lynggaard
 */
public class PartialInputOutputDataObject {
    
    /** Holds value of property format. */
    private int format;
    
    /** Holds value of property component. */
    private Component component;
    
    /** Holds value of property fileName. */
    private File fileName;
    
    /** Creates a new instance of PartialInputOutputDataObject */
    public PartialInputOutputDataObject() {
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
    
    /** Getter for property component.
     * @return Value of property component.
     */
    public Component getComponent() {
        return this.component;
    }
    
    /** Setter for property component.
     * @param component New value of property component.
     */
    public void setComponent(Component component) {
        this.component = component;
    }
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public File getFileName() {
        return this.fileName;
    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(File fileName) {
        this.fileName = fileName;
    }
    
}
