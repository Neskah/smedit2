/*
 * 2014 SMEdit2 development team
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
package jo.util;

/**
 * Handles the lookup and download of the client jar files when updated
 *
 * @Auther Robert Barefoot for SMEdit2 - version 1.0
 */
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jo.sm.ui.UpdateGUI;

public class Update {

    private static int update = -1;

    /**
     * @return the update
     */
    public static int getUpdate() {
        return update;
    }

    /**
     * @param aUpdate the update to set
     */
    public static void setUpdate(int aUpdate) {
        update = aUpdate;
    }

    /**
     * @return the download
     */
    public static UpdateGUI getDownload() {
        return download;
    }

    /**
     * @param aDownload the download to set
     */
    public static void setDownload(UpdateGUI aDownload) {
        download = aDownload;
    }
    private final Window parent;
    private static UpdateGUI download = null;
    private static final byte[] buffer = new byte[1024];

    private static int getLatestVersion() {
        return -1;
    }

    public Update(final Window parent) {
        this.parent = parent;
    }

    public void checkUpdate(final boolean checkup) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        if (GlobalConfiguration.getVersion() >= getLatestVersion()) {
            return;
        }
        if (getLatestVersion() > GlobalConfiguration.getVersion()) {
            setUpdate(JOptionPane.showConfirmDialog(parent,
                    "A newer version of the application is available.\n\n"
                            + "Do you wish to update?\n\n"
                            + "Choosing not to update may result\n"
                            + "in problems running the client...",
                    "Update Found", JOptionPane.YES_NO_OPTION));
            if (getUpdate() != 0) {
                return;
            }
            try {
                if (getUpdate() == 0) {
                    updateApp();
                }
            } catch (final Exception e) {
            }
        }
    }

    public void download(final String address, final String localFileName) {

    }

    public void updateApp() {

    }
}
