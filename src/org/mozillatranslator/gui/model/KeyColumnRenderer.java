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

package org.mozillatranslator.gui.model;

import java.awt.Color;
import java.awt.Font;
import java.util.StringTokenizer;
import javax.swing.table.DefaultTableCellRenderer;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/** This renderer allows us to "mark" the Key column in different ways based on
 * the Phrase showed. This means that KeyColumn.getValue returns the whole
 * Phrase, instead of just the key name for that Phrase.
 *
 * @author rpalomares
 */
public class KeyColumnRenderer extends DefaultTableCellRenderer {
    private final String localeName;

    /** Creates a new instance of KeyColumnRenderer
     * @param localeName Key code for locale (e.g.: en-US) */
    public KeyColumnRenderer(String localeName) {
        super();
        this.localeName = localeName;
    }
    
    @Override
    public void setValue(Object value) {
        Phrase currentPhrase = (Phrase) value;
        StringTokenizer st;
        
        if (currentPhrase.getLocalizationNote() != null) {
            String regularFontName = getFont().getFamily();
            int regularFontSize = getFont().getSize();
            
            this.setFont(new Font(regularFontName, Font.BOLD, regularFontSize));
        }

        if (currentPhrase.isKeepOriginal()) {
            Color c = Kernel.settings.getColor(Settings.TRNS_STATUS_COLOR + ".translated");
            this.setBackground(c);
            this.setToolTipText("This phrase is set to Keep Original");
        } else {
            Translation t = (Translation) currentPhrase.getChildByName(localeName);
            if (t == null) {
                Color c = Kernel.settings.getColor(Settings.TRNS_STATUS_COLOR + ".untranslated");
                this.setBackground(c);
                this.setToolTipText("Translation status of this phrase is Untranslated");
            } else {
                Color c = Kernel.settings.getColor(Settings.TRNS_STATUS_COLOR + "."
                                                 + t.getStatus().toString().toLowerCase());
                this.setBackground(c);
                this.setToolTipText("Translation status of this phrase is " + t.getStatus());
            }
        }
        setText(currentPhrase.getName());
    }
}
