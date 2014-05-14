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
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import jo.sm.ui.logic.ShipSpec;
import jo.sm.ui.logic.ShipTreeLogic;

@SuppressWarnings("serial")
public class ShipChooser extends JDialog
{
    private ShipSpec mSelected;
    
    private JTree  mTree;
    
    public ShipChooser(JFrame base)
    {
        super(base, "Choose Ship", Dialog.ModalityType.DOCUMENT_MODAL);
        // instantiate
        mTree = new JTree(ShipTreeLogic.getShipTree());
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        // layout
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, new JLabel("Select a ship to view:"));
        client.add(BorderLayout.CENTER, new JScrollPane(mTree));
        JPanel buttonBar = new JPanel();
        client.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(cancel);
        // link
        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doOK();
            }});
        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doCancel();
            }});
        setSize(640, 480);
        setLocationRelativeTo(base);
    }
    
    private void doOK()
    {
        TreePath selectedPath = mTree.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof ShipSpec)
            mSelected = (ShipSpec)selectedNode.getUserObject();
        else
            mSelected = null;
        setVisible(false);
        dispose();
    }
    
    private void doCancel()
    {
        mSelected = null;
        setVisible(false);
        dispose();
    }

    public ShipSpec getSelected()
    {
        return mSelected;
    }

    public void setSelected(ShipSpec selected)
    {
        mSelected = selected;
    }
}
