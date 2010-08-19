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

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.mozillatranslator.kernel.*;

/** Represents the entire glossary/datamodel.
 * <br>
 * The datamodel looks like this
 * <br>
 * <pre>
 * Glossary
 * + Products
 *  + Neutral platform
 *    * (Component,File)    A File can be child of a platform or a component
 *      * (Component, File) A Component can be child of a platform or a component
 *       (...)
 *        + File            Eventually, every branch ends like this
 *          + Phrase
 *            + Translation
 *  + specific Platforms (0..n)
 *    + Component
 *    * (Component,File)    A File can be child of a platform or a component
 *      * (Component, File) A Component can be child of a platform or a component
 *       (...)
 *        + File            Eventually, every branch ends like this
 *          + Phrase
 *            + Translation
 *  + Regional files
 *    + Component
 *    * (Component,File)    A File can be child of a platform or a component
 *      * (Component, File) A Component can be child of a platform or a component
 *       (...)
 *        + File            Eventually, every branch ends like this
 *          + Phrase
 *            + Translation
 *  + Custom file
 *    + File
 *      + Phrase
 *        + Translation
 * </pre>
 *
 * <h3>Change List</h3>
 *
 * <br><b>From 1.0 to 1.4 </b>
 * <li> Added productIterator
 * <li> synced versioning with CVS
 *
 *
 * @author Henrik Lynggaard
 * @version 1.4
 */
public class DataModel extends AbstractListModel implements Serializable {

    private List products;

    /** Creates new Datamodel */
    public DataModel() {
        products = new ArrayList();
    }

    /** Get the number of products
     * @return The number of products
     */
    @Override
    public int getSize() {
        return products.size();
    }

    /** Removes a product from the list of products
     * @param p The product to remove
     */
    public void removeProduct(Product p) {
        products.remove(p);
        fireContentsChanged(this, 0, products.size());
    }

    /** Adds a product to the list of products
     * @param p The product to add
     */
    public void addProduct(Product p) {
        products.add(p);
        fireContentsChanged(this, 0, products.size());
    }

    /** Gets a product based on the index in the product list
     * @param index the index in the product list
     * @return the Product at the index
     */
    @Override
    public java.lang.Object getElementAt(int index) {
        return products.get(index);
    }

    /** Gets the Root of the datamodel tree.
     * @param wantedLevel The maximum depth level desired for the tree
     * @return The root of the tree
     */
    public DefaultMutableTreeNode getTree(int wantedLevel) {

        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Root");
        buildTree(treeRoot, wantedLevel);
        return treeRoot;
    }

    /** Builds the datamodel tree
     * @param myself the node to add the children to
     * @param wantedLevel The maximum depth level desired for the tree
     */
    public void buildTree(DefaultMutableTreeNode myself, int wantedLevel) {
        Kernel.appLog.log(Level.INFO, "Building tree");
        Iterator productIterator = products.iterator();

        while (productIterator.hasNext()) {

            Product currentProduct = (Product) productIterator.next();
            Kernel.appLog.log(Level.INFO, "build" + currentProduct.getName());
            DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(currentProduct);

            myself.add(currentNode);

            currentProduct.buildTree(currentNode, wantedLevel);
        }
    }

    /** Returns the Products as an Array
     * @return An array of products
     */
    public Object[] toArray() {
        return products.toArray();
    }

    /** Returns a iterator for the products
     * @return the iterator
     * @since 1.4
     */
    public Iterator productIterator() {
        return products.iterator();
    }

    /**
     * Fill the parent array element corresponding to this level
     * @param parentArray
     */
    public void fillParentArray(String[] parentArray) {
        parentArray[0] = "Datamodel";
    }

    /**
     * Traverses the product list of this datamodel
     * @param command An action to do with this level of the datamodel
     * @param maxLevel The maximum depth level to dig into the tree
     */
    public void traverse(TraverseCommand command, int maxLevel) {

        command.action(this);
        if (maxLevel > TreeNode.LEVEL_MODEL) {
            Iterator productIterator = products.iterator();

            while (productIterator.hasNext()) {
                Product currentProduct = (Product) productIterator.next();

                currentProduct.traverse(command, maxLevel);
            }
        }
    }

    /**
     * Returns one product by its name
     * @param name the product name to look for
     * @return a TreeNode which represents the product
     */
    public TreeNode getChildByName(String name) {
        return getChildByName(name, true);
    }

    /**
     * Returns one product by its name
     * @param name the product name to look for
     * @param sensitive true if the case must be honored in the search
     * @return a TreeNode which represents the product
     */
    public TreeNode getChildByName(String name, boolean sensitive) {
        TreeNode current = null;
        TreeNode result = null;
        boolean done = false;
        Iterator childIterator = products.iterator();

        while (!done && childIterator.hasNext()) {
            current = (TreeNode) childIterator.next();

            if ((name.equals(current.getName()) && sensitive)
                    || name.equalsIgnoreCase(current.getName())) {
                done = true;
                result = current;
            }
        }
        return result;
    }
}
