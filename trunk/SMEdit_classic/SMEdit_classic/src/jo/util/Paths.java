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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Robert Barefoot
 */
public class Paths {

    public static final String ROOT = ".";
    public static final String RESOURCES = ROOT + File.separator + "resources";
    public static final String SVERSION = RESOURCES + File.separator + "start_version.dat";
    private static Map<String, File> downloadCache;
    private static final Logger log = Logger.getLogger(Paths.class.getName());
    /* file locations */
    private static Properties mProps;
    private static File mStarMadeDir;

    

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
            downloadCache.put(URLs.DOWNLOAD, new File(getHomeDirectory(), "jo_sm_classic.jar"));
            downloadCache.put(URLs.DOWNLOADPLUG, new File(getPluginsDirectory(), "JoFileMods.jar"));

            /* ICONS */
            downloadCache.put(URLs.ICON_FILE_ACCOUNT, new File(getIconDirectory(), "account.png"));
            downloadCache.put(URLs.ICON_FILE_HOME, new File(getIconDirectory(), "home.png"));
            downloadCache.put(URLs.ICON_FILE_PLUGINS, new File(getIconDirectory(), "plugins.png"));
            downloadCache.put(URLs.ICON_FILE_UNDO, new File(getIconDirectory(), "undo.png"));
            downloadCache.put(URLs.ICON_FILE_REDO, new File(getIconDirectory(), "redo.png"));
            downloadCache.put(URLs.ICON_FILE_STOP, new File(getIconDirectory(), "stop.png"));
            downloadCache.put(URLs.ICON_FILE_FILL, new File(getIconDirectory(), "fill.png"));
            downloadCache.put(URLs.ICON_FILE_SHOT, new File(getIconDirectory(), "shot.png"));
            downloadCache.put(URLs.ICON_FILE_FILE, new File(getIconDirectory(), "open.png"));
            downloadCache.put(URLs.ICON_FILE_BPFILE, new File(getIconDirectory(), "open_print.png"));
            downloadCache.put(URLs.ICON_FILE_SAVE, new File(getIconDirectory(), "save.png"));
            downloadCache.put(URLs.ICON_FILE_SAVEAS, new File(getIconDirectory(), "save_as.png"));
            
            downloadCache.put(URLs.ICON_FILE_WIKI, new File(getIconDirectory(), "wiki.png"));
            downloadCache.put(URLs.ICON_FILE_FACE, new File(getIconDirectory(), "face.png"));
            downloadCache.put(URLs.ICON_FILE_TWIT, new File(getIconDirectory(), "twit.png"));
            downloadCache.put(URLs.ICON_FILE_PROJECT, new File(getIconDirectory(), "web.png"));
            
        }
        return Collections.unmodifiableMap(downloadCache);
    }
    /* folder directories */

    public static String getOptsFile() {
        final String path;
        if (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS) {
            path = System.getProperty("user.home") + File.separator + ".smeopts";
        } else {
            path = Paths.getUnixHome() + File.separator + ".smeopts";
        }
        return path;
    }

    public static String getHomeDirectory() {
        Properties props = getProps();
        String home = props.getProperty("starmade.home", "");
        return home + File.separator + "third-party" + File.separator + GlobalConfiguration.NAME;

    }
    
    public static String getCacheDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Cache";
    }
    
    public static String getLogsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Logs";
    }
    
    public static String getPluginsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Plugins";
    }
    
    public static String getResourceDirectory() {
        return Paths.getHomeDirectory() + File.separator + "resources";
    }
    
    public static String getScreenshotsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Screenshots";
    }
    
    public static String getSettingsDirectory() {
        return Paths.getHomeDirectory() + File.separator + "Settings";
    }

    public static String getIconDirectory() {
        return Paths.getResourceDirectory() + File.separator + "images";
    }

    public static String getShapeLibraryDirectory() {
        return Paths.getPluginsDirectory() + File.separator + "shapeLibrary";
    }

    public static String getPathCache() {
        return Paths.getSettingsDirectory() + File.separator + "start_path.txt";
    }

    public static String getUnixHome() {
        final String home = System.getProperty("user.home");
        return home == null ? "~" : home;
    }

    public static Properties getProperties() {
        return mProps;
    }

    public static Properties getProps() {
        if (!validateCurrentDirectory()) {
            Properties p = new Properties();
            File home = new File(System.getProperty("user.home"));
            File props = new File(home, ".josm");
            if (props.exists()) {
                try {
                    try (FileInputStream fis = new FileInputStream(props)) {
                        mProps.load(fis);
                    }
                } catch (IOException e) {

                }
            } else {
                mProps = new Properties();
            }
            saveProps();
        }
        return getProperties();
    }

    private static boolean isStarMadeDirectory(File d) {
        if (!d.exists()) {
            return false;
        }
        File smJar = new File(d, "StarMade.jar");
        if (!smJar.exists()) {
            return false;
        }
        File crashJar = new File(d, "CrashAndBugReport.jar");
        if (!crashJar.exists()) {
            return false;
        }
        return true;
    }

    public static void loadProps() {
        File home = new File(System.getProperty("user.home"));
        File props = new File(home, ".josm");
        if (props.exists()) {
            mProps = new Properties();
            try {
                try (FileInputStream fis = new FileInputStream(props)) {
                    mProps.load(fis);
                }
            } catch (IOException e) {

            }
        } else {
            mProps = new Properties();
        }
    }

    public static boolean validateCurrentDirectory() {
        loadProps();
        mStarMadeDir = new File(mProps.getProperty("starmade.home", ""));
        if (isStarMadeDirectory(mStarMadeDir)) {
            return true;
        }
        mStarMadeDir = new File(".");
        if (isStarMadeDirectory(mStarMadeDir)) {
            saveProps();
            return true;
        }
        System.out.println("Scanning current directory");
        mStarMadeDir = null;
        String home = System.getProperty("user.dir");
        lookForStarMadeDir(new File(home));
        if (mStarMadeDir != null) {
            saveProps();
            return true;
        }
        System.out.println("Scanning home directory");
        mStarMadeDir = null;
        home = System.getProperty("user.home");
        lookForStarMadeDir(new File(home));
        if (mStarMadeDir != null) {
            saveProps();
            return true;
        }
        for (;;) {
            home = JOptionPane.showInputDialog(null, "Enter in the home directory for StarMade", home);
            if (home == null) {
                return false;
            }
            mStarMadeDir = new File(home);
            if (isStarMadeDirectory(mStarMadeDir)) {
                break;
            }
        }
        saveProps();
        return true;
    }

    private static void saveProps() {
        if (mProps == null) {
            return;
        }
        if (mStarMadeDir != null) {
            mProps.put("starmade.home", mStarMadeDir.toString());
        }
        File home = new File(System.getProperty("user.home"));
        File props = new File(home, ".josm");
        try {
            try (FileWriter fos = new FileWriter(props)) {
                mProps.store(fos, "StarMade Utils defaults");
            }
        } catch (IOException e) {

        }
    }

    private static void lookForStarMadeDir(File root) {
        if (isStarMadeDirectory(root)) {
            mStarMadeDir = root;
            return;
        }
        File[] children = root.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                lookForStarMadeDir(child);
                if (mStarMadeDir != null) {
                    return;
                }
            }
        }
    }

    private Paths() {
        mProps = new Properties();
    }

}
