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

package org.mozillatranslator.io.common;

import java.io.*;
import java.util.*;
import java.text.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;

/**
 *
 * @author  henrik
 */
public class XmlExporter {
    private static final String DMY_HMS = "dd-MM-yyyy hh:mm:ss";
    private static Document document;
    
    // FIXME: We need to fix exceptions
    /**
     * Saves an entire glossary as a XML file
     *
     * @param   bw  a BufferedWriter representing the actual file where the XML
     *              is being saved
     * @throws ParserConfigurationException
     * @throws DOMException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     **/
    public static void saveGlossary(BufferedWriter bw) throws ParserConfigurationException,
            DOMException, IOException, TransformerConfigurationException,
            TransformerException {
        DateFormat df = new SimpleDateFormat(XmlExporter.DMY_HMS);
        Iterator productIterator;
        Iterator productChildIterator;
        Iterator fileOrCompIterator;
        Iterator fileIterator;
        Element productElement;
        Element productChildsElement;
        Element pCElement;
        Element componentsElement = null;
        Element filesElement = null;
        Element root;
        Product currentProduct;
        ProductChild currentChild;
        Component currentComponent;
        GenericFile currentFile;

        root = XmlExporter.writeRootStart();

        productIterator = Kernel.datamodel.productIterator();
        while (productIterator.hasNext()) {
            currentProduct = (Product) productIterator.next();
            productElement = addNode(root, "parent");
            writeCommonData(productElement, currentProduct);
            
            // A product child is a platform, a region or a custom container
            productChildsElement = addNode(productElement, "productChilds");
            productChildIterator = currentProduct.iterator();
            while (productChildIterator.hasNext()) {
                currentChild = (ProductChild) productChildIterator.next();
                pCElement = addNode(productChildsElement, "productchild");
                writeCommonData(pCElement, (MozTreeNode) currentChild);
                writeTextTag(pCElement, "type", currentChild.getTypeName());
                
                if (currentChild instanceof CustomContainer) {
                    filesElement = addNode(pCElement, "files");
                    
                    fileIterator = currentChild.iterator();
                    while (fileIterator.hasNext()) {
                        currentFile = (GenericFile) fileIterator.next();
                        currentFile.increaseReferenceCount();
                        XmlExporter.saveFile(bw, currentFile, filesElement);
                        currentFile.decreaseReferenceCount();
                    }
                } else {
                    // We need to replicate here most of saveComponent() method
                    // because a productChild could have a file hanging directly
                    // from it, and we don't want to generalize saveComponent()
                    // to any MozTreeNode
                    
                    if (currentChild.getFilesSize()>0) {
                        filesElement = addNode(pCElement, "files");
                        writeTextTag(filesElement, "count", "" +
                                currentChild.getFilesSize());
                    }
                    
                    if (currentChild.getComponentsSize()>0) {
                        componentsElement = addNode(pCElement, "components");
                    }
                    
                    fileOrCompIterator = currentChild.iterator();
                    while (fileOrCompIterator.hasNext()) {
                        Object o = fileOrCompIterator.next();
                        
                        if (o instanceof GenericFile) {
                            currentFile = (GenericFile) o;
                            currentFile.increaseReferenceCount();
                            XmlExporter.saveFile(bw, currentFile, filesElement);
                            currentFile.decreaseReferenceCount();
                        } else {
                            currentComponent = (Component) o;
                            XmlExporter.saveComponent(bw, currentComponent, componentsElement);
                        }
                    }
                }
            }
        }
        XmlExporter.writeRootEnd(bw);
        document = null;
        
    }
    

    //FIXME: Exception
    /**
     * Generates the DOM (sub)tree for a Component object
     *
     * @param bw        a BufferedWriter representing the actual file where the
     *                  the XML is being saved
     * @param component the component to be exported as an XML chunk
     * @param root      the DOM parent element
     * @throws ParserConfigurationException
     * @throws DOMException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     **/
    public static void saveComponent(BufferedWriter bw, Component component,
            Element root) throws ParserConfigurationException, DOMException,
            IOException, TransformerConfigurationException, TransformerException {

        DateFormat df = new SimpleDateFormat(XmlExporter.DMY_HMS);
        boolean inside;
        Iterator fileOrCompIterator;
        GenericFile currentFile;
        Component currentComponent;
        Element componentElement;
        Element componentsElement = null;
        Element filesElement = null;
        Element parent;
        
        // If root==null, we're being called to export a partial glossary, so
        // we've got to start the XML document here
        if (root == null) {
            parent = XmlExporter.writeRootStart();
            inside = false;
        } else {
            parent = root;
            inside = true;
        }
        
        componentElement = addNode(parent, "component");
        writeCommonData(componentElement, component);

        if (component.getFilesSize()>0) {
            filesElement = addNode(componentElement, "files");
            writeTextTag(filesElement, "count", "" + component.getFilesSize());
        }
        
        if (component.getComponentsSize()>0) {
            componentsElement = addNode(componentElement, "components");
        }
        
        fileOrCompIterator = component.iterator();
        while (fileOrCompIterator.hasNext()) {
            Object o = fileOrCompIterator.next();
            
            if (o instanceof GenericFile) {
                currentFile = (GenericFile) o;
                currentFile.increaseReferenceCount();
                XmlExporter.saveFile(bw, currentFile, filesElement);
                currentFile.decreaseReferenceCount();
            } else {
                currentComponent = (Component) o;
                XmlExporter.saveComponent(bw, currentComponent, componentsElement);
            }
            
        }
        if (!inside) {
            XmlExporter.writeRootEnd(bw);
            document = null;
        }
    }
    
    /**
     * Exports a file into the XML document
     *
     * @param bw
     * @param mFile
     * @param root
     * @throws ParserConfigurationException
     * @throws DOMException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     **/
    public static void saveFile(BufferedWriter bw, GenericFile mFile, Element root) throws
            ParserConfigurationException, DOMException, IOException,
            TransformerConfigurationException, TransformerException {
        
        DateFormat df = new SimpleDateFormat(XmlExporter.DMY_HMS);
        Iterator phraseIterator, translationIterator, entityIterator;
        ExternalEntity curEntity;
        Phrase currentPhrase;
        Translation curTran;
        Element fileElement;
        Element licenseElement;
        Element extEntitiesElement;
        Element phrasesElement;
        Element curExtEntityElement;
        Element curPhraseElement;
        Element translationsElement;
        Element curTransElement;
        boolean inside = true;
        Element parent = root;
        
        if (root == null) {
            parent = XmlExporter.writeRootStart();
            inside = false;
        }
        fileElement = addNode(parent, "file");
        
        writeCommonData(fileElement, (MozTreeNode) mFile);
        writeTextTag(fileElement, "realfilename", mFile.getRealFilename());
        writeTextTag(fileElement, "relativefilename", mFile.getRelativeFilename());
        writeTextTag(fileElement, "type", mFile.getTypeName());
        
        if (mFile.getLicenseBlock() != null) {
            licenseElement = addNode(fileElement, "license");
            MozLicense thisFileLicense = mFile.getLicenseBlock();
            
            writeTextTag(licenseElement, "licenseblock",
                    thisFileLicense.getLicenseBlock());
            writeTextTag(licenseElement, "licensecontributor",
                    thisFileLicense.getLicenseContributor());
            writeTextTag(licenseElement, "licenseinsertionpoint",
                    "" + thisFileLicense.getInsertionPos());
        }
        
        if ((mFile instanceof DTDFile) &&
                (((DTDFile) mFile).getExternalEntities() != null)) {
            extEntitiesElement = addNode(fileElement, "externalentities");
            entityIterator = ((DTDFile) mFile).getExternalEntities().iterator();
            while (entityIterator.hasNext()) {
                curEntity = (ExternalEntity) entityIterator.next();
                curExtEntityElement = addNode(extEntitiesElement, "externalentity");
                
                writeTextTag(curExtEntityElement, "name", curEntity.getName());
                writeTextTag(curExtEntityElement, "systemId", curEntity.getSystemId());
                writeTextTag(curExtEntityElement, "publicId", curEntity.getPublicId());
            }
        }

        phrasesElement = addNode(fileElement, "phrases");
        writeTextTag(phrasesElement, "count", "" + mFile.getSize());
        
        phraseIterator = mFile.iterator();
        while (phraseIterator.hasNext()) {
            currentPhrase = (Phrase) phraseIterator.next();
            curPhraseElement = addNode(phrasesElement, "phrase");
            
            writeCommonData(curPhraseElement, currentPhrase);
            writeTextTag(curPhraseElement, "text", currentPhrase.getText());
            writeTextTag(curPhraseElement, "keeporiginal", "" +
                    currentPhrase.isKeepOriginal());
            writeTextTag(curPhraseElement, "fuzzy", "" + currentPhrase.isFuzzy());
            
            if (currentPhrase.getAccessConnection() != null) {
                writeTextTag(curPhraseElement, "accessconnection",
                        currentPhrase.getAccessConnection().getName());
            }
            if (currentPhrase.getCommandConnection() != null) {
                writeTextTag(curPhraseElement, "commandconnection",
                        currentPhrase.getCommandConnection().getName());
            }
            if (currentPhrase.getLabelConnection() != null) {
                writeTextTag(curPhraseElement, "labelconnection",
                        currentPhrase.getLabelConnection().getName());
            }
            translationsElement = addNode(curPhraseElement, "translations");
            
            writeTextTag(translationsElement, "count", "" + currentPhrase.getSize());
            translationIterator = currentPhrase.iterator();
            
            while (translationIterator.hasNext()) {
                curTran = (Translation) translationIterator.next();
                curTransElement = addNode(translationsElement, "translation");
                
                writeTextTag(curTransElement, "locale" , curTran.getName());
                writeTextTag(curTransElement, "text", curTran.getText());
                writeTextTag(curTransElement, "status", "" + curTran.getStatus());
                writeTextTag(curTransElement, "comment", curTran.getComment());
                writeTextTag(curTransElement, "alteredtime", "" +
                        curTran.getAlteredTime());
                writeTextTag(curTransElement, "friendlyalteredtime",
                        df.format(new Date(curTran.getAlteredTime())));
            }
        }
        
        if (!inside) {
            XmlExporter.writeRootEnd(bw);
            document = null;
        }
    }
    
    public static Element addNode(Element parent, String tag) throws DOMException {
        Element result = document.createElement(tag);
        parent.appendChild(result);
        return result;
    }
    
    public static String escapeText(String text) {
        String result;
        
        result = text.replaceAll("\\p{Cntrl}", "?");
        return result;
    }

    /**
     * Writes the common data of nearly every datamodel element
     *
     * @param e             the DOM element representing currentNode
     * @param currentNode   the datamodel node to which the data belongs
     **/
    private static void writeCommonData(Element e, MozTreeNode currentNode) {
        DateFormat df = new SimpleDateFormat(XmlExporter.DMY_HMS);

        writeTextTag(e, "name", currentNode.getName());
        writeTextTag(e, "alteredtime", "" + currentNode.getAlteredTime());
        writeTextTag(e, "friendlyalteredtime", df.format(new Date(currentNode.getAlteredTime())));
    }
    
    public static void writeTextTag(Element parent, String tag, String text) throws DOMException {
        Element textElement = document.createElement(tag);
        
        textElement.appendChild(document.createTextNode(escapeText(text)));
        parent.appendChild(textElement);
    }
    
    /**
     * Declares and starts a new XML document
     *
     * @throws ParserConfigurationException
     **/
    public static Element writeRootStart() throws ParserConfigurationException {
        Element root;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
        root = (Element) document.createElement("mozillatranslator");
        document.appendChild(root);
        
        return root;
    }
    
    /**
     * Finishes the XML document (private property document)
     *
     * @param bw    the BufferedWriter where the XML file is being saved
     **/
    public static void writeRootEnd(BufferedWriter bw) throws
            TransformerConfigurationException, TransformerException {

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(bw);
        transformer.transform(source, result);
    }
}
