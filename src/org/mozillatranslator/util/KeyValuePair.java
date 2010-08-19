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

package org.mozillatranslator.util;

/** The class represent a pair consisting of a key and a display
 * value
 *
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class KeyValuePair
{
    
    /** Holds value of property key. */
    private Object key;
    
    /** Holds value of property value. */
    private String value;
    
    /** Creates new KeyValuePair
     * @param k The key
     * @param v The value
     */
    public KeyValuePair(Object k, String v)
    {
        key = k;
        value = v;
    }
    
    /** Getter for property key.
     * @return Value of property key.
     */
    public Object getKey()
    {
        return key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     */
    public void setKey(Object key)
    {
        this.key = key;
    }
    
    /** Getter for property value.
     * @return Value of property value.
     */
    public String getValue()
    {
        return value;
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /** overload of object.toString()
     * @return The value of the key
     */
    @Override
    public String toString()
    {
        return value;
    }
    
}
