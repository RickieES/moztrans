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
 * Ricardo Palomares (refactoring to remove subcomponent from the datamodel, making
 *                    this class non-static)
 *
 * CvsTransfer.java
 * Created on August 21, 2004, 8:35 PM
 */

package org.mozillatranslator.io.common;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.io.*;

/**
 *
 * @author  henrik
 */
public class CvsTransfer {
    private ImportExportDataObject dao = new ImportExportDataObject();
    private String l10n;
    private Product prod;
    private File imexDir;
    private Settings set = Kernel.settings;
    private static final Logger fLogger = Logger.
            getLogger(CvsTransfer.class.getPackage().getName());
    
    
    /**
     * Default constructor
     */
    public CvsTransfer() {
    }
    
    /**
     * Constructor normally used
     * @param prod the Product object that will be updated
     * @param imexDir Import/Export directory
     */
    public CvsTransfer(Product prod, File imexDir) {
        this.prod = prod;
        this.imexDir = imexDir;
    }
    
    /************************************************************************
     ** SAVE TRANSLATION ROUTINES
     ************************************************************************/
    
    /**
     * Saves a product in a suitable form for commiting it to CVS
     *
     * @param l10n          the l10n to save
     **/
    public void saveProduct(String l10n) {
        ProductChild neutral;
        
        this.l10n = l10n;
        neutral = this.prod.getChildByType(ProductChild.TYPE_NEUTRAL);
        Iterator componentIterator = neutral.iterator();
        saveComponent(componentIterator, this.imexDir.getAbsolutePath());
    }
    
    /**
     * Saves a component, iterating recursively through its component children
     *
     * @author Ricardo
     *
     * @param thisLevel     a level's iterator to process its children
     * @param parentPath    the path where the children will be saved (or
     *                      created, if they are components/dirs)
     **/
    private void saveComponent(Iterator thisLevel, String parentPath) {
        MozTreeNode currentNode;
        File subDirFile = null;
        
        // For every children in thisLevel
        while (thisLevel.hasNext()) {
            // Let's get it
            currentNode = (MozTreeNode) thisLevel.next();
            
            // If it's a component, we have to recurse over it
            if (currentNode instanceof Component) {
                String altExportDir = ((Component) currentNode).getExportedToDir();
                
                // If we have an alternative export dir for this component,
                if (altExportDir != null) {
                    // let's use the alternative
                    altExportDir = altExportDir.replace(
                            Component.ALTEXPORTDIR_LOCALE_PATTERN, this.l10n);
                    subDirFile = new File(this.imexDir, altExportDir);
                
                    // If it is NOT named "MT_default", we should create the
                    // subdirectory before processing currentNode children
                } else if (!currentNode.getName().equalsIgnoreCase(
                           StructureAccess.DEFAULT_SUBCOMPONENT)) {
                    // If user preference is to replace "en-US" directories name
                    // wit "ab-CD" name, let's take it into account
                    if (set.getBoolean(Settings.EXPORT_REPLACE_ENUS) &&
                            !this.l10n.equals(Kernel.ORIGINAL_L10N) &&
                            currentNode.getName().equals(Kernel.ORIGINAL_L10N)) {
                        subDirFile = new File(parentPath, this.l10n);
                    } else {
                        subDirFile = new File(parentPath, currentNode.getName());
                    }
                } else {
                    subDirFile = new File(parentPath);
                }
                
                Iterator nextLevel = currentNode.iterator();
                saveComponent(nextLevel, subDirFile.getPath());
            } else {
                saveFile((MozFile) currentNode, parentPath);
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
     **/
    private void saveFile(MozFile currentFile, String parentPath) {
        File thisFile;
        File subDirFile;
        ImportExportDataObject data = new ImportExportDataObject();
        
        // If user preference is to replace "en-US" directories name
        // wit "ab-CD" name, let's take it into account
        if (set.getBoolean(Settings.EXPORT_REPLACE_ENUS) &&
                !this.l10n.equals(Kernel.ORIGINAL_L10N) &&
                (parentPath.indexOf(Kernel.ORIGINAL_L10N) > -1)) {
            parentPath = parentPath.replace(Kernel.ORIGINAL_L10N, this.l10n);
        }
        
        subDirFile = new File(parentPath);
        
        if (!subDirFile.exists()) {
            subDirFile.mkdirs();
        }
        
        thisFile = new File(parentPath, currentFile.getName());
        currentFile.increaseReferenceCount();
        
        data.setChangeList(null);
        data.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
        data.setL10n(l10n);
        data.setFileName(thisFile.getAbsolutePath());
        data.setFileContent(null);
        currentFile.save(data);
        
        try {
            if (data.getFileContent() != null && data.getFileContent().length > 0) {
                fLogger.log(Level.INFO, "Should be {0}", thisFile.getCanonicalPath());
                FileUtils.saveWithLicence(thisFile.getCanonicalPath(),
                        data.getFileContent(), currentFile);
            } else {
                fLogger.log(Level.INFO, "Empty file: {0} not written",
                        thisFile.getCanonicalPath());
            }
        } catch (IOException e) {
            fLogger.log(Level.WARNING, "Error during file export: {0}",
                    e.getMessage());
        }
        currentFile.decreaseReferenceCount();
    }
    
    
    /************************************************************************
     ** LOAD PRODUCT (CVS IMPORT DIRECTORY) ROUTINES
     ************************************************************************/
    
    /**
     * Loads the original content of a product from a directory structure,
     * supposedly checked out from a CVS repository
     *
     * @return  a list with all changes
     */
    public List loadProduct() {
        File[] componentDirs;
        List changeList = new ArrayList();
        ProductChild neutral = prod.getChildByType(ProductChild.TYPE_NEUTRAL);
        
        this.l10n = Kernel.ORIGINAL_L10N;
        
        // We'll use MozTreeNode.mark to mark the nodes present in the import
        // so we can delete later the obsolete nodes
        neutral.traverse(new ClearMarkTraverse(), TreeNode.LEVEL_TRANSLATION);
        neutral.setMark();
        
        componentDirs = imexDir.listFiles();
        
        loadComponent(componentDirs, (MozTreeNode) neutral, changeList);
        
        neutral.deleteUntouched();
        return changeList;
    }
    
    
    /**
     * Loads a translation of a product from a directory structure,
     * supposedly checked out from a CVS repository
     *
     * @param l10n          the l10n code
     */
    public void loadTranslation(String l10n) {
        File[] componentDirs;
        List changeList = new ArrayList();
        ProductChild neutral = prod.getChildByType(ProductChild.TYPE_NEUTRAL);
        
        this.l10n = l10n;
        
        componentDirs = imexDir.listFiles();
        loadComponent(componentDirs, (MozTreeNode) neutral, changeList);
    }
    
    /**
     * Process a directory to load its content into the datamodel
     *
     * @author Ricardo
     *
     * @param thisLevelFiles    the list of files/dir to be loaded
     * @param parent            the node representing the parent of the newly
     *                          created children
     * @param   changeList  the list accumulating all changed strings
     **/
    private void loadComponent(File[] thisLevelFiles, MozTreeNode parent,
            List changeList) {
        
        Component currentComponent;
        File[] nextLevelFiles;
        int numFilesOrDirs = 0;
        boolean loadingOriginal = (this.l10n.equals(Kernel.ORIGINAL_L10N));
        
        try {
            numFilesOrDirs = thisLevelFiles.length;
        } catch (java.lang.NullPointerException e) {
            fLogger.log(Level.WARNING, "Dir not found or is empty: {0}",
                    parent.getName());
            numFilesOrDirs = 0;
        }
        
        for(int index = 0; index < numFilesOrDirs; index++) {
            File currentFileOrDir = thisLevelFiles[index];
            
            // We skip CVS, SVN and HG administrative directories and files
            if (currentFileOrDir.getName().equals("CVS") ||
                    currentFileOrDir.getName().equals(".svn") ||
                    currentFileOrDir.getName().equals(".hg") ||
                    currentFileOrDir.getName().equals(".hgignore") ||
                    currentFileOrDir.getName().equals(".hgtags")) {
                continue;
            }

            // If we're dealing with a directory, it's a component in the
            // datamodel
            if (currentFileOrDir.isDirectory()) {
                
                // We look up the component in the datamodel
                try {
                    currentComponent = (Component) parent.getChildByName(
                            currentFileOrDir.getName());
                } catch (ClassCastException ex) {
                    // Funny, the node exists but is not a component
                    if (loadingOriginal) {
                        TreeNode aNode = parent.getChildByName(currentFileOrDir.getName());
                        aNode.removeChildren();
                        aNode = null;
                    }
                    currentComponent = null;
                }
                
                // If the component doesn't exists and we're not importing a
                // translation, we must create the component and associate it
                // to its parent
                if (currentComponent == null) {
                    if (loadingOriginal) {
                        currentComponent = new Component(currentFileOrDir.getName(),
                                parent);
                        parent.addChild(currentComponent);
                    } else {
                        continue;
                    }
                }
                
                if (loadingOriginal) {
                    // This component exists in the directory structure, so we mark
                    // it because it won't need to be deleted
                    currentComponent.setMark();
                }
                
                nextLevelFiles = currentFileOrDir.listFiles();
                loadComponent(nextLevelFiles, currentComponent, changeList);
            } else {
                loadFile(currentFileOrDir, (MozTreeNode) parent, changeList);
            }
        }
    }
    
    
    /**
     * Loads a file into the datamodel
     *
     * @author  Ricardo
     *
     * @param   thisFile    the file to be loaded
     * @param   parent      the Component representing the parent of the MozFile
     *                      that will be created in the datamodel
     * @param   changeList  the list accumulating all changed strings
     **/
    private void loadFile(File thisFile, MozTreeNode parent, List changeList) {
        boolean loadingOriginal = (this.l10n.equals(Kernel.ORIGINAL_L10N));
        int changeSizeHack = 0;
        GenericFile currentFile;
        
        try {
            currentFile = (GenericFile) parent.getChildByName(thisFile.getName());
        } catch (ClassCastException ex) {
            // Funny, the node exists but is not a file
            if (loadingOriginal) {
                TreeNode aNode = parent.getChildByName(thisFile.getName());
                aNode.removeChildren();
                aNode = null;
            }
            currentFile = null;
        }
        
        if (currentFile == null) {
            if (loadingOriginal) {
                currentFile = FileFactory.createFile(thisFile.getName(), parent);
                parent.addChild(currentFile);
                currentFile.startRefFromOne();
            } else {
                return;
            }
        } else {
            currentFile.increaseReferenceCount();
        }
        
        if (loadingOriginal) {
            currentFile.setMark();
            changeSizeHack = changeList.size();
        }
        
        readFile(thisFile, currentFile, changeList, l10n);
        
        if ((!loadingOriginal) || (changeSizeHack == changeList.size())) {
            currentFile.decreaseReferenceCount();
        }
    }
    
    /**
     * Parses a file having into account the type of the file
     *
     * @param loadFile  the file in the systemfile to be parsed
     * @param mfile     the GenericFile in the datamodel
     * @param cl        the list accumulating the changed strings
     * @param l10n      the l10n
     **/
    protected void readFile(File loadFile, GenericFile mfile, List cl, String l10n) {
        try {
            FileInputStream fis = new FileInputStream(loadFile);
            dao.setFileContent(FileUtils.loadFile(fis));
            dao.setFileName(loadFile.getAbsolutePath());
            dao.setChangeList(cl);
            dao.setFormat(ImportExportDataObject.FORMAT_MOZILLA);
            dao.setL10n(l10n);
            mfile.load(dao);
        } catch(FileNotFoundException e) {
            fLogger.log(Level.WARNING, "File {0} couldn''t be found, ignored.",
                    loadFile.getAbsolutePath());
        } catch (IOException e) {
            fLogger.log(Level.SEVERE, "An I/O error happened while reading {0}",
                    loadFile.getAbsolutePath());
        }
    }
}
