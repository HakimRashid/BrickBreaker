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
public class BrickBreaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Dev Console:");
        GameFrame test = new GameFrame();
        test.setVisible(true);


//        ____________________________
//        UNUSED CODE COULD BE USEFUL:
//        ----------------------------

//Manual button adding for levels without gridlayout:
//        int numLoops = 1;
//        int numJLoops = 1;
//        for (int i = 1; i <= Math.ceil((double) numLevels / 3); i++) {
//            if (numLevels - numLoops >= 3) {
//                numJLoops = 3;
//            } else {
//                numJLoops = numLevels - numLoops + 1;
//            }
//            for (int j = 1; j <= numJLoops; j++) {
//                panel[7].add(btn[numLoops + 16] = new JButton());
//                btn[numLoops + 16].setBounds(COMPONENT_WIDTH * 2 * j, COMPONENT_HEIGHT * 2 * i, COMPONENT_WIDTH, COMPONENT_HEIGHT);
//                btn[numLoops + 16].setText("" + numLoops);
//                btn[numLoops + 16].addActionListener(this);
//                btn[numLoops + 16].setOpaque(true);
//                btn[numLoops + 16].setBackground(Color.BLACK);
//                btn[numLoops + 16].setForeground(Color.YELLOW);
//                numLoops++;
//            }
//        }
//
//Ball-brick collision using row manager:
//            for (int i = 0; i < numBrickRows; i++) {
//                for (int j = 0; j < brickRow[i].getNumBricks(); j++) {
//                    if (brickRow[i].getBrick(j).getHp() >= 2) {
//                        //Top and bottom of brick
//                        if (ball.intersects(brickRow[i].getBrick(j).x + 1, brickRow[i].getBrick(j).y, BRICK_WIDTH - 1, BRICK_HEIGHT)) {
//                            ball.setYVelocity(-ball.getYVelocity());
//                            brickRow[i].getBrick(j).setHp(brickRow[i].getBrick(j).getHp() - 1);
//                        }
//                        //Left of brick
//                        if (ball.intersects(brickRow[i].getBrick(j).x, brickRow[i].getBrick(j).y, 1, BRICK_HEIGHT)) {
//                            ball.setXVelocity(-ball.getXVelocity());
//                            brickRow[i].getBrick(j).setHp(brickRow[i].getBrick(j).getHp() - 1);
//                        }
//                        //Right of brick
//                        if (ball.intersects(brickRow[i].getBrick(j).x + BRICK_WIDTH - 1, brickRow[i].getBrick(j).y, 1, BRICK_HEIGHT)) {
//                            ball.setXVelocity(-ball.getXVelocity());
//                            brickRow[i].getBrick(j).setHp(brickRow[i].getBrick(j).getHp() - 1);
//                        }
//                    } else {
//                        if (brickRow[i].getBrick(j).getHp() == 1) {
//                            if (ball.intersects(brickRow[i].getBrick(j))) {
//                                brickRow[i].getBrick(j).setHp(brickRow[i].getBrick(j).getHp() - 1);
//                            }
//                        }
//                    }
//                }
//
//            }
// LEVEL CREATOR CODE(OLD):
//                        for (int i = 0; i < WIZARD_HEIGHT / (BRICK_HEIGHT - BRICK_HEIGHT / (WIZARD_HEIGHT / 28)); i++) {
//                            for (int j = 0; j < WIZARD_WIDTH / (BRICK_WIDTH + BRICK_WIDTH / (WIZARD_WIDTH / 125)) - 1; j++) {
//                                brick[numBricks++] = new Brick(BRICK_WIDTH * j, BRICK_HEIGHT * i, BRICK_WIDTH, BRICK_HEIGHT, BRICK_HP, BRICK_PwrUP, BRICK_COLOUR, true);
//                            }
//                        }
//        if (rbnLayoutAssist.isSelected()) {
//            int numBrick = checkCollisionsMode1();
//            if (numBrick < WIZARD_WIDTH / (BRICK_WIDTH + BRICK_WIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (BRICK_HEIGHT - BRICK_HEIGHT / (WIZARD_HEIGHT / 28))) {
//                numBricks1++;
//                brick[numBrick].mousePressed(me);
//                brick[numBrick].setEditMode(true);
//                brick[numBrick].setColour(BRICK_COLOUR);
//                if (rbnEreaser.isSelected()) {
//                    brick[numBrick].setEreased(true);
//                    numBricks1--;
//                }
//            }
//        }
//    private int checkCollisionsMode1() {
//        for (int i = 0; i < numBricks; i++) {
//            try {
//                if (area.contains(this.getMousePosition())) {
//                    if (brick[i].intersects(area.getMousePosition().x, area.getMousePosition().y, 20, 1)) {
//                        return i;
//                    }
//                }
//            } catch (NullPointerException e) {
//            }
//        }
//        return WIZARD_WIDTH / (BRICK_WIDTH + BRICK_WIDTH / (WIZARD_WIDTH / 125)) * WIZARD_HEIGHT / (BRICK_HEIGHT - BRICK_HEIGHT / (WIZARD_HEIGHT / 28)) + 1;
//    }
//    private void readScores() {
//        try {
//            Scanner scFile;
//            scFile = new Scanner(new File("SCORES.txt"));
//            if (more) {
//                for (int i = 0; i < lastScore; i++) {
//                    scFile.nextLine();
//                }
//                numScores -= 15;
//            }
//            while (scFile.hasNext() && !(numScores == 30)) {
//                Scanner scLine;
//                scLine = new Scanner(scFile.nextLine()).useDelimiter(": ");
//                scoreBoard[numScores] = scLine.next();
//                scores[numScores++] = scLine.nextInt();
//                scLine.close();
//            }
//            if (scFile.hasNext()) {
//                more = true;
//            } else {
//                more = false;
//                sort();
//                scFile.close();
//                for (int i = 0; i < numScores; i++) {
//                    taScores.setText(taScores.getText() + (i + 1) + ". " + scoreBoard[i] + ": " + scores[i] + "\n");
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.err.println(e);
//        }
//    }
//
//    private void sort() {
//        for (int i = 0; i < numScores - 1; i++) {
//            for (int j = 0; j < numScores - 1 - i; j++) {
//                if (scores[j] < scores[j + 1]) {
//                    int temp = scores[j];
//                    scores[j] = scores[j + 1];
//                    scores[j + 1] = temp;
//                    String temp1 = scoreBoard[j];
//                    scoreBoard[j] = scoreBoard[j + 1];
//                    scoreBoard[j + 1] = temp1;
//                }
//            }
//        }
//    }
    }

}
