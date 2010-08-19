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

package org.mozillatranslator.datamodel;

import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import org.mozillatranslator.kernel.*;

/**
 * Interface with the basic methods all nodes in the datamodel must have
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public interface TreeNode extends Comparable, Serializable {
    /**
     * Contant representing the model level
     */
    public static final int LEVEL_MODEL = 0;
    /**
     * Constant representing the product level
     */
    public static final int LEVEL_PRODUCT = 1;
    /**
     * Constant representing the productchild level (which may be platform
     * neutral, win, mac, unix, or custom)
     */
    public static final int LEVEL_PRODUCTCHILD = 2;
    /**
     * Constant representing the component level. Keep in mind that several
     * actual levels may be considered components
     */
    public static final int LEVEL_COMPONENT = 3;
    /**
     * Constant representing the file level
     */
    public static final int LEVEL_FILE = 4;
    /**
     * Constant representing the phrase level
     */
    public static final int LEVEL_PHRASE = 5;
    /**
     * Constant representing the translation
     */
    public static final int LEVEL_TRANSLATION = 6;


    // Properties support

    /**
     * Setter for the name property
     * @param name
     */
    public void setName(String name);

    /**
     * Getter for the name property
     * @return the name property (product name, platform name, file name, phrase
     * name or locale code)
     */
    public String getName();

    /**
     * Setter for the mark property, which is a boolean one, to true
     */
    public void setMark();

    /**
     * Getter for the mark property, which is a boolean one
     * @return true is the mark is set
     */
    public boolean isMark();

    /**
     * Setter for the mark property, which is a boolean one, to false
     */
    public void clearMark();

    /**
     * Returns true if a leaf node lacks a mark, or operates recursively if
     *              the node is not a leaf node
     * @return true if a leaf node lacks a mark, false otherwise
     */
    public boolean deleteUntouched();


    // Child management
    /**
     * Adds a new child to this node
     * @param child the node to be added
     */
    public void addChild(TreeNode child);

    /**
     * Removes a child
     * @param child the child to be removed
     */
    public void removeChild(TreeNode child);

    /**
     * Removes all children of this node
     */
    public void removeChildren();

    /**
     * Gets a child with the name passed as a parameter
     * @param name the name of the child to be looked up
     * @return the searched child, or null if no child has that name
     */

    public TreeNode getChildByName(String name);

    /**
     * Gets a child with the name passed as a parameter
     * @param name the name of the child to be looked up
     * @param sensitive true if the name is to be compared in a case sensitive
     * way
     * @return the searched child, or null if no child has that name
     */
    public TreeNode getChildByName(String name, boolean sensitive);

    /**
     * Returns true if this node has children
     * @return true if this node has children
     */
    public boolean hasChildren();

    /**
     * Returns a reference to the children list of this node
     * @return a reference to the children list of this node
     */
    public List getAllChildren();

    /**
     * Returns the children list as an array of objects
     * @return An array of Object instances with the children
     */
    public Object[] toArray();

    /**
     * Connects this node to its parent
     * @param value the parent node
     */
    public void setParent(TreeNode value);

    /**
     * Gets the parent of this node
     * @return the parent of this node
     */
    public TreeNode getParent();

    /**
     * Returns the iterator for the children of this node
     * @return an Iterator
     */
    public Iterator<TreeNode> iterator();


    // Secondary methods
    /**
     * Returns the number of children of this node
     * @return the number of children of this node
     */
    public int getSize();

    /**
     * Returns the number of children which are files
     * @return int files being children of this node
     **/
    public int getFilesSize();

    /**
     * Returns the number of children which are Component instances
     * @return int files being children of this node
     **/
    public int getComponentsSize();

    /**
     * Returns the nth child represented by the index
     * @param index the child index number to return
     * @return a child object (TreeNode)
     */
    public java.lang.Object getElementAt(int index);

    @Override
    public int compareTo(java.lang.Object obj);

    /**
     * Prepares a string representation of this node
     * @return  a string representation of this node
     */
    @Override
    public String toString();

    /** Builds a datamodel tree
     * @param myself the node to add the children to
     * @param level The maximum depth level desired for the tree
     */
    public void buildTree(DefaultMutableTreeNode myself, int level);

    /**
     * Marks the MozTreeNode with the current time as provided by Kernel
     * (ie.: the exact current time except if a certain batch operation, like
     * loading or saving a glossary, is being performed, in which case a fixed
     * time is returned)
     */
    public void touch();

    /**
     * Setter for alteredTime
     * @param time a time as returned by Date.getTime()
     */
    public void setAlteredTime(long time);

    /**
     * Getter for alteredTime
     * @return alteredTime value
     */
    public long getAlteredTime();

    /**
     * Fills the parent array passed as a parameter with the names of every
     * parent of this node (except for Component instances, which are concatenated
     * as a single String)
     * @param parentArray a String Array which will be filled with the name of
     * every ancestor of this node
     */
    public void fillParentArray(String[] parentArray);

    /**
     * Traverses the entire datamodel tree performing a "command" on every node
     * @param command the TraverseCommand to be executed on every node
     * @param maxLevel the maximum depth level to reach
     */
    public void traverse(TraverseCommand command, int maxLevel);

    /**
     * Returns the nature of the node (is this node a Component, a File,
     * a Phrase...?)
     * @return an int value as defined by TreeNode.LEVEL_... constants
     */
    public int getLevel();
}
