/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hakim
 */
public class DataHandler {

    public boolean searchUser(String username) {
        boolean userFound = false;
        Account accountFound = null;
        try {
            Connect conn = new Connect();
            String sql = "SELECT * FROM accountmanager.accounts WHERE username = \"" + username + "\"";
            System.out.println("checkLogin(): " + sql);
            ResultSet rs = conn.query(sql);
            if (rs.next()) {
                int id = rs.getInt("accountid");
                String inUsername = rs.getString("username");
                Account current = new Account(inUsername, null);
                current.setAccountID(id);
                accountFound = current;
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        if (accountFound != null) {
            System.err.println("Account found: " + accountFound.getName());
            userFound = true;
        }
        return userFound;
    }

    public Account checkLogin(String username, String password) {
        Account accountFound = null;
        try {
            Connect conn = new Connect();
            String sql = "SELECT * FROM accountmanager.accounts WHERE username = \"" + username + "\" AND password = \"" + password + "\"";
            System.out.println("checkLogin(): " + sql);
            ResultSet rs = conn.query(sql);
            if (rs.next()) {
                int id = rs.getInt("accountid");
                String inUsername = rs.getString("username");
                String pass = rs.getString("password");
                int levelsWon = rs.getInt("levelsWon");
                int levelsLost = rs.getInt("levelsLost");
                int highestScore = rs.getInt("highestScore");
                Account current = new Account(inUsername, pass);
                current.setAccountID(id);
                current.setLevelsWon(levelsWon);
                current.setLevelsLost(levelsLost);
                current.setHighestScore(highestScore);
                accountFound = current;
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return accountFound;
    }

    public void signIn(Account account) {
        try {
            Connect conn = new Connect();
            String sql = "INSERT INTO accountmanager.accounts(username, password) VALUES(\"" + account.getName() + "\", \"" + account.getPassword() + "\")";
            System.err.println("signIn(): " + sql);
            conn.update(sql);
            conn.close();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void updateAccount(Account account, String oldUsername, String oldPassword) {
        try {
            Connect conn = new Connect();
            String sql = "UPDATE accountmanager.accounts SET username = \"" + account.getName() + "\", password = \"" + account.getPassword() + "\", levelslost = " + account.getLevelsLost() + ", levelswon = " + account.getLevelsWon() + ", highestScore = " + account.getHighestScore() + ", playTime = (\"" + account.getTotalPlayTime() + "\") " + " WHERE  username = \"" + oldUsername + "\" AND password = \"" + oldPassword + "\"";
            System.out.println("updateAccount(): " + sql);
            conn.update(sql);
            conn.close();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public String[] getAccountScores() {
        String[] leaderBoard = new String[15];
        try {
            Connect conn = new Connect();
            String sql = "SELECT username, highestScore FROM accountmanager.accounts";
            System.out.println("getAccountScores(): " + sql);
            ResultSet rs = conn.query(sql);
            int[] scores = new int[15];
            String[] names = new String[15];
            int k = 0;
            while (rs.next()) {
                if (!(rs.getString("username") == null && rs.getInt("highestScore") == 0)) {
                    names[k] = rs.getString("username");
                    scores[k++] = rs.getInt("highestScore");
                }
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
            }
            for (int i = 0; i < k; i++) {
                leaderBoard[i] = names[i] + ": " + scores[i];
            }
            rs.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println(e);
        }
        return leaderBoard;
    }
}
