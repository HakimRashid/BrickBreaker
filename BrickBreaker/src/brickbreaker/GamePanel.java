/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Hakim
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int GAME_WIDTH = 1000;
    public static final int GAME_HEIGHT = GAME_WIDTH * 5 / 9;
    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    private static final int BALL_DIAMETER = 1;
    private static final int PADDLE_HEIGHT = 25;
    private static final int PADDLE_WIDTH = 200;
    private static final int PWRUP_WIDTH = 10;
    private final Thread myThread;
    private final Random random;
    private final long createdMillis = System.currentTimeMillis();
    private Image image;
    private Paddle paddle;
    private final Ball[] ball = new Ball[100];
    private final Brick[] brick = new Brick[1000];
    private final PwrUP[] pwrUPs = new PwrUP[1000];
    private final TimeKeeper[] timeKeeper = new TimeKeeper[9];
    private final Missile[] missile = new Missile[3];
    private CollisionDebugger Debugger;
    private int numBounces;
    private int numBalls;
    private int numBricks;
    private int numPwrUPs;
    private int numLevel;
    private int numBrickMove = 10;
    private int numMissiles;
    private int numTimeKeepers = 2;
    private int score = 0;
    private int levelsWon;
    private LocalTime totalPlayTime = LocalTime.of(0, 0, 0);
    private boolean debugMode;
    private boolean gameIsRunning;
    private boolean gamePaused;
    private boolean gameOver;
    private boolean inARow;
    private boolean showPowerUpTime = true;
    private boolean missiles;
    private boolean pwrUP;
    private boolean showScore;
    private boolean threadOn;
    private boolean lastLevelWon;

    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(new KL());
        this.setPreferredSize(SCREEN_SIZE);
        myThread = new Thread(this);
        random = new Random();
    }

    /**
     * Starts a thread path in this program
     */
    public void startThread() {
        myThread.start();
        threadOn = true;
    }

    /**
     * Spawns a new ball at a specified x and y coordinate
     * @param x
     * @param y 
     */
    public void newBall(int x, int y) {
        ball[numBalls++] = new Ball(x, y, BALL_DIAMETER + 60, BALL_DIAMETER + 60);
    }
    /**
     * Spawns multiple balls at a specified x and y coordinate
     * @param x
     * @param y
     * @param numBalls 
     */
    private void spawnBalls(int x, int y, int numBalls) {
        for (int i = 0; i < numBalls; i++) {
            ball[this.numBalls++] = new Ball(x, y, BALL_DIAMETER + 60, BALL_DIAMETER + 60);
        }
        ball[this.numBalls - 1].setXVelocity(-ball[0].getXVelocity());
        ball[this.numBalls - 2].setYVelocity(-ball[0].getYVelocity());
    }

    /**
     * Spawns a new paddle
     */
    public void newPaddle() {
        paddle = new Paddle(GAME_WIDTH / 2 - PADDLE_WIDTH / 2, GAME_HEIGHT - 25, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
    /**
     * Spawns a new debugging tool that responds to user inputs
     */
    public void newDebuggerTool() {
        Debugger = new CollisionDebugger(GAME_WIDTH / 2, GAME_HEIGHT / 2, BALL_DIAMETER + 60, BALL_DIAMETER + 60);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    /**
     * Pauses the game and counts how long the game has been paused for
     * @param gamePaused 
     */
    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
        if (!gamePaused) {
            for (int i = 0; i < numTimeKeepers; i++) {
                timeKeeper[i].setStartTime(timeKeeper[1].getTime() + timeKeeper[i].getStartTime());
            }
        }
    }

    public int getScore() {
        return score;
    }

    /**
     * Draws objects on the screen and updates the screen every time the repaint() method is called
     * @param graphics
     */
    @Override
    public void paint(Graphics graphics) {
        if (gameOver || lastLevelWon) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 1000, 555);
            if (gameOver) {
                graphics.setColor(Color.RED);
                graphics.setFont(new Font("SansSerif", Font.PLAIN, 50));
                graphics.drawString("GAME OVER", GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2 + 10);
            } else {
                graphics.setColor(Color.YELLOW);
                graphics.setFont(new Font("SansSerif", Font.ITALIC, 50));
                graphics.drawString("THANK YOU FOR PLAYING!", GAME_WIDTH / 3 - 150, GAME_HEIGHT / 2 + 10);
            }
        } else {
            image = createImage(this.getWidth(), this.getHeight());
            draw(image.getGraphics());
            graphics.drawImage(image, 0, 0, this);
            if (gamePaused) {
                paintComponents(graphics);
                graphics.setColor(Color.yellow);
                graphics.drawString("Paused", 950, 20);
            }
            int pwrUPTemp = 20;
            if (numTimeKeepers > 2 && showPowerUpTime) {
                graphics.setColor(Color.yellow);
                if (debugMode) {
                    pwrUPTemp = 30;
                }
                for (int i = 2; i < numTimeKeepers; i++) {
                    pwrUPTemp += 10*i;
                    graphics.drawString(timeKeeper[i].toString() + "", 910 - timeKeeper[i].toString().length(), pwrUPTemp);
                }
            }
            if (showScore) {
                graphics.setColor(Color.yellow);
                graphics.drawString("Score: " + score, 10, 20);
            }
        }
    }

    /**
     * Runs the draw method in each class telling them to draw the selves in respect to their individual values
     * @param graphics 
     */
    private void draw(Graphics graphics) {
        try {
            for (int i = 0; i < numPwrUPs; i++) {
                pwrUPs[i].drawItem(graphics);
            }
            for (int i = 0; i < numMissiles; i++) {
                missile[i].drawMissile(graphics);
            }
            for (int i = 0; i < numBricks; i++) {
                brick[i].drawBrick(graphics);
            }
            paddle.drawPaddle(graphics);
            if (debugMode) {
                Debugger.drawItem(graphics);
            } else {
                for (int i = 0; i < numBalls; i++) {
                    ball[i].drawBall(graphics);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Tells each class to increment all their x and y values by their respective increments
     */
    private void move() {
        for (int i = 0; i < numPwrUPs; i++) {
            pwrUPs[i].move();
        }
        for (int i = 0; i < numMissiles; i++) {
            missile[i].move();
        }
        if (!debugMode) {
            if (!gamePaused) {
                paddle.move();
            }
            if (gameIsRunning && !gamePaused) {
                for (int i = 0; i < numBalls; i++) {
                    ball[i].move();
                }
            } else {
                if (!gamePaused) {
                    ball[0].setX((int) (paddle.getX() + PADDLE_WIDTH / 2));
                }
            }
        } else {
            Debugger.move();
        }
    }

    /**
     * Checks to see which objects intersecting and tells them to respond accordingly
     */
    private void checkCollision() {
        try {
            //Ball-Border collision
            if (!debugMode && !gameOver) {
                for (int i = 0; i < numBalls; i++) {
                    if (ball[i].y <= 0 || ball[i].x > GAME_WIDTH || ball[i].x < 0) {
                        ball[i].setYVelocity(-ball[i].getYVelocity());
                        if (ball[i].getXVelocity() == 0) {
                            ball[i].setXVelocity(1);
                        }
                    }
                    if (ball[i].y >= GAME_HEIGHT) {
                        deleteBalls(i);
                        if (numBalls == 0) {
                            gameOver = true;
                            repaint();
                        }
                    }
                    if (ball[i].x <= 0) {
                        ball[i].setXVelocity(-ball[i].getXVelocity());
                        if (ball[i].getYVelocity() == 0) {
                            ball[i].setYVelocity(1);
                        }
                    }
                    if (ball[i].x >= GAME_WIDTH - 25) {
                        ball[i].setXVelocity(-ball[i].getXVelocity());
                        if (ball[i].getYVelocity() == 0) {
                            ball[i].setYVelocity(1);
                        }
                    }
                }
            }
            //Ball-paddle collision
            if (debugMode) {
                if (Debugger.intersects(paddle.x, paddle.y + 1, 1, paddle.height)) {
                    Debugger.setYDirection(-Debugger.getyDirection());
                    Debugger.setXDirection(-Debugger.getxDirection());
                    System.out.println("LEFT MOST");
                    numBounces++;
                }
                if (Debugger.intersects(paddle.x, paddle.y + 1, 1 + paddle.width / 4, paddle.height)) {
                    Debugger.setYDirection(-Debugger.getyDirection());
                    Debugger.setXDirection(-Debugger.getxDirection());
                    System.out.println("LEFT");
                    numBounces++;
                }
                if (Debugger.intersects(paddle.x + paddle.width - 1, paddle.y + 1, 1, paddle.height)) {
                    Debugger.setYDirection(-Debugger.getyDirection());
                    Debugger.setXDirection(-Debugger.getxDirection());
                    System.out.println("RIGHT MOST");
                    numBounces++;
                }
                if (Debugger.intersects(paddle.x + paddle.width - paddle.width / 4 - 1, paddle.y + 1, paddle.width / 4 - 1, paddle.height)) {
                    Debugger.setYDirection(-Debugger.getyDirection());
                    Debugger.setXDirection(-Debugger.getxDirection());
                    System.out.println("RIGHT");
                    numBounces++;
                }
                if (Debugger.intersects(paddle.x + paddle.width / 4 + 1, paddle.y + 1, paddle.width - paddle.width / 2 - 1, paddle.height)) {
                    Debugger.setYDirection(-Debugger.getyDirection());
                    Debugger.setY((int) Debugger.getY() - 1);
//                System.out.println("MIDDLE");
                    numBounces++;
                }
                //Debugger-brick collision
                for (int i = 0; i < numBricks; i++) {
                    if ((brick[i].getY() + brick[i].height) >= (GAME_HEIGHT - 25)) {
                        System.err.println("Game Over");
                    }
                    if (brick[i].getHp() >= 2) {
                        if (brick[i].intersects(Debugger)) {
                            if (Debugger.getxDirection() == 0) {
                                Debugger.setXDirection(1);
                            }
                        }
                        //Top and bottom of brick
                        if (Debugger.intersects(brick[i].x + 1, brick[i].y, brick[i].width - 1, brick[i].height)) {
                            Debugger.setYDirection(-Debugger.getyDirection());
                        }
                        //Left of brick
                        if (Debugger.intersects(brick[i].x, brick[i].y, 1, brick[i].height)) {
                            Debugger.setXDirection(-Debugger.getxDirection());
                        }
                        //Right of brick
                        if (Debugger.intersects(brick[i].x + brick[i].width - 1, brick[i].y, 1, brick[i].height)) {
                            Debugger.setXDirection(-Debugger.getxDirection());
                        }
                        brick[i].setHp(brick[i].getHp() - 1);
                    } else {
                        if (brick[i].getHp() == 1) {
                            if (Debugger.intersects(brick[i])) {
                                brick[i].setHp(brick[i].getHp() - 1);
                                brick[i].setColour(Color.BLACK);
                                deleteBrick(i);
                                if (inARow) {
                                    score += 200;
                                } else {
                                    score += 100;
                                }
                                inARow = true;
                            }
                        }
                    }
                }
            } else {
                //Ball-paddle collision
                //Left most part of paddle
                for (int i = 0; i < numBalls; i++) {
                    if (ball[i].intersects(paddle.x, paddle.y + 1, 1, paddle.height)) {
                        ball[i].setX((int) ball[i].x - 1);
                        if (paddle.getXDirection() < 0 && ball[i].getYVelocity() >= 2) {
                            ball[i].setXVelocity(-ball[i].getXVelocity() - 1);
                            ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                        } else {
                            ball[i].setXVelocity(-ball[i].getXVelocity());
                            ball[i].setYVelocity(-ball[i].getYVelocity());
                        }
                        inARow = false;
                        if (gameIsRunning) {
                            numBounces++;
                        }
                    }
                    //Left part of paddle
                    if (ball[i].intersects(paddle.x, paddle.y + 1, 1 + paddle.width / 4, paddle.height)) {
                        ball[i].setY((int) ball[i].getY() - 1);
                        if (ball[i].getXVelocity() > 0) {
                            if (paddle.getXDirection() > 0 && ball[i].getYVelocity() >= 2) {
                                ball[i].setXVelocity(ball[i].getXVelocity() + 1);
                                ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                            } else {
                                ball[i].setXVelocity(-ball[i].getXVelocity());
                                ball[i].setYVelocity(-ball[i].getYVelocity());
                            }
                        } else {
                            if (paddle.getXDirection() < 0) {
                                ball[i].setYVelocity(-ball[i].getYVelocity());
                                if (ball[i].getYVelocity() >= 2) {
                                    ball[i].setXVelocity(-ball[i].getXVelocity() - 1);
                                    ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                                } else {
                                    ball[i].setXVelocity(-ball[i].getXVelocity());
                                    ball[i].setYVelocity(-ball[i].getYVelocity());
                                }
                            }
                        }
                        inARow = false;
                        if (gameIsRunning) {
                            numBounces++;
                        }
                    }
                    //Right most part of paddle
                    if (ball[i].intersects(paddle.x + paddle.width - 1, paddle.y + 1, 1, paddle.height)) {
                        ball[i].setX((int) ball[i].x + 1);
                        if (ball[i].getXVelocity() < 0) {
                            if (paddle.getXDirection() > 0 && ball[i].getYVelocity() >= 2) {
                                ball[i].setXVelocity(-ball[i].getXVelocity() - 1);
                                ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                            } else {
                                ball[i].setXVelocity(-ball[i].getXVelocity());
                                ball[i].setYVelocity(-ball[i].getYVelocity());
                            }
                        } else {
                            if (paddle.getXDirection() > 0 && ball[i].getYVelocity() >= 1) {
                                ball[i].setXVelocity(-ball[i].getXVelocity() - 1);
                                ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                            }
                        }
                        inARow = false;
                        if (gameIsRunning) {
                            numBounces++;
                        }
                    }
                    //Right part of paddle
                    if (ball[i].intersects(paddle.x + paddle.width - paddle.width / 4 - 1, paddle.y + 1, paddle.width / 4 - 1, paddle.height)) {
                        ball[i].setY((int) ball[i].getY() - 1);
                        if (ball[i].getXVelocity() < 0) {
                            if (paddle.getXDirection() < 0) {
                                ball[i].setYVelocity(-ball[i].getYVelocity());
                                if (ball[i].getYVelocity() >= 2) {
                                    ball[i].setXVelocity(ball[i].getXVelocity() - 1);
                                    ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                                }
                            } else {
                                if (ball[i].getYVelocity() >= 2 && paddle.getXDirection() > 0) {
                                    ball[i].setXVelocity(ball[i].getXVelocity() + 1);
                                    ball[i].setYVelocity(-ball[i].getYVelocity() - 1);
                                } else {
                                    ball[i].setXVelocity(-ball[i].getXVelocity());
                                    ball[i].setYVelocity(-ball[i].getYVelocity());
                                }
                            }
                        } else {
                            ball[i].setYVelocity(-ball[i].getYVelocity());
                        }
                        inARow = false;
                        if (gameIsRunning) {
                            numBounces++;
                        }
                    }
//             Middle part of paddle
                    if (ball[i].intersects(paddle.x + paddle.width / 4 + 1, paddle.y + 1, paddle.width - paddle.width / 2 - 1, paddle.height)) {
                        ball[i].setY((int) ball[i].getY() - 1);
                        if (ball[i].getXVelocity() == 0) {
                            ball[i].setXVelocity(ball[i].getXVelocity() + 1);
                            ball[i].setYVelocity(-ball[i].getYVelocity());
                        }
                        if (paddle.getXDirection() < 0) {
                            if (ball[i].getXVelocity() < 0 && ball[i].getYVelocity() >= 2) {
                                ball[i].setXVelocity(ball[i].getXVelocity() - 1);
                                ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                            } else {
                                if (paddle.getXDirection() > 0) {
                                    if (ball[i].getXVelocity() > 0 && ball[i].getYVelocity() >= 2) {
                                        ball[i].setXVelocity(-ball[i].getXVelocity() - 1);
                                        ball[i].setYVelocity(-ball[i].getYVelocity() + 1);
                                    }
                                }
                            }
                        }
                        if (paddle.getXDirection() == 0) {
                            ball[i].setYVelocity(-ball[i].getYVelocity());
                        }
                        inARow = false;
                        if (gameIsRunning) {
                            numBounces++;
                        }
                    }
                }
                //Paddle-border collision
                if (paddle.x <= 0) {
                    paddle.setX(0);
                }
                if ((paddle.x + paddle.width) >= GAME_WIDTH) {
                    paddle.setX(GAME_WIDTH - paddle.width);
                }
                //Ball-brick collision
                for (int i = 0; i < numBricks; i++) {
                    for (int j = 0; j < numBalls; j++) {
                        if ((brick[i].getY() + brick[i].height) >= (GAME_HEIGHT - 25)) {
                            gameOver = true;
                        }
                        if (brick[i].getHp() >= 2) {
                            if (brick[i].intersects(ball[j])) {
                                if (ball[j].getXVelocity() == 0) {
                                    ball[j].setXVelocity(1);
                                }
                            }
                            //Top and bottom of brick
                            if (ball[j].intersects(brick[i].x + 1, brick[i].y, brick[i].width - 1, brick[i].height)) {
                                ball[j].setYVelocity(-ball[j].getYVelocity());
                            }
                            //Left of brick
                            if (ball[j].intersects(brick[i].x, brick[i].y, 1, brick[i].height)) {
                                ball[j].setXVelocity(-ball[j].getXVelocity());
                            }
                            //Right of brick
                            if (ball[j].intersects(brick[i].x + brick[i].width - 1, brick[i].y, 1, brick[i].height)) {
                                ball[j].setXVelocity(-ball[j].getXVelocity());
                            }
                            brick[i].setHp(brick[i].getHp() - 1);
                        } else {
                            if (brick[i].getHp() == 1) {
                                if (ball[j].intersects(brick[i])) {
                                    brick[i].setHp(brick[i].getHp() - 1);
                                    deleteBrick(i);
                                    if (inARow) {
                                        score += 200;
                                    } else {
                                        score += 100;
                                    }
                                    inARow = true;
                                }
                            }
                        }
                    }
                    for (int j = 0; j < numMissiles; j++) {
                        if (missile[j].intersects(brick[i])) {
                            deleteBrick(i);
                        }
                    }
                }
            }
            for (int i = 0; i < numPwrUPs; i++) {
                if (numPwrUPs > 0) {
                    if (pwrUPs[i].intersects(paddle.x, paddle.y, paddle.width, paddle.height)) {
                        int pwrUP = pwrUPs[i].getPwrUP();
                        deletePwrUP(i);
                        if (findTimeKeeper(pwrUP) != -1) {
                            timeKeeper[searchTimeKeeper(pwrUP)].setStartTime(gameTime());
                        } else {
                            if (!(pwrUP == 2)) {
                                timeKeeper[numTimeKeepers++] = new TimeKeeper(pwrUP, gameTime());
                            }
                            pwrUPManager(true, pwrUP);
                        }
                    }
                }
                if (numPwrUPs > 0) {
                    if (pwrUPs[i].y > GAME_HEIGHT) {
                        deletePwrUP(i);
                    }
                }
            }
            for (int i = 0; i < numMissiles; i++) {
                if (missile[i].y < 0 || missile[i].y > GAME_HEIGHT) {
                    deleteMissile(i);
                }
            }
        } catch (NullPointerException e) {
        }
    }

    /**
     * Moves all the bricks down by a certain number of pixels
     * @param inc 
     */
    private void brickMove(int inc) {
        for (int i = 0; i < numBricks; i++) {
            for (int j = 0; j < inc; j++) {
                brick[i].move(1);
            }
        }
    }

    /**
     * Counts the number times the ball has bounced on the paddle and moves the bricks down after 4 bounces of the ball
     */
    private void brickPos() {
        if (numBounces == 4) {
            brickMove(numBrickMove);
            numBounces = 0;
        }
    }

    /**
     * Deletes the specified brick in the brick array and creates a power up corresponding to the power up value of the deleted brick
     * @param pos 
     */
    private void deleteBrick(int pos) {
        if (brick[pos].getPwrUP() > 0) {
            pwrUPs[numPwrUPs++] = new PwrUP(brick[pos].x + brick[pos].width / 2, brick[pos].y, PWRUP_WIDTH, PWRUP_WIDTH, brick[pos].getPwrUP());
        }
        for (int i = pos; i < numBricks; i++) {
            brick[i] = brick[i + 1];
        }
        numBricks--;
    }

    /**
     * Deletes the specified power up in the power up array
     * @param pos 
     */
    private void deletePwrUP(int pos) {
        for (int i = pos; i < numPwrUPs; i++) {
            pwrUPs[i] = pwrUPs[i + 1];
        }
        numPwrUPs--;
    }

    /**
     * Deletes the specified ball in the ball array
     * @param pos 
     */
    private void deleteBalls(int pos) {
        for (int i = pos; i < numBalls; i++) {
            ball[i] = ball[i + 1];
        }
        numBalls--;
    }

    /**
     * Deletes the specified time keeper in the time keeper array
     * @param pos 
     */
    private void deleteTimeKeeper(int pos) {
        for (int i = pos; i < numTimeKeepers; i++) {
            timeKeeper[i] = timeKeeper[i + 1];
        }
        numTimeKeepers--;
    }

    /**
     * Deletes the specified missile in the missile array
     * @param pos 
     */
    private void deleteMissile(int pos) {
        for (int i = pos; i < numMissiles; i++) {
            missile[i] = missile[i + 1];
        }
        numMissiles--;
    }

    /**
     * The run() method in gamePanel is a method that creates an alternate execution path that loops continuously whilst simultaneously allowing other sections of code in other classes to run independently (a new thread). It does this by looping every time the difference between the “JvM’s high resolution time source” in the past and currently is greater than or equal to 1(running code that the programmer wants to be looped when this statement is true), which can be done by claiming two variables(that have the current value of the “JvM’s high resolution time source”) separated by at least 4 lines of code and subtracting them on the 5th line of code, the separation allows a time difference to culminate because different lines of code take different amounts of time to be executed by the JvM, we measure this time difference to create a loop that ticks at the frequency we want, this process is repeated multiple times every second(60 times in the case below). The run method in gamePanel is designed so that approximately 60 loops happen every second.This number can be affected by many factors making it an imperfect game loop; however it gets the job done. During each iteration the method runs all the other main methods necessary for the game to run as intended.
     * This one in particular updates power up time countdowns, checks for collisions between objects, updates the position of the objects, repaints the screen to make it appear as though everything is moving as intended and checks whether the player has lost or won.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60;
        double nanoSeconds = 1000000000 / amountOfTicks;
        double delta = 0;
        while (myThread.isAlive() && !gameOver) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoSeconds;
            lastTime = now;
            if (delta >= 1) {
                //Case game won
                if (numBricks == 0 && gameIsRunning) {
                    levelsWon++;
                    numLevel++;
                    restart();
                }
                if (numBalls == 0 && !debugMode) {
                    gameOver = true;
                    repaint();
                }
                if (!gameIsRunning) {
                    timeKeeper[0] = new TimeKeeper(-1, gameTime());
                }
                totalPlayTime = totalPlayTime.plusSeconds(timeKeeper[0].getTime() - totalPlayTime.getSecond());
                if (!gamePaused) {
                    move();
                }
                missiles();
                brickPos();
                pwrUPTimeManager();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    private void restart() {
        //reset of fields necessary for a level restart
        numBounces = 0;
        numBricks = 0;
        numPwrUPs = 0;
        numBrickMove = 10;
        numMissiles = 0;
        numTimeKeepers = 2;
        debugMode = false;
        gameIsRunning = false;
        gamePaused = false;
        inARow = false;
        missiles = false;
        lastLevelWon = !scanNewLevel(numLevel);
        ball[0].setLocation(getWidth() / 2 - 10, getHeight() - 25);
        paddle.setLocation(GAME_WIDTH / 2 - PADDLE_WIDTH / 2, GAME_HEIGHT - 25);
    }

    /**
     * Creates the missiles at the edges of paddle every 5 seconds whilst the power up is enabled
     */
    private void missiles() {
        if (missiles && findTimeKeeper(4) % 5 == 0 && numMissiles == 0) {
            for (int i = 0; i < 2; i++) {
                missile[numMissiles++] = new Missile(paddle.x + paddle.width * i, paddle.y, 5, 20);
                missile[numMissiles - 1].setColor(Color.WHITE);
            }
        }
    }

    /**
     * Finds the time keeper that is timing the specified power up and returns the time of said time keeper
     * @param pwrUP
     * @return 
     */
    private int findTimeKeeper(int pwrUP) {
        for (int i = 2; i < numTimeKeepers; i++) {
            if (timeKeeper[i].getPwrUP() == pwrUP) {
                return timeKeeper[i].getTime();
            }
        }
        return -1;
    }
    
    /**
     * Finds the time keeper that is timing the specified power up and returns its position in the array
     * @param pwrUP
     * @return 
     */
    private int searchTimeKeeper(int pwrUP) {
        for (int i = 2; i < numTimeKeepers; i++) {
            if (timeKeeper[i].getPwrUP() == pwrUP) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calculates and returns the amount of time since the class was called
     * @return 
     */
    private int gameTime() {
        final long nowMillis = System.currentTimeMillis();
        return (int) ((nowMillis - this.createdMillis) / 1000);
    }

    /**
     * Updates the time of all the existing time keepers in the time keeper array
     */
    private void pwrUPTimeManager() {
        for (int i = 2; i < numTimeKeepers && !gamePaused; i++) {
            timeKeeper[i].setTime(60 + timeKeeper[i].getStartTime() - gameTime());
            if (timeKeeper[i].getTime() == 0) {
                pwrUPManager(false, timeKeeper[i].getPwrUP());
                deleteTimeKeeper(i);
            }
        }
        if (gamePaused) {
            timeKeeper[1].setTime(gameTime() - timeKeeper[1].getStartTime());
        }
        timeKeeper[0].setTime(gameTime() - timeKeeper[0].getStartTime());
    }

    /**
     * Creates the circumstances required of certain power ups
     * @param status
     * @param pwrUP 
     */
    private void pwrUPManager(boolean status, int pwrUP) {
        if (status) {
            switch (pwrUP) {
                case 1:
                    paddle.setWidth((int) (paddle.getWidth() * 2));
                    break;
                case 2:
                    spawnBalls(ball[0].x, ball[0].y, 2);
                    break;
                case 3:
                    paddle.setWidth((int) (paddle.getWidth() / 2));
                    break;
                case 4:
                    missiles = true;
                    break;
                case 5:
                    paddle.setSpeed(paddle.getSpeed() / 2);
                    break;
                case 6:
                    paddle.setSpeed(paddle.getSpeed() * 2);
                    break;
                default:
                    System.err.println("PwrUp error(gamepanel line 642)");
            }
        } else {
            switch (pwrUP) {
                case 1:
                    paddle.setWidth((int) (paddle.getWidth() / 2));
                    break;
                case 2:
                    spawnBalls(ball[0].x, ball[0].y, 2);
                    break;
                case 3:
                    paddle.setWidth((int) (paddle.getWidth() * 2));
                    break;
                case 4:
                    missiles = false;
                    break;
                case 5:
                    paddle.setSpeed(paddle.getSpeed() * 2);
                    break;
                case 6:
                    paddle.setSpeed(paddle.getSpeed() / 2);
                    break;
                default:
                    System.err.println("PwrUp error(gamepanel line 642)");
            }
        }
    }

    /**
     * Finds the specified level, returns true if it was found and false if it wasn't and creates bricks using the information provided about said level
     * @param level
     * @return 
     */
    public boolean scanNewLevel(int level) {
        boolean found = false;
        numLevel = level;
        try {
            Scanner scFile;
            scFile = new Scanner(new File("LEVELS.txt")).useDelimiter("LEVEL, ");
            while (scFile.hasNext()) {
                Scanner scLevel;
                scLevel = new Scanner(scFile.nextLine()).useDelimiter(", ");
                scLevel.next();
                if (scLevel.nextInt() == level) {
                    found = true;
                    int lines = scLevel.nextInt();
                    numBricks = lines;
                    for (int i = 0; i < lines; i++) {
                        Scanner scData;
                        scData = new Scanner(scFile.nextLine()).useDelimiter(", ");
                        int x = scData.nextInt();
                        int y = scData.nextInt();
                        int width = scData.nextInt();
                        int height = scData.nextInt();
                        int hp = scData.nextInt();
                        String colour = scData.next();
                        Color brickColour;
                        switch (colour.substring(14)) {
                            case "[r=255,g=0,b=0]":
                                brickColour = Color.RED;
                                break;
                            case "[r=0,g=255,b=0]":
                                brickColour = Color.GREEN;
                                break;
                            case "[r=0,g=0,b=255]":
                                brickColour = Color.BLUE;
                                break;
                            case "[r=255,g=0,b=255]":
                                brickColour = Color.MAGENTA;
                                break;
                            case "[r=0,g=255,b=255]":
                                brickColour = Color.CYAN;
                                break;
                            case "[r=255,g=255,b=0]":
                                brickColour = Color.YELLOW;
                                break;
                            case "[r=0,g=0,b=0]":
                                brickColour = Color.BLACK;
                                break;
                            case "[r=255,g=255,b=255]":
                                brickColour = Color.WHITE;
                                break;
                            case "[r=128,g=128,b=128]":
                                brickColour = Color.GRAY;
                                break;
                            case "[r=64,g=64,b=64]":
                                brickColour = Color.DARK_GRAY;
                                break;
                            case "[r=192,g=192,b=192]":
                                brickColour = Color.LIGHT_GRAY;
                                break;
                            case "[r=255,g=200,b=0]":
                                brickColour = Color.ORANGE;
                                break;
                            case "[r=255,g=175,b=175]":
                                brickColour = Color.PINK;
                                break;
                            default:
                                throw new AssertionError();
                        }
                        int pwrUP = scData.nextInt();
                        boolean editMode = scData.nextBoolean();
                        brick[i] = new Brick(x, y, width, height, hp, pwrUP, brickColour, editMode);
                        scData.close();
                    }
                }
                scLevel.close();
            }
            scFile.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        return found;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setNumBrickMove(int numBrickMove) {
        this.numBrickMove = numBrickMove;
    }

    public void setShowPowerUpTime(boolean showPowerUpTime) {
        this.showPowerUpTime = showPowerUpTime;
    }

    public void setShowScore(boolean showScore) {
        this.showScore = showScore;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isThreadOn() {
        return threadOn;
    }

    public boolean isLastLevelWon() {
        return lastLevelWon;
    }

    public int getLevelsWon() {
        return levelsWon;
    }

    public void setTotalPlayTime(LocalTime totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public LocalTime getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * This inner class passes key events from the user to the fields in the main class(game panel)
     */
    private class KL extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            if (debugMode) {
                Debugger.keyPressed(event);
            }
            if (!debugMode && !gamePaused) {
                paddle.keyPressed(event);
                if (event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W) {
                    gameIsRunning = true;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            if (debugMode) {
                Debugger.keyReleased(event);
            }
            if (!debugMode && !gamePaused) {
                paddle.keyReleased(event);
            }
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gamePaused = !(gamePaused && !debugMode);
                if (gamePaused) {
                    timeKeeper[1] = new TimeKeeper(-1, gameTime());
                } else {
                    for (int i = 0; i < numTimeKeepers; i++) {
                        timeKeeper[i].setStartTime(timeKeeper[1].getTime() + timeKeeper[i].getStartTime());
                    }
                }
            }
        }
    }
}
