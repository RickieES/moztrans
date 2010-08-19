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


package org.mozillatranslator.kernel;

import org.mozillatranslator.datamodel.*;
/** This traverse interface is used by the datamodel to traverse it
 * @author Henrik Lynggaard
 */
public interface TraverseCommand
{
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(TreeNode currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(DataModel currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(Product currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(ProductChild currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(Component currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(GenericFile currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(Phrase currentNode);
    
    /** This is called before the children is passed,
     * and it is here the traver should do its main part.
     * It must return true if the traverse is to go deeper,
     * or false otherwise
     * @param currentNode The current node being passed
     * @return true if the traverse should go deeper, false otherwise */    
    public boolean action(Translation currentNode);
    
    /* 
     * Post children methods
     */
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(TreeNode currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(DataModel currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(Product currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(ProductChild currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(Component currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(GenericFile currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(Phrase currentNode);
    
    /** This is called after all the children has been passed,
     * it is used to clean up.
     * @param currentNode The current node being passed */    
    public void postop(Translation currentNode);
    
}
