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

/** Runs Batch jobs from the command line
 * @author Henrik Lynggaard
 * @version 1.0
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BatchControl {
    private HashMap<String, BatchCommand> commandMap = new HashMap<>(10);

    public BatchControl() {
        int count = Kernel.settings.getInteger(Settings.BATCH_COMMAND_COUNT);
        for (int i = 0; i < count; i++) {
            try {
                String curName = Kernel.settings.getString(
                        Settings.BATCH_COMMAND_PREFIX + i + Settings.BATCH_COMMAND_NAME);
                String curClass = Kernel.settings.getString(
                        Settings.BATCH_COMMAND_PREFIX + i + Settings.BATCH_COMMAND_CLASS);
                BatchCommand curCommand = (BatchCommand) Class.forName(curClass)
                        .getDeclaredConstructor().newInstance();
                commandMap.put(curName, curCommand);
            } catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException | NoSuchMethodException
                    | SecurityException | IllegalArgumentException
                    | InvocationTargetException ex) {
                Logger.getLogger(BatchControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** Runs a set of batch commands
     * @param args The commands to run
     */
    public void run(String[] args) {
        try {
            String commandFile;
            DocumentBuilder builder;
            Document document;
            FileInputStream fis;
            Node currentNode;
            NodeList commands;
            String commandName;
            BatchCommand currentCommand;
            boolean failed = false;
            int i = 0;
            commandFile = args[0];
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            fis = new FileInputStream(commandFile);
            document = builder.parse(fis);
            commands = document.getDocumentElement().getChildNodes();
            while (!failed && i < commands.getLength()) {
                currentNode = commands.item(i);
                if (currentNode.getNodeType() == Element.ELEMENT_NODE) {
                    commandName = currentNode.getNodeName();
                    if (commandMap.containsKey(commandName)) {
                        currentCommand = commandMap.get(commandName);
                        failed = currentCommand.action((Element) currentNode);
                    }
                }
                i++;
            }
        } catch (BatchException | SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(BatchControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
