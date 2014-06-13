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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
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
import jo.sm.ui.act.Shot;
import jo.sm.ui.act.edit.RedoAction;
import jo.sm.ui.act.edit.RedoActionButton;
import jo.sm.ui.act.edit.UndoAction;
import jo.sm.ui.act.edit.UndoActionButton;
import jo.sm.ui.act.file.OpenExistingAction;
import jo.sm.ui.act.file.OpenExistingAction1;
import jo.sm.ui.act.file.OpenFileAction;
import jo.sm.ui.act.file.OpenFileAction1;
import jo.sm.ui.act.file.QuitAction;
import jo.sm.ui.act.file.SaveAction;
import jo.sm.ui.act.file.SaveAsBlueprintAction;
import jo.sm.ui.act.file.SaveAsBlueprintAction1;
import jo.sm.ui.act.file.SaveAsFileAction;
import jo.sm.ui.act.file.SaveAsFileAction1;
import jo.sm.ui.act.memRefresh;
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


@SuppressWarnings("serial")
public class RenderFrame extends JFrame {

    private static final Logger log = Logger.getLogger(RenderFrame.class.getName());
    private static String[] mArgs;

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
                //e.printStackTrace();
            }
        }
        SplashScreen.close();
        log.config("Main application started: " + GlobalConfiguration.NAME);
    }

    private boolean compactToolbars = true;
    private boolean borderedButtons = true;
    private RenderPanel mClient;
    private JToolBar outerToolBar;
    private JToolBar innerToolBar;

    private JButton mPlugins;

    public RenderFrame(String[] args) {
        setTitle(GlobalConfiguration.NAME + "_Classic version 1." + ((float) GlobalConfiguration.getVersion() / 100));
        mArgs = args;
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        setSize(1024, 768);

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
        getContentPane().add(new EditPanel(mClient, this), BorderLayout.WEST);
        getContentPane().add(mClient, BorderLayout.CENTER);
        getContentPane().add(new StatusPanel(), BorderLayout.SOUTH);

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
    }

    private void setupToolbars() {
        setOuterToolBar(new JToolBar());
        setInnerToolBar(new JToolBar());

        JButton openPrintButton;
        final ImageIcon op = new ImageIcon(Paths.getIconDirectory() + "/open_print.png");
        openPrintButton = getDefaultButton(new OpenExistingAction1(this), "open a blueprint", op);
        outerToolBar.add(openPrintButton);

        JButton openButton;
        final ImageIcon o = new ImageIcon(Paths.getIconDirectory() + "/open.png");
        openButton = getDefaultButton(new OpenFileAction1(this), "open a file", o);
        outerToolBar.add(openButton);

        JButton savePrintButton;
        final ImageIcon sp = new ImageIcon(Paths.getIconDirectory() + "/save.png");
        savePrintButton = getDefaultButton(new SaveAsBlueprintAction1(this, false), "Save blueprint", sp);
        outerToolBar.add(savePrintButton);

        JButton saveButton;
        final ImageIcon sa = new ImageIcon(Paths.getIconDirectory() + "/save_as.png");
        saveButton = getDefaultButton(new SaveAsFileAction1(this), "Save file", sa);
        outerToolBar.add(saveButton);

        JButton screenButton;
        final ImageIcon s = new ImageIcon(Paths.getIconDirectory() + "/shot.png");
        screenButton = getDefaultButton(new Shot(this), "Screenshots of work", s);
        outerToolBar.add(screenButton);

        outerToolBar.addSeparator();
        outerToolBar.addSeparator();

        JButton undoButton;
        final ImageIcon u = new ImageIcon(Paths.getIconDirectory() + "/undo.png");
        undoButton = getDefaultButton(new UndoActionButton(this), "Undo last action", u);
        outerToolBar.add(undoButton);

        JButton redoButton;
        final ImageIcon r = new ImageIcon(Paths.getIconDirectory() + "/redo.png");
        redoButton = getDefaultButton(new RedoActionButton(this), "Redo last action", r);
        outerToolBar.add(redoButton);
        
        outerToolBar.add(Box.createHorizontalGlue());
        
        final ImageIcon p = new ImageIcon(Paths.getIconDirectory() + "/plugins.png");
        mPlugins = getDefaultActionlessButton("Plugins", "List of avalable plugins", p);
        outerToolBar.add(mPlugins);
        mPlugins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPlugin();
            }
        });

        /*add memory ProgressBar*/
        JButton memButton;
        final ImageIcon c = new ImageIcon(Paths.getIconDirectory() + "/cpu.png");
        memButton = getProgressButton(new memRefresh(), "Click to refresh Memory use", c);
        outerToolBar.add(memButton);
        
        
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
        /*link*/
        menuFile.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_FILE));
        menuEdit.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_EDIT));
        menuView.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_VIEW));
        menuModify.addMenuListener(new PluginPopupListener(this, IBlocksPlugin.SUBTYPE_MODIFY, IBlocksPlugin.SUBTYPE_GENERATE));
    }

    /**
     * Makes a JButton with the given icon and tooltop. If the icon cannot be
     * loaded, then the text will be used instead.
     *
     * Adds this RenderFame as an actionListener.
     *
     * @return a shiny new JButton
     *
     */
    private JButton getDefaultButton(final Action a, final String tip, final ImageIcon i) {
        final JButton button = new JButton(a);
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(6, 3, 6, 3));
        button.setPreferredSize(new Dimension(32, 32));
        button.setMaximumSize(new Dimension(32, 32));
        button.setBorder(new EmptyBorder(3, 3, 3, 3));

        return button;
    }

    /**
     * Makes a JButton with the given icon and tooltop. If the icon cannot be
     * loaded, then the text will be used instead.
     *
     * Adds this RenderFame as an actionListener.
     *
     * @return a shiny new JButton
     *
     */
    private JButton getDefaultActionlessButton(final String text, final String tip, final ImageIcon i) {
        final JButton button = new JButton();
        button.setText(text);
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);
        button.setMargin(new Insets(6, 3, 6, 3));
        button.setPreferredSize(new Dimension(75, 32));
        button.setMaximumSize(new Dimension(75, 32));
        button.setBorder(new EmptyBorder(3, 3, 3, 3));
        button.setFont(new Font("Tahoma", 0, 10));

        return button;
    }

    /**
     * Makes a JButton with the given icon and tooltop. If the icon cannot be
     * loaded, then the text will be used instead.
     *
     * Adds this RenderFame as an actionListener.
     *
     * @return a shiny new JButton
     *
     */
    private JButton getProgressButton(final Action a, final String tip, final ImageIcon i ) {
        MemProgressBar mem = new MemProgressBar();
        mem.setMaximumSize(new Dimension(250, 32));
        final JButton button = new JButton(a);
        //button.add(Box.createHorizontalGlue());
        button.add(mem);
        button.setToolTipText(tip);
        button.setIcon(i);
        button.setFocusable(false);

        button.setPreferredSize(new Dimension(250, 32));
        button.setMaximumSize(new Dimension(250, 32));
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
