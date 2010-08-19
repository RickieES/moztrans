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

import java.io.*;
import org.mozillatranslator.io.*;
/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface ProductChild extends TreeNode
{
    
    public static final int TYPE_NEUTRAL = 1;
    public static final int TYPE_WINDOWS = 2;
    public static final int TYPE_UNIX = 3;
    public static final int TYPE_MAC = 4;
    public static final int TYPE_REGION = 5;
    public static final int TYPE_CUSTOM = 6;
    public static final int TYPE_OTHER = 100;
    
    public int getType();
    
    public String getTypeName();
    
    
    public void load(ProductChildInputOutputDataObject dataObject) throws IOException;
    
    public void save(ProductChildInputOutputDataObject dataObject)  throws IOException;
    
    public String getJarFile();
    
    public String getJarInXpiFile(String l10n);
    
    public String getLocaleDisplay(String l10n);
    
    
    
    
    
}

