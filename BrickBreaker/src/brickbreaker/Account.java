/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.time.LocalTime;

/**
 *
 * @author Hakim
 */
public class Account {

    private String name;
    private int accountID;
    private String password;
    private int totalPlayTime = 0;
    private int highestScore = 0;
    private int levelsWon = 0;
    private int levelsLost = 0;
    private int playerRating;
    private int winLossRatio;
    private boolean changed;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        changed = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        changed = true;
    }

    public LocalTime getTotalPlayTime() {
        return LocalTime.of(totalPlayTime/3600, totalPlayTime/60, totalPlayTime);
    }
    
    /**
     * Converts LocalTime into seconds and saves it
     * @param totalPlayTime 
     */
    public void setTotalPlayTime(LocalTime totalPlayTime){
        this.totalPlayTime = totalPlayTime.getHour()/3600 + totalPlayTime.getMinute()/60 + totalPlayTime.getSecond();
        changed = true;
    }

    public void addTotalPlayTime(int seconds) {
        totalPlayTime += seconds;
        changed = true;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setLevelsWon(int levelsWon) {
        this.levelsWon = levelsWon;
        changed = true;
    }

    public void setLevelsLost(int levelsLost) {
        this.levelsLost = levelsLost;
        changed = true;
    }

    /**
     * Saves the high score if it is greater than the currently saved high score
     * @param highestScore 
     */
    public void setHighestScore(int highestScore) {
        if (highestScore > this.highestScore) {
            this.highestScore = highestScore;
            playerRating = (this.highestScore * levelsWon) / levelsLost;
            changed = true;
        }
    }

    public int getLevelsWon() {
        return levelsWon;
    }

    /**
     * Adds to the amount of levels won
     * @param levelsWon 
     */
    public void addLevelsWon(int levelsWon) {
        this.levelsWon += levelsWon;
        winLossRatio = this.levelsWon / levelsLost;
        playerRating = (highestScore * this.levelsWon) / levelsLost;
        changed = true;
    }

    public int getLevelsLost() {
        return levelsLost;
    }

    /**
     * Adds to the amount of levels lost
     * @param levelsLost 
     */
    public void addLevelsLost(int levelsLost) {
        this.levelsLost += levelsLost;
        winLossRatio = levelsWon / this.levelsLost;
        playerRating = (highestScore * levelsWon) / this.levelsLost;
        changed = true;
    }

    public int getPlayerRating() {
        return playerRating;
    }

    public int getWinLossRatio() {
        return winLossRatio;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
        changed = true;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public String toString() {
        return "Account{" + "name=" + name + ", accountID=" + accountID + ", password=" + password + ", totalPlayTime=" + totalPlayTime + ", highestScore=" + highestScore + ", levelsWon=" + levelsWon + ", levelsLost=" + levelsLost + ", playerRating=" + playerRating + ", winLossRatio=" + winLossRatio + '}';
    }
}
