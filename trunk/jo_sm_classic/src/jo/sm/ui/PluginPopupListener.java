/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jo.sm.ui;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 *
 * @author Robert Barefoot
 */
class PluginPopupListener implements MenuListener {
    private final int[] mTypes;
    private final RenderFrame outer;

    public PluginPopupListener(final RenderFrame outer, int... types) {
        this.outer = outer;
        mTypes = types;
    }

    @Override
    public void menuCanceled(MenuEvent ev) {
    }

    @Override
    public void menuDeselected(MenuEvent ev) {
    }

    @Override
    public void menuSelected(MenuEvent ev) {
        outer.updatePopup((JMenu) ev.getSource(), mTypes);
    }
    
}
