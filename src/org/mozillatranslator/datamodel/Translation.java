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

/**
 *
 * @author  Henrik Lynggaard
 * @version 1.0
 */
public class Translation extends MozTreeNode {
    public static final int STATUS_NOTSEEN = 0;
    public static final int STATUS_CHANGED = 1;
    public static final int STATUS_TRANSLATED = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_ACCEPTED = 4;
    public static final int STATUS_PERFECT = 5;
    public static final int STATUS_OTHER = 6;
    public static final int STATUS_MIGRATED = 7;

    /** Holds value of property text. */
    private String text;

    /** Holds value of property status. */
    private TrnsStatus status;

    /** Holds value of property comment. */
    private String comment;

    /** Creates new Component
     * @param n name of the item
     * @param p parent item of this one
     * @param t translated text
     * @param s status
     */
    public Translation(String n, TreeNode p, String t, TrnsStatus s) {
        super(n, p, TreeNode.LEVEL_TRANSLATION);
        text = t;
        status = s;
        comment = "";
        children = null; // Translation is a leaf object, doesn't have children
    }

    /** Getter for property text.
     * @return Value of property text.
     */
    public String getText() {
        return text;
    }

    /** Setter for property text.
     * @param text New value of property text.
     */
    public void setText(final String text) {
        this.text = text;
        touch(); // Update last modified time on changing text
    }

    /** Getter for property status.
     * @return Value of property status.
     */
    public TrnsStatus getStatus() {
        return status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(final TrnsStatus status) {
        this.status = status;
    }

    /** Getter for property comment.
     * @return Value of property comment.
     */
    public String getComment() {
        return comment;
    }

    /** Setter for property comment.
     * @param comment New value of property comment.
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Checks if the Translation has not been marked and, if so, clears the children
     * list and returns true
     * @return true if the Phrase has not been marked and, therefore, must be
     * deleted
     */
    @Override
    public boolean deleteUntouched() {
        return mark;
    }
}
