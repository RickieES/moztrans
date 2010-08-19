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
 *
 */

package org.mozillatranslator.io;

import org.mozillatranslator.io.common.FileUtils;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import java.util.zip.ZipException;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.common.*;


/**
 * This class is used to load the contents of a JAR file (i.e. a platform,
 * including the ab-CD.jar neutral platform, or a region JAR)
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class StructureAccess {
    public static final String DEFAULT_SUBCOMPONENT = "MT_default";
    private static ImportExportDataObject dao = new ImportExportDataObject();
    private static final Logger fLogger = Logger.getLogger(StructureAccess.class.
            getPackage().getName());

    /************************************************************************
     ** LOAD JAR ROUTINES
     ************************************************************************/

    /**
     * Loads the files
     *
     * @param dataObject    an object with a bunch of properties needed to load
     *                      a JAR file, from a File pointing to the JAR file to
     *                      a list to hold the new and updated strings
     **/
    public void load(ProductChildInputOutputDataObject dataObject) throws IOException {
        if (dataObject.getL10n().equals(Kernel.ORIGINAL_L10N)) {
            loadOriginal(dataObject);
        } else {
            loadTranslated(dataObject);
        }
    }

    /**
     * Loads the files into the original product
     *
     * @param dataObject    an object with a bunch of properties needed to load
     *                      a JAR file, from a File pointing to the JAR file to
     *                      a list to hold the new and updated strings
     **/
    public void loadOriginal(ProductChildInputOutputDataObject dataObject) throws
            IOException {

        ProductChild pc = dataObject.getProductChild();
        StringTokenizer tokenizer;
        String localeToken;
        String localenameToken;
        String thisLevelToken;

        MozTreeNode parent;
        Component currentComponent;
        GenericFile currentFile;

        FileInputStream fis;
        JarInputStream jis;
        BufferedInputStream bis;
        JarEntry je;
        boolean done;

        try {
            // If the JAR input stream is not provided by dataObject
            if (dataObject.getJarInStream() == null) {
                // We create the JAR input stream ourselves from the real file
                fis = new FileInputStream(dataObject.getRealFile());
                jis = new JarInputStream(fis);
                bis = new BufferedInputStream(jis);
            } else {
                // We use the stream provided by dataObject
                jis = dataObject.getJarInStream();
                bis = dataObject.getBuffInStream();
            }

            done = false;
            while (!done) {
                localeToken = null;
                localenameToken = null;
                thisLevelToken = null;
                je = null;

                je = jis.getNextJarEntry();
                done = (je == null);
                if (!done) {
                    // If the JAR entry is a directory entry or if it is a
                    // contents.rdf, we don't need to process it
                    if (je.getName().endsWith("/") ||
                            je.getName().endsWith("contents.rdf")) {
                        continue;
                    }

                    tokenizer = new StringTokenizer(je.getName(), "/", false);

                    localeToken = tokenizer.nextToken();
                    // We only care about the dirs and files under "locale" dir
                    if (!localeToken.equalsIgnoreCase("locale")) {
                        continue;
                    }

                    // If the product only has to have content under /locale/ab-CD
                    if (((Product) pc.getParent()).isOnlyLocaleAbCD()) {
                        if (tokenizer.hasMoreTokens()) {
                            localenameToken = tokenizer.nextToken();
                            // If we are working with something like "en-US",
                            // test localenameToken against the full lang + region code;
                            // if not, test against the country code only
                            if (!localenameToken.regionMatches(0, Kernel.ORIGINAL_L10N,
                                    Kernel.ORIGINAL_L10N.length() - localenameToken.length(),
                                    localenameToken.length())) {
                                continue;
                            }
                        } else {
                            // We don't want to consider anything that is not under
                            // a locale
                            continue;
                        }
                    }

                    // We start with parent being the producthild; this allows
                    // files to exist directly under product children
                    parent = (MozTreeNode) pc;

                    while (tokenizer.hasMoreTokens()) {
                        thisLevelToken = tokenizer.nextToken();

                        if (tokenizer.hasMoreTokens()) {
                            // The current level is a component, since it has
                            // children (dir/files)
                            try {
                                currentComponent = (Component) parent.getChildByName(thisLevelToken);
                            } catch (ClassCastException ex) {
                                // Funny, the node exists but is not a component
                                TreeNode aNode = parent.getChildByName(thisLevelToken);
                                aNode.removeChildren();
                                aNode = null;
                                currentComponent = null;
                            }

                            if (currentComponent == null) {
                                currentComponent = new Component(thisLevelToken, parent);
                                parent.addChild(currentComponent);
                            }

                            currentComponent.setMark();
                            parent = currentComponent;
                        } else {
                            try {
                                currentFile = (GenericFile) parent.getChildByName(thisLevelToken);
                            } catch (ClassCastException ex) {
                                // Funny, the node exists but is not a file
                                TreeNode aNode = parent.getChildByName(thisLevelToken);
                                aNode.removeChildren();
                                aNode = null;
                                currentFile = null;
                            }

                            if (currentFile == null) {
                                currentFile = FileFactory.createFile(thisLevelToken, parent);
                                parent.addChild(currentFile);
                                currentFile.startRefFromOne();
                            } else {
                                currentFile.increaseReferenceCount();
                            }

                            currentFile.setMark();
                            int changeSizeHack = dataObject.getChangeList().size();
                            readFile(bis, currentFile, dataObject.getL10n(),
                                    dataObject.getChangeList());

                            if (changeSizeHack == dataObject.getChangeList().size()) {
                                currentFile.decreaseReferenceCount();
                            }
                        }
                    }
                }
            }

            if (dataObject.getJarInStream() == null) {
                bis.close();
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error reading jar file", e);
            throw new IOException("Error reading jar file \n" + e.getMessage());
        }
    }


    /**
     * Loads the files into the translated product
     *
     * @param dataObject    an object with a bunch of properties needed to load
     *                      a JAR file, from a File pointing to the JAR file to
     *                      a list to hold the new and updated strings
     **/
    public void loadTranslated(ProductChildInputOutputDataObject dataObject) throws
            IOException {
        ProductChild pc = dataObject.getProductChild();

        StringTokenizer tokenizer;
        String localeToken;
        String localenameToken;
        String thisLevelToken;

        MozTreeNode parent;
        Component currentComponent;
        GenericFile currentFile;

        FileInputStream fis;
        JarInputStream jis;
        BufferedInputStream bis;
        JarEntry je;
        boolean done;
        boolean itMayExists;

        try {
            // If the JAR input stream is not provided by dataObject
            if (dataObject.getJarInStream() == null) {
                // We create the JAR input stream ourselves from the real file
                fis = new FileInputStream(dataObject.getRealFile());
                jis = new JarInputStream(fis);
                bis = new BufferedInputStream(jis);
            } else {
                // We use the stream provided by dataObject
                jis = dataObject.getJarInStream();
                bis = dataObject.getBuffInStream();
            }

            done = false;
            while (!done) {
                localeToken = null;
                localenameToken = null;
                thisLevelToken = null;
                je = null;

                je = jis.getNextJarEntry();
                done = (je == null);
                if (!done) {
                    // If the JAR entry is a directory entry or if it is a
                    // contents.rdf, we don't need to process it
                    if (je.getName().endsWith("/") ||
                            je.getName().endsWith("contents.rdf")) {
                        continue;
                    }

                    tokenizer = new StringTokenizer(je.getName(), "/", false);

                    localeToken = tokenizer.nextToken();
                    // We only care about the dirs and files under "locale" dir
                    if (!localeToken.equalsIgnoreCase("locale")) {
                        continue;
                    }

                    // If the product only has to have content under /locale/ab-CD
                    if (((Product) pc.getParent()).isOnlyLocaleAbCD()) {
                        if (tokenizer.hasMoreTokens()) {
                            localenameToken = tokenizer.nextToken();
                            // If we are working with something like "ab-CD",
                            // test localenameToken against the full lang + region code;
                            // if not, test against the country code only
                            if (!localenameToken.regionMatches(0, dataObject.getL10n(),
                                    dataObject.getL10n().length() - localenameToken.length(),
                                    localenameToken.length())) {
                                continue;
                            }
                        } else {
                            // We don't want to consider anything that is not under
                            // a locale
                            continue;
                        }
                    }

                    // We start with parent being the producthild; this allows
                    // files to exist directly under product children
                    parent = (MozTreeNode) pc;

                    // Since we are loading a translation, it could happen that
                    // the JAR file contains dirs and files non present in the
                    // datamodel and, if so, we MUST NOT create them, so we need
                    // to mark when are we sure that a JAR entry doesn't have to
                    // be further processed because one of the dir/components
                    // doesn't exist in the original datamodel. We use
                    // itMayExists for that
                    itMayExists = true;
                    while (tokenizer.hasMoreTokens() && itMayExists) {
                        thisLevelToken = tokenizer.nextToken();

                        if (tokenizer.hasMoreTokens()) {
                            // The current level is a component, since it has
                            // children (dir/files)
                            currentComponent = (Component) parent.getChildByName(thisLevelToken);
                            if (currentComponent != null) {
                              parent = currentComponent;
                            } else {
                                itMayExists = false;
                            }
                        } else {
                            currentFile = (GenericFile) parent.getChildByName(thisLevelToken);
                            if (currentFile != null) {
                                currentFile.increaseReferenceCount();
                                readFile(bis, currentFile, dataObject.getL10n(),
                                        dataObject.getChangeList());
                                currentFile.decreaseReferenceCount();
                            }
                        }
                    }
                }
            }
            if (dataObject.getJarInStream() == null) {
                bis.close();
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error reading jar file", e);
            throw new IOException("Error reading jar file \n" + e.getMessage());
        }
    }



    /************************************************************************
     ** SAVE JAR ROUTINES
     ************************************************************************/


    /**
     * Saves the files
     *
     * @param dataObject    an object with a bunch of properties needed to save
     *                      a JAR file, such as a File pointing to the JAR file
     **/
    public void save(ProductChildInputOutputDataObject dataObject) throws
            IOException {

        ProductChild pc = dataObject.getProductChild();
        FileOutputStream fos;
        JarOutputStream jos;

        Iterator componentIterator;
        String relative;

        try {
            if (dataObject.getJarOutStream() == null) {
                // We create the JAR output stream ourselves from the real file
                fos = new FileOutputStream(dataObject.getRealFile());
                jos = new JarOutputStream(fos);
            } else {
                // We use the stream provided by dataObject
                jos = dataObject.getJarOutStream();
            }

            componentIterator = pc.iterator();
            relative = "locale";
            
            // If the product only has to have content under /locale/ab-CD
            if (((Product) pc.getParent()).isOnlyLocaleAbCD()) {
                relative = relative + "/" + dataObject.getLocaleDisplay();
            }

            saveComponent(componentIterator, relative, dataObject, jos);
            
            // We only generate manifests (contents.rdf files) if the product
            // is flagged as to only have content under /locale/ab-CD
            if (((Product) pc.getParent()).isOnlyLocaleAbCD()) {
                writeManifest(dataObject, jos);
            }

            if (dataObject.getJarOutStream() == null) {
                jos.close();
            }

            if (dataObject.isUseExternalZIP()) {
                // Expand and compress the JAR/ZIP file to workaround bug 197792
                FileUtils.expandCompress(dataObject);
            }
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error writing jar file", e);
            throw new IOException("Error writing jar file \n" + e.getMessage());
        }
    }


    /**
     * Saves a component, iterating recursively through its component children
     *
     * @author Ricardo
     *
     * @param thisLevel     a level's iterator to process its children
     * @param parentPath    the path where the children will be saved (or
     *                      created, if they are components/dirs)
     * @param dataObject    the ProductChildInputOutputDataObject used to
     *                      save this JAR
     * @param jos           the JarOutputStream where the dirs and files will
     *                      be saved
     **/
    private void saveComponent(Iterator thisLevel, String parentPath,
            ProductChildInputOutputDataObject dataObject, JarOutputStream jos)
            throws IOException {

        MozTreeNode currentNode;
        String subDir = null;

        // For every children in thisLevel
        while (thisLevel.hasNext()) {
            // Let's get it
            currentNode = (MozTreeNode) thisLevel.next();

            // If it's a component, we have to recurse over it
            if (currentNode instanceof Component) {

                // It it is NOT named "MT_default", we should create the
                // subdirectory before processing currentNode children
                if (!currentNode.getName().equalsIgnoreCase(
                        StructureAccess.DEFAULT_SUBCOMPONENT)) {

                    // If user preference is to replace "en-US" directories name
                    // wit "ab-CD" name, let's take it into account
                    if (currentNode.getName().equals(Kernel.ORIGINAL_L10N) &&
                            !dataObject.getLocaleDisplay().equals(Kernel.ORIGINAL_L10N)) {
                        subDir = dataObject.getLocaleDisplay();
                    } else {
                        subDir = currentNode.getName();
                    }
                }

                Iterator nextLevel = currentNode.iterator();
                saveComponent(nextLevel, parentPath + "/" + subDir, dataObject, jos);
            } else {
                saveFile((MozFile) currentNode, parentPath, dataObject, jos);
            }
        }
    }


    /**
     * Saves a file
     *
     * @author Ricardo
     *
     * @param currentNode   the MozFile representing a file
     * @param parentPath    the path where the file will be saved
     * @param dataObject    the ProductChildInputOutputDataObject used to
     *                      save this JAR
     * @param jos           the JarOutputStream where the dirs and files will
     *                      be saved
     **/
    private void saveFile(MozFile currentFile, String parentPath,
            ProductChildInputOutputDataObject dataObject, JarOutputStream jos)
            throws IOException {

        String relative;
        JarEntry je;

        currentFile.increaseReferenceCount();
        relative = parentPath + "/" + currentFile.getName();
        je = new JarEntry(relative);
        try {
            jos.putNextEntry(je);
            writeFile(currentFile, dataObject.getL10n(), jos);
            currentFile.decreaseReferenceCount();
        } catch (ZipException e1) {
            // Kernel.appLog.log(Level.SEVERE, "Error putting entry into jar file", e1);
        }
    }


    /*
     * Utility functions
     *
     */
    protected void readFile(BufferedInputStream inStream, GenericFile mfile,
            String lz, List cl) throws IOException {
        try {
            dao.setFileName(mfile.getName());
            dao.setFileContent(FileUtils.loadFile(inStream));
            dao.setChangeList(cl);
            dao.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
            dao.setL10n(lz);
            mfile.load(dao);
        } catch (IOException e) {
            // FIXME:Exception
            Kernel.appLog.severe("Could not read file "  + mfile.getName());
            throw new IOException("Could not read file " + mfile.getName());
        }
    }

    protected void writeFile(GenericFile mfile, String l10n, OutputStream os) throws IOException {
        try {
            dao.setFileName(mfile.getName());
            dao.setFileContent(null);
            dao.setChangeList(null);
            dao.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
            dao.setL10n(l10n);
            mfile.save(dao);
            FileUtils.saveFileWithLicense(os, dao.getFileContent(), mfile);
        } catch (IOException e) {
            Kernel.appLog.log(Level.SEVERE, "Could not write file" + mfile.getName(), e);
            throw new IOException("Could not write file" + mfile.getName());
        }
    }

    // TODO FIXME: We need a better write Manifest method.
    public void writeManifest(ProductChildInputOutputDataObject dataObject,
            JarOutputStream jos) throws IOException {
        Iterator manifestIterator;
        ProductChild currentPlatform;
        Component manifestComponent;
        String maniFest;
        PrintWriter pw;

        fLogger.info("Version is" + dataObject.getVersion());
        try {
            currentPlatform = dataObject.getProductChild();
            manifestIterator = currentPlatform.iterator();
            while (manifestIterator.hasNext()) {
                Object o = manifestIterator.next();

                // Now we can have files hanging directly from product children
                // so we must skip it
                if (!(o instanceof Component)) {
                    continue;
                }

                manifestComponent = (Component) o;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
                pw = new PrintWriter(osw);

                // START OF MANIFEST !!
                pw.println("<?xml version=\"1.0\"?>");
                pw.println("<RDF:RDF xmlns:RDF=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
                pw.println("         xmlns:chrome=\"http://www.mozilla.org/rdf/chrome#\">");
                pw.println();
                pw.println("  <!-- list all the skins being supplied by this package -->");
                pw.println("  <RDF:Seq about=\"urn:mozilla:locale:root\">");
                pw.println("    <RDF:li resource=\"urn:mozilla:locale:" +
                        dataObject.getLocaleDisplay() + "\"/>");

                pw.println("  </RDF:Seq>");
                pw.println();
                pw.println("  <!-- locale information -->");
                pw.println("  <RDF:Description about=\"urn:mozilla:locale:" +
                        dataObject.getLocaleDisplay() + "\"");

                pw.println("       chrome:displayName=\"" + dataObject.getDisplay() + "\"");
                pw.println("       chrome:author=\"" + dataObject.getAuthor() + "\"");
                pw.println("       chrome:name=\"" + dataObject.getLocaleDisplay() + "\"");
                pw.println("       chrome:localeVersion=\"" + dataObject.getVersion() + "\"");

                if (currentPlatform instanceof Region) {
                    pw.println("       chrome:localeType=\"region\"");
                }

                pw.println("       chrome:previewURL=\"" + dataObject.getPreviewUrl() + "\">");
                pw.println("   <chrome:packages>");
                pw.println("      <RDF:Seq about=\"urn:mozilla:locale:" +
                        dataObject.getLocaleDisplay() + ":packages\">");

                pw.println("        <RDF:li resource=\"urn:mozilla:locale:" +
                        dataObject.getLocaleDisplay() + ":" + manifestComponent.getName() + "\"/>");

                pw.println("      </RDF:Seq>");
                pw.println("    </chrome:packages>");
                pw.println("  </RDF:Description>");
                pw.println("<RDF:Description about=\"urn:mozilla:locale:" +
                        dataObject.getLocaleDisplay() + ":" + manifestComponent.getName() +
                        "\" chrome:localeVersion=\"" + dataObject.getVersion() + "\"/>");

                pw.println("</RDF:RDF>");
                // END OF MANIFEST !!

                pw.close();
                maniFest = "locale/"  + dataObject.getLocaleDisplay() + "/" +
                        manifestComponent.getName() + "/contents.rdf";

                JarEntry je = new JarEntry(maniFest);
                jos.putNextEntry(je);
                FileUtils.saveFile(jos, baos.toByteArray());
            }
        } catch (IOException e) {
            Kernel.appLog.log(Level.SEVERE, "could not write manifest file", e);
            throw new IOException("Could not write manifest file");
        }
    }
}
