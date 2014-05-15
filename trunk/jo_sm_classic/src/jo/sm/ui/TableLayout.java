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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public final class TableLayout extends GridBagLayout {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 507791756710474413L;

    static final String[] defaults = {
        "gridx", "gridy", "gridwidth", "gridheight", "weightx", "weighty",};

    GridBagConstraints defaultGBC = new GridBagConstraints();
    int currentX;
    int currentY;

    public TableLayout() {
        super();
        defaultGBC.weightx = 1;
        defaultGBC.weighty = 1;
        currentX = 0;
        currentY = 0;
    }

    public TableLayout(String defaultConstraints) {
        super();
        java.util.StringTokenizer st = new java.util.StringTokenizer(defaultConstraints, " x,-");
        defaultGBC.weightx = 1;
        defaultGBC.weighty = 1;
        while (st.hasMoreTokens()) {
            String tok = st.nextToken().toLowerCase();
//            int o;
//            String key;
//            String val;
            parseToken(defaultGBC, "", tok);
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (!(constraints instanceof String)) {
            super.addLayoutComponent(comp, constraints);
            return;
        }
        java.util.StringTokenizer st = new java.util.StringTokenizer((String) constraints, " x,-");
        GridBagConstraints gbc = new GridBagConstraints();
        copy(gbc, defaultGBC);
        gbc.weightx = 1;
        gbc.weighty = 1;
        for (int i = 0; st.hasMoreTokens(); i++) {
            String tok = st.nextToken().toLowerCase();
//            int o;
//            String key;
//            String val;
            parseToken(gbc, defaults[i], tok);
        }
        currentX = gbc.gridx;
        currentY = gbc.gridy;
        super.addLayoutComponent(comp, gbc);
    }

    void parseToken(GridBagConstraints gbc, String theDefault, String tok) {
        int o = tok.indexOf("=");
        String key;
        String val;
        if (o > 0) {
            key = tok.substring(0, o);
            val = tok.substring(o + 1);
        } else {
            key = theDefault;
            val = tok;
        }
        parseSetting(gbc, key, val);
    }

    private void parseSetting(GridBagConstraints gbc, String key, String val) {
        int v;
        try {
            v = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            v = 0;
        }
        switch (key) {
            case "x":
            case "gridx":
                if (val.equals(".")) {
                    gbc.gridx = currentX;
                } else if (val.equals("+")) {
                    gbc.gridx = ++currentX;
                } else {
                    gbc.gridx = v;
                }   break;
            case "y":
            case "gridy":
                if (val.equals(".")) {
                    gbc.gridy = currentY;
                } else if (val.equals("+")) {
                    gbc.gridy = ++currentY;
                } else {
                    gbc.gridy = v;
                }   break;
            case "gridwidth":
            case "width":
                gbc.gridwidth = v;
                break;
            case "gridheight":
            case "height":
                gbc.gridheight = v;
                break;
            case "weightx":
                gbc.weightx = v;
                break;
            case "weighty":
                gbc.weighty = v;
                break;
            case "ipadx":
                gbc.ipadx = v;
                break;
            case "ipady":
                gbc.ipady = v;
                break;
            case "fill":
        switch (val) {
            case "none":
                gbc.fill = GridBagConstraints.NONE;
                break;
            case "horizontal":
            case "h":
                gbc.fill = GridBagConstraints.HORIZONTAL;
                break;
            case "vertical":
            case "v":
                gbc.fill = GridBagConstraints.VERTICAL;
                break;
            case "both":
            case "hv":
                gbc.fill = GridBagConstraints.BOTH;
                break;
        }
break;
            case "anchor":
        switch (val) {
            case "center":
                gbc.anchor = GridBagConstraints.CENTER;
                break;
            case "north":
            case "n":
                gbc.anchor = GridBagConstraints.NORTH;
                break;
            case "northeast":
            case "ne":
                gbc.anchor = GridBagConstraints.NORTHEAST;
                break;
            case "east":
            case "e":
                gbc.anchor = GridBagConstraints.EAST;
                break;
            case "southeast":
            case "se":
                gbc.anchor = GridBagConstraints.SOUTHEAST;
                break;
            case "south":
            case "s":
                gbc.anchor = GridBagConstraints.SOUTH;
                break;
            case "southwest":
            case "sw":
                gbc.anchor = GridBagConstraints.SOUTHWEST;
                break;
            case "west":
            case "w":
                gbc.anchor = GridBagConstraints.WEST;
                break;
            case "northwest":
            case "nw":
                gbc.anchor = GridBagConstraints.NORTHWEST;
                break;
        }
break;
        }
    }

    void copy(GridBagConstraints lvalue, GridBagConstraints rvalue) {
        lvalue.anchor = rvalue.anchor;
        lvalue.fill = rvalue.fill;
        lvalue.gridheight = rvalue.gridheight;
        lvalue.gridwidth = rvalue.gridwidth;
        lvalue.gridx = rvalue.gridx;
        lvalue.gridy = rvalue.gridy;
        lvalue.insets = rvalue.insets;
        lvalue.ipadx = rvalue.ipadx;
        lvalue.ipady = rvalue.ipady;
        lvalue.weightx = rvalue.weightx;
        lvalue.weighty = rvalue.weighty;
    }
}
