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

package org.mozillatranslator.batchcontrol;

import java.util.logging.*;
import org.mozillatranslator.kernel.*;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.runner.*;
import org.w3c.dom.*;

/**
 *
 * @author  henrik
 */
public class UpdateGlossaryCommand extends BatchCommand {
    private static final Logger fLogger = Logger.getLogger(UpdateGlossaryCommand.class.
            getPackage().getName());
    
    // information needed
    private String name;
    
    /** Creates a new instance of UpdateGlossaryCommand */
    public UpdateGlossaryCommand() {
    }
    
    public boolean action(Element command) throws BatchException {
        NodeList list = command.getChildNodes();
        Node currentNode;
        Node valueNode;
        for (int i = 0; i < list.getLength(); i++) {
            currentNode = list.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getNodeName().equalsIgnoreCase("name")) {
                    valueNode = currentNode.getFirstChild();
                    name = valueNode.getNodeValue();
                    Product prod = (Product) Kernel.datamodel.getChildByName(name);
                    if (prod != null) {
                        UpdateProductRunner upr = new UpdateProductRunner(prod);
                        fLogger.info("Starting update");
                        Serializer.action(upr);
                        fLogger.info("Ending update");
                    }
                    fLogger.info("Product name: *" + name + "*");
                }
            }
            
        }
        return true;
    }
    
    public boolean  wasSuccessfull() {
        return true;
    }
    
}
