/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Hakim
 */
public class Paddle extends Rectangle {

    private int xDirection = 0;
    private int speed = 10;

    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_D:
                setXDirection(speed);
                move();
                break;
            case KeyEvent.VK_RIGHT:
                setXDirection(speed);
                move();
                break;
            case KeyEvent.VK_A:
                setXDirection(-speed);
                move();
                break;
            case KeyEvent.VK_LEFT:
                setXDirection(-speed);
                move();
                break;
        }
    }

    public void setXDirection(int direction) {
        xDirection = direction;
    }

    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_D:
                setXDirection(0);
                move();
                break;
            case KeyEvent.VK_RIGHT:
                setXDirection(0);
                move();
                break;
            case KeyEvent.VK_A:
                setXDirection(0);
                move();
                break;
            case KeyEvent.VK_LEFT:
                setXDirection(0);
                move();
                break;
        }
    }

    public void move() {
        this.x += xDirection;
    }

    public void drawPaddle(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.fillRect(this.x, this.y, this.width, this.height);
    }

    public int getXDirection() {
        return xDirection;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
