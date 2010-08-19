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

package org.mozillatranslator.filter;

import java.util.*;

import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  henrik
 */
public class RedundantPostOpTraverse extends EmptyTraverseCommand
{
    private List collectedList;
    
    /** Creates a new instance of FilterTraverse */
     public RedundantPostOpTraverse(List colList)
    {
        collectedList = colList;
    }
    
    public boolean action(GenericFile currentNode)
    {
        boolean found = false;
        
        Iterator colIterator = collectedList.iterator();
        
        while (!found && colIterator.hasNext())
        {
            TreeNode colNode = (TreeNode) colIterator.next();
            GenericFile colParent = (GenericFile) colNode.getParent();
            if (colParent.getName().equals(currentNode.getName()))
            {            
                found = true;
            }
        }
        if (!found)
        {
            currentNode.decreaseReferenceCount();
        }
            
            
        return true;
    }
   
}
