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

/** This class is a subclass of the traverse Class.
 * It is used by the traverse function to fill
 * a list with the names of the product childs
 * @author Henrik Lynggaard */
public class FillwithPorrTraverse extends EmptyTraverseCommand
{
    private List theList;
    
    /** Creates a new instance of FilterTraverse
     * @param colList The list to fill with the names */
    public FillwithPorrTraverse(List colList)
    {
        theList = colList;
    }
    
    /** This is called by the traverser when it passed a product
     * child.
     * @param currentNode The current Product child
     * @return true, meaning tha the traverseing should go
     * deeper if needed */    
    public boolean action(ProductChild currentNode)
    {
        String value;
        
        value = currentNode.getParent().getName() + " >> " + currentNode.getTypeName();
        KeyValuePair pair = new KeyValuePair(currentNode, value);
        theList.add(pair);
        return true;        
    }
}
