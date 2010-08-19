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

import org.mozillatranslator.kernel.*;
/**
 *
 * @author  henrik
 */
public class ClearMarkTraverse extends EmptyTraverseCommand
{
    
    /** Creates a new instance of ClearMarkTraverse */
    public ClearMarkTraverse()
    {
    }
    
   public boolean action(ProductChild currentNode)
    {
        currentNode.clearMark();
        return true;
    }
    
    public boolean action(Product currentNode)
    {
        currentNode.clearMark();
        return true;        
    }

    
    public boolean action(GenericFile currentNode)
    {
        currentNode.increaseReferenceCount();
        
        currentNode.clearMark();
        return true;        
    }
    
    public boolean action(Translation currentNode)
    {
        currentNode.setMark();
        return true;
    }
    
    public boolean action(Phrase currentNode)
    {
        currentNode.clearMark();
        return true;        
    }
    
    public boolean action(Component currentNode)
    {
        currentNode.clearMark();
        return true;        
    }
    
    public void postop(GenericFile currentNode)
    {
        currentNode.decreaseReferenceCount();
    }
    
}
