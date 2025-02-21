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
public class Missile extends Rectangle{
    
    private int speed = -10;
    private Color color;
    
    public Missile(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    public void move(){
        y += speed;
    }
    
    public void drawMissile(Graphics graphics){
        graphics.setColor(color);
        graphics.fillOval(x, y, width, height);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
