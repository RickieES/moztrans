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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.*;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.mozillatranslator.datamodel.DataModel;
import org.mozillatranslator.datamodel.TranslationSuggestions;
import org.mozillatranslator.gui.MainWindow;
import org.mozillatranslator.gui.dialog.EditPhraseDialog;
import org.mozillatranslator.gui.model.ComplexColumnFactory;

/** This class contains hook for a lot of singletons
 * @author Henrik Lynggaard
 * @version 1.0 */
public class Kernel {

    /** Reference to the feedback provider, which allows to set messages in the
     * status bar and show progress in long tasks */
    public static FeedbackProvider feedback = null;
    /** This is the Settings singleton */
    public static Settings settings;
    /** this is the main window singleton */
    public static MainWindow mainWindow;
    /** This is the datamodel singleton */
    public static DataModel datamodel;
    /** This is the edit phrase dialogbox singleton */
    public static EditPhraseDialog editPhrase;
    /** This singleton point the the i18n manager for the program */
    public static L10nManager l10n;
    /** this constant equals the orginal locale "en_US" */
    public static final String ORIGINAL_L10N = "en-US";
    /** Reference to the logger which register messages and exceptions into a
     * log file */
    public static final Logger appLog = Logger.getLogger("org.mozillatranslator");
    /** Reference to the translation suggestion class */
    public static final TranslationSuggestions ts = new TranslationSuggestions();

    private static long currentTime;
    private static boolean timeBatch = false;

    /**
     * this is first called to setup everything
     * @param hasGui True if the application will run in GUI mode
     */
    public static void init(final boolean hasGui) {
        FileHandler fileHandler;
        ConsoleHandler conHandler;
        MemoryHandler memHandler;
        try {
            feedback = new FeedbackProvider(hasGui);
            fileHandler = new FileHandler("mozillatranslator.log", 0, 3, false);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            memHandler = new MemoryHandler(fileHandler, 1000, Level.WARNING);

            conHandler = new ConsoleHandler();
            conHandler.setLevel(Level.WARNING);

            appLog.addHandler(memHandler);
            appLog.addHandler(conHandler);
            appLog.setLevel(Level.INFO);
            appLog.setUseParentHandlers(false);

            Kernel.appLog.info("Making settings");
            settings = new Settings();

            l10n = new L10nManager();
            datamodel = new DataModel();
            Kernel.appLog.log(Level.INFO, "Kernel.setttings {0}", Kernel.settings);
        } catch (Exception e) {
            System.err.println("Could not init \n" + e);
        }
    }

    /** This will open the main window */
    public static void startWindow() {
        if (!Kernel.setBestAvailableLookAndFeel()) {
            System.exit(1);
        }
        ComplexColumnFactory.init();
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        editPhrase = new EditPhraseDialog();
    }

    public static List<String> getAvailableLookAndFeels() {
        ArrayList<String> availableLookAndFeels = new ArrayList<String>();

        for(LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
            availableLookAndFeels.add(lafInfo.getName());
        }
        return availableLookAndFeels;
    }
    
    private static boolean setBestAvailableLookAndFeel() {
        String preferredLafName = Kernel.settings.getString(Settings.GUI_LOOK_AND_FEEL);
        
        for(LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
            if (lafInfo.getName().equals(preferredLafName)) {
                try {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                } catch (Exception e1) {
                    // UnsupportedLookAndFeelException, ClassNotFoundException,
                    // InstantiationException or IllegalAccessException could be
                    // thrown; in any case, we fallback to the cross-platform L&F
                    try {
                        Kernel.appLog.log(Level.WARNING, "L&F {0} couldn't be applied", preferredLafName);
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e2) {
                        Kernel.appLog.log(Level.SEVERE,
                                "Default Metal L&F couldn't be applied, execution can't continue");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** This is used to lock the timer, so that all altered times
     * after this will get the same altered time */
    public static void startTimeBatch() {
        final Date dat = new Date();
        currentTime = dat.getTime();
        timeBatch = true;
    }

    /** this will free the timer */
    public static void endTimeBatch() {
        timeBatch = false;
    }

    /** Used by the touch method, returns the current time or
     * if locked the time of the lock
     * @return the time  */
    public static long getCurrentTime() {
        long result;
        if (timeBatch) {
            result = currentTime;
        } else {
            final Date dat = new Date();
            result = dat.getTime();
        }
        return result;
    }

    /** This uses the l10n manager to translate a string
     * in the program gui
     * @param key the key to look up
     *
     * @return the translated string */
    public static String translate(final String key) {
        return l10n.translate(key);
    }
}
