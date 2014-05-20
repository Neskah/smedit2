/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jo.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Wayne
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
        }
        return Collections.unmodifiableMap(downloadCache);
    }
    /* folder directories */

    public static String getHomeDirectory() {
        final String env = System.getenv(GlobalConfiguration.NAME.toUpperCase() + "_HOME");
        if ((env == null) || env.isEmpty()) {
            return (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS ? FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() : Paths.getUnixHome()) + File.separator + GlobalConfiguration.NAME;
        } else {
            return env;
        }
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
