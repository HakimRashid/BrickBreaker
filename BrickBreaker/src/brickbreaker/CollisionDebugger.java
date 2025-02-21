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
public class CollisionDebugger extends Rectangle {

    private int xDirec = 0;
    private int yDirec = 0;
    private final int speed = 1;

    public CollisionDebugger(int x, int y, int width, int height) {
        super(x, y, width/4, height/4);
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
            case KeyEvent.VK_S:
                setYDirection(speed);
                move();
                break;
            case KeyEvent.VK_DOWN:
                setYDirection(speed);
                move();
                break;
            case KeyEvent.VK_W:
                setYDirection(-speed);
                move();
                break;
            case KeyEvent.VK_UP:
                setYDirection(-speed);
                move();
                break;
        }
    }

    public void setXDirection(int direction) {
        xDirec = direction;
    }
    
    public void setYDirection(int direction){
        yDirec = direction;
    }

    public int getxDirection() {
        return xDirec;
    }

    public int getyDirection() {
        return yDirec;
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
            case KeyEvent.VK_S:
                setYDirection(0);
                move();
                break;
            case KeyEvent.VK_DOWN:
                setYDirection(0);
                move();
                break;
            case KeyEvent.VK_W:
                setYDirection(0);
                move();
                break;
            case KeyEvent.VK_UP:
                setYDirection(0);
                move();
                break;
        }
    }

    public void move() {
        x += xDirec;
        y += yDirec;
//        System.err.println("Debugger(" + x + ";" + y + ")");
    }

    public void drawItem(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.drawString("DEBUG MODE", 910, 20);
        graphics.setColor(Color.RED);
        graphics.fillOval(x, y, width, height);
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}
