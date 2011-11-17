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
 * Portions created by Ricardo Palomares are
 * Copyright (C) Ricardo Palomares.
 * All Rights Reserved.
 *
 * Contributor(s):
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.datamodel;

import org.mozillatranslator.kernel.*;

/**
 * Traverse commmand for clearing the Result Comment in all Phrase items
 *
 * @author rpalomares
 */
public class ClearResultComment extends EmptyTraverseCommand {
    
    /** Creates a new instance of ClearResultComment */
    public ClearResultComment() {
    }
    
    /**
     * Clears the Result Comment field on the phrase passed as a parameter
     *
     * @param currentNode   the phrase whose Result Comment is to be cleared
     * @return true if the action has completed sucessfully
     */
    @Override
    public boolean action(Phrase currentNode) {
        currentNode.setFilterResult("");
        return true;        
    }
}
