/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author Hakim
 */
public class LevelCreatorWizard extends JPanel implements MouseListener {

    public static final int WIZARD_WIDTH = 1000 + 100 + 100 * 3 / 2;
    public static final int WIZARD_HEIGHT = 1000 * 5 / 9;
    private static final Dimension SCREEN_SIZE = new Dimension(WIZARD_WIDTH, WIZARD_HEIGHT);
    private int brickHEIGHT = 20;
    private int brickWIDTH = 40;
    private int brickHP;
    private int brickPwrUP;
    private Color brickCOLOUR;
    private Image image;
    private final Brick[] brick = new Brick[WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28))];
    private int numBricks = 0;
    private int numLevels = 0;
    private boolean invalidDataType = true;
    private boolean mouseClicked;
    private final int COMPONENT_WIDTH = 1000 / 10;
    private final int COMPONENT_HEIGHT = WIZARD_HEIGHT / 15;
    private static JLabel scratchPad;
    private final JTextField txtBrickHP;
    private final JTextField txtBrickWidth;
    private final JTextField txtBrickHeight; 
    private final JComboBox cbBrickColour;
    private final JComboBox cbBrickPwrUP;
    private final JRadioButton rbnAddGrid;
    private final JRadioButton rbnLayoutAssist;
    private final JRadioButton rbnEreaser;

    public LevelCreatorWizard() {
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.add(scratchPad = new JLabel());
        scratchPad.addMouseListener(this);
        scratchPad.setBounds(0, 0, WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT);
        scratchPad.setBackground(Color.BLACK);
        scratchPad.setOpaque(true);
        this.add(txtBrickHP = new JTextField());
        txtBrickHP.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 2, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        txtBrickHP.setOpaque(true);
        txtBrickHP.setText("0");
        txtBrickHP.addMouseListener(this);
        this.add(txtBrickWidth = new JTextField());
        txtBrickWidth.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 3, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        txtBrickWidth.setOpaque(true);
        txtBrickWidth.setText(brickWIDTH + "");
        txtBrickWidth.addMouseListener(this);
        this.add(txtBrickHeight = new JTextField());
        txtBrickHeight.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 4, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        txtBrickHeight.setOpaque(true);
        txtBrickHeight.setText(brickHEIGHT + "");
        txtBrickHeight.addMouseListener(this);
        this.add(cbBrickColour = new JComboBox());
        cbBrickColour.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 5, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        cbBrickColour.setOpaque(true);
        cbBrickColour.setModel(new DefaultComboBoxModel(new String[]{"RED", "GREEN", "BLUE", "MAGENTA", "CYAN", "YELLOW", "BLACK", "WHITE", "GRAY", "DARK_GRAY", "LIGHT_GRAY", "ORANGE", "PINK"}));
        cbBrickColour.addMouseListener(this);
        this.add(cbBrickPwrUP = new JComboBox());
        cbBrickPwrUP.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 6, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        cbBrickPwrUP.setOpaque(true);
        cbBrickPwrUP.setModel(new DefaultComboBoxModel(new String[]{"NONE", "BIG PADDLE", "3 MORE", "SMALL PADDLE", "MISSILES", "PADDLE DWN", "PADDLE UP"}));
        this.add(rbnAddGrid = new JRadioButton());
        rbnAddGrid.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 7, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        rbnAddGrid.setOpaque(false);
        rbnAddGrid.setForeground(Color.WHITE);
        rbnAddGrid.setText("Add Grid");
        rbnAddGrid.addMouseListener(this);
        this.add(rbnLayoutAssist = new JRadioButton());
        rbnLayoutAssist.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 8, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        rbnLayoutAssist.setOpaque(false);
        rbnLayoutAssist.setForeground(Color.WHITE);
        rbnLayoutAssist.setText("Layout Assist");
        rbnLayoutAssist.setVisible(false);
        rbnLayoutAssist.addMouseListener(this);
        this.add(rbnEreaser = new JRadioButton());
        rbnEreaser.setBounds(WIZARD_WIDTH - COMPONENT_WIDTH, WIZARD_HEIGHT - COMPONENT_HEIGHT * 9, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        rbnEreaser.setOpaque(false);
        rbnEreaser.setForeground(Color.WHITE);
        rbnEreaser.setText("Ereaer");
        rbnEreaser.addMouseListener(this);
    }

    /**
     * Passes the values from each of the user input components into the variable fields after performing data validation
     */
    public void initializeValues() {
        boolean dataTypeValid = false;
        boolean dataTypeValid1 = false;
        boolean dataTypeValid2 = false;
        try {
            if (Integer.parseInt(txtBrickHP.getText() + "") > 0) {
                brickHP = Integer.parseInt(txtBrickHP.getText() + "");
                dataTypeValid = true;
            } else {
                txtBrickHP.setText("0");
                invalidDataType = true;
            }
            if (Integer.parseInt(txtBrickWidth.getText() + "") >= 40) {
                brickWIDTH = Integer.parseInt(txtBrickWidth.getText() + "");
                dataTypeValid1 = true;
            } else {
                if (Integer.parseInt(txtBrickWidth.getText() + "") == 0) {
                    txtBrickWidth.setText(brickWIDTH + "");
                    dataTypeValid1 = true;
                } else {
                    txtBrickWidth.setText("0");
                    invalidDataType = true;
                }
            }
            if (Integer.parseInt(txtBrickHeight.getText() + "") >= 20) {
                brickHEIGHT = Integer.parseInt(txtBrickHeight.getText() + "");
                dataTypeValid2 = true;
            } else {
                if (Integer.parseInt(txtBrickHeight.getText() + "") == 0) {
                    txtBrickHeight.setText(brickHEIGHT + "");
                    dataTypeValid2 = true;
                } else {
                    txtBrickHeight.setText("0");
                    invalidDataType = true;
                }
            }
            if (dataTypeValid && dataTypeValid1 && dataTypeValid2) {
                invalidDataType = false;
            }
        } catch (NumberFormatException e) {
        }
        switch (cbBrickColour.getSelectedIndex() + 1) {
            case 1:
                brickCOLOUR = Color.RED;
                break;
            case 2:
                brickCOLOUR = Color.GREEN;
                break;
            case 3:
                brickCOLOUR = Color.BLUE;
                break;
            case 4:
                brickCOLOUR = Color.MAGENTA;
                break;
            case 5:
                brickCOLOUR = Color.CYAN;
                break;
            case 6:
                brickCOLOUR = Color.YELLOW;
                break;
            case 7:
                brickCOLOUR = Color.BLACK;
                break;
            case 8:
                brickCOLOUR = Color.WHITE;
                break;
            case 9:
                brickCOLOUR = Color.GRAY;
                break;
            case 10:
                brickCOLOUR = Color.DARK_GRAY;
                break;
            case 11:
                brickCOLOUR = Color.LIGHT_GRAY;
                break;
            case 12:
                brickCOLOUR = Color.ORANGE;
                break;
            case 13:
                brickCOLOUR = Color.PINK;
                break;
            default:
        }
        brickPwrUP = cbBrickPwrUP.getSelectedIndex();
    }

    /**
     * Draws objects and components on the screen and updates the screen every time the repaint() method is called.
     * This method in particular is special in that it creates bricks at the position of the mouse each time it is clicked
     * @param graphics 
     */
    @Override
    public void paint(Graphics graphics) {
        image = createImage(this.getWidth(), this.getHeight());
        graphics.drawImage(image, 0, 0, this);
        graphics.setColor(Color.yellow);
        graphics.drawString("HP", WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT - COMPONENT_HEIGHT * 4 / 3);
        graphics.drawString("Width", WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT - COMPONENT_HEIGHT * 7 / 3);
        graphics.drawString("Height", WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT - COMPONENT_HEIGHT * 10 / 3);
        graphics.drawString("Colour", WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT - COMPONENT_HEIGHT * 13 / 3);
        graphics.drawString("Effect", WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, WIZARD_HEIGHT - COMPONENT_HEIGHT * 16 / 3);
        this.paintComponents(graphics);
        if (rbnAddGrid.isSelected()) {
            graphics.setColor(Color.YELLOW);
            for (int i = 0; i < WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)); i++) {
                graphics.drawLine(brickWIDTH * i, 0, brickWIDTH * i, WIZARD_HEIGHT);
            }
            for (int i = 0; i < WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28)); i++) {
                graphics.drawLine(0, brickHEIGHT * i, WIZARD_WIDTH - COMPONENT_WIDTH * 3 / 2, brickHEIGHT * i);
            }
        }
        if (!invalidDataType) {
            if (rbnAddGrid.isSelected()) {
                if (rbnLayoutAssist.isSelected()) {
                    try {
                        if (scratchPad.contains(this.getMousePosition()) && !rbnEreaser.isSelected()) {
                            if (mouseClicked && numBricks < WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28)) + 1) {
                                brick[numBricks++] = new Brick(scratchPad.getMousePosition().x - scratchPad.getMousePosition().x % brickWIDTH, scratchPad.getMousePosition().y - scratchPad.getMousePosition().y % brickHEIGHT, brickWIDTH, brickHEIGHT, brickHP, brickPwrUP, brickCOLOUR, false);
                            }
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }
            try {
                if (!rbnLayoutAssist.isSelected() && scratchPad.contains(this.getMousePosition()) && !rbnEreaser.isSelected()) {
                    if (mouseClicked && numBricks < WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28)) + 1) {
                        brick[numBricks++] = new Brick(scratchPad.getMousePosition().x - brickWIDTH / 2, scratchPad.getMousePosition().y - brickHEIGHT / 2, brickWIDTH, brickHEIGHT, brickHP, brickPwrUP, brickCOLOUR, false);
                    }
                }
            } catch (NullPointerException e) {
            }
            for (int i = 0; i < numBricks; i++) {
                if (!brick[i].isEreased()) {
                    brick[i].drawBrick(graphics);
                }
            }
        } else {
            graphics.setColor(Color.RED);
            graphics.drawString("Invalid Brick Data", WIZARD_WIDTH - COMPONENT_WIDTH * 4 / 3, WIZARD_HEIGHT - COMPONENT_HEIGHT * 29 / 3);
        }
        graphics.setColor(Color.YELLOW);
        graphics.drawString("Bricks Placed: " + numBricks + "/609", WIZARD_WIDTH - COMPONENT_WIDTH * 4 / 3, WIZARD_HEIGHT - COMPONENT_HEIGHT * 27 / 3);
        mouseClicked = false;
    }

    /**
     * Helps in the erasing of bricks. It checks to see if the cursor intersects any of the bricks and returns which brick(s) the cursor is intersecting
     * @return 
     */
    private int checkCollisions() {
        for (int i = 0; i < numBricks; i++) {
            try {
                if (scratchPad.contains(this.getMousePosition())) {
                    if (brick[i].intersects(scratchPad.getMousePosition().x, scratchPad.getMousePosition().y, 20, 1)) {
                        return i;
                    }
                }
            } catch (NullPointerException e) {
            }
        }
        return WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28)) + 1;
    }

    /**
     * Deletes the specified brick in the brick array
     * @param pos 
     */
    private void delete(int pos) {
        for (int i = pos; i < numBricks; i++) {
            brick[i] = brick[i + 1];
        }
        numBricks--;
    }

    /**
     * repaints() the screen every time the mouse is clicked or deletes a brick every time the mouse is clicked on a certain brick and eraser mode is enabled 
     * @param me 
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (rbnAddGrid.isSelected()) {
            rbnLayoutAssist.setVisible(true);
        }
        if (!rbnAddGrid.isSelected()) {
            rbnLayoutAssist.setVisible(false);
            rbnLayoutAssist.setSelected(false);
        }
        if (rbnEreaser.isSelected()) {
            int numBrick = checkCollisions();
            if (numBrick < WIZARD_WIDTH / (brickWIDTH + brickWIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (brickHEIGHT - brickHEIGHT / (WIZARD_HEIGHT / 28))) {
                brick[numBrick].mousePressed(me);
                brick[numBrick].setEditMode(true);
                if (rbnEreaser.isSelected()) {
                    brick[numBrick].setEreased(true);
                    delete(numBrick);
                }
            }
        }
        initializeValues();
        repaint();
        mouseClicked = true;
    }

    /**
     * Saves the level into LEVELS.txt
     */
    public void finish() {
        if (!(numBricks == 0)) {
            numLevels = find();
            try {
                String temp = "";
                Scanner scFile;
                scFile = new Scanner(new File("LEVELS.txt"));
                while (scFile.hasNextLine()) {
                    temp += scFile.nextLine() + "\n";
                }
                numLevels++;
                FileWriter writer;
                writer = new FileWriter("LEVELS.txt");
                writer.write(temp);
                writer.write("LEVEL, " + numLevels + ", " + numBricks + "\n");
                for (int i = 0; i < numBricks; i++) {
                    if (!brick[i].isEreased()) {
                        writer.write(brick[i].toString() + "\n");
                    }
                }
                writer.close();
                scFile.close();
            } catch (IOException e) {
                System.err.println("LEVELS.txt deleted: " + e);
                System.exit(0);
            }
        }
    }

    /**
     * Finds the number of the last level created and increments to get the number of the current level being created
     * @return 
     */
    private int find() {
        int level = 0;
        try {
            Scanner scFile;
            scFile = new Scanner(new File("LEVELS.txt")).useDelimiter("LEVEL, ");
            while (scFile.hasNext()) {
                Scanner scLevel;
                scLevel = new Scanner(scFile.next()).useDelimiter(", ");
                level = scLevel.nextInt();
                scLevel.next();
            }
        } catch (Exception e) {
            System.err.println("No levels found(LevelCreatorWizard line 381): " + e);
            level = 0;
        }
        return level;
    }

    @Override
    public void mousePressed(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
