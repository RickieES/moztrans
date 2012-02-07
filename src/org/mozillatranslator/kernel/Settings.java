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

import java.awt.Font;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozillatranslator.datamodel.TrnsStatus;

/** This classs hold the application settings
 *
 * <h3>Change List</h3>
 *
 * <br><b>from 1,5 to 1.12</b>
 * <li> refactor so all getter need default value
 *
 *
 * <br><b>From 1.0 to 1.5 </b>
 * <li> synced with CVS version
 * <li> added system version setting
 * <li> swithed to Prepertiespersistance
 *
 * @author Henrik Lynggaard
 * @version 1.5
 */
public class Settings {
    // Settings setting;

    public static final String SETTINGS_FILENAME = "settings.filename";
    public static final String SYSTEM_VERSION = "system.version";
    // Logging settings
    public static final String LOGGING_SOUT = "logging.sout";
    public static final String LOGGING_SERR = "logging.serr";
    public static final String LOGGING_FILE = "logging.tofile";
    public static final String LOGGING_FILENAME = "logging.filename";
    public static final String LOGGING_NETWORK = "logging.tonetwork";
    public static final String LOGGING_POPUP = "logging.toscreen";
    // Datamodel
    public static final String DATAMODEL_FILENAME = "datamodel.filename";
    public static final String DATAMODEL_PCLASS = "datamodel.persistanceclass";
    // GUI columns
    public static final String COLUMN_COUNT = "column.count";
    public static final String COLUMN_CLASS_PREFIX = "column.";
    public static final String COLUMN_CLASS_SUFFIX = ".class";
    // L10n settings
    public static final String L10N_OVERRIDE = "l10n.override";
    public static final String L10N_COUNTRY = "l10n.country";
    public static final String L10N_LANGUAGE = "l10n.language";
    // License information
    public static final String LICENSE_DTD = "license.dtd";
    public static final String LICENSE_PROPERTIES = "license.properties";
    public static final String LICENSE_CONTRIBUTOR = "license.contributor";
    // Remembered state
    public static final String STATE_L10N = "state.l10n";
    public static final String STATE_COLUMN = "state.column.";
    public static final String STATE_COLUMN_COUNT = "state.column_count";
    public static final String STATE_FILELOCATION = "state.filelocation";
    public static final String STATE_AUTHOR = "state.author";
    public static final String STATE_DISPLAY = "state.display";
    public static final String STATE_PREVIEW = "state.preview";
    public static final String STATE_VERSION = "state.version";
    public static final String STATE_XPI_FILE = "state.xpifile";
    public static final String STATE_JAR_FILE = "state.jarfile";
    public static final String STATE_SEARCH_STRING = "state.search.string";
    public static final String STATE_SEARCH_WHERE = "state.search.where";
    public static final String STATE_SEARCH_HOW = "state.search.how";
    public static final String STATE_SEARCH_CASE = "state.search.case";
    public static final String STATE_SEARCH_KO_AS_TRANSLATED = "state.search.koastranslated";
    // Edit and view parameters
    public static final String QA_DTD_ORIG_ENTITIES_IGNORED = "qa.dtd_ignored_entities.orig";
    public static final String QA_DTD_TRNS_ENTITIES_IGNORED = "qa.dtd_ignored_entities.trns";
    public static final String QA_ENDING_CHECKED_CHARS = "qa.ending_checked_chars";
    public static final String FONT_EDITPHRASE_NAME = "font.editphrase.name";
    public static final String FONT_EDITPHRASE_SIZE = "font.editphrase.size";
    public static final String FONT_EDITPHRASE_STYLE = "font.editphrase.style";
    public static final String FONT_TABLEVIEW_NAME = "font.tableview.name";
    public static final String FONT_TABLEVIEW_SIZE = "font.tableview.size";
    public static final String FONT_TABLEVIEW_STYLE = "font.tableview.style";
    public static final String QA_PAIRED_CHARS_LIST = "qa.paired_chars_list";
    // Suggestions related preferences
    public static final String USE_SUGGESTIONS = "suggestions.enabled";
    public static final String SUGGESTIONS_MATCH_VALUE = "suggestions.match.value";
    public static final String AUTOTRANSLATE_ON_UPDATE = "autotranslate.on.update";
    // GUI parameters
    public static final String GUI_SHOW_WHAT_DIALOG = "gui.showwhat";
    public static final String GUI_IMPORT_FILE_CHOOSER_PATH = "gui.filechooser.import.lastpath";
    public static final String GUI_EXPORT_FILE_CHOOSER_PATH = "gui.filechooser.export.lastpath";
    public static final String GUI_MAIN_WINDOW_WIDTH = "gui.main.window.width";
    public static final String GUI_MAIN_WINDOW_HEIGHT = "gui.main.window.height";
    public static final String GUI_LOOK_AND_FEEL = "gui.look.and.feel";
    // Info about commandkeys and accesskeys connections
    public static final String CONN_LABEL_PATTERNS = "connection.label.patterns";
    public static final String CONN_AKEYS_PATTERNS = "connection.accesskey.patterns";
    public static final String CONN_CKEYS_PATTERNS = "connection.commandkey.patterns";
    public static final String CONN_LABEL_CASESENSE = "connection.label.casesensitive";
    public static final String CONN_AKEYS_CASESENSE = "connection.accesskey.casesensitive";
    public static final String CONN_CKEYS_CASESENSE = "connection.commandkey.casesensitive";
    // Do we want to replace en-US with ab-CD on exporting?
    public static final String EXPORT_REPLACE_ENUS = "export.replace.enus";
    public static final String EXPORT_ONLY_MODIFIED = "export.only.modified";
    public static final String EXPORT_ENUS_VALUE_ON_EMPTY_TRANSLATIONS = "export.enus.on.empty.trns";
    // Do we want to use external ZIP utilities on exporting?
    public static final String USE_EXTERNAL_ZIP = "export.use_external_zip";
    public static final String EXTERNAL_ZIP_PATH = "export.zip.path";
    public static final String EXTERNAL_UNZIP_PATH = "export.unzip.path";
    // Encodings to use when reading/writing files other than DTDs/.properties
    public static final String ENCODING_OTHERFILES = "encoding.otherfiles";
    // Batch control
    public static final String BATCH_COMMAND_COUNT = "batch.command.count";
    public static final String BATCH_COMMAND_PREFIX = "batch.command.";
    public static final String BATCH_COMMAND_NAME = ".name";
    public static final String BATCH_COMMAND_CLASS = ".class";
    // Advanced search
    public static final String ADV_SEARCH_ENABLE_1 = "advanced.search.enable.1";
    public static final String ADV_SEARCH_ENABLE_2 = "advanced.search.enable.2";
    public static final String ADV_SEARCH_ENABLE_3 = "advanced.search.enable.3";
    public static final String ADV_SEARCH_FIELD_1 = "advanced.search.field.1";
    public static final String ADV_SEARCH_FIELD_2 = "advanced.search.field.2";
    public static final String ADV_SEARCH_FIELD_3 = "advanced.search.field.3";
    public static final String ADV_SEARCH_RULE_1 = "advanced.search.rule.1";
    public static final String ADV_SEARCH_RULE_2 = "advanced.search.rule.2";
    public static final String ADV_SEARCH_RULE_3 = "advanced.search.rule.3";
    public static final String ADV_SEARCH_TEXT_1 = "advanced.search.text.1";
    public static final String ADV_SEARCH_TEXT_2 = "advanced.search.text.2";
    public static final String ADV_SEARCH_TEXT_3 = "advanced.search.text.3";
    public static final String ADV_SEARCH_CASE_1 = "advanced.search.case.1";
    public static final String ADV_SEARCH_CASE_2 = "advanced.search.case.2";
    public static final String ADV_SEARCH_CASE_3 = "advanced.search.case.3";
    // Auto-accesskeys assign
    public static final String AUTOAA_ONLY_FUZZIES = "autoassignakeys.only.fuzzies";
    public static final String AUTOAA_KEEP_EXISTING = "autoassignakeys.keep.existing";
    public static final String AUTOAA_HONOR_ORIGINAL = "autoassignakeys.honor.original";
    public static final String AUTOAA_CHAR_LIST = "autoassignakeys.character.list";
    public static final String AUTOAA_BAD_CHARS = "autoassignakeys.bad.characters";
    // Translation Status colors
    public static final String TRNS_STATUS_COLOR = "trnsstatus.color";

    private Properties current;

    /** Creates new Settings */
    public Settings() {
        Properties startup = new Properties();

        // system setting
        startup.setProperty(SETTINGS_FILENAME, "mozillatranslator.properties");
        startup.setProperty(SYSTEM_VERSION, "5.26");
        // logging settings
        startup.setProperty(LOGGING_SOUT, "true");
        startup.setProperty(LOGGING_SERR, "false");
        startup.setProperty(LOGGING_NETWORK, "false");
        startup.setProperty(LOGGING_FILE, "true");
        startup.setProperty(LOGGING_FILENAME, "mozillatranslator.log");
        startup.setProperty(LOGGING_POPUP, "true");

        // Edit and view preferences
        startup.setProperty(QA_DTD_ORIG_ENTITIES_IGNORED, "");
        startup.setProperty(QA_DTD_TRNS_ENTITIES_IGNORED, "");
        startup.setProperty(QA_ENDING_CHECKED_CHARS, " .,:");
        startup.setProperty(QA_PAIRED_CHARS_LIST, "()[]¡!¿?");
        startup.setProperty(FONT_EDITPHRASE_NAME, "DialogInput");
        startup.setProperty(FONT_EDITPHRASE_SIZE, "12");
        startup.setProperty(FONT_EDITPHRASE_STYLE, Integer.toString(Font.PLAIN));
        startup.setProperty(FONT_TABLEVIEW_NAME, "Dialog");
        startup.setProperty(FONT_TABLEVIEW_SIZE, "12");
        startup.setProperty(FONT_TABLEVIEW_STYLE, Integer.toString(Font.PLAIN));

        // Suggestions preferences
        startup.setProperty(USE_SUGGESTIONS, "true");
        startup.setProperty(SUGGESTIONS_MATCH_VALUE, "80");
        startup.setProperty(AUTOTRANSLATE_ON_UPDATE, "true");

        // Do we want to replace en-US with ab-CD on exporting?
        startup.setProperty(EXPORT_REPLACE_ENUS, "false");
        startup.setProperty(EXPORT_ONLY_MODIFIED, "true");
        startup.setProperty(EXPORT_ENUS_VALUE_ON_EMPTY_TRANSLATIONS, "false");
        startup.setProperty(USE_EXTERNAL_ZIP, "false");
        startup.setProperty(EXTERNAL_ZIP_PATH, "");
        startup.setProperty(EXTERNAL_UNZIP_PATH, "");

        // Encodings
        startup.setProperty(ENCODING_OTHERFILES, "UTF-8");

        // Datamodel persistance
        startup.setProperty(DATAMODEL_FILENAME, "Glossary.zip");
        startup.setProperty(DATAMODEL_PCLASS,
                "org.mozillatranslator.io.glossary.PropertiesPersistance");

        // l10n settings
        startup.setProperty(L10N_OVERRIDE, "false");
        startup.setProperty(L10N_COUNTRY, "");
        startup.setProperty(L10N_LANGUAGE, "");

        // Columns
        startup.setProperty(COLUMN_COUNT, "19");
        startup.setProperty(COLUMN_CLASS_PREFIX + "0" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.ProductColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "1" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.ContainerColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "2" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.ComponentColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "3" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.FileColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "4" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.KeyColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "5" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.OriginalTextColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "6" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.TranslatedTextColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "7" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.CurrentTextColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "8" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.KeepOriginalColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "9" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.TranslatedStatusColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "10" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.OriginalAccessColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "11" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.TranslatedAccessColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "12" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.CurrentAccessColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "13" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.OriginalCommandColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "14" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.TranslatedCommandColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "15" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.CurrentCommandColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "16" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.TranslatedCommentColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "17" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.FuzzyColumn");
        startup.setProperty(COLUMN_CLASS_PREFIX + "18" + COLUMN_CLASS_SUFFIX,
                "org.mozillatranslator.gui.model.FilterResultColumn");

        // Batch commands
        startup.setProperty(BATCH_COMMAND_COUNT, "2");
        startup.setProperty(BATCH_COMMAND_PREFIX + "0" + BATCH_COMMAND_NAME,
                "load-glossary");
        startup.setProperty(BATCH_COMMAND_PREFIX + "0" + BATCH_COMMAND_CLASS,
                "org.mozillatranslator.batchcontrol.LoadDataModelCommand");

        startup.setProperty(BATCH_COMMAND_PREFIX + "1" + BATCH_COMMAND_NAME,
                "update-glossary");
        startup.setProperty(BATCH_COMMAND_PREFIX + "1" + BATCH_COMMAND_CLASS,
                "org.mozillatranslator.batchcontrol.UpdateGlossaryCommand");

        // license information
        startup.setProperty(LICENSE_DTD, "");
        startup.setProperty(LICENSE_PROPERTIES, "");
        startup.setProperty(LICENSE_CONTRIBUTOR, "");

        // gui service parameters
        startup.setProperty(GUI_SHOW_WHAT_DIALOG, "true");
        startup.setProperty(GUI_IMPORT_FILE_CHOOSER_PATH, ".");
        startup.setProperty(GUI_EXPORT_FILE_CHOOSER_PATH, ".");
        startup.setProperty(GUI_MAIN_WINDOW_WIDTH, "800");
        startup.setProperty(GUI_MAIN_WINDOW_HEIGHT, "600");
        startup.setProperty(GUI_LOOK_AND_FEEL, "Metal");

        // accesskey and commandkey connection parameters
        startup.setProperty(CONN_LABEL_PATTERNS, ".label|.button|[:empty:]");
        startup.setProperty(CONN_AKEYS_PATTERNS, ".accesskey|.akey");
        startup.setProperty(CONN_CKEYS_PATTERNS, ".key|.commandkey");
        startup.setProperty(CONN_LABEL_CASESENSE, "false");
        startup.setProperty(CONN_AKEYS_CASESENSE, "false");
        startup.setProperty(CONN_CKEYS_CASESENSE, "false");

        // auto accesskey assignment
        startup.setProperty(AUTOAA_ONLY_FUZZIES, "true");
        startup.setProperty(AUTOAA_KEEP_EXISTING, "true");
        startup.setProperty(AUTOAA_HONOR_ORIGINAL, "false");
        startup.setProperty(AUTOAA_CHAR_LIST,
                "1234567890ABCDEFGHKLMNOPQRSTUVWXYZIJabcdefhkmnosuvwxyzijltrpgq");
        startup.setProperty(AUTOAA_BAD_CHARS, "fijltrpgq");

        // Translation Status colors
        for(TrnsStatus ts : TrnsStatus.values()) {
            startup.setProperty(TRNS_STATUS_COLOR + "." + ts.toString().toLowerCase(),
                    ts.colorAsRGBString());
        }

        // create the real setting with default values
        current = new Properties(startup);
        load();
    }

    /** Returns a setting as a string, using an empty string as default.
     * @return the result
     * @param key the key to get */
    public String getString(String key) {
        return current.getProperty(key, "");
    }

    /** Returns a setting as a string
     * @return the result
     * @param defValue the default value if the setting is not found
     * @param key the key to get */
    public String getString(String key, String defValue) {
        return current.getProperty(key, defValue);
    }

    /** Returns a setting as a boolean, using false as default
     * @return the result
     * @param key the key to get */
    public boolean getBoolean(String key) {
        return Boolean.valueOf(current.getProperty(key, "false")).booleanValue();
    }

    /** Returns a setting as a boolean
     * @return the result
     * @param defValue the default value if the setting is not found
     * @param key the key to get */
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.valueOf(current.getProperty(key, "" + defValue)).booleanValue();
    }

    /** Returns a setting as a int,using 0 as the default
     * @param key the key to get
     * @return the result
     */
    public int getInteger(String key) {
        String s = current.getProperty(key, "0");
        int i = Integer.parseInt(s);
        return i;
    }

    /** Returns a setting as a int
     * @param key the key to get
     * @param defValue The default value
     * @return the result
     */
    public int getInteger(String key, int defValue) {
        return Integer.parseInt(current.getProperty(key, "" + defValue));
    }

    /** Set a setting to a string value
     * @param key the key to set
     * @param value The new value
     */
    public void setString(String key, String value) {
        current.setProperty(key, value);
    }

    /** Set a setting to a boolean value
     * @param key the key to set
     * @param value The new value
     */
    public void setBoolean(String key, boolean value) {
        current.setProperty(key, "" + value);
    }

    /** Set a setting to a int value
     * @param key the key to set
     * @param value The new value
     */
    public void setInteger(String key, int value) {
        current.setProperty(key, "" + value);
    }

    /** Saves the settings */
    public void save() {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream(current.getProperty(SETTINGS_FILENAME));
            current.store(fos, "MozillaTranslator "
                    + current.getProperty(SYSTEM_VERSION)
                    + " configurations file");
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Loads the settings */
    public final void load() {
        FileInputStream fis;
        File testing;
        testing = new File(current.getProperty(SETTINGS_FILENAME));
        if (testing.exists()) {
            try {
                fis = new FileInputStream(current.getProperty(SETTINGS_FILENAME));
                current.load(fis);
                fis.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
