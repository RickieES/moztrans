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
 * Ricardo Palomares (refactoring to remove subcomponent from the datamodel and
 *                    adding additional functions for accesskeys/commandkeys
 *                    handling)
 *
 */

package org.mozillatranslator.datamodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;
import java.util.logging.*;
import java.util.StringTokenizer;
import org.mozillatranslator.kernel.*;

/** Implements a phrase, which is a string coming from a .DTD entity or a
 * .properties property
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class Phrase extends MozTreeNode {
    private String text;
    private final static String EMPTY = "[:empty:]";
    private final PropertyChangeSupport pcs;
    /**
     * A comparator for Phrase objects ordering based on text property
     */
    public static final Comparator<Phrase> TEXT_ORDER = new Comparator<Phrase>() {
                        @Override
                        public int compare(Phrase p1, Phrase p2) {
                            return p2.getText().compareToIgnoreCase(p1.getText());
                        }
    };

    /*
     * This is actually not part of the glossary. We use this field to aggregate
     * textual results from filters/queries (i.e.: bad accesskey, wrong puntuaction,
     * etc.)
     */
    private String filterResult;

    /*
     * Calling fillParentArray() to get the component and product columns for a
     * given Phrase in a ComplexTableWindow is too expensive and a Phrase
     * never changes its parent, so we save the value in componentPath,
     * calculating it at Phrase creation time
     */
    private Product productParent;
    private String componentPath;

    private String localizationNote;
    private boolean keepOriginal;
    private boolean fuzzy;
    private int sort;
    private Phrase labelConnection;
    private Phrase accessConnection;
    private Phrase commandConnection;
    private static final Logger fLogger = Logger.getLogger(Phrase.class.getPackage().getName());

    /** Creates new Component
     *
     * @param n     the entity/property name
     * @param p     the parent of this node (usually a subclass of MozFile)
     * @param value the entity/property text (the original one)
     */
    public Phrase(String n, TreeNode p, String value) {
        super(n, p, TreeNode.LEVEL_PHRASE);

        final String[] parentList = {"", "", "", "", "", "", "", "", "", ""};
        keepOriginal = false;
        fuzzy = false;
        sort = -1;

        this.fillParentArray(parentList);
        componentPath = parentList[TreeNode.LEVEL_COMPONENT];
        productParent = (Product) Kernel.datamodel.getChildByName(parentList[TreeNode.LEVEL_PRODUCT]);
        
        if (Kernel.settings.getBoolean(Settings.USE_SUGGESTIONS)) {
            pcs = new PropertyChangeSupport(this);
            this.addPropertyChangeListener(Kernel.ts);
        } else {
            pcs = null;
        }
        
        this.setText(value);
    }

    /** Returns the original text of a phrase.
     *
     * @return Value of property text.
     */
    public String getText() {
        return text;
    }

    /** Sets the original text of a phrase.
     *
     * @param text New value of property text.
     */
    public final void setText(final String text) {
        String old = this.text;
        this.text = text;
        touch(); // Updates last modified time on changing text
        if (Kernel.settings.getBoolean(Settings.USE_SUGGESTIONS)
                && (!(isAccesskey() || isCommandkey()))) {
            this.pcs.firePropertyChange("text", old, text);
        }
    }

    /**
     * Getter for componentPath
     * @return componentPath value
     */
    public String getComponentPath() {
        return componentPath;
    }

    /**
     * Setter for componentPath
     * @param componentPath the new value of componentPath
     */
    public void setComponentPath(final String componentPath) {
        this.componentPath = componentPath;
    }

    public Product getProductParent() {
        return productParent;
    }

    /**
     * Returns true if the phrase is set to always use the original value, even
     * if translations are available.
     *
     * @return true if keepOriginal is set
     */
    public boolean isKeepOriginal() {
        return keepOriginal;
    }

    /**
     * Sets the keepOriginal value
     *
     * @param value true or false depending if the original phrase text must
     *              always be used.
     */
    public void setKeepOriginal(final boolean value) {
        keepOriginal = value;
        touch(); // Updates last modified time on changing Keep Original value
    }

    /**
     * Returns true if the phrase is marked as "fuzzy" (ie., it has changed and
     * the translator hasn't validated the current translation)
     *
     * @return true if the phrase is fuzzy
     */
    public boolean isFuzzy() {
        return fuzzy;
    }

    /**
     * Sets the fuzzy flag
     *
     * @param value true or false, depending if the phrase is in a fuzzy status
     */
    public void setFuzzy(final boolean value) {
        fuzzy = value;
    }

    /**
     * Returns the sort value of this phrase
     *
     * @return the ordinal sort number
     */
    public int getSort() {
        return sort;
    }

    /** Sets the sort value of this phrase
     *
     * @param   sort    the ordinal value of this phrase in its parent file
     */
    public void setSort(final int sort) {
        this.sort = sort;
    }

    /**
     * Returns the access connection, which is a reference to another phrase
     * that has been identified as the accesskey for the label represented by
     * this phrase. Only useful for phrases representing labels.
     *
     * @see #isLabel()
     * @return null if no access connection exists, or the Phrase object pointing
     *              to the accesskey for this label
     */
    public Phrase getAccessConnection() {
        return accessConnection;
    }

    /**
     * Sets the access connection for this phrase
     *
     * @param value the Phrase object pointing to the accesskey
     */
    public void setAccessConnection(final Phrase value) {
        accessConnection = value;
    }

    /**
     * Returns the command connection, which is a reference to another phrase
     * that has been identified as the commandkey for the label represented by
     * this phrase. Only useful for phrases representing labels.
     *
     * @see #isLabel()
     * @return null if no command connection exists, or the Phrase object pointing
     *              to the commandkey for this label
     */
    public Phrase getCommandConnection() {
        return commandConnection;
    }

    /**
     * Sets the command connection for this phrase
     *
     * @param value the Phrase object pointing to the commandkey
     */
    public void setCommandConnection(final Phrase value) {
        commandConnection = value;
    }

    /**
     * Returns the label connection, which is a reference to another phrase
     * that has been identified as the label for the accesskey/commandkey
     * represented by this phrase. Only useful for phrases representing accesskeys
     * or commandkeys.
     *
     * @see #isAccesskey()
     * @see #isCommandkey()
     * @return null if no label connection exists, or the Phrase object pointing
     *              to the label for this accesskey/commandkey
     */
    public Phrase getLabelConnection() {
        return this.labelConnection;
    }

    /**
     * Sets the label connection for this phrase
     *
     * @param labelConnection Setter for labelConnection property
     */
    public void setLabelConnection(final Phrase labelConnection) {
        this.labelConnection = labelConnection;
    }

    /**
     * Adds a child to Phrase (a Phrase child is a Translation)
     * @param child the child to be added
     */
    @Override public void addChild(final TreeNode child) {
        children.add(child);
    }

    /**
     * Removes the child from this Phrase
     * @param child the child to be removed
     */
    @Override public void removeChild(final TreeNode child) {
        if (children.contains(child)) {
            children.remove(child);
            child.setParent(null);
        }
    }

    /**
     * Checks if the Phrase has not been marked and, if so, clears the children
     * list and returns true
     * @return true if the Phrase has not been marked and, therefore, must be
     * deleted
     */
    @Override public boolean deleteUntouched() {
        boolean result = false;
        if (!mark) {
            fLogger.log(Level.INFO, "Found untouched {0}", name);
            children.clear();
            result = true;
        }
        return result;
    }

    /**
     * Returns true if this phrase is a label as specified in
     * Settings.getProperty(Settings.CONN_LABEL_PATTERNS)
     *
     * @return true if the phrase is a label
     */
    public boolean isLabel() {
        final Settings set = Kernel.settings;
        String pattern = null;
        boolean includeEmpty = false;
        boolean result = false;
        final boolean caseSensitive = set.getBoolean(Settings.CONN_LABEL_CASESENSE);
        final StringTokenizer st = new StringTokenizer(
                set.getString(Settings.CONN_LABEL_PATTERNS), "|");

        while ((!result) && (st.hasMoreTokens())) {
            pattern = st.nextToken();
            includeEmpty = includeEmpty || (pattern.equals(Phrase.EMPTY));

            result = ((caseSensitive && (this.name.endsWith(pattern))) ||
                    (this.name.toLowerCase().endsWith(pattern.toLowerCase())));
        }

        // If we haven't found if this is a label so far and we have to take into
        // account the empty suffix, then we need to consider keys with no suffix
        // (it means, with no other label suffix, but also not including any of
        // the accesskeys and commandkeys suffixes)
        if (!result && includeEmpty) {
            result = !(this.isAccesskey() || this.isCommandkey());
        }

        return result;
    }

    /**
     * Returns true if this phrase is an accesskey as specified in
     * Settings.getProperty(Settings.CONN_AKEYS_PATTERNS)
     *
     * @return true if the phrase is an accesskey
     */
    public boolean isAccesskey() {
        final Settings set = Kernel.settings;
        String pattern = null;
        boolean result = false;
        final boolean caseSensitive = set.getBoolean(Settings.CONN_AKEYS_CASESENSE);
        final StringTokenizer st = new StringTokenizer(
                set.getString(Settings.CONN_AKEYS_PATTERNS), "|");

        while ((!result) && (st.hasMoreTokens())) {
            pattern = st.nextToken();

            result = ((caseSensitive && (this.name.endsWith(pattern))) ||
                    (this.name.toLowerCase().endsWith(pattern.toLowerCase())));
        }

        return result;
    }

    /**
     * Returns true if this phrase is an commandkey as specified in
     * Settings.getProperty(Settings.CONN_CKEYS_PATTERNS)
     *
     * @return true if the phrase is a commandkey
     */
    public boolean isCommandkey() {
        final Settings set = Kernel.settings;
        String pattern = null;
        boolean result = false;
        final boolean caseSensitive = set.getBoolean(Settings.CONN_CKEYS_CASESENSE);
        final StringTokenizer st = new StringTokenizer(
                set.getString(Settings.CONN_CKEYS_PATTERNS), "|");

        while ((!result) && (st.hasMoreTokens())) {
            pattern = st.nextToken();

            result = ((caseSensitive && (this.name.endsWith(pattern))) ||
                    (this.name.toLowerCase().endsWith(pattern.toLowerCase())));
        }

        return result;
    }

    /**
     * Tries to find the accesskey and commandkey for this phrase (which is
     * supposed to be a label), and makes the connections
     *
     * @param keepExisting  true if existing connections must be preserved, false
     *                      if they should be overwrited
     * @return true if at least one connection is made
     */
    public boolean linkLabel2Keys(final boolean keepExisting) {
        final Settings set = Kernel.settings;
        boolean includeEmpty = false;
        boolean result = false;
        boolean caseSensitive;
        boolean suffixFound;
        boolean aKeyFound = false;
        boolean cKeyFound = false;
        StringTokenizer st;
        String suffix = null;
        String keyRoot = null;
        String nameAKey = null;
        String nameCKey = null;
        Phrase phAKey = null;
        Phrase phCKey = null;

        // First of all, we need to check that we are dealing with a label
        if (result = this.isLabel()) {
            // Find out which suffix applies, so we can extract the "key root"
            // (for instance: the key root of "blabla.label" would be "blabla")
            caseSensitive = set.getBoolean(Settings.CONN_LABEL_CASESENSE);
            st = new StringTokenizer(set.getString(Settings.CONN_LABEL_PATTERNS), "|");
            suffixFound = false;

            while ((!suffixFound) && (st.hasMoreTokens())) {
                suffix = st.nextToken();
                includeEmpty = includeEmpty || (suffix.equals(Phrase.EMPTY));

                suffixFound = ((caseSensitive && this.name.endsWith(suffix)) ||
                               (this.name.toLowerCase().endsWith(suffix.toLowerCase())));
            }

            if (!suffixFound && includeEmpty) {
                suffixFound = !(this.isAccesskey() || this.isCommandkey());
                suffix = (suffixFound) ? "" : suffix;
            }


            // If no suffix found, no positive result anyway
            result = suffixFound;

            if (suffixFound) {
                keyRoot = this.name.substring(0, this.name.length() - suffix.length());

                // Let's start with the accesskey
                caseSensitive = set.getBoolean(Settings.CONN_AKEYS_CASESENSE);
                st = new StringTokenizer(set.getString(Settings.CONN_AKEYS_PATTERNS), "|");

                // For every possible accesskey suffix, try to locate the phrase
                while ((!aKeyFound) && (st.hasMoreTokens())) {
                    suffix = st.nextToken();
                    nameAKey = keyRoot + suffix;

                    phAKey = (Phrase) this.getParent().getChildByName(nameAKey, caseSensitive);
                    aKeyFound = (phAKey != null);
                }

                // We will only return true if at least one connection is made
                result = false;

                // And, if found, make the connection
                if (aKeyFound && (this.getAccessConnection()==null || !keepExisting)) {
                    this.setAccessConnection(phAKey);
                    phAKey.setLabelConnection(this);
                    result = true;
                }


                // Repeat the process with the commandkey
                caseSensitive = set.getBoolean(Settings.CONN_CKEYS_CASESENSE);
                st = new StringTokenizer(set.getString(Settings.CONN_CKEYS_PATTERNS), "|");

                // For every possible commandkey suffix, try to locate the phrase
                while ((!cKeyFound) && (st.hasMoreTokens())) {
                    suffix = st.nextToken();
                    nameCKey = keyRoot + suffix;

                    phCKey = (Phrase) this.getParent().getChildByName(nameCKey, caseSensitive);
                    cKeyFound = (phCKey != null);
                }

                // And, if found, make the connection
                if (cKeyFound && (this.getCommandConnection()==null || !keepExisting)) {
                    this.setCommandConnection(phCKey);
                    phCKey.setLabelConnection(this);
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Tries to find the label for this phrase (which is supposed to be an
     * accesskey), and makes the connections
     *
     * @return true if the connection is made
     */
    public boolean linkAKey2Label() {
        final Settings set = Kernel.settings;
        boolean includeEmpty = false;
        boolean result = false;
        boolean caseSensitive;
        boolean suffixFound;
        boolean labelFound = false;
        StringTokenizer st;
        String suffix = null;
        String keyRoot = null;
        String nameLabel = null;
        Phrase phLabel = null;

        // First of all, we need to check that we are dealing with an accesskey
        if (result = this.isAccesskey()) {
            // Find out which suffix applies, so we can extract the "key root"
            // (for instance: the key root of "blabla.accesskey" would be "blabla")
            caseSensitive = set.getBoolean(Settings.CONN_AKEYS_CASESENSE);
            st = new StringTokenizer(set.getString(Settings.CONN_AKEYS_PATTERNS), "|");
            suffixFound = false;

            while ((!suffixFound) && (st.hasMoreTokens())) {
                suffix = st.nextToken();
                suffixFound = ((caseSensitive && this.name.endsWith(suffix)) ||
                               (this.name.toLowerCase().endsWith(suffix.toLowerCase())));
            }

            // If no suffix found, no positive result anyway
            result = suffixFound;

            if (suffixFound) {
                keyRoot = this.name.substring(0, this.name.length() - suffix.length());

                // Now we look up for the label
                caseSensitive = set.getBoolean(Settings.CONN_LABEL_CASESENSE);
                st = new StringTokenizer(set.getString(Settings.CONN_LABEL_PATTERNS), "|");

                // For every possible label suffix, try to locate the phrase
                while ((!labelFound) && (st.hasMoreTokens())) {
                    suffix = st.nextToken();
                    includeEmpty = includeEmpty || (suffix.equals(Phrase.EMPTY));
                    nameLabel = keyRoot + suffix;

                    phLabel = (Phrase) this.getParent().getChildByName(nameLabel, caseSensitive);
                    labelFound = (phLabel != null);
                }

                if (includeEmpty) {
                    phLabel = (Phrase) this.getParent().getChildByName(keyRoot, caseSensitive);
                    labelFound = (phLabel != null);
                }

                // We will only return true if the connection is made
                result = false;

                // And, if found, make the connection
                if (labelFound) {
                    this.setLabelConnection(phLabel);
                    phLabel.setAccessConnection(this);
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Tries to find the label for this phrase (which is supposed to be a
     * commandkey), and makes the connections
     *
     * @return true if the connection is made
     */
    public boolean linkCKey2Label() {
        final Settings set = Kernel.settings;
        boolean includeEmpty = false;
        boolean result = false;
        boolean caseSensitive;
        boolean suffixFound;
        boolean labelFound = false;
        StringTokenizer st;
        String suffix = null;
        String keyRoot = null;
        String nameLabel = null;
        Phrase phLabel = null;

        // First of all, we need to check that we are dealing with a commandkey
        if (result = this.isCommandkey()) {
            // Find out which suffix applies, so we can extract the "key root"
            // (for instance: the key root of "blabla.commandkey" would be "blabla")
            caseSensitive = set.getBoolean(Settings.CONN_CKEYS_CASESENSE);
            st = new StringTokenizer(set.getString(Settings.CONN_CKEYS_PATTERNS), "|");
            suffixFound = false;

            while ((!suffixFound) && (st.hasMoreTokens())) {
                suffix = st.nextToken();
                suffixFound = ((caseSensitive && this.name.endsWith(suffix)) ||
                               (this.name.toLowerCase().endsWith(suffix.toLowerCase())));
            }

            // If no suffix found, no positive result anyway
            result = suffixFound;

            if (suffixFound) {
                keyRoot = this.name.substring(0, this.name.length() - suffix.length());

                // Now we look up for the label
                caseSensitive = set.getBoolean(Settings.CONN_LABEL_CASESENSE);
                st = new StringTokenizer(set.getString(Settings.CONN_LABEL_PATTERNS), "|");

                // For every possible label suffix, try to locate the phrase
                while ((!labelFound) && (st.hasMoreTokens())) {
                    suffix = st.nextToken();
                    includeEmpty = includeEmpty || (suffix.equals(Phrase.EMPTY));
                    nameLabel = keyRoot + suffix;

                    phLabel = (Phrase) this.getParent().getChildByName(nameLabel, caseSensitive);
                    labelFound = (phLabel != null);
                }

                if (includeEmpty) {
                    phLabel = (Phrase) this.getParent().getChildByName(keyRoot, caseSensitive);
                    labelFound = (phLabel != null);
                }

                // We will only return true if the connection is made
                result = false;

                // And, if found, make the connection
                if (labelFound) {
                    this.setLabelConnection(phLabel);
                    phLabel.setCommandConnection(this);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Getter for filterResult property
     * @return the filterResult property
     */
    public String getFilterResult() {
        return filterResult;
    }

    /**
     * Setter for filterResult property
     * @param filterResult the new value for filterResult
     */
    public void setFilterResult(final String filterResult) {
        this.filterResult = filterResult;
    }

    /**
     * Adds a string to the filterResult property
     * @param filterResult the string to be added to filterResult
     */
    public void addFilterResult(final String filterResult) {
        if ((getFilterResult() == null) || (getFilterResult().length() == 0)) {
            setFilterResult("- " + filterResult);
        } else {
            setFilterResult(getFilterResult() + "\n- " + filterResult);
        }
    }

    /**
     * Getter for localizationNote property
     * @return the value of localizationNote
     */
    public String getLocalizationNote() {
        return localizationNote;
    }

    /**
     * Setter for localizationNote property
     * @param localizationNote the new value for localizationNote property
     */
    public void setLocalizationNote(final String localizationNote) {
        this.localizationNote = localizationNote;
    }
    
    /**
     * Allows other objects to listen on changes in this object properties
     * Actually, only "text" property will notify of changes at this moment
     * @param listener an object implementing PropertyChangeListener
     */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        if (Kernel.settings.getBoolean(Settings.USE_SUGGESTIONS)) {
            this.pcs.addPropertyChangeListener(listener);
        }
    }

    /**
     * Allows other objects to stop listening on changes in this object
     * properties
     * @param listener an object implementing PropertyChangeListener which
     *                 should be already registered
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (Kernel.settings.getBoolean(Settings.USE_SUGGESTIONS)) {
            this.pcs.removePropertyChangeListener(listener);
        }
    }    
}
