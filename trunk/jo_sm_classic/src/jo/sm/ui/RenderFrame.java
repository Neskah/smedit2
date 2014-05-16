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
package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.act.edit.RedoAction;
import jo.sm.ui.act.edit.UndoAction;
import jo.sm.ui.act.file.OpenExistingAction;
import jo.sm.ui.act.file.OpenFileAction;
import jo.sm.ui.act.file.QuitAction;
import jo.sm.ui.act.file.SaveAction;
import jo.sm.ui.act.file.SaveAsBlueprintAction;
import jo.sm.ui.act.file.SaveAsFileAction;
import jo.sm.ui.act.view.AxisAction;
import jo.sm.ui.act.view.DontDrawAction;
import jo.sm.ui.act.view.PlainAction;
import jo.sm.ui.logic.MenuLogic;
import jo.sm.ui.logic.ShipSpec;
import jo.sm.ui.logic.ShipTreeLogic;
import jo.sm.ui.lwjgl.LWJGLRenderPanel;
import jo.util.GlobalConfiguration;
import jo.util.Paths;
import jo.util.Resources;
import jo.util.SplashScreen;

@SuppressWarnings("serial")
public class RenderFrame extends JFrame {

    private static String[] mArgs;
    private RenderPanel mClient;
    private TrayIcon trayIcon;

    public RenderFrame(String[] args) {
        super("SMEdit");
        mArgs = args;
        // instantiate
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        JMenu menuView = new JMenu("View");
        JMenu menuModify = new JMenu("Modify");
        if ((mArgs.length > 0) && (mArgs[0].equals("-opengl"))) {
            mClient = new LWJGLRenderPanel();
        } else {
            mClient = new AWTRenderPanel();
        }
        // layout
        setJMenuBar(menuBar);
        menuBar.add(menuFile);
        menuFile.add(new OpenExistingAction(this));
        menuFile.add(new OpenFileAction(this));
        menuFile.add(new JSeparator());
        menuFile.add(new SaveAction(this));
        JMenu saveAs = new JMenu("Save As");
        menuFile.add(saveAs);
        saveAs.add(new SaveAsBlueprintAction(this, false));
        saveAs.add(new SaveAsBlueprintAction(this, true));
        saveAs.add(new SaveAsFileAction(this));
        JSeparator menuFileStart = new JSeparator();
        menuFileStart.setName("pluginsStartHere");
        menuFile.add(menuFileStart);
        menuFile.add(new JSeparator());
        menuFile.add(new QuitAction(this));
        menuBar.add(menuEdit);
        menuEdit.add(new UndoAction(this));
        menuEdit.add(new RedoAction(this));
        menuEdit.add(new JSeparator());
        menuBar.add(menuView);
        menuView.add(new JCheckBoxMenuItem(new PlainAction(this)));
        menuView.add(new JCheckBoxMenuItem(new AxisAction(this)));
        menuView.add(new JCheckBoxMenuItem(new DontDrawAction(this)));
        JSeparator viewFileStart = new JSeparator();
        viewFileStart.setName("pluginsStartHere");
        menuView.add(viewFileStart);
        menuBar.add(menuModify);
        getContentPane().add(BorderLayout.WEST, new EditPanel(mClient));
        getContentPane().add(BorderLayout.CENTER, mClient);
        getContentPane().add(BorderLayout.SOUTH, new StatusPanel());
        // link
        menuFile.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_FILE));
        menuEdit.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_EDIT));
        menuView.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_VIEW));
        menuModify.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_MODIFY, IBlocksPlugin.SUBTYPE_GENERATE));

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                mClient.requestFocusInWindow();
            }
        });
        setSize(1024, 768);
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                trayIcon();
                Thread t = new Thread((Runnable) mClient);
                t.start();
                
            }
        });

    }

    public void updatePopup(JMenu menu, int... subTypes) {
        MenuLogic.clearPluginMenus(menu);
        ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        if (spec == null) {
            return;
        }
        int type = spec.getClassification();
        int lastModIndex = menu.getItemCount();
        int lastCount = 0;
        for (int subType : subTypes) {
            int thisCount = MenuLogic.addPlugins(mClient, menu, type, subType);
            if ((thisCount > 0) && (lastCount > 0)) {
                JSeparator sep = new JSeparator();
                sep.setName("plugin");
                menu.add(sep, lastModIndex);
                lastCount = 0;
            }
            lastCount += thisCount;
            lastModIndex = menu.getItemCount();
        }
    }

    public static void preLoad() {
        Properties props = StarMadeLogic.getProps();
        String home = props.getProperty("starmade.home", "");
        if (!StarMadeLogic.isStarMadeDirectory(home)) {
            home = System.getProperty("user.dir");
            if (!StarMadeLogic.isStarMadeDirectory(home)) {
                home = JOptionPane.showInputDialog(null, "Enter in the home directory for StarMade", home);
                if (home == null) {
                    System.exit(0);
                }
            }
            props.put("starmade.home", home);
            StarMadeLogic.saveProps();
        }
        StarMadeLogic.setBaseDir(home);
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen(args);
        preLoad();
        final RenderFrame f = new RenderFrame(args);
        f.setVisible(true);
        if (!splash.error) {
            splash.setModalityType(Dialog.ModalityType.MODELESS);
            try {
                final ShipSpec spec = ShipTreeLogic.getBlueprintSpec("Isanth-VI", true);
                if (spec != null) {
                    IRunnableWithProgress t = new IRunnableWithProgress() {
                        @Override
                        public void run(IPluginCallback cb) {
                            StarMadeLogic.getInstance().setCurrentModel(spec);
                            StarMadeLogic.setModel(ShipTreeLogic.loadShip(spec, cb));
                        }
                    };
                    RunnableLogic.run(f, "Loading...", t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SplashScreen.close();
    }

    public RenderPanel getClient() {
        return mClient;
    }

    public void setClient(RenderPanel client) {
        mClient = client;
    }


    private void trayIcon() {
        if (SystemTray.isSupported()) {
            final SystemTray tray = SystemTray.getSystemTray();
            final Image icon = GlobalConfiguration
                    .getImage(Resources.ICON);
            final ActionListener exitListener = new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    System.exit(0);
                }
            };

            final ActionListener rightClickListener = new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final String msg = "You have controled the view of the client by\n"
                            + "right clicking on the system tray icon,\n"
                            + "and chosing the control client view option.\n"
                            + "To again toggle the view state of the client,\n"
                            + "simply click the system tray icon again.";
                    trayIcon.displayMessage("Controling, " + getTitle(), msg,
                            TrayIcon.MessageType.INFO);
                    setVisible(!isVisible());
                }
            };

            final ActionListener clickListener = new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {

                    final String msg = "You have controled the view of the client by,\n"
                            + "clicking on the system tray icon.\n"
                            + "To again toggle the view state of the client,\n"
                            + "simply click the system tray icon again.";
                    trayIcon.displayMessage("Controling, " + getTitle(), msg,
                            TrayIcon.MessageType.INFO);
                    setVisible(!isVisible());

                }
            };

            final PopupMenu popup = new PopupMenu();
            final MenuItem exitOption = new MenuItem("Exit Client");
            exitOption.addActionListener(exitListener);
            final MenuItem hideTaskBarIcon = new MenuItem("Control Client View");
            hideTaskBarIcon.addActionListener(rightClickListener);

            popup.add(hideTaskBarIcon);
            popup.add(exitOption);

            trayIcon = new TrayIcon(icon, getTitle(), popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(clickListener);

            try {
                tray.add(trayIcon);
            } catch (final Exception e) {
                //
            }
        } else {
            //

        }

    }

}
