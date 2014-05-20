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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.BlocksPluginAction;
import jo.vecmath.Point3i;

@SuppressWarnings("serial")
public class EditPanel extends JPanel {

    private boolean mPainting;

    private final RenderPanel mRenderer;

    private JLabel mSymLabel;

    private JLabel mColor;
    private JLabel mCurrent;
    private JLabel mChoice;
    private JButton mGrey;
    private JButton mBlack;
    private JButton mRed;
    private JButton mPurple;
    private JButton mBlue;
    private JButton mGreen;
    private JButton mBrown;
    private JButton mYellow;
    private JButton mWhite;
    private JButton mClear;
    private JSpinner mRadius;
    private JLabel mRaLabel;
    private JButton mAll;
    private JButton mPlugins;
    private JCheckBox mXSymmetry;
    private JCheckBox mYSymmetry;
    private JCheckBox mZSymmetry;

    public EditPanel(RenderPanel renderer) {
        mRenderer = renderer;
        initComponents();

        // link
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doMouseClick(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON3) {
                    mPainting = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() == MouseEvent.BUTTON3) {
                    mPainting = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent ev) {
                if (mPainting) {
                    doMouseClick(ev.getX(), ev.getY());
                }
            }
        };
        mRenderer.addMouseListener(ma);
        mRenderer.addMouseMotionListener(ma);
        mClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorClick(null, (short) -1);
            }
        });
        mAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorAll();
            }
        });
        mPlugins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPlugin();
            }
        });
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        mColor = new JLabel("Current Color:");
        mCurrent = new JLabel("None");
        mChoice = new JLabel("Color Choices:");
        mGrey = newButton(BlockTypes.HULL_COLOR_GREY_ID);
        mBlack = newButton(BlockTypes.HULL_COLOR_BLACK_ID);
        mRed = newButton(BlockTypes.HULL_COLOR_RED_ID);
        mPurple = newButton(BlockTypes.HULL_COLOR_PURPLE_ID);
        mBlue = newButton(BlockTypes.HULL_COLOR_BLUE_ID);
        mGreen = newButton(BlockTypes.HULL_COLOR_GREEN_ID);
        mBrown = newButton(BlockTypes.HULL_COLOR_BROWN_ID);
        mYellow = newButton(BlockTypes.HULL_COLOR_YELLOW_ID);
        mWhite = newButton(BlockTypes.HULL_COLOR_WHITE_ID);
        mRadius = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
        mSymLabel = new JLabel("Paint Symmetry:");
        mRaLabel = new JLabel("Paint Radius:");
        mXSymmetry = new JCheckBox("X");
        mXSymmetry.setToolTipText("paint port/starboard");
        mYSymmetry = new JCheckBox("Y");
        mYSymmetry.setToolTipText("paint dorsal/ventral");
        mZSymmetry = new JCheckBox("Z");
        mZSymmetry.setToolTipText("paint fore/aft");
        mClear = new JButton("Clear");
        mClear.setToolTipText("Stop painting");
        mAll = new JButton("Paint All");
        mAll.setToolTipText("Set all hulls to current color");
        mPlugins = new JButton("Mods");
        mPlugins.setToolTipText("Invoke a mod");
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mXSymmetry)
                        .addPreferredGap(UNRELATED)
                        .addComponent(mYSymmetry)
                        .addPreferredGap(UNRELATED)
                        .addComponent(mZSymmetry))
                    .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(mColor)
                        .addComponent(mCurrent)
                        .addComponent(mChoice)
                        .addComponent(mGrey)
                        .addComponent(mBlack)
                        .addComponent(mRed)
                        .addComponent(mPurple)
                        .addComponent(mBlue)
                        .addComponent(mGreen)
                        .addComponent(mBrown)
                        .addComponent(mYellow)
                        .addComponent(mWhite))
                    .addComponent(mRadius, PREFERRED_SIZE, 93, PREFERRED_SIZE)
                    .addComponent(mClear)
                    .addComponent(mAll)
                    .addComponent(mSymLabel)
                    .addComponent(mRaLabel))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mColor)
                .addGap(18, 18, 18)
                .addComponent(mCurrent)
                .addPreferredGap(RELATED, 18, Short.MAX_VALUE)
                .addComponent(mChoice)
                .addPreferredGap(UNRELATED)
                .addComponent(mGrey)
                .addPreferredGap(RELATED)
                .addComponent(mBlack)
                .addPreferredGap(RELATED)
                .addComponent(mRed)
                .addPreferredGap(RELATED)
                .addComponent(mPurple)
                .addPreferredGap(RELATED)
                .addComponent(mBlue)
                .addPreferredGap(RELATED)
                .addComponent(mGreen)
                .addPreferredGap(RELATED)
                .addComponent(mBrown)
                .addPreferredGap(RELATED)
                .addComponent(mYellow)
                .addPreferredGap(RELATED)
                .addComponent(mWhite)
                .addGap(18, 18, 18)
                .addComponent(mRaLabel)
                .addPreferredGap(RELATED)
                .addComponent(mRadius, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .addComponent(mSymLabel)
                .addPreferredGap(RELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(mXSymmetry)
                    .addComponent(mYSymmetry)
                    .addComponent(mZSymmetry))
                .addPreferredGap(UNRELATED)
                .addComponent(mClear)
                .addPreferredGap(RELATED)
                .addComponent(mAll)
                .addContainerGap())
        );
    }// </editor-fold> 

    private JButton newButton(final short blockID) {
        ImageIcon rawImage = BlockTypeColors.getBlockImage(blockID);
        Image image = rawImage.getImage().getScaledInstance(26, 26,
                Image.SCALE_DEFAULT);
        final JButton btn = new JButton(new ImageIcon(image));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doColorClick(btn.getIcon(), blockID);
            }
        });
        return btn;
    }

    private void doPlugin() {
        JPopupMenu popup = new JPopupMenu();
        int classification = StarMadeLogic.getInstance().getCurrentModel().getClassification();
        List<IBlocksPlugin> plugins = StarMadeLogic.getBlocksPlugins(classification, IBlocksPlugin.SUBTYPE_PAINT);
        if (plugins.isEmpty()) {
            popup.add("no plugins");
        }

        for (IBlocksPlugin plugin : plugins) {
            BlocksPluginAction action = new BlocksPluginAction(mRenderer, plugin);
            JMenuItem men = new JMenuItem(action);
            popup.add(men);
        }
        Dimension d = mPlugins.getSize();
        popup.show(mPlugins, d.width, d.height);
    }

    private void doColorClick(Icon color, short blockID) {
        StarMadeLogic.getInstance().setSelectedBlockType(blockID);
        if (StarMadeLogic.getInstance().getSelectedBlockType() == -1) {
            mCurrent.setIcon(null);
            mCurrent.setText("None");
        } else {
            mCurrent.setIcon(color);
            mCurrent.setText("");
        }
    }

    private void doColorAll() {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0) {
            return;
        }
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Iterator<Point3i> i;
        if ((StarMadeLogic.getInstance().getSelectedLower() != null) && (StarMadeLogic.getInstance().getSelectedUpper() != null)) {
            i = new CubeIterator(StarMadeLogic.getInstance().getSelectedLower(), StarMadeLogic.getInstance().getSelectedUpper());
        } else {
            i = grid.iteratorNonNull();
        }
        colorByIterator(grid, i, false);
    }

    private void colorByIterator(SparseMatrix<Block> grid, Iterator<Point3i> i, boolean symmetric) {
        List<Point3i> coords = new ArrayList<>();
        while (i.hasNext()) {
            coords.clear();
            coords.add(i.next());
            if (symmetric) {
                if (mXSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.x != 8) {
                            coords.add(new Point3i(16 - p1.x, p1.y, p1.z));
                        }
                    }
                }
                if (mYSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.y != 8) {
                            coords.add(new Point3i(p1.x, 16 - p1.y, p1.z));
                        }
                    }
                }
                if (mZSymmetry.isSelected()) {
                    for (int j = coords.size() - 1; j >= 0; j--) {
                        Point3i p1 = coords.get(j);
                        if (p1.z != 8) {
                            coords.add(new Point3i(p1.x, p1.y, 16 - p1.z));
                        }
                    }
                }
            }
            for (Point3i c : coords) {
                paintBlock(grid, c);
            }
        }
        mRenderer.repaint();
    }

    private void paintBlock(SparseMatrix<Block> grid, Point3i coords) {
        Block block = grid.get(coords);
        if (block == null) {
            return;
        }
        short newID = BlockTypes.getColoredBlock(block.getBlockID(), StarMadeLogic.getInstance().getSelectedBlockType());
        if (newID != -1) {
            block.setBlockID(newID);
        }
    }

    private void doMouseClick(int x, int y) {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0) {
            return;
        }
        RenderPoly b = mRenderer.getTileAt(x, y);
        if (b == null) {
            return;
        }
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Point3i p = b.getPosition();
        if (p == null) {
            return;
        }
        int r = (Integer) mRadius.getValue() - 1;
        Point3i lower = new Point3i(p.x - r, p.y - r, p.z - r);
        Point3i upper = new Point3i(p.x + r, p.y + r, p.z + r);
        colorByIterator(grid, new CubeIterator(lower, upper), true);
    }
}
