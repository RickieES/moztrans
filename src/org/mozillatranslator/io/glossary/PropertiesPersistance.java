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
 * Ricardo Palomares (refactoring to remove subcomponent from the datamodel)
 */
package org.mozillatranslator.io.glossary;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.StructureAccess;
import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.mozillatranslator.dataobjects.LoadGlossaryDataObject;

/** This classs load and saves the datamodel in a properties file
 *
 * <h3>Change List</h3>
 *
 *
 *
 * @author Henrik Lynggaard
 * @version 1.1
 */
public class PropertiesPersistance implements GlossaryAccess {

    private static final String PERSISTANCE_VERSION_KEY = "glossary_version";
    private static final String PERSISTANCE_VERSION_VALUE = "5.30";
    private static final String PLATFORM = ".P";
    private static final String REGION = ".R";
    private static final String CUSTOM = ".C";
    private static final String NAME = ".name";
    private static final String JARFILE = ".jarFile";
    private static final String COUNT = ".count";
    private static final String ALTERED = ".alteredTime";
    private static final String TYPE = ".nodetype";
    private static final String PRODUCT_COUNT = "product_count";
    private static final String PRODUCT_CVSIMPORTORIGINAL = ".cvs_import_original_path";
    private static final String PRODUCT_CVSIMPORTTRANSLATION = ".cvs_import_translation_path";
    private static final String PRODUCT_CVSEXPORTTRANSLATION = ".cvs_export_translation_path";
    private static final String PRODUCT_LOCALE_ABCD_ONLY = ".consider_locale_abcd_only";
    private static final String PRODUCT_GLOSSARY_FILENAME = ".glossary.filename";
    private static final String PLATFORM_ISNEUTRAL = ".isNeutral";
    private static final String PLATFORM_TYPE = ".type";
    private static final String COMPONENT_EXPORTTO = ".exportTo";
    private static final String FILE_LICENSE = ".license";
    private static final String FILE_LICENSE_BLOCK = ".license_block";
    private static final String FILE_LICENSE_CONTRIB = ".license_contributor";
    private static final String FILE_LICENSE_INSPOINT = ".license_inspoint";
    private static final String FILE_DONTEXPORT = ".dontexport";
    private static final String BINFILE_MD5 = ".md5";
    private static final String ENTITY_BAG = ".E";
    private static final String PUBLICID = ".publicId";
    private static final String SYSTEMID = ".systemId";
    private static final String PHRASE_KEY = ".key";
    private static final String PHRASE_TEXT = ".text";
    private static final String PHRASE_KEEPORIGINAL = ".keep";
    private static final String PHRASE_FUZZY = ".fuzzy";
    private static final String PHRASE_ACCESSCON = ".access";
    private static final String PHRASE_COMMANDCON = ".command";
    private static final String PHRASE_LABELCON = ".label";
    private static final String PHRASE_SORT = ".sort";
    private static final String PHRASE_L10NNOTE = ".L10n_note";
    private static final String TRANSLATION_STATUS = ".status";
    private static final String TRANSLATION_TEXT = ".text";
    private static final String TRANSLATION_COMMENT = ".comment";
    private static final String CUSTOM_REAL = ".realfile";
    private static final String CUSTOM_RELATIVE = ".relative";

    /** Creates new PropertiesPersistance */
    public PropertiesPersistance() {
    }

    /**
     * Save the entire datamodel as a glossary
     */
    @Override
    public void saveEntireGlossary() {
        DataModel datamodel = Kernel.datamodel;
        Product currentProduct;
        int productCount = 0;
        Iterator productIterator;
        Properties model = new Properties();
        File bkf = new File("Glossary.bkf");
        File gls = new File(Kernel.settings.getString(Settings.DATAMODEL_FILENAME, "Glossary.zip"));
        FileOutputStream fos;
        ZipOutputStream zos;
        ZipEntry ze;

        if (bkf.exists()) {
            bkf.delete();
        }

        if (gls.exists()) {
            gls.renameTo(bkf);
        }

        // Initialize the Properties file ("glossary.txt") in which we are
        // saving the datamodel
        model.clear();
        model.setProperty(PRODUCT_COUNT, "" + datamodel.getSize());
        model.setProperty(PERSISTANCE_VERSION_KEY, PERSISTANCE_VERSION_VALUE);

        Kernel.feedback.progress("Saving glossary file");
        try {
            fos = new FileOutputStream(Kernel.settings.getString(Settings.DATAMODEL_FILENAME, "Glossary.zip"));
            zos = new ZipOutputStream(fos);

            // Each glossary may have one or more products; let's iterate over them
            productIterator = datamodel.productIterator();
            while (productIterator.hasNext()) {
                currentProduct = (Product) productIterator.next();

                if (Kernel.settings.getBoolean(Settings.DATAMODEL_ONE_FILE_PER_PRODUCT)) {
                    Properties singleProductModel = new Properties();
                    singleProductModel.clear();
                    saveProduct(singleProductModel, currentProduct, "" + productCount);
                    String prodGlossFilename = storeModelInZip(zos, productCount, singleProductModel);
                    singleProductModel.clear();
                    model.setProperty("" + productCount + PRODUCT_GLOSSARY_FILENAME, prodGlossFilename);
                } else {
                    saveProduct(model, currentProduct, "" + productCount);
                }
                productCount++;
            }

            ze = new ZipEntry("glossary.txt");
            zos.putNextEntry(ze);
            model.store(zos, "Translated with MozillaTranslator "
                    + Kernel.settings.getString(Settings.SYSTEM_VERSION,
                    "(unknown version)"));
            zos.closeEntry();
            writeImages(zos);
            zos.close();
        } catch (OutOfMemoryError e) {
            Kernel.feedback.progress("Out of memory while saving "
                    + Kernel.settings.getString(Settings.DATAMODEL_FILENAME, "Glossary.zip")
                    + "; trying to save glossary.txt (i.e., glossary except images)");
            try {
                fos = new FileOutputStream("glossary.txt");
                model.store(fos, "Translated with MozillaTranslator "
                        + Kernel.settings.getString(Settings.SYSTEM_VERSION,
                        "(unknown version)"));
                fos.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(Kernel.mainWindow.getRootPane(),
                        "Error saving glossary.txt; data has NOT been saved",
                        "Critical error", JOptionPane.ERROR_MESSAGE);
                Kernel.appLog.log(Level.SEVERE, "Logging under development", ex);
            }
        } catch (IOException e) {
            Kernel.appLog.log(Level.SEVERE, "Logging under development", e);
        }
        model.clear();
    }

    private String storeModelInZip(ZipOutputStream zos, int productCount, Properties singleProductModel)
        throws OutOfMemoryError, IOException {

        String productGlossaryFilename = "product_glossary." + productCount + ".txt";
        ZipEntry ze = new ZipEntry(productGlossaryFilename);
        zos.putNextEntry(ze);
        singleProductModel.store(zos, "Translated with MozillaTranslator "
                + Kernel.settings.getString(Settings.SYSTEM_VERSION,
                "(unknown version)"));
        zos.closeEntry();
        return productGlossaryFilename;
    }

    private void saveProduct(Properties store, Product currentProduct, String productPrefix) {
        Platform currentPlatform;
        Iterator platformIterator;
        int platformCount = 0;

        Kernel.feedback.progress("Saving product: " + currentProduct.getName());

        // Do Platform Neutral
        writePlatform(store, currentProduct.getNeutralPlatform(), productPrefix + ".P.0");
        platformCount++;

        // Do the rest of platforms
        platformIterator = currentProduct.platformIterator();
        while (platformIterator.hasNext()) {
            currentPlatform = (Platform) platformIterator.next();
            writePlatform(store, currentPlatform, productPrefix + ".P." + platformCount);
            platformCount++;
        }

        // Now we know how many platforms we have saved
        store.setProperty(productPrefix + COUNT, "" + platformCount);

        // Do regional
        writeRegional(store, currentProduct.getRegional(), productPrefix + ".R");

        // Do custom
        writeCustom(store, currentProduct.getCustomContainer(), productPrefix + ".C");

        // Finally, let's save product specifics
        // Product name
        store.setProperty(productPrefix + NAME, currentProduct.getName());

        // Product altered time
        store.setProperty(productPrefix + ALTERED, "" + currentProduct.getAlteredTime());

        // Product CVS Import original path
        // TODO: next version should include "." into the CVS properties literals
        store.setProperty(productPrefix  + PRODUCT_CVSIMPORTORIGINAL,
                "" + currentProduct.getCVSImportOriginalPath());

        // Product CVS Import Translation path
        store.setProperty(productPrefix + PRODUCT_CVSIMPORTTRANSLATION,
                "" + currentProduct.getCVSImportTranslationPath());

        // Product CVS Export translation path
        store.setProperty(productPrefix + PRODUCT_CVSEXPORTTRANSLATION,
                "" + currentProduct.getCVSExportTranslationPath());

        // Product Consider files under /locale/ab-CD only (currently used only in JAR mode)
        store.setProperty(productPrefix  + PRODUCT_LOCALE_ABCD_ONLY,
                "" + currentProduct.isOnlyLocaleAbCD());

    }

    /**
     * Save a component of the datamodel (including its children) as a partial
     * glossary
     *
     * @param   currentNode     the component to be saved
     * @param   exportFile      the real file in which the partial glossary will
     *                          be saved
     * @author  Ricardo
     */
    public void savePartialGlossary(Component currentNode, File exportFile) {
        Properties model = new Properties();
        FileOutputStream fos;
        ZipOutputStream zos;
        ZipEntry ze;
        int childrenCount;
        String prefix = "PARTIAL";

        // Initialize the Properties file ("partial.txt") in which we are
        // saving the component
        model.clear();

        childrenCount = writeContent(model, currentNode, prefix);
        model.setProperty(prefix + COUNT, "" + childrenCount);
        model.setProperty(PERSISTANCE_VERSION_KEY, PERSISTANCE_VERSION_VALUE);

        try {
            fos = new FileOutputStream(exportFile);
            zos = new ZipOutputStream(fos);
            ze = new ZipEntry("partial.txt");
            zos.putNextEntry(ze);
            model.store(zos, "Translated with MozillaTranslator " + Kernel.settings.getString(Settings.SYSTEM_VERSION, "(unknown version)"));
            zos.closeEntry();
            zos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesPersistance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesPersistance.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.clear();
    }

    /**
     * Write a platform, either Neutral or not, in the glossary, including all
     * of its descendants
     *
     * @param currentPlatform   the platform to be saved
     * @param prefix            the prefix that identifies the platform in the
     *                          property key
     */
    private void writePlatform(Properties model, Platform currentPlatform, String prefix) {
        int componentCount;

        componentCount = writeContent(model, currentPlatform, prefix);

        model.setProperty(prefix + COUNT, "" + componentCount);
        model.setProperty(prefix + JARFILE, currentPlatform.getJarFile());
        model.setProperty(prefix + NAME, currentPlatform.getName());
        model.setProperty(prefix + PLATFORM_ISNEUTRAL,
                "" + currentPlatform.isNeutral());
        model.setProperty(prefix + ALTERED, "" + currentPlatform.getAlteredTime());
        model.setProperty(prefix + PLATFORM_TYPE, "" + currentPlatform.getType());
    }

    /**
     * Write the regional content in the glossary, including all of its descendants
     *
     * @param currentRegion     the region to be saved
     * @param prefix            the prefix that identifies the region in the
     *                          property key
     */
    private void writeRegional(Properties model, Region currentRegion, String prefix) {
        int componentCount;

        componentCount = writeContent(model, currentRegion, prefix);
        model.setProperty(prefix + COUNT, "" + componentCount);
        model.setProperty(prefix + JARFILE, currentRegion.getJarFile());
        model.setProperty(prefix + NAME, currentRegion.getName());
        model.setProperty(prefix + ALTERED, "" + currentRegion.getAlteredTime());
    }

    /**
     * Write the custom container in the glossary (including all of its descendants)
     *
     * @param localroot     the custom container to be saved
     * @param prefix        the prefix that identifies the custom container in
     *                      the property key
     */
    private void writeCustom(Properties model, CustomContainer localroot, String prefix) {
        Iterator fileIterator;
        MozFile currentFile;
        String filePrefix;
        int phraseCount;

        int fileCount = 0;
        fileIterator = localroot.iterator();
        while (fileIterator.hasNext()) {
            currentFile = (MozFile) fileIterator.next();
            currentFile.increaseReferenceCount();
            filePrefix = prefix + "." + fileCount;

            phraseCount = writeFile(model, currentFile, filePrefix);

            model.setProperty(filePrefix + COUNT, "" + phraseCount);
            model.setProperty(filePrefix + NAME, currentFile.getName());
            model.setProperty(filePrefix + ALTERED, "" + currentFile.getAlteredTime());
            model.setProperty(filePrefix + CUSTOM_REAL, currentFile.getRealFilename());
            model.setProperty(filePrefix + CUSTOM_RELATIVE,
                    currentFile.getRelativeFilename());

            fileCount++;
            currentFile.decreaseReferenceCount();
        } // end of file loop
        model.setProperty(prefix + COUNT, "" + fileCount);
    }

    /**
     * Write a component (a dir, possibly with subdirs) in the glossary,
     * including all of its descendants
     *
     * @param currentNode   the component to be saved
     * @param prefix        the prefix that identifies the component in the
     *                      property key
     */
    private int writeContent(Properties model, TreeNode currentNode, String prefix) {
        Iterator fileOrComponentIterator, entityIterator;

        Object o;
        Component currentComponent;
        MozFile currentFile;
        ExternalEntity curEntity;

        int thisLevelCount;
        int subLevelCount;
        int entityCount;

        String thisLevelPrefix;
        String subLevelType;

        // code begins
        thisLevelCount = 0;
        fileOrComponentIterator = currentNode.iterator();
        while (fileOrComponentIterator.hasNext()) {
            o = fileOrComponentIterator.next();
            thisLevelPrefix = prefix + "." + thisLevelCount;

            // If this is a component, recurse over it; otherwise, process the file
            if (o instanceof Component) {
                subLevelType = Component.TYPE_DIR;
                currentComponent = (Component) o;
                subLevelCount = writeContent(model, currentComponent, thisLevelPrefix);

                if (currentComponent.getExportedToDir() != null) {
                    model.setProperty(thisLevelPrefix + COMPONENT_EXPORTTO,
                            currentComponent.getExportedToDir());
                }
            } else {
                subLevelType = Component.TYPE_FILE;
                currentFile = (MozFile) o;
                currentFile.increaseReferenceCount();
                subLevelCount = writeFile(model, currentFile, thisLevelPrefix);

                // Save license and external entities information
                if (currentFile != null) {

                    // Old license information
                    if (currentFile.getLicenseFile() != null
                            && !"".equals(currentFile.getLicenseFile())) {
                        model.setProperty(thisLevelPrefix + FILE_LICENSE,
                                currentFile.getLicenseFile());
                    }

                    // New license information
                    if (currentFile.getLicenseBlock() != null) {
                        model.setProperty(thisLevelPrefix + FILE_LICENSE_BLOCK,
                                currentFile.getLicenseBlock().getLicenseBlock());

                        if (currentFile.getLicenseBlock().getLicenseContributor() != null) {
                            model.setProperty(thisLevelPrefix + FILE_LICENSE_CONTRIB,
                                    currentFile.getLicenseBlock().getLicenseContributor());
                        }

                        model.setProperty(thisLevelPrefix + FILE_LICENSE_INSPOINT,
                                "" + currentFile.getLicenseBlock().getInsertionPos());
                    }

                    model.setProperty(thisLevelPrefix + FILE_DONTEXPORT,
                            "" + currentFile.isDontExport());

                    // If the file is a DTD, maybe it has external entities to be saved
                    if ((currentFile instanceof DTDFile)
                            && (((DTDFile) currentFile).getExternalEntities() != null)) {
                        entityIterator = ((DTDFile) currentFile).getExternalEntities().
                                iterator();
                        entityCount = 0;
                        while (entityIterator.hasNext()) {
                            curEntity = (ExternalEntity) entityIterator.next();
                            model.setProperty(thisLevelPrefix + ENTITY_BAG + "."
                                    + entityCount + NAME, curEntity.getName());
                            model.setProperty(thisLevelPrefix + ENTITY_BAG + "."
                                    + entityCount + PUBLICID, curEntity.getPublicId());
                            model.setProperty(thisLevelPrefix + ENTITY_BAG + "."
                                    + entityCount + SYSTEMID, curEntity.getSystemId());
                            entityCount++;
                        }
                        model.setProperty(thisLevelPrefix + ENTITY_BAG
                                + COUNT, "" + entityCount);
                    }

                    // If the file is a binary file, we have to save its MD5 hash
                    if (currentFile instanceof BinaryFile) {
                        model.setProperty(thisLevelPrefix + BINFILE_MD5,
                                ((BinaryFile) currentFile).getMd5Hash());
                    }
                }
                currentFile.decreaseReferenceCount();
            }

            model.setProperty(thisLevelPrefix + COUNT, "" + subLevelCount);
            model.setProperty(thisLevelPrefix + NAME, ((MozTreeNode) o).getName());
            model.setProperty(thisLevelPrefix + ALTERED, "" + ((MozTreeNode) o).getAlteredTime());
            model.setProperty(thisLevelPrefix + TYPE, subLevelType);

            thisLevelCount++;
        } // end of component loop
        return thisLevelCount;
    }

    /**
     * Write a file, including its phrases and translations
     *
     * @param currentFile the datamodel.File object to be saved
     * @param prefix      the string to be prepended to form the key identifier
     *                    for the property in glossary.txt
     * @return int        the number of phrases included in the file
     **/
    private int writeFile(Properties model, MozFile currentFile, String filePrefix) {
        int phraseCount = 0;
        int translationCount;

        String phrasePrefix;
        String translationPrefix;

        Phrase currentPhrase;
        Translation currentTranslation;

        Iterator phraseIterator = currentFile.iterator();
        Iterator translationIterator;

        while (phraseIterator.hasNext()) {
            currentPhrase = (Phrase) phraseIterator.next();
            phrasePrefix = filePrefix + "." + phraseCount;

            translationCount = 0;
            translationIterator = currentPhrase.iterator();
            while (translationIterator.hasNext()) {
                currentTranslation = (Translation) translationIterator.next();
                translationPrefix = phrasePrefix + "." + translationCount;

                model.setProperty(translationPrefix + NAME,
                        currentTranslation.getName());
                model.setProperty(translationPrefix + TRANSLATION_STATUS,
                        "" + currentTranslation.getStatus());
                model.setProperty(translationPrefix + TRANSLATION_TEXT,
                        currentTranslation.getText());
                if (currentTranslation.getComment() != null) {
                    model.setProperty(translationPrefix + TRANSLATION_COMMENT,
                            currentTranslation.getComment());
                }
                model.setProperty(translationPrefix + ALTERED,
                        "" + currentTranslation.getAlteredTime());

                translationCount++;
            } // end of translation loop

            model.setProperty(phrasePrefix + COUNT, "" + translationCount);
            model.setProperty(phrasePrefix + PHRASE_KEY, currentPhrase.getName());
            if (currentPhrase.getSort() > -1) {
                model.setProperty(phrasePrefix + PHRASE_SORT, "" + currentPhrase.getSort());
            }
            model.setProperty(phrasePrefix + PHRASE_TEXT, currentPhrase.getText());
            model.setProperty(phrasePrefix + PHRASE_KEEPORIGINAL,
                    "" + currentPhrase.isKeepOriginal());
            model.setProperty(phrasePrefix + PHRASE_FUZZY,
                    "" + currentPhrase.isFuzzy());
            model.setProperty(phrasePrefix + ALTERED,
                    "" + currentPhrase.getAlteredTime());

            if (currentPhrase.getLocalizationNote() != null) {
                model.setProperty(phrasePrefix + PHRASE_L10NNOTE,
                        currentPhrase.getLocalizationNote());
            }

            if (currentPhrase.getAccessConnection() != null
                    && currentPhrase.getAccessConnection().isAccesskey()) {
                model.setProperty(phrasePrefix + PHRASE_ACCESSCON,
                        "" + currentPhrase.getAccessConnection().getName());
            }
            if (currentPhrase.getCommandConnection() != null
                    && currentPhrase.getCommandConnection().isCommandkey()) {
                model.setProperty(phrasePrefix + PHRASE_COMMANDCON,
                        ""
                        + currentPhrase.getCommandConnection().getName());
            }
            if (currentPhrase.getLabelConnection() != null
                    && currentPhrase.getLabelConnection().isLabel()) {
                model.setProperty(phrasePrefix + PHRASE_LABELCON,
                        "" + currentPhrase.getLabelConnection().getName());
            }
            phraseCount++;
        } // end of Phrase  loop

        return phraseCount;
    }


    /*
     * Loading rutines
     */
    /**
     * Load the datamodel from the glossary
     *
     * @param data  a container for the data needed for loading a glossary
     *              (namely, a filename)
     **/
    @Override
    public void loadEntireGlossary(LoadGlossaryDataObject data) {
        Properties model = new Properties();
        FileInputStream fis;
        InputStream is;
        ZipFile zipfile;
        ZipEntry ze;
        File testing;
        HashMap<String, ZipEntry> entryList = new HashMap<String, ZipEntry>();
        int productMax;
        String name;
        String productGlossaryFile;

        model.clear();

        try {
            testing = data.getRealFile();
            if (testing.exists()) {
                Kernel.feedback.progress("Loading glossary file into memory");
                fis = new FileInputStream(data.getFileName());
                zipfile = new ZipFile(data.getFileName());

                // We load the ZipEntries into the HashMap
                Enumeration<? extends ZipEntry> zipEntriesList = zipfile.entries();
                while (zipEntriesList.hasMoreElements()) {
                    ze = zipEntriesList.nextElement();
                    entryList.put(ze.getName(), ze);
                }

                if (entryList.get("glossary.txt") == null) {
                    throw new Exception("No glossary.txt inside " + data.getFileName());
                }

                is = zipfile.getInputStream(entryList.get("glossary.txt"));
                model.load(is);
                is.close();

                productMax = Integer.parseInt(model.getProperty(PRODUCT_COUNT));
                for (int productCount = 0; productCount < productMax; productCount++) {
                    // We try to read the product's individual glossary filename property
                    productGlossaryFile = model.getProperty("" + productCount + PRODUCT_GLOSSARY_FILENAME, null);
                    if (productGlossaryFile != null) {
                        if (entryList.get(productGlossaryFile) == null) {
                            throw new Exception("Missing " + productGlossaryFile + " in " + data.getFileName());
                        }
                        is = zipfile.getInputStream(entryList.get(productGlossaryFile));
                        Properties productModel = new Properties();
                        productModel.load(is);
                        loadProduct(productModel, productCount);
                    } else {
                        loadProduct(model, productCount);
                    }
                }
                readImages();
                System.gc();
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error loading glossary", e);
        }
    }

    private void loadProduct(Properties model, int productCount) {
        int platformMax;
        Product currentProduct;
        String name;
        long alteredTime;

        name = model.getProperty("" + productCount + NAME);
        Kernel.feedback.progress("Loading product: " + name);
        alteredTime = Long.parseLong("0" + model.getProperty(""
                + productCount + ALTERED, ""));
        platformMax = Integer.parseInt(model.getProperty(""
                + productCount + COUNT));

        currentProduct = new Product(name);
        currentProduct.setAlteredTime(alteredTime);

        currentProduct.setCVSImportOriginalPath(model.getProperty(""
                + productCount + PRODUCT_CVSIMPORTORIGINAL));

        // Product CVS Import Translation path
        if (model.getProperty("" + productCount + PRODUCT_CVSIMPORTTRANSLATION) != null) {
            currentProduct.setCVSImportTranslationPath(model.getProperty(""
                    + productCount + PRODUCT_CVSIMPORTTRANSLATION));
        } else {
            currentProduct.setCVSImportTranslationPath(model.getProperty(""
                    + productCount + "." + PRODUCT_CVSIMPORTTRANSLATION));
        }

        // Product CVS Export translation path
        if (model.getProperty("" + productCount + PRODUCT_CVSEXPORTTRANSLATION) != null) {
            currentProduct.setCVSExportTranslationPath(model.getProperty(""
                    + productCount + PRODUCT_CVSEXPORTTRANSLATION));
        } else {
            currentProduct.setCVSExportTranslationPath(model.getProperty(""
                    + productCount + "." + PRODUCT_CVSEXPORTTRANSLATION));
        }

        currentProduct.setOnlyLocaleAbCD(Boolean.valueOf(
                model.getProperty("" + productCount + PRODUCT_LOCALE_ABCD_ONLY,
                "true")).booleanValue());

        Kernel.datamodel.addProduct(currentProduct);
        for (int platformCount = 0; platformCount < platformMax;
                platformCount++) {
            readPlatform(model, currentProduct, "" + productCount + ".P."
                    + platformCount);
        }
        readRegional(model, currentProduct, "" + productCount + ".R");
        readCustom(model, currentProduct.getCustomContainer(),
                "" + productCount + ".C");

        // FIXME maybe there is better way to handle versions
        currentProduct.setVersion(Kernel.settings.getString(Settings.STATE_VERSION));
    }

    /**
     * Loads a component from a partial glossary
     *
     * @param   currentNode     the component to be loaded
     * @param   importFile      the real file from which the partial glossary
     *                          will be loaded
     * @author  Ricardo
     */
    public void loadPartialGlossary(Component currentNode, File importFile) {
        Properties model = new Properties();
        FileInputStream fis;
        ZipInputStream zis;

        String prefix = "PARTIAL";
        model.clear();
        try {
            if (importFile.exists()) {
                fis = new FileInputStream(importFile);
                zis = new ZipInputStream(fis);
                zis.getNextEntry();
                model.load(zis);
                zis.closeEntry();
                zis.close();

                readContent(model, currentNode, prefix);
                System.gc();
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error loading partial glossary", e);
        }
    }

    /**
     * Read a platform from the glossary, including all of its descendants
     *
     * @param currentProduct    the product of which this platform is a child
     * @param prefix            the prefix that identifies the platform in the
     *                          property key
     */
    private void readPlatform(Properties model, Product currentProduct, String prefix) {
        String name;
        String jarFile;
        boolean neutral;

        Platform currentPlatform;
        long alteredTime;

        name = model.getProperty(prefix + NAME);
        jarFile = model.getProperty(prefix + JARFILE);
        alteredTime = Long.parseLong("0" + model.getProperty(prefix + ALTERED, ""));

        neutral = Boolean.valueOf(model.getProperty(prefix + PLATFORM_ISNEUTRAL)).booleanValue();
        int type = Integer.parseInt(model.getProperty(prefix + PLATFORM_TYPE,
                "" + ProductChild.TYPE_OTHER));

        if (neutral) {
            currentPlatform = currentProduct.getNeutralPlatform();
        } else {
            currentPlatform = new Platform(name, currentProduct, type);
            currentProduct.addChild(currentPlatform);
        }
        currentPlatform.setJarFile(jarFile);
        currentPlatform.setAlteredTime(alteredTime);
        readContent(model,currentPlatform, prefix);
    }

    /**
     * Read the regional content from the glossary, including all of its descendants
     *
     * @param currentProduct    the product of which the regional content is a child
     * @param prefix            the prefix that identifies the regional content in the
     *                          property key
     */
    private void readRegional(Properties model, Product currentProduct, String prefix) {
        String name;
        String jarFile;
        Region currentRegion;
        long alteredTime;

        name = model.getProperty(prefix + NAME);
        jarFile = model.getProperty(prefix + JARFILE);
        alteredTime = Long.parseLong("0" + model.getProperty(prefix + ALTERED, ""));

        currentRegion = currentProduct.getRegional();
        currentRegion.setJarFile(jarFile);
        currentRegion.setAlteredTime(alteredTime);
        readContent(model, currentRegion, prefix);
    }

    /**
     * Read custom container from the glossary, including all of its descendants
     *
     * @param localroot     the product of which the custom container is a child
     * @param prefix        the prefix that identifies the custom container in the
     *                      property key
     */
    private void readCustom(Properties model, CustomContainer localroot, String prefix) {
        int fileMax;
        String filePrefix;

        fileMax = Integer.parseInt(model.getProperty("" + prefix + COUNT));
        for (int fileCount = 0; fileCount < fileMax; fileCount++) {
            filePrefix = prefix + "." + fileCount;
            readFile(model, localroot, filePrefix);
        } // end of file loop
    }

    /**
     * Read the descendants of a MozTreeNode (it can be a Platform, a Region, or a
     * Component) from the glossary
     *
     * @param currentNode   the MozTreeNode of which the content is going to be read
     * @param prefix        the prefix that identifies the MozTreeNode of which
     *                      the content is going to be read
     */
    private void readContent(Properties model, MozTreeNode currentNode, String prefix) {
        Component currentComponent;

        boolean loadingPartial = (prefix.startsWith("PARTIAL"));

        int thisLevelMax;
        long alteredTime;

        String subLevelPrefix;
        String subLevelType;
        String name;
        String exportToDir;

        thisLevelMax = Integer.parseInt(model.getProperty("" + prefix + COUNT));
        for (int thisLevelCount = 0; thisLevelCount < thisLevelMax; thisLevelCount++) {
            subLevelPrefix = prefix + "." + thisLevelCount;

            // If we're reading a glossary made with a previous version of MT,
            // TYPE property won't be defined for each node, because it was expected
            // to have only Platform/Region -> Component -> Subcomponent -> File,
            // so we have to mark it some way and proceed keeping that in mind
            subLevelType = model.getProperty(subLevelPrefix + TYPE, "undefined");

            if (subLevelType.equals("undefined")) {
                // We're reading a glossary made with a previous version of MT,
                // so it only contains Platform/Region -> Component -> Subcomponent -> File
                // and we have to find out in which level are we now
                int numDots = 0;
                int lastOccurrence = 0;

                while (subLevelPrefix.indexOf(".", lastOccurrence) > -1) {
                    numDots++;
                    lastOccurrence = subLevelPrefix.indexOf(".", lastOccurrence) + 1;
                }

                // And it turns out that Regional branch has one level less than
                // platforms (because a Regional branch is identified as
                // <product_number>.R, whereas a platform branch is identified
                // as <product_number.P.<platform_number>)
                if (subLevelPrefix.contains(".R")) {
                    switch (numDots) {
                        // 2 <-> Component level for a region branch
                        // 3 <-> Subcomponent level for a region branch
                        case 2:
                        case 3:
                            subLevelType = Component.TYPE_DIR;
                            break;
                        default:
                            subLevelType = Component.TYPE_FILE;
                            break;
                    } // switch end
                } else {
                    switch (numDots) {
                        // 3 <-> Component level for a platform branch
                        // 4 <-> Subcomponent level for a platform branch
                        case 3:
                        case 4:
                            subLevelType = Component.TYPE_DIR;
                            break;
                        default:
                            subLevelType = Component.TYPE_FILE;
                            break;
                    } // switch end
                }
            }

            if (subLevelType.equals(Component.TYPE_DIR)) {
                name = model.getProperty(subLevelPrefix + NAME);

                // We want to remove "MT_default" nodes, so the files
                // will be hanging directly from currentNode
                if (name.equals(StructureAccess.DEFAULT_SUBCOMPONENT)) {
                    currentComponent = (Component) currentNode;
                } else {
                    if (loadingPartial) {
                        currentComponent = (Component) currentNode.getChildByName(name);
                    } else {
                        currentComponent = new Component(name, currentNode);
                        currentNode.addChild(currentComponent);
                    }

                    if (currentComponent != null) {
                        alteredTime = Long.parseLong("0" + model.getProperty(
                                subLevelPrefix + ALTERED, ""));
                        currentComponent.setAlteredTime(alteredTime);

                        exportToDir = model.getProperty(subLevelPrefix + COMPONENT_EXPORTTO);
                        currentComponent.setExportedToDir(exportToDir);
                    }
                }

                readContent(model, currentComponent, subLevelPrefix);
            } else {
                readFile(model, currentNode, subLevelPrefix);
            }
        } // end of component loop
    }

    /**
     * Read a file from the glossary
     *
     * @param parent     the MozTreeNode of which this file is a child
     * @param filePrefix the property key prefix that identifies the file to be
     *        read
     */
    public void readFile(Properties model, MozTreeNode parent, String filePrefix) {
        GenericFile currentFile;
        Phrase currentPhrase;
        Translation currentTranslation;
        Phrase accessPhrase;
        Phrase commandPhrase;
        Phrase labelPhrase;
        ExternalEntity currentEntity;
        ArrayList entitiesList;
        TrnsStatus t;

        boolean keep;
        boolean fuzzy;

        int entityMax;
        int phraseMax;
        int phraseSort;
        int status;
        int translationMax;
        long alteredTime;

        String licenseFile;
        String name;
        String key;
        String text;
        String comment;
        String real;
        String relative;
        String accessCon;
        String commandCon;
        String labelCon;
        String translationPrefix;
        String phrasePrefix;
        String entityPrefix;
        String l10nNote;


        // The name of the file as a MozTreeNode property
        name = model.getProperty(filePrefix + NAME);
        alteredTime = Long.parseLong("0" + model.getProperty(filePrefix + ALTERED, ""));
        licenseFile = model.getProperty(filePrefix + FILE_LICENSE, "");

        currentFile = (GenericFile) parent.getChildByName(name);

        if (currentFile == null) {
            currentFile = FileFactory.createFile(name, parent);
            parent.addChild(currentFile);
        }

        currentFile.setLicenseFile(licenseFile);
        currentFile.setAlteredTime(alteredTime);
        currentFile.setDontExport(Boolean.valueOf(model.getProperty(filePrefix
                + FILE_DONTEXPORT, "false")).booleanValue());

        // If there is a license block
        if (model.getProperty(filePrefix + FILE_LICENSE_BLOCK) != null) {
            currentFile.setLicenseBlock(new MozLicense((MozFile) currentFile));
            currentFile.getLicenseBlock().setInsertionPos(Integer.parseInt(
                    model.getProperty(filePrefix + FILE_LICENSE_INSPOINT, "0")));
            currentFile.getLicenseBlock().setLicenseBlock(
                    model.getProperty(filePrefix + FILE_LICENSE_BLOCK, ""));
            currentFile.getLicenseBlock().setLicenseContributor(
                    model.getProperty(filePrefix + FILE_LICENSE_CONTRIB, ""));
        }

        if (parent instanceof CustomContainer) {
            real = model.getProperty(filePrefix + CUSTOM_REAL, "");
            relative = model.getProperty(filePrefix + CUSTOM_RELATIVE, "");

            currentFile.setRealFilename(real);
            currentFile.setRelativeFilename(relative);
        }

        if (currentFile instanceof DTDFile) {
            entityMax = Integer.parseInt(model.getProperty("" + filePrefix
                    + ENTITY_BAG + COUNT, "0"));

            if (entityMax > 0) {
                entitiesList = new ArrayList();
                ((DTDFile) currentFile).setExternalEntities(entitiesList);

                for (int entityCount = 0; entityCount < entityMax; entityCount++) {
                    entityPrefix = filePrefix + ENTITY_BAG + "." + entityCount;

                    currentEntity = new ExternalEntity();
                    currentEntity.setName(model.getProperty(entityPrefix + NAME, ""));
                    currentEntity.setPublicId(model.getProperty(entityPrefix + PUBLICID, ""));
                    currentEntity.setSystemId(model.getProperty(entityPrefix + SYSTEMID, ""));
                    entitiesList.add(entityCount, currentEntity);
                }
            }
        }

        if (currentFile instanceof BinaryFile) {
            ((BinaryFile) currentFile).setMd5Hash(model.getProperty("" + filePrefix
                    + BINFILE_MD5));
        }

        phraseMax = Integer.parseInt(model.getProperty("" + filePrefix + COUNT, "0"));

        for (int phraseCount = 0; phraseCount < phraseMax; phraseCount++) {
            phrasePrefix = filePrefix + "." + phraseCount;

            key = model.getProperty(phrasePrefix + PHRASE_KEY);
            text = model.getProperty(phrasePrefix + PHRASE_TEXT);

            accessCon = model.getProperty(phrasePrefix + PHRASE_ACCESSCON);
            commandCon = model.getProperty(phrasePrefix + PHRASE_COMMANDCON);
            labelCon = model.getProperty(phrasePrefix + PHRASE_LABELCON);

            keep = Boolean.valueOf(model.getProperty(phrasePrefix
                    + PHRASE_KEEPORIGINAL)).booleanValue();
            fuzzy = Boolean.valueOf(model.getProperty(phrasePrefix
                    + PHRASE_FUZZY)).booleanValue();
            phraseSort = Integer.parseInt(model.getProperty(phrasePrefix
                    + PHRASE_SORT, "-1"));
            alteredTime = Long.parseLong("0" + model.getProperty(phrasePrefix
                    + ALTERED, ""));

            l10nNote = model.getProperty(phrasePrefix + PHRASE_L10NNOTE);
            // We don't want empty L10N notes
            if ((l10nNote != null) && (l10nNote.length() == 0)) {
                l10nNote = null;
            }

            currentPhrase = (Phrase) currentFile.getChildByName(key);
            if (currentPhrase == null) {
                currentPhrase = new Phrase(key, currentFile, text);
                currentFile.addChild(currentPhrase);
            }

            currentPhrase.setKeepOriginal(keep);
            currentPhrase.setFuzzy(fuzzy);
            currentPhrase.setSort(phraseSort);
            currentPhrase.setAlteredTime(alteredTime);
            currentPhrase.setLocalizationNote(l10nNote);

            try {
                if (currentPhrase.getName().indexOf("lang.version") > -1) {
                    Kernel.settings.setString(Settings.STATE_VERSION, currentPhrase.getText());
                }
            } catch (NullPointerException e) {
                System.err.println("Error while trying to access to object created after: "
                        + key + " in file " + currentFile + " with the text " + text);
            }

            translationMax = Integer.parseInt(model.getProperty("" + phrasePrefix + COUNT));
            for (int translationCount = 0; translationCount < translationMax; translationCount++) {
                translationPrefix = phrasePrefix + "." + translationCount;

                name = model.getProperty(translationPrefix + NAME);

                try {
                    t = TrnsStatus.valueOf(model.getProperty(translationPrefix
                                + TRANSLATION_STATUS));
                } catch (IllegalArgumentException e) {
                    t = (text.trim().length() == 0) ?
                            TrnsStatus.Untranslated :
                            (currentPhrase.isFuzzy()) ? TrnsStatus.Modified :
                                                        TrnsStatus.Translated;
                }
                text = model.getProperty(translationPrefix + TRANSLATION_TEXT);
                comment = model.getProperty(translationPrefix + TRANSLATION_COMMENT);
                alteredTime = Long.parseLong("0" + model.getProperty(translationPrefix
                        + ALTERED, ""));

                currentTranslation = (Translation) currentPhrase.getChildByName(name);
                if (currentTranslation == null) {
                    currentTranslation = new Translation(name, currentPhrase,
                            text, t);
                    currentPhrase.addChild(currentTranslation);
                }

                currentTranslation.setAlteredTime(alteredTime);
                currentTranslation.setComment(comment);
            } // end of translation loop

            // We are processing a label with accesskey & commmandkey information,
            // let's look up the accesskey MozTreeNode
            if (accessCon != null && (!accessCon.equals(""))) {
                accessPhrase = (Phrase) currentFile.getChildByName(accessCon);
                if (accessPhrase != null) {
                    currentPhrase.setAccessConnection(accessPhrase);
                    accessPhrase.setLabelConnection(currentPhrase);
                }
            }

            // We are processing a label with accesskey & commmandkey information,
            // let's look up the commandkey MozTreeNode
            if (commandCon != null && (!commandCon.equals(""))) {
                commandPhrase = (Phrase) currentFile.getChildByName(commandCon);
                if (commandPhrase != null) {
                    currentPhrase.setCommandConnection(commandPhrase);
                    commandPhrase.setLabelConnection(currentPhrase);
                }
            }

            // We are processing an accesskey or a commandkey with label information,
            // let's look up the label MozTreeNode
            if (labelCon != null && (!labelCon.equals(""))) {
                labelPhrase = (Phrase) currentFile.getChildByName(labelCon);
                if (labelPhrase != null) {
                    if (currentPhrase.isAccesskey()) {
                        labelPhrase.setAccessConnection(currentPhrase);
                    } else {
                        labelPhrase.setCommandConnection(currentPhrase);
                    }
                    currentPhrase.setLabelConnection(labelPhrase);
                }
            }

        } //end of phrase loop
        currentFile.decreaseReferenceCount();
    }

    /**
     * Reads the images inside the Glossary.zip archive
     */
    public void readImages() {
        boolean done;
        String fileName;
        File testing;
        ZipEntry ze;
        ZipInputStream zis;
        FileInputStream fis;
        StringTokenizer tokenizer;
        String currentToken;
        MozTreeNode currentNode;

        try {
            fileName = Kernel.settings.getString(Settings.DATAMODEL_FILENAME,
                    "Glossary.zip");
            testing = new File(fileName);
            if (testing.exists()) {
                fis = new FileInputStream(fileName);
                zis = new ZipInputStream(fis);
                done = false;
                while (!done) {
                    ze = zis.getNextEntry();
                    // If we've got an entry and it's not glossary.txt
                    if (ze != null && !ze.getName().equals("glossary.txt")) {
                        // Find file and do stuff
                        tokenizer = new StringTokenizer(ze.getName(), "/", false);
                        currentToken = tokenizer.nextToken();
                        currentNode = (MozTreeNode) Kernel.datamodel.getChildByName(currentToken);

                        while ((tokenizer.hasMoreTokens()) && (currentNode != null)) {
                            currentToken = tokenizer.nextToken();

                            // If there are more tokens, we're dealing with a
                            // product, platform/region or component, else with a file
                            if (tokenizer.hasMoreTokens()) {
                                currentNode = (MozTreeNode) currentNode.getChildByName(currentToken);
                            } else {
                                readAFile(currentToken, currentNode, zis);
                            }
                        }
                    } else {
                        done = (ze == null);
                    }
                }
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error during reading of images {0}", e);
        }
    }

    /**
     * Takes an image filename inside the Glossary.zip archive and puts it in
     * the right place in the model
     *
     * @param fileToken     the last part of the filename inside the ZIP file
     * @param parent        the Component to which the file belongs
     * @param zis           the stream representing the ZIP file
     **/
    private void readAFile(String fileToken, MozTreeNode parent, ZipInputStream zis)
            throws java.io.IOException {
        String clipFileToken;
        BinaryFile currentFile;

        if (fileToken.endsWith("_original")) {
            clipFileToken = fileToken.substring(0, fileToken.indexOf("_original"));
        } else {
            clipFileToken = fileToken.substring(0, fileToken.indexOf("_translated"));
        }
        currentFile = (BinaryFile) parent.getChildByName(clipFileToken);

        if (currentFile != null) {
            if (fileToken.endsWith("_original")) {
                currentFile.setBinaryContent(FileUtils.loadFile(zis));
            } else {
                currentFile.setTranslatedContent(FileUtils.loadFile(zis));
            }
        }
    }

    /**
     * Saves images in the glossary by invoking WriteImagesTraverse
     * @param zos The ZIP file (Glossary.zip)
     */
    public void writeImages(ZipOutputStream zos) {
        Kernel.datamodel.traverse(new WriteImagesTraverse(zos), TreeNode.LEVEL_FILE);
    }
}
