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

package org.mozillatranslator.io.common;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozillatranslator.datamodel.GenericFile;
import org.mozillatranslator.dataobjects.JarXpiDataObject;
import org.mozillatranslator.kernel.Kernel;
import org.mozillatranslator.kernel.Settings;

/** This is a utility class for file handling
 *
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class FileUtils {
    private static final Logger fLogger = Logger.getLogger(FileUtils.class.getPackage().getName());

    /** This file saves a file given by fileContent to the
     * output stream specified
     * @param os The output stream to write to
     * @param fileContent the content to be written
     * @throws IOException thrown if a underlying IOException is thrown */
    public static void saveFile(OutputStream os, byte[] fileContent) throws IOException {
        // FIXME: can we refactor the JarEnry stuff into this method
        try {
            os.write(fileContent);
            os.flush();
        } catch (IOException e) {
            fLogger.log(Level.SEVERE, "Could not compact file", e);
            throw new IOException("Could not compact file");
        }
    }

    /** This will save the given file content to the output stream,
     * but it will also prepend the file with a license statement based
     * On the genericFile passed
     * @param os The output stream to write to
     * @param fileContent The content to be written
     * @param mfile This is used to base the license file format
     * @throws IOException Thrown if a underlying IOException is thrown */
    public static void saveFileWithLicense(OutputStream os, byte[] fileContent,
            GenericFile mfile) throws IOException {
        String license = mfile.getLicenseFile();
        if (license != null) {
            File licenseFile = new File(license);
            if (licenseFile.exists()) {
                FileInputStream fis = new FileInputStream(license);
                saveFile(os, loadFile(fis));
            }
        }
        saveFile(os, fileContent);
    }

    /** This loads a file from the input stream into the byteArray
     * @param is The input stream to read from
     * @throws IOException Is thrown if a underlying IOException is thrown
     * @return A byte[] with the file's content */
    public static byte[] loadFile(InputStream is) throws IOException {
        boolean theEnd = false;
        int trans;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            while (!theEnd) {
                trans = is.read();
                if (trans == -1) {
                    theEnd = true;
                } else {
                    bos.write(trans);
                }
            }
            bos.close();
        } catch (IOException e) {
            Kernel.appLog.log(Level.SEVERE, "could not extract file");
            throw new IOException("Could not extract file");
        }
        return bos.toByteArray();
    }

    /** This will save the given content to a file with the
     * given name
     * @param realName The filename to save the content to
     * @param content The content to save
     * @throws IOException Is thrown if a underlying IOException is thrown */
    public static void save(String realName, byte[] content) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(realName);

            fos.write(content);
            fos.close();
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "cannot save file", e);
            throw new IOException("Could not save file " + realName);
        }
    }

    /** This will save the given content to a file with the
     * given name
     * @param realName The filename to save the content to
     * @param content The content to save
     * @param mfile A datamodel file  object representing what to save
     * @throws IOException Is thrown if a underlying IOException is thrown */
    public static void saveWithLicence(String realName, byte[] content,
            GenericFile mfile) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(realName);

            saveFileWithLicense(fos, content, mfile);
            //fos.write(content);
            fos.close();
        } catch (Exception e) {
            Kernel.appLog.log(Level.SEVERE, "cannot save file", e);
            throw new IOException("Could not save file " + realName);
        }
    }
    
    /**
     * Uses external ZIP utility to expand and compress the ZIP/JAR created
     * with the internal Java classes, which does not take into account Unix
     * permissions
     * @param dao a JarXpiDataObject representing the ZIP file to be expanded
     * and compressed
     */
    public static void expandCompress(JarXpiDataObject dao) {
        String randomDir = FileUtils.getRandomDir();
        String workDir = dao.getRealFile().getParentFile().getPath() + "/" +
                         randomDir;
        String unzipArgs = " -d " + workDir + " " +
                           dao.getRealFile().getPath() + " *";
        String zipArgs = " -r -9 " + dao.getRealFile().getPath() + " .";
        Process p;
        File tempDir;

        try {
            p = Runtime.getRuntime().exec(Kernel.settings.getString(
                    Settings.EXTERNAL_UNZIP_PATH) + unzipArgs);
            dumpCmdOutput(p.getInputStream(), Level.FINE);
            dumpCmdOutput(p.getErrorStream(), Level.FINE);
            
            p.waitFor();
            dao.getRealFile().delete();
            tempDir = new File(workDir);
            p = Runtime.getRuntime().exec(Kernel.settings.getString(
                    Settings.EXTERNAL_ZIP_PATH) + zipArgs, null, new File(workDir));
            dumpCmdOutput(p.getInputStream(), Level.FINE);
            dumpCmdOutput(p.getErrorStream(), Level.FINE);

            p.waitFor();
            FileUtils.removeDir(tempDir);
        } catch (InterruptedException ex) {
            fLogger.log(Level.WARNING,
                    "Something went wrong with the ZIP workaround.", ex);
        } catch (IOException ex) {
            fLogger.log(Level.WARNING,
                    "Something went wrong with the ZIP workaround.", ex);
        }
    }
    
    private static void dumpCmdOutput(InputStream is, Level logLevel) throws
            IOException {
        InputStreamReader isr;
        BufferedReader br;
        String line;
        
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        
        while ((line = br.readLine()) != null) {
            fLogger.log(logLevel, line);
        }
    }

    /** This will generate a random eight characters string and check if a
     * directory already exists with that name until it can be used as a
     * name for a random, temporary directory
     *
     * @return String   a string that can be safely used to create a temp dir
     */
    public static String getRandomDir() {
        StringBuilder result = new StringBuilder(8);
        Random r = new Random();
        boolean fileExists;
        File currentDir;

        do {
            for(int i = 0; i < 4; i++) {
                result.insert(i, Byte.toString((byte) (10 + r.nextInt(90))));
            }

            currentDir = new File("./" + result.toString());
            fileExists = currentDir.exists();
        } while (fileExists);

        return result.toString();
    }

    /** This static method will recursively delete the directory passed as
     * argument.
     *
     * @param  dir a File object representing the directory to be deleted
     * @return true if everything has gone OK, false otherwise
     */
    public static boolean removeDir(File dir) {
        return removeDir(dir, 0);
    }

    /** This static method will recursively delete the directory passed as
     * argument.
     *
     * @param   dir a File object representing the directory to be deleted
     * @returns true if everything has gone OK, false otherwise
     */
    private static boolean removeDir(File dir, int level) {
        boolean result = true;
        int i = 0;

        if ((level != 0) || (dir.isDirectory())) {
            // We get the list of files and dirs inside the current directory
            File[] files = dir.listFiles();
            while (result && (i < files.length)) {
                if (files[i].isDirectory()) {
                    // We try to delete the subdirectory
                    result = removeDir(files[i], level + 1);
                } else {
                    // We try to delete the file
                    result = files[i].delete();
                }
                i++;
            }
            result = result && dir.delete();
        }
        return result;
    }

    /**
     * Builds and returns a product repository dir, prepending the user
     * specific repositories base dir (if it is set) to the path passed
     * as a parameter
     * @param path a path, usually an import original or import/export translation path from a SCM-based product
     * @return the above path with the repo base dir and a separator, if applicable
     */
    public static String getFullRepoDir(String path) {
        StringBuilder fullPath = new StringBuilder();
        if (Kernel.settings.getString(Settings.REPOSITORIES_BASE).trim().length() > 0) {
            fullPath.append(Kernel.settings.getString(Settings.REPOSITORIES_BASE).trim());
            if (path.substring(path.length() - 1, path.length()).equals(File.separator)) {
                fullPath.append(File.separator);
            }
        }
        fullPath.append(path);
        return fullPath.toString();
    }

}
