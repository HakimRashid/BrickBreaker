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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Hakim
 */
public class GameFrame extends JFrame implements ActionListener {

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 5 / 9;
    private static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    private final int COMPONENT_WIDTH = SCREEN_WIDTH / 10;
    private final int COMPONENT_HEIGHT = SCREEN_HEIGHT / 15;
    private final JLabel lblTitle1;
    private final JLabel lblTitle2;
    private final JButton[] btn = new JButton[110];
    private String[] btnName = new String[10];
    private final JTextArea taScores;
    private final JTextArea textArea;
    private final JScrollPane pane;
    private final JPanel[] panel = new JPanel[5];
    private GamePanel gamePanel;
    private final ScoreManager scoreScreen;
    private final LevelCreatorWizard levelCreator;
    private final AccountManager mainDesk;
    private final LEVELS level;
    private final int[] scores = new int[30];
    private final String[] scoreBoard = new String[30];
    private int numScores;
    private int numLevels;
    private int lastScore;
    private boolean more;
    private boolean debugMode;
    private boolean showTimers = true;
    private boolean showScore;
    private boolean gameHasStarted;
    private final static CardLayout CL = new CardLayout();
    private final JButton btnOK;
    private final JTextField txtName;
    private boolean loggedIn;

    public GameFrame() {
        this.addKeyListener(new KL());
        this.addMouseListener(new ML());
        this.setResizable(false);
        this.setTitle("BRICK BREAKER!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.setVisible(true);

        this.add(panel[0] = new JPanel());
        panel[0].addMouseListener(new ML());
        panel[0].setLayout(CL);
        for (int i = 1; i < 5; i++) {
            panel[i] = new JPanel();
            panel[i].setLayout(null);
            panel[i].setBackground(Color.BLACK);
        }
        panel[0].add("TitleScreen", panel[1]);
        panel[0].add("Options", panel[2]);
        panel[0].add("Help", panel[3]);
        panel[0].add("GamePanel", gamePanel = new GamePanel());
        panel[0].add("NamePanel", panel[4]);
        panel[0].add("LevelCreator", levelCreator = new LevelCreatorWizard());
        panel[0].add("Levels", pane = new JScrollPane());
        panel[0].add("Score", scoreScreen = new ScoreManager());
        panel[0].add("AccountManager", mainDesk = new AccountManager());
        pane.setViewportView(level = new LEVELS());
        gamePanel.run();
        scoreScreen.startThread();
        mainDesk.startThread();
        mainDesk.suspendThread();
        panel[0].setPreferredSize(SCREEN_SIZE);
        panel[1].setPreferredSize(SCREEN_SIZE);
        this.pack();
        panel[1].add(lblTitle1 = new JLabel());
        lblTitle1.setBounds(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 6, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        lblTitle1.setOpaque(true);
        lblTitle1.setText("BRICK");
        lblTitle1.setBackground(Color.BLACK);
        lblTitle1.setForeground(Color.BLUE);
        lblTitle1.setFont(new Font("Monospaced Bold", Font.ITALIC, 40));
        panel[1].add(lblTitle2 = new JLabel());
        lblTitle2.setBounds(SCREEN_WIDTH / 3 + COMPONENT_WIDTH * 3 / 2, SCREEN_HEIGHT / 6, COMPONENT_WIDTH * 4, COMPONENT_HEIGHT);
        lblTitle2.setOpaque(true);
        lblTitle2.setText("BREAKER!");
        lblTitle2.setBackground(Color.BLACK);
        lblTitle2.setForeground(Color.YELLOW);
        lblTitle2.setFont(new Font("Monospaced Bold", Font.ITALIC, 40));
        btnName = new String[]{"PLAY", "OPTIONS", "HELP", "LEVELS", "EXIT", "DONE", "ENTER", "RESUME", "Login/Sign in", "Back", "LevelCreatorWizard", "DebugMode: OFF", "Show Score: OFF", "Show Timers: ON", "Controls & Dimensions", "Back"};
        for (int i = 0; i < 10; i++) {
            btn[i] = new JButton();
            btn[i].setOpaque(true);
            btn[i].setText(btnName[i]);
            btn[i].addActionListener(this);
            btn[i].setBackground(Color.BLACK);
            btn[i].setForeground(Color.YELLOW);
        }
        for (int i = 0; i < 5; i++) {
            panel[1].add(btn[i]);
            btn[i].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH / 3 / 2, SCREEN_HEIGHT / 2 + COMPONENT_HEIGHT * i, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        }
        panel[1].add(btn[16] = new JButton());
        btn[16].setBackground(Color.BLACK);
        btn[16].setForeground(Color.YELLOW);
        btn[16].setText(btnName[8]);
        btn[16].setBounds(SCREEN_WIDTH - COMPONENT_WIDTH * 3 / 2, SCREEN_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        btn[16].addActionListener(this);
        panel[1].add(taScores = new JTextArea());
        taScores.setBounds(0, 0, COMPONENT_WIDTH * 3, SCREEN_HEIGHT);
        taScores.setOpaque(true);
        taScores.setBackground(Color.BLACK);
        taScores.setForeground(Color.YELLOW);
        taScores.setText("HIGH SCORES:\n");
        readScores();
        while (more) {
            readScores();
        }
        //Options
        for (int i = 11; i < 16; i++) {
            panel[2].add(btn[i] = new JButton());
            btn[i].setOpaque(true);
            btn[i].setText(btnName[i]);
            btn[i].addActionListener(this);
            btn[i].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH * 2 / 3, SCREEN_HEIGHT / 2 + COMPONENT_HEIGHT * (i - 10), COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
//            btn[i].setBackground(Color.BLACK);
//            btn[i].setForeground(Color.YELLOW);
        }
        //Help
        panel[3].add(textArea = new JTextArea());
        panel[3].add(btn[8] = new JButton());
        btn[8].addActionListener(this);
        btn[8].setOpaque(true);
        btn[8].setBackground(Color.BLACK);
        btn[8].setForeground(Color.YELLOW);
        btn[8].setText(btnName[9]);
        btn[8].setBounds(0, SCREEN_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
        textArea.setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH * 3, 0, COMPONENT_WIDTH * 6, SCREEN_HEIGHT);
        textArea.setOpaque(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.YELLOW);
        textArea.setText("*help text goes here*");
        //Levels
        initializeLevels();
        level.add(btn[10] = new JButton());
        btn[10].addActionListener(this);
        btn[10].setOpaque(true);
        btn[10].setText(btnName[10]);
        btn[10].setBackground(Color.BLACK);
        btn[10].setForeground(Color.YELLOW);
        btn[10].setBounds(SCREEN_WIDTH - COMPONENT_WIDTH * 2, COMPONENT_HEIGHT, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        level.add(btn[9] = new JButton());
        btn[9].addActionListener(this);
        btn[9].setOpaque(true);
        btn[9].setBackground(Color.BLACK);
        btn[9].setForeground(Color.YELLOW);
        btn[9].setText(btnName[9]);
        btn[9].setBounds(SCREEN_WIDTH - COMPONENT_WIDTH * 2, COMPONENT_HEIGHT * 2 + 10, COMPONENT_WIDTH * 3 / 2, COMPONENT_HEIGHT);
        pane.revalidate();
        //Score
        scoreScreen.add(btnOK = new JButton());
        btnOK.setBounds(SCREEN_WIDTH / 2 + COMPONENT_WIDTH / 6, (int) (SCREEN_HEIGHT / 1.7), COMPONENT_WIDTH, COMPONENT_HEIGHT);
        btnOK.setOpaque(true);
        btnOK.setText("OK");
        btnOK.addActionListener(this);
        //LevelCreator
        levelCreator.add(btn[5]);
        btn[5].setBounds(LevelCreatorWizard.WIZARD_WIDTH - COMPONENT_WIDTH, LevelCreatorWizard.WIZARD_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        //NamePanel
        panel[4].add(btn[6]);
        btn[6].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH / 3 / 2 + COMPONENT_WIDTH, SCREEN_HEIGHT / 2, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        btn[6].setOpaque(true);
        btn[6].setText(btnName[6]);
        btn[6].addActionListener(this);
        panel[4].add(txtName = new JTextField());
        txtName.setOpaque(true);
        txtName.setFont(new Font("Monospaced Bold", Font.ITALIC, 12));
        txtName.setText("Enter Name Here");
        txtName.setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH - COMPONENT_WIDTH / 6, SCREEN_HEIGHT / 2, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
        //AccountManager
        mainDesk.add(btn[17] = new JButton());
        btn[17].setBounds(0, AccountManager.MANAGER_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        btn[17].addActionListener(this);
        btn[17].setText(btnName[9]);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.requestFocus();
        if (ae.getSource().equals(btn[0]) && !loggedIn) {
            CL.show(panel[0], "NamePanel");
        }
        if (ae.getSource().equals(btn[6])) {
            CL.show(panel[0], "GamePanel");
            if (txtName.getText().equalsIgnoreCase("") || txtName.getText().equalsIgnoreCase("Enter Name Here")) {
                scoreScreen.setPlayerName(null);
            } else {
                scoreScreen.setPlayerName(txtName.getText());
            }
            if (!gameHasStarted) {
                panel[0].setPreferredSize(gamePanel.getPreferredSize());
                this.pack();
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
        if (ae.getSource().equals(btn[3])) {
            CL.show(panel[0], "Levels");
            panel[0].setPreferredSize(level.getPreferredSize());
            this.pack();
        }
        if (ae.getSource().equals(btn[4])) {
            System.exit(0);
        }
        for (int i = 1; i <= numLevels; i++) {
            if (ae.getSource().equals(btn[i + 20])) {
                if (gamePanel.scanNewLevel(Integer.parseInt(btn[i + 20].getText()))) {
                    CL.show(panel[0], "GamePanel");
                    panel[0].setPreferredSize(gamePanel.getPreferredSize());
                    this.pack();
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
                }
            }
        }
        if (ae.getSource().equals(btn[10])) {
            CL.show(panel[0], "LevelCreator");
            panel[0].setPreferredSize(levelCreator.getPreferredSize());
            this.pack();
        }
        if (ae.getSource().equals(btnOK)) {
            CL.show(panel[0], "TitleScreen");
            panel[0].setPreferredSize(panel[1].getPreferredSize());
            if (!loggedIn) {
                scoreScreen.print();
            }
            this.pack();
        }
        if (ae.getSource().equals(btn[5])) {
            CL.show(panel[0], "TitleScreen");
            panel[0].setPreferredSize(panel[1].getPreferredSize());
            levelCreator.finish();
            initializeLevels();
            this.pack();
        }
        if (ae.getSource().equals(btn[1])) {
            CL.show(panel[0], "Options");
            panel[2].requestFocus();
        }
        if (ae.getSource().equals(btn[11])) {
            if (debugMode) {
                debugMode = false;
                btn[11].setText(btn[11].getText().substring(0, 11) + "OFF");
            } else {
                debugMode = true;
                btn[11].setText(btn[11].getText().substring(0, 11) + "ON");
            }
            gamePanel.setDebugMode(debugMode);
        }
        if (ae.getSource().equals(btn[12])) {
            if (showScore) {
                showScore = false;
                btn[12].setText(btn[12].getText().substring(0, 12) + "OFF");
            } else {
                showScore = true;
                btn[12].setText(btn[12].getText().substring(0, 12) + "ON");
            }
            gamePanel.setShowScore(showScore);
        }
        if (ae.getSource().equals(btn[13])) {
            if (showTimers) {
                showTimers = false;
                btn[13].setText(btn[13].getText().substring(0, 13) + "OFF");
            } else {
                showTimers = true;
                btn[13].setText(btn[13].getText().substring(0, 13) + "ON");
            }
            gamePanel.setShowPowerUpTime(showTimers);
        }
        if (ae.getSource().equals(btn[15]) || ae.getSource().equals(btn[9]) || ae.getSource().equals(btn[8])) {
            if (gameHasStarted && !ae.getSource().equals(btn[8])) {
                CL.show(panel[0], "GamePanel");
            } else {
                gameHasStarted = false;
                replaceButtons();
                CL.show(panel[0], "TitleScreen");
            }
            panel[0].setPreferredSize(panel[1].getPreferredSize());
            this.pack();
        }
        if (ae.getSource().equals(btn[2])) {
            CL.show(panel[0], "Help");
        }
        if (ae.getSource().equals(btn[7])) {
            gamePanel.setGamePaused(false);
        }
        if (ae.getSource().equals(btn[16])) {
            CL.show(panel[0], "AccountManager");
            mainDesk.resumeThread();
        }
        if (ae.getSource().equals(btn[17])) {
            CL.show(panel[0], "TitleScreen");
            mainDesk.suspendThread();
        }
    }

    private void readScores() {
        try {
            Scanner scFile;
            scFile = new Scanner(new File("SCORES.txt"));
            if (more) {
                for (int i = 0; i < lastScore; i++) {
                    scFile.nextLine();
                }
                numScores -= 15;
            }
            while (scFile.hasNext() && !(numScores == 30)) {
                Scanner scLine;
                scLine = new Scanner(scFile.nextLine()).useDelimiter(": ");
                scoreBoard[numScores] = scLine.next();
                scores[numScores++] = scLine.nextInt();
                scLine.close();
            }
            if (scFile.hasNext()) {
                more = true;
            } else {
                more = false;
                sort();
                scFile.close();
                for (int i = 0; i < numScores; i++) {
                    taScores.setText(taScores.getText() + (i + 1) + ". " + scoreBoard[i] + ": " + scores[i] + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    private void sort() {
        for (int i = 0; i < numScores - 1; i++) {
            for (int j = 0; j < numScores - 1 - i; j++) {
                if (scores[j] < scores[j + 1]) {
                    int temp = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = temp;
                    String temp1 = scoreBoard[j];
                    scoreBoard[j] = scoreBoard[j + 1];
                    scoreBoard[j + 1] = temp1;
                }
            }
        }
    }

    private void replaceButtons() {
        panel[1].add(btn[1]);
        btn[1].setBounds(SCREEN_WIDTH / 2 - COMPONENT_WIDTH / 3 / 2, SCREEN_HEIGHT / 2 + COMPONENT_HEIGHT, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        panel[3].add(btn[8]);
        btn[8].setText("Back");
        btn[8].setBounds(0, SCREEN_HEIGHT - COMPONENT_HEIGHT, COMPONENT_WIDTH * 2, COMPONENT_HEIGHT);
    }

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
        for (int i = 1; i <= Math.ceil((double) numLevels / 3); i++) {
            if (numLevels - numLoops >= 3) {
                numJLoops = 3;
            } else {
                numJLoops = numLevels - numLoops + 1;
            }
            for (int j = 1; j <= numJLoops; j++) {
                level.add(btn[numLoops + 20] = new JButton());
                btn[numLoops + 20].setBounds(COMPONENT_WIDTH * 2 * j, COMPONENT_HEIGHT * 2 * i, COMPONENT_WIDTH, COMPONENT_HEIGHT);
                btn[numLoops + 20].setText("" + numLoops);
                btn[numLoops + 20].addActionListener(this);
                btn[numLoops + 20].setOpaque(true);
                btn[numLoops + 20].setBackground(Color.BLACK);
                btn[numLoops + 20].setForeground(Color.YELLOW);
                numLoops++;
            }
        }
        pane.setSize(level.getPreferredSize());
        pane.revalidate();
    }

    private class ML implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
            if (gamePanel.isGameOver()) {
                replaceButtons();
                scoreScreen.setScore(gamePanel.getScore());
                panel[0].setPreferredSize(scoreScreen.getPreferredSize());
                CL.show(panel[0], "Score");
                scoreScreen.setDone(false);
                System.out.println(gamePanel.getScore());
                panel[0].add("GamePanel", gamePanel = new GamePanel());
                gameHasStarted = false;
            } else {
                if (!scoreScreen.isDone() && me.getClickCount() == 2) {
                    scoreScreen.finish();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent me) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private class KL extends KeyAdapter {

        /**
         *
         * @param event
         */
        @Override
        public void keyPressed(KeyEvent event) {
            if (gameHasStarted) {
                gamePanel.keyPressed(event);
                if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    btn[8].setText("EXIT GAME");
                    gamePanel.add(btn[7]);
                    gamePanel.add(btn[1]);
                    gamePanel.add(btn[8]);
                }
                if (gamePanel.isGamePaused()) {
                    for (int i = 7; i < 9; i++) {
                        gamePanel.remove(btn[i]);
                    }
                }
            }
        }

        /**
         *
         * @param event
         */
        @Override
        public void keyReleased(KeyEvent event) {
            if (gameHasStarted) {
                gamePanel.keyReleased(event);
            }
        }
    }
}
