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

package org.mozillatranslator.util;

/**
 * This 
 * @author rpalomares
 */
public class EnhancedString {
    private String value;
    
    /** Creates a new instance of EnhancedString */
    public EnhancedString() {
        this.value = "";
    }
    
    public EnhancedString(String value) {
        this.value = value;
    }
    
    public void set(String value) {
        this.value = value;
    }
    
    public String trim() {
        return this.value.trim();
    }
    
    public char charAt(int index) {
        return this.value.charAt(index);
    }
    
    public String toUpperCase() {
        return this.value.toUpperCase();
    }
    
    public boolean contains(CharSequence toCheck) {
        return this.value.contains(toCheck);
    }
    
    public int indexOf(String toCheck) {
        return this.value.indexOf(toCheck);
    }
    
    public int indexOf(char toCheck) {
        return this.value.indexOf(toCheck);
    }
    
    public String substring(int start, int end) {
        return this.value.substring(start, end);
    }
    
    public String substring(int start) {
        return this.value.substring(start);
    }

    public int length() {
        return this.value.length();
    }
    
    /**
     * Returns the string without leading spaces
     *
     * @return the string without leading spaces
     */
    public String ltrim() {
        int pos = 0;
        
        while ((pos < this.value.length()) && (this.value.charAt(pos) == ' ')) {
            pos++;
        }
        return this.value.substring(pos);
    }
    
    /**
     * Return the string without trailing spaces
     *
     * @return the string without trailing spaces
     */
    public String rtrim() {
        int pos = this.value.length() - 1;
        
        while ((pos >= 0) && (this.value.charAt(pos) == ' ')) {
            pos--;
        }
        return this.value.substring(0, pos);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
 