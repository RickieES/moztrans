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
 * Ricardo Palomares Martinez (Initial Code)
 *
 */

package org.mozillatranslator.datamodel;

import java.util.logging.Logger;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.kernel.Kernel;

/**
 * This class implements license information attached to a file
 *
 * @author Ricardo
 */
public class MozLicense {
    private static final Logger fLogger = Logger.getLogger(MozLicense.class.getPackage().
            getName());
    
    /**
     * Holds the original license block
     **/
    private String licenseBlock;
    
    /**
     * Holds the string with the contributor; it may be empty to use a
     * "generic contributor"
     */
    private String licenseContributor;
    
    /**
     * The character position in which the contributor is to be inserted
     */
    private int insertionPos;
    
    /**
     * The MozFile holding this instance
     */
    MozFile parent;
    
    /** Creates a new instance of MozLicense */
    public MozLicense(MozFile parent) {
        this.parent = parent;
    }

    public String getLicenseBlock() {
        return licenseBlock;
    }

    public void setLicenseBlock(String licenseBlock) {
        this.licenseBlock = licenseBlock;
    }

    public String getLicenseContributor() {
        return licenseContributor;
    }

    public void setLicenseContributor(String licenseContributor) {
        this.licenseContributor = licenseContributor;
    }

    public int getInsertionPos() {
        return insertionPos;
    }

    public void setInsertionPos(int insertionPos) {
        this.insertionPos = insertionPos;
    }
    
    /**
     * Returns a string with the license adapted to the translated file.
     * If the original license is empty, then an empty string ("") will
     * be returned.
     *
     * @return the translated license block
     */
    public String getTranslatedLicense() {
        Settings set = Kernel.settings;
        MozFile parent;
        String result = null;
        
        // If the license block is not empty
        if (licenseBlock!=null && licenseBlock.length() > 0) {
            // If the license contributor specific is empty, then take
            // the general contributor value
            if (licenseContributor == null || licenseContributor.length()==0) {
                licenseContributor = set.getString(Settings.LICENSE_CONTRIBUTOR);
            }
            
            try {
                result = licenseBlock.substring(0, insertionPos) + 
                        ((this.parent instanceof PropertiesFile) ? "#   " : "   -   ") +
                        licenseContributor + "\n" + licenseBlock.substring(insertionPos);
            } catch (StringIndexOutOfBoundsException ex) {
                fLogger.info("MozLicense parent (" + this.parent.getName() +
                        ") has no valid license");
                fLogger.info("Additional info: license len   " + this.licenseBlock.length());
                fLogger.info("                 insertion pos " + this.insertionPos);
            }
            
            return result;
        } else {
            return "";
        }
    }
}
