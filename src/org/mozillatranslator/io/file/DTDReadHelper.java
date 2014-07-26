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
 * Ricardo Palomares (modifications to preserve original licenses and to deal with
 *                    external entities)
 *
 */


package org.mozillatranslator.io.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.mozillatranslator.datamodel.ExternalEntity;
import org.mozillatranslator.datamodel.MozLicense;
import org.mozillatranslator.io.common.MozIOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;


/**
 *
 * @author henrik
 */
public class DTDReadHelper extends DefaultHandler2 {

    // In order to get the SAXParser work properly with a DTD file not bound to any XML, we need to trick it into
    // believing it is working with an XML file
    private static String dummyXml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<!DOCTYPE dialog SYSTEM \"MozillaTranslator\">" +
            "<dialog></dialog>";
    private static final Logger fLogger = Logger.getLogger(DTDReadHelper.class.getPackage().
            getName());
    private LinkedHashMap map;
    private LinkedHashMap commentMap;
    private InputStream is;
    private MozLicense thisFileLicense;
    private ArrayList externalEntities = new ArrayList();
    private String tempCommentHolder = null;


    /** Creates a new instance of DTDReadHelper */
    public DTDReadHelper() {
        super();
        thisFileLicense = null;
    }


    @Override
    public InputSource resolveEntity(java.lang.String name, java.lang.String publicId,
            java.lang.String baseURI, java.lang.String systemId) {

        if ((name != null) && (name.startsWith("%"))) {
            return new InputSource(new StringReader(""));
        } else {
            return new InputSource(is);
        }
    }


    @Override
public void externalEntityDecl(String name, String publicId, String systemId) {
    fLogger.info("We have found an external entity decl. with these values:");
    fLogger.log(Level.INFO, "Name: {0} - Public ID: {1} - System ID: {2}", new Object[]{name, publicId, systemId});

    this.externalEntities.add(new ExternalEntity(name, publicId, systemId));
}


    @Override
    public void comment(char ch [], int start, int length) throws SAXException	{
        String thisComment = new String(ch, start, length);
        String entityName = null;
        int contribLine;
        int blankLine;
        Pattern p;
        Matcher m;


        // Have we found the MPL1 license block?
        if (thisComment.indexOf("*** BEGIN LICENSE BLOCK ***") > -1) {
            contribLine = thisComment.indexOf("Contributor");
            blankLine = thisComment.indexOf("   -\n", contribLine);
            thisFileLicense = new MozLicense(null);
            thisFileLicense.setLicenseBlock(thisComment);
            thisFileLicense.setInsertionPos(blankLine);
        } else if (thisComment.indexOf("http://mozilla.org/MPL/2.0/") > -1) {
            thisFileLicense = new MozLicense(null);
            thisFileLicense.setLicenseBlock(thisComment);
            thisFileLicense.setInsertionPos(-1); // No contributors to be added in this license
        } else if (thisComment.toUpperCase().indexOf("LOCALIZATION NOTE") > -1) {
            /* The localization note format should be:
             *   LOCALIZATION NOTE (entity): comment
             * comment may expand several lines
             *
             * However, sometimes no entity is given, so we need to take two
             * possible courses of action:
             *
             * - if entity is given, add this comment directly to commentMap
             * - if no entity is given, save the comment and associate it to the
             *   following entity read
             */

            p = Pattern.compile("LOCALIZATION NOTE\\s+\\(([^).]+)\\):?[\\n.]+$",
                    Pattern.CASE_INSENSITIVE);
            m = p.matcher(thisComment);

            if (m.matches()) {
                entityName = m.group(1);
            }

            // Force tempCommentHolder = null to discard potential previous
            // comments not assigned to entities
            tempCommentHolder = null;

            // If we've got a localization note referencing an entity,
            if (entityName != null) {
                // Let's put the entity name and the comment in a hash, for
                // quick retrieval later when creating the Phrase objects
                commentMap.put(entityName, thisComment);
            } else {
                // Let's save the comment in a temporal place so we can
                // associate it with the next read entity
                tempCommentHolder = thisComment;
            }
        }
    }


    public MozLicense getThisFileLicense() {
        return this.thisFileLicense;
    }


    public void loadOriginal(InputStream is, LinkedHashMap map,
            LinkedHashMap commentMap) throws MozIOException {
        try {
            this.is = is;
            this.map = map;
            this.commentMap = commentMap;

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            XMLReader xmlread = saxParser.getXMLReader();
            xmlread.setProperty("http://xml.org/sax/properties/lexical-handler", this );
            xmlread.setProperty("http://xml.org/sax/properties/declaration-handler", this);

            saxParser.parse(new ByteArrayInputStream(dummyXml.getBytes()), this);
        } catch (Exception e) {
            throw new MozIOException("DTD load original",e);
        }
    }


    @Override
    public void internalEntityDecl(String name, String value) {
        map.put(name, value);

        // If we had a saved localization note not associated to any entity,
        // we assume it is for this entity and bind them
        if (tempCommentHolder != null) {
            // Let's put the entity name and the comment in a hash, for
            // quick retrieval later when creating the Phrase objects
            commentMap.put(name, tempCommentHolder);

            // And remove the comment from the temporary holder
            tempCommentHolder = null;
        }
    }

    public ArrayList getExternalEntities() {
        return externalEntities;
    }
}
