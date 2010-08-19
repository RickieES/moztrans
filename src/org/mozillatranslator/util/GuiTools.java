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
package org.mozillatranslator.util;

import java.awt.*;

/** Minor tools for the gui part of this application
 * @author Henrik Lynggaard
 * @version 1.0
 */
public class GuiTools {

    /** The width of the screen */
    private static int scrWidth;
    /** The height of the screen */
    private static int scrHeight;

    static {
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        scrWidth = (int) scr.getWidth() / 2;
        scrHeight = (int) scr.getHeight() / 2;
    }

    /** Centers a frame on the screen
     * @param frame The frame to center
     */
    public static void placeFrameAtCenter(Component frame) {
        int x;
        int y;

        Dimension frm = frame.getSize();

        x = (int) (scrWidth - (frm.getWidth() / 2));
        y = (int) (scrHeight - (frm.getHeight() / 2));

        frame.setLocation(x, y);
    }
}
