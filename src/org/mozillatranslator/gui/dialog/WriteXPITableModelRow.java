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

package org.mozillatranslator.gui.dialog;

import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class WriteXPITableModelRow {
    private String product;
    private String version;
    private Product node;
    private boolean neutral;
    private boolean windows;
    private boolean unix;
    private boolean mac;
    private boolean region;
    private boolean custom;
    private boolean checkall;
    
    /** Creates new WriteXPITableModelRow */
    public WriteXPITableModelRow(Product n) {
        this(n, false);
    }
    
    /**
     * WriteXPITableModelRow
     *
     * @param n Product
     * @param b boolean
     */
    public WriteXPITableModelRow(Product n, boolean b) {
        product = n.toString();
        node = n;
        version = n.getVersion();
        if (b) {
            neutral = true;
            windows = true;
            mac     = true;
            unix    = true;
            region  = true;
            custom  = true;
            checkall = true;
        }
    }
    
    
    public String getProduct() {
        return product;
    }
    
    /** Getter for property neutral.
     * @return Value of property neutral.
     */
    public boolean isNeutral() {
        return neutral;
    }
    
    /** Setter for property neutral.
     * @param neutral New value of property neutral.
     */
    public void setNeutral(boolean neutral) {
        this.neutral = neutral;
    }
    
    /** Getter for property windows.
     * @return Value of property windows.
     */
    public boolean isWindows() {
        return windows;
    }
    
    /** Setter for property windows.
     * @param windows New value of property windows.
     */
    public void setWindows(boolean windows) {
        this.windows = windows;
    }
    
    /** Getter for property unix.
     * @return Value of property unix.
     */
    public boolean isUnix() {
        return unix;
    }
    
    /** Setter for property unix.
     * @param unix New value of property unix.
     */
    public void setUnix(boolean unix) {
        this.unix = unix;
    }
    
    /** Getter for property mac.
     * @return Value of property mac.
     */
    public boolean isMac() {
        return mac;
    }
    
    /** Setter for property mac.
     * @param mac New value of property mac.
     */
    public void setMac(boolean mac) {
        this.mac = mac;
    }
    
    /** Getter for property region.
     * @return Value of property region.
     */
    public boolean isRegion() {
        return region;
    }
    
    /** Setter for property region.
     * @param region New value of property region.
     */
    public void setRegion(boolean region) {
        this.region = region;
    }
    
    /** Getter for property custom.
     * @return Value of property custom.
     */
    public boolean isCustom() {
        return custom;
    }
    
    /** Setter for property custom.
     * @param custom New value of property custom.
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Product getNode() {
        return node;
    }
    
    public String getVersion() {
        return version;
    }

    public boolean isCheckall() {
        return checkall;
    }
    
    public void setCheckall(boolean checkall) {
        this.checkall = checkall;
    }
}
