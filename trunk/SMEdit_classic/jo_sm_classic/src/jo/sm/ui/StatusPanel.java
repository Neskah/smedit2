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
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jo.log.LabelLogHandler;



public class StatusPanel extends JPanel {
    
    private final static Logger log = Logger.getLogger(StatusPanel.class.getName());
    private final JPanel contentPanel;
    private final JPanel southPanel;
    private final JPanel midPanel;
    private final ToolPanel toolBar;
    private final LabelLogHandler handler;
    private final Font font;

    
    /** Creates a new instance of StatusBar */
    public StatusPanel() {
        contentPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        midPanel = new JPanel(new BorderLayout());
        toolBar = new ToolPanel(this);
        midPanel.add(toolBar);
        handler = new LabelLogHandler();
        font = handler.label.getFont();
        setLayout(new BorderLayout());
        Logger.getLogger("").addHandler(handler);
        
        southPanel.add(new JLabel(new TriangleSquareWindowsCornerIcon()), BorderLayout.EAST);
        
        handler.label.setBorder(javax.swing.BorderFactory.createTitledBorder(" App Events "));
        handler.label.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize()));
        handler.label.setPreferredSize(new Dimension(1000, 30 + 12));

        contentPanel.add(handler.label, BorderLayout.NORTH);
        contentPanel.add(midPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        
    }
    
}

