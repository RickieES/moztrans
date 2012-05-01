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
 * Ricardo Palomares (Initial code)
 *
 */
package org.mozillatranslator.dataobjects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.mozillatranslator.datamodel.Phrase;
import org.mozillatranslator.datamodel.Product;

/**
 * Dataobject comprising all the data needed for a product update, both
 * for JAR-based or VCS-based products
 * @author rpalomares
 */
public class ProductUpdateDataObject {
    private Product prod;
    private String l10n;
    private List<Phrase> changeList;
    private boolean runAutoTranslate;
    private File importDir;

    public ProductUpdateDataObject() {
        changeList = new ArrayList<Phrase>();
    }

    public List<Phrase> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<Phrase> changeList) {
        this.changeList = changeList;
    }

    public File getImportDir() {
        return importDir;
    }

    public void setImportDir(File importDir) {
        this.importDir = importDir;
    }

    public String getL10n() {
        return l10n;
    }

    public void setL10n(String l10n) {
        this.l10n = l10n;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public boolean isRunAutoTranslate() {
        return runAutoTranslate;
    }

    public void setRunAutoTranslate(boolean runAutoTranslate) {
        this.runAutoTranslate = runAutoTranslate;
    }
}
