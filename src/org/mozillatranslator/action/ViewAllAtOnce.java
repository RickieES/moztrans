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

import org.mozillatranslator.filter.AllAtOnce;
import org.mozillatranslator.filter.Filter;
import org.mozillatranslator.kernel.Kernel;

/** Action to run the following filters (queries) all at once:
 *
 *  - Untranslated strings
 *  - Keep Original with translation
 *  - Original == Translated
 *  - Check variables
 *  - Check keybinding
 *  - DONT_TRANSLATE not honored
 *
 * @author Ricardo Palomares
 * @version 1.0
 */
public class ViewAllAtOnce extends DialogAction {

    /** Creates a new instance of ViewAllAtOnce */
    public ViewAllAtOnce() {
         super(Kernel.translate("menu.edit.qa.all_at_once.label"));
    }

    /**
     * Returns the filter to apply to each phrase
     * @param localeName Locale code
     * @return the filter (which, actually, applies in turn several filters)
     */
    protected Filter getFilter(String localeName) {
        return new AllAtOnce(localeName);
    }
}
