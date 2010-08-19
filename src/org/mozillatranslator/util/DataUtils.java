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


package org.mozillatranslator.util;

import java.util.*;

import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
/** This class is a utility class for the datamodel
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class DataUtils
{
    
    
    /** This will return a complete list of platforms and regions
     * in all the products
     * @return a list of regions and platforms
     */    
    public static List fillWithPors()
    {
        List theList = new ArrayList();
        
        FillwithPorrTraverse trav = new FillwithPorrTraverse(theList);
        Kernel.datamodel.traverse(trav, TreeNode.LEVEL_PRODUCTCHILD);
       
        return theList;
    }
    
    /** This will return a list of possible types of platforms
     * @return a list of platform types
     */    
    public static List fillWithTypes()
    {
        KeyValuePair pair;
        List theList = new ArrayList();
        
        pair = new KeyValuePair(new Integer(ProductChild.TYPE_WINDOWS), Kernel.translate("Windows"));
        theList.add(pair);
        
        pair = new KeyValuePair(new Integer(ProductChild.TYPE_UNIX), Kernel.translate("Unix"));
        theList.add(pair);
        
        pair = new KeyValuePair(new Integer(ProductChild.TYPE_MAC), Kernel.translate("Mac"));
        theList.add(pair);
        
        pair = new KeyValuePair(new Integer(ProductChild.TYPE_OTHER), Kernel.translate("Other"));
        theList.add(pair);
       
        return theList;
    }
}
