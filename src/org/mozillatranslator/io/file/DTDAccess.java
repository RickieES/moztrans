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

import org.mozillatranslator.io.common.*;
import org.mozillatranslator.datamodel.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;

/** This class is used to read/write a DTD file from/to the disk using its
 * native format
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class DTDAccess extends FileAccessAdapter {
    private static final String FILE_ENCODING = "UTF-8";

    // saving variables
    private ByteArrayOutputStream baos;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    private LinkedHashMap map;
    private Iterator mapIterator;
    String currentKey;

    // loading variables
    private ByteArrayInputStream bais;
    private String key;
    private String value;

    /** Creates new DTDAccess */
    public DTDAccess() {
    }

/* ----------------------------
 * save functions
 * ----------------------------
 */

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    protected void beginWrite(ImportExportDataObject dataObject) throws MozIOException {
        try {
            baos = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(baos, FILE_ENCODING);
            bw = new BufferedWriter(osw);

            DTDFile datamodelFile = (DTDFile) dataObject.getNode();

            // Save license, if the file has one
            MozLicense thisFileLicense = datamodelFile.getLicenseBlock();
            if (thisFileLicense != null) {
                bw.write("<!--" + thisFileLicense.getTranslatedLicense() + "-->");
                bw.newLine();
            }

            // Save the external entities, if the file has anyone
            if (datamodelFile.getExternalEntities() != null) {
                String line;
                Iterator eeIter = datamodelFile.getExternalEntities().iterator();

                while (eeIter.hasNext()) {
                    ExternalEntity ee = (ExternalEntity) eeIter.next();

                    line = "<!ENTITY " + ee.getName().charAt(0) + " " +
                            ee.getName().substring(1) +
                            ((ee.getPublicId().equals("")) ?
                                " SYSTEM \"" + ee.getSystemId() :
                                " PUBLIC \"" + ee.getPublicId()
                                ) + "\">";
                    bw.write(line, 0, line.length());
                    bw.newLine();

                    bw.write(ee.getName() + ";", 0, ee.getName().length() + 1);
                    bw.newLine();
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new MozIOException("Wrong encoding for dtd file", e);
        } catch (IOException e) {
            throw new MozIOException("Cannot write license in dtd file", e);
        }
    }

    /** {@inheritDoc}
     * @param writeKey {@inheritDoc}
     * @param writeValue {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    public void writeLine(String writeKey, String writeValue) throws MozIOException {
        String delim = "\"";
        try {
            String line;
            if (writeValue.indexOf(delim) != -1) {
                delim = "'";
            }

            line = "<!ENTITY " + writeKey + " " + delim +
                    customWriteEscape(writeValue) + delim + ">";
            bw.write(line, 0, line.length());
            bw.newLine();
        } catch (IOException e) {
            throw new MozIOException("Cannot write line in dtd file", e);
        }

    }

    private String customWriteEscape(String text) {
        String result;
        result = text.replaceAll("%", "&#037;");
        return result;
    }

    /** {@inheritDoc}
     * @param dataObject A dataobject telling the writer what mozilla file to write
     * and where it should be placed.
     * @throws MozIOException Is thrown if there happends an IO that prevents writing.
     */
    public void endWrite(ImportExportDataObject dataObject) throws MozIOException {
        try {
            bw.close();
            dataObject.setFileContent(baos.toByteArray());
        } catch (IOException e) {
            throw new MozIOException("Cannot close stream for dtd file", e);
        }
        // dont't need these anymore, but since we are keeping a static
        // DTDAccess object let's dereference them.
        baos = null;
        osw = null;
        bw = null;
    }

/* ----------------------------
 * load functions
 * ----------------------------
 */
    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    public void beginRead(ImportExportDataObject dataObject) throws MozIOException {
        try {
            bais = new ByteArrayInputStream(dataObject.getFileContent());
            map = new LinkedHashMap();
            commentMap = new LinkedHashMap();

            DTDFile datamodelFile = (DTDFile) dataObject.getNode();
            DTDReadHelper helper = new DTDReadHelper();
            helper.loadOriginal(bais, map, commentMap);

            if (helper.getThisFileLicense() != null) {
                MozLicense thisFileLicense = datamodelFile.getLicenseBlock();
                if (thisFileLicense == null) {
                    thisFileLicense = new MozLicense(datamodelFile);
                    datamodelFile.setLicenseBlock(thisFileLicense);
                }

                thisFileLicense.setInsertionPos(helper.getThisFileLicense().getInsertionPos());
                thisFileLicense.setLicenseBlock(helper.getThisFileLicense().getLicenseBlock());
            }

            mapIterator = map.keySet().iterator();

            if (helper.getExternalEntities() != null) {
                datamodelFile.setExternalEntities(helper.getExternalEntities());
            }
        } catch (Exception e) {
            throw new MozIOException("Cannot read dtd file " + dataObject.getNode().getName(), e);
        }
    }

    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    public boolean hasMore() throws MozIOException {
        return mapIterator.hasNext();
    }

    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    public String getKey() throws MozIOException {
        currentKey = (String) mapIterator.next();
        return currentKey;
    }

    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    public String getValue() throws MozIOException {
        return (String) map.get(currentKey);
    }

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    public  void endRead(ImportExportDataObject dataObject) throws MozIOException {
        try {
            bais.close();
            map = null;
            mapIterator= null;
            currentKey= null;
        } catch (IOException e) {
            throw new MozIOException("Cannot close stream for dtd reading", e);
        }
    }
}
