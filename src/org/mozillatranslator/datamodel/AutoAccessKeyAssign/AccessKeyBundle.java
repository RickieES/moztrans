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

package org.mozillatranslator.datamodel.AutoAccessKeyAssign;

import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Translation;

/**
 *
 * @author rpalomares
 */
public class AccessKeyBundle {
    Phrase original;
    Translation aKeyItem;
    Translation boundLabel;
    boolean fuzzy;
    String proposedValue;

    /**
     * Default constructor
     */
    public AccessKeyBundle() {
        fuzzy = false;
        proposedValue = "";
    }
    
    /**
     * Full constructor providing all properties
     * @param original the Phrase object representing the accesskey
     * to auto-fill (in the corresponding locale)
     * @param locale the locale code
     */
    public AccessKeyBundle(Phrase original, String locale) {
        super();
        this.original = original;
        this.aKeyItem = (Translation) original.getChildByName(locale);
        if (original.getLabelConnection() != null) {
            Phrase label = original.getLabelConnection();
            this.boundLabel = (Translation) label.getChildByName(locale);
        }
        this.fuzzy = original.isFuzzy();
        
        // Assigns the current value; the .substring(0, 1) is to
        // remove possible extra chars
        try {
            if (original.isKeepOriginal()) {
                this.proposedValue = original.getText().substring(0, 1);
            } else {
                this.proposedValue = aKeyItem.getText().substring(0, 1);
            }
        } catch (NullPointerException e) {
            // We're here because aKeyItem is null, or else because either
            // original.getText() or aKeyItem.getText() are null
            this.proposedValue = "";
        } catch (StringIndexOutOfBoundsException e) {
            // We're here because either original.getText() or
            // aKeyItem.getText() have length() == 0
            this.proposedValue = "";
        }
    }

    /**
     * Getter for aKeyItem
     * @return the accesskey to auto-assign
     */
    public Translation getAKeyItem() {
        return aKeyItem;
    }

    /**
     * Setter for aKeyItem
     * @param aKeyItem the accesskey to auto-assign
     */
    public void setAKeyItem(Translation aKeyItem) {
        this.aKeyItem = aKeyItem;
    }

    /**
     * Getter for boundLabel
     * @return the Translation object representing the label where the
     * accesskey is to be applied
     */
    public Translation getBoundLabel() {
        return boundLabel;
    }

    /**
     * Getter for fuzzy
     * @return the fuzzy state of the accesskey
     */
    public boolean isFuzzy() {
        return fuzzy;
    }

    /**
     * Sets the fuzzy value
     * @param fuzzy true or false
     */
    public void setFuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
    }

    /**
     * Getter for original
     * @return the Phrase object parenting the accesskey to auto-assign
     */
    public Phrase getOriginal() {
        return original;
    }

    /**
     * Getter for proposedValue, the string in which we'll save the
     * proposed value during auto-assignment
     * @return a string with the proposed accesskey value
     */
    public String getProposedValue() {
        return proposedValue;
    }

    /**
     * Setter for proposedValue, the string in which we'll save the
     * proposed value during auto-assignment
     * @param proposedValue a string with the proposed accesskey value
     */
    public void setProposedValue(String proposedValue) {
        this.proposedValue = (proposedValue.length() > 0) ?
                                proposedValue.substring(0, 1) :
                                "";
    }
    
    /**
     * Checks if character ch is present in associated label of this bundle
     * @param ch a string with the character to look for
     * @return true if the character is present (and, therefore, can be used
     * as accesskey for the label)
     */
    public boolean isInLabel(String ch) {
        boolean result = false;
        
        try {
            result = (this.getBoundLabel().getText().indexOf(ch.substring(0, 1)) != -1);
        } catch (NullPointerException e) {
            // This phrase has no bound label, or else it is empty, so we will
            // treat the accesskey of the original phrase as the only valid
            // character
            result = (this.getOriginal().getText().equals(ch));
        }
        return result;
    }
}
