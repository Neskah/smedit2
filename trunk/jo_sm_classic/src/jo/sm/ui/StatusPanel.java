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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import jo.log.TextAreaLogHandler;

import jo.sm.logic.StarMadeLogic;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {

    //private final JProgressBar mMemory;
    private final MemProgressBar mMemory;
    private JLabel mStatus;
    private final JButton mAbout;
    public JScrollPane textScroll;
    private final Color mNormal;

    public StatusPanel() {
        // instantiate
        
        mAbout = new JButton("About");
        mMemory = new MemProgressBar();
        mMemory.setStringPainted(true);
        mMemory.setIndeterminate(true);
        mNormal = mMemory.getBackground();
        
        textScroll = new JScrollPane(TextAreaLogHandler.TEXT_AREA,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScroll.setBorder(null);
                textScroll.setPreferredSize(new Dimension(1024, 120));
		textScroll.setVisible(true);
        // layout
        setLayout(new BorderLayout());
        add(mAbout, BorderLayout.EAST);
        add(mMemory, BorderLayout.WEST);
        //add(textScroll, BorderLayout.SOUTH);
        mAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                DlgAbout dlg = new DlgAbout(getFrame());
                dlg.setVisible(true);
            }
        });
        StarMadeLogic.getInstance().addPropertyChangeListener("statusMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent ev) {
                mStatus.setText((String) ev.getNewValue());
            }
        });
        Thread t = new Thread("mem_ticker") {
            @Override
            public void run() {
                doTicker();
            }
        };
        t.setDaemon(true);
        t.start();
    }

    private void doTicker() {
        mMemory.setIndeterminate(false);
        for (;;) {
            int free = (int) (Runtime.getRuntime().freeMemory() / 1024L / 1024L);
            int total = (int) (Runtime.getRuntime().totalMemory() / 1024L / 1024L);
            int max = (int) (Runtime.getRuntime().maxMemory() / 1024L / 1024L);
            free += (max - total);
            mMemory.setValue(free);
            mMemory.setString(free + "M");
            mMemory.setToolTipText("Free=" + free + ", max=" + max + ", total=" + total);
            if (free * 100 / max < 5) {
                mMemory.setBackground(Color.red);
                mStatus.setText("LOW ON MEMORY");
            } else {
                mMemory.setBackground(mNormal);
                mStatus.setText(StarMadeLogic.getInstance().getStatusMessage());
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
    }

    private JFrame getFrame() {
        for (Component c = this; c != null; c = c.getParent()) {
            if (c instanceof JFrame) {
                return (JFrame) c;
            }
        }
        return null;
    }
}
