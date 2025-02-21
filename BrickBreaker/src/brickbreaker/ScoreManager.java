/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JPanel;

/**
 *
 * @author Hakim
 */
public class ScoreManager extends JPanel implements Runnable {

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 5 / 9;
    private static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    private final long createdMillis = System.currentTimeMillis();
    private final Thread scoreThread;
    private final TimeKeeper scoreCount = new TimeKeeper(0, 0);
    private String playerName;
    private int score = 0;
    private Image image;
    private boolean done = true;
    private boolean finished;

    public ScoreManager() {
        scoreThread = new Thread(this);
        this.setLayout(null);
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(Color.BLACK);
    }

    public void startThread() {
        scoreThread.start();
    }

    /**
     * Returns the amount of time passed since the class was created in milliseconds
     * @return 
     */
    private int runTime() {
        final long nowMillis = System.currentTimeMillis();
        return (int) ((nowMillis - this.createdMillis) / 10);
    }

    /**
     * updates the score counter each time the method is called
     */
    private void timeManager() {
        if (!done) {
            scoreCount.setTime(runTime() - scoreCount.getStartTime());
            if (scoreCount.getTime() == score) {
                done = true;
            }
        }
    }

    /**
     * Skips the score counting
     */
    public void finish() {
        done = true;
        scoreCount.setTime(score);
    }

    /**
     * The run() method in gamePanel is a method that creates an alternate execution path that loops continuously whilst simultaneously allowing other sections of code in other classes to run independently (a new thread). It does this by looping every time the difference between the “JvM’s high resolution time source” in the past and currently is greater than or equal to 1(running code that the programmer wants to be looped when this statement is true), which can be done by claiming two variables(that have the current value of the “JvM’s high resolution time source”) separated by at least 4 lines of code and subtracting them on the 5th line of code, the separation allows a time difference to culminate because different lines of code take different amounts of time to be executed by the JvM, we measure this time difference to create a loop that ticks at the frequency we want, this process is repeated multiple times every second(60 times in the case below). The run method in gamePanel is designed so that approximately 60 loops happen every second.This number can be affected by many factors making it an imperfect game loop; however it gets the job done. During each iteration the method runs all the other main methods necessary for the game to run as intended.
     * This one in particular updates the timer and repaints the screen 60 times every second
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60;
        double nanoSeconds = 1000000000 / amountOfTicks;
        double delta = 0;
        double time = 0;
        while (scoreThread.isAlive()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoSeconds;
            lastTime = now;
            if (delta >= 1) {
                timeManager();
                repaint();
            }
        }
    }

    /**
     * Puts the counter on the screen and paints every detail on the JPanel including some components every time the repaint() mehtod is called this updates the screen making objects appear to be moving.
     * @param graphics 
     */
    @Override
    public void paint(Graphics graphics) {
        image = createImage(this.getWidth(), this.getHeight());
        graphics.drawImage(image, 0, 0, this);
        this.paintComponents(graphics);
        graphics.setColor(Color.yellow);
        graphics.setFont(new Font("Monospaced Bold", Font.ITALIC, 40));
        graphics.drawString("FINAL SCORE:", SCREEN_WIDTH / 3 + 85, SCREEN_HEIGHT / 2);
        graphics.drawString(scoreCount.getTime() + "", SCREEN_WIDTH / 3 + 85, (int) (SCREEN_HEIGHT / 1.8));
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Saves the scores to a file named "SCORES.txt"
     */
    public void print() {
        try {
            String temp = "";
            Scanner scFile;
            scFile = new Scanner(new File("SCORES.txt"));
            while (scFile.hasNextLine()) {
                temp += scFile.nextLine() + "\n";
            }
            FileWriter writer;
            writer = new FileWriter("SCORES.txt");
            writer.write(temp);
            writer.write(playerName + ": ");
            writer.write(score + "");
            writer.close();
            scFile.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public boolean isDone() {
        return done;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setDone(boolean done) {
        if (!done) {
            scoreCount.setStartTime(runTime());
        }
        this.done = done;
        finished = done;
    }
    
    /**
     * Returns all guest scores and names as an array of strings in order of highest score to lowest, it loops through the whole "SCORES.txt" file and selects the top 15 scores and organizes them into an array in name + score pairs. 
     * @return 
     */
    private String[] readGuestScores0() {
        int[] scores = new int[15];
        String[] names = new String[15];
        int k = 0;
        try {
            Scanner scFile = new Scanner(new File("SCORES.txt"));
            while (scFile.hasNextLine()) {
                Scanner scLine = new Scanner(scFile.nextLine()).useDelimiter(": ");
                names[k] = scLine.next();
                scores[k++] = scLine.nextInt();
                for (int i = 0; i < k - 1; i++) {
                    for (int j = 0; j < k - 1 - i; j++) {
                        if (scores[j] < scores[j + 1]) {
                            int temp = scores[j];
                            scores[j] = scores[j + 1];
                            scores[j + 1] = temp;
                            String name = names[j];
                            names[j] = names[j + 1];
                            names[j + 1] = name;
                        }
                    }
                }
                if (k == 15) {
                    k = 14;
                }
                scLine.close();
            }

            scFile.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        String[] leaderBoard0 = new String[15];
        for (int i = 0; i < 15; i++) {
            leaderBoard0[i] = names[i] + ": " + scores[i];
        }
        return leaderBoard0;
    }

    /**
     * Returns a string containing the top 15 scores in the "SCORES.txt" document numbered from 1 being the largest to 15 being the smallest in successive lines
     * @return 
     */
    public String readGuestScores() {
        String leaderBoard0 = "HIGH SCORES:\n";
        int[] scores = new int[15];
        String[] names = new String[15];
        int k = 0;
        try {
            Scanner scFile = new Scanner(new File("SCORES.txt"));
            while (scFile.hasNextLine()) {
                Scanner scLine = new Scanner(scFile.nextLine()).useDelimiter(": ");
                names[k] = scLine.next();
                scores[k++] = scLine.nextInt();
                for (int i = 0; i < k - 1; i++) {
                    for (int j = 0; j < k - 1 - i; j++) {
                        if (scores[j] < scores[j + 1]) {
                            int temp = scores[j];
                            scores[j] = scores[j + 1];
                            scores[j + 1] = temp;
                            String name = names[j];
                            names[j] = names[j + 1];
                            names[j + 1] = name;
                        }
                    }
                }
                if (k == 15) {
                    k = 14;
                }
                scLine.close();
            }

            scFile.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        for (int i = 0; i < 15; i++) {
            if (!(names[i] == null)) {
                leaderBoard0 += (i + 1) + "." + names[i] + ": " + scores[i] + "\n";
            }
        }
        return leaderBoard0;
    }

    /**
     * Sorts the top 15 account scores and the top 15 guest scores and organizes them from 1 being the largest to 30 being the smallest and returns a string containing only the top 15 amongst them in the same fashion as readGuestScores()
     * @return 
     */
    public String readAllScores() {
        String[] leaderBoard0 = readGuestScores0();
        String[] leaderBoardS = (new DataHandler()).getAccountScores();
        String[] leaderBoard1 = new String[15];
        String[] leaderBoard2 = new String[30];
        for (int i = 0; i < 15; i++) {
            leaderBoard1[i] = leaderBoardS[i];
        }
        for (int i = 0; i < 15; i++) {
            leaderBoard2[i] = leaderBoard0[i];
            leaderBoard2[i + 15] = leaderBoard1[i];
        }
        for (int i = 0; i < 30 - 1; i++) {
            for (int j = 0; j < 30 - 1 - i; j++) {
                if (leaderBoard2[j] == null || leaderBoard2[j + 1] == null) {
                    if (leaderBoard2[j + 1] == null) {
                        String temp = leaderBoard2[j + 1];
                        leaderBoard2[j + 1] = leaderBoard2[j];
                        leaderBoard2[j] = temp;
                    }
                    if (leaderBoard2[j] == null) {
                        String temp = leaderBoard2[j];
                        leaderBoard2[j] = leaderBoard2[j + 1];
                        leaderBoard2[j + 1] = temp;
                    }
                } else {
                    if (Integer.parseInt(leaderBoard2[j].substring(leaderBoard2[j].indexOf(":") + 2)) < Integer.parseInt(leaderBoard2[j + 1].substring(leaderBoard2[j + 1].indexOf(":") + 2))) {
                        String temp = leaderBoard2[j];
                        leaderBoard2[j] = leaderBoard2[j + 1];
                        leaderBoard2[j + 1] = temp;
                    }
                }
            }
        }
        String leaderBoard3 = "HIGH SCORES:\n";
        for (int i = 0; i < 15; i++) {
            if (!leaderBoard2[i].contains("null")) {
                leaderBoard3 += (i + 1) + "." + leaderBoard2[i] + "\n";
            }
        }
        return leaderBoard3;
    }
}
