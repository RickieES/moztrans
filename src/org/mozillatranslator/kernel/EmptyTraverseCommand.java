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

/** this is a emtpy implementation of the traverse interface
 * @author henrik */
public class EmptyTraverseCommand implements TraverseCommand {

    /** Creates a new instance of EmptyTraverseCommand */
    public EmptyTraverseCommand() {
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(TreeNode currentNode) {
        boolean result = true;
        int level = currentNode.getLevel();

        switch (level) {
            case TreeNode.LEVEL_MODEL:
                result = action((DataModel) currentNode);
                break;
            case TreeNode.LEVEL_PRODUCT:
                result = action((Product) currentNode);
                break;
            case TreeNode.LEVEL_PRODUCTCHILD:
                result = action((ProductChild) currentNode);
                break;
            case TreeNode.LEVEL_COMPONENT:
                result = action((Component) currentNode);
                break;
            case TreeNode.LEVEL_FILE:
                result = action((GenericFile) currentNode);
                break;
            case TreeNode.LEVEL_PHRASE:
                result = action((Phrase) currentNode);
                break;
            case TreeNode.LEVEL_TRANSLATION:
                result = action((Translation) currentNode);
                break;
        }
        return result;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(ProductChild currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(DataModel currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(Product currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(GenericFile currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(Translation currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @return true
     * @param currentNode The current node passed by the traverse */
    @Override
    public boolean action(Phrase currentNode) {
        return true;
    }

    /** This emtpy implementation just returns true
     * @param currentNode The current node passed by the traverse
     * @return true */
    @Override
    public boolean action(Component currentNode) {
        return true;
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(TreeNode currentNode) {
        int level = currentNode.getLevel();

        switch (level) {
            case TreeNode.LEVEL_MODEL:
                postop((DataModel) currentNode);
                break;
            case TreeNode.LEVEL_PRODUCT:
                postop((Product) currentNode);
                break;
            case TreeNode.LEVEL_PRODUCTCHILD:
                postop((ProductChild) currentNode);
                break;
            case TreeNode.LEVEL_COMPONENT:
                postop((Component) currentNode);
                break;
            case TreeNode.LEVEL_FILE:
                postop((GenericFile) currentNode);
                break;
            case TreeNode.LEVEL_PHRASE:
                postop((Phrase) currentNode);
                break;
            case TreeNode.LEVEL_TRANSLATION:
                postop((Translation) currentNode);
                break;
        }
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(ProductChild currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(DataModel currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(Product currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(GenericFile currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(Translation currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(Phrase currentNode) {
    }

    /** This empty implementation does nothing
     * @param currentNode The current node passed by the traverse */
    @Override
    public void postop(Component currentNode) {
    }
}
