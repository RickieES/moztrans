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
 * Ricardo Palomares (Initial code)
 *
 */

package org.mozillatranslator.datamodel;

import java.awt.Color;

/**
 * Translation possible statuses (see
 * http://kenai.com/bugzilla/show_bug.cgi?id=3642 for more details)
 * @author rpalomares
 */
public enum TrnsStatus {
    Untranslated ("non-empty string with no translation nor Keep Original flag",
            Color.RED),
    Modified ("en-US string that has been modified since last time it was "
            + "translated", new Color(244, 192, 32)),
    Approximated ("en-US string that has been auto-translated based on a non-100% "
            + "match with the best possible coincidence", new Color(244, 220, 102)),
    Proposed ("en-US string that has been auto-translated based on a 100% match "
            + "with the most repeated translation among two or more",
            new Color(244, 232, 148)),
    Copied ("en-US string that has been auto-translated based on a 100% match "
            + "with only one translation", new Color(244, 244, 196)),
    Translated ("en-US string with Keep Original set or manually translated",
            Color.WHITE);

    private final String description;
    private final Color displayedColor;

    TrnsStatus(String description, Color displayedColor) {
        this.description = description;
        this.displayedColor = displayedColor;
    }

    public String description() {
        return description;
    }

    public Color displayedColor() {
        return displayedColor;
    }

    public String colorAsRGBString() {
        return Integer.toString(displayedColor.getRed()) + ","
             + Integer.toString(displayedColor.getGreen()) + ","
             + Integer.toString(displayedColor.getBlue());
    }
}
