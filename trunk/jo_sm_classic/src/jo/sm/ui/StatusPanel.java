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
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * StatusBar displayed at the bottom of the GUI for status text. 
 */
public class StatusPanel extends JPanel {
    
    private final JLabel leftStatusLabel;
    private final JPanel contentPanel;

    
    /** Creates a new instance of StatusBar */
    public StatusPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(10, 23));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel(new TriangleSquareWindowsCornerIcon()), BorderLayout.SOUTH);
        rightPanel.setOpaque(false);

        add(rightPanel, BorderLayout.EAST);
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);
        
        // setMainLeftComponent
        leftStatusLabel = new JLabel();
        contentPanel.add(leftStatusLabel, BorderLayout.WEST);
    }

    public void setLabelText(String text, Icon icon) {
        leftStatusLabel.setText(text);
        leftStatusLabel.setIcon(icon);
    }
    
}

