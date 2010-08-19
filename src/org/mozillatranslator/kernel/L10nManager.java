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

package org.mozillatranslator.kernel;


import java.util.*;
import java.util.logging.*;

/** This class translates string s in the gui
 * @author Henrik Lynggaard
 * @version 1.0 */
public class L10nManager {
    private static final Logger fLogger = Logger.getLogger(L10nManager.class.getPackage().getName());
    private ResourceBundle bundle;
    private boolean override;
    
    /** Creates new L10nManager */
    public L10nManager() {
        override = Kernel.settings.getBoolean(Settings.L10N_OVERRIDE);
        Locale loaded;
        if (override) {
            String c = Kernel.settings.getString(Settings.L10N_COUNTRY, "en");
            String l = Kernel.settings.getString(Settings.L10N_LANGUAGE, "us");
            
            loaded = new Locale(l, c);
            bundle = ResourceBundle.getBundle("org/mozillatranslator/resource/userinterface", loaded);
            fLogger.info("Locale: " + loaded.getLanguage() + "-" + loaded.getCountry());
        } else {
            bundle = ResourceBundle.getBundle("org/mozillatranslator/resource/userinterface", Locale.getDefault());
            fLogger.info("Locale: " + Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry());
        }
    }
    
    /** This methd will translate a string
     * @param key the key to look up
     * @return the translated string */
    public String translate(String key) {
        return  bundle.getString(key);
    }
    
}
