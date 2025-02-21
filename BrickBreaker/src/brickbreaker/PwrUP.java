/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Hakim
 */
public class PwrUP extends Rectangle {

    private static double YVelocity = 1;
    private final int pwrUP;
    
    public PwrUP(int x, int y, int width, int height, int pwrUP) {
        super(x, y, height, width);
        this.pwrUP = pwrUP;
    }

    public void setYVelocity(double YVelocity) {
        PwrUP.YVelocity = YVelocity;
    }

    public void move() {
        y += YVelocity;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPwrUP() {
        return pwrUP;
    }

    public void drawItem(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.fillRect(x, y, width, height);
    }
}
