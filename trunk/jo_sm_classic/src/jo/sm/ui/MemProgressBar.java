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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author elkes
 */
public class MemProgressBar extends JProgressBar {

    private final long kbFactor = (long) Math.pow(1024, 2);
    private final int maxHeapSizeMb;

    public MemProgressBar() {
        super();
        setStringPainted(true);
        maxHeapSizeMb = (int) (Runtime.getRuntime().maxMemory() / kbFactor);
        setMinimum(0);
        setMaximum(maxHeapSizeMb); 
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.gc();
            }
            @Override
            public void mousePressed(MouseEvent me) {}
            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}
            @Override
            public void mouseExited(MouseEvent me) {}
        });
        startTimer();
    }

    private void startTimer(){
        MemoryMonitorTimer memoryMonitorTimer = new MemoryMonitorTimer();
        memoryMonitorTimer.start();
    }
    
    private class MemoryMonitorTimer extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    final int usedMemoryMb = (int) (usedMem / kbFactor);
                    final String barString = usedMemoryMb + "Mb/" + maxHeapSizeMb + "Mb";
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setValue(usedMemoryMb);
                            setString(barString);
                        }
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException exc) {
                System.err.println("MemProgressBar.MemoryMonitorTimer: "+exc);
            }
        }
    }
}
