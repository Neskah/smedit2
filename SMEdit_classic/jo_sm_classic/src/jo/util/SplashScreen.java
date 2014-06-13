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
 *
 */
package jo.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.BorderFactory.createTitledBorder;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import jo.log.LabelLogHandler;
import jo.log.LogOutputStream;
import jo.log.SystemConsoleHandler;
import jo.util.io.HttpClient;

/**
 * This is the main splash or load screen for the app. It handles the start up
 * of the client app logic in the config file and other areas
 *
 * @authoe SMEdit2 development team - version 1.0
 */
public class SplashScreen extends JDialog {

    private final static Logger log = Logger.getLogger(SplashScreen.class.getName());
    private static final long serialVersionUID = 5520543482560560389L;
    private static SplashScreen instance = null;

    private static void bootstrap() {
        Logger.getLogger("").addHandler(new SystemConsoleHandler());
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            private final Logger log = Logger.getLogger("EXCEPTION");

            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                final String ex = "Exception", msg = t.getName() + ": ";
                if (GlobalConfiguration.isRUNNING_FROM_JAR()) {
                    Logger.getLogger(ex).logp(Level.SEVERE, "EXCEPTION", "", "Unhandled exception in thread " + t.getName() + ": ", e);
                    //Logger.getLogger(ex).log(Level.SEVERE, "{0}{1}", new Object[]{msg, e.toString()});
                } else {
                    log.logp(Level.SEVERE, ex, "", msg, e);
                }
            }
        });
        if (!GlobalConfiguration.isRUNNING_FROM_JAR()) {
            System.setErr(new PrintStream(new LogOutputStream(Logger.getLogger("STDERR"), Level.SEVERE), true));
        }
    }

    public static void close() {
        if (instance != null) {
            instance.dispose();
        }
    }

    public final boolean error;
    private final String[] args;

    public SplashScreen(final String[] args) {
        instance = this;
        this.args = args;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(1);
            }
        });

        setTitle(GlobalConfiguration.NAME);
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        final ImageIcon icon = new ImageIcon();
        icon.setImage(GlobalConfiguration.getImage(Resources.SPLASH));
        final JLabel label1 = new JLabel();
        label1.setIcon(icon);
        final LabelLogHandler handler = new LabelLogHandler();
        Logger.getLogger("").addHandler(handler);
        handler.label.setBorder(createTitledBorder(" Startup Events "));
        final Font font = handler.label.getFont();
        handler.label.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        handler.label.setPreferredSize(new Dimension(400, 30 + 12));
        final JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setOpaque(true);
        progress.setSize(380, 15);
        progress.setLocation(10, 255);
        label1.add(progress);
        add(label1, BorderLayout.NORTH);
        //add(progress, BorderLayout.CENTER);
        add(handler.label, BorderLayout.SOUTH);
        pack();
        try {
            log.info("Loading");
            setLocationRelativeTo(getOwner());
            setResizable(false);
            setVisible(true);
            setAlwaysOnTop(true);
            Thread.sleep(500);
        } catch (InterruptedException | SecurityException exc) {
        }
        String err = null;

        try {

            log.info("Starting Bootstrap");
            bootstrap();
            Thread.sleep(500);
        } catch (InterruptedException exc) {
        }
        /*
        try {
            log.info("Creating Directories");
            GlobalConfiguration.createDirectories();
            Thread.sleep(500);
        } catch (InterruptedException exc) {
        }
        try {
            log.info("Downloading Resources");
            for (final Entry<String, File> item
                    : Paths.getDownloadCaches().entrySet()) {
                try {
                    HttpClient.download(new URL(item.getKey()), item.getValue());
                } catch (final IOException ignored) {
                }
            }
            Thread.sleep(500);
        } catch (InterruptedException exc) {
        }
                */
        if (err == null) {
            this.error = false;
            try {
                log.info("Loading Application");
                GlobalConfiguration.registerLogging();
                Logger.getLogger("").removeHandler(handler);
                Thread.sleep(500);
            } catch (InterruptedException | SecurityException exc) {
            }
        } else {
            this.error = true;
            progress.setIndeterminate(false);
            log.severe(err);
        }
    }


}

