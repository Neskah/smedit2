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
import java.awt.Component;
import java.awt.Graphics;
import java.util.logging.Logger;
import javax.swing.Icon;

/**
 *
 * @author Robert Barefoot
 */
public class TriangleSquareWindowsCornerIcon implements Icon {

    //RGB values discovered using ZoomIn
    private static final Color THREE_D_EFFECT_COLOR = new Color(255, 255, 255);
    private static final Color SQUARE_COLOR_LEFT = new Color(184, 180, 163);
    private static final Color SQUARE_COLOR_TOP_RIGHT = new Color(184, 180, 161);
    private static final Color SQUARE_COLOR_BOTTOM_RIGHT = new Color(184, 181, 161);

    //Dimensions
    private static final int WIDTH = 12;
    private static final int HEIGHT = 12;



    @Override
    public int getIconHeight() {
        return HEIGHT;
    }

    @Override
    public int getIconWidth() {
        return WIDTH;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        //Layout a row and column "grid"
        int firstRow = 0;
        int firstColumn = 0;
        int rowDiff = 4;
        int columnDiff = 4;

        int secondRow = firstRow + rowDiff;
        int secondColumn = firstColumn + columnDiff;
        int thirdRow = secondRow + rowDiff;
        int thirdColumn = secondColumn + columnDiff;


        //Draw the white squares first, so the gray squares will overlap
        draw3dSquare(g, firstColumn+1, thirdRow+1);

        draw3dSquare(g, secondColumn+1, secondRow+1);
        draw3dSquare(g, secondColumn+1, thirdRow+1);

        draw3dSquare(g, thirdColumn+1, firstRow+1);
        draw3dSquare(g, thirdColumn+1, secondRow+1);
        draw3dSquare(g, thirdColumn+1, thirdRow+1);

        //draw the gray squares overlapping the white background squares
        drawSquare(g, firstColumn, thirdRow);

        drawSquare(g, secondColumn, secondRow);
        drawSquare(g, secondColumn, thirdRow);

        drawSquare(g, thirdColumn, firstRow);
        drawSquare(g, thirdColumn, secondRow);
        drawSquare(g, thirdColumn, thirdRow);

    }

    private void draw3dSquare(Graphics g, int x, int y){
        Color oldColor = g.getColor(); //cache the old color
        g.setColor(THREE_D_EFFECT_COLOR); //set the white color
        g.fillRect(x,y,2,2); //draw the square
        g.setColor(oldColor); //reset the old color
    }


    private void drawSquare(Graphics g, int x, int y){
        Color oldColor = g.getColor();
        g.setColor(SQUARE_COLOR_LEFT);
        g.drawLine(x,y, x,y+1);
        g.setColor(SQUARE_COLOR_TOP_RIGHT);
        g.drawLine(x+1,y, x+1,y);
        g.setColor(SQUARE_COLOR_BOTTOM_RIGHT);
        g.drawLine(x+1,y+1, x+1,y+1);
        g.setColor(oldColor);
    }
    private static final Logger LOG = Logger.getLogger(TriangleSquareWindowsCornerIcon.class.getName());

}