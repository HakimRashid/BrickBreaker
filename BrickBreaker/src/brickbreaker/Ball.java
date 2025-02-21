/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.*;
import java.util.*;

/**
 *
 * @author Hakim
 */
public class Ball extends Rectangle {

    private final Random randomX;
    private int XVelocity;
    private int YVelocity;
    static int SPEED = 2;

    public Ball(int x, int y, int width, int height) {
        super(x, y, width / 4, height / 4);
        randomX = new Random();
        int XDirec = randomX.nextInt(2);
        if (XDirec >= 0) {
            XVelocity = (XDirec--) * SPEED;
        }
        if (XDirec < 0) {
            XVelocity = (XDirec++) * SPEED;
        }

        int YDirec = -1;
        if (YDirec >= 0) {
            YVelocity = (YDirec--) * SPEED;
        }
        if (YDirec < 0) {
            YVelocity = (YDirec++) * SPEED;
        }
    }

    public void setXVelocity(int XVelocity) {
        this.XVelocity = XVelocity;
    }

    public int getXVelocity() {
        return XVelocity;
    }

    public void setYVelocity(int YVelocity) {
        this.YVelocity = YVelocity;
    }

    public int getYVelocity() {
        return YVelocity;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSPEED(int SPEED) {
        Ball.SPEED = SPEED;
    }

    public void move() {
        this.x += XVelocity;
        this.y += YVelocity;
    }

    public void drawBall(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillOval(this.x, this.y, this.height, this.width);
    }

}
