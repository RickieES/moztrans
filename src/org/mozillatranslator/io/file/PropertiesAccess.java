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
 * Ricardo Palomares (modifications to preserve original licenses and to deal
 *                    with INI files)
 *
 */

package org.mozillatranslator.io.file;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mozillatranslator.datamodel.MozFile;
import org.mozillatranslator.datamodel.MozLicense;
import org.mozillatranslator.dataobjects.ImportExportDataObject;
import org.mozillatranslator.io.common.MozIOException;
import org.mozillatranslator.util.EnhancedString;


/** This class implements the native format for properties files.
 * @author Henrik Lynggaard
 * @version 1.7
 */
public class PropertiesAccess extends FileAccessAdapter {
    // Saving variables
    private ByteArrayOutputStream baos;
    private OutputStreamWriter osw;
    private BufferedWriter bw;
    private boolean isIniFile;

    // SortedProperties is defined at the bottom of this file, and extends
    // java.util.Properties
    private SortedProperties prop;
    private Enumeration enumeration;
    private String currentKey;
    
    /** Creates new PropertiesAccess */
    public PropertiesAccess() {
    }

/* -----------------------------
 * Save rutines
 * -----------------------------
 */

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    @Override
    public void beginWrite(ImportExportDataObject dataObject) throws MozIOException {
        baos = new ByteArrayOutputStream();
        osw = new OutputStreamWriter(baos, Charset.forName("UTF-8"));
        bw = new BufferedWriter(osw);

        MozFile datamodelFile = (MozFile) dataObject.getNode();
        
        // PropertiesAccess also deals with INI files, so we flag if we're
        // processing an INI file instead of a regular Properties file
        isIniFile = datamodelFile.getName().endsWith(".ini");
        
        MozLicense thisFileLicense = datamodelFile.getLicenseBlock();
        if (thisFileLicense != null) {
            try {
                bw.write(thisFileLicense.getTranslatedLicense());
            } catch (IOException e) {
                throw new MozIOException("Cannot write license in "
                        + ((isIniFile) ? "INI" : "properties") + " file", e);
            }
        }
    }

    /** {@inheritDoc}
     * @param key {@inheritDoc}
     * @param value {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    @Override
    public void writeLine(String key, String value) throws MozIOException {
        String line;
        
        if (isIniFile && key.startsWith("section.")) {
            line = key.substring(key.indexOf('[')) + "\n";
        } else {
            line = key + ((isIniFile) ? "=" : " = ") + value + "\n";
        }
        
        try {
            bw.write(line);
        } catch (IOException e) {
            throw new MozIOException("Cannot save "
                        + ((isIniFile) ? "INI" : "properties") + " file", e);
        }
    }

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    @Override
    public void endWrite(ImportExportDataObject dataObject) throws MozIOException {
        try {
            bw.flush();
        } catch (IOException e) {
            throw new MozIOException("Cannot save "
                        + ((isIniFile) ? "INI" : "properties") + " file", e);
        }

        dataObject.setFileContent(baos.toByteArray());
    }

/* -----------------------------
 * load rutines
 * -----------------------------
 */

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    @Override
    public void beginRead(ImportExportDataObject dataObject) throws MozIOException {
        try {
            prop = new SortedProperties();
            ByteArrayInputStream bais = new ByteArrayInputStream(dataObject.getFileContent());
            commentMap = new LinkedHashMap();

            // Let's rescue the MozLicense reference, if it exists
            if (hasLicense(bais)) {
                MozFile datamodelFile = (MozFile) dataObject.getNode();
                MozLicense thisFileLicense = datamodelFile.getLicenseBlock();
                if (thisFileLicense == null) {
                    thisFileLicense = new MozLicense(datamodelFile);
                    datamodelFile.setLicenseBlock(thisFileLicense);
                }

                extractLicense(bais, thisFileLicense);
            }

            prop.load(bais, commentMap);
            bais.close();
            enumeration = prop.keys();
        } catch (IOException e) {
            throw new MozIOException("Cannot read INI/properties file", e);
        }
    }

    /**
     * Tests if a license block exists in the ByteArrayInputStream passed as a
     * parameter
     *
     * @param   bais    the ByteArrayInputStream to test
     * @return  true if a license block exists, false otherwise
     **/
    private boolean hasLicense(ByteArrayInputStream bais) throws IOException {
        boolean hasIt = false;
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais, "UTF-8"));

        while (((line = reader.readLine()) != null) && (!hasIt)) {
            hasIt = (line.indexOf("*** BEGIN LICENSE BLOCK ***") > -1);
        }

        bais.reset();
        return hasIt;
    }

    /**
     * Extracts a license from the ByteArrayInputStream representing the
     * Properties file and puts it into thisFileLicense
     *
     * @param   bais            the ByteArrayInputStream containing the license
     * @param   thisFileLicense the MozLicense to hold the extracted license
     **/
    private void extractLicense(ByteArrayInputStream bais, MozLicense thisFileLicense)
            throws IOException {
        String line;
        String theLicense;
        int insertingPos;
        BufferedReader reader = new BufferedReader(new InputStreamReader(bais, "UTF-8"));
        boolean contribSectionFound = false;
        boolean contribEndLocated = false;

        // Let's find the license block start
        while (((line = reader.readLine()) != null) &&
                (line.indexOf("*** BEGIN LICENSE BLOCK ***") == -1)) {
        }

        theLicense = line + "\n";

        // Now, let's find the license block end, and while we are at it, locate
        // the start and end of Contributor(s) section
        while ((line != null) && (line.indexOf("*** END LICENSE BLOCK ***") == -1)) {
             line = reader.readLine();

             // We have found the Contributor(s) section start
             if (!contribSectionFound && line.indexOf("Contributor") > -1) {
                 contribSectionFound = true;
             }

             // If the Contributor(s) section has been found, let's find its end
             if (contribSectionFound && !contribEndLocated && line.trim().equals("#")) {
                 contribEndLocated = true;
                 thisFileLicense.setInsertionPos(theLicense.length());
             }

             // Add this line to the license
             theLicense += line + "\n";
        }

        thisFileLicense.setLicenseBlock(theLicense);
        bais.reset();
    }


    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public  boolean hasMore() throws MozIOException {
        return enumeration.hasMoreElements();
    }

    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getKey() throws MozIOException {
        currentKey = (String) enumeration.nextElement();
        return currentKey;
    }

    /** {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public  String getValue() throws MozIOException {
        return (String) prop.get(currentKey);
    }

    /** {@inheritDoc}
     * @param dataObject {@inheritDoc}
     * @throws MozIOException {@inheritDoc}
     */
    @Override
    public void endRead(ImportExportDataObject dataObject) throws MozIOException {
        prop = null;
    }


    class SortedProperties extends java.util.Properties implements Enumeration {
        SortedProperties() {
        }

        public void load(InputStream inStream, LinkedHashMap commentMap) throws
                IOException {
            Matcher m;
            Pattern p;
            // The spec says that the file must be encoded using ISO-8859-1, but
            // we really need to read UTF-8. :-)
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "UTF-8"));
            EnhancedString line = new EnhancedString(reader.readLine());
            final int STATUS_NULL = 0;
            final int STATUS_COMMENT = 1;
            final int STATUS_L10N_COMMENT = 2;
            final int STATUS_KEY = 3;
            final int STATUS_VALUE = 4;
            final int STATUS_EOF = 99;
            int currentStatus = STATUS_NULL;
            int keyDelimiter;
            int pos;
            char c;
            String keyName = null;
            StringBuilder key = null;
            StringBuilder value = null;
            StringBuilder l10nComment = null;

            while (line.toString() != null) {
                line.set(line.ltrim());
                if (line.length() == 0) {
                    currentStatus = STATUS_NULL;
                    line.set(reader.readLine());
                    continue;
                }

                c = line.charAt(0);

                // If the line is a comment
                if ((c == '#') || (c == '!') || (c == ';')) {
                    // If previous line was a l10n comment, we're still on it,
                    // else this is just a comment
                    currentStatus = (currentStatus == STATUS_L10N_COMMENT) ?
                        STATUS_L10N_COMMENT : STATUS_COMMENT;

                    // If the line has "Localization note" inside it, mark it
                    currentStatus = (line.toUpperCase().contains("LOCALIZATION NOTE")) ?
                        STATUS_L10N_COMMENT : currentStatus;
                } else {
                    // We're definitely NOT reading a comment, since it doesn't
                    // start with # or !; it may be a new key or part of a value
                    // In the second case, currentStatus will be marking it
                    currentStatus = (currentStatus == STATUS_VALUE) ?
                        STATUS_VALUE : STATUS_KEY;
                }

                switch (currentStatus) {
                    case STATUS_NULL:
                        // We may need to do something here, as this really
                        // marks the end of an element processing
                        break;
                    case STATUS_COMMENT:
                        break;
                    case STATUS_L10N_COMMENT:
                        if (l10nComment != null) {
                            l10nComment.append("\n");
                            l10nComment.append(line);
                        } else {
                            l10nComment = new StringBuilder(line.toString());

                            /* The localization note format should be:
                             *   LOCALIZATION NOTE (key): comment
                             * comment may expand several lines
                             *
                             * However, sometimes no key is given, so we need
                             * to take two possible courses of action:
                             *
                             * - if key is given, set entityName to it
                             * - if no key is given, set entityName to null
                             *
                             * Since l10n notes get added to the map when a new
                             * key is parsed, setting entityName will result in
                             * using the parsed key for the l10n note. L10n notes
                             * written at the end of file without explicit
                             * key reference will be discarded.
                             */

                            p = Pattern.compile("LOCALIZATION NOTE\\s\\(((.+))\\)",
                                    Pattern.CASE_INSENSITIVE);
                            m = p.matcher(l10nComment);

                            if (m.find()) {
                                keyName = m.group(1);
                            }
                        }
                        break;
                    case STATUS_KEY:
                        key = new StringBuilder();

                        // IMPORTANT: we assume keys don't split over several lines
                        // Let's look for the key - value separator, usually '='
                        keyDelimiter = line.indexOf('=');
                        keyDelimiter = (keyDelimiter == -1) ?
                            line.indexOf(':') : keyDelimiter;
                        
                        // We may be dealing with INI files too, so we must manage
                        // INI section headers, converting
                        //   [Header]
                        // to
                        //   section.[Header]=[Header]
                        if ((keyDelimiter == -1) && (line.charAt(0)=='[')) {
                            line.set("section." + line + "=" + line);
                            keyDelimiter = line.indexOf('=');
                        }

                        // If no key delimiter has been found, it is likely a
                        // syntax error, but we will just ignore the line
                        if (keyDelimiter == -1) {
                            currentStatus = STATUS_NULL;
                            line.set(reader.readLine());
                            continue;
                        }

                        // The key is the left side of the delimiter, removing
                        // existing spaces
                        key.append(line.substring(0, keyDelimiter).trim());

                        // If we have a pending l10n comment, let's add it to the
                        // l10n notes map
                        if (l10nComment != null) {
                            keyName = (keyName == null) ? key.toString() : keyName;

                            commentMap.put(keyName, l10nComment.toString());
                            keyName = null;
                            l10nComment = null;
                        }

                        line.set(line.substring(keyDelimiter + 1));
                        currentStatus = STATUS_VALUE;
                        // No break here, since we want to process the remaining
                        // content of line as the value

                    case STATUS_VALUE:
                        if (value == null) {
                            pos = 0;
                            while (pos < line.length() &&
                                   Character.isWhitespace(line.charAt(pos)))
                                pos++;
                            value = new StringBuilder();
                        } else {
                            line.set(line.ltrim());
                            pos = 0;
                        }
                        
                        while (pos < line.length()) {
                            c = line.charAt(pos++);
                            if (c == '\\') {
                                if (pos != line.length()) {
                                    c = line.charAt(pos++);
                                    switch (c) {
                                        case 'n':
                                            value.append("\\n");
                                            break;
                                        case 't':
                                            value.append("\\t");
                                            break;
                                        case 'r':
                                            value.append("\\r");
                                            break;
                                        case 'u':
                                            // Hack to deal with shorter than 4 digits Unicode sequences
                                            int unicodeSequenceEnd = pos;

                                            while ((unicodeSequenceEnd < line.length())
                                                    && (unicodeSequenceEnd < (pos + 4))
                                                    && ("0123456789ABCDEFabcdef".indexOf(line.charAt(unicodeSequenceEnd)) != -1)) {
                                                unicodeSequenceEnd++;
                                            }

                                            char uni = (char) Integer.parseInt(line.substring(pos, unicodeSequenceEnd), 16);
                                            value.append(uni);
                                            pos = unicodeSequenceEnd;
                                            break;
                                        default:
                                            value.append(c);
                                            break;
                                    }
                                } else {
                                    value.append('\\');
                                    value.append('\n');
                                }
                            } else {
                                value.append(c);
                            }
                        }
                        
                        if ((line.length() == 0) ||
                                (line.charAt(line.length() - 1) != '\\')) {
                            
                            put(key.toString(), value.toString());
                            currentStatus = STATUS_NULL;
                            key = null;
                            value = null;
                        }
                }

                line.set(reader.readLine());
            }

            // If we have a pending l10n comment with a key name, let's add it
            // to the l10n notes map
            if ((l10nComment != null) && (keyName != null)) {
                keyName = (keyName == null) ? key.toString() : keyName;

                commentMap.put(keyName, l10nComment.toString());
            }
        }

        @Override
        public Object put(Object key,Object value) {
            return sortedMap.put(key,value);
        }

        @Override
        public Object get(Object key) {
            return sortedMap.get(key);
        }

        @Override
        public Enumeration keys() {
            hiddenIterator =sortedMap.keySet().iterator();
            return this;
        }

        @Override
        public boolean hasMoreElements() {
            return hiddenIterator.hasNext();
        }

        @Override
        public Object nextElement() {
            return hiddenIterator.next();
        }

        private Iterator hiddenIterator;
        private LinkedHashMap sortedMap = new LinkedHashMap();
    }
}
