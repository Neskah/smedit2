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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.filechooser.FileSystemView;
import jo.sm.logic.StarMadeLogic;

/**
 *
 * @author Robert Barefoot
 */
public class Paths {


    public static final String ROOT = "." + File.separator + "resources";
    public static final String VERSION = ROOT + File.separator + "version.dat";
    private static Map<String, File> downloadCache;
    /* file locations */

    public static String getCacheDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Cache";
    }

    public static String getCollectDirectory() {
        final File dir = new File(Paths.getPluginsDirectory(), ".jar");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String path = dir.getAbsolutePath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (final UnsupportedEncodingException ignored) {
        }
        return path;
    }

    public static Map<String, File> getDownloadCaches() {
        if (downloadCache == null) {
            downloadCache = new HashMap<>(8);
            /* FILES */
            /* ICONS */
            downloadCache.put(URLs.ICON_FILE_ACCOUNT, new File(getIconDirectory(), "account.png"));
            downloadCache.put(URLs.ICON_FILE_HOME, new File(getIconDirectory(), "home.png"));
            downloadCache.put(URLs.ICON_FILE_PLUGINS, new File(getIconDirectory(), "plugins.png"));
            downloadCache.put(URLs.ICON_FILE_UNDO, new File(getIconDirectory(), "undo.png"));
            downloadCache.put(URLs.ICON_FILE_REDO, new File(getIconDirectory(), "redo.png"));
        }
        return Collections.unmodifiableMap(downloadCache);
    }
    /* folder directories */

    public static String getHomeDirectory() {
        Properties props = StarMadeLogic.getProps();
        String home = props.getProperty("starmade.home", "");
        return home + File.separator + "third-party" + File.separator + GlobalConfiguration.NAME;

    }

    public static String getIconDirectory() {
        return Paths.getHomeDirectory() + File.separator + "resources" + File.separator + "images";
    }

    public static String getLogsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Logs";
    }

    public static String getMenuCache() {
        return Paths.getSettingsDirectory() + File.separator + "Menu.txt";
    }

    public static String getPathCache() {
        return Paths.getSettingsDirectory() + File.separator + "path.txt";
    }

    public static String getScreenshotsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Screenshots";
    }

    public static String getPluginsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Plugins";
    }

    public static String getSettingsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Settings";
    }

    public static String getUnixHome() {
        final String home = System.getProperty("user.home");
        return home == null ? "~" : home;
    }

    public static String getVersionCache() {
        return Paths.getCacheDirectory() + File.separator + "info.dat";
    }
    
}

