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
 * Portions created by Ricardo Palomares are
 * Copyright (C) Ricardo Palomares.
 * All Rights Reserved.
 *
 * Contributor(s):
 * Ricardo Palomares (Initial Code)
 *
 */


package org.mozillatranslator.datamodel;

/**
 * This class represents an external entity. External entities are used in some
 * DTD files in Mozilla Localization (at the time of this writing, netError.dtd
 * and others), and must be stored apart from ordinary entities. They are only
 * used at loading/importing and saving/exporting time, so the datamodel doesn't
 * actually need to care about them besides being able to store the ArrayList
 * in which we save them.
 *
 * @author rpalomares
 */
public class ExternalEntity {
    private String name;
    private String publicId;
    private String systemId;

    /** Creates a new instance of ExternalEntity */
    public ExternalEntity() {
    }

    public ExternalEntity(String name, String publicId, String systemId) {
        this.name = name;
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public String getName() {
        return (name == null) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicId() {
        return (publicId == null) ? "" : publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSystemId() {
        return (systemId == null) ? "" : systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

}
