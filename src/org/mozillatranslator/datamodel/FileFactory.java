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

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class FileFactory {
    public static GenericFile createFile(String name, TreeNode parent) {
        GenericFile result = null;
        if (name.endsWith("dtd") || name.endsWith("ent")) {
            result = new DTDFile(name, parent);
        } else if (name.endsWith("properties") || name.endsWith("ini")) {
            result = new PropertiesFile(name, parent);
        } else if (name.endsWith("gif") || name.endsWith("png")) {
            result = new BinaryFile(name, parent);
        } else {
            result = new PlainFile(name, parent);
        }
        return result;
    }
    
    public static GenericFile changeType(GenericFile currentFile) {
        String valuator;
        GenericFile result = null;
        
        TreeNode parent = currentFile.getParent();
        parent.removeChild(currentFile);
        
        if (currentFile.getRealFilename().equals("")) {
            valuator = currentFile.getName();
        } else {
            valuator = currentFile.getRealFilename();
        }
        
        if (valuator.endsWith("dtd") || valuator.endsWith("ent")) {
            result = new DTDFile(currentFile.getName(), parent);
        } else if (valuator.endsWith("properties")) {
            result = new PropertiesFile(currentFile.getName(), parent);
        } else {
            result = new PlainFile(currentFile.getName(), parent);
        }
        
        result.setRealFilename(currentFile.getRealFilename());
        result.setRelativeFilename(currentFile.getRelativeFilename());
        
        return result;
    }
}
