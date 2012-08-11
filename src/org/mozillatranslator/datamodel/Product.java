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

package org.mozillatranslator.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class Product extends MozTreeNode {
    private Platform neutral;
    private List platforms;
    private Region regional;
    private CustomContainer custom;
    private String version;
    private String CVSImportOriginalPath;
    private String CVSImpExpTranslationPath;
    private boolean onlyLocaleAbCD;
    
    /** Creates new Product
     * @param n Name of the product
     */
    public Product(String n) {
        super(n, null, TreeNode.LEVEL_PRODUCT);
        name = n;
        neutral = new Platform("Platform neutral", this, Platform.TYPE_NEUTRAL);
        platforms = new ArrayList();
        regional = new Region("Regional files", this);
        custom = new CustomContainer("Custom files", this);
        buildList();
    }
    
    /**
     * Returns the Neutral platform
     * @return the netural platform
     */
    public Platform getNeutralPlatform() {
        return neutral;
    }
    
    /**
     * Adds a platform
     * @param p the platform to be add
     */
    public void addPlatform(Platform p) {
        platforms.add(p);
        buildList();
    }
    
    /**
     * Removes a platform
     * @param p The platform to remove
     */
    public void removePlatform(Platform p) {
        platforms.remove(p);
        buildList();
    }
    
    
    /**
     * Adds a child to the product
     * @param child the child to add
     */
    @Override public void addChild(TreeNode child) {
        addPlatform((Platform) child);
    }
    
    /**
     * Removes a child from the product
     * @param child the child to remove
     */
    @Override public void removeChild(TreeNode child) {
        removePlatform((Platform) child);
    }
    
    private void buildList() {
        children.clear();
        children.add(neutral);
        children.addAll(platforms);
        children.add(regional);
        children.add(custom);
        touch(); // Update last modified time when re-creating the children list
    }
    
    
    /**
     * Returns an iterator for platforms
     * @return the iterator
     */
    public Iterator platformIterator() {
        return platforms.iterator();
    }
    
    /**
     * Returns a list of platform objects
     * @return the list of platform objects
     */
    public Object[] platformArray() {
        return platforms.toArray();
    }
    
    public Region getRegional() {
        return regional;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Object[] customArray() {
        return custom.toArray();
    }
    
    public CustomContainer getCustomContainer() {
        return custom;
    }
    
    public ProductChild getChildByType(int type) {
        ProductChild current;
        ProductChild result = null;
        boolean done = false;
        Iterator childIterator = children.iterator();
        
        while (!done && childIterator.hasNext()) {
            current = (ProductChild) childIterator.next();
            
            if (current.getType() == type) {
                done = true;
                result = current;
            }
        }
        return result;
    }
    
    public String getCVSImportOriginalPath() {
        return CVSImportOriginalPath;
    }
    
    public void setCVSImportOriginalPath(String CVSImportOriginalPath) {
        this.CVSImportOriginalPath = CVSImportOriginalPath;
    }
    
    public String getCVSImpExpTranslationPath() {
        return CVSImpExpTranslationPath;
    }
    
    public void setCVSImpExpTranslationPath(String CVSImpExpTranslationPath) {
        this.CVSImpExpTranslationPath = CVSImpExpTranslationPath;
    }
    
    public boolean isOnlyLocaleAbCD() {
        return onlyLocaleAbCD;
    }
    
    /**
     * Sets the onlyLocaleAbCD property
     * @param onlyLocaleAbCD 
     */
    public void setOnlyLocaleAbCD(boolean onlyLocaleAbCD) {
        this.onlyLocaleAbCD = onlyLocaleAbCD;
    }
}
