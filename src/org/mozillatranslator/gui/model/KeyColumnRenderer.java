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

import java.awt.Font;
import javax.swing.table.DefaultTableCellRenderer;
import org.mozillatranslator.datamodel.Phrase;

/** This renderer allows us to "mark" the Key column in different ways based on
 * the Phrase showed. This means that KeyColumn.getValue returns the whole
 * Phrase, instead of just the key name for that Phrase.
 *
 * @author rpalomares
 */
public class KeyColumnRenderer extends DefaultTableCellRenderer {
    /** Creates a new instance of KeyColumnRenderer */
    public KeyColumnRenderer() {
        super();
    }
    
    @Override
    public void setValue(Object value) {
        Phrase currentPhrase = (Phrase) value;
        
        if (currentPhrase.getLocalizationNote() != null) {
            String regularFontName = getFont().getFamily();
            int regularFontSize = getFont().getSize();
            
            this.setFont(new Font(regularFontName, Font.BOLD, regularFontSize));
        }
        
        setText(currentPhrase.getName());
    }
}
