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


package org.mozillatranslator.io.file;

import org.mozillatranslator.dataobjects.ImportExportDataObject;
import org.mozillatranslator.io.common.*;

import org.mozillatranslator.io.common.XmlExporter;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;
import java.io.*;

/**
 *
 * @author  henrik
 */
public class FileToXmlAccess implements  FileAccess
{
    
    /** Creates a new instance of FileToXmlAccess */
    public FileToXmlAccess()
    {
    }
    
    public void load(ImportExportDataObject dataObject)
    {
        // load not supported yet
    }
    
    public void save(ImportExportDataObject dataObject)
    {
        ByteArrayOutputStream baos;
        OutputStreamWriter osw;
        BufferedWriter bw;
        GenericFile mFile;



        try
        {
            baos = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(baos, "UTF-8");
            bw = new BufferedWriter(osw);
            mFile = (GenericFile) dataObject.getNode();
            XmlExporter.saveFile(bw, mFile, null);
            bw.close();
            dataObject.setFileContent(baos.toByteArray());            
        }
        catch (Exception e)
        {
            Kernel.appLog.severe("Error during XML export " + e);
        }
    }
}
