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

import java.nio.charset.Charset;
import java.util.logging.Level;
import org.mozillatranslator.io.common.*;
import java.io.*;
import org.mozillatranslator.kernel.*;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class TextAccess extends FileAccessAdapter {
    private final static String UNKNOWN_FILETYPE_DEFAULT_KEY = "MT_UknownFileType";
    private String value;
    private boolean justStarted;
    
    /** Creates new DTDAccess */
    public TextAccess() {
    }
    
    @Override
    public void beginWrite(ImportExportDataObject dataObject) throws MozIOException {
        Kernel.appLog.log(Level.INFO, "Starting to write textfile {0}",
                            dataObject.getNode().getName());
    }
    
    @Override
    public void writeLine(String key, String writeValue) throws MozIOException {
        if (key.equals(UNKNOWN_FILETYPE_DEFAULT_KEY)) {
            this.value = writeValue;
        }
    }
    
    @Override
    public void endWrite(ImportExportDataObject dataObject) throws MozIOException {
        String fileCharSet;
        
        try {
            fileCharSet = Kernel.settings.getString(Settings.ENCODING_OTHERFILES);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter ows = new OutputStreamWriter(baos, fileCharSet);
            ows.write(value);
            ows.close();
            dataObject.setFileContent(baos.toByteArray());
        } catch (IOException e) {
            throw new MozIOException("Cannot write text file", e);
        }
    }
    
    @Override
    public void beginRead(ImportExportDataObject dataObject) throws MozIOException {
        String fileCharSet;
        StringBuffer rawBuild;
        ByteArrayInputStream bais;
        InputStreamReader isr;
        BufferedReader br;
        int number;
        boolean theEnd;
        char letter;
        
        justStarted = true;
        try {
            fileCharSet = Kernel.settings.getString(Settings.ENCODING_OTHERFILES);
            
            // ready stringbuffer
            rawBuild = new StringBuffer((int) dataObject.getFileContent().length);
            
            // ready streams
            bais = new ByteArrayInputStream(dataObject.getFileContent());
            isr = new InputStreamReader(bais, Charset.forName(fileCharSet));
            br = new BufferedReader(isr);
            
            // load file
            theEnd = false;
            while (!theEnd) {
                number = br.read();
                if (number == -1) {
                    theEnd = true;
                } else {
                    letter = (char) number;
                    rawBuild.append(letter);
                }
            }
            value = rawBuild.toString();
            
            br.close();
        } catch (IOException e) {
            throw new MozIOException("Cannot read text file", e);
        }
    }
    
    @Override
    public boolean hasMore() throws MozIOException {
        boolean result = justStarted;
        justStarted = false;
        return result;
    }
    
    @Override
    public String getKey() throws MozIOException {
        return UNKNOWN_FILETYPE_DEFAULT_KEY;
    }
    
    @Override
    public String getValue() throws MozIOException {
        return value;
    }
    
    @Override
    public void endRead(ImportExportDataObject dataObject) throws MozIOException {
        value = null;
    }
}
