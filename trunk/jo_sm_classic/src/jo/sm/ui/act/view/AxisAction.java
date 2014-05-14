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
package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class AxisAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public AxisAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Axis");
        setToolTipText("Display Axis");
        setChecked(mFrame.getClient().isAxis());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
    	mFrame.getClient().setAxis(!mFrame.getClient().isAxis());
        setChecked(!mFrame.getClient().isAxis());
    }

}
