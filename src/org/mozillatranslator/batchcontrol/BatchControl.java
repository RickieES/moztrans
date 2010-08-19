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

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import org.mozillatranslator.kernel.*;

public class BatchControl
{
    
    private HashMap commandMap = new HashMap();
    
    public BatchControl()
    {
        int count = Kernel.settings.getInteger(Settings.BATCH_COMMAND_COUNT);
        try
        {
            for (int i = 0; i < count; i++)
            {
                String curName = Kernel.settings.getString(Settings.BATCH_COMMAND_PREFIX + i + Settings.BATCH_COMMAND_NAME);
                String curClass = Kernel.settings.getString(Settings.BATCH_COMMAND_PREFIX + i + Settings.BATCH_COMMAND_CLASS);
                
                BatchCommand curCommand = (BatchCommand) Class.forName(curClass).newInstance();
                
                commandMap.put(curName, curCommand);
            }
        }
        catch (Exception e)
        {
            Kernel.appLog.log(Level.SEVERE, "Could not initialize BatchControl", e);
        }
        
        
    }
    /** Runs a set of batch commands
     * @param commands The commands to run
     */
    public void run(String[] args)
    {
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
        
        try
        {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            
            fis = new FileInputStream(commandFile);
            document = builder.parse(fis);
            
            commands = document.getDocumentElement().getChildNodes();
            
            
            while (!failed && i < commands.getLength())
            {
                currentNode = commands.item(i);
                
                if (currentNode.getNodeType() == Element.ELEMENT_NODE)
                {
                    commandName = currentNode.getNodeName();
                    
                    if (commandMap.containsKey(commandName))
                    {
                        currentCommand = (BatchCommand) commandMap.get(commandName);
                        failed = currentCommand.action((Element) currentNode);
                    }
                }
                i++;
            }
            
        }
        catch (Exception e)
        {
            Kernel.appLog.log(Level.SEVERE, "Batch run stopped", e);
        }
        
    }
}

