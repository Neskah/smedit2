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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.act.GoToProject;
import jo.sm.ui.act.edit.RedoAction;
import jo.sm.ui.act.edit.UndoAction;
import jo.sm.ui.act.file.OpenExistingAction;
import jo.sm.ui.act.file.OpenFileAction;
import jo.sm.ui.act.file.QuitAction;
import jo.sm.ui.act.file.SaveAction;
import jo.sm.ui.act.file.SaveAsBlueprintAction;
import jo.sm.ui.act.file.SaveAsFileAction;
import jo.sm.ui.act.help.AboutAction;
import jo.sm.ui.act.plugin.BlocksPluginAction;
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
import jo.util.Update;

@SuppressWarnings("serial")
public class RenderFrame extends JFrame {

    private static String[] mArgs;
    private static RenderFrame gui;
    private static Update updater = new Update(gui);

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
        updater.checkUpdate(true);
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

    private boolean compactToolbars = true;
    private boolean borderedButtons = true;
    private RenderPanel mClient;
    private TrayIcon trayIcon;
    private JToolBar outerToolBar;
    private JToolBar innerToolBar;

    private JButton             mPlugins;

    public RenderFrame(String[] args) {
        super("SMEdit");
        mArgs = args;
        /* outer-most containers actually reserved for docking the toolbars.
         * so "cp" is actually not the contentpane of the JPanel, but let's
         * ignore that. */
        JPanel outerToolPane = (JPanel) super.getContentPane();
        JPanel innerToolPane = new JPanel(new BorderLayout());
        JPanel cp = new JPanel(new BorderLayout());
        outerToolPane.setLayout(new BorderLayout());
        outerToolPane.add(innerToolPane, BorderLayout.CENTER);
        innerToolPane.add(cp, BorderLayout.CENTER);

        if ((mArgs.length > 0) && (mArgs[0].equals("-opengl"))) {
            mClient = new LWJGLRenderPanel();
        } else {
            mClient = new AWTRenderPanel();
        }

        setupMenus();
        setupToolbars();

        cp.setLayout(new BorderLayout());

        /* Toolbar placement */
        innerToolPane.add(innerToolBar, BorderLayout.NORTH);
        outerToolPane.add(outerToolBar, BorderLayout.NORTH);
        getContentPane().add(BorderLayout.WEST, new EditPanel(mClient));
        getContentPane().add(BorderLayout.CENTER, mClient);
        getContentPane().add(BorderLayout.SOUTH, new StatusPanel());

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

    private void setupToolbars() {
        setOuterToolBar(new JToolBar());
        setInnerToolBar(new JToolBar());

        JButton projectButton;
        final ImageIcon h = new ImageIcon(Paths.getIconDirectory()+ "/home.png");
        projectButton = getDefaultButton(new GoToProject(), "Visit the SMEdit Project", h);
        outerToolBar.add(projectButton);
        
        final ImageIcon p = new ImageIcon(Paths.getIconDirectory()+ "/plugins.png");
        mPlugins = getDefaultActionlessButton("List of avalable plugins", p);
        outerToolBar.add(mPlugins);
        mPlugins.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doPlugin();
            }});

        /*add memory ProgressBar*/
        MemProgressBar mem = new MemProgressBar();
        mem.setMaximumSize(new Dimension(300, 22));
        outerToolBar.add(Box.createHorizontalGlue());
        outerToolBar.add(mem);
    }

    private void setupMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        JMenu menuEdit = new JMenu("Edit");
        menuEdit.setMnemonic(KeyEvent.VK_E);
        JMenu menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);
        JMenu menuModify = new JMenu("Modify");
        menuModify.setMnemonic(KeyEvent.VK_M);
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setMnemonic(KeyEvent.VK_H);
        /*layout*/
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
        menuBar.add(menuHelp);
        menuHelp.add(new AboutAction(this));
        /*link*/
        menuFile.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_FILE));
        menuEdit.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_EDIT));
        menuView.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_VIEW));
        menuModify.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_MODIFY, IBlocksPlugin.SUBTYPE_GENERATE));
    }

    private JButton getDefaultButton(final Action a, final String tip, final ImageIcon i) {
        final JButton button = new JButton(a);
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(2, 0, 2, 0));
        button.setPreferredSize(new Dimension(28, 28));
        button.setMaximumSize(new Dimension(28, 28));
        button.setBorder(new EmptyBorder(3, 3, 3, 3));

        return button;
    }
    
    private JButton getDefaultActionlessButton(final String tip, final ImageIcon i) {
        final JButton button = new JButton();
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(2, 0, 2, 0));
        button.setPreferredSize(new Dimension(28, 28));
        button.setMaximumSize(new Dimension(28, 28));
        button.setBorder(new EmptyBorder(3, 3, 3, 3));

        return button;
    }

    /**
     *
     * @param menu
     * @param subTypes
     */
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
    
    public void doPlugin() {
        JPopupMenu popup = new JPopupMenu();
        int classification = StarMadeLogic.getInstance().getCurrentModel().getClassification();
        List<IBlocksPlugin> plugins = StarMadeLogic.getBlocksPlugins(classification, IBlocksPlugin.SUBTYPE_PAINT);
        if (plugins.isEmpty()) {
            popup.add("no plugins");
        }

        for (IBlocksPlugin plugin : plugins) {
            BlocksPluginAction action = new BlocksPluginAction(mClient, plugin);
            JMenuItem men = new JMenuItem(action);
            popup.add(men);
        }
        Dimension d = mPlugins.getSize();
        popup.show(mPlugins, d.width, d.height);
    }

    /**
     *
     * @return
     */
    public RenderPanel getClient() {
        return mClient;
    }

    /**
     *
     * @param client
     */
    public void setClient(RenderPanel client) {
        mClient = client;
    }

    /**
     *
     */
    public void About_ActionPerformed() {
        new About(this, true).setVisible(true);
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

    /**
     *
     * @return
     */
    public JFrame getFrame() {
        for (Component c = this; c != null; c = c.getParent()) {
            if (c instanceof JFrame) {
                return (JFrame) c;
            }
        }
        return null;
    }

    /**
     * @return the compactToolbars
     */
    public boolean isCompactToolbars() {
        return compactToolbars;
    }

    /**
     * @param compactToolbars the compactToolbars to set
     */
    public void setCompactToolbars(boolean compactToolbars) {
        this.compactToolbars = compactToolbars;
    }

    /**
     * @return the borderedButtons
     */
    public boolean isBorderedButtons() {
        return borderedButtons;
    }

    /**
     * @param borderedButtons the borderedButtons to set
     */
    public void setBorderedButtons(boolean borderedButtons) {
        this.borderedButtons = borderedButtons;
    }

    /**
     * @return the outerToolBar
     */
    public JToolBar getOuterToolBar() {
        return outerToolBar;
    }

    /**
     * @param outerToolBar the outerToolBar to set
     */
    public void setOuterToolBar(JToolBar outerToolBar) {
        this.outerToolBar = outerToolBar;
    }

    /**
     * @return the innerToolBar
     */
    public JToolBar getInnerToolBar() {
        return innerToolBar;
    }

    /**
     * @param innerToolBar the innerToolBar to set
     */
    public void setInnerToolBar(JToolBar innerToolBar) {
        this.innerToolBar = innerToolBar;
    }
}
