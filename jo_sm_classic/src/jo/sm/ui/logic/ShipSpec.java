/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */
package jo.sm.ui.logic;

import java.io.File;

import jo.sm.data.Entity;

public class ShipSpec {

    public static final int BLUEPRINT = 0;
    public static final int DEFAULT_BLUEPRINT = 1;
    public static final int ENTITY = 2;
    public static final int FILE = 3;

    private int mClassification;
    private int mType;
    private String mName;
    private Entity mEntity;
    private File mFile;

    @Override
    public String toString() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Entity getEntity() {
        return mEntity;
    }

    public void setEntity(Entity entity) {
        mEntity = entity;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public int getClassification() {
        return mClassification;
    }

    public void setClassification(int classification) {
        mClassification = classification;
    }
}
