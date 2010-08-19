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


package org.mozillatranslator.gui;

import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;
/**
 *
 * @author  Henrik Lynggaard
 */
public class MigrateProductDataObject implements DataObject
{
    
    /** Holds value of property source. */
    private Product source;
    
    /** Holds value of property destination. */
    private Product destination;
    
    /** Holds value of property l10n. */
    private String l10n;
    
    /** Creates a new instance of MigrateProductDataObject */
    public MigrateProductDataObject()
    {
    }
    
    /** Getter for property source.
     * @return Value of property source.
     */
    public Product getSource()
    {
        return this.source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     */
    public void setSource(Product source)
    {
        this.source = source;
    }
    
    /** Getter for property destination.
     * @return Value of property destination.
     */
    public Product getDestination()
    {
        return this.destination;
    }
    
    /** Setter for property destination.
     * @param destination New value of property destination.
     */
    public void setDestination(Product destination)
    {
        this.destination = destination;
    }
    
    /** Getter for property l10n.
     * @return Value of property l10n.
     */
    public String getL10n()
    {
        return this.l10n;
    }
    
    /** Setter for property l10n.
     * @param l10n New value of property l10n.
     */
    public void setL10n(String l10n)
    {
        this.l10n = l10n;
    }
    
}
