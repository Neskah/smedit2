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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.UIManager;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Data;
import jo.sm.ship.logic.DataLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3i;

@SuppressWarnings("serial")
public class RenderApplet extends JApplet
{
	private Map<Point3i,Data>	mShip;
    private RenderPanel mClient;

    public void init()
    {
    	setNativeLookAndFeel();
        // instantiate
        mClient = new AWTRenderPanel();
        // layout
        getContentPane().add(BorderLayout.CENTER, mClient);
        getContentPane().add(BorderLayout.SOUTH, new BegPanel());
    }
    
    public void start()
    {
        // load
        mShip= new HashMap<Point3i, Data>();
        for (int i = 1; i < 999; i++)
        {
        	String url = getParameter("data"+i);
        	if (url == null)
        	{
        		break;
        	}
        	try
        	{
        		URL u = new URL(url);
        		InputStream is = u.openStream();
                Point3i position = new Point3i(i, i, i);
        		Data datum = DataLogic.readFile(is, true);
        		if (datum != null)
        		{
                    mShip.put(position, datum);
        		}
        	}
        	catch (IOException e)
        	{
        		e.printStackTrace();
        	}
        }
        SparseMatrix<Block> grid = ShipLogic.getBlocks(mShip);
        StarMadeLogic.setModel(grid);
    }

    public static void setNativeLookAndFeel() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
          System.out.println("Error setting native LAF: " + e);
        }
      }
}
