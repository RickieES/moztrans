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


package org.mozillatranslator.gui.model;

import org.mozillatranslator.datamodel.*;
/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class StatusCell
{

    private static StatusCell noStatusCell = new StatusCell(-1, "");
    
    private static StatusCell notSeenCell = new StatusCell(Translation.STATUS_NOTSEEN, "Not seen");
    private static StatusCell changedCell = new StatusCell(Translation.STATUS_CHANGED, "Changed");
    private static StatusCell translatedCell = new StatusCell(Translation.STATUS_TRANSLATED, "Translated");
    private static StatusCell errorCell = new StatusCell(Translation.STATUS_ERROR, "Error");
    private static StatusCell acceptedCell = new StatusCell(Translation.STATUS_ACCEPTED, "Accepted");
    private static StatusCell perfectCell = new StatusCell(Translation.STATUS_PERFECT, "Perfect");
    private static StatusCell otherCell = new StatusCell(Translation.STATUS_OTHER, "Other");
    private static StatusCell migratedCell = new StatusCell(Translation.STATUS_MIGRATED, "Migrated");
    
    private int key;
    private String value;

    /** Creates new StatusCell */
    public StatusCell(int k, String v) 
    {
        key = k;
        value = v;
        
    }
    
    public static StatusCell lookupStatusCell(int status)
    {
        StatusCell result = noStatusCell;
        switch (status)
        {
            case Translation.STATUS_NOTSEEN:
                result = notSeenCell;
                break;
            case Translation.STATUS_CHANGED:
                result = changedCell;
                break;
            case Translation.STATUS_TRANSLATED:
                result = translatedCell;
                break;
            case Translation.STATUS_ERROR:
                result = errorCell;
                break;
            case Translation.STATUS_ACCEPTED:
                result = acceptedCell;
                break;
            case Translation.STATUS_PERFECT:
                result = perfectCell;                
                break;
            case Translation.STATUS_OTHER:
                result = otherCell;
                break;
            case Translation.STATUS_MIGRATED:
                result = migratedCell;
                break;                
        }
        return result;
    }
    
    public static StatusCell getNoStatusCell()
    {
        return noStatusCell;
    }
    
    /** Getter for property key.
     * @return Value of property key.
     */
    public int getKey()
    {
        return key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     */
    public void setKey(int key)
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
    
    public String toString()
    {
        return value;
    }
        
}
