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

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.GridLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;
import jo.vecmath.logic.Point3iLogic;

public class RenderPanelKeyEventDispatcher implements KeyEventDispatcher {

    private static final int PAN_XM = 'A';
    private static final int PAN_XP = 'D';
    private static final int PAN_YM = 'W';
    private static final int PAN_YP = 'S';
    private static final int PAN_ZM = 'Q';
    private static final int PAN_ZP = 'E';

    private static final int SELECTION_MOVE = 0;
    private static final int SELECTION_MOVE_UPPER = 0x40;
    private static final int SELECTION_MOVE_LOWER = 0x80;
    private static final int SELECTION_NUDGE = 0x200;

    private final AWTRenderPanel mPanel;

    public RenderPanelKeyEventDispatcher(AWTRenderPanel panel) {
        mPanel = panel;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent ev) {
        if (!isFocused()) {
            return false;
        }
        if (ev.getID() == KeyEvent.KEY_PRESSED) {
            doKeyDown(ev.getKeyCode(), ev.getModifiersEx());
        } else if (ev.getID() == KeyEvent.KEY_RELEASED) {
            doKeyUp(ev.getKeyCode(), ev.getModifiersEx());
        }
        return false;
    }

    public void doKeyDown(int keyCode, int keyMod) {
        //System.out.println("code="+Integer.toHexString(keyCode)+", mod="+Integer.toHexString(keyMod));
        Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
        Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
        if ((lower != null) && (upper != null)) {
            normalize(lower, upper);
            if (keyMod == SELECTION_MOVE_UPPER) {
                doMoveUpperSelection(keyCode, upper);
            } else if (keyMod == SELECTION_MOVE_LOWER) {
                doMoveLowerSelection(keyCode, lower);
            } else if (keyMod == SELECTION_MOVE) {
                doMoveSelection(keyCode, lower, upper);
            } else if (keyMod == SELECTION_NUDGE) {
                doNudgeSelection(keyCode, lower, upper);
            }
            mPanel.updateTiles();
        } else {
            doPan(keyCode, keyMod);
        }
    }

    private void normalize(Point3i lower, Point3i upper) {
        Point3i l = Point3iLogic.min(lower, upper);
        Point3i u = Point3iLogic.max(lower, upper);
        lower.set(l);
        upper.set(u);
    }

    private void doPan(int keyCode, int keyMod) {
        if (keyMod == 0) {
            Point3i delta = keyToDelta(keyCode, null);
            mPanel.mPOVTranslate.x += delta.x;
            mPanel.mPOVTranslate.y += delta.y;
            mPanel.mPOVTranslate.z += delta.z;
            mPanel.updateTransform();
        }
    }

    private void doMoveUpperSelection(int keyCode, Point3i upper) {
        keyToDelta(keyCode, upper);
    }

    private void doMoveLowerSelection(int keyCode, Point3i lower) {
        keyToDelta(keyCode, lower);
    }

    private void doMoveSelection(int keyCode, Point3i lower, Point3i upper) {
        keyToDelta(keyCode, lower);
        keyToDelta(keyCode, upper);
    }

    private void doNudgeSelection(int keyCode, Point3i lower, Point3i upper) {
        mPanel.getUndoer().checkpoint(StarMadeLogic.getModel());
        SparseMatrix<Block> clip = GridLogic.extract(StarMadeLogic.getModel(), lower, upper);
        GridLogic.delete(StarMadeLogic.getModel(), lower, upper);
        keyToDelta(keyCode, lower);
        keyToDelta(keyCode, upper);
        GridLogic.insert(StarMadeLogic.getModel(), clip, lower);
    }

    private Point3i keyToDelta(int keyCode, Point3i delta) {
        if (delta == null) {
            delta = new Point3i();
        }
        if (keyCode == PAN_XP) {
            delta.x++;
        } else if (keyCode == PAN_XM) {
            delta.x--;
        } else if (keyCode == PAN_YP) {
            delta.y++;
        } else if (keyCode == PAN_YM) {
            delta.y--;
        } else if (keyCode == PAN_ZP) {
            delta.z++;
        } else if (keyCode == PAN_ZM) {
            delta.z--;
        }
        return delta;
    }

    public void doKeyUp(int keyCode, int keyMod) {

    }

    private boolean isFocused() {
        for (Component c = mPanel; c != null; c = c.getParent()) {
            if (c instanceof JFrame) {
                if (((JFrame) c).isActive()) {
                    return true;
                }
            }
        }
        return false;
    }
}
