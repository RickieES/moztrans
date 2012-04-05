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

package org.mozillatranslator.runner;

import org.mozillatranslator.dataobjects.ProductChildInputOutputDataObject;
import java.io.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.io.*;

/** Ths runner will generate a jar file
 * @author Henrik Lynggaard
 * @version 1.0 */
public class WriteJarRunner extends MozTask {
    /** creates a new WriteJarRunner */
    public WriteJarRunner() {
    }

    /** This method is called to write the jar file */
    @Override
    public void taskImplementation() throws MozException {
        try {
            ProductChildInputOutputDataObject jarData =
                    (ProductChildInputOutputDataObject) dataObject;
            jarData.getProductChild().save(jarData);
        } catch (IOException e) {
            throw new MozException("Error while writing jar", e);
        }
    }

    @Override
    public String getTitle() {
        return "Write Jar File";
    }
}
