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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BegPanel extends JPanel {

    private static final int TICK = 200;
    private static final int CHOP = 120;

    public static final String THE_RAIDERS_LAMENT_AUDIO = "http://podiobooks.com/title/the-raiders-lament";
    public static final String THE_RAIDERS_LAMENT = "https://www.smashwords.com/books/view/347157";
    public static final String DOCUMENTATION = "http://www.starmadewiki.com/wiki/SMEdit";

    private int mMessageOffset;
    private int mRepeats;

    private final JLabel mStatus;
    private final JButton mAudio;
    private final JButton mText;

    public BegPanel() {
        mRepeats = 3;
        // instantiate
        mStatus = new JLabel(MESSAGE.substring(0, CHOP));
        setBackground(Color.cyan);
        mAudio = new JButton("Audiobook");
        mText = new JButton("E-book");
        Dimension d1 = mAudio.getPreferredSize();
        Dimension d2 = mText.getPreferredSize();
        mStatus.setPreferredSize(new Dimension(1024 - d1.width - d2.width, Math.max(d1.height, d2.height)));
        // layout
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add("Center", mStatus);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        buttons.add(mAudio);
        buttons.add(mText);
        add("East", buttons);
        // link
        Thread t = new Thread("beg_ticker") {
            @Override
            public void run() {
                doTicker();
            }
        };
        t.setDaemon(true);
        t.start();
        mText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(THE_RAIDERS_LAMENT);
            }
        });
        mText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                doGoto(THE_RAIDERS_LAMENT_AUDIO);
            }
        });
    }

    private void doTicker() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        for (;;) {
            try {
                Thread.sleep(TICK);
            } catch (InterruptedException e) {
            }
            mMessageOffset++;
            if (mMessageOffset == MESSAGE.length()) {
                mMessageOffset = 0;
                mRepeats--;
                if (mRepeats < 0) {
                    return;
                }
            }
            String msg = MESSAGE.substring(mMessageOffset) + MESSAGE.substring(0, mMessageOffset);
            msg = msg.substring(0, CHOP);
            mStatus.setText(msg);
        }
    }

    private void doGoto(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE)) {
                try {
                    desktop.browse(URI.create(url));
                    return;
                } catch (IOException e) {
                    // handled below
                }
            }
        }
    }

    public static final String MESSAGE = "This software is made freely available with no charge or limitation. "
            + "Even the source is included. "
            + "It was originally distributed as \"begware\", promoting my book \"The Raider's Lament\". "
            + "I wanted enough downloads/sales to earn enough to buy a Minecraft Lego kit for my daughter. "
            + "The good people of this community helped me reach the target, and a huge shout out to Kahulbane"
            + " for donating quite a bit of it! "
            + "So I've now officially considered this software 'paid for' and have removed the begware nagger. "
            + "If you are interested you can still download my book. I'd appreciate it, even more if you read it "
            + "and review it. You can still choose to donate by buying. Further proceeds will go towards buying "
            + "the other Minecraft kids for my daughter! "
            + "The buttons below will take you to the audiobook page (free) and the"
            + "eBook page (first 20%, the full book for $.99. "
            + "Thank you for using and supporting SMEdit. ";
}
