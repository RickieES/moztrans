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
 * Ricardo Palomares (modifications to preserve original licenses)
 *
 */
package org.mozillatranslator.io.file;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.*;
import org.mozillatranslator.io.common.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;

/**
 * This class is used as a base class, when making format specific readers and
 * writers. It hold all the common functionality like traversing and getting the
 * right value to read/write depending on the locale.
 *
 * It calls its protected abstract method for all things that are format or
 * filetype dependent.
 *
 * Load semantics are like this:
 * <CODE>
 * beginRead(Dataobject);
 *
 * while (hasMore())
 * {
 *    getKey();
 *    getValue();
 * }
 * endRead(dataObject);
 * </CODE>
 *
 *
 * Save semantic:
 * <CODE>
 * beginWrite(dataObject);
 * while (...)
 * {
 *    writeLine(key,value);
 * }
 * endWrite(dataObject);
 * </CODE>
 *
 * @author Henrik Lynggaard
 **/
public abstract class FileAccessAdapter implements FileAccess {
    protected LinkedHashMap commentMap;
    private static final Logger fLogger = Logger.getLogger(
            FileAccessAdapter.class.getPackage().getName());

    /***************************************************************************
     * Saving files
     **************************************************************************/

    /**
     * This method is the public method that does the save semantics
     *
     * @param dataObject The dataObject passed to the subclass methods.
     **/
    @Override
    public void save(ImportExportDataObject dataObject) {
        Phrase currentPhrase;
        Translation currentTranslation;
        String key;
        String value;
        boolean original;

        original = dataObject.getL10n().equals(Kernel.ORIGINAL_L10N) ? true : false;
        try {
            // The following beginWrite method is overriden by every specific
            // file access (like DTDAccess or PropertiesAccess)
            beginWrite(dataObject);

            List childs = dataObject.getNode().getAllChildren();
            Collections.sort(childs,new FileAccessAdaptorComparator());
            Iterator fileIterator = childs.iterator();

            while (fileIterator.hasNext()) {
                currentPhrase = (Phrase) fileIterator.next();
                currentTranslation = (Translation) currentPhrase.getChildByName(
                        dataObject.getL10n());
                key = currentPhrase.getName();

                if (original || currentTranslation == null || currentPhrase.isKeepOriginal()) {
                    value = currentPhrase.getText();
                } else {
                    value = currentTranslation.getText();
                }
                // The following writeLine method is overriden by every specific
                // file access (like DTDAccess or PropertiesAccess)
                writeLine(key, value);
            }

            // The following endWrite method is overriden by every specific
            // file access (like DTDAccess or PropertiesAccess)
            endWrite(dataObject);
        } catch (MozIOException e) {
            fLogger.log(Level.SEVERE, "Unable to write file {0}", e.getMessage());
        }
    }


    /**
     * This method is called in the beginning of a save of the a file.The
     * implementation can use this to setup objects before the individual writes.
     *
     * @param   dataObject      the DataObject from the save() method
     * @throws  MozIOException  is thrown if some io problem prevents the writing
     **/
    protected abstract void beginWrite(ImportExportDataObject dataObject) throws
            MozIOException;

    /**
     * This method is called for every phrase in the file.The implementation must
     * write the phrase to a file, or store it locally until the endWrite call.
     *
     * @param   key             the key to write
     * @param   value           the value to write
     * @throws  MozIOException  ts thrown if some io problem prevents the writing
     **/
    protected abstract void writeLine(String key, String value) throws MozIOException;

    /**
     * The method is called after all the phrases have been written. The
     * implementation must clean up after itself and write any remaining data to
     * the disk.
     *
     * @param   dataObject      the DataObject from the save() method
     * @throws  MozIOException  is thrown if some io problem prevents the writing
     */
    protected abstract void endWrite(ImportExportDataObject dataObject) throws
            MozIOException;

    /***************************************************************************
     * Loading files
     **************************************************************************/


    /**
     * This method is the public method that does the load semantics
     *
     * @param   dataObject  the dataObject passed to the subclass methods
     **/
    @Override
    public void load(ImportExportDataObject dataObject) {
        String key = null;
        String value = null;
        String l10nNote = null;
        MozFile currentFile = null;
        Phrase currentPhrase = null;
        Translation currentTranslation = null;
        boolean original;

        try {
            original = dataObject.getL10n().equals(Kernel.ORIGINAL_L10N);
            currentFile = (MozFile) dataObject.getNode();
            int sortIndex = 0;

            // The following beginRead method is overriden by every specific
            // file access (like DTDAccess or PropertiesAccess)
            beginRead(dataObject);

            if ((commentMap != null) && (commentMap.size() == 0)) {
                // If there are no l10n comments, let's save the load of looking
                // for them for every entity/key in this file
                commentMap = null;
            }

            while (hasMore()) {
                key   = getKey();
                value = getValue();
                currentPhrase = (Phrase) currentFile.getChildByName(key);
                if (original) {
                    // TODO check version
                    if (key.indexOf("lang.version") > -1) {
                        Kernel.settings.setString(Settings.STATE_VERSION, value);
                    }

                    if (currentPhrase == null) {
                        currentPhrase = new Phrase(key, currentFile, value);
                        currentFile.addChild(currentPhrase);
                        dataObject.getChangeList().add(currentPhrase);
                        findConnections(currentPhrase, currentFile);
                    } else {
                        if (!currentPhrase.getText().equals(value)) {
                            currentPhrase.setFilterResult(
                                    "Update info: previous original value was: \n" +
                                    "[" + currentPhrase.getText() + "]");
                            currentPhrase.setText(value);
                            dataObject.getChangeList().add(currentPhrase);
                            findConnections(currentPhrase, currentFile);
                        }
                    }

                    if (commentMap != null) {
                        l10nNote = (String) commentMap.get(key);
                        if (l10nNote != null) {
                            fLogger.log(Level.INFO, "Comment found for {0}", key);
                            currentPhrase.setLocalizationNote(l10nNote);
                        }
                    }

                    currentPhrase.setSort(sortIndex++);
                    fLogger.log(Level.FINE, "Set Phrase {0} to sort{1}",
                            new Object[]{currentPhrase, sortIndex - 1});
                    currentPhrase.setMark();
                } else {
                    if (currentPhrase != null) {
                        currentTranslation = (Translation) currentPhrase.getChildByName(
                                dataObject.getL10n());

                        if (!value.equals(currentPhrase.getText())) {
                            if (currentTranslation == null) {
                                currentTranslation = new Translation(dataObject.getL10n(),
                                        currentPhrase, value, TrnsStatus.Untranslated);
                                currentPhrase.addChild(currentTranslation);
                                dataObject.getChangeList().add(currentPhrase);
                            } else {
                                if (!currentTranslation.getText().equals(value)) {
                                    currentTranslation.setText(value);
                                    dataObject.getChangeList().add(currentPhrase);
                                }
                            }
                        }
                    }
                }
            }

            // The following endRead method is overriden by every specific
            // file access (like DTDAccess or PropertiesAccess)
            endRead(dataObject);
        } catch (MozIOException e) {
            fLogger.log(Level.SEVERE, "Unable to read file: {0}", e.getMessage());
        }
    }


    /**
     * Finds accesskeys and commandkeys related to label phrases
     *
     * @param   currentPhrase   the current phrase, whether it is a label, an
     *                          accesskey or a commandkey
     * @param   currentFile     the current file
     **/
    private void findConnections(Phrase currentPhrase, GenericFile currentFile) {
        if (currentPhrase.isLabel()) {
            currentPhrase.linkLabel2Keys(true);
        } else if (currentPhrase.isAccesskey()) {
            currentPhrase.linkAKey2Label();
        } else if (currentPhrase.isCommandkey()) {
            currentPhrase.linkCKey2Label();
        }
    }


    /**
     * This method is called before the reading starts. the implementation can
     * use this to setup objects if needed.
     *
     * @param   dataObject      the DataObject from the load() method
     * @throws  MozIOException  is thrown if some io problem prevents the reading
     **/
    protected abstract void beginRead(ImportExportDataObject dataObject) throws MozIOException;


    /**
     * This is called to check if there are more phrases waiting in the file to
     * be read. The implementation must check, if there are more phrases to be
     * read, and advance to the next if there is any.
     *
     * @throws  MozIOException  is thrown if some io problem prevents the reading
     * @return  true if there are more phrases, false otherwise
     **/
    protected abstract boolean hasMore() throws MozIOException;


    /**
     * This is called after a successfull call to hasMore(). It retrieves the key
     * for storing in the phrase
     *
     * @throws  MozIOException  is thrown if some io problem prevents the reading
     * @return  the key for this phrase
     **/
    protected abstract String getKey() throws MozIOException;


    /**
     * This is called after a successful call to hasMore(). It retrieves the
     * value for storing in the phrase
     *
     * @throws  MozIOException  is thrown if some io problem prevents the reading
     * @return  the value for this phrase
     **/
    protected abstract String getValue() throws MozIOException;


    /**
     * The method is called after all the phrases have been read. The
     * implementation must clean up after itself.
     *
     * @param   dataObject      the DataObject from the load() method
     * @throws  MozIOException  is thrown if some io problem prevents the reading
     */
    protected abstract void endRead(ImportExportDataObject dataObject) throws
            MozIOException;

    class FileAccessAdaptorComparator implements java.util.Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Phrase p1 = (Phrase) o1;
            Phrase p2 = (Phrase) o2;
            int i1 = p1.getSort();
            int i2 = p2.getSort();

            return (i1==i2) ? 0 : ((i1<i2) ? -1 : 1);
        }
    }
}
