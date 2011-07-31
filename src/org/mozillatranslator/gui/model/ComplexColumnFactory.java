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

import java.util.*;
import org.mozillatranslator.kernel.*;
import java.util.logging.*;

/**
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class ComplexColumnFactory {
    private static List<ComplexColumn> columnList;

    public static void init() {
        int count;
        String currentClass;
        String currentPreference;
        ComplexColumn currentColumn;

        columnList = new ArrayList<ComplexColumn>();
        count = Kernel.settings.getInteger(Settings.COLUMN_COUNT);
        for (int i = 0; i < count; i++) {
            try {
                currentPreference = Settings.COLUMN_CLASS_PREFIX + i
                                    + Settings.COLUMN_CLASS_SUFFIX;
                currentClass = Kernel.settings.getString(currentPreference);
                currentColumn = (ComplexColumn) Class.forName(currentClass).
                        newInstance();
                columnList.add(currentColumn);
            } catch (InstantiationException ex) {
                Logger.getLogger(ComplexColumnFactory.class.getName()).log(Level.SEVERE,
                                    null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ComplexColumnFactory.class.getName()).log(Level.SEVERE,
                                    null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ComplexColumnFactory.class.getName()).log(Level.SEVERE,
                                    null, ex);
            }
        }
    }

    public static Object[] toArray() {
        return columnList.toArray();
    }
}
