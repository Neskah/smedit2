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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
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
    private final String[] mArgs;
    
    public SMEdit(final String[] args) {
        mArgs = args;
        mOptionDir = new File(Paths.getHomeDirectory());
        File jo_smJar = new File(mOptionDir, "jo_sm_classic.jar");
        
        try {
            URL josmURL = jo_smJar.toURI().toURL();
            URLClassLoader smLoader = new URLClassLoader(new URL[]{josmURL}, SMEdit.class.getClassLoader());
            Class<?> rf = smLoader.loadClass("Boot");
            Method main = rf.getMethod("main", String[].class);
            main.invoke(null, (Object) mArgs);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException | MalformedURLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
