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

package org.mozillatranslator.runner;

import org.mozillatranslator.dataobjects.ProductChildInputOutputDataObject;
import org.mozillatranslator.io.common.FileUtils;
import org.mozillatranslator.dataobjects.WriteXpiDataObject;
import org.mozillatranslator.datamodel.*;
import org.mozillatranslator.kernel.*;
import java.io.*;
import java.util.jar.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.*;

/** This runner will write a XPI file
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class WriteXPIRunner extends MozTask {
    private WriteXpiDataObject  xpiData;
    /** Creates new WriteXPIRunner */
    public WriteXPIRunner() {
    }

    /** The method that is run and that creates the file
     * @throws MozException 
     */
    @Override
    public void taskImplementation() throws MozException {
        Iterator currentIterator;
        ProductChild currentChild;
        InputStream is;
        FileOutputStream fos;
        JarOutputStream jos;
        JarOutputStream currentJos;
        ByteArrayOutputStream currentBaos;
        JarEntry je;

        xpiData = (WriteXpiDataObject) dataObject;

        ProductChildInputOutputDataObject pcioDataObject =
                new ProductChildInputOutputDataObject();
        pcioDataObject.setVersion(xpiData.getVersion());
        try {
            fos = new FileOutputStream(xpiData.getFileName());
            jos = new JarOutputStream(fos);

            /*
             * Creation of the Neutral ab-CD.JAR
             */
            Kernel.appLog.info("Exporting Platform Neutral");
            currentIterator = xpiData.getNeutrals().iterator();
            if (currentIterator.hasNext()) {
                currentBaos = new ByteArrayOutputStream();
                currentJos = new JarOutputStream(currentBaos);
                pcioDataObject.setJarOutStream(currentJos);

                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    currentChild.save(pcioDataObject);
                }
                currentJos.close();
                je = new JarEntry("chrome/" + pcioDataObject.getJarFileInXpi() +
                        ".jar");
                jos.putNextEntry(je);
                FileUtils.saveFile(jos, currentBaos.toByteArray());
            }

            /*
             * Creation of the Windows ab-win.jar
             */
            Kernel.appLog.info("Exporting Windows");
            currentIterator = xpiData.getWindows().iterator();
            if (currentIterator.hasNext()) {
                currentBaos = new ByteArrayOutputStream();
                currentJos = new JarOutputStream(currentBaos);
                pcioDataObject.setJarOutStream(currentJos);

                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    currentChild.save(pcioDataObject);
                }
                currentJos.close();
                je = new JarEntry("chrome/" + pcioDataObject.getJarFileInXpi() +
                        ".jar");
                jos.putNextEntry(je);
                FileUtils.saveFile(jos, currentBaos.toByteArray());
            }

            /*
             * Creation of the Mac ab-mac.jar
             */
            Kernel.appLog.info("Exporting Mac");
            currentIterator = xpiData.getMacs().iterator();
            if (currentIterator.hasNext()) {
                currentBaos = new ByteArrayOutputStream();
                currentJos = new JarOutputStream(currentBaos);
                pcioDataObject.setJarOutStream(currentJos);

                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    currentChild.save(pcioDataObject);
                }
                currentJos.close();
                je = new JarEntry("chrome/" + pcioDataObject.getJarFileInXpi() +
                        ".jar");
                jos.putNextEntry(je);
                FileUtils.saveFile(jos, currentBaos.toByteArray());
            }

            /*
             * Creation of the Unix ab-unix.jar
             */
            Kernel.appLog.info("Exporting unix");
            currentIterator = xpiData.getUnixes().iterator();
            if (currentIterator.hasNext()) {
                currentBaos = new ByteArrayOutputStream();
                currentJos = new JarOutputStream(currentBaos);
                pcioDataObject.setJarOutStream(currentJos);

                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    currentChild.save(pcioDataObject);
                }
                currentJos.close();
                je = new JarEntry("chrome/" + pcioDataObject.getJarFileInXpi() +
                        ".jar");
                jos.putNextEntry(je);
                FileUtils.saveFile(jos, currentBaos.toByteArray());
            }

            /*
             * Creation of the Region CD.jar
             */
            Kernel.appLog.info("Exporting regions");
            
            // We can't know in advance if the regions will have something
            // to export/write, so we mark currentBaos and currentJos as
            // null, and signal that there is something to write by assigning
            // them again a not-null value
            currentBaos = null;
            currentJos = null;
            currentIterator = xpiData.getRegions().iterator();
            if (currentIterator.hasNext()) {
                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    
                    if (currentChild.hasChildren()) {
                        if (currentBaos == null) {
                            currentBaos = new ByteArrayOutputStream();
                            currentJos = new JarOutputStream(currentBaos);
                            pcioDataObject.setJarOutStream(currentJos);
                        }
                        currentChild.save(pcioDataObject);
                    }
                }
                
                if (currentJos != null) {
                    currentJos.close();
                    je = new JarEntry("chrome/" + pcioDataObject.getJarFileInXpi()
                                      + ".jar");
                    jos.putNextEntry(je);
                    FileUtils.saveFile(jos, currentBaos.toByteArray());
                }
            }

            /*
             * Creation of the custom files
             */
            Kernel.appLog.info("Exporting customs");
            currentIterator = xpiData.getCustoms().iterator();
            if (currentIterator.hasNext()) {
                pcioDataObject.setJarOutStream(jos);

                while (currentIterator.hasNext()) {
                    resetJarData(pcioDataObject);
                    currentChild = (ProductChild) currentIterator.next();
                    currentChild.save(pcioDataObject);
                }
            }

            // Install script
            je = new JarEntry("install.js");
            jos.putNextEntry(je);
            writeInstallScript(jos);

            // If file "install.properties" exists in the current directory,
            // use it (this allows localizers to translate it); else, get the
            // en-US install.properties from the MozillaTranslator JAR itself.
            je = null;
            File instProp = new File("./install.properties");
            if (instProp.exists()) {
                je = new JarEntry("install.properties");
                is = new BufferedInputStream(new FileInputStream(instProp));
            } else {
                // We try to get the bundled en-US install.properties
                is = (InputStream) this.getClass().getResourceAsStream(
                        "/org/mozillatranslator/resource/install.properties");

                // If the file exists in the JAR itself
                if (is != null) {
                    je = new JarEntry("install.properties");
                }
            }

            // If we have found any install.properties,
            if (je != null) {
                jos.putNextEntry(je);
                currentBaos = new ByteArrayOutputStream();
                int b;
                while ((b = is.read()) != -1) {
                    currentBaos.write(b);
                }
                FileUtils.saveFile(jos, currentBaos.toByteArray());
            }

            jos.close();

            if (xpiData.isUseExternalZIP()) {
                // Expand and compress the JAR/ZIP file to workaround bug 197792
                FileUtils.expandCompress(xpiData);
            }
            
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "Error writing xpi file", e);
            JOptionPane.showMessageDialog(Kernel.mainWindow, "" + e.getMessage(),
                    "Error xpi export", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetJarData(ProductChildInputOutputDataObject pcioDataObject) {
        pcioDataObject.setAuthor(xpiData.getAuthor());

        pcioDataObject.setChangeList(null);
        pcioDataObject.setDisplay(xpiData.getDisplay());
        pcioDataObject.setFormat(ProductChildInputOutputDataObject.FORMAT_JAR);
        pcioDataObject.setL10n(xpiData.getL10n());
        pcioDataObject.setPreviewUrl(xpiData.getPreviewUrl());
        pcioDataObject.setVersion(xpiData.getVersion());
    }

    /**
     * This will write the install script-
     * @param jos the jar output stream to write to
     * @throws IOException If anything blows
     */
    private void writeInstallScript(JarOutputStream jos) throws IOException {
        PrintWriter pw;
        final String QUOTE_SEMICOLON = "\";";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, "iso8859-1");
        pw = new PrintWriter(osw);

        // license part
        pw.println("// ***** BEGIN LICENSE BLOCK *****");
        pw.println("// Version: MPL 1.1/GPL 2.0/LGPL 2.1");
        pw.println("//");
        pw.println("// The contents of this file are subject to the Mozilla Public License Version");
        pw.println("// 1.1 (the \"License\"); you may not use this file except in compliance with");
        pw.println("// the License. You may obtain a copy of the License at");
        pw.println("// http://www.mozilla.org/MPL/");
        pw.println("//");
        pw.println("// Software distributed under the License is distributed on an \"AS IS\" basis,");
        pw.println("// WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License");
        pw.println("// for the specific language governing rights and limitations under the");
        pw.println("// License.");
        pw.println("//");
        pw.println("// The Original Code is mozilla.org code.");
        pw.println("//");
        pw.println("// The Initial Developer of the Original Code is");
        pw.println("// the Mozilla Organization.");
        pw.println("// Portions created by the Initial Developer are Copyright (C) 1998-2004");
        pw.println("// the Initial Developer. All Rights Reserved.");
        pw.println("//");
        pw.println("// Contributor(s):");
        pw.println("//   Henrik Lynggaard <admin@mozillatranslator.org>");
        pw.println("//   Robert Kaiser <KaiRo@KaiRo.at>");
        pw.println("//   Nagarjuna Venna <vnagarjuna@yahoo.com>");
        pw.println("//");
        pw.println("// Alternatively, the contents of this file may be used under the terms of");
        pw.println("// either the GNU General Public License Version 2 or later (the \"GPL\"), or");
        pw.println("// the GNU Lesser General Public License Version 2.1 or later (the \"LGPL\"),");
        pw.println("// in which case the provisions of the GPL or the LGPL are applicable instead");
        pw.println("// of those above. If you wish to allow use of your version of this file only");
        pw.println("// under the terms of either the GPL or the LGPL, and not to allow others to");
        pw.println("// use your version of this file under the terms of the MPL, indicate your");
        pw.println("// decision by deleting the provisions above and replace them with the notice");
        pw.println("// and other provisions required by the GPL or the LGPL. If you do not delete");
        pw.println("// the provisions above, a recipient may use your version of this file under");
        pw.println("// the terms of any one of the MPL, the GPL or the LGPL.");
        pw.println("//");
        pw.println("// ***** END LICENSE BLOCK *****");
        pw.println("");
        // l10n resources
        pw.println("// --- strings specific to that single Language Pack ---");
        pw.println("var prettyName =\"" + xpiData.getDisplay() + QUOTE_SEMICOLON);
        pw.println("var langcode = \"" + xpiData.getL10n().substring(0,
                xpiData.getL10n().indexOf("-")) + QUOTE_SEMICOLON);
        pw.println("var regioncode = \"" + xpiData.getL10n().substring(
                xpiData.getL10n().indexOf("-") + 1) + QUOTE_SEMICOLON);
        pw.println("var version = \"" + xpiData.getVersion() + QUOTE_SEMICOLON);
        pw.println("// --- end pack-specific strings ---");
        pw.println("");
        // verify disk space function
        pw.println("// this function verifies disk space in kilobyes");
        pw.println("function verifyDiskSpace(dirPath, spaceRequired) {");
        pw.println("    var spaceAvailable;");
        pw.println("");
        pw.println("    // Get the available disk space on the given path");
        pw.println("    spaceAvailable = fileGetDiskSpaceAvailable(dirPath);");
        pw.println("");
        pw.println("    // Convert the available disk space into kilobytes");
        pw.println("    spaceAvailable = parseInt(spaceAvailable / 1024);");
        pw.println("");
        pw.println("  // do the verification");
        pw.println("    if(spaceAvailable < spaceRequired) {");
        pw.println("        logComment(\"Insufficient disk space: \" + dirPath);");
        pw.println("        logComment(\"  required : \" + spaceRequired + \" K\");");
        pw.println("        logComment(\"  available: \" + spaceAvailable + \" K\");");
        pw.println("        return(false);");
        pw.println("    }");
        pw.println("");
        pw.println("    return(true);");
        pw.println("}");
        pw.println("");
        // error strings
        pw.println("// this function converts an error number to the error code");
        pw.println("function ErrCode(errornum) {");
        pw.println("    if (errornum == 0)  {");
        pw.println("        errorstring = \"SUCCESS\";");
        pw.println("    }");
        pw.println("    else if (errornum == -200)  {");
        pw.println("        errorstring = \"BAD_PACKAGE_NAME\";");
        pw.println("    }");
        pw.println("    else if (errornum == -201) {");
        pw.println("        errorstring = \"UNEXPECTED_ERROR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -202) {");
        pw.println("        errorstring = \"ACCESS_DENIED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -203) {");
        pw.println("        errorstring = \"TOO_MANY_CERTIFICATES\";");
        pw.println("    }");
        pw.println("    else if (errornum == -204) {");
        pw.println("        errorstring = \"NO_INSTALL_SCRIPT\";");
        pw.println("    }");
        pw.println("    else if (errornum == -205) {");
        pw.println("        errorstring = \"NO_CERTIFICATE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -206) {");
        pw.println("        errorstring = \"NO_MATCHING_CERTIFICATE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -207) {");
        pw.println("        errorstring = \"CANT_READ_ARCHIVE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -208) {");
        pw.println("        errorstring = \"INVALID_ARGUMENTS\";");
        pw.println("    }");
        pw.println("    else if (errornum == -209) {");
        pw.println("        errorstring = \"ILLEGAL_RELATIVE_PATH\";");
        pw.println("    }");
        pw.println("    else if (errornum == -210) {");
        pw.println("        errorstring = \"USER_CANCELLED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -211) {");
        pw.println("        errorstring = \"INSTALL_NOT_STARTED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -212) {");
        pw.println("        errorstring = \"SILENT_MODE_DENIED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -213) {");
        pw.println("        errorstring = \"NO_SUCH_COMPONENT\";");
        pw.println("    }");
        pw.println("    else if (errornum == -214) {");
        pw.println("        errorstring = \"DOES_NOT_EXIST\";");
        pw.println("    }");
        pw.println("    else if (errornum == -215) {");
        pw.println("        errorstring = \"READ_ONLY\";");
        pw.println("    }");
        pw.println("    else if (errornum == -216) {");
        pw.println("        errorstring = \"IS_DIRECTORY\";");
        pw.println("    }");
        pw.println("    else if (errornum == -217) {");
        pw.println("        errorstring = \"NETWORK_FILE_IS_IN_USE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -218) {");
        pw.println("        errorstring = \"APPLE_SINGLE_ERR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -219) {");
        pw.println("        errorstring = \"INVALID_PATH_ERR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -220) {");
        pw.println("        errorstring = \"PATCH_BAD_DIFF\";");
        pw.println("    }");
        pw.println("    else if (errornum == -221) {");
        pw.println("        errorstring = \"PATCH_BAD_CHECKSUM_TARGET\";");
        pw.println("    }");
        pw.println("    else if (errornum == -222) {");
        pw.println("        errorstring = \"PATCH_BAD_CHECKSUM_RESULT\";");
        pw.println("    }");
        pw.println("    else if (errornum == -223) {");
        pw.println("        errorstring = \"UNINSTALL_FAILED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -224) {");
        pw.println("        errorstring = \"PACKAGE_FOLDER_NOT_SET\";");
        pw.println("    }");
        pw.println("    else if (errornum == -225) {");
        pw.println("        errorstring = \"EXTRACTION_FAILED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -226) {");
        pw.println("        errorstring = \"FILENAME_ALREADY_USED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -227) {");
        pw.println("        errorstring = \"INSTALL_CANCELLED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -228) {");
        pw.println("        errorstring = \"DOWNLOAD_ERROR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -229) {");
        pw.println("        errorstring = \"SCRIPT_ERROR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -230) {");
        pw.println("        errorstring = \"ALREADY_EXISTS\";");
        pw.println("    }");
        pw.println("    else if (errornum == -231) {");
        pw.println("        errorstring = \"IS_FILE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -232) {");
        pw.println("        errorstring = \"SOURCE_DOES_NOT_EXIST\";");
        pw.println("    }");
        pw.println("    else if (errornum == -233) {");
        pw.println("        errorstring = \"SOURCE_IS_DIRECTORY\";");
        pw.println("    }");
        pw.println("    else if (errornum == -234) {");
        pw.println("        errorstring = \"SOURCE_IS_FILE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -235) {");
        pw.println("        errorstring = \"INSUFFICIENT_DISK_SPACE\";");
        pw.println("    }");
        pw.println("    else if (errornum == -236) {");
        pw.println("        errorstring = \"FILENAME_TOO_LONG\";");
        pw.println("    }");
        pw.println("    else if (errornum == -237) {");
        pw.println("        errorstring = \"UNABLE_TO_LOCATE_LIB_FUNCTION\";");
        pw.println("    }");
        pw.println("    else if (errornum == -238) {");
        pw.println("        errorstring = \"UNABLE_TO_LOAD_LIBRARY\";");
        pw.println("    }");
        pw.println("    else if (errornum == -239) {");
        pw.println("        errorstring = \"CHROME_REGISTRY_ERROR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -240) {");
        pw.println("        errorstring = \"MALFORMED_INSTALL\";");
        pw.println("    }");
        pw.println("    else if (errornum == -299) {");
        pw.println("        errorstring = \"OUT_OF_MEMORY\";");
        pw.println("    }");
        pw.println("    else if (errornum == 999) {");
        pw.println("        errorstring = \"REBOOT_NEEDED\";");
        pw.println("    }");
        pw.println("    else if (errornum == -5550) {");
        pw.println("        errorstring = \"SHAPE_UNKNOWN_ERROR\";");
        pw.println("    }");
        pw.println("    else if (errornum == -5551) {");
        pw.println("        errorstring = \"SHAPE_INVALID_ARGUMENT\";");
        pw.println("    }");
        pw.println("    else {");
        pw.println("        errorstring = \"Error No. \" + errornum;");
        pw.println("    }");
        pw.println("");
        pw.println("    return(errorstring);");
        pw.println("}");
        pw.println("");
        pw.println("// this function converts an error number to a string with code and number");
        pw.println("function ErrWithCode(errornum) {");
        pw.println("  return(err + \" (\" + ErrCode(err) + \")\");");
        pw.println("}");
        pw.println("");
        // determine platform
        pw.println("// OS type detection - which platform?");
        pw.println("function getPlatform() {");
        pw.println("    var platformStr;");
        pw.println("    var platformNode;");
        pw.println("");
        pw.println("    if('platform' in Install) {");
        pw.println("        platformStr = new String(Install.platform);");
        pw.println("");
        pw.println("        if (!platformStr.search(/^Macintosh/))");
        pw.println("            platformNode = 'mac';");
        pw.println("        else if (!platformStr.search(/^Win/))");
        pw.println("            platformNode = 'win';");
        pw.println("        else if (!platformStr.search(/^OS\\/2/))");
        pw.println("            platformNode = 'win';");
        pw.println("        else");
        pw.println("            platformNode = 'unix';");
        pw.println("    }");
        pw.println("    else {");
        pw.println("        var fOSMac  = getFolder(\"Mac System\");");
        pw.println("        var fOSWin  = getFolder(\"Win System\");");
        pw.println("");
        pw.println("        logComment(\"fOSMac: \"  + fOSMac);");
        pw.println("        logComment(\"fOSWin: \"  + fOSWin);");
        pw.println("");
        pw.println("        if(fOSMac != null)");
        pw.println("            platformNode = 'mac';");
        pw.println("        else if(fOSWin != null)");
        pw.println("            platformNode = 'win';");
        pw.println("        else");
        pw.println("            platformNode = 'unix';");
        pw.println("    }");
        pw.println("");
        pw.println("    return platformNode;");
        pw.println("}");
        pw.println("");
        pw.println("// *** start main install routine ***");
        pw.println("");
        // variable definitions
        pw.println("var srDest = 1600;   // we need 1600KB free space for installation!");
        pw.println("var err;");
        pw.println("var fProgram;");
        pw.println("var platformNode;");
        pw.println("var profileInstall = 0;");
        pw.println("var alertstring;");
        pw.println("var wantGlobalInstall = 0;");
        pw.println("");
        pw.println("platformNode = getPlatform();");
        pw.println("logComment(\"language pack installation: platform type detected as: \" + platformNode);");
        pw.println("");
        // setup to install
        pw.println("var chromeNode = langcode + \"-\" + regioncode;");
        pw.println("var regName    = \"locales/mozilla/\" + chromeNode;");
        pw.println("var chromeName = chromeNode + \".jar\";");
        pw.println("var regionFile = regioncode + \".jar\";");
        pw.println("var platformName = langcode + \"-\" + platformNode + \".jar\";");
        pw.println("var localeName = \"locale/\" + chromeNode + \"/\";");
        pw.println("var regionName = \"locale/\" + regioncode + \"/\";");
        pw.println("");
        pw.println("err = initInstall(prettyName, regName, version);");
        pw.println("logComment(\"initInstall: \" + ErrWithCode(err));");
        pw.println("");
        pw.println("var messages = loadResources(\"install.properties\");");
        pw.println("");
        pw.println("fProgram = getFolder(\"Program\");");
        pw.println("logComment(\"fProgram: \" + fProgram);");
        pw.println("");
        pw.println("fSearchPlugins = getFolder(\"Program\", \"searchplugins\");");
        pw.println("logComment(\"searchplugins path detected as: \" + fSearchPlugins);");
        pw.println("");
        pw.println("if ((err == 0) && verifyDiskSpace(fProgram, srDest)) {");
        pw.println("    var chromeType = LOCALE;");
        pw.println("    var tellRestart = 0;");
        pw.println("");
        pw.println("  // ask for global or profile install");
        pw.println("  if (messages) {");
        pw.println("    if (confirm(messages.install_confirm)) {");
        pw.println("      wantGlobalInstall = 1;");
        pw.println("    }");
        pw.println("  }");
        pw.println("  else {");
        pw.println("    logComment(\"Missing message support detected. Trying to global install without asking the user.\");");
        pw.println("    wantGlobalInstall = 1;");
        pw.println("  }");
        pw.println("");
        pw.println("  if (wantGlobalInstall) {");
        pw.println("    err = addDirectory(\"\", \"chrome\", fProgram, \"chrome\");");
        pw.println("    logComment(\"addDirectory() for chrome returned: \" + ErrWithCode(err));");
        pw.println("");
        pw.println("    if (err == 0) {");
        pw.println("      // install defaults");
        pw.println("      err2 = addDirectory(\"\", \"defaults\", fProgram, \"defaults\");");
        pw.println("      logComment(\"addDirectory() for defaults returned: \" + ErrWithCode(err2));");
        pw.println("");
        pw.println("      // install search plugins");
        pw.println("      err2 = addDirectory(\"\", \"searchplugins\", fSearchPlugins, \"\");");
        pw.println("      logComment(\"addDirectory() for searchplugins returned: \" + ErrWithCode(err2));");
        pw.println("");
        pw.println("      // install myspell directories");
        pw.println("//      err2 = addDirectory(\"\", \"myspell\", fProgram, \"components/myspell\");");
        pw.println("//      logComment(\"addDirectory() for myspell returned: \" + ErrWithCode(err2));");
        pw.println("    }");
        pw.println("  }");
        pw.println("");
        pw.println("  if ((!wantGlobalInstall) || (err != 0)) {");
        pw.println("    // return value 0 is SUCCESS");
        pw.println("    logComment(\"addDirectory() to \" + fProgram + \"failed!\");");
        pw.println("");
        pw.println("    // couldn\'t install globally, try installing to the profile (does only install chrome part, no defaults, no searchplugins)");
        pw.println("    resetError();");
        pw.println("    chromeType |= PROFILE_CHROME;");
        pw.println("    profileInstall = 1;");
        pw.println("    fProgram = getFolder(\"Profile\");");
        pw.println("    logComment(\"try installing to the user profile: \" + fProgram);");
        pw.println("    err = addDirectory(\"\", \"chrome\", fProgram, \"chrome\");");
        pw.println("    logComment(\"addDirectory() for profile chrome returned: \" + ErrWithCode(err));");
        pw.println("  }");
        pw.println("");
        pw.println("  setPackageFolder(fProgram);");
        pw.println("");
        pw.println("  // check return value: 0 is SUCCESS, 999 is REBOOT_NEEDED");
        pw.println("  if ((err == 0) || (err == 999)) {");
        pw.println("    var tellReboot = 0;");
        pw.println("    if (err == 999) {");
        pw.println("        tellReboot = 1;");
        pw.println("        resetError();");
        pw.println("    }");
        pw.println("");
        // register chrome
        pw.println("    // register chrome");
        pw.println("    var cf = getFolder(fProgram, \"chrome/\" + chromeName);");
        pw.println("    var pf = getFolder(fProgram, \"chrome/\" + platformName);");
        pw.println("    var rf = getFolder(fProgram, \"chrome/\" + regionFile);");
        pw.println("");
        iterateComponents(xpiData.getNeutrals().iterator(), ProductChild.TYPE_NEUTRAL, pw);
        iterateComponents(xpiData.getWindows().iterator(), ProductChild.TYPE_WINDOWS, pw);
        iterateComponents(xpiData.getMacs().iterator(), ProductChild.TYPE_MAC, pw);
        iterateComponents(xpiData.getUnixes().iterator(), ProductChild.TYPE_UNIX, pw);
        iterateComponents(xpiData.getRegions().iterator(), ProductChild.TYPE_REGION, pw);
        pw.println("");
        // after components
        pw.println("    err = performInstall();");
        pw.println("    logComment(\"performInstall() returned: \" + ErrWithCode(err));");
        pw.println("    if (err == 999) {");
        pw.println("      tellReboot = 1;");
        pw.println("      resetError();");
        pw.println("      err = 0;");
        pw.println("    }");
        pw.println("    if (err == 0) {");
        pw.println("      if (profileInstall == 1) {");
        pw.println("        if (messages) { alertstring = messages.install_successprofile; }");
        pw.println("      }");
        pw.println("      else {");
        pw.println("        if (messages) { alertstring = messages.install_successglobal; }");
        pw.println("      }");
        pw.println("      if (tellReboot == 1) {");
        pw.println("        if (messages) { alertstring = alertstring + \"\\n\\n\" + messages.tell_reboot; }");
        pw.println("        logComment(\"REBOOT_NEEDED (999): warning user that he needs to reboot his system.\");");
        pw.println("        cancelInstall(REBOOT_NEEDED);");
        pw.println("      }");
        pw.println("      else {");
        pw.println("        if (tellRestart == 1) {");
        pw.println("          if (messages) { alertstring = alertstring + \"\\n\\n\" + messages.tell_restart; }");
        pw.println("          logComment(\"we were using DELAYED_CHROME: warning user that he needs to restart Mozilla.\");");
        pw.println("        }");
        pw.println("      }");
        pw.println("      if (messages) { alertstring = alertstring + \"\\n\\n\" + messages.howto_switch; }");
        pw.println("    }");
        pw.println("    else {");
        pw.println("      if (messages) { alertstring = messages.install_failed + \" \" + ErrWithCode(err) + \".\"; }");
        pw.println("      cancelInstall(err);");
        pw.println("    }");
        pw.println("  }");
        pw.println("  else {");
        pw.println("    cancelInstall(err);");
        pw.println("    logComment(\"cancelInstall due to error: \" + ErrWithCode(err));");
        pw.println("    if (messages) { alertstring = messages.install_cancel + \" \" + ErrWithCode(err) + \".\"; }");
        pw.println("    if ((err == -202) || (err == -215)) {");
        pw.println("      // -202 is ACCESS_DENIED, -215 is READ_ONLY");
        pw.println("      if (messages) { alertstring = alertstring + \"\\n\\n\" + messages.need_write_perm; }");
        pw.println("      if (err == -202) {");
        pw.println("        logComment(\"ACCESS_DENIED (-202): warning user that he probably has no write access to chrome.\");");
        pw.println("      }");
        pw.println("      else {");
        pw.println("        logComment(\"READ_ONLY (-215): warning user that he probably has no write access to chrome.\");");
        pw.println("      }");
        pw.println("    }");
        pw.println("  }");
        pw.println("  if (messages) { alert(alertstring); }");
        pw.println("}");
        pw.println("else {");
        pw.println("  if (err == 0)");
        pw.println("    cancelInstall(INSUFFICIENT_DISK_SPACE);");
        pw.println("  else");
        pw.println("    cancelInstall(err);");
        pw.println("}");

        pw.close();
        FileUtils.saveFile(jos, baos.toByteArray());
    }

    private void iterateComponents(Iterator prodChildIterator, int type, PrintWriter pw) {
        ProductChild prodChild;
        Component currentComponent;
        Iterator componentIterator;
        boolean first = true;
        while (prodChildIterator.hasNext()) {
            prodChild = (ProductChild) prodChildIterator.next();

            componentIterator = prodChild.iterator();

            while (componentIterator.hasNext()) {
                currentComponent = (Component) componentIterator.next();
                switch (type) {
                    case ProductChild.TYPE_NEUTRAL:
                        pw.println("registerChrome(chromeType, cf , localeName + \"" +
                                currentComponent.getName() + "/\");");
                        break;
                    case ProductChild.TYPE_WINDOWS:
                        if (first) {
                            pw.println("if (platformNode == \"win\") {");
                            first = false;
                        }
                        pw.println("registerChrome(chromeType, pf , localeName + \"" +
                                currentComponent.getName() + "/\");");
                        break;
                    case ProductChild.TYPE_UNIX:
                        if (first) {
                            pw.println("if (platformNode == \"unix\") {");
                            first = false;
                        }
                        pw.println("registerChrome(chromeType, pf , localeName + \"" +
                                currentComponent.getName() + "/\");");
                        break;
                    case ProductChild.TYPE_MAC:
                        if (first) {
                            pw.println("if (platformNode == \"mac\") {");
                            first = false;
                        }
                        pw.println("registerChrome(chromeType, pf , localeName + \"" +
                                currentComponent.getName() + "/\");");
                        break;
                    case ProductChild.TYPE_REGION:
                        pw.println("registerChrome(chromeType, rf , regionName + \"" +
                                currentComponent.getName() + "/\");");
                        break;
                }
            }
            if (!first) {
                pw.println("}");
            }
        }
    }

    @Override
    public String getTitle() {
         return "Write XPI";
    }
}
