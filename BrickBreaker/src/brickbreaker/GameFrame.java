/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Hakim
 */
public class GameFrame extends JFrame implements ActionListener, MouseListener, Runnable {

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 555;
    private static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    private final int COMPONENT_WIDTH = SCREEN_WIDTH / 10;
    private final int COMPONENT_HEIGHT = SCREEN_HEIGHT / 15;
    private final JButton[] btn = new JButton[116];
    private String[] btnName = new String[10];
    private final javax.swing.JPanel[] panel = new javax.swing.JPanel[6];
    private final String[] difficulty = new String[]{"easy", "medium", "hard"};
    private int numLevels;
    private boolean debugMode;
    private boolean showTimers = true;
    private boolean showScore;
    private boolean gameHasStarted;
    private final static CardLayout CL = new CardLayout();
    private boolean loggedIn;
    private boolean levelJmp;
    private int levelNo;
    private boolean gamePaused;
    private int numDiff;

    public GameFrame() {
        initComponents();
        myThread.start();
        setVisible(true);
    }

    /**
     * Uses action listeners and a CardLayout to transition between JPanels every time specific buttons are pressed
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(btn[2]) && !levelJmp) {
            gamePanel.requestFocus();
            CL.show(panel[0], "GamePanel");
            if (txtName.getText().equalsIgnoreCase("") || txtName.getText().equalsIgnoreCase("Enter Name Here")) {
                scoreManager.setPlayerName("Guest" + ((int) (new Random()).nextInt(999)));
            } else {
                scoreManager.setPlayerName(txtName.getText());
            }
            if (!gameHasStarted) {
                panel[0].setPreferredSize(gamePanel.getPreferredSize());
                pack();
                gamePanel.scanNewLevel(1);
                gamePanel.newPaddle();
                if (debugMode) {
                    gamePanel.setDebugMode(true);
                    gamePanel.newDebuggerTool();
                } else {
                    gamePanel.setDebugMode(false);
                    gamePanel.newBall(gamePanel.getWidth() / 2 - 10, gamePanel.getHeight() - 25);
                }
                gamePanel.setShowPowerUpTime(showTimers);
                if (!gamePanel.isThreadOn()) {
                    gamePanel.startThread();
                }
                gameHasStarted = true;
            }
        }
        for (int i = 1; i <= numLevels; i++) {
            if (ae.getSource().equals(btn[i + 16])) {
                if (!loggedIn) {
                    levelJmp = true;
                    levelNo = i + 16;
                    CL.show(panel[0], "NamePanel");
                    setSize(SCREEN_SIZE);
                } else {
                    if (gamePanel.scanNewLevel(Integer.parseInt(btn[i + 16].getText()))) {
                        gamePanel.requestFocus();
                        gamePanel.setTotalPlayTime(accountManager.getAccount().getTotalPlayTime());
                        CL.show(panel[0], "GamePanel");
                        panel[0].setPreferredSize(gamePanel.getPreferredSize());
                        pack();
                        gamePanel.newPaddle();
                        if (debugMode) {
                            gamePanel.newDebuggerTool();
                        } else {
                            gamePanel.newBall(gamePanel.getWidth() / 2 - 10, gamePanel.getHeight() - 25);
                        }
                        if (!gamePanel.isThreadOn()) {
                            gamePanel.startThread();
                        }
                        gameHasStarted = true;
                        levelJmp = false;
                    }
                }
            }
        }
        if (ae.getSource().equals(btn[2]) && levelJmp) {
            if (gamePanel.scanNewLevel(Integer.parseInt(btn[levelNo].getText()))) {
                CL.show(panel[0], "GamePanel");
                if (txtName.getText().equalsIgnoreCase("") || txtName.getText().equalsIgnoreCase("Enter Name Here")) {
                    scoreManager.setPlayerName("Guest" + ((int) (new Random()).nextInt(999)));
                } else {
                    scoreManager.setPlayerName(txtName.getText());
                }
                panel[0].setPreferredSize(gamePanel.getPreferredSize());
                pack();
                gamePanel.newPaddle();
                if (debugMode) {
                    gamePanel.newDebuggerTool();
                } else {
                    gamePanel.newBall(gamePanel.getWidth() / 2 - 10, gamePanel.getHeight() - 25);
                }
                if (!gamePanel.isThreadOn()) {
                    gamePanel.startThread();
                }
                gameHasStarted = true;
                levelJmp = false;
            }
        }
        if (ae.getSource().equals(btn[6])) {
            CL.show(panel[0], "LevelCreator");
            panel[0].setPreferredSize(levelCreator.getPreferredSize());
            pack();
        }
        if (ae.getSource().equals(btnOK)) {
            CL.show(panel[0], "TitleScreen");
            panel[0].setSize(panel[1].getSize());
            if (!loggedIn) {
                scoreManager.print();
            }
            pack();
        }
        if (ae.getSource().equals(btn[1])) {
            CL.show(panel[0], "TitleScreen");
            setSize(SCREEN_SIZE);
            levelCreator.finish();
            initializeLevels();
        }
        if (ae.getSource().equals(btn[8])) {
            if (debugMode) {
                debugMode = false;
                btn[8].setText(btn[8].getText().substring(0, 11) + "OFF");
            } else {
                debugMode = true;
                btn[8].setText(btn[8].getText().substring(0, 11) + "ON");
            }
            gamePanel.setDebugMode(debugMode);
        }
        if (ae.getSource().equals(btn[11])) {
            numDiff++;
            if (numDiff == 3) {
                numDiff = 0;
            }
            btn[11].setText(btn[11].getText().substring(0, btn[11].getText().indexOf(":") + 2) + difficulty[numDiff]);
            gamePanel.setNumBrickMove(10 * (numDiff + 1));
        }
        if (ae.getSource().equals(btn[9])) {
            if (showScore) {
                showScore = false;
                btn[9].setText(btn[9].getText().substring(0, 12) + "OFF");
            } else {
                showScore = true;
                btn[9].setText(btn[9].getText().substring(0, 12) + "ON");
            }
            gamePanel.setShowScore(showScore);
        }
        if (ae.getSource().equals(btn[10])) {
            if (showTimers) {
                showTimers = false;
                btn[10].setText(btn[10].getText().substring(0, 13) + "OFF");
            } else {
                showTimers = true;
                btn[10].setText(btn[10].getText().substring(0, 13) + "ON");
            }
            gamePanel.setShowPowerUpTime(showTimers);
        }
        if (ae.getSource().equals(btn[12]) || ae.getSource().equals(btn[3]) || ae.getSource().equals(btn[7])) {
            if (gameHasStarted && !ae.getSource().equals(btn[7])) {
                CL.show(panel[0], "GamePanel");
            } else {
                gameHasStarted = false;
                panel[0].add("GamePanel", gamePanel = new GamePanel());
                CL.show(panel[0], "TitleScreen");
                setSize(SCREEN_SIZE);
            }
        }
        if (ae.getSource().equals(btn[5])) {
            CL.show(panel[0], "TitleScreen");
            setSize(SCREEN_SIZE);
        }
        if (ae.getSource().equals(btn[3])) {
            gamePanel.setGamePaused(false);
            gamePanel.remove(btn[3]);
            gamePanel.remove(btn[0]);
            gamePanel.remove(btn[7]);

        }
        if (ae.getSource().equals(btn[13])) {
            CL.show(panel[0], "TitleScreen");
        }
        if (ae.getSource().equals(btn[0])) {
            CL.show(panel[0], "Options");
        }
    }

    /**
     * Auto generates levels in a JPanel that correspond to the number of levels to allow for level selection.
     */
    private void initializeLevels() {
        numLevels = 0;
        try {
            Scanner scFile;
            scFile = new Scanner(new File("LEVELS.txt")).useDelimiter("LEVEL, ");
            while (scFile.hasNext()) {
                Scanner scLevel;
                scLevel = new Scanner(scFile.next()).useDelimiter(", ");
                scLevel.nextInt();
                scLevel.next();
                numLevels++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Format Error LEVELS.txt(GameFrame line 277): " + e);
            numLevels = 0;
        }
        int numLoops = 1;
        int numJLoops = 1;
        for (int i = 1; i <= (int) Math.ceil((double) numLevels / 3); i++) {
            if (numLevels - numLoops >= 3) {
                numJLoops = 3;
            } else {
                numJLoops = numLevels - numLoops + 1;
            }
            for (int j = 1; j <= numJLoops; j++) {
                panel[5].add(btn[numLoops + 16] = new JButton());
                btn[numLoops + 16].setBounds(COMPONENT_WIDTH * 2 * j, COMPONENT_HEIGHT * 2 * i, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                btn[numLoops + 16].setText("" + numLoops);
                btn[numLoops + 16].addActionListener(this);
                btn[numLoops + 16].setOpaque(true);
                btn[numLoops + 16].setBackground(Color.BLACK);
                btn[numLoops + 16].setForeground(Color.YELLOW);
                numLoops++;
            }
        }
        if (numLevels == 99) {
            btn[6].setVisible(false);
        }
        if (numLevels > 0) {
            panel[5].setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH, btn[numLevels + 16].getY() + COMPONENT_HEIGHT * 2));
        } else {
            panel[5].setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH, btn[6].getY() + COMPONENT_HEIGHT * 2));
        }
    }

    /**
     * Initializes all the components. i.e. buttons and JPanels. It also adds them to other JPanels to allow for smooth transitions
     */
    private void initComponents() {

        cbScores = new javax.swing.JComboBox<>();
        spHighScores = new javax.swing.JScrollPane();
        spHelp = new javax.swing.JScrollPane();
        taHighScores = new javax.swing.JTextArea();
        btnExit = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();
        btnLevels = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        lblPlay1 = new javax.swing.JLabel();
        lblLevels1 = new javax.swing.JLabel();
        lblOptions1 = new javax.swing.JLabel();
        lblHelp1 = new javax.swing.JLabel();
        lblExit1 = new javax.swing.JLabel();
        lblLevels2 = new javax.swing.JLabel();
        lblPlay2 = new javax.swing.JLabel();
        lblOptions2 = new javax.swing.JLabel();
        lblHelp2 = new javax.swing.JLabel();
        lblExit2 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        btnAccountManager = new javax.swing.JButton();
        myThread = new java.lang.Thread(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BRICKBREAKER!");
        setResizable(false);
        setBackground(Color.BLACK);
        setSize(new java.awt.Dimension(SCREEN_SIZE));

        add(panel[0] = new javax.swing.JPanel());
        panel[0].addMouseListener((MouseListener) this);
        panel[0].setLayout(CL);
        for (int i = 1; i < 6; i++) {
            panel[i] = new javax.swing.JPanel();
            panel[i].setLayout(null);
            panel[i].setBackground(Color.BLACK);
        }
        panel[0].add("TitleScreen", panel[1]);
        panel[0].add("Options", panel[2]);
        panel[0].add("Help", panel[3]);
        panel[0].add("GamePanel", gamePanel = new GamePanel());
        panel[0].add("NamePanel", panel[4]);
        panel[0].add("LevelCreator", levelCreator = new LevelCreatorWizard());
        panel[0].add("Levels", spLevels = new javax.swing.JScrollPane(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        panel[0].add("Score", scoreManager = new ScoreManager());
        panel[0].add("AccountManager", accountManager = new AccountManager());
        spLevels.setBorder(BorderFactory.createEmptyBorder());
        spLevels.getViewport().add(panel[5]);
        gamePanel.run();
        scoreManager.startThread();
        accountManager.startThread();
        panel[0].setPreferredSize(SCREEN_SIZE);
        btnName = new String[]{"OPTIONS", "DONE", "ENTER", "RESUME", "Login/Sign in", "Back", "LevelCreatorWizard", "Back", "DebugMode: OFF", "Show Score: OFF", "Show Timers: ON", "Difficulty: easy", "Back"};
        for (int i = 0; i < 8; i++) {
            btn[i] = new JButton();
            btn[i].setOpaque(true);
            btn[i].setText(btnName[i]);
            btn[i].addActionListener(this);
            btn[i].setBackground(Color.BLACK);
            btn[i].setForeground(Color.YELLOW);
        }
        //Options
        for (int i = 8; i < 13; i++) {
            panel[2].add(btn[i] = new JButton());
            btn[i].setOpaque(true);
            btn[i].setText(btnName[i]);
            btn[i].addActionListener(this);
            btn[i].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH * 2 / 3, SCREEN_HEIGHT / 2 + COMPONENT_HEIGHT * (i - 10), COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
        }
        //Help
        spHelp.setViewportView(textArea = new javax.swing.JTextArea());
        panel[3].add(spHelp);
        panel[3].add(btn[7]);
        btn[7].setBounds(0, SCREEN_HEIGHT - COMPONENT_HEIGHT * 2, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
        spHelp.setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH * 3, 0, COMPONENT_WIDTH * 6, SCREEN_HEIGHT + COMPONENT_WIDTH * 10);
        textArea.setPreferredSize(new java.awt.Dimension(COMPONENT_WIDTH * 6, SCREEN_HEIGHT));
        textArea.setOpaque(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.YELLOW);
        try {
            Scanner scFile = new Scanner(new File("help.txt"));
            while (scFile.hasNextLine()) {
                textArea.setText(textArea.getText() + "\n" + scFile.nextLine());
            }
            scFile.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        //Levels
        initializeLevels();
        panel[5].add(btn[6]);
        btn[6].setBounds(SCREEN_WIDTH - COMPONENT_WIDTH * 2, COMPONENT_HEIGHT, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        panel[5].add(btn[5]);
        btn[5].setBounds(SCREEN_WIDTH - COMPONENT_WIDTH * 2, COMPONENT_HEIGHT * 2 + 10, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        spLevels.revalidate();
        //Score
        scoreManager.add(btnOK = new JButton());
        btnOK.setBounds(SCREEN_WIDTH / 2 + COMPONENT_WIDTH / 6, (int) (SCREEN_HEIGHT / 1.7), COMPONENT_WIDTH, COMPONENT_HEIGHT);
        btnOK.setOpaque(true);
        btnOK.setText("OK");
        btnOK.addActionListener(this);
        //LevelCreator
        levelCreator.add(btn[1]);
        btn[1].setBounds(LevelCreatorWizard.WIZARD_WIDTH - COMPONENT_WIDTH, LevelCreatorWizard.WIZARD_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        //NamePanel
        panel[4].add(btn[2]);
        btn[2].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH / 3 / 2 + COMPONENT_WIDTH, SCREEN_HEIGHT / 2, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        panel[4].add(txtName = new javax.swing.JTextField());
        txtName.setOpaque(true);
        txtName.setFont(new Font("Monospaced Bold", Font.ITALIC, 12));
        txtName.setText("Enter Name Here");
        txtName.setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH - COMPONENT_WIDTH / 6, SCREEN_HEIGHT / 2, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
        //AccountManager
        accountManager.add(btn[13] = new JButton());
        btn[13].setBounds(0, AccountManager.MANAGER_HEIGHT - COMPONENT_HEIGHT * 2, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        btn[13].addActionListener(this);
        btn[13].setText(btnName[5]);

        cbScores.setForeground(Color.YELLOW);
        cbScores.setBackground(Color.BLACK);
        cbScores.setModel(new DefaultComboBoxModel(new String[]{"Guest Scores", "Account Scores", "All Scores"}));
        cbScores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        panel[1].add(cbScores);
        cbScores.setBounds(0, 358, 181, 26);

        panel[1].add(taHighScores = new javax.swing.JTextArea());
        taHighScores.setLineWrap(true);
        taHighScores.setOpaque(true);
        taHighScores.setBackground(Color.BLACK);
        taHighScores.setForeground(Color.YELLOW);
        taHighScores.setEditable(false);
        taHighScores.setColumns(20);
        taHighScores.setRows(5);
        taHighScores.setFont(new java.awt.Font(Font.DIALOG, Font.CENTER_BASELINE, 14));
        taHighScores.setText(scoreManager.readGuestScores());
        spHighScores.setViewportView(taHighScores);
        spHighScores.setOpaque(true);
        spHighScores.setForeground(Color.BLACK);

        panel[1].add(spHighScores);
        spHighScores.setBounds(0, 0, 181, 349);

        btnExit.setBorder(BorderFactory.createEmptyBorder());
        btnExit.setContentAreaFilled(false);
        btnExit.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnExit.setForeground(Color.YELLOW);
        btnExit.setText("EXIT");
        btnExit.addMouseListener((MouseListener) this);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        panel[1].add(btnExit);
        btnExit.setBounds(502, 421, 122, 20);

        btnHelp.setBorder(BorderFactory.createEmptyBorder());
        btnHelp.setContentAreaFilled(false);
        btnHelp.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnHelp.setForeground(Color.YELLOW);
        btnHelp.setText("HELP");
        btnHelp.addMouseListener((MouseListener) this);
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });
        panel[1].add(btnHelp);
        btnHelp.setBounds(502, 383, 122, 20);

        btnOptions.setBorder(BorderFactory.createEmptyBorder());
        btnOptions.setContentAreaFilled(false);
        btnOptions.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnOptions.setForeground(Color.YELLOW);
        btnOptions.setText("OPTIONS");
        btnOptions.addMouseListener((MouseListener) this);
        btnOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionsActionPerformed(evt);
            }
        });
        panel[1].add(btnOptions);
        btnOptions.setBounds(502, 345, 122, 20);

        btnLevels.setBorder(BorderFactory.createEmptyBorder());
        btnLevels.setContentAreaFilled(false);
        btnLevels.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnLevels.setForeground(Color.YELLOW);
        btnLevels.setText("LEVELS");
        btnLevels.addMouseListener((MouseListener) this);
        btnLevels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLevelsActionPerformed(evt);
            }
        });
        panel[1].add(btnLevels);
        btnLevels.setBounds(502, 307, 122, 20);

        btnPlay.setBorder(BorderFactory.createEmptyBorder());
        btnPlay.setContentAreaFilled(false);
        btnPlay.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnPlay.addMouseListener((MouseListener) this);
        btnPlay.setText("PLAY");
        btnPlay.setForeground(Color.YELLOW);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        panel[1].add(btnPlay);
        btnPlay.setBounds(502, 269, 122, 20);

        lblPlay1.setForeground(Color.YELLOW);
        lblPlay1.setVisible(false);
        lblPlay1.setText("<<");
        panel[1].add(lblPlay1);
        lblPlay1.setBounds(630, 273, 14, 16);

        lblLevels1.setForeground(Color.YELLOW);
        lblLevels1.setVisible(false);
        lblLevels1.setText("<<");
        panel[1].add(lblLevels1);
        lblLevels1.setBounds(630, 311, 14, 16);

        lblOptions1.setForeground(Color.YELLOW);
        lblOptions1.setVisible(false);
        lblOptions1.setText("<<");
        panel[1].add(lblOptions1);
        lblOptions1.setBounds(630, 349, 14, 16);

        lblHelp1.setForeground(Color.YELLOW);
        lblHelp1.setVisible(false);
        lblHelp1.setText("<<");
        panel[1].add(lblHelp1);
        lblHelp1.setBounds(630, 387, 14, 16);

        lblExit1.setForeground(Color.YELLOW);
        lblExit1.setVisible(false);
        lblExit1.setText("<<");
        panel[1].add(lblExit1);
        lblExit1.setBounds(630, 425, 14, 16);

        lblLevels2.setForeground(Color.YELLOW);
        lblLevels2.setVisible(false);
        lblLevels2.setText(">>");
        panel[1].add(lblLevels2);
        lblLevels2.setBounds(469, 311, 14, 16);

        lblPlay2.setForeground(Color.YELLOW);
        lblPlay2.setVisible(false);
        lblPlay2.setText(">>");
        panel[1].add(lblPlay2);
        lblPlay2.setBounds(469, 273, 14, 16);

        lblOptions2.setForeground(Color.YELLOW);
        lblOptions2.setVisible(false);
        lblOptions2.setText(">>");
        panel[1].add(lblOptions2);
        lblOptions2.setBounds(469, 349, 14, 16);

        lblHelp2.setForeground(Color.YELLOW);
        lblHelp2.setVisible(false);
        lblHelp2.setText(">>");
        panel[1].add(lblHelp2);
        lblHelp2.setBounds(469, 387, 14, 16);

        lblExit2.setForeground(Color.YELLOW);
        lblExit2.setVisible(false);
        lblExit2.setText(">>");
        panel[1].add(lblExit2);
        lblExit2.setBounds(469, 425, 14, 16);

        lblTitle.setForeground(Color.YELLOW);
        lblTitle.setFont(new java.awt.Font("Sitka Heading", 1, 48)); // NOI18N
        lblTitle.setText("BRICK BREAKER");
        lblTitle.setToolTipText("");
        panel[1].add(lblTitle);
        lblTitle.setBounds(391, 134, 359, 70);
        btnAccountManager.setBackground(Color.BLACK);
        btnAccountManager.setForeground(Color.YELLOW);
        btnAccountManager.setBorderPainted(false);
        btnAccountManager.setFocusPainted(false);
        btnAccountManager.setContentAreaFilled(false);
        //btnAccountManager.setVisible(false);//<---
        btnAccountManager.addMouseListener((MouseListener) this);
        btnAccountManager.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnAccountManager.setText("LogIn/Sign In");
        btnAccountManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountManagerActionPerformed(evt);
            }
        });
        panel[1].add(btnAccountManager);
        btnAccountManager.setBounds(820, 480, 160, 30);
    }

    private void btnAccountManagerActionPerformed(java.awt.event.ActionEvent evt) {
        CL.show(panel[0], "AccountManager");
    }

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
        System.exit(0);
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        switch (cbScores.getSelectedIndex()) {
            case 0:
                taHighScores.setText(scoreManager.readGuestScores());
                break;
            case 1:
                String[] leaderBoard = (new DataHandler()).getAccountScores();
                taHighScores.setText("HIGH SCORES:\n");
                for (int i = 0; i < leaderBoard.length; i++) {
                    if (!(leaderBoard[i] == null)) {
                        taHighScores.setText(taHighScores.getText() + (i + 1) + "." + leaderBoard[i] + "\n");
                    }
                }
                break;
            case 2:
                taHighScores.setText(scoreManager.readAllScores());
                break;
            default:
                throw new AssertionError();
        }
    }

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {
        if (!loggedIn) {
            CL.show(panel[0], "NamePanel");
        } else {
            gamePanel.requestFocus();
            gamePanel.setTotalPlayTime(accountManager.getAccount().getTotalPlayTime());
            CL.show(panel[0], "GamePanel");
            if (!gameHasStarted) {
                panel[0].setPreferredSize(gamePanel.getPreferredSize());
                pack();
                gamePanel.scanNewLevel(1);
                gamePanel.newPaddle();
                if (debugMode) {
                    gamePanel.setDebugMode(true);
                    gamePanel.newDebuggerTool();
                } else {
                    gamePanel.setDebugMode(false);
                    gamePanel.newBall(gamePanel.getWidth() / 2 - 10, gamePanel.getHeight() - 25);
                }
                gamePanel.setShowPowerUpTime(showTimers);
                if (!gamePanel.isThreadOn()) {
                    gamePanel.startThread();
                }
                gameHasStarted = true;
            }
        }
    }

    private void btnLevelsActionPerformed(java.awt.event.ActionEvent evt) {
        CL.show(panel[0], "Levels");
        panel[0].setPreferredSize(SCREEN_SIZE);
        pack();
    }

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {
        CL.show(panel[0], "Help");
        panel[3].add(btn[7]);
        btn[7].setText(btnName[7]);
        btn[7].setBounds(0, SCREEN_HEIGHT - COMPONENT_HEIGHT * 2, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
    }

    private void btnOptionsActionPerformed(java.awt.event.ActionEvent evt) {
        CL.show(panel[0], "Options");
        panel[2].requestFocus();
    }

    private javax.swing.JButton btnAccountManager;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnLevels;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cbScores;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane spHighScores;
    private javax.swing.JScrollPane spHelp;
    private javax.swing.JScrollPane spLevels;
    private javax.swing.JTextArea taHighScores;
    private javax.swing.JLabel lblExit1;
    private javax.swing.JLabel lblExit2;
    private javax.swing.JLabel lblHelp1;
    private javax.swing.JLabel lblHelp2;
    private javax.swing.JLabel lblLevels1;
    private javax.swing.JLabel lblLevels2;
    private javax.swing.JLabel lblOptions1;
    private javax.swing.JLabel lblOptions2;
    private javax.swing.JLabel lblPlay1;
    private javax.swing.JLabel lblPlay2;
    private java.lang.Thread myThread;
    private GamePanel gamePanel;
    private ScoreManager scoreManager;
    private LevelCreatorWizard levelCreator;
    private AccountManager accountManager;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextArea textArea;

    /**
     * Used to redirect the user to score screen from game panel in the event of a game over or level won and save the player's scores
     * @param me 
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (gamePanel.isGameOver()) {
            scoreManager.setScore(gamePanel.getScore());
            if (loggedIn) {
                accountManager.getAccount().setHighestScore(gamePanel.getScore());
                accountManager.getAccount().setTotalPlayTime(gamePanel.getTotalPlayTime());
                accountManager.getAccount().addLevelsWon(gamePanel.getLevelsWon());
                accountManager.getAccount().addLevelsLost(1);
            }
            panel[0].setPreferredSize(scoreManager.getPreferredSize());
            pack();
            CL.show(panel[0], "Score");
            scoreManager.setDone(false);
            panel[0].add("GamePanel", gamePanel = new GamePanel());
            gameHasStarted = false;
            taHighScores.setText(scoreManager.readGuestScores());
        } else {
            if (!scoreManager.isDone() && me.getClickCount() == 2) {
                scoreManager.finish();
            }
        }
        if (gamePanel.isLastLevelWon()) {
            scoreManager.setScore(gamePanel.getScore());
            if (loggedIn) {
                accountManager.getAccount().setHighestScore(gamePanel.getScore());
                accountManager.getAccount().setTotalPlayTime(gamePanel.getTotalPlayTime());
                accountManager.getAccount().addLevelsWon(gamePanel.getLevelsWon());
            }
            panel[0].setPreferredSize(scoreManager.getPreferredSize());
            pack();
            CL.show(panel[0], "Score");
            scoreManager.setDone(false);
            panel[0].add("GamePanel", gamePanel = new GamePanel());
            gameHasStarted = false;
            taHighScores.setText(scoreManager.readGuestScores());
        }
        taHighScores.setText(scoreManager.readGuestScores());
    }

    @Override
    public void mousePressed(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Used in tandem with mouseExited() to create fancy animation on the title screen with the buttons. If the mouse enters a button's area increase it's font size and add arrows to the sides and reverses this process once the mouse exits
     * @param me 
     */
    @Override
    public void mouseEntered(MouseEvent me) {
        if (me.getSource().equals(btnPlay)) {
            lblPlay1.setVisible(true);
            lblPlay2.setVisible(true);
            btnPlay.setFont(new Font(btnPlay.getFont().toString(), Font.BOLD, 18));
        }
        if (me.getSource().equals(btnLevels)) {
            lblLevels1.setVisible(true);
            lblLevels2.setVisible(true);
            btnLevels.setFont(new Font(btnLevels.getFont().toString(), Font.BOLD, 18));
        }
        if (me.getSource().equals(btnOptions)) {
            lblOptions1.setVisible(true);
            lblOptions2.setVisible(true);
            btnOptions.setFont(new Font(btnOptions.getFont().toString(), Font.BOLD, 18));
        }
        if (me.getSource().equals(btnHelp)) {
            lblHelp1.setVisible(true);
            lblHelp2.setVisible(true);
            btnHelp.setFont(new Font(btnHelp.getFont().toString(), Font.BOLD, 18));
        }
        if (me.getSource().equals(btnExit)) {
            lblExit1.setVisible(true);
            lblExit2.setVisible(true);
            btnExit.setFont(new Font(btnExit.getFont().toString(), Font.BOLD, 18));
        }
        if (me.getSource().equals(btnAccountManager)) {
            btnAccountManager.setFont(new Font(btnAccountManager.getFont().toString(), Font.BOLD, 18));
        }
    }

    /**
     * Used in tandem with mouseEntered() to create fancy animation on the title screen with the buttons. If the mouse enters a button's area increase it's font size and add arrows to the sides and reverses this process once the mouse exits
     * @param me 
     */
    @Override
    public void mouseExited(MouseEvent me) {
        if (me.getSource().equals(btnPlay)) {
            lblPlay1.setVisible(false);
            lblPlay2.setVisible(false);
            btnPlay.setFont(new Font(btnPlay.getFont().toString(), Font.BOLD, 16));
        }
        if (me.getSource().equals(btnLevels)) {
            lblLevels1.setVisible(false);
            lblLevels2.setVisible(false);
            btnLevels.setFont(new Font(btnLevels.getFont().toString(), Font.BOLD, 16));
        }
        if (me.getSource().equals(btnOptions)) {
            lblOptions1.setVisible(false);
            lblOptions2.setVisible(false);
            btnOptions.setFont(new Font(btnOptions.getFont().toString(), Font.BOLD, 16));
        }
        if (me.getSource().equals(btnHelp)) {
            lblHelp1.setVisible(false);
            lblHelp2.setVisible(false);
            btnHelp.setFont(new Font(btnHelp.getFont().toString(), Font.BOLD, 16));
        }
        if (me.getSource().equals(btnExit)) {
            lblExit1.setVisible(false);
            lblExit2.setVisible(false);
            btnExit.setFont(new Font(btnExit.getFont().toString(), Font.BOLD, 16));
        }
        if (me.getSource().equals(btnAccountManager)) {
            btnAccountManager.setFont(new Font(btnAccountManager.getFont().toString(), Font.BOLD, 16));
        }
    }

    /**
     * The run() method in gamePanel is a method that creates an alternate execution path that loops continuously whilst simultaneously allowing other sections of code in other classes to run independently (a new thread). It does this by looping every time the difference between the “JvM’s high resolution time source” in the past and currently is greater than or equal to 1(running code that the programmer wants to be looped when this statement is true), which can be done by claiming two variables(that have the current value of the “JvM’s high resolution time source”) separated by at least 4 lines of code and subtracting them on the 5th line of code, the separation allows a time difference to culminate because different lines of code take different amounts of time to be executed by the JvM, we measure this time difference to create a loop that ticks at the frequency we want, this process is repeated multiple times every second(60 times in the case below). The run method in gamePanel is designed so that approximately 60 loops happen every second.This number can be affected by many factors making it an imperfect game loop; however it gets the job done. During each iteration the method runs all the other main methods necessary for the game to run as intended.
     * This one in particular insures that each component visible on screen has focus; which is necessary for user inputs to register in the respective classes(the ones currently visible).
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
                try {
                    if (getFocusOwner() == null && gamePanel.isVisible()) {
                        gamePanel.requestFocus();
                    } else {
                        if (gamePanel.isVisible() && !getFocusOwner().equals(gamePanel)) {
                            gamePanel.requestFocus();
                        }
                    }
                } catch (Exception e) {
                }
                if (gamePanel.isGamePaused()) {
                    if (!gamePaused) {
                        btn[7].setText("EXIT GAME");
                        gamePanel.add(btn[3]);
                        gamePanel.add(btn[0]);
                        gamePanel.add(btn[7]);
                        gamePaused = true;
                    }
                } else {
                    gamePaused = false;
                    gamePanel.remove(btn[3]);
                    gamePanel.remove(btn[0]);
                    gamePanel.remove(btn[7]);
                }
                loggedIn = accountManager.getStatus();
                if (loggedIn) {
                    btnAccountManager.setText("Logged In: " + accountManager.getAccount().getName());
                } else {
                    btnAccountManager.setText("LogIn/Sign In");
                }
                if (panel[1].isVisible() && !(getSize() == SCREEN_SIZE)) {
                    setSize(SCREEN_SIZE);
                }
                delta--;
            }
        }
    }
}
