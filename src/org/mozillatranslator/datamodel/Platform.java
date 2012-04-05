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

import java.io.IOException;
import org.mozillatranslator.dataobjects.ProductChildInputOutputDataObject;
import org.mozillatranslator.io.StructureAccess;

/**
 * <h3>Change List</h3>
 *
 * <br><b>From 1.0 to 1.2 </b>
 * <li> Added get/set neutral
 * <li> synced versioning with CVS
 *
 * @author  Henrik Lynggaard
 * @version 1.2
 */
public class Platform extends MozTreeNode implements ProductChild {
    private static StructureAccess jaraccess = new StructureAccess();
    private String jarFile;
    private int type;

    /** Creates new Platform */
    public Platform(String n, TreeNode p, int t) {
        super(n, p, TreeNode.LEVEL_PRODUCTCHILD);
        type = t;
        jarFile = "";
    }

    public void setJarFile(String file) {
        jarFile = file;
        touch(); // Update last modified time when changing the JAR file
    }

    @Override
    public String getJarFile() {
        return jarFile;
    }

    public void setType(int value) {
        type = value;
        touch(); // Update last modified time on changing the type of this platform
    }

    @Override
    public int getType() {
        return type;
    }

    public boolean isNeutral() {
        return (type == TYPE_NEUTRAL);
    }

    @Override
    public String getTypeName() {
        String result = "";
        switch (type) {
            case TYPE_NEUTRAL:
                result = "Neutral";
                break;
            case TYPE_WINDOWS:
                result = "win";
                break;
            case TYPE_UNIX:
                result = "unix";
                break;
            case TYPE_MAC:
                result = "mac";
                break;
            case TYPE_OTHER:
                result = name;
        }
        return result;
    }

    public static String  getTypeDef(int type) {
        String result = "";
        switch (type) {
            case TYPE_WINDOWS:
                result = "-win";
                break;
            case TYPE_UNIX:
                result = "-unix";
                break;
            case TYPE_MAC:
                result = "-mac";
                break;
        }
        return result;
    }

    @Override
    public void load(ProductChildInputOutputDataObject dataObject) throws IOException {
        dataObject.setProductChild(this);
        switch (dataObject.getFormat()) {
            case ProductChildInputOutputDataObject.FORMAT_JAR:
                jaraccess.load(dataObject);
                break;
        }
    }

    @Override
    public void save(ProductChildInputOutputDataObject dataObject) throws IOException {
        dataObject.setProductChild(this);
        switch (dataObject.getFormat()) {
            case ProductChildInputOutputDataObject.FORMAT_JAR:
                jaraccess.save(dataObject);
                break;
        }
    }

    @Override
    public String getJarInXpiFile(String l10n) {
        String result = l10n;
        switch (this.type) {
            case ProductChild.TYPE_NEUTRAL:
                result = l10n;
                break;
            case ProductChild.TYPE_UNIX:
            case ProductChild.TYPE_WINDOWS:
            case ProductChild.TYPE_MAC:
                result = l10n.substring(0, l10n.indexOf("-")) + getTypeDef(type);
                break;
        }
        return result;
    }

    @Override
    public String getLocaleDisplay(String l10n) {
        return l10n;
    }
}
