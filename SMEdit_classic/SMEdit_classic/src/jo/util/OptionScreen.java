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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import jo.sm.edit.SMEdit;

public class OptionScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private static String[] mArgs;
    public final static String fileName = Paths.getOptsFile();
    private static final Logger log = Logger.getLogger(OptionScreen.class.getName());
    private Properties mProps;
    private final File mStarMadeDir;
    private SMEdit screen;

    /* For tile properties dialog */
    private final JSpinner memoryOpt;
    private final JLabel tileImg;
    private final JTextField home;
    private final JButton applyBtn;
    private final JButton cancelBtn;
    private final JComboBox comBox;

    private final JLabel osLable;
    private final JLabel memoryLable;
    private final JLabel homeLable;
    private final JLabel osLable2;
    private final JLabel memoryLable2;

    @SuppressWarnings("unchecked")
    public OptionScreen(final String[] args) {
        loadJosmProps();
        mArgs = args;
        setIconImage(GlobalConfiguration.getImage(Resources.ICON));
        setTitle("Preloaded Option Settings for " + GlobalConfiguration.NAME + "_Classic version 1." + ((float) GlobalConfiguration.getVersion() / 100));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
            }
        });
        comBox = new JComboBox(new DefaultComboBoxModel(new String[]{"32 bit", "64 bit"}));
        comBox.setBorder(BorderFactory.createEtchedBorder());
        if (null != mProps.getProperty("bit", "")) {
            switch (mProps.getProperty("bit", "")) {
                case "32 bit":
                    comBox.setSelectedIndex(0);
                    break;
                case "64 bit":
                    comBox.setSelectedIndex(1);
                    break;
            }
        } else {
            comBox.setSelectedIndex(0);
        }

        memoryOpt = new JSpinner();
        memoryOpt.setBorder(BorderFactory.createEtchedBorder());
        if (null != mProps.getProperty("memory", "")) {
            switch (mProps.getProperty("memory", "")) {
                case "0":
                    memoryOpt.setValue(2);
                    break;
                case "1":
                    memoryOpt.setValue(1);
                    break;
                case "2":
                    memoryOpt.setValue(2);
                    break;
                case "3":
                    memoryOpt.setValue(3);
                    break;
                case "4":
                    memoryOpt.setValue(4);
                    break;
                case "5":
                    memoryOpt.setValue(5);
                    break;
                case "6":
                    memoryOpt.setValue(6);
                    break;
                case "7":
                    memoryOpt.setValue(7);
                    break;
                case "8":
                    memoryOpt.setValue(8);
                    break;
                case "9":
                    memoryOpt.setValue(9);
                    break;
                case "10":
                    memoryOpt.setValue(10);
                    break;
                case "11":
                    memoryOpt.setValue(11);
                    break;
                case "12":
                    memoryOpt.setValue(12);
                    break;
                case "13":
                    memoryOpt.setValue(13);
                    break;
                case "14":
                    memoryOpt.setValue(14);
                    break;
                case "15":
                    memoryOpt.setValue(15);
                    break;
                case "16":
                    memoryOpt.setValue(16);
                    break;
                case "17":
                    memoryOpt.setValue(17);
                    break;
                case "18":
                    memoryOpt.setValue(18);
                    break;
                case "19":
                    memoryOpt.setValue(19);
                    break;
                case "20":
                    memoryOpt.setValue(20);
                    break;
                case "21":
                    memoryOpt.setValue(21);
                    break;
                case "22":
                    memoryOpt.setValue(22);
                    break;
                case "23":
                    memoryOpt.setValue(23);
                    break;
                case "24":
                    memoryOpt.setValue(24);
                    break;
                case "25":
                    memoryOpt.setValue(25);
                    break;
                case "26":
                    memoryOpt.setValue(26);
                    break;
                case "27":
                    memoryOpt.setValue(27);
                    break;
                case "28":
                    memoryOpt.setValue(28);
                    break;
                case "29":
                    memoryOpt.setValue(29);
                    break;
                case "30":
                    memoryOpt.setValue(30);
                    break;
                case "31":
                    memoryOpt.setValue(31);
                    break;
                case "32":
                    memoryOpt.setValue(32);
                    break;

            }
        }

        home = new JTextField(mProps.getProperty("starmade.home", ""));
        mStarMadeDir = new File(home.getText());

        osLable = new JLabel();
        osLable.setText("OS Trype?");
        memoryLable = new JLabel();
        memoryLable.setText("Memory Use?");
        homeLable = new JLabel();
        homeLable.setText("Starmade Home?");
        osLable2 = new JLabel();
        osLable2.setText("if you dont know leave as 32 bit");
        memoryLable2 = new JLabel();
        memoryLable2.setText("this needs to be greater than 0");
        applyBtn = new JButton("Save");
        applyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProps();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screen = new SMEdit(mArgs);
            }
        });

        tileImg = new JLabel();
        final ImageIcon icon = new ImageIcon();
        icon.setImage(GlobalConfiguration.getImage(Resources.SPLASH));
        tileImg.setIcon(icon);
        tileImg.setHorizontalAlignment(SwingConstants.CENTER);
        tileImg.setBorder(new TitledBorder("Options"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(applyBtn, PREFERRED_SIZE, 175, PREFERRED_SIZE)
                                        .addPreferredGap(RELATED, DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cancelBtn, PREFERRED_SIZE, 175, PREFERRED_SIZE))
                                .addComponent(tileImg, PREFERRED_SIZE, 450, PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(TRAILING, false)
                                                .addComponent(homeLable, LEADING, DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                                .addComponent(memoryLable, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(osLable, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(home, PREFERRED_SIZE, 350, PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(TRAILING, false)
                                                                .addComponent(comBox, LEADING, 0, 100, Short.MAX_VALUE)
                                                                .addComponent(memoryOpt, LEADING))
                                                        .addPreferredGap(RELATED)
                                                        .addGroup(layout.createParallelGroup(LEADING)
                                                                .addComponent(osLable2, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(memoryLable2, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tileImg, PREFERRED_SIZE, 320, PREFERRED_SIZE)
                        .addPreferredGap(UNRELATED)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(osLable)
                                .addComponent(comBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(osLable2))
                        .addPreferredGap(RELATED)
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(memoryLable)
                                        .addComponent(memoryOpt, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addComponent(memoryLable2))
                        .addPreferredGap(RELATED)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(homeLable)
                                .addComponent(home, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addPreferredGap(RELATED, 13, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(applyBtn, PREFERRED_SIZE, 48, PREFERRED_SIZE)
                                .addComponent(cancelBtn, PREFERRED_SIZE, 48, PREFERRED_SIZE))
                        .addGap(15, 15, 15))
        );

        pack();
        setVisible(true);

    }

    private void loadJosmProps() {
        File uHome = new File(System.getProperty("user.home"));
        File props = new File(uHome, ".josm");
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

    private void saveProps() {
        if (mProps == null) {
            return;
        }
        if (mStarMadeDir != null) {
            mProps.put("starmade.home", home.getText());
            mProps.put("bit", comBox.getSelectedItem().toString());
            mProps.put("memory", memoryOpt.getValue().toString());
        }
        File uHome = new File(System.getProperty("user.home"));
        File props = new File(uHome, ".josm");
        try {
            try (FileWriter fos = new FileWriter(props)) {
                mProps.store(fos, "StarMade Utils defaults");
            }
        } catch (IOException e) {

        }
        screen = new SMEdit(mArgs);
    }

}
