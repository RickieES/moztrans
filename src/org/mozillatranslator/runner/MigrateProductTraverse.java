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

package org.mozillatranslator.runner;

import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
import java.util.Stack;

/** This traverse class is used to migrate one product into another
 * @author Henrik Lynggaard
 **/
public class MigrateProductTraverse extends EmptyTraverseCommand {
    private Product destProduct;
    private ProductChild destProductChild;
    private Stack componentStack;
    private GenericFile destFile;
    private Phrase destPhrase;
    private String l10n;
    
    
    /** Creates a new instance of EmptyTraverseCommand
     * @param dest The destination product
     * @param l10n The locale to be migrated
     **/
    public MigrateProductTraverse(Product dest, String l10n) {
        destProduct = dest;
        this.l10n = l10n;
        componentStack = new Stack();
    }
    
    /** This is called by the traverse when a product child is
     * encountered. It tries to find a product child with the
     * same name in the destination product
     *
     * @param currentNode The source product child
     * @return true if a match is found in the destination, otherwise false
     **/
    @Override
    public boolean action(ProductChild currentNode) {
        destProductChild = (destProduct != null) ? (ProductChild)
                destProduct.getChildByName(currentNode.getName()) : null;
        
        return (destProductChild != null);
    }
    
    /** This is called by the traverse when a component is
     * encountered. It tries to find a component with the
     * same name in the destination product
     *
     * @param currentNode The current component
     * @return true if a match is found in the destination, otherwise false
     **/
    @Override
    public boolean action(Component currentNode) {
        Component newLevel = null;
        
        // If we are in the initial level under ProductChild
        if (componentStack.empty()) {
            // If the ProductChild exists in the destination product...
            if (destProductChild != null) {
                // ...and the component too
                newLevel = (Component) destProductChild.getChildByName(currentNode.getName());
                if (newLevel != null) {
                    componentStack.push(newLevel);
                }
            }
        } else {
            // If a component with the same name exists in the destination product
            // as a child of the last pushed component to the stack
            newLevel = (Component) ((Component) componentStack.peek()).getChildByName(
                    currentNode.getName());
            if (newLevel != null) {
                componentStack.push(newLevel);
            }
        }
        return (newLevel != null) ?
                newLevel.getName().equals(currentNode.getName()) : false;
    }
    
    /** This is called by the traverse when a file is
     * encountered. It tries to find a file with the
     * same name in the destination product
     *
     * @param currentNode the current file
     * @return true if a match is found in the destination, otherwise false
     **/
    @Override
    public boolean action(GenericFile currentNode) {
        Component parentOrigComp = (Component) currentNode.getParent();
        Component curDestComp = (Component) componentStack.peek();
        
        if ((curDestComp != null) && (parentOrigComp.getName().equals(curDestComp.getName()))) {
            destFile = (GenericFile) curDestComp.getChildByName(currentNode.getName());
            currentNode.increaseReferenceCount();
            if (destFile != null) {
                destFile.increaseReferenceCount();
            }
        } else {
            destFile = null;
        }
        return (destFile != null);
    }
    
    /** This is called by the traverse when a phrase is
     * encountered. It tries to find a phrase with the
     * same name in the destination product
     *
     * @param currentNode The currentPhrase
     * @return true if a match is found in the destination, otherwise false
     **/
    @Override
    public boolean action(Phrase currentNode) {
        Translation sourceTranslation, destTranslation;
        
        destPhrase = (destFile != null) ? (Phrase)
                destFile.getChildByName(currentNode.getName()) : null;
        
        if (destPhrase != null) {
            destPhrase.setKeepOriginal(currentNode.isKeepOriginal());
            sourceTranslation = (Translation) currentNode.getChildByName(l10n);
            
            if (sourceTranslation != null) {
                destTranslation = (Translation) destPhrase.getChildByName(l10n);
                
                if (destTranslation == null) {
                    destTranslation = new Translation(l10n, destPhrase,
                            sourceTranslation.getText(),
                            sourceTranslation.getStatus());
                    destPhrase.addChild(destTranslation);
                }
            }
        }
        return true;
    }
    
    /** This is called after all the children of a Component have been
     * passed. It is used to remove the component from the Component stack
     *
     * @param currentNode The current Component
     **/
    @Override
    public void postop(Component currentNode) {
        Component thisLevel = (componentStack.empty()) ? null : (Component) componentStack.peek();
        
        if ((thisLevel != null) && (thisLevel.getName().equals(currentNode.getName()))) {
            componentStack.pop();
        }
    }

    /** This is called after all the children of a file have been
     * passed. It is used to clean up afterwards calling decreaseReferenceCount
     *
     * @param currentNode The current file
     **/
    @Override
    public void postop(GenericFile currentNode) {
        currentNode.decreaseReferenceCount();
        if (destFile != null) {
            destFile.decreaseReferenceCount();
        }
    }
}
