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
package org.mozillatranslator.action;

import org.mozillatranslator.io.common.*;
import org.mozillatranslator.gui.dialog.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.runner.*;

/**
 *
 * @author Henrik Lynggaard
 * @version 1.00
 */
public class ExportFileToXmlAction extends TaskAction {
    /** Creates new AboutAction */
    public ExportFileToXmlAction() {
        super(Kernel.translate("menu.export.xml.file.label"),
              new FileTransferDialog(Kernel.translate("Export"),
              ImportExportDataObject.FORMAT_XML),
              new ExportFileRunner());
    }
}
