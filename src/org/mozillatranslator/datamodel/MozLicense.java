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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

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
        this.insertionPos = -1;
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
        StringBuilder result = new StringBuilder();

        // If the license block is not empty
        if (licenseBlock != null && licenseBlock.length() > 0) {
            // If the license contributor specific is empty, then take
            // the general contributor value
            if (licenseContributor == null || licenseContributor.length() == 0) {
                licenseContributor = set.getString(Settings.LICENSE_CONTRIBUTOR);
            }

            try {
                if (this.getInsertionPos() != -1) {
                    result.append(licenseBlock.substring(0, insertionPos));
                    result.append((this.parent instanceof PropertiesFile) ? "#   " : "   -   ");
                    result.append(licenseContributor);
                    result.append("\n");
                }
                result.append(licenseBlock.substring(Math.max(0, insertionPos)));
            } catch (StringIndexOutOfBoundsException ex) {
                fLogger.log(Level.INFO, "MozLicense parent ({0}) has no valid license", this.parent.getName());
                fLogger.log(Level.INFO, "Additional info: license len   {0}", this.licenseBlock.length());
                fLogger.log(Level.INFO, "                 insertion pos {0}", this.insertionPos);
            }
            return result.toString();
        } else {
            return "";
        }
    }
}
