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
 * Ricardo Palomares (Initial Code)
 *
 */

package org.mozillatranslator.action;

import org.mozillatranslator.filter.CheckDuplicated;
import org.mozillatranslator.filter.Filter;
import org.mozillatranslator.kernel.Kernel;

/**
 * Checks translations for duplicated words, like in "This is is a typo"
 * @author rpalomares
 */
public class ViewDuplicated extends DialogAction {
    /**
     * Creates a View Duplicated action
     */
    public ViewDuplicated() {
        super(Kernel.translate("menu.edit.qa.checkduplicated.label"));
    }
    
    /**
     * Returns the filter to check for duplicated words
     * @param localeName Locale code
     * @return the filter to check for duplicated words
     */
    @Override protected Filter getFilter(String localeName) {
        return new CheckDuplicated(localeName);
    }
}
