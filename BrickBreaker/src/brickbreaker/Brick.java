/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 * @author Hakim
 */
public class Brick extends Rectangle {

    private int hp;
    private Color colour;
    private final int pwrUP;
    private boolean clicked = false;
    private boolean editMode = false;
    private boolean ereased = false;

    public Brick(int x, int y, int width, int height, int hp, int pwrUP, Color colour, boolean editMode) {
        super(x, y, width, height);
        this.hp = hp;
        this.pwrUP = pwrUP;
        this.colour = colour;
        this.editMode = editMode;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public int getHp() {
        return hp;
    }
    
    public int getPwrUP() {
        return pwrUP;
    }

    public boolean isEreased() {
        return ereased;
    }

    public void setEreased(boolean ereased) {
        this.ereased = ereased;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void mousePressed(MouseEvent me) {
        if (MouseEvent.BUTTON1 == me.getButton()) {
            clicked = true;
        }
    }

    public void drawBrick(Graphics graphics) {
        if ((clicked && editMode)  || !editMode) {
            graphics.setColor(colour);
            graphics.fillRect(x, y, width, height);
        }
    }

    public void move(int inc) {
        y += inc;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + width + ", " + height + ", " + hp + ", " + colour + ", " + pwrUP + ", " + editMode;
    }

}
