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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Hakim
 */
public class AccountManager extends JPanel implements Runnable, ActionListener, MouseListener {

    public static final int MANAGER_WIDTH = 1000;
    public static final int MANAGER_HEIGHT = MANAGER_WIDTH * 5 / 9;
    private static final Dimension SCREEN_SIZE = new Dimension(MANAGER_WIDTH, MANAGER_HEIGHT);
    private final int COMPONENT_WIDTH = MANAGER_WIDTH / 10;
    private final int COMPONENT_HEIGHT = MANAGER_HEIGHT / 15;
    private final JButton[] btn = new JButton[6];
    private final JPasswordField[] password = new JPasswordField[2];
    private final JTextField username;
    private final JRadioButton rbnShowPassword;
    private final String[] cmpntName = new String[]{"Login", "Sign In", "Log Out", "LeaderBoard", "Stats", "UserName", "Password", "Re-enter password"};
    private final Thread myThread;
    private final DataHandler dataHandler;
    private Account account;
    private Image image;
    private boolean signUp;
    private boolean wrongDetails;
    private boolean usernameEmpty;
    private boolean usernameTaken;
    private boolean passwordMissMatch;
    private boolean accountScreen;
    private boolean loggedIn;

    public AccountManager() {
        dataHandler = new DataHandler();
        myThread = new Thread(this);
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setPreferredSize(SCREEN_SIZE);
        for (int i = 0; i < 2; i++) {
            this.add(btn[i] = new JButton());
            btn[i].setText(cmpntName[i]);
            btn[i].setOpaque(true);
            btn[i].setBounds(MANAGER_WIDTH / 3 + MANAGER_WIDTH / 20 + COMPONENT_WIDTH * 2 * i, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 6, COMPONENT_WIDTH, COMPONENT_HEIGHT);
            btn[i].addActionListener(this);
        }
        this.add(rbnShowPassword = new JRadioButton());
        rbnShowPassword.setText("Show Password");
        rbnShowPassword.setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34 + COMPONENT_WIDTH * 2, MANAGER_HEIGHT / 2 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 4, COMPONENT_HEIGHT / 2);
        rbnShowPassword.setOpaque(false);
        rbnShowPassword.addActionListener(this);
        rbnShowPassword.setForeground(Color.yellow);
        this.add(username = new JTextField());
        username.setBounds(MANAGER_WIDTH / 2 - MANAGER_WIDTH / 9, MANAGER_HEIGHT / 3 + MANAGER_HEIGHT / 12 + COMPONENT_HEIGHT * 2 * 0, COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
        username.addMouseListener(this);
        username.setText(cmpntName[5]);
        setUpWizard(true, false);
    }

    public void startThread() {
        myThread.start();
        myThread.setName("AccountManager");
    }

    /**
     * Draws objects on the screen and updates the screen every time the repaint() method is called.
     * Generates certain screens depending on what fields are true and false. i.e. generates the account screen if the loggedIn field is true
     * @param graphics 
     */
    @Override
    public void paint(Graphics graphics) {
        image = createImage(this.getWidth(), this.getHeight());
        graphics.drawImage(image, 0, 0, this);
        this.paintComponents(graphics);
        graphics.setColor(Color.yellow);
        if (accountScreen) {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, MANAGER_WIDTH, MANAGER_HEIGHT);
            graphics.setColor(Color.yellow);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
            graphics.drawString("Highest Score attained: " + account.getHighestScore(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9);
            graphics.drawString("Levels Lost: " + account.getLevelsLost(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 12);
            graphics.drawString("Levels Won: " + account.getLevelsWon(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 18);
            graphics.drawString("Win Loss Ratio(WLR): " + account.getWinLossRatio(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2);
            graphics.drawString("Player Rating: " + account.getPlayerRating(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 34);
            graphics.drawString("Total Play Time: " + account.getTotalPlayTime(), MANAGER_WIDTH / 3, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 18);
            graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 40));
            graphics.drawString("Welcome back " + account.getName() + "!", 440 - account.getName().length() * 12, 140);
            graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            graphics.drawString("Stats:", MANAGER_WIDTH / 3, 200);
            graphics.drawString("Change Details:", MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, 200);
            this.paintComponents(graphics);
        } else {
            username.setBounds(MANAGER_WIDTH / 2 - MANAGER_WIDTH / 9, MANAGER_HEIGHT / 3 + MANAGER_HEIGHT / 12 + COMPONENT_HEIGHT * 2 * 0, COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
            graphics.drawString("Username:", 315, 255);
            graphics.drawString("Password:", 315, 330);
            if (signUp) {
                graphics.drawString("Repeat Password:", 270, 400);
            } else {
                btn[1].setText("Sign In");
            }
            graphics.setColor(Color.red);
            if (usernameEmpty) {
                graphics.drawString("You can't leave this field empty!", 720, 255);
            } else {
                graphics.setColor(Color.black);
                graphics.fillRect(username.getX() + COMPONENT_WIDTH * 3, username.getY(), COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                graphics.setColor(Color.red);
                if (wrongDetails) {
                    graphics.drawString("Wrong username or password!", 720, 290);
                } else {
                    if (signUp) {
                        graphics.setColor(Color.black);
                        graphics.fillRect(username.getX() + COMPONENT_WIDTH * 3, username.getY(), COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                        graphics.setColor(Color.red);
                    }
                }
            }
            if (usernameTaken && !usernameEmpty) {
                graphics.drawString("This username is already taken!", 720, 255);
            } else {
                if (signUp) {
                    graphics.setColor(Color.black);
                    graphics.fillRect(username.getX() + COMPONENT_WIDTH * 3, username.getY(), COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                    graphics.setColor(Color.red);
                }
            }
            if (!loggedIn || signUp) {
                if (password[0].getText().isEmpty()) {
                    graphics.drawString("You can't leave this field empty!", 720, 340);
                } else{
                    graphics.setColor(Color.black);
                    graphics.fillRect(720, 340, 200, 25);
                    graphics.setColor(Color.red);
                }
            }
            if (passwordMissMatch && signUp && !password[0].getText().isEmpty()) {
                graphics.drawString("Passwords don't match!", 720, 400);
            } else {
                if (signUp) {
                    graphics.setColor(Color.black);
                    graphics.fillRect(password[1].getX() + COMPONENT_WIDTH * 3, password[1].getY(), COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                    graphics.setColor(Color.red);
                }
            }
            graphics.setColor(Color.yellow);
            graphics.setFont(new Font("Dialogue Bold", 40, 40));
            graphics.drawString("Account Manager", 390, 180);
        }
    }

    /**
     * The run() method in gamePanel is a method that creates an alternate execution path that loops continuously whilst simultaneously allowing other sections of code in other classes to run independently (a new thread). It does this by looping every time the difference between the “JvM’s high resolution time source” in the past and currently is greater than or equal to 1(running code that the programmer wants to be looped when this statement is true), which can be done by claiming two variables(that have the current value of the “JvM’s high resolution time source”) separated by at least 4 lines of code and subtracting them on the 5th line of code, the separation allows a time difference to culminate because different lines of code take different amounts of time to be executed by the JvM, we measure this time difference to create a loop that ticks at the frequency we want, this process is repeated multiple times every second(60 times in the case below). The run method in gamePanel is designed so that approximately 60 loops happen every second.This number can be affected by many factors making it an imperfect game loop; however it gets the job done. During each iteration the method runs all the other main methods necessary for the game to run as intended.
     * This method in particular performs data validation and repaint() 60 times every second
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60;
        double nanoSeconds = 1000000000 / amountOfTicks;
        double delta = 0;
        double time = 0;
        while (myThread.isAlive()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoSeconds;
            lastTime = now;
            if (delta >= 1) {
                if (signUp) {
                    passwordVerification();
                    usernameVerification();
                    wrongDetails = false;
                }
                if (loggedIn) {
                    if (account.isChanged()) {
                        dataHandler.updateAccount(account, account.getName(), account.getPassword());
                        account.setChanged(false);
                    }
                }
                repaint();
            }
        }
    }

    /**
     * Helps in JPasswordField placement
     * @param login
     * @param remove 
     */
    private void setUpWizard(boolean login, boolean remove) {
        if (!remove) {
            if (login) {
                this.add(password[0] = new JPasswordField());
                password[0].setEchoChar((char) 0);
                password[0].setBounds(MANAGER_WIDTH / 2 - MANAGER_WIDTH / 9, MANAGER_HEIGHT / 3 + MANAGER_HEIGHT / 12 + COMPONENT_HEIGHT * 2 * 1, COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                password[0].setText(cmpntName[6]);
                password[0].addMouseListener(this);
            } else {
                for (int i = 0; i < 2; i++) {
                    this.add(password[i] = new JPasswordField());
                    password[i].setEchoChar((char) 0);
                    password[i].setBounds(MANAGER_WIDTH / 2 - MANAGER_WIDTH / 9, MANAGER_HEIGHT / 3 + MANAGER_HEIGHT / 12 + COMPONENT_HEIGHT * 2 * (i + 1), COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
                    password[i].setText(cmpntName[i + 6]);
                    password[i].addMouseListener(this);
                }
            }
        } else {
            if (login) {
                this.remove(password[0]);
            } else {
                for (int i = 0; i < 2; i++) {
                    this.remove(password[i]);
                }
            }
        }
    }

    /**
     * Performs certain functions if certain buttons are pressed. i.e. Setting fields to true and false and logging in and creating accounts
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (rbnShowPassword.isSelected()) {
            password[0].setEchoChar((char) 0);
        } else {
            password[0].setEchoChar('\u2022');
        }
        if (ae.getSource().equals(btn[0])) {
            usernameVerification();
            if (!signUp) {
                Account account = dataHandler.checkLogin(username.getText(), password[0].getText());
                if (account != null) {
                    if (dataHandler.searchUser(username.getText())) {
                        System.err.println("access granted");
                        loggedIn = true;
                    }
                    this.account = account;
                    this.add(btn[2] = new JButton());
                    btn[2].setText(cmpntName[2]);
                    btn[2].addActionListener(this);
                    btn[2].setBounds(MANAGER_WIDTH / 2 - COMPONENT_WIDTH / 2, MANAGER_HEIGHT / 2 + COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                    accountScreen = true;
                    wrongDetails = false;
                    if (loggedIn) {
                        this.remove(btn[0]);
                        btn[1].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34 + COMPONENT_WIDTH + COMPONENT_WIDTH / 4, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT);
                        btn[1].setText("Change");
                        username.setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                        username.setText(cmpntName[5]);
                        rbnShowPassword.setVisible(false);
                        setUpWizard(true, true);
                        setUpWizard(false, false);
                        password[0].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                        password[1].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT * 2, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                    }
                } else {
                    if (dataHandler.searchUser(username.getText())) {
                        System.err.println("access denied");
                    }
                    wrongDetails = true;
                }
            } else {
                setUpWizard(false, true);
                setUpWizard(true, false);
                for (int i = 0; i < 2; i++) {
                    btn[i].setBounds(MANAGER_WIDTH / 3 + MANAGER_WIDTH / 20 + COMPONENT_WIDTH * 2 * i, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 6, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                }
                signUp = false;
            }
        }
        if (ae.getSource().equals(btn[1]) && !accountScreen) {
            if (!signUp) {
                setUpWizard(true, true);
                setUpWizard(false, false);
                for (int i = 0; i < 2; i++) {
                    btn[i].setBounds(MANAGER_WIDTH / 3 + MANAGER_WIDTH / 20 + COMPONENT_WIDTH * 2 * i, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 3, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                }
                btn[1].setText("Sign Up");
                signUp = true;
            } else {
                if (passwordVerification() && usernameVerification()) {
                    dataHandler.signIn(account = new Account(username.getText(), password[1].getText()));
                    this.add(btn[2] = new JButton());
                    btn[2].setText(cmpntName[2]);
                    btn[2].addActionListener(this);
                    btn[2].setBounds(MANAGER_WIDTH / 2 - COMPONENT_WIDTH / 2, MANAGER_HEIGHT / 2 + COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                    btn[1].setVisible(false);
                    btn[0].setVisible(false);
                    signUp = false;
                    loggedIn = true;
                    rbnShowPassword.setVisible(false);
                    if (loggedIn) {
                        this.remove(btn[0]);
                        btn[1].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34 + COMPONENT_WIDTH + COMPONENT_WIDTH / 4, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT);
                        btn[1].setText("Change");
                        username.setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                        username.setText(cmpntName[5]);
                        rbnShowPassword.setVisible(false);
                        setUpWizard(false, true);
                        setUpWizard(false, false);
                        password[0].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                        password[1].setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34, MANAGER_HEIGHT / 2 - MANAGER_HEIGHT / 9 + COMPONENT_HEIGHT * 2, COMPONENT_WIDTH + COMPONENT_WIDTH / 6, COMPONENT_HEIGHT / 2);
                    }
                    accountScreen = true;
                }
            }
        }
        if (ae.getSource().equals(btn[1]) && accountScreen) {
            if (passwordVerification() && usernameVerification()) {
                String oldName = account.getName();
                String oldPassword = account.getPassword();
                account.setName(username.getText());
                account.setPassword(password[0].getText());
                dataHandler.updateAccount(account, oldName, oldPassword);
            }
        }
        if (ae.getSource().equals(btn[2])) {
            loggedIn = false;
            accountScreen = false;
            this.add(rbnShowPassword);
            setUpWizard(false, true);
            this.remove(btn[1]);
            this.remove(btn[2]);
            rbnShowPassword.setVisible(true);
            for (int i = 0; i < 2; i++) {
                this.add(btn[i] = new JButton());
                btn[i].setText(cmpntName[i]);
                btn[i].setOpaque(true);
                btn[i].setBounds(MANAGER_WIDTH / 3 + MANAGER_WIDTH / 20 + COMPONENT_WIDTH * 2 * i, MANAGER_HEIGHT / 2 + MANAGER_HEIGHT / 6, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                btn[i].addActionListener(this);
            }
            rbnShowPassword.setText("Show Password");
            rbnShowPassword.setBounds(MANAGER_WIDTH / 2 + MANAGER_WIDTH / 34 + COMPONENT_WIDTH * 2, MANAGER_HEIGHT / 2 + COMPONENT_HEIGHT, COMPONENT_WIDTH + COMPONENT_WIDTH / 4, COMPONENT_HEIGHT / 2);
            rbnShowPassword.setOpaque(false);
            rbnShowPassword.addActionListener(this);
            rbnShowPassword.setForeground(Color.yellow);
            this.add(username);
            username.setBounds(MANAGER_WIDTH / 2 - MANAGER_WIDTH / 9, MANAGER_HEIGHT / 3 + MANAGER_HEIGHT / 12 + COMPONENT_HEIGHT * 2 * 0, COMPONENT_WIDTH * 3, COMPONENT_HEIGHT);
            username.addMouseListener(this);
            username.setText(cmpntName[5]);
            setUpWizard(true, false);
        }
    }

    /**
     * Performs data validation on passwords
     * @return 
     */
    private boolean passwordVerification() {
        passwordMissMatch = !password[0].getText().equals(password[1].getText());
        return !(passwordMissMatch || password[0].getText().isEmpty());
    }

    /**
     *Performs data validation on usernames
     * @return 
     */
    private boolean usernameVerification() {
        usernameEmpty = username.getText().isEmpty();
        if (signUp) {
            usernameTaken = dataHandler.searchUser(username.getText());
        }
        return !(usernameEmpty || usernameTaken);
    }

    public boolean getStatus() {
        return loggedIn;
    }

    public Account getAccount() {
        return account;
    }

    /**
     * When the mouse is clicked on certain text fields for the first time, the field is cleared automaitcally
     * @param me 
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource().equals(password[0]) && !(password[0].getEchoChar() == '\u2022') && !rbnShowPassword.isSelected()) {
            password[0].setText(null);
            password[0].setEchoChar('\u2022');
        }
        if (me.getSource().equals(password[1]) && !(password[1].getEchoChar() == '\u2022') && !rbnShowPassword.isSelected()) {
            password[1].setText(null);
            password[1].setEchoChar('\u2022');
        }
        if (me.getSource().equals(username) && username.getText().equals("UserName")) {
            username.setText(null);
        }

    }

    @Override
    public void mousePressed(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
