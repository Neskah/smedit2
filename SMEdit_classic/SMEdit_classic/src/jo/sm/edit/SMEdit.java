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
 *
 */
package jo.sm.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JFrame;
import jo.util.GlobalConfiguration;
import jo.util.OptionScreen;
import jo.util.Paths;
import jo.util.Update;
import jo.util.io.HttpClient;

/**
 * @Auther Robert Barefoot for SMEdit Classic and SMEdit2 - version 1.0
 */
public class SMEdit extends JFrame {

    private static File mOptionDir;
    private static SMEdit app;
    private static Properties mProps;
    private static final long serialVersionUID = 1L;

    private static final Update updater = new Update(app);
    private static final Logger log = Logger.getLogger(SMEdit.class.getName());

    public static void main(final String[] args) {
        GlobalConfiguration.createDirectories();
            for (final Map.Entry<String, File> item : Paths.getDownloadCaches().entrySet()) {
                try {
                    HttpClient.download(new URL(item.getKey()), item.getValue());
                } catch (final IOException e) {
                }
            }
        if (!Paths.validateCurrentDirectory()) {
            return;
        }

        updater.checkUpdate(true);
        if (updater.update == -1) {
           OptionScreen opts = new OptionScreen(args); 
        }
        
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

    private String osbit;
    private final String flags;

    public SMEdit(final String[] args) {
        loadProps();
        mOptionDir = new File(Paths.getHomeDirectory());
        File jo_smJar = new File(mOptionDir, "jo_sm_classic.jar");
        final String os = System.getProperty("os.name").toLowerCase();

        if (null != mProps.getProperty("bit", "")) {
            switch (mProps.getProperty("bit", "")) {
                case "32 bit":
                    //osbit = "-d32";
                    osbit = "";
                    break;
                case "64 bit":
                    osbit = "-d64";
                    break;
            }
        } else {
            osbit = "";
        }
        if (null != mProps.getProperty("memory", "")) {
            flags = "-Xmx" + mProps.getProperty("memory", "") + "g";
        } else {
            flags = "-Xmx2g";
        }
        try {
            if (os.contains("windows")) {
                Runtime.getRuntime().exec(
                        "javaw " + osbit + " " + flags + " -jar " + jo_smJar);
            } else {
                Runtime.getRuntime().exec(
                        new String[]{
                            "/bin/sh",
                            "-c",
                            "javaw " + osbit + " " + flags + " -jar " + jo_smJar});
            }
        } catch (IOException e) {
        }
        System.exit(0);

    }

}
