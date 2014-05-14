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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import jo.log.LogFormatter;
import jo.log.SystemConsoleHandler;
import jo.log.TextAreaLogHandler;

/**
 * Handles the configuration of the main app layout and logic start up
 *
 * @author SMEdit development team - version 1.0
 */
@SuppressWarnings({"CallToPrintStackTrace", "null"})
public class GlobalConfiguration {

    public enum OperatingSystem {

        MAC, WINDOWS, LINUX, UNKNOWN
    }

    public static class Paths {

        public static class Resources {

            public static final String ROOT = "resources";
            public static final String ROOT_IMG = "/" + ROOT + "/images";
            public static final String SPLASH = ROOT_IMG + "/splash.png";
            public static final String ICON = ROOT_IMG + "/icon.png";
            public static final String HOME = ROOT_IMG + "/home.png";
            public static final String PLAY = ROOT_IMG + "/play.png";
            public static final String CUT = ROOT_IMG + "/cut.png";
            public static final String COPY = ROOT_IMG + "/copy.png";
            public static final String PASTE = ROOT_IMG + "/paste.png";
            public static final String SAVE = ROOT_IMG + "/save.png";
            public static final String UPARROW = ROOT_IMG + "/arrow_up.png";
            public static final String DOWNLOAD = ROOT_IMG + "/download.png";
            public static final String DOWNARROW = ROOT_IMG + "/arrow_down.png";
            public static final String VERSION = ROOT + "/version.dat";
        }

        public static class URLs {
            

            public static final String URL = "http://";
            public static final String CODE = ".googlecode.com/";
            public static final String FILE = "smedit2";
            public static final String HOME = "smedit2";
            public static final String LOC = "svn/trunk/resources/";
            public static final String SVNDAT = URL + FILE + CODE + LOC;
            public static final String SVNICON = URL + FILE + CODE + LOC + "resources/images";
            public static final String OPENSVN = URL + HOME + CODE;
            /* url */
            public static final String PROJECT = URL + "code.google.com/p/smedit2/";
            public static final String SITE = URL + "lazygamerz.org";
            /* files */

            /* icons */
            public static final String ICON_FILE_ACCOUNT = SVNICON + "account.png";

        }

        public static final String ROOT = "." + File.separator + "resources";
        public static final String VERSION = ROOT + File.separator + "version.dat";
        
        
        private static Map<String, File> downloadCache;
        /* file locations */

        public static String getCacheDirectory() {
            return Paths.getHomeDirectory() + File.separator + "Cache";
        }

        public static String getCollectDirectory() {
            final File dir = new File(
                    GlobalConfiguration.Paths.getPluginsDirectory(),
                    ".jar");
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

            }
            return downloadCache;
        }
        /* folder directories */

        public static String getHomeDirectory() {
            final String env = System.getenv(GlobalConfiguration.NAME
                    .toUpperCase() + "_HOME");
            if ((env == null) || env.isEmpty()) {
                return (GlobalConfiguration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS ? FileSystemView
                        .getFileSystemView().getDefaultDirectory()
                        .getAbsolutePath()
                        : Paths.getUnixHome())
                        + File.separator + GlobalConfiguration.NAME;
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
    public static final String NAME = "SMEdit";
    public static final String NAME_LOWERCASE = NAME.toLowerCase();
    public static final String SITE_NAME = "Lazygamerz";
    private static final OperatingSystem CURRENT_OS;
    public static boolean RUNNING_FROM_JAR = false;

    static {
        final URL resource = GlobalConfiguration.class.getClassLoader()
                .getResource(Paths.Resources.VERSION);
        if (resource != null) {
            GlobalConfiguration.RUNNING_FROM_JAR = true;

        }
        final String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            CURRENT_OS = OperatingSystem.MAC;
        } else if (os.contains("Windows")) {
            CURRENT_OS = OperatingSystem.WINDOWS;
        } else if (os.contains("Linux")) {
            CURRENT_OS = OperatingSystem.LINUX;
        } else {
            CURRENT_OS = OperatingSystem.UNKNOWN;
        }

        if (GlobalConfiguration.RUNNING_FROM_JAR) {
            String path;
            path = resource.toString();
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (final UnsupportedEncodingException ignored) {
            }
            final String prefix = "jar:file:/";
            if (path.indexOf(prefix) == 0) {
                path = path.substring(prefix.length());
                path = path.substring(0, path.indexOf('!'));
                if (File.separatorChar != '/') {
                    path = path.replace('/', File.separatorChar);
                }
                try {
                    final File pathfile = new File(Paths.getPathCache());
                    if (pathfile.exists()) {
                        pathfile.delete();
                    }
                    pathfile.createNewFile();
                    try (Writer out = new BufferedWriter(new FileWriter(
                            Paths.getPathCache()))) {
                        out.write(path);
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDirectories() {
        final ArrayList<String> dirs = new ArrayList<>();
        dirs.add(Paths.getHomeDirectory());
        dirs.add(Paths.getIconDirectory());
        dirs.add(Paths.getLogsDirectory());
        dirs.add(Paths.getScreenshotsDirectory());
        dirs.add(Paths.getPluginsDirectory());
        dirs.add(Paths.getCacheDirectory());
        dirs.add(Paths.getSettingsDirectory());
        if (GlobalConfiguration.RUNNING_FROM_JAR) {

        }

        for (final String name : dirs) {
            final File dir = new File(name);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    public static OperatingSystem getCurrentOperatingSystem() {
        return GlobalConfiguration.CURRENT_OS;
    }

    public static Image getImage(final String resource) {
        try {
            return Toolkit.getDefaultToolkit().getImage(
                    getResourceURL(resource));
        } catch (final MalformedURLException e) {
        }
        return null;
    }

    public static BufferedImage getImageFile(final File resource) {
        try {
            return ImageIO.read(resource);
        } catch (final IOException e) {
        }
        return null;
    }

    public static URL getResourceURL(final String path)
            throws MalformedURLException {
        return RUNNING_FROM_JAR ? GlobalConfiguration.class.getResource(path)
                : new File(path).toURI().toURL();
    }

    public static int getVersion() {
		try {
			final InputStream is = RUNNING_FROM_JAR ? GlobalConfiguration.class
					.getClassLoader().getResourceAsStream(
							Paths.Resources.VERSION) : new FileInputStream(
									Paths.VERSION);

							int off = 0;
							final byte[] b = new byte[2];
							while ((off += is.read(b, off, 2 - off)) != 2) {
							}

							return ((0xFF & b[0]) << 8) + (0xFF & b[1]);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

    public static void registerLogging() {
        final Properties logging = new Properties();
        final String logFormatter = LogFormatter.class.getCanonicalName();
        final String fileHandler = FileHandler.class.getCanonicalName();
        logging.setProperty("handlers",
                TextAreaLogHandler.class.getCanonicalName() + "," + fileHandler);
        logging.setProperty(".level", "CONFIG");
        logging.setProperty(SystemConsoleHandler.class.getCanonicalName()
                + ".formatter", logFormatter);
        logging.setProperty(fileHandler + ".formatter", logFormatter);
        logging.setProperty(TextAreaLogHandler.class.getCanonicalName()
                + ".formatter", logFormatter);
        logging.setProperty(fileHandler + ".pattern", Paths.getLogsDirectory()
                + File.separator + "%u.%g.log");
        logging.setProperty(fileHandler + ".count", "10");
        final ByteArrayOutputStream logout = new ByteArrayOutputStream();
        try {
            logging.store(logout, "");
            LogManager.getLogManager().readConfiguration(
                    new ByteArrayInputStream(logout.toByteArray()));
        } catch (final IOException | SecurityException ignored) {
        }
    }
}
