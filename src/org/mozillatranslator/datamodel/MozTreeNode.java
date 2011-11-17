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
 * Ricardo Palomares (added getFilesSize and getComponentsSize)
 *
 */
package org.mozillatranslator.datamodel;

import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.mozillatranslator.kernel.*;
import java.text.Collator;
import java.text.CollationKey;

/**
 * <h3>Change List</h3>
 *
 * <br><b>From 1.2 to 1.3</b>
 * <li>Added getParent
 *
 * <br><b>From 1.0 to 1.2 </b>
 * <li> Added iterator
 * <li> synced versioning with CVS
 *
 * @author  Henrik Lynggaard
 * @version 1.2
 */
public abstract class MozTreeNode extends AbstractListModel implements TreeNode {

    /** Node name (filename, entity/key name, locale code for translation... */
    protected String name;
    /** List of children nodes phrases of a file, translations of a phrase... */
    protected List children;
    /** Parent node of this one */
    protected TreeNode parent;
    /** TODO: document properly this variable */
    protected boolean mark;
    /** Timestamp of last altered time */
    protected long alteredTime;
    /** Sets a correspondence between the node type and a array with different
     *  levels */
    protected int treeLevel;
    private static final Logger fLogger = Logger.getLogger(MozTreeNode.class.getPackage().getName());

    /** Creates new MozTreeNode
     * @param n the name of the MozTreeNode
     * @param p the parent of this node
     * @param level the tree level in the datamodel hierarchy as defined in
     *              TreeNode
     */
    public MozTreeNode(String n, TreeNode p, int level) {
        name = n;
        parent = p;
        treeLevel = level;
        children = new ArrayList(1);
        mark = false;
    }

    /**
     * Setter for name property
     * @param n the name to set
     */
    @Override
    public void setName(String n) {
        name = n;
    }

    /**
     * Getter for name property
     * @return the name value
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Setter for mark boolean property
     */
    @Override
    public void setMark() {
        mark = true;
    }

    /**
     * Getter for mark boolean property
     * @return mark value
     */
    @Override
    public boolean isMark() {
        return mark;
    }

    /**
     * Unsets the mark boolean property
     */
    @Override
    public void clearMark() {
        mark = false;
    }

    /*
     * Methods to manage the children list
     */

    /**
     * Adds a TreeNode child to the list
     * @param child the TreeNode child to be added
     */
    @Override
    public void addChild(TreeNode child) {
        children.add(child);
    }

    /**
     * Removes a TreeNode child from the list
     * @param child the TreeNode child to be removed
     */
    @Override
    public void removeChild(TreeNode child) {
        if (children.contains(child)) {
            children.remove(child);
            child.setParent(null);
            touch(); // Update last modified time when a child is deleted
        }
    }

    /**
     * Removes all the children from the list in a recursive way
     */
    @Override
    public void removeChildren() {
        Iterator itChildren = children.iterator();
        while (itChildren.hasNext()) {
            MozTreeNode child = (MozTreeNode) itChildren.next();
            if (child.getAllChildren() != null) {
                child.removeChildren();
            }
            child.setParent(null);
        }
        itChildren = null;

        while (children.size() > 0) {
            children.remove(0);
        }
        touch(); // Update last modified time when removing all children
    }

    /**
     * Gets a child by its name in a case sensitive way
     * @param childName the name of the child to get
     * @return the desired child, or null if the child is not found
     */
    @Override
    public TreeNode getChildByName(String childName) {
        return getChildByName(childName, true);
    }

    /**
     * Gets a child by its name
     * @param childName the name of the child to get
     * @param sensitive true if the name should be looked up in a case sensitive
     *                  way
     * @return the desired child, or null if the child is not found
     */
    @Override
    public TreeNode getChildByName(String childName, boolean sensitive) {
        TreeNode current = null;
        TreeNode result = null;
        boolean done = false;
        Iterator childIterator = children.iterator();

        if (sensitive) {
            while (!done && childIterator.hasNext()) {
                current = (TreeNode) childIterator.next();

                if (childName.equals(current.getName())) {
                    done = true;
                    result = current;
                }
            }
        } else {
            while (!done && childIterator.hasNext()) {
                current = (TreeNode) childIterator.next();

                if (childName.equalsIgnoreCase(current.getName())) {
                    done = true;
                    result = current;
                }
            }
        }
        return result;
    }

    /**
     * Checks if the node has children
     * @return true if there is one or more children
     */
    @Override
    public boolean hasChildren() {
        return children.size() > 0 ? true : false;
    }

    /**
     * Returns the children list as an array of objects
     * @return An array of Object instances with the children
     */
    @Override
    public Object[] toArray() {
        return children.toArray();
    }

    /**
     * Returns the number of children which are files
     * @return int files being children of this node
     **/
    @Override
    public int getFilesSize() {
        int result = 0;
        Iterator childrenIterator = children.iterator();

        while (childrenIterator.hasNext()) {
            Object currentNode = childrenIterator.next();

            if (currentNode instanceof GenericFile) {
                result++;
            }
        }
        return result;
    }

    /**
     * Returns the number of children which are Component instances
     * @return int files being children of this node
     **/
    @Override
    public int getComponentsSize() {
        int result = 0;
        Iterator childrenIterator = children.iterator();

        while (childrenIterator.hasNext()) {
            Object currentNode = childrenIterator.next();

            if (currentNode instanceof Component) {
                result++;
            }
        }
        return result;
    }

    /*
     * Methods for the AbstractListModel
     */

    @Override
    public int getSize() {
        return children.size();
    }

    @Override
    public java.lang.Object getElementAt(int index) {
        return children.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result;

        if (obj instanceof TreeNode) {
            TreeNode other = (TreeNode) obj;
            result = name.equals(other.getName());
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.parent != null ? this.parent.hashCode() : 0);
        return hash;
    }


    /*
     * Methods for the Comparable interface
     */
    @Override
    public int compareTo(java.lang.Object obj) {
        TreeNode other = (TreeNode) obj;
        return name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     *
     * @param myself
     * @param wantedLevel
     */
    @Override
    public void buildTree(DefaultMutableTreeNode myself, int wantedLevel) {
        // If we need to build a tree with all components, we can't stop at
        // first occurrence of a component; it may have children that are
        // component too
        if ((wantedLevel == treeLevel && treeLevel == TreeNode.LEVEL_COMPONENT)
                || (wantedLevel > treeLevel)) {
            Iterator childIterator = children.iterator();

            while (childIterator.hasNext()) {
                TreeNode currentChild = (TreeNode) childIterator.next();

                // When should we recurse over children?
                // a) When we have not reached yet the wanted depth level
                boolean recurse = (wantedLevel > treeLevel);
                // b) When we have reached the wanted depth level but the
                // current child is of the same level
                recurse = recurse
                        || ((wantedLevel == treeLevel)
                            && (currentChild.getLevel() == wantedLevel));

                if (recurse) {
                    DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(currentChild);
                    myself.add(currentNode);
                    currentChild.buildTree(currentNode, wantedLevel);
                }
            }
            orderChildren(myself);
        }
    }

    /**
     * Orders the children of a DefaultMutableTreeNode
     * @param parent
     */
    private void orderChildren(Object parent) {
        if (parent == null) {
            return;
        }

        if (parent instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
            ArrayList childrenList = Collections.list(node.children());
            Collections.sort(childrenList, this.getComparator());
            node.removeAllChildren();
            Iterator childrenIterator = childrenList.iterator();
            while (childrenIterator.hasNext()) {
                node.add((DefaultMutableTreeNode) childrenIterator.next());
            }
        }
    }

    /**
     * getComparator
     *
     * @return Object
     */
    private Comparator getComparator() {
        class ProductCollator extends Collator {

            /**
             * Compares its two arguments for order.
             *
             * @param o1 the first object to be compared.
             * @param o2 the second object to be compared.
             * @return a negative integer, zero, or a positive integer as the first
             *   argument is less than, equal to, or greater than the second.
             */
            @Override
            public int compare(Object o1, Object o2) {
                /*
                int res = compare(((MozTreeNode)((DefaultMutableTreeNode) o1).
                getUserObject()).getName(), ((MozTreeNode)
                ((DefaultMutableTreeNode)o2).getUserObject()).getName());
                 */
                int res = 0;
                MozTreeNode node1;
                MozTreeNode node2;

                node1 = (MozTreeNode) ((DefaultMutableTreeNode) o1).getUserObject();
                node2 = (MozTreeNode) ((DefaultMutableTreeNode) o2).getUserObject();

                // We always want components before files
                res = (node1.getLevel() > node2.getLevel()) ? 1
                        : ((node1.getLevel() < node2.getLevel()) ? -1 : 0);

                if (res == 0) {
                    res = compare(node1.getName(), node2.getName());
                }

                return res;
            }

            /**
             * Transforms the String into a series of bits that can be compared bitwise
             * to other CollationKeys.
             *
             * @param source the string to be transformed into a collation key.
             * @return the CollationKey for the given String based on this Collator's
             *   collation rules. If the source String is null, a null CollationKey is
             *   returned.
             */
            @Override
            public CollationKey getCollationKey(String source) {
                return Collator.getInstance().getCollationKey(source);
            }

            /**
             * Compares the source string to the target string according to the
             * collation rules for this Collator.
             *
             * @param source the source string.
             * @param target the target string.
             * @return Returns an integer value. Value is less than zero if source is
             *   less than target, value is zero if source and target are equal, value
             *   is greater than zero if source is greater than target.
             */
            @Override
            public int compare(String source, String target) {
                return Collator.getInstance().compare(source, target);
            }

            /**
             * Returns a hash code value for the object.
             *
             * @return a hash code value for this object.
             */
            @Override
            public int hashCode() {
                return Collator.getInstance().hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final ProductCollator other = (ProductCollator) obj;
                return true;
            }
        }
        return new ProductCollator();
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean deleteUntouched() {
        boolean result = false;
        if (!mark) {
            fLogger.log(Level.INFO, "Found untouched {0}", name);
            result = true;
        } else {
            Iterator childIterator = children.iterator();

            while (childIterator.hasNext()) {
                TreeNode currentChild = (TreeNode) childIterator.next();
                if (currentChild.deleteUntouched()) {
                    fLogger.log(Level.INFO, "Removing child {0}", currentChild.getName());
                    childIterator.remove();
                    this.touch();
                }
            }
        }
        return result;
    }

    /**
     * Returns a reference to the children list
     * @return a reference to the children list
     */
    @Override
    public List getAllChildren() {
        return children;
    }

    /** Returns a iterator for the children.
     * @return The iterator
     * @since 1.2
     */
    @Override
    public Iterator iterator() {
        return children.iterator();
    }

    /**
     * Setter for TreeNode parent
     * @param value the TreeNode to be set as parent of this one
     */
    @Override
    public void setParent(TreeNode value) {
        parent = value;
    }

    /**
     * Getter for TreeNode parent
     * @return a reference to the parent TreeNode
     */
    @Override
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Marks the MozTreeNode with the current time as provided by Kernel
     * (ie.: the exact current time except if a certain batch operation, like
     * loading or saving a glossary, is being performed, in which case a fixed
     * time is returned)
     */
    @Override
    public final void touch() {
        setAlteredTime(Kernel.getCurrentTime());
    }

    /**
     * Setter for alteredTime
     * @param time a time as returned by Date.getTime()
     */
    @Override
    public void setAlteredTime(long time) {
        alteredTime = time;
    }

    /**
     * Getter for alteredTime
     * @return alteredTime value
     */
    @Override
    public long getAlteredTime() {
        return alteredTime;
    }

    /**
     * Fills the parent array passed as a parameter with the names of every
     * parent of this node (except for Component instances, which are concatenated
     * as a single String)
     * @param parentArray a String Array which will be filled with the name of
     * every ancestor of this node
     */
    @Override
    public void fillParentArray(String[] parentArray) {
        if (parent != null) {
            parent.fillParentArray(parentArray);
        }

        if (treeLevel == TreeNode.LEVEL_COMPONENT) {
            parentArray[treeLevel] = parentArray[treeLevel] + "/" + name;
        } else {
            parentArray[treeLevel] = name;
        }
    }

    /**
     * Traverses the entire datamodel tree performing a "command" on every node
     * @param command the TraverseCommand to be executed on every node
     * @param maxLevel the maximum depth level to reach
     */
    @Override
    public void traverse(TraverseCommand command, int maxLevel) {
        if (command.action(this)) {
            if (maxLevel > treeLevel) {
                Iterator childIterator = children.iterator();

                while (childIterator.hasNext()) {
                    MozTreeNode currentChild = (MozTreeNode) childIterator.next();
                    currentChild.traverse(command, maxLevel);
                }
            }
        }
        command.postop(this);
    }

    /**
     * Returns the nature of the node (is this node a Component, a File,
     * a Phrase...?)
     * @return an int value as defined by TreeNode.LEVEL_... constants
     */
    @Override
    public int getLevel() {
        return treeLevel;
    }

    /**
     * MT 5.0x used getLevel() for finding out both the depth of a MozTreeNode
     * in the datamodel tree (ie.: how many parents does this node have?) and
     * the nature of the node itself (ie.: is this node a Component, a File, a
     * Platform...?). This is no longer possible with a single query in MT 5.1,
     * as Component nodes can be children of other Component nodes, so getDepth()
     * will return the actual depth of the node in the datamodel tree, and
     * getLevel will keep returning the nature of the node
     *
     * @return number of parents of this node
     */
    public int getDepth() {
        int depth = 1;
        TreeNode thisParent = this.getParent();

        while (thisParent != null) {
            depth++;
            thisParent = thisParent.getParent();
        }
        return depth;
    }
}
