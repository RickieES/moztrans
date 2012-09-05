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

import java.util.ArrayList;
import java.util.List;
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.kernel.EmptyTraverseCommand;

/**
 *
 * @author  henrik
 */
public class FilterTraverse extends EmptyTraverseCommand {
    private Filter currentFilter;
    // private GenericFile currentFile;
    private boolean currentFileDirty;
    private List<Phrase> resultList;
    
    /** Creates a new instance of FilterTraverse
     * @param currentFilter The filter that is being executed
     */
    public FilterTraverse(Filter currentFilter) {
        this.currentFilter = currentFilter;
        resultList = new ArrayList<Phrase>();
    }
    
    @Override
    public boolean action(GenericFile currentNode) {
        currentNode.increaseReferenceCount();
        currentFileDirty = false;
        return true;
    }
    
    @Override
    public boolean action(Phrase currentNode) {
        if (currentFilter.check(currentNode)) {
            resultList.add(currentNode);
            currentFileDirty = true;
        }
        return true;
    }
    
    public List<Phrase> getResultList() {
        return resultList;
    }
    
    @Override
    public void postop(GenericFile currentNode) {
        if (!currentFileDirty) {
            currentNode.decreaseReferenceCount();
        }
    }
}
