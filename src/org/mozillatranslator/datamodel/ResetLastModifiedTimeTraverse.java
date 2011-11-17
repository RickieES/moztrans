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

import org.mozillatranslator.kernel.EmptyTraverseCommand;
import org.mozillatranslator.kernel.Kernel;

/**
 * Traverse command to reset last modified time of every node in datamodel
 * @author rpalomares
 */
public class ResetLastModifiedTimeTraverse extends EmptyTraverseCommand {
    Long alteredTime;

    public ResetLastModifiedTimeTraverse() {
        alteredTime = Kernel.getCurrentTime();
    }

    public ResetLastModifiedTimeTraverse(Long date) {
        alteredTime = date;
    }

    @Override
    public boolean action(TreeNode node) {
        node.setAlteredTime(alteredTime);
        return true;
    }

}
