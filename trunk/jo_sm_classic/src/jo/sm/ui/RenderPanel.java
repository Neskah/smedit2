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

import javax.swing.JPanel;

import jo.sm.data.RenderPoly;
import jo.sm.data.UndoBuffer;
import jo.sm.ship.data.Block;

@SuppressWarnings("serial")
public abstract class RenderPanel extends JPanel {

    public abstract void updateTransform();

    public abstract void updateTiles();

    public abstract RenderPoly getTileAt(double x, double y);

    public abstract Block getBlockAt(double x, double y);

    public abstract boolean isPlainGraphics();

    public abstract void setPlainGraphics(boolean plainGraphics);

    public abstract boolean isAxis();

    public abstract void setAxis(boolean axis);

    public abstract boolean isDontDraw();

    public abstract void setDontDraw(boolean dontDraw);

    public abstract void setCloseRequested(boolean pleaseClose);

    public abstract UndoBuffer getUndoer();

    public abstract void setUndoer(UndoBuffer undoer);

    public abstract void undo();

    public abstract void redo();
}
