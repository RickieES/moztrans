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
 */

package org.mozillatranslator.gui.model;

/**
 * Sort Helper to improve sort performance
 *
 * @author Bernat Arlandis i Mañó
 */
public class ComparableIndexedObject implements Comparable<ComparableIndexedObject> {
    private int index;
    private Comparable object;

    public ComparableIndexedObject(int index, Comparable object) {
        this.index = index;
        this.object = object;
    }

    public int getIndex() {
        return index;
    }

    public Comparable getValue() {
        return object;
    }

    @Override
    public int compareTo(ComparableIndexedObject o) {
        Comparable object2 = o.getValue();
        if (object==null || object2==null) {
            if (object==object2) {
                return 0;
            }
            return (object==null)? -1 : 1;
        }
        return object.compareTo(object2);
    }
}
