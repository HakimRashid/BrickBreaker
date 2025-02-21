/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

/**
 *
 * @author Hakim
 */
public class TimeKeeper {

    private int time = 60;
    private int startTime;
    private final int pwrUP;

    public TimeKeeper(int pwrUP, int startTime) {
        this.pwrUP = pwrUP;
        this.startTime = startTime;
        if (pwrUP == -1) {
            time = 0;
        }
    }

    public int getPwrUP() {
        return pwrUP;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        String temp = "";
        switch (pwrUP) {
            case -1:
                temp = "Level Play Time: " + time;
                break;
            case 1:
                temp = "BIG PAD: " + time;
                break;
            case 2:
                temp = "3 MORE BALLS";
                break;
            case 3:
                temp = "SMALL PAD: " + time;
                break;
            case 4:
                temp = "MISSILES: " + time;
                break;
            case 5:
                temp = "PAD SPD DWN: " + time;
                break;
            case 6:
                temp = "PAD SPD UP: " + time;
                break;
            default:
        }
        return temp;
    }

}
