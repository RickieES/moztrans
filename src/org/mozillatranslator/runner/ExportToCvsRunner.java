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
 * Ricardo Palomares (make CvsTransfer.java a non-static class)
 *
 */

package org.mozillatranslator.runner;

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.*;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.gui.*;

/** This runner will update a product
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class ExportToCvsRunner extends Thread {
    
    /** the product to update
     */
    private Product prod;
    private File importDir;
    private String l10n;
    /** Creates new UpdateProductRunner
     * @param p The product to update
     */
    public ExportToCvsRunner(Product p, File id, String l10n) {
        this.prod = p;
        this.importDir = id;
        this.l10n = l10n;
    }
    
    /** This is the main method
     */
    public void run() {
        CvsTransfer cvsInstance = new CvsTransfer(this.prod, this.importDir);
        cvsInstance.saveProduct(this.l10n);
    }
}
