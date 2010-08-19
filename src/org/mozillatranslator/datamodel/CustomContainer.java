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
import org.mozillatranslator.io.*;

/** A container for custom files added to a Product
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class CustomContainer extends MozTreeNode implements ProductChild {
    private static CustomContainerAccess containeraccess = new CustomContainerAccess();

    /** Creates new Custom
     * @param n The name of the container
     * @param p The parent.
     */
    public CustomContainer(String n, TreeNode p) {
        super(n, p, TreeNode.LEVEL_PRODUCTCHILD);
    }

    public int getType() {
        return ProductChild.TYPE_CUSTOM;
    }

    public void load(ProductChildInputOutputDataObject dataObject) throws IOException {
        dataObject.setProductChild(this);
        switch (dataObject.getFormat()) {
            case ProductChildInputOutputDataObject.FORMAT_JAR:
                containeraccess.load(dataObject);
                break;
        }
    }

    public void save(ProductChildInputOutputDataObject dataObject) throws IOException {
        dataObject.setProductChild(this);
        switch (dataObject.getFormat()) {
            case ProductChildInputOutputDataObject.FORMAT_JAR:
                containeraccess.save(dataObject);
                break;
        }
    }

    public String getJarFile() {
        return null;
    }

    public String getJarInXpiFile(String l10n) {
        return null;
    }

    public String getLocaleDisplay(String l10n) {
        return null;
    }

    public String getTypeName() {
        return "Custom";
    }
}
